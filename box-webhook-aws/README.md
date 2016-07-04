## Using Box Webhooks and Amazon Web Services to receive File Preview Notifications via Email Example

### Prerequisites

In order to package this example you will need to have Gradle installed.

On a Mac, you can install Gradle with [brew](http://brew.sh/):
```sh
brew install gradle
```

For other platforms see information on Gradle website [here](https://docs.gradle.org/current/userguide/installation.html) . 

Check that your Gradle is installed correctly with:
```sh
gradle -v
```

You will need to have Amazon Web Services account created to use this example. To create new account visit [here](https://console.aws.amazon.com/console/home).

###  This is a sample Application and Not Production Code

In order to create the simplest possible code to demonstrate how to use Box Webhooks together with AWS this sample COMPLETELY IGNORES App User Authentication.  
It asks for passwords but ignores them.

###  Create a Box Application that supports App Users

You will need to create an application that supports App Users. Steps are described [here](https://docs.box.com/docs/configuring-box-platform). 

During the process you have created private key. This key is needed by the Box SDK to authenticate yourself and your App Users. 
Copy your private key file into `config` directory of the sample root.

###  Setup AWS Environment

As for Amazon Web Services we are going to use the following services:

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
* **CloudFormation**
  * gives an easy way to create and manage a collection of related AWS resources
  * service used to prepare our Sample AWS Environment

#### 1. Connect your Box Application with the Sample Application

In order for the sample application to work with your Box Application you have created in previous steps, you will need to fill the following values.
Rename `gradle.properties.template` file in sample root to `gradle.properties` and replace placeholders with values for your application and enterprise account.

```
{boxEnterpriseId}         enterprise ID of your Box account
{boxClientId}             client ID of the Box Application
{boxClientSecret}         client secret of the Box Application
{boxPublicKeyId}          ID of your public key in the Box Application
{boxPrivateKeyPassword}   password for your private key
{boxPrivateKeyFile}       file name of your private key file you have copied into `config` directory
```
You are now ready to build your packages.

#### 2. Preparing Application Packages for AWS

In order to get packages we need to configure AWS environment use Gradle build task in root directory of the sample application:

```sh
gradle build
```

The build process will build WAR file for our Web Application to be deployed on Elastic Beanstalk, ZIP file for Lambda function and also CloudFormation configuration file.
Files can be found in `build/aws-setup` directory.

#### 3. Uploading packages to S3

 We are now ready to setup AWS environment. We will do so by using CloudFormation. To make our packages accessible to CloudFormation, we will upload them to S3 bucket.

 1. Go to [S3 Console](https://console.aws.amazon.com/s3) and Create New Bucket
 2. Choose name for your bucket (e.g. `box-webhook-aws-config`), region and click on Create
 3. In your bucket, use Upload action and pick package files of the sample project to upload into bucket - `box-webhook-aws-sns.zip` and `box-webhook-aws-webapp.war` from `build/aws-setup`
 4. Wait until the files are upload before proceeding to next step

#### 4. Creating CloudFormation Stack

 1. Go to [CloudFormation Console](https://console.aws.amazon.com/cloudformation) and Create New Stack
 2. Use Choose File in Choose a template section and select `build/aws-setup/cloudformation.json`
 3. On the Next page (Specify Details) fill out Stack name (e.g. `box-webhook-aws-stack`) and S3 Bucket name, where you uploaded the packages
 4. On the Next page (Options) just click on Next
 5. On the Review page you have to accept Capabilities at the bottom of the page:
    _'I acknowledge that this template might cause AWS CloudFormation to create IAM resources.'_
 6. Click on Create and give it some time to prepare the environment (you can watch progress in 'Events' tab)
 7. After everything is finished switch to 'Outputs' tab of the stack, where 'URL of the Web Application' can be found

### Example in Action
 
 After the setup above everything should be ready to test the example.

 1. Go to your 'URL of the Web Application', which will start your Sample Web Application
 2. Login as your AppUser (or create a new one)
 3. Open any of your files, which supports Preview (or upload a new one)
 4. Now you can fill out Email Address for notification and confirm it by OK
 5. If everything is set up properly you should get Subscription Confirmation Email from AWS Notifications, which you must Confirm (using the link in email)
 6. Preview the file to test the functionality