CREATE TABLE refresh_token (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    token_hash VARCHAR(64)     NOT NULL UNIQUE,   -- SHA-256 jako hex = 64 znaků
    expires_at TIMESTAMPTZ  NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    revoked_at TIMESTAMPTZ                     -- NULL = platný
);

CREATE INDEX idx_refresh_token_user_id ON refresh_token(user_id);

CREATE TABLE password_reset_token (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    token_hash VARCHAR(64)     NOT NULL UNIQUE,   -- SHA-256 jako hex = 64 znaků
    expires_at TIMESTAMPTZ  NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    used_at    TIMESTAMPTZ                     -- NULL = nepoužitý
);

CREATE INDEX idx_password_reset_token_user_id ON password_reset_token(user_id);