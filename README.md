# Task Management System (Spring Boot)

## Introduction
Task Management System provides APIs for user authentication, project management, and task tracking.
It uses JWT for authentication and role-based authorization (USER, MANAGER).

## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- SQL Server
- Spring Security + JWT
- Swagger (springdoc-openapi)
- Maven

## Setup
### Database
Update credentials in profile files:
- `application-dev.yml`
- `application-prod.yml`

Default configuration uses SQL Server. Create databases:
- `TaskManagerDev`
- `TaskManagerProd`

### Profiles
Active profile is set in `application.yml`:
```yaml
spring:
  profiles:
    active: dev
```

Override when running:
```
--spring.profiles.active=dev
--spring.profiles.active=prod
```

## Build
```bash
mvn clean package
```
Output jar: `target/*.jar`

## Run
```bash
java -jar target/<app>.jar
```

Run with profile:
```bash
java -jar target/<app>.jar --spring.profiles.active=dev
java -jar target/<app>.jar --spring.profiles.active=prod
```

## Swagger
Swagger UI:
- `/swagger-ui.html`
- `/swagger-ui/index.html`

OpenAPI JSON:
- `/v3/api-docs`

## Authentication (JWT)
1. Register user: `POST /api/auth/register`
2. Login: `POST /api/auth/login`
3. Use token in header:
```
Authorization: Bearer <token>
```

Default role for new users: `USER`.
To test `MANAGER` role, insert into `user_roles` or update via DB.

## API Notes
### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`

### Project
- `POST /api/projects` (MANAGER only)
- `GET /api/projects`

### Task
- `POST /api/tasks`
- `PUT /api/tasks/{taskId}/assign/{userId}`
- `PUT /api/tasks/{taskId}/status`
- `GET /api/tasks/user/{userId}` (USER only sees own)
- `GET /api/tasks/project/{projectId}` (USER only sees own in project)

## Test Flow (Postman/Swagger)
1. Register user.
2. Login to get JWT token.
3. Call secured APIs with header `Authorization: Bearer <token>`.
4. Validate:
   - USER cannot create project.
   - USER cannot view tasks of other users.

## Config Check
If you see DB or port errors:
1. Check `application-<profile>.yml` for datasource.
2. Confirm SQL Server is running.
3. Confirm correct profile is active.

## Expected Result
Anyone can clone, configure DB, run with a profile, and test via Swagger/Postman successfully.

## Weekly Progress (Week 1 - 10)
### Week 1
- Entities: init project, base package structure.
- API: health run, initial controller skeleton.
- Notes: verified app boots with Maven.

### Week 2
- Entities: User/Project/Task models and basic fields.
- Database: SQL Server connection + JPA config.
- Notes: created initial tables via Hibernate.

### Week 3
- API: User CRUD (create, list).
- Service: input validation + duplicate check.
- Exception: GlobalExceptionHandler (basic).

### Week 4
- Entities: mapping User-Task, Project-Task.
- API: list tasks by user/project.
- Notes: fixed JSON infinite loop handling.

### Week 5
- Service: assign task, update status, business rules.
- Rules: DONE không update, TODO -> DONE bị chặn.
- Notes: chuẩn hóa logic trong service layer.

### Week 6
- Validation: @NotBlank, @Size, deadline > current date.
- Response: ApiResponse chuẩn hóa output.
- Exception: BadRequest/NotFound + map lỗi theo field.

### Week 7
- Auth: JWT login/register.
- Security: filter + SecurityConfig + role USER/MANAGER.
- Authorization: MANAGER tạo project, USER xem task của mình.

### Week 8
- Test: unit test TaskService với JUnit5 + Mockito.
- Coverage: createTask, assignTask, updateStatus (success/fail).
- Notes: dùng ArgumentCaptor verify dữ liệu save.

### Week 9
- Swagger: springdoc-openapi + @Operation/@Tag.
- Deploy: profile dev/prod, run jar, config check.
- Docs: README cập nhật hướng dẫn.

### Week 10
- Demo: script chi tiết + checklist.
- Q&A: JPA, JWT, phân quyền.
- Final: review bug nhỏ + chuẩn bị nộp.
