# DLMS — Driving License Management System (Backend)

REST API for the **Driving License Management System**, an internal tool for a driving-license department. It covers the full lifecycle of a license: candidate records, service requests, the exam path (vision → theory → practical), license issuance, blocking/unblocking, driver profiles and system administration.

There is no public/citizen access: the API is used only by department staff through the React frontend.

---

## Tech stack

| Layer | Technology |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 3.4.2 (Web, Data JPA, Security, Validation, Cache) |
| Database | MySQL 8.0 |
| Migrations | Flyway |
| Authentication | JWT (jjwt 0.12.6) + BCrypt |
| Cache | Redis 7 (Spring Cache) |
| Mapping | MapStruct 1.6.3 + Lombok |
| API docs | springdoc-openapi 2.8.5 (Swagger UI) |
| File storage | Cloudinary |
| Config | spring-dotenv (`.env` file) |
| Tests | JUnit 5, Spring Security Test, H2 (in-memory) |
| DevOps | Docker, Docker Compose, GitHub Actions |

---

## Roles

| Role | Scope |
| --- | --- |
| `ADMIN` | User management, license category and exam fee configuration, admin dashboard, read access to all data |
| `AGENT` | Daily operations: persons, requests, exams, payments, license issuance, blocking/unblocking, agent dashboard |

Access control is enforced per endpoint with `@PreAuthorize` (see the endpoint table below).

---

## Project structure

```
src/main/java/com/drivinglicense
├── config/        # Cloudinary, OpenAPI (Swagger), Redis cache configuration
├── controller/    # REST controllers
├── dto/           # Request/response DTOs, grouped by domain
├── entity/        # JPA entities
├── enums/         # Role, RequestStatus, ServiceType, ExamType, IssueReason, ...
├── exception/     # Custom exceptions + GlobalExceptionHandler (@RestControllerAdvice)
├── mapper/        # MapStruct mappers (entity <-> DTO)
├── repository/    # Spring Data JPA repositories
├── security/      # SecurityConfig, JwtAuthenticationFilter, JwtService, UserDetailsServiceImpl
└── service/       # Service interfaces + impl/ implementations

src/main/resources
├── application.properties
└── db/migration/  # Flyway scripts (V1 schema, V2 exam fees seed, V3 license categories seed)
```

Request flow: `Controller → DTO → Service → Repository → Entity → MySQL`.

---

## Prerequisites

- **Docker** and **Docker Compose** (recommended path), or
- **JDK 17**, a **MySQL 8** server and a **Redis** server for running without Docker
- A **Cloudinary** account (the application will not start without Cloudinary credentials, see below)

The Maven wrapper (`./mvnw`) is included, so a local Maven install is not required.

---

## Environment variables

All sensitive values are read from environment variables. For local development, create a `.env` file at the project root (it is loaded by `spring-dotenv` and by Docker Compose). **Never commit this file.**

```env
# Database
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password

# JWT
JWT_SECRET=your_base64_encoded_secret
JWT_EXPIRATION=3600000

# Redis (optional, defaults shown)
REDIS_HOST=localhost
REDIS_PORT=6379

# Cloudinary
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

| Variable | Required | Default | Notes |
| --- | --- | --- | --- |
| `DB_USERNAME` | yes | — | MySQL user |
| `DB_PASSWORD` | yes | — | MySQL password. In Docker Compose it is also used as the MySQL **root** password |
| `JWT_SECRET` | yes | — | Must be **Base64-encoded** (it is decoded with `Decoders.BASE64`). Use at least 32 random bytes |
| `JWT_EXPIRATION` | no | `3600000` | Token lifetime in milliseconds (default = 1 hour) |
| `REDIS_HOST` | no | `localhost` | |
| `REDIS_PORT` | no | `6379` | |
| `CLOUDINARY_CLOUD_NAME` | yes | — | |
| `CLOUDINARY_API_KEY` | yes | — | |
| `CLOUDINARY_API_SECRET` | yes | — | |
| `SPRING_DATASOURCE_URL` | no | `jdbc:mysql://localhost:3306/dlms_db?...` | Override the JDBC URL (used by Docker Compose) |

Generate a valid JWT secret with:

```bash
openssl rand -base64 32
```

> The Cahier des Charges specifies a 24-hour token validity. To match it, set `JWT_EXPIRATION=86400000`.

---

## Running the project

### Option 1 — Docker Compose (full stack)

`docker-compose.yml` starts three containers:

| Service | Container | Host port |
| --- | --- | --- |
| MySQL 8.0 | `dlms-mysql` | `3307` → 3306 |
| Redis 7 (alpine) | `dlms-redis` | `6379` |
| Backend | `dlms-backend` | `8080` |

```bash
docker compose up --build
```

The backend waits for MySQL to be healthy, then Flyway creates the schema and seeds the reference data.

> ⚠️ The `backend` service in `docker-compose.yml` does not currently pass the `CLOUDINARY_*` variables to the container, and the `.env` file is not copied into the image. Since `CloudinaryConfig` requires them, add them to the `backend.environment` section before running:
>
> ```yaml
>       CLOUDINARY_CLOUD_NAME: ${CLOUDINARY_CLOUD_NAME}
>       CLOUDINARY_API_KEY: ${CLOUDINARY_API_KEY}
>       CLOUDINARY_API_SECRET: ${CLOUDINARY_API_SECRET}
> ```

Stop everything with `docker compose down` (add `-v` to also delete the MySQL volume).

### Option 2 — Run locally with Maven

1. Start MySQL and Redis. You can reuse the Compose services:

   ```bash
   docker compose up -d mysql redis
   ```

2. Point the application to the database. `application.properties` uses `localhost:3306`, but the Compose MySQL is exposed on **3307**, so either run your own MySQL on 3306 with a `dlms_db` database, or add this to `.env`:

   ```env
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3307/dlms_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
   DB_USERNAME=root
   ```

3. Run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows: `mvnw.cmd spring-boot:run`

The API is available at `http://localhost:8080`.

### Frontend (CORS)

CORS only allows the Vite dev server origins:

- `http://localhost:5173`
- `http://localhost:5174`

The React frontend calls the API at `http://localhost:8080/api`. Any other origin must be added in `SecurityConfig.corsConfigurationSource()`.

---

## First login (initial admin account)

The Flyway migrations seed license categories and exam fees, but **no user account is created**. `/api/users` is restricted to `ADMIN`, so the first administrator must be inserted directly in the database.

Login uses the user's **email**, and passwords are stored as **BCrypt** hashes. Generate a BCrypt hash of your chosen password with any BCrypt tool, then run (replace every placeholder):

```sql
INSERT INTO person (national_number, first_name, last_name, email)
VALUES ('<NATIONAL_NUMBER>', '<FIRST_NAME>', '<LAST_NAME>', '<ADMIN_EMAIL>');

INSERT INTO app_user (email, password, creation_date, user_status, role, person_id)
VALUES ('<ADMIN_EMAIL>', '<BCRYPT_HASH>', CURDATE(), 'ACTIVE', 'ADMIN', LAST_INSERT_ID());
```

Once logged in as admin, create other users through `POST /api/users`.

---

## Database

The schema is managed by **Flyway** (`spring.jpa.hibernate.ddl-auto=validate`, so Hibernate only validates, it never alters tables).

| Migration | Content |
| --- | --- |
| `V1__create_dlms_db_tables.sql` | Tables: `person`, `driver`, `app_user`, `license_category`, `exam_type_config`, `request`, `license`, `license_block`, `international_license`, `exam`, `payment` |
| `V2__seed_exam_type_config.sql` | Exam fees: `VISION` = 10.00, `THEORY` = 20.00 |
| `V3__seed_license_category.sql` | The 7 license categories (Catégorie 1 to 7) with minimum age, validity duration and fee |

Any schema change must be added as a **new** migration file (`V4__...sql`); never edit an already-applied migration.

---

## Authentication

1. Call `POST /api/auth/login` with:

   ```json
   { "email": "<email>", "password": "<password>" }
   ```

2. The response contains the token:

   ```json
   {
     "success": true,
     "message": "...",
     "data": { "token": "<jwt>", "email": "...", "role": "ADMIN", "fullName": "..." }
   }
   ```

3. Send it on every other request:

   ```
   Authorization: Bearer <jwt>
   ```

Public routes: `/api/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`. Everything else requires a valid token. Sessions are stateless.

---

## API documentation (Swagger)

With the application running:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Use the **Authorize** button with the JWT from `/api/auth/login` to test protected endpoints.

---

## Endpoints

List endpoints are paginated with `page` (default `0`) and `size` (default `10`).

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| **Auth** | | | |
| POST | `/api/auth/login` | public | Log in and get a JWT |
| **Dashboards** | | | |
| GET | `/api/admin/dashboard` | ADMIN | Admin dashboard statistics |
| GET | `/api/agent/dashboard` | AGENT | Agent dashboard statistics |
| **Persons** | | | |
| POST | `/api/persons` | AGENT | Create a person |
| GET | `/api/persons` | ADMIN, AGENT | List persons (paginated) |
| GET | `/api/persons/{id}` | AGENT | Get a person |
| GET | `/api/persons/search?query=` | ADMIN, AGENT | Search persons |
| PUT | `/api/persons/{id}` | AGENT | Update a person |
| **Requests** | | | |
| POST | `/api/requests` | AGENT | Create a service request |
| GET | `/api/requests` | ADMIN, AGENT | List requests — filters: `status`, `serviceType`, `nationalNumber` |
| GET | `/api/requests/{id}` | AGENT | Get a request |
| PATCH | `/api/requests/{id}/cancel` | AGENT | Cancel a request |
| **Exams** | | | |
| POST | `/api/exams` | AGENT | Schedule an exam |
| GET | `/api/exams` | AGENT | List exams — filters: `appointmentDate`, `examType`, `pendingOnly` |
| GET | `/api/exams/{id}` | AGENT | Get an exam |
| GET | `/api/exams/request/{requestId}` | AGENT | Exams of a request |
| PATCH | `/api/exams/{id}/result` | AGENT | Record an exam result |
| **Payments** | | | |
| POST | `/api/payments` | AGENT | Record a payment |
| GET | `/api/payments/{id}` | ADMIN | Get a payment |
| GET | `/api/payments/request/{requestId}` | ADMIN | Payments of a request |
| **Licenses** | | | |
| POST | `/api/licenses` | AGENT | Issue a license |
| GET | `/api/licenses` | ADMIN, AGENT | List licenses — filters: `blockingStatus`, `issueReason`, `query` |
| GET | `/api/licenses/{id}` | AGENT | Get a license |
| GET | `/api/licenses/driver/{driverId}` | AGENT | Licenses of a driver |
| **License blocks** | | | |
| POST | `/api/license-blocks` | AGENT | Block a license |
| PATCH | `/api/license-blocks/unblock/{requestId}` | AGENT | Unblock a license |
| GET | `/api/license-blocks/license/{licenseId}` | ADMIN, AGENT | Block history of a license |
| **Drivers** | | | |
| GET | `/api/drivers` | AGENT | List drivers (paginated) |
| GET | `/api/drivers/{id}` | AGENT | Get a driver |
| GET | `/api/drivers/search` | AGENT | Search by `nationalNumber` or `licenseNumber` |
| **License categories** | | | |
| GET | `/api/license-categories` | ADMIN, AGENT | List categories |
| GET | `/api/license-categories/{id}` | ADMIN | Get a category |
| PUT | `/api/license-categories/{id}` | ADMIN | Update min. age, validity, fee |
| **Exam type configs** | | | |
| GET | `/api/exam-type-configs` | ADMIN, AGENT | List exam fees |
| GET | `/api/exam-type-configs/{id}` | ADMIN, AGENT | Get an exam fee |
| PUT | `/api/exam-type-configs/{id}` | ADMIN | Update an exam fee |
| **Users** | | | |
| POST | `/api/users` | ADMIN | Create a user (linked to an existing person) |
| GET | `/api/users` | ADMIN | List users (paginated) |
| GET | `/api/users/{id}` | ADMIN | Get a user |
| PUT | `/api/users/{id}` | ADMIN | Update a user |
| DELETE | `/api/users/{id}` | ADMIN | Delete a user |

### Response format

Successful responses are wrapped in `ApiResponseDTO`:

```json
{ "success": true, "message": "...", "data": { } }
```

Paginated data uses `PageResponseDTO`:

```json
{ "content": [ ], "page": 0, "totalPages": 1, "totalElements": 3 }
```

Errors are handled centrally by `GlobalExceptionHandler` (`ResourceNotFoundException`, `BusinessException`, `ValidationException`, `UnauthorizedException`, Bean Validation errors) and returned as `ErrorResponseDTO`:

```json
{ "status": 404, "message": "...", "path": "/api/..." }
```

---

## Caching

Redis is used as the Spring Cache provider (`RedisConfig`: JSON serialization with `GenericJackson2JsonRedisSerializer`, 1-hour TTL, null values not cached).

| Cache | Content | Evicted when |
| --- | --- | --- |
| `licenseCategories` | License categories | A category is updated |
| `examTypeConfigs` | Exam fees | An exam fee is updated |
| `users` | Users | A user is created, updated or deleted |
| `drivers` | Driver lookups | A license is issued / person data changes |
| `licenses` | License by id | A license is blocked or unblocked |

---

## Tests

```bash
./mvnw test          # run tests
./mvnw clean verify  # full build + tests (same command as the CI)
```

- `PersonServiceImplTest` runs with the `test` profile (`application-test.properties`): in-memory **H2** in MySQL mode, Flyway disabled, cache disabled.
- `DrivingLicenseManagementSystemBackendApplicationTests` (`contextLoads`) uses the **default** profile, so it needs a running MySQL and Redis plus all environment variables (JWT and Cloudinary included).

Surefire reports are written to `target/surefire-reports`.

---

## CI/CD

The GitHub Actions workflow `.github/workflows/.ci-cd.yml` runs on every push and pull request to `main`:

1. **build-and-test** — starts MySQL 8.0 and Redis 7 service containers, sets up JDK 17 (Temurin), runs `./mvnw clean verify -B`, then uploads the test reports as an artifact.
2. **docker-push** — only on push to `main`, after tests pass: builds the image from the `Dockerfile` and pushes it to Docker Hub as `<DOCKERHUB_USERNAME>/dlms-backend:latest`.

Required GitHub repository secrets:

| Secret | Used for |
| --- | --- |
| `JWT_SECRET` | Tests |
| `JWT_EXPIRATION` | Tests |
| `CLOUDINARY_CLOUD_NAME` | Tests |
| `CLOUDINARY_API_KEY` | Tests |
| `CLOUDINARY_API_SECRET` | Tests |
| `DOCKERHUB_USERNAME` | Docker Hub login and image tag |
| `DOCKERHUB_TOKEN` | Docker Hub login |

---

## Deployment

The `Dockerfile` is a multi-stage build:

1. `eclipse-temurin:17-jdk-alpine` — resolves dependencies and packages the JAR (`-DskipTests`)
2. `eclipse-temurin:17-jre-alpine` — runs `app.jar` on port `8080`

To deploy the image published by the CI:

```bash
docker pull <DOCKERHUB_USERNAME>/dlms-backend:latest
```

Run it with the environment variables listed above, a reachable MySQL 8 database (`dlms_db`) and a Redis instance. Set `SPRING_DATASOURCE_URL`, `REDIS_HOST` and `REDIS_PORT` to your production hosts, and add the production frontend origin to the CORS configuration.

---

## Known gaps (compared to the Cahier des Charges)

These points are listed so they are not mistaken for implemented features:

- No initial admin account is seeded (see *First login*).
- Default JWT lifetime is 1 hour; the Cahier requires 24 hours (configurable via `JWT_EXPIRATION`).
- `docker-compose.yml` does not pass the Cloudinary variables to the backend container.
- International licenses have an entity, DTOs and a mapper, but no REST controller is exposed yet.
- No endpoint to delete a person or to edit a request's notes (both are described in §7.1 and §7.2).
