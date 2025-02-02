CREATE TABLE IF NOT EXISTS categories
(
    id INT NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL,
    category_description VARCHAR(500),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS users
(
    id INT NOT NULL AUTO_INCREMENT,
    user_username VARCHAR(100) NOT NULL,
    user_password VARCHAR(100) NOT NULL,
    user_role VARCHAR(10) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS todos
(
    id INT NOT NULL AUTO_INCREMENT,
    todo_name VARCHAR(100) NOT NULL,
    todo_description VARCHAR(500),
    todo_deadline DATETIME,
    category_id INT,
    user_id INT,
    PRIMARY KEY(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

