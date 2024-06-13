CREATE TABLE history (
                         id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                         expression VARCHAR(256) NOT NULL,
                         evaluation VARCHAR(32) NOT NULL
);

CREATE TABLE isBanned (
                          is_banned BOOLEAN NOT NULL
);

INSERT INTO isBanned VALUES (false);
