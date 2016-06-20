package com.box.samples.aws.sns;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.box.samples.aws.sns.logging.LambdaLoggerSlf4j;
import com.box.samples.aws.sns.utils.FileUtils;
import com.box.samples.aws.sns.utils.StringUtils;
import com.box.samples.aws.sns.utils.ValidationUtils;
import com.box.samples.aws.sns.validation.Validation;
import com.box.samples.aws.sns.validation.ValidationException;
import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxAPIException;
import com.box.sdk.BoxEnterprise;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFile.Info;
import com.box.sdk.BoxUser;


/**
 * A AWS Lambda implementation.
 */
public class BoxAWSSNSLambda implements RequestStreamHandler {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(BoxAWSSNSLambda.class);

    /**
     * Service resolver.
     */
    private final Services services = new Services();

    /**
     * Constructor.
     */
    public BoxAWSSNSLambda() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        LambdaLoggerSlf4j.setContext(context);

        try {
            JSONObject response;

            try {
                JSONObject request = request(input);
                response = this.handle(request);
            } catch (ValidationException e) {
                response = ResponseBuilder.badRequest(e.getValidation());
            } catch (BoxAPIException e) {
                this.logger.error("Invocation failed " + e.getResponseCode() + ":" + e.getResponse(), e);
                response = ResponseBuilder.error(e.getMessage());
            } catch (Exception e) {
                this.logger.error("Invocation failed: ", e);
                response = ResponseBuilder.error(e.getMessage());
            }

            OutputStreamWriter writer = new OutputStreamWriter(output, StringUtils.UTF_8);
            response.write(writer);
            writer.flush();
        } finally {
            output.close();
            input.close();
        }
    }

    /**
     * Creates request {@link JSONObject} for a provided input.
     *
     * @param input
     *            for request
     * @return created {@link JSONObject} request
     */
    private JSONObject request(InputStream input) {
        JSONTokener tokener = new JSONTokener(new InputStreamReader(input, StringUtils.UTF_8));
        return new JSONObject(tokener);
    }

    /**
     * Handles a provided request.
     *
     * @param request
     *            for processing
     * @return response
     *
     * @see #handle(JSONObject, String)
     */
    private JSONObject handle(JSONObject request) {
        Validation validation = new Validation();
        ValidationUtils.notNull(validation, "request", request);
        validation.validate();

        String command = request.optString("command");
        ValidationUtils.notBlank(validation, "command", command);
        validation.validate();

        return this.handle(request, command);
    }

    /**
     * Handles a provided request.
     *
     * @param request
     *            for processing
     * @param command
     *            extracted command from the request
     * @return response
     */
    private JSONObject handle(JSONObject request, String command) {
        switch (command) {
            case "/ping":
                return ResponseBuilder.ok();
            case "/sns/e-mail":
                return ResponseBuilder.ok(this.email(request));
            case "/sns/sms":
                return ResponseBuilder.ok(this.sms(request));
            case "/box/preview":
                this.onBoxPreview(request);
                return ResponseBuilder.ok();
            default:
                return ResponseBuilder.notFound();
        }
    }

    /**
     * Builds SNS Web Hook for a provided E-mail.
     *
     * @param request
     *            for processing
     * @return response (web hook)
     */
    private JSONObject email(JSONObject request) {
        Validation validation = new Validation();
        String userId = request.optString("userId");
        ValidationUtils.notBlank(validation, "userId", userId);
        String email = request.optString("email");
        ValidationUtils.notBlank(validation, "email", email);
        validation.validate();

        String webHookId = UUID.randomUUID().toString();
        String topicARN = this.snsToEmail(webHookId, email);
        this.webHookToTopic(webHookId, userId, topicARN);
        return this.webHook(webHookId);
    }

    /**
     * SNS to an email.
     *
     * @param id
     *            web hook id
     * @param email
     *            SNS email
     * @return SNS ARN
     */
    private String snsToEmail(String id, String email) {
        AmazonSNS sns = this.services.getAmazonSNS();
        String topicARN = sns.createTopic("box_webhook_" + id).getTopicArn();
        sns.subscribe(topicARN, "email", email);
        return topicARN;
    }

    /**
     * Builds SNS Web Hook for a provided SMS.
     *
     * @param request
     *            for processing
     * @return response (web hook)
     */
    private JSONObject sms(JSONObject request) {
        Validation validation = new Validation();
        String userId = request.optString("userId");
        ValidationUtils.notBlank(validation, "userId", userId);
        String phone = request.optString("phone");
        ValidationUtils.notBlank(validation, "phone", phone);
        validation.validate();

        String webHookId = UUID.randomUUID().toString();
        String topicARN = this.snsForSMS(phone);
        this.webHookToTopic(webHookId, userId, topicARN);
        return this.webHook(webHookId);
    }

    /**
     * SNS to an SMS.
     *
     * @param phone
     *            number
     * @return SNS ARN
     */
    private String snsForSMS(String phone) {
        AmazonSNS sns = this.services.getAmazonSNS();
        String topicARN = sns.createTopic("webhook:sms:" + phone).getTopicArn();
        sns.subscribe(topicARN, "sms", phone);
        return topicARN;
    }

    /**
     * Creates relation entry between web hook and SNS ARN.
     *
     * @param webHookId
     *            id of web-hook
     * @param topicARN
     *            SNS ARN
     */
    private void webHookToTopic(String webHookId, String userId, String topicARN) {
        Configuration configuration = this.services.getConfiguration();
        AmazonDynamoDB dynamoDB = this.services.getAmazonDynamoDB();
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("webHookId", new AttributeValue().withS(webHookId));
        item.put("userId", new AttributeValue().withS(userId));
        item.put("topic", new AttributeValue().withS(topicARN));
        dynamoDB.putItem(configuration.getTableNameTopicByWebhook(), item);
    }

    /**
     * Wraps Web Hook ID to a JSON object.
     *
     * @param id
     *            of web-hook
     * @return {@link JSONObject} for web hook
     */
    private JSONObject webHook(String id) {
        return new JSONObject().put("id", id);
    }

    /**
     * Invoked on Box File preview.
     *
     * @param request
     *            for processing
     */
    private void onBoxPreview(JSONObject request) {
        Validation validation = new Validation();

        String webHookId = request.optString("webHookId");
        String fileId = request.optString("fileId");

        ValidationUtils.notBlank(validation, "webHookId", webHookId);
        ValidationUtils.notBlank(validation, "fileId", fileId);
        validation.validate();

        Map<String, AttributeValue> webHook = this.getWebHook(webHookId);
        String message = message(webHook.get("userId").getS(), fileId);
        this.publishSNS(webHook.get("topic").getS(), message);
    }

    /**
     * Publish message to a provided SNS topic.
     *
     * @param topicARN
     *            SNS ARN
     * @param message
     *            for publishing (payload)
     */
    private void publishSNS(String topicARN, String message) {
        AmazonSNS sns = this.services.getAmazonSNS();
        sns.publish(topicARN, message);
    }

    /**
     * Resolves web hook.
     *
     * @param webHookId
     *            identity of web hook
     * @return resolved web hook
     */
    private Map<String, AttributeValue> getWebHook(String webHookId) {
        Configuration configuration = this.services.getConfiguration();
        AmazonDynamoDB dynamoDB = this.services.getAmazonDynamoDB();
        Map<String, AttributeValue> key = Collections.singletonMap("webHookId", new AttributeValue().withS(webHookId));
        return dynamoDB.getItem(configuration.getTableNameTopicByWebhook(), key).getItem();
    }

    /**
     * Creates a message for a provided file ID.
     *
     * @param userId
     *            Box User ID
     * @param fileId
     *            identity to file
     * @return created message
     */
    private String message(String userId, String fileId) {
        BoxAPIConnection boxAPIConnection = this.services.getBoxAPIConnection(userId);
        BoxFile file = new BoxFile(boxAPIConnection, fileId);
        Info fileInfo = file.getInfo();
        BoxUser user = new BoxUser(boxAPIConnection, userId);
        BoxUser.Info userInfo = user.getInfo("name", "login", "enterprise");
        BoxEnterprise enterprise = userInfo.getEnterprise();
        String name = userInfo.getLogin();
        String message;
        message = new StringBuilder()
                                  .append("File: ")
                                  .append(fileInfo.getName())
                                  .append('\n')
                                  .append("Size: ")
                                  .append(FileUtils.humanReadableSize(fileInfo.getSize()))
                                  .append('\n')
                                  .append("Was previewed by User: ")
                                  .append(userInfo.getName())
                                  .append('\n')
                                  .append("User's Login Name: ")
                                  .append(userInfo.getLogin())
                                  .append('\n')
                                  .append("from Enterprise: ")
                                  .append(enterprise == null ? "none" : enterprise.getName())
                                  .append('\n')
                                  .toString();
        return message;
    }

}
