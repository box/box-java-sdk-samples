package com.box.sdk.webhookawssample;

import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.webhookawssample.helpers.AWSHelper;
import com.box.sdk.webhookawssample.helpers.BoxHelper;
import com.mashape.unirest.http.utils.ClientFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

        final String userId = getCurrentUserId(request);
        final String fileId = request.getParameter("fileId");
        final String email = request.getParameter("emailAddress");
        final String webhookTriggerID = registerWebhookTrigger(userId, fileId, email);

        createWebhook(userId, webhookTriggerID);

        //refresh page after done
        response.sendRedirect("docdetails?id=" + fileId);
    }

    private String getCurrentUserId(HttpServletRequest request) {
        return BoxHelper.getBoxAppUserId(request);
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

        //set headers necessary
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        //set request payload
        request.setEntity(getWebhookTriggerEntity(userId, fileId, email));

        final HttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new IOException("Error while trying to register preview notification on AWS: " + response.getStatusLine().getReasonPhrase());
        }

        final JSONObject responseObject = new JSONObject(EntityUtils.toString(response.getEntity()));
        return responseObject.getString("id");
    }

    private HttpEntity getWebhookTriggerEntity(String userId, String fileId, String email) throws UnsupportedEncodingException {
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
     * @param userId id of user
     * @param webhookTriggerId id of trigger registered on AWS
     */
    private void createWebhook(String userId, String webhookTriggerId) {
        final String triggerURL = AWSHelper.getAPIGatewayInvokeWebhookEmailTriggerURL(webhookTriggerId);
        final BoxDeveloperEditionAPIConnection boxConnection = BoxHelper.userClient(userId);

        //TODO: Box Webhooks
    }
}
