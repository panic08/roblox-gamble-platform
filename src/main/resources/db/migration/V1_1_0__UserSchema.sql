CREATE TABLE IF NOT EXISTS users_table(
  id SERIAL PRIMARY KEY,
  role VARCHAR(255) NOT NULL,
  registered_at BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS users_roblox_data_table(
    id SERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    roblox_id BIGINT UNIQUE NOT NULL,
    roblox_nickname VARCHAR(255) NOT NULL,
    roblox_avatar_link VARCHAR(255) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users_table (id)
);