CREATE TABLE IF NOT EXISTS Employee (
    nic VARCHAR(100) UNIQUE NOT NULL ,
    id INT PRIMARY KEY AUTO_INCREMENT ,
    name VARCHAR(200) NOT NULL ,
    dob DATE NOT NULL ,
    contact VARCHAR(200) NOT NULL ,
    gender ENUM('MALE','FEMALE') NOT NULL ,
    profile_pic MEDIUMBLOB NOT NULL ,
    marital_status ENUM('MARRIED','UNMARRIED') NOT NULL ,
    address VARCHAR(300) NOT NULL ,
    user_type ENUM('USER','ADMIN') NOT NULL ,
    nationality VARCHAR(100) NOT NULL ,


    user_name VARCHAR(100) UNIQUE NOT NULL ,
    password VARCHAR(100) NOT NULL ,
    designation ENUM('EXECUTIVE','NON-EXECUTIVE') NOT NULL ,
    joined_date DATE NOT NULL ,
    union_member BOOLEAN NOT NULL ,
    status ENUM('ACTIVE','INACTIVE') NOT NULL ,


    basic_salary INT NOT NULL ,
    bank_name VARCHAR(100) NOT NULL ,
    account_no INT NOT NULL ,
    branch_name VARCHAR(200) NOT NULL ,


    cv LONGBLOB NOT NULL ,
    birth_certificate LONGBLOB NOT NULL ,
    offer_letter LONGBLOB NOT NULL ,
    agreement_letter LONGBLOB NOT NULL
);

CREATE TABLE IF NOT EXISTS Status (
    id INT NOT NULL PRIMARY KEY ,
    resign_date DATE NOT NULL ,
    CONSTRAINT primary_key1 FOREIGN KEY (id) REFERENCES Employee (id)
);

CREATE TABLE IF NOT EXISTS Attendance (
    id INT NOT NULL ,
    date DATE NOT NULL ,
    in_time TIME NOT NULL ,
    off_time TIME NOT NULL ,
    CONSTRAINT foreign_key2 FOREIGN KEY (id) REFERENCES Employee (id),
    CONSTRAINT composite_key2 PRIMARY KEY (id,date)
);

CREATE TABLE IF NOT EXISTS Leaves (
    id INT NOT NULL ,
    leave_date DATE NOT NULL ,
    apply_date DATE NOT NULL ,
    status ENUM('APPROVED','PENDING','REJECTED') NOT NULL ,
    leave_type ENUM('SICK','OTHER') NOT NULL,
    CONSTRAINT foreign_key3 FOREIGN KEY (id) REFERENCES Employee (id),
    CONSTRAINT composite_key3 PRIMARY KEY (id,leave_date)
);

CREATE TABLE IF NOT EXISTS Leave_Description (
    id INT NOT NULL ,
    leave_date DATE NOT NULL ,
    CONSTRAINT foreign_key4 FOREIGN KEY (id,leave_date) REFERENCES Leaves (id,leave_date),
    CONSTRAINT composite_key4 PRIMARY KEY (id,leave_date)
);

CREATE TABLE IF NOT EXISTS Payroll (
    id INT NOT NULL ,
    date DATE NOT NULL ,
    name VARCHAR(200) NOT NULL ,
    basic_salary INT NOT NULL ,
    overtime_payment INT NOT NULL ,
    bonus INT NOT NULL ,
    tax DOUBLE NOT NULL ,
    epf DOUBLE NOT NULL ,
    etf DOUBLE NOT NULL ,
    union_fee DOUBLE NOT NULL ,
    CONSTRAINT foreign_key5 FOREIGN KEY (id) REFERENCES Employee (id),
    CONSTRAINT composite_key5 PRIMARY KEY (id,date)
);