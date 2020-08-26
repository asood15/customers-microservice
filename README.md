## Customers-Microservice

After cloning the project, cd to the local path where the project is

- Building the project
```
mvn clean install
```

- Running the tests
```
mvn test
```

- Running the project <br>

Using Maven
```
mvn spring-boot:run
```
Using java jar
```
java -jar target/customers-microservice-0.0.1-SNAPSHOT.jar
```
Default port configured is 8080. 
URL for in memory database http://localhost:8080/h2-console/login.jsp
There is no password set and default username is sa

### Assumptions
1. A default schema.sql and data.sql are present in the src/main/resources. schema.sql creates the table and data.sql populates withfive default records. Hibernate ddl is set to false to allow schema.sql script to run
2. Customer table consists of the following fields: id (auto incremented, primary key), customerUuid(used to uniquely identify a customer), firstName, lastName, dateOfBirth, password (stored in hashed format), email (a unique identifier for each customer, needed for logging in) 
3. Integration tests have their own schema, data and drop files to keep testing seperate from the main code
4. Data Transfer Object is used to send customerDetails as a response of the API. It does not include password as this is sensitive information. For login and update endpoints other models are used to get request bodies to provide loose coupling
5. data.sql file in src/main/resources has the data that is loaded when the application starts up. All passwords are hashed and stored. To test the passwords use the firstName(lowercase)+321. Example when firstName is Ashima then password is ashima321
6. In production, spring security should be used to authenticate a user and set JWT tokens in the header for any future requets to secure the application. Unauthenticated requests and invalid JWT tokens in headers should bot be allowed to make any API calls

### Api Details
1. To authenticate a customer (POST): http://localhost:8080/api/customers/login
This is a Http Post request and requires email and password to be sent as request body in JSON. Returns customer details when successful. Throws CustomerAuthenticationException when password does not match and throws CustomerNotFoundException when no email matching the input is found. <br>
Example:<br>
curl --header "Content-Type: application/json" --request POST --data "{\"email\":\"ashima@gmail.com\",\"password\":\"ashima321\"}" http://localhost:8080/api/customers/login

2. To retrieve a customer based on uuid (GET): http://localhost:8080/api/customers/{customerUuid}
This is a Http Get request and rrquires customerUuid as a path parameter. Returns customer details when successful. Throws CustomerNotFoundException when no matching customerUuid to the path param is found. <br> 
Example: <br>
curl  http://localhost:8080/api/customers/2bf1e766-7f49-431b-8efa-713c27cdb107

3. To update a customer's details (PUT):  http://localhost:8080/api/customers/resetpassword/{customerUuid}
This is a Http Put request and requires customerUuid as a path parameter and a request body containing firstName, lastName and dateOfBirth as JSON. Email, id, uuid cannot be changed and there is a different call to change password. Returns customer details when successful. Throws CustomerNotFoundException when no matching customerUuid to the path param is found. <br>
Example: <br>
curl --header "Content-Type: application/json" --request PUT --data "{\"dateOfBirth\":\"1988-02-15\", \"firstName\":\"Jenny\", \"lastName\":\"Warner\"}" http://localhost:8080/api/customers/2bf1e766-7f49-431b-8efa-713c27cdb107

4. To get 3 customers sorted by date descending (GET) http://localhost:8080/api/customers/sortandlimit
This is a Http Get request and returns a list of 3 customers ordered by their date of birth in descending order. <br>
Example: <br>
curl  http://localhost:8080/api/customers/sortandlimit

5. To update a password (PUT) http://localhost:8080/api/customers/{customerUuid}/resetpassword
This is a Http Put request requires customerUuid as a path param and the old and new password to be changed as a request body. Returns customer details when successful. Throws CustomerNotFoundException when no matching customerUuid to the path param is found.  
Example: <br>
curl --header "Content-Type: application/json" --request PUT --data "{\"oldPassword\":\"ashima321\",\"newPassword\":\"ashima\"}" http://localhost:8080/api/customers/2bf1e766-7f49-431b-8efa-713c27cdb107/resetpassword

