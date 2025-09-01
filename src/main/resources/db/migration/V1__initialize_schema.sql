
CREATE TABLE client (
    client_id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NULL,
    full_name VARCHAR(255) NULL,
    phone_number VARCHAR(255) NULL,
    PRIMARY KEY (client_id)
);

CREATE TABLE `user` (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    email VARCHAR(255) NULL,
    totp_secret VARCHAR(255) NULL,
    totp_confirmed BOOLEAN NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE commodity (
    commodity_id BIGINT NOT NULL AUTO_INCREMENT,
    commodity_code VARCHAR(255) NULL,
    description VARCHAR(255) NULL,
    PRIMARY KEY (commodity_id)
);

CREATE TABLE declaration (
    declaration_id BIGINT NOT NULL AUTO_INCREMENT,
    client_id BIGINT NULL,
    `date` VARCHAR(255) NULL,
    identifier_code VARCHAR(255) NULL,
    sender_name VARCHAR(255) NULL,
    sender_address VARCHAR(255) NULL,
    sender_country_code VARCHAR(255) NULL,
    sender_phone_number VARCHAR(255) NULL,
    receiver_name VARCHAR(255) NULL,
    receiver_address VARCHAR(255) NULL,
    receiver_country_code VARCHAR(255) NULL,
    receiver_phone_number VARCHAR(255) NULL,
    total_value DOUBLE NOT NULL,
    total_weight DOUBLE NOT NULL,
    PRIMARY KEY (declaration_id),
    FOREIGN KEY (client_id) REFERENCES client(client_id)
);

CREATE TABLE package_item (
    package_item_id BIGINT NOT NULL AUTO_INCREMENT,
    commodity_id BIGINT NULL,
    declaration_id BIGINT NULL,
    net_weight DOUBLE NOT NULL,
    quantity INT NOT NULL,
    used BOOLEAN NOT NULL,
    `value` DOUBLE NOT NULL,
    PRIMARY KEY (package_item_id),
    FOREIGN KEY (commodity_id) REFERENCES commodity(commodity_id),
    FOREIGN KEY (declaration_id) REFERENCES declaration(declaration_id)
);
