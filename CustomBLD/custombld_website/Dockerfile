# Build stage
FROM node:18-alpine as build

# Add build argument with default value
ARG NODE_ENV=development
ENV NODE_ENV=$NODE_ENV

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies and env-cmd
RUN npm install && npm install -g env-cmd

# Copy source code and environment files
COPY . .

# Build the application with the correct environment
RUN if [ "$NODE_ENV" = "production" ]; then \
    npm run build; \
    else \
    npm run build:dev; \
    fi

# Production stage
FROM nginx:alpine

# Copy built assets from build stage
COPY --from=build /app/build /usr/share/nginx/html

# Copy nginx configuration
# COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"] 

