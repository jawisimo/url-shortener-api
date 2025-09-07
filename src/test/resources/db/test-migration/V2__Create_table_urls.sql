CREATE TABLE IF NOT EXISTS urls
(
    id             BIGSERIAL PRIMARY KEY,
    short_url_code VARCHAR(50),
    long_url       VARCHAR(2000)                       NOT NULL,
    visits         BIGINT    DEFAULT 0                 NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    expires_at     TIMESTAMP,
    user_id        BIGINT                              NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
