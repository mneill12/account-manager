#  Account Manager 

### Account Manager Environment Requirements 
- Maven 
- Java 11

### Build/Run Account Manager 
1. Run `$ mvn clean install` from account-manager root directory
2. Run jar created in target directly from account-manager root directory `$ java -jar target\account-manager-0.0.1-SNAPSHOT.jar`. This will start the app on `http://localhost:8080`

**NOTE** - To ensure the correct database file with preloaded accounts is used the jar must be run from the root directory e.g. `C:\Users\user\projects\account-manager`

### Account Manager API
Account manager exposes endpoints to manage `Customer Accounts` and `Transfers` see below for details.

#### Customer Accounts 
- `get` `/api/v1/customerAccount` - get all customer accounts
- `post` `/api/v1/customerAccount` - create a new customer account
- `get` `/api/v1/customerAccount/{accountNumber}` - get a customer account by account number
- `get` `/api/v1/customerAccount/{accountNumber}/balance` - get a customer account's balance by account number

#### Transfers
- `post` `/api/v1/transfer` - create a transfer to transfer an amount from a source account to an acquiring account

### Full API Reference - Swagger
For full API/Model definition please see the Swagger endpoint here: `http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config` 
