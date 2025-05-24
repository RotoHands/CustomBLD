#!/bin/bash
set -e

# Create logs database
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE logs_db;
EOSQL

# Connect to logs database and create tables
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "logs_db" <<-EOSQL
    CREATE TABLE IF NOT EXISTS request_logs (
        id SERIAL PRIMARY KEY,
        timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        ip_address VARCHAR(45),
        user_agent TEXT,
        request_data JSONB,
        endpoint VARCHAR(255),
        method VARCHAR(10),
        response_time INTEGER,
        status_code INTEGER,
        error_message TEXT
    );

    -- Create indexes for better query performance
    CREATE INDEX IF NOT EXISTS idx_request_logs_timestamp ON request_logs(timestamp);
    CREATE INDEX IF NOT EXISTS idx_request_logs_endpoint ON request_logs(endpoint);
    CREATE INDEX IF NOT EXISTS idx_request_logs_status_code ON request_logs(status_code);
EOSQL 