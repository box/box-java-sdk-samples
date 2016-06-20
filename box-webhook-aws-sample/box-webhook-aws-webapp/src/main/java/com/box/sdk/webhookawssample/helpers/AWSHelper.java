package com.box.sdk.webhookawssample.helpers;

import java.text.MessageFormat;

/**
 * AWS related helper methods.
 */
public final class AWSHelper {
    private static final String API_GATEWAY_URL = System.getProperty("awsAPIGatewayURL");
    private static final String API_GATEWAY_REGISTER_WEBHOOK_EMAIL_TRIGGER_URL = API_GATEWAY_URL + "/sns/email";
    private static final String API_GATEWAY_INVOKE_WEBHOOK_EMAIL_TRIGGER_URL = API_GATEWAY_URL + "/sns/box/preview/{0}";

    /**
     * Private constructor for utility class.
     */
    private AWSHelper() {
    }

    /**
     * API Gateway URL of resource to register new Webhook Email Trigger.
     *
     * @return url of the resource
     */
    public static String getAPIGatewayRegisterWebhookEmailTriggerURL() {
        return API_GATEWAY_REGISTER_WEBHOOK_EMAIL_TRIGGER_URL;
    }

    /**
     * API Gateway URL of resource to invoke Webhook Email Trigger.
     *
     * @param webhookTriggerID id of the trigger created in AWS
     * @return url of the resource
     */
    public static String getAPIGatewayInvokeWebhookEmailTriggerURL(String webhookTriggerID) {
        return MessageFormat.format(API_GATEWAY_INVOKE_WEBHOOK_EMAIL_TRIGGER_URL, webhookTriggerID);
    }
}
