CREATE TABLE IF NOT EXISTS tasks
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    state       VARCHAR(25) NOT NULL,
    project_id  UUID NOT NULL REFERENCES projects(id),
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL
);

CREATE INDEX IX_TASKS ON tasks (state, project_id);
