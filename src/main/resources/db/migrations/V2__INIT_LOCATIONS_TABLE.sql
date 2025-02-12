CREATE TABLE Locations (
    ID SERIAL PRIMARY KEY,
    Name VARCHAR(128),
    UserId INTEGER NOT NULL REFERENCES Users(ID),
    Latitude DECIMAL(4, 2),
    Longtitude DECIMAL(4, 2)
);