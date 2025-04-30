FROM postgres:17-alpine

# Create directory for dump file
WORKDIR /docker-entrypoint-initdb.d

# Copy the backup file
COPY all_solves_db.backup .

# Create a restore script
RUN echo '#!/bin/bash' > /docker-entrypoint-initdb.d/restore.sh && \
    echo 'set -e' >> /docker-entrypoint-initdb.d/restore.sh && \
    echo 'pg_restore -v --clean --no-owner --no-privileges --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" /docker-entrypoint-initdb.d/all_solves_db.backup' >> /docker-entrypoint-initdb.d/restore.sh && \
    chmod +x /docker-entrypoint-initdb.d/restore.sh

# Set environment variables
ENV POSTGRES_DB=all_solves_db
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

# The backup file will be automatically executed by PostgreSQL's initialization scripts
# as it's in the /docker-entrypoint-initdb.d directory 