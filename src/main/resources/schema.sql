CREATE TABLE menu_items(
	item_id LONG PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50),
	price NUMBER(4,2),
	spice_level NUMBER(2)
);

CREATE TABLE orders(
	order_id LONG PRIMARY KEY AUTO_INCREMENT,
	customer_name VARCHAR(50),
	food_id LONG,
	FOREIGN KEY (food_id) REFERENCES menu_items(item_id)
);

CREATE TABLE sec_user (
  userId            BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  email             VARCHAR(75) NOT NULL UNIQUE,
  encryptedPassword VARCHAR(128) NOT NULL,
  enabled           BIT NOT NULL 
);

CREATE TABLE sec_role(
  roleId   BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  roleName VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE user_role
(
  id     BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  userId BIGINT NOT NULL,
  roleId BIGINT NOT NULL
);

ALTER TABLE user_role
  ADD CONSTRAINT user_role_uk UNIQUE (userId, roleId);

ALTER TABLE user_role
  ADD CONSTRAINT user_role_fk1 FOREIGN KEY (userId)
  REFERENCES sec_user (userId);
 
ALTER TABLE user_role
  ADD CONSTRAINT user_role_fk2 FOREIGN KEY (roleId)
  REFERENCES sec_role (roleId);