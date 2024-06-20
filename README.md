## Project Overview
This project is a simple REST service built using Spring Boot that allows for basic account operations. 
The main functionality includes adding accounts, reading account details, and transferring money between accounts. 
The service ensures thread-safety during money transfers and includes a notification system for account holders during transfers.

## Features
## Add a New Account: Create a new account with a unique ID and an initial balance.
## Get Account Details: Retrieve details of an account using its ID.
## Transfer Money: Transfer money between accounts securely with thread-safety.
## Notification System: Notify account holders about the transfer details.

## Setup Instructions
## Prerequisites
    JDK 17
    Maven
    An IDE with Lombok plugin installed (e.g., IntelliJ IDEA, Eclipse)Installation
    
    ### Clone the Repository: git clone https://github.com/hariOmm12/CaseStudy-Challenge-TransferMoney.git
    ### Build the Project: mvn clean install
    ### Run the Application: mvn spring-boot:run
    
## Documentation
    ### API documentation :- http://localhost:18080/swagger-ui/index.html#/
    
## Usage
The application exposes a REST API for interacting with accounts. Below are some sample endpoints:

##  Add Account:
     ### Endpoint:
              POST http://localhost:18080/v1/accounts
              Content-Type: application/json
              Request Body:

                          {
                              "accountId": "111",
                              "balance": 100.00
                          }
##   Get Account:
      ###  Endpoint: 
                GET http://localhost:18080/v1/accounts/{accountId}
                
## Transfer Money:
     ###   Endpoint:
                POST http://localhost:18080/v1/accounts/transfer
                Content-Type: application/json
                Request Body:

                            {
                                "accountFromId": "12345",
                                "accountToId": "67890",
                                "amount": 500
                            }
## Improvements
Given more time, the following improvements and additional work would be important before turning this project into a production application:

Database Integration
Replace the in-memory account repository with a persistent database such as MySQL or PostgreSQL.
Implement proper transaction management to ensure data consistency.

Security
Implement authentication and authorization to secure the endpoints.
Use HTTPS to secure data in transit.

Scalability
Optimize the application for high concurrency and large data sets.
Use caching strategies to improve performance.

Testing
Add performance and load testing.
Increase test coverage with unit and integration tests.
