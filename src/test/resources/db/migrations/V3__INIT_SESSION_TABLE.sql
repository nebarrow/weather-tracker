CREATE TABLE Sessions (
                          ID UUID PRIMARY KEY,
                          UserId INTEGER REFERENCES Users(ID),
                          ExpiresAt DATE
);