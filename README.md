# Contacts
> This application stores the user information along with 
> contact details like address and phone numbers.

## General Information
- This application typically can be used as light-weight microservice and other applications and use it as reference for contacts.

## Technologies Used
- Java 1.8
- Spring Boot 2.4.8
- Mysql for main app and h2 databases for test
- Jacoco plugin for coverage of tests

## Features
- Saves a user by posting the user field information
- Retrieves the user by using user id
- Retrieves the user by using set of user ids

## Requirements
For building and running the application you need:
- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally
There are several ways to run this Spring Boot application on your local machine.
One way is to execute the `main` method in the `uk.co.jpmorgan.contactmanager.ContactApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

### Details:
1. Clone the project using
   `git clone https://github.com/projectcheckout/contacts.git`
   Please type this as copy and paste can cause issues in git commands
2. Change the directory to checked out contacts folder.
3. In contacts folder, build the project using
   `mvn clean install`
4. Run using `mvn spring-boot:run`
5. You can run standalone jar as: <br>
   `$ mvn clean package spring-boot:repackage` <br>
   `$ java -jar target/contacts-0.0.1-SNAPSHOT.jar`
6. The web application is accessible at: [http://localhost:8080/contact-service](http://localhost:8080/contact-service)
7. Visit swagger docs at: [http://localhost:8080/contact-service/swagger-ui/](http://localhost:8080/contact-service/swagger-ui/), it has one POST (to create user) and two GET end-points (to get one user putting id in url /users/{id} or list of users putting comma separated user ids in query parameter /users?userIds=1,2)

## Constraints
- User: firstName, lastName, address, phoneNumbers are required
- Address: houseNumber or flatNumber is required. Street, postCode, city, countryCode are mandatory.
  Also, postCode and countryCode will be validated
- Phone: internationalDialCode and phoneNumber are mandatory and will be validated.