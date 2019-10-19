CREATE DATABASE anna_bot;
USE anna_bot;
CREATE TABLE recipes(id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL) CHARACTER SET utf8;
CREATE TABLE products(id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255) NOT NULL UNIQUE) CHARACTER SET utf8;
CREATE TABLE ingredients(id INT PRIMARY KEY AUTO_INCREMENT, amount INT, recipe_id INT, CONSTRAINT recipe_id_fk FOREIGN KEY (recipe_id) REFERENCES recipes(id), product_id INT, CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES products(id)) CHARACTER SET utf8;
