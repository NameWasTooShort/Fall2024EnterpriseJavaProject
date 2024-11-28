INSERT INTO menu_items(name,price,spice_level) VALUES('Appetizer1', 10, 0);
INSERT INTO menu_items(name,price,spice_level) VALUES('Appetizer2', 10.99, 1);
INSERT INTO menu_items(name,price,spice_level) VALUES('Main1', 15.99, 10);
INSERT INTO menu_items(name,price,spice_level) VALUES('Main2', 10.99, 4);
INSERT INTO menu_items(name,price,spice_level) VALUES('Dessert1', 10.99, 0);
INSERT INTO menu_items(name,price,spice_level) VALUES('Dessert2', 12.99, 0);
INSERT INTO menu_items(name,price,spice_level) VALUES('Drink1', 3, 0);
INSERT INTO menu_items(name,price,spice_level) VALUES('Drink2', 2.50, 0);

INSERT INTO sec_user (email, encryptedPassword, enabled)
VALUES ('user@test.ca', '$2a$10$1ltibqiyyBJMJQ4hqM7f0OusP6np/IHshkYc4TjedwHnwwNChQZCy', 1);
INSERT INTO sec_user (email, encryptedPassword, enabled)
VALUES ('guest@test.ca', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);
INSERT INTO sec_user (email, encryptedPassword, enabled)
VALUES ('admin@test.ca', '$2a$10$1ltibqiyyBJMJQ4hqM7f0OusP6np/IHshkYc4TjedwHnwwNChQZCy', 1);

 
INSERT INTO sec_role (roleName) VALUES ('ROLE_USER');
INSERT INTO sec_role (roleName) VALUES ('ROLE_GUEST');
INSERT INTO sec_role (roleName) VALUES ('ROLE_ADMIN');

 
INSERT INTO user_role (userId, roleId) VALUES (1, 1);
INSERT INTO user_role (userId, roleId) VALUES (2, 2);
INSERT INTO user_role (userId, roleId) VALUES (3, 3);