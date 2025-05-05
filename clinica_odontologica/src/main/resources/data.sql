INSERT INTO roles (id_role , ROLE_TYPE_ENUM) VALUES
(1, 'DENTIST'),
(2, 'SECRETARIAT'),
(3, 'PATIENT');

INSERT INTO permissions (id_permission_entity, name_permission) VALUES
(1, 'CREATE'),
(2, 'READ'),
(3, 'UPDATE'),
(4, 'DELETE');

INSERT INTO role_permission (id_role_permission, id_role, id_permission_entity) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 2, 1),
(6, 2, 2),
(7, 2, 3),
(8, 2, 4),
(9, 3, 1),
(10,3, 2);
INSERT INTO schedules (id_schedule, star_time, time_over, working_days) VALUES
(1, '8:00', '17:00', 'MONDAY-WEDNESDAY'),
(2, '8:00', '17:00', 'TUESDAY-FRIDAY'),
(3, '8:00', '17:00', 'WEDNESDAY-SATURDAY'),
(4, '8:00', '17:00', 'THURSDAY-SATURDAY'),
(5, '8:00', '17:00', 'FRIDAY'),
(6, '8:00', '12:00', 'SATURDAY');


INSERT INTO users (id_user, username, password, is_enable, account_no_expired, account_no_locked, creadential_no_expired, role_id_user) VALUES
-- dentist123
(1, 'dentist_user', '$2a$10$J0mH998181fWFj1jtqdwQOWOLIrV8/yuq2U2x1LYZnwX2C3MKTi1q', true, true, true, true, 1),
-- secretariat123
(2, 'secretariat_user', '$2a$10$Z4jDYSg7Y.gr.sqx6.WLD./P54UyYAHET9a7Nb.64CaYPYLPOVINu',true, true, true, true, 2),
-- patientt123
(3, 'patient_user', '$2a$10$eHXLBvNKzofKlzNgmLeQCeQ9hxzElxl0MSNtFwIXSRRVkADlCGGbS',true, true, true, true, 3 );

INSERT INTO secretariats (id_person, dni, name, lastname, number_phone, address, date_of_birth, sector, id_user_secretariat) VALUES
--Date format yyyy/mm/dd
(1, '1234567890', 'Jane', 'Doe', '3003113233', 'walk Street', '1996-12-25','managnament', 2);

INSERT INTO dentists (id_person, dni, name, lastname, number_phone, address, date_of_birth, speciality,id_user_dentist, id_scheduel_dentist) VALUES
(2, '1234567891', 'Jhon', 'Doe', '3003113234', 'five aveneu', '1998-10-19', 'orthodontics', 1 ,2);

INSERT INTO patients (id_person, dni, name, lastname, number_phone, address, date_of_birth, medical_insurance_type,blood_type,id_user_patient)VALUES
(3, '1234567892', 'Jhonny', 'Doe', '3003113231', 'six aveneu', '1978-04-09', 'free', 'o+',3);

INSERT INTO responsible_per_patient(id_person, dni, name, lastname, number_phone, address, date_of_birth,relationship_type, id_patient) VALUES
(4, '1234567893', 'Jenny', 'Doe', '3003113230', 'person Street', '1978-04-09', 'father', 3);

INSERT INTO turns (id_turn, date_turn, shift_time, disease, id_turn_dentist, id_turn_patient) VALUES
(1, '2025-04-25', '13:00', 'Gingivitis', 2, 3)

