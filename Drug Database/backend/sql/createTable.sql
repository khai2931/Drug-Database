CREATE DATABASE drug_database;
USE drug_database;
CREATE TABLE User (
	loginName VARCHAR(50),
	password VARCHAR(50),
	isAdmin BOOLEAN,
    lastName VARCHAR(50),
    firstName VARCHAR(50),
    clinicName VARCHAR(50),
    email VARCHAR(50),
    phone VARCHAR(50),
    PRIMARY KEY (loginName, password)
);

CREATE TABLE DrugOrder (
	orderNumber INT,
    datetime VARCHAR(50),
    drugName VARCHAR(50),
    strength VARCHAR(50),
    form VARCHAR(50),
    quantity INT,
    otherNotes TEXT,
    PRIMARY KEY (orderNumber)
);