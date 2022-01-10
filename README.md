# Expense Reimbursement System - Java / Spring

This application is an expense reimbursement system back-end, built via the Spring framework in Java.
It is a RESTful API that allows admins, managers, and employees to login, view and manage business related expenses that need to be reimbursed.

## User Stories

User stories describe the functionality of the application and are categorized by the roles defined below:

### Users

* All users can login using their username and password

### Admins

Admins can:
* View all users
* Edit user information
* Delete users
* Add new users

### Managers

Managers can:

* View all reimbursement requests
* Approve or deny a request

### Employees

Employees can:

* Submit a new reimbursement request
* Upload an image of a receipt for the expense
* View their own reimbursement requests

## Endpoints

* `/login` - for authentication
* `/users` - for admins to GET and POST to all users
* `/users/{id}` - for admins to GET, PUT, or DELETE a single user
* `/reimbursements` - for employees to GET and POST new expense reimbursement requests
* `/reimbursements/{id}` - for employees to GET, PUT, or DELETE an expense
* `/reimbursements/{id}/receipt` - for employees to POST an image of their receipt
* `/reimbursements/{id}/approve` - for managers to approve an expense via PUT requests
* `/reimbursements/{id}/reject` - for managers to reject an expense via DELETE requests

Any collection endpoint can be paginated via a query param `?limit=5&offset=10`.
Also, query params for reimbursements include: `?before=12-05-2018&after=07-04-2017` for filtering by date.

## Tech

* Spring Boot
  * Spring MVC
  * Spring AOP
  * Spring Data JPA
  * Spring Security
  * Spring HATEOAS
* AWS S3 - for storing receipt image uploads
* JSON web tokens
* Swagger API documentation

## Local Deployment

### Environment
For prod environment, the following environment variables are required:
* `ERS_db_url`
* `ERS_db_username`
* `ERS_db_password`

You must ensure these are set in the environment, or else pass them as command-line arguments to set as spring boot properties

To run from source code:

```bash
git clone https://github.com/acrenwelge/ERS-Java.git
cd ERS-Java
mvn package
cd target/ && java -jar myArtifact.jar
```

Alternatively, run with Docker:

```
# clone the repo & cd into it..
docker build .
docker run -p 80:8080 -dit <imagename>
```

Or grab the public image:

```
docker run -p 80:8080 -dit acrenwelge/ers-java:latest
```
