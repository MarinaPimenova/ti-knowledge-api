# TI Knowledge API

`ti-knowledge-api` is a Spring Boot microservice of the **Training Internal (TI) Knowledge Platform**.

It is responsible for managing technical interview questions, answers, learning resources, and code examples.

## Technology Stack

| Category          | Technology                  |
| ----------------- | --------------------------- |
| Language          | Java 21                     |
| Framework         | Spring Boot 4.0.7           |
| Build             | Gradle                      |
| API               | REST / Spring MVC           |
| Database          | PostgreSQL                  |
| Persistence       | Spring Data JPA / Hibernate |
| Security          | Spring Security / OAuth 2.0 |
| Identity Provider | Okta                        |
| API Documentation | Springdoc OpenAPI           |
| Metrics           | Micrometer / Prometheus     |
| Tracing           | Micrometer Tracing / Brave  |
| Containerization  | Docker                      |

## Prerequisites

* Java 21
* Docker
* PostgreSQL
* Okta OAuth 2.0 configuration

The project uses the Gradle Wrapper, so Gradle does not need to be installed separately.

Check Java:

```bash
java -version
```

## Build

Build the application:

```bash
./gradlew clean build
```

Build without tests:

```bash
./gradlew clean build -x test
```

The generated JAR is available in:

```text
build/libs/
```

## Run Locally

Start the application with:

```bash
./gradlew bootRun
```

The default port is:

```text
8081
```

Application URL:

```text
http://localhost:8081
```

## Configuration

Main configuration:

```text
src/main/resources/application.yaml
```

Local configuration:

```text
src/main/resources/application-local.yaml
```

The application requires the following environment variables:

```text
KNOWLEDGE_DB_URL
KNOWLEDGE_USER
KNOWLEDGE_PASSWORD
OKTA_OAUTH2_ISSUER
```

The server port can be changed with:

```text
SERVER_PORT
```

Default:

```text
8081
```

The application uses PostgreSQL schema:

```text
knowledge
```

Hibernate schema generation is disabled, so database schema changes must be managed separately.

## API

### Version

```http
GET /rest/v1/version
```

Example:

```text
http://localhost:8081/rest/v1/version
```

HTTP request:

```text
http/version.http
```

### Questions

Question API requests are available in:

```text
http/question/question.http
```

## OpenAPI / Swagger

OpenAPI specification:

```text
http://localhost:8081/v3/api-docs
```

Swagger UI:

```text
http://localhost:8081/swagger-ui/index.html
```

## Actuator

Health endpoint:

```text
http://localhost:8081/actuator/health
```

Prometheus metrics:

```text
http://localhost:8081/actuator/prometheus
```

## Docker

Build the application:

```bash
./gradlew clean build
```

Build the Docker image:

```bash
docker build -t ti-knowledge-api:latest .
```

Run the container:

```bash
docker run --rm \
  -p 8081:8081 \
  ti-knowledge-api:latest
```

The application is then available at:

```text
http://localhost:8081
```

Helper scripts are available under:

```text
k8s/
```

* `build-target.sh` — builds the application
* `build-docker-image.sh` — builds the Docker image
* `build-target-and-image.sh` — builds both

## Gradle Commands

| Command                  | Description                 |
| ------------------------ | --------------------------- |
| `./gradlew clean`        | Clean build directory       |
| `./gradlew build`        | Build application           |
| `./gradlew test`         | Run tests                   |
| `./gradlew bootRun`      | Run application             |
| `./gradlew dependencies` | Show dependencies           |
| `./gradlew tasks`        | Show available Gradle tasks |

## Troubleshooting

See:

```text
doc/Troubleshooting.md
```

for common development and runtime issues.
