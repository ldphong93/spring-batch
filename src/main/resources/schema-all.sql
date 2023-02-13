DROP TABLE people IF EXISTS;

CREATE TABLE people  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    birth_day VARCHAR(20),
    birth_year INT,
    zodiac_sign VARCHAR(20)
);

