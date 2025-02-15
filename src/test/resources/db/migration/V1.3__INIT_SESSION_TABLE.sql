CREATE TABLE "SESSIONS" (
                          ID UUID PRIMARY KEY,
                          UserId INTEGER REFERENCES Users(ID),
                          ExpiresAt DATE
);