CREATE TABLE IF NOT EXISTS upgrader_items_table(
    id SERIAL PRIMARY KEY,
    item_id BIGINT NOT NULL,

    FOREIGN KEY (item_id) REFERENCES items_table (id)
);
