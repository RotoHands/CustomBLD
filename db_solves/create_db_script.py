import sqlite3
import csv
import os

# File paths
csv_file = "db_solves\\all_333_solves.csv"  # Replace with your CSV file path
db_file = "db_solves\\333_solve.db"

# Create the database and table
conn = sqlite3.connect(db_file)
cursor = conn.cursor()

# Create the scrambles table with additional columns for first_edges and first_corners
cursor.execute("""DROP TABLE IF EXISTS scrambles""")
cursor.execute("""
CREATE TABLE IF NOT EXISTS scrambles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    scramble TEXT NOT NULL,
    scramble_type TEXT,
    
    edges TEXT,
    edge_buffer TEXT,
    first_lp_edges_join TEXT,
    length_edges INTEGER,
    flips TEXT,
    
    corners TEXT,
    corner_buffer TEXT,
    length_corners INTEGER,
    twist_clockwise TEXT,
    twist_counterclockwise TEXT,
    sum_of_twists INTEGER,
    first_lp_corners_join TEXT,
   
    wings TEXT,
    wings_buffer TEXT,
    first_lp_wings_join TEXT,
    wings_length INTEGER,
    
    midges TEXT,
    midges_buffer TEXT,
    first_lp_midges_join TEXT,
    midges_length INTEGER,
    
    tcenters TEXT,
    tcenters_buffer TEXT,
    first_lp_tcenters_join TEXT,
    tcenters_length INTEGER,
    
    xcenters TEXT,
    xcenters_buffer TEXT,
    first_lp_xcenters_join TEXT,
    xcenters_length INTEGER
    
)
""")
print("Database and table created.")

# Read the CSV and insert data into the database
with open(csv_file, "r", encoding="utf-8") as file:
    csv_reader = csv.DictReader(file)
    for row in csv_reader:
        cursor.execute("""
        INSERT INTO scrambles (
            scramble, edges, flip, corners, twist_clockwise, twist_counterclockwise, first_lp_edges_join, first_lp_corners_join, edge_buffer, corner_buffer
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?)
        """, (
            row["scramble"],
            row["edges"],
            row["flip"],
            row["corners"],
            row["Twist Clockwise"],
            row["Twist Counterclockwise"],
            row["first_lp_edges_join"],
            row["first_lp_corners_join"],
            row["edge_buffer"],
            row["corner_buffer"]
        ))
    print("Data inserted into the database.")

# Commit changes and close connection
conn.commit()
conn.close()
print(f"Database saved as {db_file}")
