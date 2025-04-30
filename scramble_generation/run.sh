#!/bin/bash

# Set PostgreSQL password
export PGPASSWORD=postgres

# Function to parse YAML using Python
parse_yaml() {
    python3 -c "
import yaml
with open('$1', 'r') as f:
    config = yaml.safe_load(f)
print(config.get('backup', {}).get('$2', True))
"
}

if [ "$1" = "--clean" ]; then
    echo "Cleaning database tables..."
    psql -h db -U postgres -d all_solves_db -c "TRUNCATE TABLE scrambles RESTART IDENTITY;"
    echo "Database tables cleaned"
    shift
fi
# Wait for database to be ready
echo "Waiting for database to be ready..."
until pg_isready -h db -U postgres; do
    echo "Database is unavailable - sleeping"
    sleep 1
done
echo "Database is ready!"

# Check for existing backup and restore if found
LATEST_BACKUP=$(ls -t db_solves/custombld_pg_*.pg_backup 2>/dev/null | head -n1)
RESTORE_SUCCESS=false

if [ ! -z "$LATEST_BACKUP" ]; then
    echo "Found existing backup: $LATEST_BACKUP"
    echo "Restoring database from backup..."
    
    # Attempt to restore database
    pg_restore -h db -U postgres -d all_solves_db -v --clean "$LATEST_BACKUP"
    
    # Check if restore was successful
    if [ $? -eq 0 ]; then
        echo "Database restored successfully"
        RESTORE_SUCCESS=true
        
        # Check row count in the scrambles table
        echo "Checking row count in scrambles table..."
        ROWS=$(psql -h db -U postgres -d all_solves_db -t -c "SELECT COUNT(*) FROM scrambles;")
        echo "Current number of scrambles in database: $ROWS"
    else
        echo "Database restore failed. Will proceed with empty database."
    fi
else
    echo "No backup files found. Will proceed with empty database."
fi

# Run the main script with all arguments

echo "Starting Python script to generate new scrambles..."
python all_together_script.py "$@"

# Wait for the script to finish
sleep 5

# Create timestamp for backup files
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

# Read backup configuration
BACKUP_ENABLED=$(parse_yaml "config_scramble_generation.yaml" "enabled")
SQLITE_BACKUP=$(parse_yaml "config_scramble_generation.yaml" "sqlite_backup")

if [ "$BACKUP_ENABLED" = "True" ]; then
    # Delete previous backup files if they exist
    echo "Removing previous backup files..."
    rm -f db_solves/custombld_pg_*.pg_backup
    
    # Create a new backup
    echo "Creating new PostgreSQL database backup..."
    pg_dump -h db -U postgres -d all_solves_db -F c -Z 9 -v -f db_solves/custombld_pg_${TIMESTAMP}.pg_backup

    # Verify that the new backup was created
    if [ -f "db_solves/custombld_pg_${TIMESTAMP}.pg_backup" ]; then
        echo "Backup file created successfully: db_solves/custombld_pg_${TIMESTAMP}.pg_backup"
        
        # Check final row count
        FINAL_ROWS=$(psql -h db -U postgres -d all_solves_db -t -c "SELECT COUNT(*) FROM scrambles;")
        echo "Final number of scrambles in database: $FINAL_ROWS"
        
        if [ "$RESTORE_SUCCESS" = true ] && [ ! -z "$ROWS" ]; then
            NEW_ROWS=$((FINAL_ROWS - ROWS))
            echo "Added $NEW_ROWS new scrambles to the database"
        fi
    else
        echo "Error: PostgreSQL backup file creation failed!"
    
    else
        echo "PostgreSQL backup disabled in configuration"
    fi

    if [ "$SQLITE_BACKUP" = "True" ]; then
        rm -f db_solves/custombld_sqlite_*.db
        
        # Create SQLite version
        echo "Creating SQLite backup..."
        pg_dump -h db -U postgres -d all_solves_db -v -f db_solves/custombld_pg_for_sqlite_${TIMESTAMP}.db
        sed 's/public\.//' -i db_solves/custombld_pg_for_sqlite_${TIMESTAMP}.db
        java -jar db_solves/pg2sqlite-1.1.1.jar -f true -d db_solves/custombld_pg_for_sqlite_${TIMESTAMP}.db -o db_solves/custombld_sqlite_${TIMESTAMP}.db -t text
        rm db_solves/custombld_pg_for_sqlite_${TIMESTAMP}.db
        echo "SQLite backup completed"
    else
        echo "SQLite backup disabled in configuration"
    fi
else
    echo "Backup creation disabled in configuration"
fi

unset PGPASSWORD
echo "Script completed" 