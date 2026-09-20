CREATE TABLE projects
(
    id          UUID primary key,
    name        VARCHAR(100),
    description VARCHAR(200),
    owner_id    UUID REFERENCES users(id) ON DELETE CASCADE,
    task_id     UUID references tasks(id),
    created_at  TIMESTAMP,
    update_at   TIMESTAMP
);

CREATE INDEX IX_Projects ON projects (name, owner_id, task_id);