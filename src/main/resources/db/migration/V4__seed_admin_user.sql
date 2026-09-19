INSERT INTO person (national_number, first_name, last_name, birth_day, email, gender)
VALUES ('ADMIN0001', 'System', 'Admin', '1990-01-01', 'Errachid@gmail.com', 'MALE');

INSERT INTO app_user (email, password, creation_date, user_status, role, person_id)
VALUES ('Errachid@gmail.com', '$2b$10$cJIwa167B2oE1NgkVWsRCO2TOb.w40Cl6U5794GOtnpEQOSdutcua', CURDATE(), 'ACTIVE', 'ADMIN', LAST_INSERT_ID());