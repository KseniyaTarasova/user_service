CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(20)        NOT NULL,
    surname    VARCHAR(30)        NOT NULL,
    birth_date DATE               NOT NULL,
    email      VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS card_info
(
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER         NOT NULL,
    number          CHAR(16) UNIQUE NOT NULL,
    holder          VARCHAR(50)     NOT NULL,
    expiration_date DATE            NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_full_name ON users (surname, name);

CREATE INDEX idx_holder ON card_info (holder);
