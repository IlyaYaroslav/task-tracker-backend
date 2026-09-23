CREATE TABLE IF NOT EXISTS projects
(
    id          UUID primary key,
    name        VARCHAR(100) NOT NULL,
    project_key VARCHAR(10) UNIQUE,
    description VARCHAR(600) NOT NULL,
    owner_id    UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL
);

CREATE INDEX IX_Projects ON projects (name, owner_id);
CREATE INDEX IX_Projects_Owner ON projects (owner_id);
