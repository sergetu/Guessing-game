-- Drop existing tables if needed (for local dev/testing)
DROP TABLE IF EXISTS leaderboard CASCADE;
DROP TABLE IF EXISTS game_record CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Table: users
CREATE TABLE users (
                       id bigserial PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE
);

-- Table: game_records
CREATE TABLE game_records (
                             id bigserial PRIMARY KEY,
                             user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                             code VARCHAR(4) NOT NULL,
                             is_win BOOLEAN NOT NULL,
                             tries_used INTEGER NOT NULL,
                             is_game_closed BOOLEAN NOT NULL
);

-- Table: leaderboard
CREATE TABLE leaderboard (
                             id bigserial PRIMARY KEY,
                             user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                             games_played BIGINT NOT NULL,
                             games_won BIGINT NOT NULL,
                             total_tries BIGINT NOT NULL,
                             rank DOUBLE PRECISION NOT NULL
);

-- Insert users
INSERT INTO users (name) VALUES
                             ('Alice'),
                             ('Bob');

-- Insert game records
INSERT INTO game_records (user_id, code, is_win, tries_used, is_game_closed)
VALUES
    (
        (SELECT id FROM users WHERE name = 'Alice'),
        '1348',
        FALSE,
        8,
        TRUE
    ),
    (
        (SELECT id FROM users WHERE name = 'Bob'),
        '7492',
        TRUE,
        4,
        TRUE
    );

-- Insert leaderboard records
INSERT INTO leaderboard (user_id, games_played, games_won, total_tries, rank)
VALUES
    (
        (SELECT id FROM users WHERE name = 'Alice'),
        1,
        0,
        8,
        0.0
    ),
    (
        (SELECT id FROM users WHERE name = 'Bob'),
        1,
        1,
        4,
        1.0
    );
