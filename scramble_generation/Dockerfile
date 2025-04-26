# Use Python 3.9 as base image
FROM python:3.9-slim

# Set working directory
WORKDIR /app/scramble_generation

# Install system dependencies including Java and Node.js
RUN apt-get update && apt-get install -y \
    build-essential \
    curl \
    gnupg \
    && mkdir -p /etc/apt/keyrings \
    && curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg \
    && echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_20.x nodistro main" | tee /etc/apt/sources.list.d/nodesource.list \
    && apt-get update \
    && apt-get install -y \
    default-jdk \
    nodejs \
    && rm -rf /var/lib/apt/lists/* \
    && apt-get clean

# Set environment variables
ENV PYTHONUNBUFFERED=1
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
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

# Compile Java files
RUN javac -encoding UTF-8 cubes/*.java

# Copy the rest of the project
COPY . .

# Set the default command to bash
CMD ["bash"] 