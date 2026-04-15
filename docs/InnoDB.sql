DROP DATABASE IF EXISTS solexclusive
CREATE DATABASE IF NOT EXISTS solexclusive

-- 1.TYPE_USERS
CREATE TABLE IF NOT EXISTS type_users (
id_type INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(50) UNIQUE
);


-- 2. USERS
CREATE TABLE IF NOT EXISTS users (
id_user INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100),
email VARCHAR(100) UNIQUE,
password VARCHAR(100),
id_type INT DEFAULT 1,
FOREIGN KEY (id_type) REFERENCES type_users(id_type)
);

-- 3. BRANDS
CREATE TABLE IF NOT EXISTS brands (
id_brand INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100)
);

-- 4. TYPE_SNEAKERS
CREATE TABLE IF NOT EXISTS type_sneakers (
    id_type_sneaker INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) 
);

-- 5. SNEAKERS
CREATE TABLE IF NOT EXISTS sneakers (
id_sneaker INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100),
description VARCHAR(200),
price DECIMAL(6,2),
filePath VARCHAR(100),
id_brand INT,
id_type_sneaker INT,
FOREIGN KEY (id_brand) REFERENCES brands(id_brand),
FOREIGN KEY (id_type_sneaker) REFERENCES type_sneakers(id_type_sneaker)
);

-- 6. STOCK (tallas y cantidad)
CREATE TABLE IF NOT EXISTS stocks (
id_stock INT AUTO_INCREMENT PRIMARY KEY,
id_sneaker INT,
size DECIMAL(4,1),
quantity INT,
FOREIGN KEY (id_sneaker) REFERENCES sneakers(id_sneaker)
);

-- 7. ORDERS
CREATE TABLE IF NOT EXISTS orders (
id_order INT AUTO_INCREMENT PRIMARY KEY,
id_user INT,
date DATETIME DEFAULT CURRENT_TIMESTAMP,
total DECIMAL(10,2),
FOREIGN KEY (id_user) REFERENCES users(id_user)
);

-- 8. ORDER_ITEMS
CREATE TABLE IF NOT EXISTS order_items (
id_item INT AUTO_INCREMENT PRIMARY KEY,
id_order INT,
id_sneaker INT,
size DECIMAL(4,1),
quantity INT,
unit_price DECIMAL(10,2),
FOREIGN KEY (id_order) REFERENCES orders(id_order),
FOREIGN KEY (id_sneaker) REFERENCES sneakers(id_sneaker)
);


