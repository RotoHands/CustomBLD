import psycopg2
import csv
import os
import glob
import argparse

# Database connection parameters
DB_PARAMS = {
    'dbname': 'all_solves_db',
    'user': 'postgres',
    'password': 'postgres',
    'host': 'localhost',
    'port': '5432'
}

def get_db_connection():
    return psycopg2.connect(**DB_PARAMS)

def create_db():
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("""
    CREATE TABLE IF NOT EXISTS scrambles (
        id SERIAL PRIMARY KEY,
        scramble_type TEXT,
        scramble TEXT NOT NULL,
        rotations_to_apply TEXT,
        
        edge_buffer TEXT,
        edges TEXT,
        edge_length INTEGER,
        edges_cycle_breaks INTEGER,
        edges_flipped INTEGER,
        edges_solved INTEGER,
        flips TEXT,
        first_edges TEXT,
                
        corner_buffer TEXT,
        corners TEXT,
        corner_length INTEGER,
        corners_cycle_breaks INTEGER,
        twist_clockwise TEXT,
        twist_counterclockwise TEXT,
        corners_twisted INTEGER,
        corners_solved INTEGER,
        corner_parity BOOLEAN,
        first_corners TEXT,
                   
    
        wing_buffer TEXT,
        wings TEXT,
        wings_length INTEGER,
        wings_cycle_breaks INTEGER,
        wings_solved INTEGER,
        wing_parity BOOLEAN,
        first_wings TEXT,
                   
        xcenter_buffer TEXT,
        xcenters TEXT,
        xcenter_length INTEGER,
        xcenters_cycle_breaks INTEGER,
        xcenters_solved INTEGER,
        xcenter_parity BOOLEAN,
        first_xcenters TEXT,

        tcenter_buffer TEXT,
        tcenters TEXT,
        tcenter_length INTEGER,
        tcenters_cycle_breaks INTEGER,
        tcenters_solved INTEGER,
        tcenter_parity BOOLEAN,
        first_tcenters TEXT
    )
    """)
    print("Database and table created.")
    conn.commit()
    conn.close()

def insert_333_bld_solves(scramble_type_input):
    conn = get_db_connection()
    cursor = conn.cursor()
    files = glob.glob(os.path.join('txt_files', "{}*_solves_*.csv".format(scramble_type_input)))
    if files:
        files.sort(key=os.path.getmtime, reverse=True)
        csv_file = files[0]
   
    # Open the CSV file and insert data into the database
    with open(csv_file, "r", encoding="utf-8") as file:
        csv_reader = csv.DictReader(file)
        for row in csv_reader:
            cursor.execute("""
            INSERT INTO scrambles (
                 scramble_type, scramble, rotations_to_apply, 
                 edge_buffer, edges, edge_length, edges_cycle_breaks, edges_flipped, edges_solved, flips, first_edges,
                 corner_buffer, corners, corner_length, corners_cycle_breaks, twist_clockwise, twist_counterclockwise, corners_twisted, corners_solved, corner_parity, first_corners
            ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """, (
                row["scramble_type"],
                row["scramble"],
                row["rotations_to_apply"],
                row["edge_buffer"],
                row["edges"],
                row["edge_length"],
                row["edges_cycle_breaks"],
                row["edges_flipped"],
                row["edges_solved"],
                row["flips"],
                row["first_edges"],
                row["corner_buffer"],
                row["corners"],
                row["corner_length"],
                row["corners_cycle_breaks"],
                row["twist_clockwise"],
                row["twist_counterclockwise"],
                row["corners_twisted"],
                row["corners_solved"],
                row["corner_parity"],
                row["first_corners"]
            ))
    conn.commit()
    conn.close()

def insert_444_bld_solves(scramble_type_input):
    conn = get_db_connection()
    cursor = conn.cursor()
    files = glob.glob(os.path.join('txt_files', "{}*_solves_*.csv".format(scramble_type_input)))
    if files:
        files.sort(key=os.path.getmtime, reverse=True)
        csv_file = files[0]
   
    with open(csv_file, "r", encoding="utf-8") as file:
        csv_reader = csv.DictReader(file)
        for row in csv_reader:
            cursor.execute("""
            INSERT INTO scrambles (
                 scramble_type, scramble, rotations_to_apply, 
                 corner_buffer, corners, corner_length, corners_cycle_breaks, twist_clockwise, twist_counterclockwise, corners_twisted, corners_solved, corner_parity, first_corners,
                 wing_buffer, wings, wings_length, wings_cycle_breaks, wings_solved, wing_parity, first_wings,
                 xcenter_buffer, xcenters, xcenter_length, xcenters_cycle_breaks, xcenters_solved, xcenter_parity, first_xcenters 
            ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """, (
                row["scramble_type"],
                row["scramble"],
                row["rotations_to_apply"],
                row["corner_buffer"],
                row["corners"],
                row["corner_length"],
                row["corners_cycle_breaks"],
                row["twist_clockwise"],
                row["twist_counterclockwise"],
                row["corners_twisted"],
                row["corners_solved"],
                row["corner_parity"],
                row["first_corners"],
                row["wing_buffer"],
                row["wings"],
                row["wings_length"],
                row["wings_cycle_breaks"],
                row["wings_solved"],
                row["wing_parity"],
                row["first_wings"],
                row["xcenter_buffer"],
                row["xcenters"],
                row["xcenter_length"],
                row["xcenters_cycle_breaks"],
                row["xcenters_solved"],
                row["xcenter_parity"],
                row["first_xcenters"]
            ))
    conn.commit()
    conn.close()

def insert_555_bld_solves(scramble_type_input):
    conn = get_db_connection()
    cursor = conn.cursor()
    files = glob.glob(os.path.join('txt_files', "{}*_solves_*.csv".format(scramble_type_input)))
    if files:
        files.sort(key=os.path.getmtime, reverse=True)
        csv_file = files[0]
   
    with open(csv_file, "r", encoding="utf-8") as file:
        csv_reader = csv.DictReader(file)
        for row in csv_reader:
            cursor.execute("""
            INSERT INTO scrambles (
                 scramble_type, scramble, rotations_to_apply, 
                 edge_buffer, edges, edge_length, edges_cycle_breaks, edges_flipped, edges_solved, flips, first_edges,
                 corner_buffer, corners, corner_length, corners_cycle_breaks, twist_clockwise, twist_counterclockwise, corners_twisted, corners_solved, corner_parity, first_corners,
                 wing_buffer, wings, wings_length, wings_cycle_breaks, wings_solved, wing_parity, first_wings,
                 xcenter_buffer, xcenters, xcenter_length, xcenters_cycle_breaks, xcenters_solved, xcenter_parity, first_xcenters, 
                 tcenter_buffer, tcenters, tcenter_length, tcenters_cycle_breaks, tcenters_solved, tcenter_parity, first_tcenters
            ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """, (
                row["scramble_type"],
                row["scramble"],
                row["rotations_to_apply"],
                row["edge_buffer"],
                row["edges"],
                row["edge_length"],
                row["edges_cycle_breaks"],
                row["edges_flipped"],
                row["edges_solved"],
                row["flips"],
                row["first_edges"],
                row["corner_buffer"],
                row["corners"],
                row["corner_length"],
                row["corners_cycle_breaks"],
                row["twist_clockwise"],
                row["twist_counterclockwise"],
                row["corners_twisted"],
                row["corners_solved"],
                row["corner_parity"],
                row["first_corners"],
                row["wing_buffer"],
                row["wings"],
                row["wings_length"],
                row["wings_cycle_breaks"],
                row["wings_solved"],
                row["wing_parity"],
                row["first_wings"],
                row["xcenter_buffer"],
                row["xcenters"],
                row["xcenter_length"],
                row["xcenters_cycle_breaks"],
                row["xcenters_solved"],
                row["xcenter_parity"],
                row["first_xcenters"],
                row["tcenter_buffer"],
                row["tcenters"],
                row["tcenter_length"],
                row["tcenters_cycle_breaks"],
                row["tcenters_solved"],
                row["tcenter_parity"],
                row["first_tcenters"]
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
    if args.scramble_type  in ["444bld", "444cto", "444edo"]:
        insert_444_bld_solves(args.scramble_type)
    if args.scramble_type in ["555bld", "5edge"]:
        insert_555_bld_solves(args.scramble_type)
    
if __name__ == "__main__":
    main()  