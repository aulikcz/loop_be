CREATE TABLE game_invite (
                                id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                game_id          BIGINT NOT NULL REFERENCES game(id) ON DELETE CASCADE,
                                code             varchar(16) NOT NULL UNIQUE,
                                created_by       BIGINT NOT NULL REFERENCES app_user(id),
                                created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
                                expires_at       TIMESTAMPTZ NOT NULL,
                                revoked_at       TIMESTAMPTZ
);

CREATE UNIQUE INDEX uq_game_invite_active ON game_invite (game_id) WHERE revoked_at IS NULL;