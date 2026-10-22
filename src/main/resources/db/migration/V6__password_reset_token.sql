CREATE TABLE reset_token (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    token_hash VARCHAR(64)     NOT NULL UNIQUE,   -- SHA-256 jako hex = 64 znaků
    used_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    expires_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_reset_token_user_id ON reset_token(user_id);