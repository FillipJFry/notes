-- Додаємо стовпець "id" до таблиці "users" з автоінкрементом
ALTER TABLE users ADD COLUMN id SERIAL;

-- Видаляємо PRIMARY KEY із стовпця "login"
ALTER TABLE users DROP CONSTRAINT users_pkey;

-- Додаємо новий PRIMARY KEY "id"
ALTER TABLE users ADD PRIMARY KEY (id);

-- Додаємо обмеження на унікальність стовпця "login"
ALTER TABLE users ADD CONSTRAINT login_unique UNIQUE (login);

-- Додаємо стовпець "id_user" до таблиці "note"
ALTER TABLE note ADD COLUMN id_user BIGINT;

-- Оновлюємо значення стовпця "id_user" з таблиці "note"
UPDATE note SET id_user = users.id FROM users WHERE users.login = 'user';

-- Змінюємо стовпець "id_user" на NOT NULL
ALTER TABLE note ALTER COLUMN id_user SET NOT NULL;

-- Додаємо зовнішній ключ до стовпця "id_user", який посилається на стовпець "id" таблиці "users"
ALTER TABLE note ADD CONSTRAINT fk_note_id_user FOREIGN KEY (id_user) REFERENCES users (id) ON DELETE CASCADE;
