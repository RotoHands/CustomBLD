import sqlite3
import csv
import os
import glob
import argparse
def create_db():
    db_file = "db_solves\\all_solves.db"
    conn = sqlite3.connect(db_file)
    cursor = conn.cursor()
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
        wing_buffer TEXT,
        first_lp_wings_join TEXT,
        length_wings INTEGER,
        is_parity_wings BOOLEAN,
        
        midges TEXT,
        midges_buffer TEXT,
        first_lp_midges_join TEXT,
        length_midges INTEGER,
        is_parity_midges BOOLEAN,
        
        xcenters TEXT,
        xcenters_buffer TEXT,
        first_lp_xcenters_join TEXT,
        length_xcenters INTEGER,
                   
        tcenters TEXT,
        tcenters_buffer TEXT,
        first_lp_tcenters_join TEXT,
        length_tcenters INTEGER
        
        
    )
    """)
    print("Database and table created.")
    conn.close()
    csv_file = "db_solves\\all_333_solves.csv"  # Replace with your CSV file path



def insert_333_bld_solves(scramble_type_input):
    db_file = "db_solves\\all_solves.db"
    conn = sqlite3.connect(db_file)
    cursor = conn.cursor()
    csv_file = glob.glob(os.path.join('txt_files', "{}*_solves_*.csv".format(scramble_type_input)))[0]

   # Open the CSV file and insert data into the database
    with open(csv_file, "r", encoding="utf-8") as file:
        csv_reader = csv.DictReader(file)
        for row in csv_reader:
            # Calculate additional derived values
            length_edges = len(row["edges"].split()) if row.get("edges") else 0
            length_flips = len(row["flip"].split()) if row.get("flip") else 0
            length_corners = len(row["corners"].split()) if row.get("corners") else 0
            sum_of_twists = len(row["Twist Clockwise"].split()) + len(row["Twist Counterclockwise"].split()) if row.get("Twist Clockwise") and row.get("Twist Counterclockwise") else 0
            is_parity = True if (len(row["corners"]) > 0 and len(row["corners"].split()[-1])==1) else False
            

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



def insert_regular_444_bld_solves():
    pass

def insert_centers_only_444_bld_solves():
    pass

def insert_wings_only_444_bld_solves():
    pass

def insert_555_bld_solves(scramble_type_input):
    db_file = "db_solves\\all_solves.db"
    conn = sqlite3.connect(db_file)
    cursor = conn.cursor()
    csv_file = glob.glob(os.path.join('txt_files', "{}*_solves_*.csv".format(scramble_type_input)))[0]
    

   # Open the CSV file and insert data into the database
    with open(csv_file, "r", encoding="utf-8") as file:
        csv_reader = csv.DictReader(file)
        for row in csv_reader:
            # Calculate additional derived values
            length_edges = len(row["edges"].split()) if row.get("edges") else 0
            length_flips = len(row["flip"].split()) if row.get("flip") else 0
            length_corners = len(row["corners"].split()) if row.get("corners") else 0
            length_wings = len(row["wings"].split()) if row.get("wings") else 0
            length_xcenters = len(row["xcenters"].split()) if row.get("xcenters") else 0
            length_tcenters = len(row["tcenters"].split()) if row.get("tcenters") else 0

            sum_of_twists = len(row["Twist Clockwise"].split()) + len(row["Twist Counterclockwise"].split()) if row.get("Twist Clockwise") and row.get("Twist Counterclockwise") else 0
            is_parity = True if len(row["corners"].split()[-1])==1 else False
            is_parity_midges = True if len(row["edges"].split()[-1])==1 else False
            is_parity_wings = True if len(row["wings"].split()[-1])==1 else False

            # Insert data into the database
            cursor.execute("""
            INSERT INTO scrambles (
                scramble_type, scramble, edges, edge_buffer, first_lp_edges_join, length_edges,
                flips, length_flips, corners, corner_buffer, length_corners, twist_clockwise,
                twist_counterclockwise, sum_of_twists, first_lp_corners_join, is_parity,
                wing_buffer, wings, length_wings, first_lp_wings_join, is_parity_wings,
                xcenters_buffer, xcenters,length_xcenters, first_lp_xcenters_join, 
                tcenters_buffer, tcenters,length_tcenters, first_lp_tcenters_join, is_parity_midges
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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
                is_parity,
                row["wing_buffer"],
                row["wings"],
                length_wings,
                row["first_lp_wings_join"],
                is_parity_wings,
                row["xcenter_buffer"],
                row["xcenters"],
                length_xcenters,
                row["first_lp_xcenters_join"],
                row["tcenter_buffer"],
                row["tcenters"],
                length_tcenters,
                row["first_lp_tcenters_join"],
                is_parity_midges

            ))
        conn.commit()
        conn.close()


def main():
    parser = argparse.ArgumentParser(description="Insert solves to db")
    parser.add_argument("scramble_type", type=str, help="The type of scrambles to generate.")
    args = parser.parse_args()

    create_db()
    if args.scramble_type in ["333ni", "edges", "corners"]:
        insert_333_bld_solves(args.scramble_type)
    if args.scramble_type == "444bld":
        insert_regular_444_bld_solves()
    if args.scramble_type == "444cto":
        insert_centers_only_444_bld_solves()
    if args.scramble_type == "444edo":  
        insert_wings_only_444_bld_solves()
    if args.scramble_type in ["555bld", "5edge"]:
        insert_555_bld_solves(args.scramble_type)
    
if __name__ == "__main__":
    main()  