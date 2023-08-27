# FlightSearchAPIProject

FlightSearchAPIProject is a Spring Boot-based RESTful API designed for flight and airport management. The project provides a comprehensive set of operations for managing flights, airports, and retrieving data from third-party mock APIs.

## Features

- **CRUD Operations**: Seamless management of flights and airports with Create, Read, Update, and Delete operations.
- **Flight Search**: Search for flights using various criteria such as departure airport, arrival airport, and travel dates.
- **Scheduled Data Fetching**: A built-in scheduler that fetches flight data from mock APIs on a daily basis.
- **Authentication & Security**: Enhanced security with authentication mechanisms to protect your data.
- **Swagger Documentation**: Easily understand and interact with the API using Swagger.

## Prerequisites

- JDK 20 or higher
- Maven
- Microsoft SQL Server

## Setup & Installation

1. **Clone the Repository**:
git clone https://github.com/mtekinn/FlightSearchAPIProject.git
cd FlightSearchAPIProject

2. **Database Configuration**:
Ensure your SQL Server is up and running. Use the `flightSearchApi` database or modify the `application.properties` to match your database setup.

3. **Run the Application**:
mvn spring-boot:run


4. **Swagger UI**:
Access the API documentation by visiting `http://localhost:8080/swagger-ui/`.

## API Endpoints

- **Flights**:
- Retrieve all flights: `GET /api/flights`
- Get flight by ID: `GET /api/flights/{id}`
- Create a new flight: `POST /api/flights`
- Update a flight: `PUT /api/flights/{id}`
- Delete a flight: `DELETE /api/flights/{id}`
- Search for flights: `GET /api/flights/search`

- **Airports**:
- Retrieve all airports: `GET /api/airports`
- Get airport by ID: `GET /api/airports/{id}`
- Create a new airport: `POST /api/airports`
- Update an airport: `PUT /api/airports/{id}`
- Delete an airport: `DELETE /api/airports/{id}`
- Search airport by city: `GET /api/airports/search`
