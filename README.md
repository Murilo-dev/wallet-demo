# Wallet appplication overview
This is a wallet demo application (backend only) done in around 6 hours for showing purposes.

## Running the application
Just run `gradle bootRun` or `WalletApplication.java` inside IntelliJ with Java 21.
For simplicity and configuration's sake, it's setup with an H2 memory temporary database.

## Design choices and compromises
The Spring RESTful API was selected to fulfill the REST/HTTP requirements. It remains a top choice due to its robustness, extensive resources, flexibility, and strong community support.

While logging operations for historical and traceability purposes are currently handled within the same operation and database,
an ideal solution would involve a queue system. Publishing data to a queue would allow another service to consume it asynchronously, improving performance given the high volume of operations and traffic.

Due to time constraints, logging for debugging and errors (potentially using Log4j), enhanced exception handling, unit testing, basic validations (assuming frontend validation), code style checks (e.g., Checkstyle), and the use of ENUMs were not implemented. These elements are essential for code quality, maintainability, and resilience."

## Testing the application
* There's a `Wallet.postman_collection.json` file on the project's root if you wish to import it on Postman. Or you can test on your terminal by running these:
    
    - This will create users #1 and #2 (Bob and David)
    > curl -X POST -H "Content-Type: application/json" -d "{ \"name\": \"Bob\" }" http://localhost:8080/api/v1/users

    > curl -X POST -H "Content-Type: application/json" -d "{ \"name\": \"David\" }" http://localhost:8080/api/v1/users

    - This will create wallets for user #1 and #2
    > curl -X POST http://localhost:8080/api/v1/wallets/1
    
    > curl -X POST http://localhost:8080/api/v1/wallets/2

    - This will deposit $150 on user #1 wallet  
    > curl -X POST -H "Content-Type: application/json" -d "{ \"balance\": \"150\" }" http://localhost:8080/api/v1/wallets/deposit/1

    - This will withdraw $10 from user #1 wallet 
    > curl -X POST http://localhost:8080/api/v1/wallets/withdraw/1?amount=10

    - This will transfer $30 from user #1 wallet to user #2 wallet 
    > curl -X POST "http://localhost:8080/api/v1/wallets/transfer?fromUserId=1&toUserId=2&amount=30"

    - This checks user #1 balance
    > curl localhost:8080/api/v1/wallets/balance/1

    - This checks user #2 balance
    >   curl localhost:8080/api/v1/wallets/balance/2

    - This checks user #1 balance history from a 'start date' to a 'end date', in this case, all 2024    
    > curl "localhost:8080/api/v1/wallets/history/1?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59"
  
  
