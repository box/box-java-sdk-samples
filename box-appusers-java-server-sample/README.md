## Using Box with App Users Servlets Example

### This is a Sample Application 
### THIS SAMPLE SHOULD NOT BE USED AS A MODEL FOR PRODUCTION CODE.  

This is a Sample Java Servlet to demonstrate how to create and use App Users through the Box Java SDK.

We have not included any security features in the application and strictly recommend NOT to use any part of the application for production usage. Do not store any sensitive data in the application. 

We recommend creating a test developer enterprise, a test application within the enterprise, and integrating the sample Box Application's API key with this Sample Application. Once the testing is done, we recommend stopping the server, deleting the app from the Box developer console, ending all Box sessions tied to the sample application, and deleting the App Users created in the test enterprise from the admin console. 

The public and private key pairs used along with this sample application should not be reused with applications handling production data and should be discarded once testing completes

Usage of this sample application with production enterprises or production data is highly undesirable.

Box is not responsible for any security incidents that may arise due to the unsafe usage of this sample application.

### Prerequisites

This sample application requires that download and install the Box Java SDK
https://github.com/box/box-java-sdk


In order to run this example you will need to have Maven installed. On a Mac, you can install Maven with [brew](http://brew.sh/):

```sh
brew install maven
```

Check that your maven version is 3.0.x or above:
```sh
mvn -v
```

#####Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files
If you don't install this, you'll get an exception about key length. This is not a Box thing, this is a U.S. Government requirement concerning strong encryption. Please follow the instructions *exactly*.
> [Java 7 installer](http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html)

> [Java 8 installer](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)

###  Create a Application that supports App Users

You will need to create an application that supports app users and describe in in a configuration file like the following:
```sh
boxClientId=<YOUR_BOX_CLIENT_ID>
boxClientSecret=<YOUR_BOX_CLIENT_SECRET>
boxEnterpriseId=<YOUR_BOX_ENTERPRISE_ID>
boxPrivateKeyFile=<YOUR_JWT_PRIVATE_KEY_FILENAME>
boxPrivateKeyPassword=<YOUR_JWT_PRIVATE_KEY_PASSWORD>
boxPublicKeyId=<YOUR_JWT_PUBLIC_KEY_ID>
```
These steps are described heere
[https://docs.box.com/docs/configuring-box-platform](https://docs.box.com/docs/configuring-box-platform)
You must complete Steps 1 through 4 to enable this sample application

### Build and Run with Maven

In order to build and run the project you must execute:
```sh
mvn clean jetty:run
```

### Or Build and Run with Gradle
```sh
gradle jettyRunWar
```

Then, go to [http://localhost:8080/login](http://localhost:8080/login).
