# Use Python 3.9 as base image
FROM python:3.9-slim

# Set working directory
WORKDIR /app/scramble_generation

# Install system dependencies including Java and Node.js
RUN apt-get update && apt-get install -y \
    build-essential \
    curl \
    gnupg \
    postgresql-client \
    pgloader \
    sqlite3 \
    && mkdir -p /etc/apt/keyrings \
    && curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg \
    && echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_20.x nodistro main" | tee /etc/apt/sources.list.d/nodesource.list \
    && apt-get update \
    && apt-get install -y \
    nodejs \
    && rm -rf /var/lib/apt/lists/* \
    && apt-get clean

# Install Eclipse Temurin JDK 21
RUN apt-get update && apt-get install -y wget \
    && wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | apt-key add - \
    && echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list \
    && apt-get update \
    && apt-get install -y temurin-21-jdk \
    && rm -rf /var/lib/apt/lists/* \
    && apt-get clean

# Set environment variables
ENV PYTHONUNBUFFERED=1
ENV JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64
ENV PATH=$PATH:$JAVA_HOME/bin

# Create necessary directories
RUN mkdir -p db_solves \
    && mkdir -p txt_files

# Copy requirements first to leverage Docker cache
COPY requirements.txt .

# Install Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy Node.js package files
COPY scrambles_generator/package*.json ./scrambles_generator/

# Install Node.js dependencies
RUN cd scrambles_generator && npm install

# Copy Java source files
COPY cubes/*.java ./cubes/

# Compile Java files with Java 21
RUN javac -encoding UTF-8 -source 21 -target 21 cubes/*.java

# Copy the rest of the project
COPY . .

# Create a script to run the main process and database operations
RUN echo '#!/bin/bash\n\
    python all_together_script.py "$@"\n\
    \n\
    # Wait for the script to finish\n\
    sleep 5\n\
    \n\
    # Create timestamp for backup files\n\
    TIMESTAMP=$(date +%Y%m%d_%H%M%S)\n\
    \n\
    # Dump PostgreSQL database\n\
    pg_dump -h db -U postgres -d all_solves_db > db_solves/postgres_backup_${TIMESTAMP}.sql\n\
    \n\
    # Create SQLite database using pgloader\n\
    pgloader "postgresql://postgres:postgres@db/all_solves_db" "sqlite:///db_solves/sqlite_backup_${TIMESTAMP}.db"\n\
    \n\
    echo "Database backups completed"\n\
    ' > /app/scramble_generation/run.sh && chmod +x /app/scramble_generation/run.sh

# Set the entry point to the run script
ENTRYPOINT ["/app/scramble_generation/run.sh"] 