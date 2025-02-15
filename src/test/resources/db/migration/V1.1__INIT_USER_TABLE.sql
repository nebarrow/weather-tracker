CREATE TABLE Users (
                       ID IDENTITY PRIMARY KEY,
                       Login VARCHAR(128) NOT NULL UNIQUE,
                       Password VARCHAR (128)
);