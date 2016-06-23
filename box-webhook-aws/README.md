## Using Box Webhooks and Amazon Web Services to receive File Preview Notifications via Email Example

### Prerequisites

In order to package this example you will need to have Maven installed.

On a Mac, you can install Maven with [brew](http://brew.sh/):
```sh
brew install maven
```

For other platforms see information on Maven website [here](https://maven.apache.org/install.html) . 

Check that your maven version is 3.0.x or above:
```sh
mvn -v
```

You will need to have Amazon Web Services account created to use this example. To create new account visit [here](https://console.aws.amazon.com/console/home).

###  This is a sample Application and Not Production Code

In order to create the simplest possible code to demonstrate how to create and use App Users from a Java servlet
this sample COMPLETELY IGNORES App User Authentication.  It asks for passwords but ignores them.


###  Create a Application that supports App Users

You will need to create an application that supports app users and describe it in a configuration file `box-webhook-aws-webapp/src/main/resources/dms-config.properties` like the following:
```sh
boxClientId=<YOUR_BOX_CLIENT_ID>
boxClientSecret=<YOUR_BOX_CLIENT_SECRET>
boxEnterpriseId=<YOUR_BOX_ENTERPRISE_ID>
boxPrivateKeyFile=<YOUR_JWT_PRIVATE_KEY_FILENAME>
boxPrivateKeyPassword=<YOUR_JWT_PRIVATE_KEY_PASSWORD>
boxPublicKeyId=<YOUR_JWT_PUBLIC_KEY_ID>
awsAPIGatewayURL=<YOUR_AWS_API_GATEWAY_URL>
```
These steps are described heere
[https://docs.box.com/docs/configuring-box-platform](https://docs.box.com/docs/configuring-box-platform)

`<YOUR_JWT_PRIVATE_KEY_FILENAME>` must be located in `box-webhook-aws-webapp/src/main/resources/` directory.

### Preparing Web Application Package

In order to build the web application you must execute in `box-webhook-aws-webapp` directory:
```sh
mvn clean package
```

Your WAR file will be located in `box-webhook-aws-webapp/target` directory.

### Or Build with Gradle
```sh
gradle war
```

And your WAR file will be located in `box-webhook-aws-webapp/build/lib` directory.

### Preparing AWS Environment
For Amazon Web Services we are going to use the following services:
* **DynamoDB**
  * non-relational database for storing mapping between user files and notification emails
  * more information can be found [here](https://aws.amazon.com/dynamodb/)
* **Simple Notification Service (SNS)**
  * service used to send notification emails
  * more information can be found [here](https://aws.amazon.com/sns/)
* **Lambda**
  * execution part of the application
  * manages webhook notification registration in storage as well as triggering email notification
  * more information can be found [here](https://aws.amazon.com/lambda/)
* **API Gateway**
  * web resources for registering webhook notifications and Box Webhooks triggers
  * more information can be found [here](https://aws.amazon.com/api-gateway/)
* **Elastic Beanstalk**
  * easy-to-use deployment of our example web application on Amazon
  * more information can be found [here](https://aws.amazon.com/elasticbeanstalk/)

#### DynamoDB

#### SNS

#### Lambda

#### API Gateway

#### Elastic Beanstalk

### Example in Action
After the setup above everything should be ready to test the example. 
 1. Go to your Elastic Beanstalk Application URL, which will start your Web Application
 2. Login as your AppUser
 3. Open any of your files, which supports Preview
 4. Now you can fill out Email Address for notification and confirm it by OK
 5. If everything is set up properly you should get Subscription Confirmation Email from AWS Notifications, which you must Confirm (using the link in email)
 6.  