# RoomMate API

A secure, scalable RESTful backend service for the RoomMate application. Built with Java 21 and Spring Boot, this API handles user authentication, authorization, and core backend functionality.

## Tech Stack
- **Java 21 LTS**
- **Spring Boot 3.5.x**
- **Spring Security** (with JWT Authentication)
- **Spring Data JPA**
- **PostgreSQL 16** (Production & Local Development)
- **H2 Database** (Testing)
- **Swagger / OpenAPI 3** (Documentation)
- **Docker Compose**

## Features
- **JWT-Based Authentication**: Secure stateless user sessions.
- **Role-Based Access Control**: Standard `USER` and `ADMIN` roles, along with an exclusive `SUPER_ADMIN` role for critical API management.
- **Household Management**: Associate roommates with shared households and define internal roles (ADMIN/MEMBER).
- **Shared Resource Scheduling**: Real-time scheduling for shared facilities like bathrooms and kitchens with daily/weekly/monthly recurrence support.
- **Chore Tracking**: Automated rotas and task management for household cleaning and maintenance.
- **Event Planning**: Collaborative household event management with multiple organizers.
- **Auditability & Traceability**: Full JPA Auditing for tracking entity lifecycle and Soft-Delete patterns for data safety.
- **Enhanced Security & Observability**: Fine-grained method-level security and structured logging for critical operations.
- **Interactive API Documentation**: Embedded Swagger UI to explore and test API endpoints.
- **Robust Test Suite**: Comprehensive unit and integration tests with high coverage for core modules.

## Getting Started

### Prerequisites
- JDK 21
- Maven
- Docker (for local PostgreSQL database)

### Local Development Setup

1. **Start the Database**
   Run the following command from the root directory to spin up the PostgreSQL database container:
   ```bash
   docker-compose up -d
   ```

2. **Run the Application**
   Start the Spring Boot application using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

### Default Credentials
Upon the first startup, the application automatically seeds a SuperAdmin account. You can use these credentials to log in:
- **Username:** `superadmin`
- **Password:** `superadmin`

## API Documentation
Once the application is running, you can access the Swagger UI documentation at:
**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

You can use the `/api/auth/login` endpoint with the credentials above to receive a Bearer token, which can be applied via the "Authorize" button in Swagger to interact with secured endpoints.

## Running Tests
Tests are configured to use an isolated in-memory H2 database. To execute the test suite:
```bash
./mvnw clean test
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
