# 📌 Task Management System (Spring Boot)

## 📖 Giới thiệu

Hệ thống quản lý công việc gồm:

* User
* Project
* Task

Cho phép:

* Tạo project
* Tạo task
* Gán task cho user
* Cập nhật trạng thái task
* Phân quyền bằng JWT

---

## ⚙️ Công nghệ sử dụng

* Java 17
* Spring Boot
* Spring Data JPA
* SQL Server
* JWT Authentication
* Maven

---

## 🚀 Cách chạy project

### 1. Clone project

```bash
git clone <link github>
```

### 2. Cấu hình database

Sửa file `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=YourDB
spring.datasource.username=sa
spring.datasource.password=123456
```

### 3. Run project

```bash
mvn spring-boot:run
```

---

## 🔐 Authentication

### Login

```http
POST /api/auth/login
```

Response:

```json
{
  "token": "JWT_TOKEN"
}
```

👉 Dùng token:

```
Authorization: Bearer <token>
```

---

## 📌 API chính

### Task

* POST /api/tasks → tạo task
* PATCH /api/tasks/{id}/status → update status
* GET /api/tasks/project/{id}
* GET /api/tasks/user/{id}

### Project

* POST /api/projects
* GET /api/projects

### User

* POST /api/auth/register
* POST /api/auth/login

---

## 📊 Business Rule

* Task chỉ assign cho user thuộc project
* Không cho update task khi status = DONE
* Deadline phải > hiện tại

---

## 🧪 Test

* Dùng Postman hoặc Swagger
* Có sẵn data test trong file SQL

### Ảnh test console

//output tuan 1

![Test Console](docs/images/test-console.png)

---

## 👨‍💻 Tác giả

* Trần Đức Hải
#duchai.40net@gmail.com
* haitdph41477@fpt.edu.vn
* ## Week 3 - User API

- Xây dựng UserEntity, Repository, Service, Controller
- Implement API create và get user
- Xử lý lỗi cơ bản (null, duplicate)
- Áp dụng Global Exception Handler
- Refactor naming theo chuẩn clean code
