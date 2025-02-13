CREATE TABLE Locations (
                           ID IDENTITY PRIMARY KEY,
                           Name VARCHAR(128),
                           UserId INTEGER NOT NULL REFERENCES Users(ID),
                           Latitude DECIMAL(5, 2),
                           Longitude DECIMAL(5, 2),
                           UNIQUE (UserId, Latitude, Longitude)
);