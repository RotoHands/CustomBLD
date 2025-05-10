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

# Wait for database to be ready with a timeout
echo "Waiting for database to be ready..."
MAX_RETRIES=30
RETRY_COUNT=0
until pg_isready -h db -U postgres -t 5 || [ $RETRY_COUNT -ge $MAX_RETRIES ]; do
    echo "Database is unavailable - sleeping (attempt $((RETRY_COUNT+1))/$MAX_RETRIES)"
    RETRY_COUNT=$((RETRY_COUNT+1))
    sleep 2
done

if [ $RETRY_COUNT -ge $MAX_RETRIES ]; then
    echo "ERROR: Database connection timed out after $MAX_RETRIES attempts. Exiting."
    exit 1
fi

echo "Database is ready!"
# Using C locale (basic ASCII) which is most compatible across systems
psql -h db -U postgres -c "DROP DATABASE IF EXISTS all_solves_db;" || true
psql -h db -U postgres -c "CREATE DATABASE all_solves_db;"

# Check for existing backup and restore if found
LATEST_BACKUP=$(ls -t db_solves/custombld_pg_*.pg_backup 2>/dev/null | head -n1)
RESTORE_SUCCESS=false

if [ ! -z "$LATEST_BACKUP" ]; then
   
    # Phase 1: Restore schema only (without data and indexes)
    echo "Phase 1: Restoring schema only..."
    pg_restore -h db -U postgres -d all_solves_db -v --no-owner --no-acl --section=pre-data "$LATEST_BACKUP" || true
    
    # Phase 2: Restore data only (fast, without index maintenance)
    echo "Phase 2: Restoring data only (without indexes)..."
    pg_restore -h db -U postgres -d all_solves_db -v --no-owner --no-acl --section=data --jobs=4 "$LATEST_BACKUP" || true
    
    # Phase 3: Create indexes after data is loaded
    echo "Phase 3: Creating indexes after data is loaded..."
    pg_restore -h db -U postgres -d all_solves_db -v --no-owner --no-acl --section=post-data "$LATEST_BACKUP" || true
    
    # Refresh collation version
   
    # Check if restore has data
    ROWS=$(psql -h db -U postgres -d all_solves_db -t -c "SELECT COUNT(*) FROM scrambles;" 2>/dev/null || echo "0")
    echo "Rows: $ROWS"
    if  [ "$ROWS" -gt 0 ]; then
        echo "Database restored successfully with $ROWS rows"
        RESTORE_SUCCESS=true
        
        # Analyze tables to update statistics for query optimizer
        echo "Analyzing database tables for optimized index usage..."
        psql -h db -U postgres -d all_solves_db -c "ANALYZE scrambles;"
        echo "Database analysis completed"
    else
        echo "Database restore did not result in a valid table or data"        
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
    
    # Refresh collation version before backup
    # psql -h db -U postgres -d all_solves_db -c "ALTER DATABASE all_solves_db REFRESH COLLATION VERSION;" || true
    
    # Analyze tables before backup to ensure statistics are up-to-date
    echo "Analyzing database tables for optimized backup..."
    psql -h db -U postgres -d all_solves_db -c "ANALYZE scrambles;"
    
    # Create a new backup with optimized settings
    echo "Creating new PostgreSQL database backup with indexes..."
    pg_dump -h db -U postgres -d all_solves_db -F c -Z 9 -v --no-owner --no-acl --create --clean \
      -f db_solves/custombld_pg_${TIMESTAMP}.pg_backup

    sleep 5
    
    # Verify that the new backup was created
    if [ -f "db_solves/custombld_pg_${TIMESTAMP}.pg_backup" ]; then
        echo "Backup file created successfully: db_solves/custombld_pg_${TIMESTAMP}.pg_backup"
        
        # Check final row count
        FINAL_ROWS=$(psql -h db -U postgres -d all_solves_db -t -c "SELECT COUNT(*) FROM scrambles;" || echo "unknown")
        echo "Final number of scrambles in database: $FINAL_ROWS"
        
        if [ "$RESTORE_SUCCESS" = true ] && [[ "$ROWS" -gt 0 ]] && [[ "$FINAL_ROWS"  -gt 0  ]]; then
            NEW_ROWS=$((FINAL_ROWS - ROWS))
            echo "Added $NEW_ROWS new scrambles to the database"
        fi
    else
        echo "Error: PostgreSQL backup file creation failed!"
    fi
else
    echo "PostgreSQL backup disabled in configuration"
fi

if [ "$SQLITE_BACKUP" = "True" ]; then
    rm -f db_solves/custombld_sqlite_*.db
    
    # Create SQLite version
    echo "Creating SQLite backup..."
    pg_dump -h db -U postgres -d all_solves_db -v --no-owner --no-acl -f db_solves/custombld_pg_for_sqlite_${TIMESTAMP}.db
    sed 's/public\.//' -i db_solves/custombld_pg_for_sqlite_${TIMESTAMP}.db
    java -jar db_solves/pg2sqlite-1.1.1.jar -f true -d db_solves/custombld_pg_for_sqlite_${TIMESTAMP}.db -o db_solves/custombld_sqlite_${TIMESTAMP}.db -t text
    rm db_solves/custombld_pg_for_sqlite_${TIMESTAMP}.db
    echo "SQLite backup completed"
else
    echo "SQLite backup disabled in configuration"
fi

unset PGPASSWORD
echo "Script completed" 