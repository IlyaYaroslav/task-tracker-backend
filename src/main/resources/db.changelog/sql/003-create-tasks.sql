CREATE TABLE tasks
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(200),
    description VARCHAR(800),
    state       VARCHAR(25),
    createdAt   TIMESTAMP,
    updatedAt   TIMESTAMP
);

CREATE INDEX IX_TASKS ON tasks(state);