# 📘 Business Document

## 1. Entity

### User

* id
* username
* password
* role

### Project

* id
* name
* description

### Task

* id
* title
* status (TODO, IN_PROGRESS, DONE)
* deadline
* user_id
* project_id

---

## 2. Quan hệ

* User - Task: 1 - N
* Project - Task: 1 - N
* User - Project: N - N

---

## 3. Flow nghiệp vụ

### Tạo task

* Nhập title, deadline, projectId
* Validate project tồn tại

### Assign task

* Kiểm tra user thuộc project
* Nếu không → lỗi

### Update status

* TODO → IN_PROGRESS → DONE
* Nếu DONE → không update tiếp

---

## 4. Rule

* Không assign task nếu user không thuộc project
* Không update khi DONE
* Deadline phải hợp lệ

---

## 5. Error Handling

* 400: dữ liệu sai
* 404: không tìm thấy
* 500: lỗi server

---

## 6. Test Console + Log

Vui lòng dán ảnh test console tại đây.

---

## 7. Init Spring Boot project

### 📌 Cách tạo

Dùng: Spring Initializr

Dependencies:

* Spring Web
* Spring Data JPA
* SQL Server Driver
* Lombok (optional)

### 📁 2. Cấu trúc package chuẩn

com.hai.taskflow
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── exception
 └── config

### ⚙️ 3. Config DB + profile

application.properties

```
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=InternIKU
spring.datasource.username=sa
spring.datasource.password=123456

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## 8. Xử lý lỗi cơ bản + Refactor naming + Commit mẫu

### 8.1 Tạo Exception chuẩn

```java
package com.example.task_manager.exception;

public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
}
```

### 8.2 Global Exception Handler

```java
package com.example.task_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleAppException(AppException e) {
        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());
        return error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "Internal Server Error");
        return error;
    }
}
```

### 8.3 Validate trong Service (ví dụ UserService)

```java
public User create(User user) {
    if (user.getUsername() == null || user.getUsername().isBlank()) {
        throw new AppException("Username không được để trống");
    }

    if (userRepository.existsByUsername(user.getUsername())) {
        throw new AppException("Username đã tồn tại");
    }

    return userRepository.save(user);
}
```

### 8.4 Refactor naming (code sạch)

```java
// Trước
UserService s;
UserRepository r;

// Sau
private final UserService userService;
private final UserRepository userRepository;
```

### 8.5 Commit + ghi chú (mẫu)

```bash
git status
git add .
git commit -m "feat: add app exception and global handler"
```

