CREATE TABLE IF NOT EXISTS items_table(
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    cost INT NOT NULL
);