# Build stage
FROM python:3.9-slim as build

WORKDIR /app

# Install build dependencies
RUN apt-get update && apt-get install -y \
    gcc \
    python3-dev \
    libpq-dev \
    && rm -rf /var/lib/apt/lists/*

# Copy requirements file
COPY requirements.txt .

# Install Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Production stage
FROM python:3.9-slim

WORKDIR /app

# Install runtime dependencies
RUN apt-get update && apt-get install -y \
    libpq5 \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copy Python dependencies from build stage
COPY --from=build /usr/local/lib/python3.9/site-packages /usr/local/lib/python3.9/site-packages

# Copy application code
COPY server.py .
COPY request_logger.py .

# Create directory for logs
RUN mkdir -p /app/db && chmod 777 /app/db

# Expose port 5000
EXPOSE 5000

# Set environment variables
ENV FLASK_APP=server.py
ENV FLASK_ENV=production

# Run the application
CMD ["python", "server.py"] 