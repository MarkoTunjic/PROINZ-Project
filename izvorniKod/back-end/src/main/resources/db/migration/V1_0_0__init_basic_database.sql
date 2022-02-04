CREATE TABLE roles
(
    id SERIAL,
    role_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE users
(
    id SERIAL,
    email VARCHAR(50) NOT NULL,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    password_hash VARCHAR (256) NOT NULL,
    date_of_birth DATE NOT NULL,
    role_id INT NOT NULL,
    username VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    UNIQUE (email,username)
);

CREATE TABLE recipes
(
    id SERIAL,
    popularity INT NOT NULL,
    title VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    last_updated_at TIMESTAMP,
    recipe_description VARCHAR(500) NOT NULL,
    estimated_time INT NOT NULL,
    created_by INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE ratings
(
    id SERIAL,
    rating_value INT NOT NULL,
    user_id INT NOT NULL,
    recipe_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id),
    CONSTRAINT valid_rating CHECK (rating_value IN (0,1,2,3,4,5))
);

CREATE TABLE ingredients
(
    id SERIAL,
    ingredient_order INT NOT NULL, --redni broj sastojka
    ingredient_name VARCHAR(25) NOT NULL, -- ime sastojka
    ingredient_measure VARCHAR(25) NOT NULL, -- ime mjere (ml, g, kg...)
    ingredient_quantity INT NOT NULL, -- kolicina sastojka
    recipe_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id)
);

CREATE TABLE images
(
    id SERIAL,
    image_data bytea,
    image_order INT NOT NULL,
    recipe_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id)
);

CREATE TABLE recipe_steps
(
    id SERIAL,
    step_order INT NOT NULL,
    step_description VARCHAR(250) NOT NULL,
    recipe_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id)
);

