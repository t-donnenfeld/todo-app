CREATE TABLE categories
(
    id INT NOT NULL,
    category_name VARCHAR(100) NOT NULL,
    category_description VARCHAR(500),
    PRIMARY KEY(id)
);

CREATE TABLE users
(
    id INT NOT NULL,
    user_username VARCHAR(100) NOT NULL,
    user_firstname VARCHAR(100) NOT NULL,
    user_lastname VARCHAR(100) NOT NULL,
    user_email VARCHAR(100) NOT NULL,
    user_password VARCHAR(100) NOT NULL,
    user_isadmin BIT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE todos
(
    id INT PRIMARY KEY NOT NULL,
    todo_name VARCHAR(100) NOT NULL,
    todo_description VARCHAR(500),
    todo_deadline DATETIME,
    category_id INT,
    user_id INT,
    PRIMARY KEY(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);