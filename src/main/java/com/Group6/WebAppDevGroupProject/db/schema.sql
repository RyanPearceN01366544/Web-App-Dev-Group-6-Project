DROP TABLE orders;
DROP TABLE users;
DROP TABLE menuitems;

-- Create User table
CREATE TABLE Users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username TEXT NOT NULL,
    email TEXT NOT NULL,
    password TEXT NOT NULL,
    role ENUM('admin', 'customer', 'staff') NOT NULL
);

-- Create MenuItem table
CREATE TABLE MenuItems (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    item_name TEXT NOT NULL,
    item_desc TEXT,
    item_price DECIMAL(10,2) NOT NULL,
    category TEXT,
    stock INT NOT NULL
);

-- Create Order table
CREATE TABLE Orders (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    order_items TEXT NOT NULL,           -- JSON format like: { "1": 1, "4": 2 }
    order_requested DATE NOT NULL,       -- When the order is placed
    order_time DATE NOT NULL,            -- When the order will be made and ready
    order_status TEXT NOT NULL DEFAULT 'ORDER SENT',
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Adding Data to Tables --
INSERT INTO users(username, email, password, role)
VALUES('Customer Example', '@example.com', 'example', 'customer');

INSERT INTO users(username, email, password, role)
VALUES('Staff Example', '@example.com', 'example', 'staff');

INSERT INTO users(username, email, password, role)
VALUES('Admin Example', '@example.com', 'example', 'admin');

INSERT INTO menuitems(item_name, item_desc, item_price, stock, category)
VALUES('Hamburger', 'A beef patty between two toasted buns.', 4.00, 24, 'Burgers');

INSERT INTO menuitems(item_name, item_desc, item_price, stock, category)
VALUES('Cheese Burger', 'A beef patty with melted cheese on top between two toasted buns.', 5.00, 20, 'Burgers');

INSERT INTO menuitems(item_name, item_desc, item_price, stock, category)
VALUES('Pizza Slice (Cheese)', 'A slice of cheese pizza (oven-cooked dough, tomato and melted cheese).', 2.50, 20, 'Pizza');

INSERT INTO menuitems(item_name, item_desc, item_price, stock, category)
VALUES('Pizza Slice (Pepperoni)', 'A slice of cheese pizza (oven-cooked dough, tomato and melted cheese) with slices of pepperoni slices on top.', 2.75, 20, 'Pizza');

INSERT INTO menuitems(item_name, item_desc, item_price, stock, category)
VALUES('French-Fries', 'A serving of fried thin potato slices.', 2.00, 30, 'Sides');

INSERT INTO menuitems(item_name, item_desc, item_price, stock, category)
VALUES('Coca-Cola', 'A cold freshing small can of Coca-Cola.', 1.00, 120, 'Drinks');