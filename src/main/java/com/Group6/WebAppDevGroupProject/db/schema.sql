-- Create User table
CREATE TABLE User (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username TEXT NOT NULL,
    email TEXT NOT NULL,
    password TEXT NOT NULL,
    role ENUM('admin', 'customer', 'staff') NOT NULL
);

-- Create MenuItem table
CREATE TABLE MenuItem (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    item_name TEXT NOT NULL,
    item_desc TEXT,
    item_price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL
);

-- Create Order table
CREATE TABLE `Order` (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_items TEXT NOT NULL,           -- JSON format like: { "1": 1, "4": 2 }
    order_requested DATE NOT NULL,       -- When the order is placed
    order_time DATE NOT NULL,            -- When the order will be made and ready
    order_status TEXT NOT NULL DEFAULT 'ORDER SENT',
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);
