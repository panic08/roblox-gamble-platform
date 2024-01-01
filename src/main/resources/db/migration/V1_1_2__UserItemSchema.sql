CREATE TABLE IF NOT EXISTS users_items_table(
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users_table (id),
    FOREIGN KEY (item_id) REFERENCES items_table (id)
);