CREATE TYPE token_type AS ENUM ('BEARER');
CREATE TABLE tokens (
    id SERIAL PRIMARY KEY,
    token TEXT UNIQUE NOT NULL,
    token_type token_type DEFAULT 'BEARER',
    revoked BOOLEAN DEFAULT FALSE,
    expired BOOLEAN DEFAULT FALSE,
    
    user_id UUID NOT NULL,
        CONSTRAINT fk_token_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE
);
CREATE INDEX idx_token_value ON tokens(token);