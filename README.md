Incident Management System

This is a Spring Boot application for managing incidents. It provides a RESTful API for creating, retrieving, updating, and deleting incidents. The system can be integrated with a React frontend for a complete full-stack application.

Table of Contents

Features

Technologies Used

Prerequisites

Setup and Installation

Running the Application

API Endpoints

Testing

Stress Testing

Caching

Front-End Integration

License

Features

Incident Management: CRUD operations for incidents (Create, Read, Update, Delete).

RESTful API: Exposed endpoints for interacting with the incident data.

Caching: Implemented caching for better performance.

Stress Testing: Included stress tests to ensure the system handles high load.

Front-End Integration: Integrated with a React front-end.

Technologies Used

Spring Boot: Backend framework.

Java 17: Programming language.

H2 Database: In-memory database for development and testing.

Caffeine: In-memory cache.

JUnit: Unit and integration testing.

Maven: Build automation tool.

React: Front-end framework (if integrated).

Prerequisites

Java 17: Ensure that JDK 17 is installed.

Maven: Make sure Maven is installed.

Node.js and npm: Required for the React front-end.

Setup and Installation

Step 1: Clone the Repository

git clone https://github.com/your-username/incident-management.git

cd incident-management

Step 2: Configure the Application

The application is configured to use an in-memory database and Caffeine for caching by default. If needed, you can modify the configuration in the application.properties file.

Step 3: Build the Application

mvn clean install

Running the Application

Step 1: Start the Backend

Run the following command to start the Spring Boot application:

mvn spring-boot:run

The application will be accessible at http://localhost:8080.

Step 2: Start the Frontend (Optional)
If you have the React frontend set up:

cd incidentreact

npm start

The React app will be accessible at http://localhost:3000.

API Endpoints

Base URL

http://localhost:8080/api/incidents

Endpoints

GET /api/incidents: Retrieve all incidents.

GET /api/incidents/{id}: Retrieve an incident by ID.

POST /api/incidents: Create a new incident.

Request Body:

json

{
  "title": "string",
  
  "description": "string",
  
  "status": "OPEN | IN_PROGRESS | CLOSED"
  
}

PUT /api/incidents/{id}: Update an existing incident.

Request Body: Same as above.

DELETE /api/incidents/{id}: Delete an incident by ID.

Testing

Unit and Integration Tests

The project includes comprehensive unit and integration tests using JUnit.

Run the tests with:

mvn test

Stress Testing

Stress tests are included to simulate high load scenarios. These tests can be run alongside the other tests:

mvn test

Look for the IncidentServiceStressTest class for stress-related tests.

Caching

The application uses Caffeine as the caching provider. The following methods are cached:

getIncidentById: Caches individual incidents by ID.

getAllIncidents: Caches the list of all incidents.

Cache configuration can be found in application.yml:

yaml

spring:

  cache:
  
    caffeine:
    
      spec: maximumSize=500,expireAfterWrite=10m

npm run build

Copy the contents of the build directory to src/main/resources/static in the Spring Boot project.

License

This project is licensed under the MIT License. See the LICENSE file for details.

