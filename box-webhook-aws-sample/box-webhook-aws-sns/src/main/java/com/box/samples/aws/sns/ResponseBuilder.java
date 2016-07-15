package com.box.samples.aws.sns;

import org.json.JSONObject;

import com.box.samples.aws.sns.validation.Validation;

/**
 * Builds responses.
 *
 * @author Stanislav Dvorscak
 *
 */
public class ResponseBuilder {

    /**
     * Only static members.
     */
    protected ResponseBuilder() {
    }

    /**
     * @return Simple 'ok' response.
     */
    public static JSONObject ok() {
        return new JSONObject().put("status", "ok");
    }

    /**
     * 'ok' response with a payload.
     *
     * @param response
     *            payload
     * @return 'ok' response
     */
    public static JSONObject ok(JSONObject response) {
        JSONObject result = new JSONObject();
        if (response != null) {
            response.keySet().forEach(key -> result.put(key, response.get(key)));
        }
        return result.put("status", "ok");
    }

    /**
     * Builds 'bad_request' response.
     *
     * @param validation
     *            context
     * @return 'bad_request' response
     */
    public static JSONObject badRequest(Validation validation) {
        return new JSONObject().put("status", "bad_request").put("violations", validation.toJSON());
    }

    /**
     * @return Builds 'not_found' response.
     */
    public static JSONObject notFound() {
        return new JSONObject().put("status", "not_found");
    }

    /**
     * @param message message of error
     * @return Builds 'error' response.
     */
    public static JSONObject error(String message) {
        return new JSONObject().put("status", "error").put("message", message);
    }

}
