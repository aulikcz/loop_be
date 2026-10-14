CREATE TABLE hello (
                       id      BIGSERIAL PRIMARY KEY,
                       message VARCHAR(255) NOT NULL
);
INSERT INTO hello (message) VALUES ('Ahoj z databáze!');