#!/bin/bash
set -e

echo "Starting phased database restore for better performance..."

# Phase 1: Restore schema only (without data and indexes)
echo "Phase 1: Restoring schema only..."
pg_restore -v --no-owner --no-privileges --section=pre-data --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" /docker-entrypoint-initdb.d/all_solves_db.backup

# Phase 2: Restore data only (fast, without index maintenance)
echo "Phase 2: Restoring data only (without indexes)..."
pg_restore -v --no-owner --no-privileges --section=data --jobs=4 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" /docker-entrypoint-initdb.d/all_solves_db.backup

# Phase 3: Create indexes after data is loaded
echo "Phase 3: Creating indexes after data is loaded..."
pg_restore -v --no-owner --no-privileges --section=post-data --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" /docker-entrypoint-initdb.d/all_solves_db.backup

# Phase 4: Analyze database for optimal query planning
echo "Phase 4: Analyzing database tables for optimized index usage..."
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "ANALYZE scrambles;"

# Phase 5: Verify data integrity
echo "Phase 5: Verifying data integrity..."
ROWS=$(psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -t -c "SELECT COUNT(*) FROM scrambles;" 2>/dev/null || echo "0")
echo "Database contains $ROWS rows"
if [ "$ROWS" -gt 0 ]; then
  echo "Database restore completed successfully"
else
  echo "WARNING: Database restore may have failed - no rows found"
fi 