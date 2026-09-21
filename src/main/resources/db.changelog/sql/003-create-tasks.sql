CREATE TABLE tasks
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(200),
    description VARCHAR(800),
    state       VARCHAR(25),
    project_id  UUID REFERENCES projects(id) CASCADE DELETE
    createdAt   TIMESTAMP,
    updatedAt   TIMESTAMP
);

CREATE INDEX IX_TASKS ON tasks (state);