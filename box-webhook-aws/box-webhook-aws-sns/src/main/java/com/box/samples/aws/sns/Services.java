package com.box.samples.aws.sns;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.box.samples.aws.sns.utils.LambdaUtils;
import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.EncryptionAlgorithm;
import com.box.sdk.IAccessTokenCache;
import com.box.sdk.InMemoryLRUAccessTokenCache;
import com.box.sdk.JWTEncryptionPreferences;

/**
 * Services provider.
 *
 * @author Stanislav Dvorscak
 *
 */
public class Services {

    /**
     * Logger for this class.
     */
    private final Logger logger = LoggerFactory.getLogger(Services.class);

    /**
     * @see #getConfiguration()
     */
    private final Supplier<Configuration> configurationProvider = LambdaUtils.lazy(this::configuration);

    /**
     * {@link IAccessTokenCache} for {@link #getBoxAPIConnection(String)}.
     */
    private final Supplier<IAccessTokenCache> boxAccessTokenCache = LambdaUtils.lazy(() -> new InMemoryLRUAccessTokenCache(1024));

    /**
     * @see #getAmazonDynamoDB()
     */
    private final Supplier<AmazonDynamoDB> dynamoDBProvider = LambdaUtils.lazy(this::amazonDynamoDB);

    /**
     * @see #getAmazonSNS()
     */
    private final Supplier<AmazonSNS> snsProvider = LambdaUtils.lazy(this::sns);

    /**
     * Constructor.
     */
    public Services() {
    }

    /**
     * @return resolves {@link Configuration}
     */
    public Configuration getConfiguration() {
        return this.configurationProvider.get();
    }

    /**
     * @return {@link Configuration} factory.
     */
    private Configuration configuration() {
        Properties properties = new Properties();
        InputStream propertiesStream = null;
        try {
            propertiesStream = getClass().getClassLoader().getResourceAsStream("box-samples-aws-sns.properties");
            properties.load(propertiesStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (propertiesStream != null) {
                try {
                    propertiesStream.close();
                } catch (IOException e) {
                    this.logger.error("Can not close properties stream: ", e);
                }
            }
        }

        return new Configuration(properties);
    }

    /**
     * {@link BoxAPIConnection} for a provided user.
     *
     * @param userId
     *            BOX user ID
     * @return resolved {@link BoxAPIConnection}
     */
    public BoxAPIConnection getBoxAPIConnection(String userId) {
        Configuration configuration = this.getConfiguration();

        JWTEncryptionPreferences preference = new JWTEncryptionPreferences();
        preference.setEncryptionAlgorithm(EncryptionAlgorithm.RSA_SHA_512);
        preference.setPublicKeyID(configuration.getBoxPublicKeyId());
        preference.setPrivateKey(configuration.getBoxPrivateKey());
        preference.setPrivateKeyPassword(configuration.getBoxPrivateKeyPassword());

        return BoxDeveloperEditionAPIConnection.getAppUserConnection(userId, configuration.getBoxClientId(),
                configuration.getBoxClientSecret(), preference, this.boxAccessTokenCache.get());
    }

    /**
     * @return factory for {@link #getAmazonSNS()}
     */
    private AmazonSNS sns() {
        AmazonSNSClient result = new AmazonSNSClient();
        result.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
        return result;
    }

    /**
     * @return AWS SNS
     */
    public AmazonSNS getAmazonSNS() {
        return this.snsProvider.get();
    }

    /**
     * @return factory for {@link #getAmazonDynamoDB()}
     */
    private AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDBClient result = new AmazonDynamoDBClient();
        result.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
        return result;
    }

    /**
     * @return AWS Dynamo DB.
     */
    public AmazonDynamoDB getAmazonDynamoDB() {
        return this.dynamoDBProvider.get();
    }

}
