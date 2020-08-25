## Customers-Microservice

After cloning the project, cd to the local path where the project

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

### Assumptions
1. A default schema.sql and data.sql are present in the src/main/resources. schema.sql creates the table and data.sql populates withfive default records. Hibernate ddl is set to false to allow schema.sql script to run
2. Customer table consists of the following fields: id (auto incremented, primary key), customerUuid(used to uniquely identify a customer), firstName, lastName, dateOfBirth, password (stored in hashed format), email (a unique identifier for each customer, needed for logging in) 
3. Integration tests have their own schema,data and drop files to keep testing seperate from the main code
4. Data Transfer Object is used to send customerDetails as a response of the API.It does not include password as this is a sensitive information. For login and update endpoints other models are used to provide loose coupling
5. data.sql file in src/main/resources has the data that is loaded when the application starts up. All passwords are firstName(lowercase)+321. Example when firstName is Ashima then password is ashima321

### Api Details
1. To authenticate a customer: http://localhost:8080/api/customers/login
Requires email and password to be sent as request parameters as JSON. Returns customer details when successful. Throws CustomerAuthenticationException when password does not match and throws CustomerNotFoundException when no email matching the input is found. <br>
Example:<br>
curl --header "Content-Type: application/json" --request GET --data "{\"email\":\"ashima@gmail.com\",\"password\":\"ashima321\"}" http://localhost:8080/api/customers/login

2. To retrieve a customer based on uuid http://localhost:8080/api/customers/{customerUuid}
Requires customerUuid as a path param. Returns customer details when successful. Throws CustomerNotFoundException when no matching customerUuid to the path param is found. <br> 
Example: <br>
curl  http://localhost:8080/customers/2bf1e766-7f49-431b-8efa-713c27cdb107

3. To update a customer's details http://localhost:8080/api/customers/resetpassword/{customerUuid}
Requires customerUuid as a path param and a request body containing firstName, lastName and dateOfBirth as JSON. Returns customer details when successful. Throws CustomerNotFoundException when no matching customerUuid to the path param is found. <br>
Example: <br>
curl --header "Content-Type: application/json" --request PUT --data "{\"dateOfBirth\":\"1988-02-15\", \"firstName\":\"Jenny\", \"lastName\":\"Warner\"}" http://localhost:8080/api/customers/2bf1e766-7f49-431b-8efa-713c27cdb107

4. To get 3 customers sorted by date descending http://localhost:8080/api/customers/sortandlimit
Returns a list of 3 customers ordered by their date of birth in descending order. <br>
Example: <br>
curl  http://localhost:8080/api/customers/sortandlimit

5. To update a password http://localhost:8080/api/customers/resetpassword/{customerUuid}
Requires customerUuid as a path param and the password to be changed as a request body. Returns customer details when successful. Throws CustomerNotFoundException when no matching customerUuid to the path param is found.  
Example: <br>
curl --header "Content-Type: application/json" --request PATCH --data "ashima" http://localhost:8080/api/customers/resetpassword/2bf1e766-7f49-431b-8efa-713c27cdb107

