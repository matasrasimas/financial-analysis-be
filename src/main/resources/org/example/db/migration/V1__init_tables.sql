CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    phone_number TEXT,
    email_address TEXT,
    hashed_password TEXT NOT NULL
);

CREATE TABLE organizations (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title TEXT NOT NULL,
    code TEXT,
    address TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE org_units (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    title TEXT NOT NULL,
    code TEXT,
    address TEXT,
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    org_unit_id UUID NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    created_at DATE NOT NULL,
    FOREIGN KEY (org_unit_id) REFERENCES org_units(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TYPE duration_enum AS ENUM ('MINUTES', 'HOURS', 'DAYS');

CREATE TABLE automatic_transactions (
    id UUID PRIMARY KEY,
    org_unit_id UUID NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    latest_transaction_date TIMESTAMP NOT NULL,
    duration INT NOT NULL CHECK (duration > 0),
    duration_unit duration_enum NOT NULL,
    FOREIGN KEY (org_unit_id) REFERENCES org_units(id) ON UPDATE CASCADE ON DELETE CASCADE
);