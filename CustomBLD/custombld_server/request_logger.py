import sqlite3
import json
import logging
from datetime import datetime
from typing import Dict, Any, List, Optional
import os

# Configure logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

class RequestLogger:
    def __init__(self, db_path: str = "/app/db/logs.db"):
        """Initialize the request logger with SQLite database."""
        self.db_path = db_path
        self._init_db()

    def _init_db(self) -> None:
        """Initialize the SQLite database and create the request_logs table if it doesn't exist."""
        # Ensure the logs directory exists
        os.makedirs(os.path.dirname(self.db_path), exist_ok=True)
        
        # Check if database file exists
        db_exists = os.path.exists(self.db_path)
        
        with sqlite3.connect(self.db_path) as conn:
            cursor = conn.cursor()
            
            # Only create table if database is new
            if not db_exists:
                logger.info("Creating new request_logs table...")
                cursor.execute('''
                    CREATE TABLE IF NOT EXISTS request_logs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                        ip_address TEXT,
                        user_agent TEXT,
                        request_data TEXT,
                        endpoint TEXT,
                        method TEXT,
                        response_time INTEGER,
                        status_code INTEGER,
                        error_message TEXT
                    )
                ''')
                # Create indexes for better query performance
                cursor.execute('CREATE INDEX IF NOT EXISTS idx_timestamp ON request_logs(timestamp)')
                cursor.execute('CREATE INDEX IF NOT EXISTS idx_endpoint ON request_logs(endpoint)')
                cursor.execute('CREATE INDEX IF NOT EXISTS idx_status_code ON request_logs(status_code)')
                conn.commit()
                logger.info("Request logs table and indexes created successfully")
            else:
                logger.info("Using existing logs.db file")

    def log_request(self, 
                   ip_address: str,
                   user_agent: str,
                   request_data: Dict[str, Any],
                   endpoint: str,
                   method: str,
                   response_time: int,
                   status_code: int,
                   error_message: Optional[str] = None) -> None:
        """Log a request to the SQLite database."""
        try:
            with sqlite3.connect(self.db_path) as conn:
                cursor = conn.cursor()
                cursor.execute('''
                    INSERT INTO request_logs (
                        ip_address, user_agent, request_data, endpoint, method,
                        response_time, status_code, error_message
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                ''', (
                    ip_address,
                    user_agent,
                    json.dumps(request_data),
                    endpoint,
                    method,
                    response_time,
                    status_code,
                    error_message
                ))
                conn.commit()
        except Exception as e:
            logger.error(f"Error logging request: {str(e)}")

    def get_recent_logs(self, limit: int = 100) -> List[Dict[str, Any]]:
        """Retrieve recent log entries from the database."""
        try:
            with sqlite3.connect(self.db_path) as conn:
                conn.row_factory = sqlite3.Row
                cursor = conn.cursor()
                cursor.execute('''
                    SELECT * FROM request_logs 
                    ORDER BY timestamp DESC 
                    LIMIT ?
                ''', (limit,))
                return [dict(row) for row in cursor.fetchall()]
        except Exception as e:
            logger.error(f"Error retrieving logs: {str(e)}")
            return []

    def clear_old_logs(self, days: int = 30) -> None:
        """Delete logs older than the specified number of days."""
        try:
            with sqlite3.connect(self.db_path) as conn:
                cursor = conn.cursor()
                cursor.execute('''
                    DELETE FROM request_logs 
                    WHERE timestamp < datetime('now', ?)
                ''', (f'-{days} days',))
                conn.commit()
                logger.info(f"Cleared logs older than {days} days")
        except Exception as e:
            logger.error(f"Error clearing old logs: {str(e)}")

    def get_stats(self) -> Dict[str, Any]:
        """Get statistics about the logged requests."""
        try:
            with sqlite3.connect(self.db_path) as conn:
                cursor = conn.cursor()
                stats = {}
                
                # Total requests
                cursor.execute('SELECT COUNT(*) FROM request_logs')
                stats['total_requests'] = cursor.fetchone()[0]
                
                # Requests by endpoint
                cursor.execute('''
                    SELECT endpoint, COUNT(*) as count 
                    FROM request_logs 
                    GROUP BY endpoint
                ''')
                stats['requests_by_endpoint'] = dict(cursor.fetchall())
                
                # Average response time
                cursor.execute('SELECT AVG(response_time) FROM request_logs')
                stats['avg_response_time'] = cursor.fetchone()[0] or 0
                
                # Error rate
                cursor.execute('''
                    SELECT 
                        COUNT(*) * 100.0 / (SELECT COUNT(*) FROM request_logs) as error_rate
                    FROM request_logs 
                    WHERE status_code >= 400
                ''')
                stats['error_rate'] = cursor.fetchone()[0] or 0
                
                return stats
        except Exception as e:
            logger.error(f"Error getting stats: {str(e)}")
            return {
                'total_requests': 0,
                'requests_by_endpoint': {},
                'avg_response_time': 0,
                'error_rate': 0
            }

    def search_logs(self, 
                   start_date: Optional[str] = None,
                   end_date: Optional[str] = None,
                   endpoint: Optional[str] = None,
                   status_code: Optional[int] = None,
                   ip_address: Optional[str] = None,
                   limit: int = 100) -> List[Dict[str, Any]]:
        """Search logs with various filters."""
        try:
            query = "SELECT * FROM request_logs WHERE 1=1"
            params = []

            if start_date:
                query += " AND timestamp >= ?"
                params.append(start_date)
            if end_date:
                query += " AND timestamp <= ?"
                params.append(end_date)
            if endpoint:
                query += " AND endpoint = ?"
                params.append(endpoint)
            if status_code:
                query += " AND status_code = ?"
                params.append(status_code)
            if ip_address:
                query += " AND ip_address = ?"
                params.append(ip_address)

            query += " ORDER BY timestamp DESC LIMIT ?"
            params.append(limit)

            with sqlite3.connect(self.db_path) as conn:
                conn.row_factory = sqlite3.Row
                cursor = conn.cursor()
                cursor.execute(query, params)
                return [dict(row) for row in cursor.fetchall()]
        except Exception as e:
            logger.error(f"Error searching logs: {str(e)}")
            return [] 