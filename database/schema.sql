-- ============================================
-- CSDL Mạng Xã Hội Đơn Giản - Spring MVC Demo
-- ============================================

DROP DATABASE IF EXISTS social_media_db;

CREATE DATABASE social_media_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE social_media_db;

-- Bảng users (có bổ sung cột password)
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER',
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- Bảng posts
CREATE TABLE IF NOT EXISTS posts (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    body        TEXT,
    user_id     INT          NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'PUBLISHED',
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng follows
CREATE TABLE IF NOT EXISTS follows (
    following_user_id  INT NOT NULL,
    followed_user_id   INT NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (following_user_id, followed_user_id),
    FOREIGN KEY (following_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (followed_user_id)  REFERENCES users(id) ON DELETE CASCADE
);

-- Dữ liệu mẫu
INSERT INTO users (username, password, role) VALUES
    ('admin',  'admin123',  'ADMIN'),
    ('alice',  'alice123',  'USER'),
    ('bob',    'bob123',    'USER'),
    ('charlie','charlie123','USER');

INSERT INTO posts (title, body, user_id, status) VALUES
    ('Chào mọi người!',     'Đây là bài viết đầu tiên của tôi.',       2, 'PUBLISHED'),
    ('Spring MVC là gì?',   'Spring MVC là một framework Java...',      2, 'PUBLISHED'),
    ('Học lập trình Java',   'Chia sẻ kinh nghiệm học Java cho beginner.', 3, 'PUBLISHED'),
    ('Review khóa học',      'Mình vừa hoàn thành khóa học Spring...',  4, 'PUBLISHED');

INSERT INTO follows (following_user_id, followed_user_id) VALUES
    (2, 3), -- alice follows bob
    (2, 4), -- alice follows charlie
    (3, 2), -- bob follows alice
    (4, 2); -- charlie follows alice
