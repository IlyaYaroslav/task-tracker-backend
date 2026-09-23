CREATE TABLE users
(
    id                          UUID PRIMARY KEY,
    first_name                  VARCHAR(800)  NOT NULL,
    last_name                   VARCHAR(700)  NOT NULL,
    profile_picture_object_name VARCHAR(500),
    user_password               VARCHAR(2000) NOT NULL,
    email                       VARCHAR(255)  NOT NULL
);

CREATE INDEX IX_USERS ON users (email, first_name, last_name);