CREATE TABLE users
(
    id                          UUID PRIMARY KEY,
    first_name                  VARCHAR(800),
    last_name                   VARCHAR(700),
    profile_picture_object_name VARCHAR(500),
    user_password               VARCHAR(2000),
    email                       VARCHAR(255)
);

CREATE INDEX IX_USERS ON users(email, first_name, last_name);