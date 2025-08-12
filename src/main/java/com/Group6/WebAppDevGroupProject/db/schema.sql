CREATE TABLE IF NOT EXISTS User (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'student', 'staff') NOT NULL
);

CREATE TABLE IF NOT EXISTS MenuItem (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    item_name VARCHAR(255) NOT NULL,
    item_desc TEXT,
    item_price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    category VARCHAR(255),
    image_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS `Order` (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_items TEXT NOT NULL,
    order_requested DATETIME NOT NULL,
    order_time DATETIME NOT NULL,
    order_status VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE IF NOT EXISTS OrderAudit (
    audit_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    user_id INT,
    action VARCHAR(255),
    action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
