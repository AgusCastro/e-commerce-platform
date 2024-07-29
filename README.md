# e-commerce Platform

## Description

This is a REST API for a e-commerce platform that allows users to CRUD products, orders and order items.
Is built with Java 21, Spring Boot, Spring Data JPA, PostgreSQL, OpenAPI, Gradle, Docker and Docker Compose.

## Requirements

* Java 21 - It is recommended to use [skdman](https://sdkman.io/) to manage the Java version.
* Docker 27.0.3 - [Install Docker](https://docs.docker.com/get-docker/)
* Docker Compose v2.28.1-desktop.1 - [Install Docker Compose](https://docs.docker.com/compose/install/)

### Building the Backend

To build the backend, run the following command:

```bash
./gradlew build
```
### Running the Backend locally

To run the backend make sure your variables for the database connection in the `application.properties` file are ok.
    
* `spring.datasource.url` > The connection URL of the database
* `spring.datasource.username` > The username of the database
* `spring.datasource.password` > The password of the database

**Current version creates the database schema on startup. (To be fixed next version)**


Then run the following command:

```bash
./gradlew bootRun
```

### Running Backend unit test

To run the backend unit tests, run the following command:

```bash
./gradlew test
```

## Running the Backend with Docker

To run the backend with Docker, run the following command:

```bash
docker-compose up
```

If the image is not found, this script will build and start the PostgreSQL database and the backend application.

## API Documentation

Running the app locally the API documentation is available at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Future Improvements (Sorting by priority)

* Create integration tests using [Testcontainers](https://testcontainers.com/).
* Add more unit tests for the mappers.

## Built With

* [Java](https://www.java.com/) - The programming language used
* [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
* [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - The web framework used
* [PostgreSQL](https://www.postgresql.org/) - The database used
* [OpenAPI](https://swagger.io/specification/) - The API documentation used
* [Swagger](https://swagger.io/) - The API documentation tool used
* [Gradle](https://gradle.org/) - The build tool used
* [Docker](https://www.docker.com/) - The containerization tool used
* [Docker Compose](https://docs.docker.com/compose/) - The containerization tool used
* [JUnit](https://junit.org/junit5/) - The testing framework used
* [Mockito](https://site.mockito.org/) - The mocking framework used

## Author
* **Agustin Castro**