CREATE TABLE IF NOT EXISTS USERS (
    user_id     INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    CONSTRAINT unique_users UNIQUE (username)
    );

