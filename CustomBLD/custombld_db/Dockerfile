FROM postgres:17-alpine

# Create directory for dump file
WORKDIR /docker-entrypoint-initdb.d

# Copy the backup file
COPY all_solves_db.backup .

# Create a phased restore script that handles schema, data, and indexes separately
RUN echo '#!/bin/bash' > /docker-entrypoint-initdb.d/restore.sh && \
    echo 'set -e' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'echo "Starting phased database restore for better performance..."' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '# Phase 1: Restore schema only (without data and indexes)' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'echo "Phase 1: Restoring schema only..."' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'pg_restore -v --no-owner --no-privileges --section=pre-data --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" /docker-entrypoint-initdb.d/all_solves_db.backup' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '# Phase 2: Restore data only (fast, without index maintenance)' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'echo "Phase 2: Restoring data only (without indexes)..."' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'pg_restore -v --no-owner --no-privileges --section=data --jobs=4 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" /docker-entrypoint-initdb.d/all_solves_db.backup' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '# Phase 3: Create indexes after data is loaded' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'echo "Phase 3: Creating indexes after data is loaded..."' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'pg_restore -v --no-owner --no-privileges --section=post-data --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" /docker-entrypoint-initdb.d/all_solves_db.backup' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '# Phase 4: Analyze database for optimal query planning' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'echo "Phase 4: Analyzing database tables for optimized index usage..."' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "ANALYZE scrambles;"' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '# Phase 5: Verify data integrity' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'echo "Phase 5: Verifying data integrity..."' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'ROWS=$(psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -t -c "SELECT COUNT(*) FROM scrambles;" 2>/dev/null || echo "0")' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'echo "Database contains $ROWS rows"' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'if [ "$ROWS" -gt 0 ]; then' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '  echo "Database restore completed successfully"' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'else' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo '  echo "WARNING: Database restore may have failed - no rows found"' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'fi' >> /docker-entrypoint-initdb.d/restore.sh && \
    chmod +x /docker-entrypoint-initdb.d/restore.sh

# Create a script to update PostgreSQL configuration for bulk loading
RUN echo '#!/bin/bash' > /docker-entrypoint-initdb.d/01-configure-postgres.sh && \
    echo 'set -e' >> /docker-entrypoint-initdb.d/01-configure-postgres.sh && \
    echo 'cat >> "$PGDATA/postgresql.conf" << EOF' >> /docker-entrypoint-initdb.d/01-configure-postgres.sh && \
    echo '# Configuration optimized for bulk loading and large database' >> /docker-entrypoint-initdb.d/01-configure-postgres.sh && \
    echo 'maintenance_work_mem = 256MB                # increase for faster index creation' >> /docker-entrypoint-initdb.d/01-configure-postgres.sh && \
    echo 'work_mem = 64MB                             # increase for complex in-memory sorts' >> /docker-entrypoint-initdb.d/01-configure-postgres.sh && \
    echo 'synchronous_commit = off                    # speeds up bulk loads (with small risk)' >> /docker-entrypoint-initdb.d/01-configure-postgres.sh && \
    echo 'max_wal_size = 2GB                          # larger checkpoints, fewer writes' >> /docker-entrypoint-initdb.d/01-configure-postgres.sh && \
    echo 'random_page_cost = 1.1                      # assumes SSD storage' >> /docker-entrypoint-initdb.d/01-configure-postgres.sh && \
    echo 'effective_cache_size = 8GB                  # assuming 16GB total RAM, adjust as needed' >> /docker-entrypoint-initdb.d/01-configure-postgres.sh && \
    echo 'EOF' >> /docker-entrypoint-initdb.d/01-configure-postgres.sh && \
    chmod +x /docker-entrypoint-initdb.d/01-configure-postgres.sh

# Set environment variables
ENV POSTGRES_DB=all_solves_db
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
# The restore script will be automatically executed by PostgreSQL's initialization scripts
