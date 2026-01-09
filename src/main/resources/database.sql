CREATE DATABASE IF NOT EXISTS bookstore_db;
USE bookstore_db;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fullname VARCHAR(100),
    role VARCHAR(20) DEFAULT 'STAFF'
);

INSERT INTO users (username, password, fullname, role)
VALUES ('admin', '123456', 'Admin Default', 'ADMIN');