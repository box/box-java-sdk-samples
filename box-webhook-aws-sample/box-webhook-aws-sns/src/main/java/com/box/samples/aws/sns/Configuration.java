package com.box.samples.aws.sns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Configuration for {@link BoxAWSSNSLambda}.
 */
public class Configuration {

    /**
     * @see #getTableNameTopicByWebhook()
     */
    private final String tableNameTopicByWebhook;

    /**
     * @see #getBoxClientId()
     */
    private final String boxClientId;

    /**
     * @see #getBoxClientSecret()
     */
    private final String boxClientSecret;

    /**
     * @see #getBoxPublicKeyId()
     */
    private final String boxPublicKeyId;

    /**
     * @see #getBoxPrivateKeyPassword()
     */
    private final String boxPrivateKeyPassword;

    /**
     * @see #getBoxPrivateKey()
     */
    private String boxPrivateKey;

    /**
     * Constructor.
     *
     * @param properties
     *            loaded {@link Properties}
     */
    public Configuration(Properties properties) {
        // AWS DynamoDB
        this.tableNameTopicByWebhook = properties.getProperty("dynamodb.table.topic_by_webhook");

        // box
        this.boxClientId = properties.getProperty("box.clientId");
        this.boxClientSecret = properties.getProperty("box.clientSecret");
        this.boxPublicKeyId = properties.getProperty("box.publicKeyId");
        this.boxPrivateKeyPassword = properties.getProperty("box.privateKeyPassword");
    }

    /**
     * @return table name for Web Hook SNS ARN mapping.
     */
    public String getTableNameTopicByWebhook() {
        return this.tableNameTopicByWebhook;
    }

    /**
     * @return Box Application client ID.
     */
    public String getBoxClientId() {
        return this.boxClientId;
    }

    /**
     * @return appropriate client secret to {@link #getBoxClientId()}
     */
    public String getBoxClientSecret() {
        return this.boxClientSecret;
    }

    /**
     * @return RSA Public key "ID" for Box Application authentication.
     * @see #getBoxPrivateKey()
     */
    public String getBoxPublicKeyId() {
        return this.boxPublicKeyId;
    }

    /**
     * @return Encrypted private key corresponding to {@link #getBoxPublicKeyId()}.
     * @see #getBoxPrivateKeyPassword()
     */
    public String getBoxPrivateKey() {
        if (this.boxPrivateKey == null) {

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream("private_key.pem")
            ))) {
                final StringBuilder fileContent = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append('\n');
                }
                this.boxPrivateKey = fileContent.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this.boxPrivateKey;
    }

    /**
     * @return De-cryption password for a provided {@link #getBoxPrivateKey()}
     */
    public String getBoxPrivateKeyPassword() {
        return this.boxPrivateKeyPassword;
    }

}
