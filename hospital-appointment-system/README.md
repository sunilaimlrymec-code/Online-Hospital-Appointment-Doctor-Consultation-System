# Hospital Appointment Scheduling & Telemedicine Management System

Spring Boot 3.x rewrite of the original JDBC + Servlets + JSP project.
Frontend is **plain static HTML/CSS/JS** (`src/main/resources/static`) talking
to a **JSON REST API** — no Thymeleaf, no JSP.

## Architecture

Strict 5-tier layering, all under `com.hospital`:

```
entity/        JPA entities (User, Doctor, Disease, Appointment, ConsultationReport)
repository/    Spring Data JPA repositories (replaces every hand-written DAO)
service/       Business logic: auth, booking conflict checks, slot generation
controller/api REST controllers returning JSON (no server-rendered views)
exception/     Custom exceptions + a single @RestControllerAdvice JSON error handler
config/        BCrypt bean, session-based auth interceptor, MVC config
dto/           Request/response DTOs (keeps password hashes out of JSON responses)
```

Static frontend:

```
static/index.html         Landing page
static/pages/*.html        Login, register, dashboards, booking, reviews, reports
static/css/main.css        Single consolidated design system
static/js/common.js        fetch() wrapper + shared nav rendering + session guard
static/js/app.js           Form validation, cascading dropdowns, calendar, countdowns
```

## Before you run it

1. Make sure MySQL is running locally and reachable at `localhost:3306`.
2. Update credentials in `src/main/resources/application.properties` if yours
   differ from the legacy project's (`root` / `ramBp123@`).
3. The schema (`hospitaldb`) is created automatically
   (`createDatabaseIfNotExist=true`), and Hibernate creates/updates the
   tables on startup (`spring.jpa.hibernate.ddl-auto=update`). If you already
   have data in the old schema, it will be reused as-is — table and column
   names match the legacy DAOs exactly (`users`, `doctors`, `diseases`,
   `appointments`, `consultation_reports`).

   > ⚠️ Passwords: the legacy app stored plain-text passwords. This version
   > hashes with BCrypt. Existing rows with plain-text passwords will fail
   > to authenticate until re-registered, or you migrate/re-hash them.

4. Seed at least one row in `diseases` (e.g. `INSERT INTO diseases (disease_name) VALUES ('Cardiology');`)
   so doctor registration and booking have something to select — the
   legacy project relied on this table being pre-populated too.

## Run it

```bash
mvn spring-boot:run
```

Then open **http://localhost:8080**.

## REST API summary

| Method | Path                              | Auth        | Purpose |
|--------|------------------------------------|-------------|---------|
| POST   | `/api/auth/register`               | -           | Patient sign-up |
| POST   | `/api/auth/login`                  | -           | Patient login (sets session) |
| POST   | `/api/auth/logout`                 | patient     | Patient logout |
| GET    | `/api/auth/me`                     | patient     | Current patient session |
| POST   | `/api/doctor/auth/register`        | -           | Doctor sign-up |
| POST   | `/api/doctor/auth/login`           | -           | Doctor login (sets session) |
| POST   | `/api/doctor/auth/logout`          | doctor      | Doctor logout |
| GET    | `/api/doctor/auth/me`              | doctor      | Current doctor session |
| GET    | `/api/diseases`                    | -           | List all diseases |
| GET    | `/api/doctors?diseaseId=`          | -           | Doctors for a disease |
| GET    | `/api/appointments/slots?doctorId=&date=` | -    | Bookable time slots |
| GET    | `/api/appointments`                | patient     | My appointments |
| POST   | `/api/appointments`                | patient     | Book an appointment (409 on conflict) |
| GET    | `/api/doctor/appointments`         | doctor      | My patients' appointments |
| POST   | `/api/reviews`                     | doctor      | Save a consultation report |
| GET    | `/api/reviews/{appointmentId}`     | -           | View a saved report |
| GET    | `/api/reviews/{appointmentId}/status` | -        | Whether a report exists |

Auth is session-cookie based (`HttpSession`), matching the original
servlet design — just with BCrypt hashing instead of plain text, and JSON
instead of redirects. All error responses share one shape:

```json
{ "timestamp": "...", "status": 404, "error": "Not Found", "message": "..." }
```
