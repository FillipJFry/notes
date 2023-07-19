ALTER TABLE users ADD COLUMN id BIGINT AUTO_INCREMENT;
ALTER TABLE users DROP PRIMARY KEY;
ALTER TABLE users ADD PRIMARY KEY(id);
ALTER TABLE users ADD CONSTRAINT login_unique UNIQUE(login);

ALTER TABLE note ADD COLUMN id_user BIGINT;
UPDATE note SET id_user = SELECT id FROM users WHERE login = 'user';
ALTER TABLE note ALTER COLUMN id_user BIGINT NOT NULL;
ALTER TABLE note ADD FOREIGN KEY(id_user) REFERENCES users(id) ON DELETE CASCADE;