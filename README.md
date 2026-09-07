# TI Knowledge API

`ti-knowledge-api` is a Spring Boot microservice of the **Training Internal (TI) Knowledge Platform**.

It is responsible for managing technical interview questions, answers, learning resources, and code examples.

## Table of Contents

* [Technology Stack](#technology-stack)
* [Prerequisites](#prerequisites)
* [Build](#build)
* [Configuration](#configuration)
* [Run Locally (as part of the platform)](#run-locally-as-part-of-the-platform)
* [API](#api)
* [OpenAPI / Swagger](#openapi--swagger)
* [Actuator](#actuator)
* [Docker](#docker)
* [Gradle Commands](#gradle-commands)
* [Troubleshooting](#troubleshooting)
* [Run as a Standalone Microservice (Local Functional Testing)](#run-as-a-standalone-microservice-local-functional-testing)

## Technology Stack

| Category           | Technology                  |
| ------------------ | ---------------------------- |
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
OKTA_DOMAIN
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

### Security

Access is authorized per path prefix (see `ResourceServerConfig`):

| Path prefix                                            | Access                                  |
| ------------------------------------------------------- | ---------------------------------------- |
| `/rest/**`, `/actuator/**`, `/v3/**`, `/swagger-ui/**`   | Public, no token required               |
| `/api/**`                                               | Requires a valid Okta JWT bearer token  |

The JWT is validated against `spring.security.oauth2.resourceserver.jwt.issuer-uri`, which is derived from `OKTA_DOMAIN`.

## Run Locally (as part of the platform)

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

This mode expects the rest of the TI platform's infrastructure (database, Okta) to already be reachable, as documented in `ti-gateway-api`. To run this service completely on its own instead, see [Run as a Standalone Microservice](#run-as-a-standalone-microservice-local-functional-testing).

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

Full CRUD for questions (`/api/v1/questions`) is available in:

```text
http/question/question.http
```

### Projects

```text
http/project/project.http
```

### Tags

```text
http/tag/tag.http
```

### Question Levels

```text
http/qlevel/qlevel.http
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

HTTP request:

```text
http/openapi/openapi.http
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

HTTP requests:

```text
http/actuator/actuator.http
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
| ------------------------- | ---------------------------- |
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

## Run as a Standalone Microservice (Local Functional Testing)

This section describes how to run **only** `ti-knowledge-api` plus the infrastructure it directly depends on, so you can manually verify functionality without starting the rest of the TI platform.

### 1. Start the required infrastructure

The only hard dependency is the service's own PostgreSQL database. Its docker-compose definition lives in the gateway repo's `docker/` folder:

```bash
cd ../ti-gateway-api/docker
docker compose -f _04_knowledge_postgres.yaml up -d
```

This starts `ti-knowledge-db` on `localhost:5432`, creates the `knowledge_db` database and runs the init scripts under `knowledge-init-scripts/`.

Optionally, start observability backends if you want to inspect metrics/traces while testing (none of these are required for functional testing):

| Compose file          | Provides                | Port |
| ---------------------- | ------------------------ | ---- |
| `_06_prometheus.yaml`  | Prometheus (metrics)     | 9090 |
| `_07_loki.yaml`        | Loki (log aggregation)   | 3100 |
| `_08_zipkin.yaml`      | Zipkin (tracing)         | 9411 |
| `_09_grafana.yaml`     | Grafana (dashboards)     | 3000 |

Run any of them the same way, e.g.:

```bash
docker compose -f _08_zipkin.yaml up -d
```

### 2. Configure the application

Use the `local` Spring profile together with `application-local.yaml`, which already points at the database started above:

```text
src/main/resources/application-local.yaml
```

Adjust it if your database credentials differ from the compose defaults (`knowledge_user` / `qwerty` / `knowledge_db`), and set `issuer-uri` to a real Okta authorization server if you plan to exercise `/api/**` endpoints.

Alternatively, export the same values as environment variables instead of using the `local` profile:

```bash
export KNOWLEDGE_DB_URL=jdbc:postgresql://localhost:5432/knowledge_db
export KNOWLEDGE_USER=knowledge_user
export KNOWLEDGE_PASSWORD=qwerty
export OKTA_DOMAIN=<your-okta-domain>
```

### 3. Run the application

From IntelliJ IDEA, run `TiKnowledgeApiApplication` with active profile `local`, or from the command line:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

### 4. Verify the service is up

These endpoints require no authentication, so use them first as a smoke test:

```bash
curl http://localhost:8081/rest/v1/version
curl http://localhost:8081/actuator/health
```

### 5. Exercise the API with the provided HTTP files

Every REST endpoint has a ready-to-run request file under `http/`. Open them with the IntelliJ HTTP Client or the VS Code REST Client extension and click "Send Request":

| File                            | Covers                                   | Auth required |
| -------------------------------- | ------------------------------------------ | -------------- |
| `http/version.http`             | `GET /rest/v1/version`                    | No             |
| `http/question/question.http`   | Full question CRUD (`/api/v1/questions`)  | Yes            |
| `http/project/project.http`     | List projects (`/api/v1/projects`)        | Yes            |
| `http/tag/tag.http`             | List tags (`/api/v1/tags`)                | Yes            |
| `http/qlevel/qlevel.http`       | List question levels (`/api/v1/qlevels`)  | Yes            |
| `http/actuator/actuator.http`   | Health / info / Prometheus metrics        | No             |
| `http/openapi/openapi.http`     | Raw OpenAPI spec + Swagger UI link        | No             |

### 6. Obtaining a JWT for the protected endpoints

Every request under `/api/**` requires a valid access token issued by the Okta authorization server configured via `OKTA_DOMAIN` / `issuer-uri`. Obtain one (e.g. via an Okta application's client-credentials grant, or by copying the access token from an existing browser session against the full platform), then replace the `<JWT>` placeholder in the `.http` files with it:

```http
Authorization: Bearer <JWT>
```

Each `.http` file that hits `/api/**` also includes a request without the `Authorization` header, to confirm the endpoint correctly returns `401 Unauthorized` when no token is supplied.

### 7. Tear down

```bash
cd ../ti-gateway-api/docker
docker compose -f _04_knowledge_postgres.yaml down
```

Stop any optional observability compose files the same way.
