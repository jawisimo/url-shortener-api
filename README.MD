# URL Shortener. Rest API.

This project provides a simple API for URL shortening, user authentication, and statistics tracking.

## Table of Contents

1. [Project Description](#project-description)
2. [Technologies Used](#technologies-used)
3. [Setup Instructions](#setup-instructions)

---

## Project Description

The URL Shortener Project provides the following features:

- User Registration & Authentication
- URL Shortening
- URL Statistics

---

## Technologies Used

- Java 21
- Spring Boot
- Spring Data
- Spring MVC
- Spring Security
- JWT
- Flyway
- Lombok
- Swagger OpenAPI
- JUnit
- Mockito
- Testcontainers
- JaCoCo
- PostgreSQL
- Docker

---

## Setup Instructions

### Development Environment (dev profile, port: 8080)

- **Start the development database stack**:  
  ```bash
  docker-compose -p url_shortener_api_devstack -f ./docker-compose-dev.yml up --build

- **Run the application**:  
  ```bash
  mvn clean spring-boot:run -Pdev

### Running tests

- **Start tests (with test database)**:  
  ```bash
  mvn clean test 

### Production Environment (prod profile, port: 9999)

- **Build the application**:  
  ```bash
  mvn clean package -Pprod

- **Start the full application stack**:  
  ```bash
  docker-compose -p url_shortener_api_prodstack -f ./docker-compose-prod.yml up --build

## Swagger UI

You can access the REST API documentation via Swagger once the full application stack is running
with the corresponding profile:

- 🛠️ [Dev Environment](http://localhost:8080/swagger-ui/index.html)
- 🚀 [Prod Environment](http://localhost:9999/swagger-ui/index.html)

## Author

Developed by [Ihor Kolybaba](https://github.com/jawisimo)
