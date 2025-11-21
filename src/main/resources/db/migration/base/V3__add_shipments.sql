
CREATE TABLE shipment (
    shipment_id BIGINT NOT NULL AUTO_INCREMENT,
    shipment_code VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    created_by BIGINT NOT NULL,
    delivery_datetime DATETIME NULL,
    delivery_state VARCHAR(255) NOT NULL,
    PRIMARY KEY (shipment_id),
    FOREIGN KEY (created_by) REFERENCES user(user_id),
    INDEX shipment_code_idx (shipment_code)
);

ALTER TABLE declaration
ADD COLUMN shipment_id BIGINT NULL,
ADD FOREIGN KEY (shipment_id) REFERENCES shipment(shipment_id);
