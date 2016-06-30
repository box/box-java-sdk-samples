package com.box.sdk.webhookawssample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxWebHook;
import com.box.sdk.webhookawssample.helpers.AWSHelper;
import com.box.sdk.webhookawssample.helpers.BoxHelper;
import com.mashape.unirest.http.utils.ClientFactory;


/**
 * @author Vladimir Hrusovsky
 */
public class NotifyOnPreviewServlet extends HttpServlet {
    private HttpClient httpClient;

    @Override
    public void init() throws ServletException {
        //prepare HTTP client
        httpClient = ClientFactory.getHttpClient();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("NotifyOnPreview Path: " + request.getServletPath());
        response.setContentType("text/html");

        final String userId = BoxHelper.getBoxAppUserId(request);
        final String fileId = request.getParameter("fileId");
        final String email = request.getParameter("emailAddress");
        final String webhookTriggerID = registerWebhookTrigger(userId, fileId, email);

        createPreviewFileWebhook(userId, fileId, webhookTriggerID);

        //refresh page after done
        response.sendRedirect("docdetails?id=" + fileId);
    }

    /**
     * Registers Webhook Trigger for specified user, file and email.
     *
     * @param userId id of user
     * @param fileId id of file
     * @param email  email which will be mapped to specified userId and fileId
     * @return id of registered Webhook Trigger to be used for later invocations
     * @throws IOException will be thrown when error occurred while registering webhook trigger
     */
    private String registerWebhookTrigger(String userId, String fileId, String email) throws IOException {
        final HttpPost request = new HttpPost(AWSHelper.getAPIGatewayRegisterWebhookEmailTriggerURL());

        //set headers necessary for JSON data
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        //set request payload with webhook trigger entity
        request.setEntity(createWebhookTriggerEntity(userId, fileId, email));

        final HttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new IOException("Error while trying to register preview notification on AWS: "
                    + response.getStatusLine().getReasonPhrase());
        }

        final JSONObject responseObject = new JSONObject(EntityUtils.toString(response.getEntity()));
        return responseObject.getString("id");
    }

    /**
     * Creates entity passed to AWS for Webhook Trigger email registration.
     *
     * @param userId id of user to be registered with the email
     * @param fileId id of file to be registered with the email
     * @param email  email to be registered for specified file and user
     * @return payload entity for Webhook Trigger registration resource
     * @throws UnsupportedEncodingException
     */
    private HttpEntity createWebhookTriggerEntity(String userId, String fileId, String email)
            throws UnsupportedEncodingException {

        return new StringEntity(
                new JSONObject()
                        .put("userId", userId)
                        .put("fileId", fileId)
                        .put("email", email)
                        .toString()
        );
    }

    /**
     * Creates Box Webhook for 'Preview File' action, which triggers our AWS API Gateway resource.
     *
     * @param userId           id of user
     * @param webhookTriggerId id of trigger registered on AWS
     */
    private void createPreviewFileWebhook(String userId, String fileId, String webhookTriggerId)
            throws MalformedURLException {

        final BoxDeveloperEditionAPIConnection boxConnection = BoxHelper.userClient(userId);
        final BoxFile file = new BoxFile(boxConnection, fileId);

        file.addWebHook(createWebhookURL(webhookTriggerId), BoxWebHook.Trigger.FILE_PREVIEWED);
    }

    private URL createWebhookURL(String webhookTriggerId) throws MalformedURLException {
        return new URL(AWSHelper.getAPIGatewayInvokeWebhookEmailTriggerURL(webhookTriggerId));
    }
}
