CREATE TABLE IF NOT EXISTS users
(
    id                          UUID PRIMARY KEY,
    first_name                  VARCHAR(100) NOT NULL,
    last_name                   VARCHAR(100),
    profile_picture_object_name VARCHAR(255),
    password                    VARCHAR(250) NOT NULL,
    email                       VARCHAR(200) NOT NULL,

    CONSTRAINT UC_User_Email UNIQUE (email)
);

CREATE INDEX IX_USERS ON users (email, first_name, last_name);