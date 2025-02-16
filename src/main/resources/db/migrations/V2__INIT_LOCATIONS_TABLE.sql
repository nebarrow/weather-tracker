CREATE TABLE Locations (
    ID SERIAL PRIMARY KEY,
    Name VARCHAR(128),
    UserId INTEGER NOT NULL REFERENCES Users(ID),
    Latitude DECIMAL(6, 2),
    Longtitude DECIMAL(6, 2),
    UNIQUE (UserId, Latitude, Longtitude)
);