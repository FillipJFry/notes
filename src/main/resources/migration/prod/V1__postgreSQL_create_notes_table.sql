DROP TABLE IF EXISTS note;

CREATE TABLE note(
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content VARCHAR(10000) NOT NULL,
    access_type VARCHAR(10) NOT NULL,
    CONSTRAINT title_len_range CHECK (length(title) >= 5),
    CONSTRAINT content_len_range CHECK (length(content) >= 5)
);

