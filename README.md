Requirements: Java 17

To run the application:
1) clone the project
2) go to org.example.banking-api-project-backend folder
4) run command "mvn spring-boot:run"

Application configuration is available in src/dev/application.yaml file. Port and customers available in the repository are configurable.
Logging configuration is available in src/dev/logback.xml. Logs are stored in target/logs folder by default.
The application is available as a REST API or GUI.

GUI is available on "http://localhost:{port}/banking-api/  (It's important to use the "/" at the end!)
REST API is available on "http://localhost:{port}/banking-api/api

Available endpoints: 
1) HTTP GET banking-api/api/customers/ -> get all customers
2) HTTP GET banking-api/api/customers/{user_id} -> retrieves customer accounts and transactions information
3) HHTP POST banking-api/accounts/add -> saves new accounts with following request body:
{
	"customerId": string,
	"initialCredit: number
}
