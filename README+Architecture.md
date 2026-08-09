# TI Knowledge API

`ti-knowledge-api` is the backend microservice responsible for managing technical interview questions and related knowledge within the **Training Internal (TI) Knowledge Platform**.

The service is built with **Java 21, Spring Boot, Spring Data JPA, PostgreSQL, Spring Security OAuth 2.0, Docker, and Gradle**.

---

## 1. TI Knowledge Platform

### Goal

The **Training Internal (TI) Knowledge Platform** is a production-like cloud-native microservices application designed to demonstrate modern Java and Spring development practices commonly used in enterprise environments.

The platform is an **Internal Knowledge Management System** for storing, organizing, importing, and exporting:

* Technical interview questions
* Answers
* Learning resources
* Code examples

The primary purpose of the project is educational. It provides hands-on experience with:

* Modern Java development
* Spring Boot
* REST APIs
* React frontend development
* Microservices architecture
* Cloud-native architecture
* OAuth 2.0 and security
* Event-driven communication
* Containerization
* Observability
* CI/CD
* AWS deployment
* Enterprise software development best practices

### Microservices

| Service                | Responsibility                                            |
| ---------------------- | --------------------------------------------------------- |
| `ti-ui`                | React frontend, dashboard, and user interactions          |
| `ti-gateway-api`       | API Gateway, BFF, OAuth 2.0 Client, routing, and security |
| **`ti-knowledge-api`** | Manage questions, answers, resources, and code examples   |
| `ti-orchestrator-api`  | Manage long-running import/export workflows               |
| `ti-import-api`        | Process Excel/CSV imports                                 |
| `ti-export-api`        | Generate export files                                     |
| `ti-audit-api`         | Store business audit records                              |
| `ti-notification-api`  | Process user notifications                                |

---

## 2. `ti-knowledge-api` Responsibilities

The `ti-knowledge-api` is the main business API for knowledge-related data.

Currently, the service provides functionality for:

* Managing technical interview questions
* Retrieving questions
* Accessing question data from PostgreSQL
* Exposing the application version
* Authentication and authorization
* Auditing entity changes
* API documentation
* Health and application metrics
* Distributed tracing and observability

The service is designed as a Spring Boot REST API and is intended to be accessed through `ti-gateway-api` in the complete platform architecture.

---

## 3. Technology Stack

| Category            | Technology                  |
| ------------------- | --------------------------- |
| Language            | Java 21                     |
| Framework           | Spring Boot 4.0.7           |
| Build               | Gradle                      |
| API                 | REST                        |
| Web                 | Spring MVC                  |
| Database            | PostgreSQL                  |
| Persistence         | Spring Data JPA / Hibernate |
| Security            | Spring Security             |
| Authentication      | OAuth 2.0 / JWT             |
| Identity Provider   | Okta                        |
| API Documentation   | Springdoc OpenAPI           |
| Containerization    | Docker                      |
| Testing             | JUnit 5                     |
| Integration Testing | Testcontainers              |
| Metrics             | Micrometer                  |
| Metrics Registry    | Prometheus / OTLP           |
| Distributed Tracing | Micrometer Tracing / Brave  |
| Code Generation     | Lombok                      |

---

## 4. Project Structure

```text
ti-knowledge-api/
├── build/                           # Gradle build output
│
├── doc/
│   └── Troubleshooting.md           # Troubleshooting guide
│
├── gradle/                          # Gradle Wrapper files
│
├── http/                            # HTTP client requests
│   ├── project/
│   │   └── project.http             # Project API requests
│   ├── question/
│   │   └── question.http            # Question API requests
│   └── version.http                 # Version API request
│
├── k8s/
│   ├── build-docker-image.sh        # Build Docker image
│   ├── build-target.sh              # Build application
│   └── build-target-and-image.sh    # Build application and Docker image
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/wk/ti/
│   │   │       ├── config/
│   │   │       │ ...
│   │   │       ├── controller/
│   │   │       │ ...
│   │   │       ├── exception/
│   │   │       │ ...
│   │   │       │
│   │   │       ├── question/
│   │   │       │ ...
│   │   │       ├── user/
│   │   │       │ ...
│   │   │       └── Application.java
│   │   │
│   │   └── resources/
│   │       ├── application.yaml
│   │
│   └── test/
│       └── java/
│           └── com/wk/ti/
│
├── .gitattributes
├── .gitignore
├── build.gradle
├── Dockerfile
├── gradlew
├── gradlew.bat
├── README.md
└── settings.gradle
```

---

## 5. Application Architecture

At the service level, the application follows a layered architecture:

```text
                    ┌─────────────────────┐
                    │      REST API       │
                    │    Controllers      │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │      Services       │
                    │   Business Logic    │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │    Repositories     │
                    │    Spring Data JPA  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     PostgreSQL      │
                    │    Knowledge DB     │
                    └─────────────────────┘
```

Security is implemented at the API level using Spring Security as an OAuth 2.0 Resource Server.

```text
Client
  │
  │ JWT
  ▼
┌─────────────────────────┐
│   ti-knowledge-api      │
│                         │
│ Spring Security         │
│ OAuth2 Resource Server  │
│          │              │
│          ▼              │
│ JWT validation          │
│          │              │
│          ▼              │
│ REST Controllers        │
└─────────────────────────┘
```

In the complete TI platform, the expected request flow is:

```text
┌─────────┐
│  ti-ui  │
└────┬────┘
     │
     ▼
┌─────────────────┐
│ ti-gateway-api  │
│       BFF       │
└────────┬────────┘
         │ JWT
         ▼
┌─────────────────────┐
│ ti-knowledge-api    │
└──────────┬──────────┘
           │
           ▼
     ┌───────────┐
     │ PostgreSQL│
     └───────────┘
```

---

## 6. Prerequisites

The following tools are required for local development:

* Java 21
* Docker
* Docker Compose, if using the local platform environment
* Git

The project uses the **Gradle Wrapper**, so a locally installed Gradle version is not required.

Verify Java:

```bash
java -version
```

Verify Docker:

```bash
docker --version
```

---

## 7. Build the Application

The Gradle Wrapper is included in the repository.

On Linux/macOS:

```bash
./gradlew clean build
```

On Windows:

```powershell
.\gradlew.bat clean build
```

To skip tests:

```bash
./gradlew clean build -x test
```

The generated Spring Boot JAR will be available under:

```text
build/libs/
```

For example:

```text
build/libs/ti-knowledge-api-0.0.1-SNAPSHOT.jar
```

---

## 8. Run the Application Locally

The application can be started using Gradle:

```bash
./gradlew bootRun
```

The default HTTP port is:

```text
8081
```

Therefore:

```text
http://localhost:8081
```

The application context path is `/`.

---

## 9. Configuration

The main configuration file is:

```text
src/main/resources/application.yaml
```

Local configuration is available in:

```text
src/main/resources/application-local.yaml
```

Important configuration properties are provided through environment variables.

### Server

```yaml
server:
  port: ${SERVER_PORT:8081}
```

The default port is `8081`.

### Database

```yaml
spring:
  datasource:
    url: ${KNOWLEDGE_DB_URL}
    username: ${KNOWLEDGE_USER}
    password: ${KNOWLEDGE_PASSWORD}
```

The following environment variables must be configured when running against PostgreSQL:

```text
KNOWLEDGE_DB_URL
KNOWLEDGE_USER
KNOWLEDGE_PASSWORD
```

### OAuth 2.0 / Okta

The application is configured as an OAuth 2.0 Resource Server:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://${OKTA_OAUTH2_ISSUER}/
```

The following environment variable is required:

```text
OKTA_OAUTH2_ISSUER
```

---

## 10. API

### Version

The version endpoint is available at:

```http
GET /rest/v1/version
```

For a local application:

```text
http://localhost:8081/rest/v1/version
```

Example response:

```text
ti-knowledge-api : 0.0.1-SNAPSHOT
```

An HTTP request example is available in:

```text
http/version.http
```

### Questions

Question-related HTTP requests are available under:

```text
http/question/question.http
```

Project-related requests are available under:

```text
http/project/project.http
```

The `.http` files can be executed directly from IntelliJ IDEA or another HTTP client supporting the HTTP Client format.

---

## 11. OpenAPI / Swagger

The application exposes OpenAPI documentation through Springdoc.

OpenAPI JSON:

```text
http://localhost:8081/v3/api-docs
```

Swagger UI is available at:

```text
http://localhost:8081/swagger-ui.html
```

or:

```text
http://localhost:8081/swagger-ui/index.html
```

The allowed Swagger origins can be configured using:

```text
SWAGGER_ALLOWED_ORIGINS
```

The default value is:

```text
http://localhost:8080
```

---

## 12. Health and Actuator

Spring Boot Actuator is enabled for application monitoring.

Actuator endpoints are exposed under:

```text
/actuator
```

For example:

```text
http://localhost:8081/actuator/health
```

The health endpoint does not expose detailed health information by default.

```yaml
management:
  endpoint:
    health:
      show-details: never
```

---

## 13. Observability

The application uses Micrometer for metrics and distributed tracing.

### Metrics

Supported registries include:

* Prometheus
* OTLP

Prometheus metrics can be accessed through the Actuator endpoint:

```text
/actuator/prometheus
```

### Distributed Tracing

The application uses:

* Micrometer Tracing
* Brave

This allows trace information to be propagated between services in the TI microservices architecture.

---

## 14. Database

The service uses PostgreSQL as its persistence layer.

Spring Data JPA and Hibernate are used for persistence.

The application uses the following PostgreSQL schema:

```text
knowledge
```

Hibernate schema generation is disabled:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none
```

This means that database structures are expected to be created and maintained separately, for example through database migration scripts.

---

## 15. Testing

The project uses:

* JUnit 5
* Spring Boot Test
* Testcontainers
* PostgreSQL Testcontainer

Run all tests:

```bash
./gradlew test
```

Run a clean build with tests:

```bash
./gradlew clean build
```

Test sources are located under:

```text
src/test/java/
```

The main test classes include:

```text
ApplicationTests.java
TestApplication.java
TestcontainersConfiguration.java
```

---

## 16. Docker

The application can be packaged as a Docker image.

First build the application:

```bash
./gradlew clean build
```

Then build the Docker image:

```bash
docker build -t ti-knowledge-api:latest .
```

The Dockerfile uses the generated JAR from:

```text
build/libs/
```

Run the container:

```bash
docker run --rm \
  -p 8081:8081 \
  ti-knowledge-api:latest
```

The API will then be available at:

```text
http://localhost:8081
```

---

## 17. Docker Image

The current Docker image is based on Java 21:

```dockerfile
FROM alpine/java:21-jre
```

The application JAR is copied into the container as:

```text
/app/app.jar
```

The container exposes:

```text
8081
```

The application is started with:

```text
java -jar app.jar
```

---

## 18. Build Scripts

Several helper scripts are available under:

```text
k8s/
```

### Build Target

```text
k8s/build-target.sh
```

This script is responsible for building the application artifact.

### Build Docker Image

```text
k8s/build-docker-image.sh
```

This script builds the Docker image.

### Build Target and Docker Image

```text
k8s/build-target-and-image.sh
```

This script combines the application build and Docker image build into a single operation.

---

## 19. Troubleshooting

Common development and runtime problems are documented in:

```text
doc/Troubleshooting.md
```

Before investigating an issue, check:

1. Java version
2. Environment variables
3. PostgreSQL availability
4. OAuth 2.0 / Okta configuration
5. Application logs
6. Docker image and container status
7. Actuator health endpoint

---

## 20. Useful Gradle Commands

### Clean

```bash
./gradlew clean
```

### Compile

```bash
./gradlew compileJava
```

### Run tests

```bash
./gradlew test
```

### Build

```bash
./gradlew build
```

### Build without tests

```bash
./gradlew build -x test
```

### Run application

```bash
./gradlew bootRun
```

### Show dependencies

```bash
./gradlew dependencies
```

### Show available tasks

```bash
./gradlew tasks
```

---

## 21. Development Workflow

A typical development workflow is:

```text
1. Checkout repository
        │
        ▼
2. Configure environment variables
        │
        ▼
3. Start PostgreSQL
        │
        ▼
4. Run ./gradlew clean build
        │
        ▼
5. Run ./gradlew bootRun
        │
        ▼
6. Test APIs using http/*.http
        │
        ▼
7. Run automated tests
        │
        ▼
8. Build Docker image
        │
        ▼
9. Deploy to Kubernetes / AWS
```

---

## 22. Git Ignore

The following directories are generated locally and should not normally be committed:

```text
.gradle/
build/
.idea/
```

The Gradle Wrapper files are committed:

```text
gradlew
gradlew.bat
gradle/
```

This allows developers and CI/CD environments to use the same Gradle version without installing Gradle globally.

---

## 23. Related TI Platform Services

The `ti-knowledge-api` is one component of the larger TI Knowledge Platform.

```text
                    ┌───────────────┐
                    │     ti-ui     │
                    │ React + Vite  │
                    └───────┬───────┘
                            │
                            ▼
                  ┌───────────────────┐
                  │  ti-gateway-api   │
                  │   API Gateway     │
                  │       + BFF       │
                  └─────────┬─────────┘
                            │
          ┌─────────────────┼──────────────────┐
          │                 │                  │
          ▼                 ▼                  ▼
 ┌────────────────┐ ┌────────────────┐ ┌────────────────┐
 │ti-knowledge-api│ │ti-orchestrator │ │ ti-audit-api   │
 │                │ │     -api       │ │                │
 │ Questions      │ │ Import/Export  │ │ Audit Records  │
 │ Answers        │ └───────┬────────┘ └────────────────┘
 │ Resources      │         │
 │ Code Examples  │         ▼
 └───────┬────────┘ ┌────────────────┐
         │           │ ti-import-api  │
         ▼           └────────────────┘
 ┌───────────────┐
 │  PostgreSQL   │
 │ Knowledge DB  │
 └───────────────┘
```

---

## 24. Project Status

`ti-knowledge-api` is an educational training project intended to demonstrate production-like enterprise development practices.

The implementation is expected to evolve as additional platform capabilities are introduced, including:

* Additional knowledge entities
* Import/export workflows
* Event-driven communication
* Audit logging
* Notifications
* Advanced observability
* Kubernetes deployment
* AWS deployment
* CI/CD automation

---

