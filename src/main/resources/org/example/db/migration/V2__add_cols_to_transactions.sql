ALTER TABLE transactions
    ADD COLUMN is_locked BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE transactions
    ADD COLUMN user_id UUID DEFAULT 'd13dbc3d-c887-4c67-9493-e9db47d4d1e6';

ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE;