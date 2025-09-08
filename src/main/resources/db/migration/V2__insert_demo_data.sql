
INSERT INTO client (email, full_name, phone_number)
VALUES
    ('janis.berzins@inbox.lv', 'Jānis Berzins', '+37144448888'),
    ('valids.ozolins@inbox.lv', 'Valdis Ozolins', '+37133338888'),
    ('karlis.kalnins@inbox.lv', 'Kārlis Kalniņš', '+37122228888');

INSERT INTO `user` (username, password, email, role, first_name, last_name)
VALUES
    ('admin', '$2a$10$4Y.gamervmN3N7kKsKSzo.ZIJKL.kbUZVbceGRk3fCvp/ylGGPDrm', 'admin@admin.lv', 'ADMIN','Jānis','Administrators'),
    ('driver', '$2a$10$4Y.gamervmN3N7kKsKSzo.ZIJKL.kbUZVbceGRk3fCvp/ylGGPDrm', 'driver@driver.lv', 'DRIVER','Pēteris','Šoferis');

INSERT INTO commodity (commodity_code, description)
VALUES
    ('0000000001', 'Table'),
    ('0000000002', 'Chair'),
    ('0000000003', 'Medicament'),
    ('0000000004', 'Tire'),
    ('0000000005', 'Window'),
    ('0000000006', 'Monitor'),
    ('0000000007', 'Boots'),
    ('0000000008', 'Bag'),
    ('0000000009', 'Used Bag'),
    ('0000000010', 'Used Boots'),
    ('0000000011', 'Used Monitor');

INSERT INTO declaration (client_id, `date`, identifier_code, sender_name, sender_address,
sender_country_code, sender_phone_number, receiver_name, receiver_address, receiver_country_code,
receiver_phone_number, total_value, total_weight, created_by, created_at)
VALUES
    (1,'20250529','1_1636SEP25','Toms Bērziņš','Ventspils iela 77','LV','+3711111111',
        'Elizabeth Oak','London street 77','UK','+449876543',191.5,27.5, 'admin', '2025-09-04 17:24:22'),
    (2,'20250528','08_2036AUG25','Pēteris Bērziņš','Rīgas iela 77','LV','+37122222222',
        'Mark Grass','Bridge street 41','UK','+442222222',1311.49,93.05, 'driver', '2025-09-04 17:24:22'),
    (3,'20250527','03_0536SEP25','John Wood','Crown street 1','UK','+4433333333',
        'Miks Bērziņš','Tukuma iela 77','LV','+37133333333',94.72,14.5, 'admin', '2025-09-04 17:24:22');

INSERT INTO package_item (commodity_id,declaration_id,net_weight,quantity,used,`value`)
VALUES
    (1,1,15.25,1,FALSE,120.50),
    (7,1,5,1,FALSE,20.50),
    (4,2,80.55,4,FALSE,1200),
    (5,2,5.25,1,FALSE,10.59),
    (8,2,7.25,2,FALSE,100.90),
    (9,1,7.25,4,TRUE,50.50),
    (10,3,7.25,1,TRUE,14.50),
    (11,3,7.25,1,TRUE,80.22);
