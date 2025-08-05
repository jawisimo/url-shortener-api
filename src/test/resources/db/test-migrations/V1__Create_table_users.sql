CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(50) UNIQUE  NOT NULL,
    email    VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100)        NOT NULL,
    role     VARCHAR(50)         NOT NULL CHECK (role IN ('ROLE_ADMIN', 'ROLE_USER'))
);
