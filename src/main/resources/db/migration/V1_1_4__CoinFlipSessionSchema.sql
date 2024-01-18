CREATE TABLE IF NOT EXISTS coinflip_sessions_table(
    id SERIAL PRIMARY KEY,
    issuer_user_id BIGINT NOT NULL,
    other_side_user_id BIGINT,
    server_seed VARCHAR(255) NOT NULL,
    client_seed VARCHAR(255) NOT NULL,
    salt VARCHAR(255),
    winner_user_id BIGINT,
    created_at BIGINT NOT NULL,

    FOREIGN KEY (issuer_user_id) REFERENCES users_table (id),
    FOREIGN KEY (other_side_user_id) REFERENCES users_table (id)
);

CREATE TABLE IF NOT EXISTS coinflip_sessions_items_table(
    id SERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL,
    session_key BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,

    FOREIGN KEY (session_id) REFERENCES coinflip_sessions_table (id),
    FOREIGN KEY (user_id) REFERENCES users_table (id),
    FOREIGN KEY (item_id) REFERENCES items_table (id)
);