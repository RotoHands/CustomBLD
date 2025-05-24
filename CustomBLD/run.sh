#!/bin/bash

# Default to development if no environment specified
ENV=${1:-dev}

# Set the environment file
export $(cat .env.$ENV | xargs)

# Run docker compose
docker compose up 