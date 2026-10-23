CREATE TABLE game_character (
                                id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                user_id          BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
                                game_system_id   BIGINT NOT NULL REFERENCES game_system(id),
                                membership_id    BIGINT REFERENCES membership(id) ON DELETE SET NULL,
                                template_version INT NOT NULL,
                                name             VARCHAR(100) NOT NULL,
                                sheet            JSONB NOT NULL DEFAULT '{}'::jsonb,
                                created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
                                updated_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_game_character_user ON game_character(user_id);
CREATE INDEX idx_game_character_membership ON game_character(membership_id);

INSERT INTO game_system (code, name) VALUES
                                         ('dnd5e',   'Dungeons & Dragons 5e'),
                                         ('generic', 'Obecná postava');