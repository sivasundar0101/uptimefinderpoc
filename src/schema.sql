CREATE TABLE url (
 id INT AUTO_INCREMENT  PRIMARY KEY,
 url VARCHAR(250) NOT NULL unique,
 timeinterval INT,
 createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 
);

