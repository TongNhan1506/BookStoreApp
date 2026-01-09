DROP DATABASE IF EXISTS bookstore_db;
CREATE DATABASE bookstore_db;
USE bookstore_db;

CREATE TABLE role (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE author (
    author_id INT AUTO_INCREMENT PRIMARY KEY,
    author_name VARCHAR(100) NOT NULL,
    nationality VARCHAR(100)
);

CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE supplier (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_name VARCHAR(100) NOT NULL,
    supplier_address VARCHAR(255),
    supplier_phone VARCHAR(20)
);

CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20),
    customer_member_rank VARCHAR(20) DEFAULT 'Member',
    customer_member_point INT DEFAULT 0
);

CREATE TABLE payment_method (
    payment_method_id INT AUTO_INCREMENT PRIMARY KEY,
    payment_method_name VARCHAR(50) NOT NULL
);

CREATE TABLE employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_name VARCHAR(100) NOT NULL,
    employee_phone VARCHAR(20),
    base_salary DECIMAL(15, 2), 
    salary_factor FLOAT DEFAULT 1.0,
    day_in DATE,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);

CREATE TABLE account (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    employee_id INT UNIQUE,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE
);

CREATE TABLE book (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    book_name VARCHAR(255) NOT NULL,
    category_id INT,
    supplier_id INT,
    selling_price DECIMAL(15, 2) NOT NULL,
    import_price DECIMAL(15, 2) NOT NULL,
    quantity INT DEFAULT 0,
    FOREIGN KEY (category_id) REFERENCES category(category_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id)
);

CREATE TABLE book_author (
    book_id INT,
    author_id INT,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    FOREIGN KEY (author_id) REFERENCES author(author_id)
);

CREATE TABLE discount (
    discount_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT,
    percent FLOAT NOT NULL, -- Ví dụ: 0.1 là giảm 10%
    start_date DATE,
    end_date DATE,
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);

CREATE TABLE import_ticket (
    import_ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_id INT,
    employee_id INT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(15, 2),
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id),
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
);

CREATE TABLE import_ticket_detail (
    import_ticket_id INT,
    book_id INT,
    import_quantity INT NOT NULL,
    import_price DECIMAL(15, 2),
    PRIMARY KEY (import_ticket_id, book_id),
    FOREIGN KEY (import_ticket_id) REFERENCES import_ticket(import_ticket_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);

CREATE TABLE bill (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    customer_id INT,
    payment_method_id INT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(15, 2),
    tax FLOAT DEFAULT 0.1,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(payment_method_id)
);

CREATE TABLE bill_detail (
    bill_id INT,
    book_id INT,
    quantity INT NOT NULL,
    unit_price DECIMAL(15, 2),
    PRIMARY KEY (bill_id, book_id),
    FOREIGN KEY (bill_id) REFERENCES bill(bill_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);

-- Vài dữ liệu mẫu để test
INSERT INTO role (role_name) VALUES ('MANAGER'), ('STAFF');
INSERT INTO employee (employee_name, role_id) VALUES ('Admin Default', 1); 
INSERT INTO account (username, password, employee_id) VALUES ('admin', 'admin', 1);