INSERT INTO Employee (nic, id, name, dob, contact, gender, profile_pic, marital_status, address, user_type, nationality, user_name, password, designation, joined_date, union_member, status, basic_salary, bank_name, account_no, branch_name, cv, birth_certificate, offer_letter, agreement_letter)
VALUES (12345678, 1, 'name 111', '1990-01-31', '133187469', 'MALE', NULL, NULL, NULL, NULL, NULL, 'abcd', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL ),
       (23412345678, 2, 'name 222', '2000-012-31', '367887469', 'FEMALE', NULL, NULL, NULL, NULL, NULL, 'abcd2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL );

INSERT INTO Payroll (id, date, name, basic_salary, overtime_payment, bonus, tax, epf, etf, union_fee)
        VALUES  (1, '2023-01-01', 'name 1', 1200, 500, 200, 10.01, 12, 9.78, 20),
                (2, '2023-01-01', 'name 2', 1500, 0, 0, 10.61, 112, 9.728, 220);
