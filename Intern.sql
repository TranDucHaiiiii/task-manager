Create database InternIKU
GO
USE InternIKU
CREATE TABLE users (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE projects (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE user_project (
    user_id BIGINT,
    project_id BIGINT,
    PRIMARY KEY (user_id, project_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (project_id) REFERENCES projects(id)
);

CREATE TABLE tasks (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    title VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    deadline DATE,
    user_id BIGINT,
    project_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (project_id) REFERENCES projects(id)
);
CREATE INDEX idx_task_user ON tasks(user_id);
CREATE INDEX idx_task_project ON tasks(project_id);
CREATE INDEX idx_task_status ON tasks(status);
--insert data test 30 record
INSERT INTO tasks(title, status, deadline, user_id, project_id) VALUES
('Fix bug login', 'TODO', '2026-04-01', 1, 1),
('Fix UI homepage', 'IN_PROGRESS', '2026-04-02', 2, 1),
('Write API auth', 'DONE', '2026-04-03', 1, 1),
('Test payment', 'TODO', '2026-04-04', 2, 1),
('Optimize query', 'IN_PROGRESS', '2026-04-05', 1, 2),
('Deploy server', 'DONE', '2026-04-06', 1, 2),
('Fix CSS mobile', 'TODO', '2026-04-07', 2, 2),
('Add feature cart', 'IN_PROGRESS', '2026-04-08', 1, 1),
('Refactor code', 'DONE', '2026-04-09', 2, 2),
('Write unit test', 'TODO', '2026-04-10', 1, 1),

('Update API docs', 'IN_PROGRESS', '2026-04-11', 2, 1),
('Fix bug logout', 'DONE', '2026-04-12', 1, 2),
('Add search feature', 'TODO', '2026-04-13', 2, 2),
('Test API', 'IN_PROGRESS', '2026-04-14', 1, 1),
('Improve UI', 'DONE', '2026-04-15', 2, 1),
('Fix database error', 'TODO', '2026-04-16', 1, 2),
('Optimize performance', 'IN_PROGRESS', '2026-04-17', 2, 2),
('Write documentation', 'DONE', '2026-04-18', 1, 1),
('Fix API response', 'TODO', '2026-04-19', 2, 1),
('Add logging', 'IN_PROGRESS', '2026-04-20', 1, 2),

('Fix deploy issue', 'DONE', '2026-04-21', 2, 2),
('Update database schema', 'TODO', '2026-04-22', 1, 1),
('Write integration test', 'IN_PROGRESS', '2026-04-23', 2, 1),
('Fix memory leak', 'DONE', '2026-04-24', 1, 2),
('Add notification', 'TODO', '2026-04-25', 2, 2),
('Test security', 'IN_PROGRESS', '2026-04-26', 1, 1),
('Fix concurrency bug', 'DONE', '2026-04-27', 2, 2),
('Improve UX', 'TODO', '2026-04-28', 1, 2),
('Update dependencies', 'IN_PROGRESS', '2026-04-29', 2, 1),
('Final testing', 'DONE', '2026-04-30', 1, 1);
ALTER TABLE tasks
ADD CONSTRAINT chk_status
CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE'));
SELECT * FROM tasks
WHERE user_id = 1;
SELECT * FROM tasks
WHERE project_id = 1;
SELECT * FROM tasks
WHERE status = 'DONE';
SELECT t.*, u.username, p.name AS project_name
FROM tasks t
JOIN users u ON t.user_id = u.id
JOIN projects p ON t.project_id = p.id
WHERE t.status = 'IN_PROGRESS';
SELECT id, username FROM users;
INSERT INTO projects(name, description) VALUES
('Project A', 'Demo'),
('Project B', 'Test');