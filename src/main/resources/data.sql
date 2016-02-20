INSERT INTO student(id, date_of_birth, email_address, first_name, last_name, password, user_name)
    VALUES (9001, '01-01-1970', 'admin@aaa.aa', 'adminFirstName', 'adminLastName', 'admin', 'admin');
INSERT INTO student(id, date_of_birth, email_address, first_name, last_name, password, user_name)
    VALUES (9002, '01-01-1970', 'user@aaa.aa', 'userFirstName', 'userLastName', 'user', 'user');
    
INSERT INTO user_roles(id, role_name, version, student_id)
    VALUES (9901, 'ROLE_ADMIN', 1, 9001);
INSERT INTO user_roles(id, role_name, version, student_id)
    VALUES (9902, 'ROLE_USER', 1, 9002);