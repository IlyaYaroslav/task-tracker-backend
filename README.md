# Task backend

This application owns projects, authentication, users, passwords, and profile pictures. It no longer calls `user-service`. PostgreSQL stores both users and projects; MinIO stores profile pictures.

## Run locally

Use JDK 26 (the Gradle toolchain), PostgreSQL, and MinIO:

```powershell
docker compose up -d
.\gradlew.bat bootRun
```

If MinIO is already running on port 9000, reuse it and start only `docker compose up -d postgres`. The local defaults use PostgreSQL at `localhost:5431/tasks` (`admin` / `password`) and MinIO at `localhost:9000` (`minioadmin` / `minioadmin`, bucket `user-files`). These credentials are for local development.

All endpoints now share `http://localhost:8080/api/task-service`. Change the frontend's former `/api/user-service` base URL to `/api/task-service` (and its port to the backend's port). There is no legacy `/api/user-service` alias.

| Method | Path below the base URL | Access |
| --- | --- | --- |
| POST | `/auth/register` | Public; JSON `name`, `email`, `password`; returns `id`, `token` |
| POST | `/auth/login` | Public; JSON `email`, `password`; returns `id`, `accessToken` |
| GET | `/users` | Bearer token; user summaries |
| GET | `/users/{userId}` | Bearer token; profile |
| PATCH | `/users/{userId}/name` | Account owner; `newFirstName`, `newLastName` |
| PATCH | `/users/{userId}/password` | Account owner; `oldPassword`, `newPassword` |
| PUT | `/users/{userId}/profile-picture` | Account owner; raw image bytes and image Content-Type |
| DELETE | `/users/{userId}/profile-picture` | Account owner |
| GET, POST | `/projects` | Bearer token |
| DELETE | `/projects/{projectId}` | Project owner |

Send `Authorization: Bearer <token>` for protected endpoints. Swagger UI is at `/api/task-service/swagger-ui/index.html`. Passwords use BCrypt; JWT subjects remain user UUIDs and claims remain compatible with the previous user service.

## Existing accounts and deployment

Liquibase creates the user tables in the tasks database; it does **not** copy accounts from the old users database. Before switching existing users:

1. Back up both databases and pause account/profile writes during the final transfer.
2. Run the merged application against the tasks database once to apply its schema migrations.
3. Export only the old `public.users` table's data and import it into the tasks database. Preserve UUIDs, password hashes, roles, and profile picture object names. For PostgreSQL tools, use `pg_dump --data-only --table=public.users --column-inserts --no-owner --no-privileges --file=users-data.sql <source-connection>` and `psql --single-transaction --set=ON_ERROR_STOP=1 --file=users-data.sql <target-connection>`. The target user table should be empty; resolve conflicting IDs/emails explicitly before importing. Do not copy the old Liquibase tracking tables.
4. Reuse the existing MinIO bucket or copy its objects, and configure the same JWT secret if existing tokens must keep working. Keeping user UUIDs preserves project ownership/member references.
5. Point the frontend at the unified API and verify login, profiles, pictures, and projects before retiring the old service.

The `dev` profile requires database settings (`POSTGRES_HOST`, `POSTGRES_PORT`, `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`), `SECURITY_JWT_SECRET` (Base64 HMAC key), and `MINIO_ENDPOINT`, `MINIO_ACCESS_KEY`, `MINIO_SECRET_KEY`. Optional settings are `MINIO_PUBLIC_ENDPOINT` (browser-accessible origin), `MINIO_BUCKET`, `MINIO_REGION`, `MINIO_INITIALIZE_BUCKET`, and `JWT_EXPIRATION_MS`. Supply them in the deployment's `task-service.env`. `USER_SERVICE_URL` is no longer used. Database exports and environment files contain private data and should stay outside Git.

## Verification

```powershell
.\gradlew.bat test
.\gradlew.bat bootJar
```

Tests use an isolated H2 database in PostgreSQL mode, run Liquibase and Hibernate schema validation, and exercise HTTP registration/login, password changes, JWT rejection, profile ownership, and project ownership. They do not require external PostgreSQL or MinIO and do not test actual image storage transfers.
