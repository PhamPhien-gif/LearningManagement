CREATE TYPE token_type AS ENUM ('ACCESS_TOKEN','REFRESH_TOKEN');
CREATE TABLE tokens (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    token TEXT UNIQUE NOT NULL,
    token_type token_type,
    revoked BOOLEAN DEFAULT FALSE,
    expired BOOLEAN DEFAULT FALSE,
    
    user_id UUID NOT NULL,
        CONSTRAINT fk_token_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE
);
CREATE INDEX idx_token_value ON tokens(token);

-- DROP TABLE IF EXISTS tokens;
