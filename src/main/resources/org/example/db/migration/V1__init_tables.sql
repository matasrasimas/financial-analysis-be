CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    amount NUMERIC(10,2) NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    createdAt TIMESTAMP NOT NULL
);

CREATE TYPE duration_enum AS ENUM ('MINUTES', 'HOURS', 'DAYS');

CREATE TABLE automatic_transactions (
    id UUID PRIMARY KEY,
    amount NUMERIC(10,2) NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    latest_transaction_date TIMESTAMP NOT NULL,
    duration_minutes INT NOT NULL CHECK (duration_minutes > 0),
    duration_unit duration_enum NOT NULL
);

CREATE TABLE organizations (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    code TEXT,
    address TEXT
);

CREATE TABLE org_units (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    title TEXT NOT NULL,
    code TEXT,
    address TEXT,
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    phone_number TEXT,
    email_address TEXT,
    hashed_password TEXT NOT NULL
);