CREATE TABLE Locations (
                           ID IDENTITY PRIMARY KEY,
                           Name VARCHAR(128),
                           UserId INTEGER NOT NULL REFERENCES Users(ID),
                           Latitude DECIMAL(6, 2),
                           Longitude DECIMAL(6, 2),
                           UNIQUE (UserId, Latitude, Longitude)
);