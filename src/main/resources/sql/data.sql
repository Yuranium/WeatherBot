CREATE TABLE TelegramClients(
    chat_id bigint NOT NULL PRIMARY KEY,
    first_name text NOT NULL,
    last_name text,
    date_of_registration timestamp NOT NULL DEFAULT current_timestamp,
    client_role text NOT NULL CHECK(client_role IN('ADMIN', 'CLIENT')) DEFAULT 'CLIENT',
    username text NOT NULL UNIQUE
);