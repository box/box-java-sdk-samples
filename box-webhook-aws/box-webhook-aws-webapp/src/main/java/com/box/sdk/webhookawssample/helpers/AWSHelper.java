package com.box.sdk.webhookawssample.helpers;

import java.text.MessageFormat;

/**
 * @author Vladimir Hrusovsky
 */
public class AWSHelper {
    private static final String API_GATEWAY_URL = ConfigHelper.properties().getProperty("awsAPIGatewayURL");
    private static final String API_GATEWAY_REGISTER_WEBHOOK_EMAIL_TRIGGER_URL = API_GATEWAY_URL + "/sns/email";
    private static final String API_GATEWAY_INVOKE_WEBHOOK_EMAIL_TRIGGER_URL = API_GATEWAY_URL + "/sns/box/preview/{0}";

    public static String getAPIGatewayRegisterWebhookEmailTriggerURL() {
        return API_GATEWAY_REGISTER_WEBHOOK_EMAIL_TRIGGER_URL;
    }

    public static String getAPIGatewayInvokeWebhookEmailTriggerURL(String webhookTriggerID) {
        return MessageFormat.format(API_GATEWAY_INVOKE_WEBHOOK_EMAIL_TRIGGER_URL, webhookTriggerID);
    }
}
