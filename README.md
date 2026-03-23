# ðŸ“Œ Task Management System (Spring Boot)

## ðŸ“– Giá»›i thiá»‡u

Há»‡ thá»‘ng quáº£n lÃ½ cÃ´ng viá»‡c gá»“m:

* User
* Project
* Task

Cho phÃ©p:

* Táº¡o project
* Táº¡o task
* GÃ¡n task cho user
* Cáº­p nháº­t tráº¡ng thÃ¡i task
* PhÃ¢n quyá»n báº±ng JWT

---

## âš™ï¸ CÃ´ng nghá»‡ sá»­ dá»¥ng

* Java 17
* Spring Boot
* Spring Data JPA
* SQL Server
* JWT Authentication
* Maven

---

## ðŸš€ CÃ¡ch cháº¡y project

### 1. Clone project

```bash
git clone <link github>
```

### 2. Cáº¥u hÃ¬nh database

Sá»­a file `application.properties`:

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

## ðŸ” Authentication

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

ðŸ‘‰ DÃ¹ng token:

```
Authorization: Bearer <token>
```

---

## ðŸ“Œ API chÃ­nh

### Task

* POST /api/tasks â†’ táº¡o task
* PATCH /api/tasks/{id}/status â†’ update status
* GET /api/tasks/project/{id}
* GET /api/tasks/user/{id}

### Project

### Task (Updated)

* POST /api/tasks
* PUT /api/tasks/{taskId}/status
* PUT /api/tasks/{taskId}/assign/{userId}
* GET /api/tasks/project/{projectId}
* GET /api/tasks/user/{userId}

* POST /api/projects
* GET /api/projects

### User

* POST /api/auth/register
* POST /api/auth/login

---

## ðŸ“Š Business Rule

* Task chá»‰ assign cho user thuá»™c project
* KhÃ´ng cho update task khi status = DONE
* Deadline pháº£i > hiá»‡n táº¡i

---

## ðŸ§ª Test

* DÃ¹ng Postman hoáº·c Swagger
* CÃ³ sáºµn data test trong file SQL

### áº¢nh test console

//output tuan 1

![Test Console](docs/images/test-console.png)

### Link áº¢nh test

* [Test Console](docs/images/test-console.png)
* [Postman Test API](docs/postman/Test_API.png)
* [Postman Image](docs/postman/img.png)

### Link áº¢nh test Postman

* [Postman Test API](docs/postman/Test_API.png)
* [Postman Image](docs/postman/img.png)

---

## ðŸ‘¨â€ðŸ’» TÃ¡c giáº£

* Tráº§n Äá»©c Háº£i
#duchai.40net@gmail.com
* haitdph41477@fpt.edu.vn
* ## Week 3 - User API

- XÃ¢y dá»±ng UserEntity, Repository, Service, Controller
- Implement API create vÃ  get user
- Xá»­ lÃ½ lá»—i cÆ¡ báº£n (null, duplicate)
- Ãp dá»¥ng Global Exception Handler
- Refactor naming theo chuáº©n clean code

* ## Week 4 - Task Mapping & API

### 1. Mapping Entity (QUAN TRỌNG NHẤT)

User ↔ Task (1-N)

```java
@OneToMany(mappedBy = "user")
private List<Task> tasks;
```

Project ↔ Task (1-N)

```java
@OneToMany(mappedBy = "project")
private List<Task> tasks;
```

Task (nhiều → 1)

```java
@ManyToOne
@JoinColumn(name = "user_id")
private User user;

@ManyToOne
@JoinColumn(name = "project_id")
private Project project;
```

### 2. Fix Lazy/Eager (CỰC KỲ HAY BỊ HỎI)

Mặc định:

```java
@ManyToOne(fetch = FetchType.LAZY)
```

Để tránh lỗi JSON (infinite loop), cách chuẩn:

```java
@JsonIgnore
private User user;
```

hoặc:

```java
@JsonManagedReference
@JsonBackReference
```

### 3. TaskRepository

```java
package com.example.task_manager.repository;

import com.example.task_manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);

    List<Task> findByProjectId(Long projectId);
}
```

### 4. TaskService

```java
package com.example.task_manager.service;

import com.example.task_manager.entity.Task;
import com.example.task_manager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<Task> getByProject(Long projectId) {
        return repository.findByProjectId(projectId);
    }
}
```

### 5. TaskController (PHẢI CÓ)

```java
package com.example.task_manager.controller;

import com.example.task_manager.entity.Task;
import com.example.task_manager.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/user/{userId}")
    public List<Task> getByUser(@PathVariable Long userId) {
        return service.getByUser(userId);
    }

    @GetMapping("/project/{projectId}")
    public List<Task> getByProject(@PathVariable Long projectId) {
        return service.getByProject(projectId);
    }
}
```
### 6. API list task

* API list task theo user: OK
* API list task theo project: OK
//week 4 - Task Mapping & API
•
Infinite loop: quan hệ 2 chiều → dùng @JsonIgnore hoặc DTO.
•
LazyInitializationException: truy cập LAZY sau session → DTO + fetch join.
•
Null FK: chưa set user/project → load entity và set trước save.
