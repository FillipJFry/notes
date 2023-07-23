DROP TABLE IF EXISTS users;

CREATE TABLE users(
    login VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    CONSTRAINT login_len_range CHECK (length(login) >= 4),
    CONSTRAINT pwd_len_range CHECK (length(password) >= 3));
