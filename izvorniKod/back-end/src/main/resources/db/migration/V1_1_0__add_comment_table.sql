CREATE TABLE comments
(
    id SERIAL,
    commented_by_user INTEGER NOT NULL REFERENCES users(id),
    commented_on_recipe INTEGER NOT NULL REFERENCES recipes(id),
    comment_text VARCHAR(512) NOT NULL,
    PRIMARY KEY (id)
);
