#!/bin/bash

# Run the main script with all arguments
python all_together_script.py "$@"

# Wait for the script to finish
sleep 5

# Create timestamp for backup files
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

# Set PostgreSQL password
export PGPASSWORD=postgres

# Dump PostgreSQL database
pg_dump -h db -U postgres -d all_solves_db > db_solves/pg_custombld_backup_${TIMESTAMP}.sql

# Create SQLite database using pgloader with type casting
pgloader --cast "column all_solves_db.* to integer" load.sql

# Unset password for security
unset PGPASSWORD

echo "Database backups completed" 