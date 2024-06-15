To run the application:
1) clone the project
2) go to org.example.banking-api-project-backend folder
4) run command "mvn spring-boot:run"

Application configuration is available in src/dev/application.yaml file. Port, base path and customers available in the repository are configurable.
Logging configuration is available in src/dev/logback.xml. Logs are stored in target/logs folder.
The application is available only as a REST API. GUI is not implemented.
Available endpoints: 
1) HTTP GET { base_path }/users/{ user_id } -> retrieves user information
2) HHTP POST  { base_path }/accounts -> saves new accounts
Request body structure: 
{
	"customerId": string,
	"initialCredit: number
}
}