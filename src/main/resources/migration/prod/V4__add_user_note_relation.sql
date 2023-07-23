ALTER TABLE users ADD COLUMN id BIGSERIAL;
ALTER TABLE users DROP CONSTRAINT users_pkey;
ALTER TABLE users ADD PRIMARY KEY (id);
ALTER TABLE users ADD CONSTRAINT login_unique UNIQUE (login);

ALTER TABLE note ADD COLUMN id_user BIGINT;
UPDATE note SET id_user = users.id FROM users WHERE users.login = 'user';
ALTER TABLE note ALTER COLUMN id_user SET NOT NULL;
ALTER TABLE note ADD CONSTRAINT fk_note_id_user FOREIGN KEY (id_user) REFERENCES users (id) ON DELETE CASCADE;
