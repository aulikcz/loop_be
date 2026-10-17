CREATE TABLE game_system (
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code             VARCHAR(50)  NOT NULL UNIQUE,   -- např. 'loop'
    name             VARCHAR(100) NOT NULL,
    sheet_template   JSONB        NOT NULL DEFAULT '{}'::jsonb,
    template_version INT          NOT NULL DEFAULT 1
);

CREATE TABLE app_user (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    auth_provider VARCHAR(100) NOT NULL,
    provider_user_id VARCHAR(100) NOT NULL,
    display_name VARCHAR (30) NOT NULL,
    email VARCHAR (100),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE(auth_provider, provider_user_id)
);

create table local_credential(
    user_id BIGINT primary key references app_user(id),
    password_hash VARCHAR(250) not null,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

create table game (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) not null,
    game_system_id BIGINT not null references game_system(id),
    created_by BIGINT not null references app_user(id),
    created_at TIMESTAMPTZ NOT NULL default now()
);

create table player_role(
    id BIGINT generated always as identity primary key,
    role varchar(50) not null unique
);

INSERT INTO player_role (role) VALUES ('NARRATOR');
INSERT INTO player_role (role) VALUES ('PLAYER');

create table membership(
    id BIGINT generated always as identity primary key,
    app_user_id BIGINT not null references app_user(id),
    game_id BIGINT not null references game(id),
    role_id BIGINT not null references player_role(id),
    unique(app_user_id, game_id)
);

create table player_character(
    id BIGINT generated always as identity primary key,
    game_id BIGINT not null references game(id),
    owner_id BIGINT not null references app_user(id),
    name varchar(50) not null,
    data jsonb NOT NULL DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ not null default now(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX membership_game_id ON membership(game_id);
CREATE INDEX player_character_game_id ON player_character(game_id);
CREATE INDEX player_character_owner_id ON player_character(owner_id);