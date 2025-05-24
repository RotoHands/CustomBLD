#!/bin/bash

# Exit on error
set -e

# Configuration
APP_DIR="/var/www/custombld"
DOMAIN="your-domain.com"
EMAIL="your-email@example.com"

# Create necessary directories
sudo mkdir -p $APP_DIR

# Install required packages
sudo apt-get update
sudo apt-get install -y nginx certbot python3-certbot-nginx

# Configure Nginx
sudo tee /etc/nginx/sites-available/custombld << EOF
server {
    listen 80;
    server_name $DOMAIN;

    location / {
        proxy_pass http://localhost:5000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade \$http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host \$host;
        proxy_cache_bypass \$http_upgrade;
    }
}

server {
    listen 80;
    server_name www.$DOMAIN;
    return 301 https://$DOMAIN\$request_uri;
}
EOF

# Enable the site
sudo ln -sf /etc/nginx/sites-available/custombld /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx

# Set up SSL
sudo certbot --nginx -d $DOMAIN -d www.$DOMAIN --non-interactive --agree-tos -m $EMAIL

# Create monitoring script
sudo tee /usr/local/bin/monitor-custombld << EOF
#!/bin/bash
LOG_FILE="/var/log/custombld-monitor.log"

# Check if services are running
check_service() {
    if ! docker compose ps | grep -q "\$1.*Up"; then
        echo "\$(date): Service \$1 is down" >> \$LOG_FILE
        docker compose restart \$1
    fi
}

# Check disk space
check_disk_space() {
    DISK_USAGE=\$(df -h / | awk 'NR==2 {print \$5}' | sed 's/%//')
    if [ \$DISK_USAGE -gt 90 ]; then
        echo "\$(date): Disk usage is high: \$DISK_USAGE%" >> \$LOG_FILE
    fi
}

# Check services
check_service website
check_service server
check_service db

# Check disk space
check_disk_space
EOF

# Make monitoring script executable
sudo chmod +x /usr/local/bin/monitor-custombld

# Set up monitoring cron job
sudo tee /etc/cron.d/custombld-monitor << EOF
*/5 * * * * root /usr/local/bin/monitor-custombld
EOF

echo "Deployment completed successfully!"
echo "Please update the following in your configuration:"
echo "1. Domain name in /etc/nginx/sites-available/custombld"
echo "2. Email address for SSL notifications"
echo "3. Database credentials in .env.prd" 