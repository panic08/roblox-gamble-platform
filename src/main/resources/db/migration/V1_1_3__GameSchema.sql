CREATE TABLE IF NOT EXISTS games_table(
    id SERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    amount INTEGER NOT NULL,
    is_win BOOLEAN NOT NULL,
    created_at BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users_table (id)
);