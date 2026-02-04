drop database if exists bookstore_db;
create database bookstore_db character set utf8mb4 collate utf8mb4_unicode_ci;

use bookstore_db;

create table membership_rank (
	rank_id int auto_increment primary key,
    rank_name varchar(50) not null,
    min_point int default 0,
    discount_percent decimal(5, 2) default 0
);

create table role (
	role_id int auto_increment primary key,
    role_name varchar(50) not null unique
);

create table action (
	action_id int auto_increment primary key,
    action_code varchar(50) not null unique,
    action_name varchar(100) not null
);

create table category (
	category_id int auto_increment primary key,
    category_name varchar(100) unique not null
);

create table author (
	author_id int auto_increment primary key,
    author_name varchar(100) not null,
    nationality varchar(100)
);

create table supplier (
	supplier_id int auto_increment primary key,
    supplier_name varchar(100) not null,
    supplier_address varchar(255),
    supplier_phone varchar(20),
    status int default 1
);

create table payment_method (
	payment_method_id int auto_increment primary key,
    payment_method_name varchar(50) not null
);

create table employee (
	employee_id int auto_increment primary key,
    employee_name varchar(100) not null,
    employee_phone varchar(20) unique not null,
    birthday date,
    base_salary decimal(15, 0) default 0,
    salary_factor decimal(5, 2) default 1,
    day_in date,
    status bit default 1,
    role_id int not null,
    foreign key (role_id) references role(role_id)
);

create table account (
	username varchar(50) primary key,
    password varchar(255) not null,
    employee_id int not null unique,
    foreign key (employee_id) references employee(employee_id),
    status int default 1
);

create table permission (
	permission_id int auto_increment primary key,
    role_id int not null,
    action_id int not null,
    is_view bit default 1,
    is_action bit default 1,
    foreign key (role_id) references role(role_id),
    foreign key (action_id) references action(action_id)
);

create table customer (
	customer_id int auto_increment primary key,
    customer_name varchar(100) not null,
    customer_phone varchar(20) unique not null,
    point int default 0,
    rank_id int default 1,
    foreign key (rank_id) references membership_rank(rank_id)
);

create table book (
	book_id int auto_increment primary key,
    book_name varchar(255) not null,
    selling_price decimal(15, 0) default 0,
    quantity int default 0,
	translator varchar(100),
    image varchar(255),
    description text,
    status bit default 1,
    category_id int not null,
    tag_detail varchar(255),
    supplier_id int not null,
    foreign key (category_id) references category(category_id),
    foreign key (supplier_id) references supplier(supplier_id)
);

create table book_author (
	book_id int not null,
    author_id int not null,
    primary key (book_id, author_id),
    foreign key (book_id) references book(book_id),
    foreign key (author_id) references author(author_id)
);

create table discount (
	discount_id int auto_increment primary key,
    percent decimal(3, 2) not null,
    start_date datetime not null,
    end_date datetime not null,
    status int default 1,
    book_id int not null,
    foreign key (book_id) references book(book_id)
);

create table inventory_log (
	log_id int auto_increment primary key,
    action varchar(50) not null,
    change_quantity int not null,
    remain_quantity int not null,
    reference_id varchar(50),
    created_date datetime default current_timestamp,
    book_id int not null,
    foreign key (book_id) references book(book_id)
);

create table bill (
	bill_id int auto_increment primary key,
    created_date datetime default current_timestamp,
    total_bill_price decimal(15, 0),
    tax decimal(2,2) default 0,
    employee_id int not null,
    customer_id int,
    payment_method_id int not null,
    earned_points int default 0,
    foreign key (employee_id) references employee(employee_id),
    foreign key (customer_id) references customer(customer_id),
    foreign key (payment_method_id) references payment_method(payment_method_id)
);

create table bill_detail (
	bill_id int not null,
    book_id int not null,
    quantity int not null,
    unit_price decimal(15, 0),
    primary key (bill_id, book_id),
    foreign key (bill_id) references bill(bill_id) on delete cascade,
    foreign key (book_id) references book(book_id)
);

create table import_ticket (
	import_ticket_id int auto_increment primary key,
    created_date datetime default current_timestamp,
    total_import_quantity int default 0,
    total_import_price decimal(15, 0),
    status int default 1, -- 0: đã hủy, 1: chờ duyệt, 2: đã nhập
    employee_id int not null,
    supplier_id int not null,
    foreign key (employee_id) references employee(employee_id),
    foreign key (supplier_id) references supplier(supplier_id)
);

create table import_ticket_detail (
	import_ticket_id int not null,
    book_id int not null,
    import_quantity int not null,
    import_price decimal(15, 0),
    primary key (import_ticket_id, book_id),
    foreign key (import_ticket_id) references import_ticket(import_ticket_id) on delete cascade,
    foreign key (book_id) references book(book_id)
);

create table system_parameter (
    param_key varchar(50) primary key,
    param_value varchar(255) not null,
    description varchar(255)
);

create index idx_inventory_date on inventory_log(created_date);
create index idx_inventory_book on inventory_log(book_id);
create index idx_bill_date on bill(created_date);

-- Khởi tạo vài dữ liệu mẫu --
INSERT INTO system_parameter (param_key, param_value, description) VALUES
('REWARD_POINTS_PER_10K', '100', 'Tỷ lệ đổi điểm: 10.000 VNĐ = 100 điểm');

INSERT INTO membership_rank (rank_name, min_point, discount_percent) VALUES
('Thành Viên', 0, 0),
('Bạc', 2000, 5),
('Vàng', 5000, 10),
('Bạch Kim', 10000, 15);

INSERT INTO role (role_name) VALUES
('Quản Lý Cửa Hàng'),
('Nhân Viên Bán Hàng'),
('Nhân Viên Kho');

INSERT INTO payment_method (payment_method_name) VALUES
('Tiền mặt'),
('Chuyển khoản QR'),
('Thẻ tín dụng');

INSERT INTO employee (employee_name, employee_phone, birthday, base_salary, role_id) VALUES
('Lê Ngọc Quý', '0901234567', '1990-01-01', 15000000, 1),
('Nguyễn Cao Tòng Nhân', '0909999888', '2000-05-15', 7000000, 2);

INSERT INTO account (username, password, employee_id) VALUES
('admin', 'admin', 1),
('staff', 'staff', 2);

INSERT INTO category (category_name) VALUES
('Tiểu Thuyết'),
('Kinh Tế'),
('Công Nghệ Thông Tin'),
('Truyện Tranh');

INSERT INTO author (author_name, nationality) VALUES
('J.K. Rowling', 'Anh'),
('Nguyễn Nhật Ánh', 'Việt Nam'),
('Robert C. Martin', 'Mỹ');

INSERT INTO supplier (supplier_name, supplier_address, supplier_phone) VALUES
('NXB Kim Đồng', 'Hà Nội', '0241111222'),
('NXB Trẻ', 'TP.HCM', '0283333444'),
('Alpha Books', 'Hà Nội', '0905555666');

INSERT INTO book (book_name, selling_price, quantity, translator, image, description, category_id, supplier_id) VALUES
('Harry Potter và Hòn Đá Phù Thủy', 150000, 100, 'Lý Lan', 'harry_potter_1.jpg', 'Tập 1 bộ truyện Harry Potter', 1, 1),
('Mắt Biếc', 110000, 50, NULL, 'mat_biec.jpg', 'Truyện dài về tình yêu', 1, 2),
('Clean Code', 450000, 20, 'Nhiều dịch giả', 'clean_code.jpg', 'Sách gối đầu giường cho Developer', 3, 3),
('Doraemon Tập 1', 25000, 200, 'Kim Đồng', 'doraemon_1.jpg', 'Mèo máy đến từ tương lai', 4, 1);

INSERT INTO book_author (book_id, author_id) VALUES
(1, 1),
(2, 2),
(3, 3);

INSERT INTO customer (customer_name, customer_phone, point, rank_id) VALUES
('Khách Vãng Lai', '0000000000', 0, 1),
('Lê Văn Giàu', '0912345678', 3500, 3);