DROP TABLE IF EXISTS appointment;

CREATE TABLE appointment (
  id INT AUTO_INCREMENT PRIMARY KEY,
  client_name VARCHAR(250) NOT NULL,
  time TIMESTAMP NOT NULL,
  price DECIMAL(7, 2),
  status VARCHAR(6)
);
