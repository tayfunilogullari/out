## Credit Module Challenge

Backend application for bank loan process that employees can create, list and pay loans for their customers.

The service has those endpoints:

- Create Loan: Creates a new loan for a given customer, amount, interest rate and number of installments.
- List Loans: Lists loans for a given customer
- List Installment: Lists installments for a given loan
- Pay Loan: Pays installment for a given loan and amount

### Version Infos
* Java version 22
* Maven version 6
* Spring version 3

### DB
H2DB (admin: )

**Authentication:**
BasicAuthentication
InMemoryUser (
    admin: 123 
    user : 123)    

Server port : 8085


**User Controller**
Under `/users`

**Loan Controller**
Under `/loan`
