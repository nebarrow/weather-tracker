CREATE TABLE Users (
    ID SERIAL PRIMARY KEY,
    Login VARCHAR(128) NOT NULL UNIQUE,
    Password VARCHAR (128)
);