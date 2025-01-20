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
    
    is_parity BOOLEAN,
    edges TEXT,
    edge_buffer TEXT,
    first_lp_edges_join TEXT,
    length_edges INTEGER,
    flips TEXT,
    length_flips INTEGER,
    
    
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


def insert_regular_333_bld_solves():
   import csv
import sqlite3

# Open the CSV file and insert data into the database
with open(csv_file, "r", encoding="utf-8") as file:
    csv_reader = csv.DictReader(file)
    for row in csv_reader:
        # Calculate additional derived values
        length_edges = len(row["edges"].split()) if row.get("edges") else 0
        length_flips = len(row["flip"].split()) if row.get("flip") else 0
        length_corners = len(row["corners"].split()) if row.get("corners") else 0
        sum_of_twists = len(row["Twist Clockwise"].split() + len(row["Twist Counterclockwise"].split())) if row.get("Twist Clockwise") and row.get("Twist Counterclockwise") else 0
        is_parity = True if len(row["edegs"][-2]==1) else False

        # Insert data into the database
        cursor.execute("""
        INSERT INTO scrambles (
            scramble_type, scramble, edges, edge_buffer, first_lp_edges_join, length_edges,
            flips, length_flips, corners, corner_buffer, length_corners, twist_clockwise,
            twist_counterclockwise, sum_of_twists, first_lp_corners_join, is_parity
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (
            row["scramble_type"], 
            row["scramble"],
            row["edges"],
            row["edge_buffer"],
            row["first_lp_edges_join"],
            length_edges,
            row["flip"],
            length_flips,
            row["corners"],
            row["corner_buffer"],
            length_corners,
            row["Twist Clockwise"],
            row["Twist Counterclockwise"],
            sum_of_twists,
            row["first_lp_corners_join"], 
            is_parity
        ))
    conn.commit()
    conn.close()

# Commit the transaction



def insert_edges_only_333_bld_solves():
    pass

def insert_corners_333_bld_solves():
    pass

def insert_regular_444_bld_solves():
    pass

def insert_centers_only_444_bld_solves():
    pass

def insert_wings_only_444_bld_solves():
    pass

def insert_regular_555_bld_solves():
    pass



def main():
    insert_regular_333_bld_solves()
    
if __name__ == "__main__":
    main()  