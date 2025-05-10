import psycopg2
import csv
import os
import glob
import argparse
import random
from psycopg2.extensions import ISOLATION_LEVEL_AUTOCOMMIT

# Database connection parameters
DB_PARAMS = {
    'dbname': 'postgres',  # Connect to default postgres database first
    'user': 'postgres',
    'password': 'postgres',
    'host': 'db',  # Use the Docker service name instead of localhost
    'port': '5432'
}

def create_database_if_not_exists():
    """Create the database if it doesn't exist"""
    try:
        # Connect to default postgres database
        print("Attempting to connect to database...")
        print(f"Connection parameters: {DB_PARAMS}")
        conn = psycopg2.connect(**DB_PARAMS)
        conn.set_isolation_level(ISOLATION_LEVEL_AUTOCOMMIT)
        cursor = conn.cursor()
        
        # Check if database exists
        cursor.execute("SELECT 1 FROM pg_database WHERE datname = 'all_solves_db'")
        exists = cursor.fetchone()
        
        if not exists:
            print("Creating database 'all_solves_db'...")
            cursor.execute('CREATE DATABASE all_solves_db')
            print("Database created successfully.")
        else:
            print("Database 'all_solves_db' already exists.")
            
        cursor.close()
        conn.close()
        
        # Update DB_PARAMS to use the new database
        DB_PARAMS['dbname'] = 'all_solves_db'
        
    except Exception as e:
        print(f"Error creating database: {e}")
        print("Please ensure the PostgreSQL container is running and accessible.")
        raise

def get_db_connection():
    try:
        return psycopg2.connect(**DB_PARAMS)
    except Exception as e:
        print(f"Error connecting to database: {e}")
        print(f"Connection parameters: {DB_PARAMS}")
        raise

def create_db():
    """Create database and tables"""
    try:
        # First ensure database exists
        create_database_if_not_exists()
        
        # Then create the table
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # Check if table exists
        cursor.execute("SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'scrambles')")
        table_exists = cursor.fetchone()[0]
        
        if not table_exists:
            print("Creating new scrambles table...")
            # Create the table
            cursor.execute("""
            CREATE TABLE scrambles (
                id SERIAL PRIMARY KEY,
                scramble_type TEXT,
                scramble TEXT NOT NULL,
                rotations_to_apply TEXT,
                random_key DOUBLE PRECISION,
                
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

            # Create indexes
            cursor.execute("""
            CREATE INDEX idx_scramble_corners_random
            ON scrambles (scramble_type, corner_buffer, random_key);

            CREATE INDEX idx_scramble_edges_random
            ON scrambles (scramble_type, edge_buffer, random_key);

            CREATE INDEX idx_scramble_corners_edges_random
            ON scrambles (scramble_type, corner_buffer, edge_buffer, random_key);

            CREATE INDEX idx_scramble_corners_wings_xcenters_random
            ON scrambles (scramble_type, corner_buffer, wing_buffer, xcenter_buffer, random_key);

            CREATE INDEX idx_scramble_xcenters_random
            ON scrambles (scramble_type, xcenter_buffer, random_key);

            CREATE INDEX idx_scramble_all_buffers_random
            ON scrambles (
                scramble_type,
                corner_buffer,
                edge_buffer,
                wing_buffer,
                xcenter_buffer,
                tcenter_buffer,
                random_key
            );

            CREATE INDEX idx_scramble_edges_wings_corners_random
            ON scrambles (scramble_type, edge_buffer, wing_buffer, corner_buffer, random_key);
            """)
            print("Table and indexes created successfully")
        else:
            print("Table 'scrambles' already exists, skipping creation")

        conn.commit()
    except Exception as e:
        print(f"Error creating database and tables: {e}")
        if conn:
            conn.rollback()
        raise
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()

def find_latest_csv_file(scramble_type_input):
    """Find the latest CSV file for the given scramble type"""
    # Get the script's directory
    script_dir = os.path.dirname(os.path.abspath(__file__))
    # Go up one level to the scramble_generation directory
    base_dir = os.path.dirname(script_dir)
    # Look in the txt_files directory
    txt_files_dir = os.path.join(base_dir, 'txt_files')
    
    print(f"Looking for CSV files in: {txt_files_dir}")
    print(f"Current directory: {os.getcwd()}")
    
    pattern = os.path.join(txt_files_dir, f"{scramble_type_input}*_solves_*.csv")
    print(f"Search pattern: {pattern}")
    
    files = glob.glob(pattern)
    if not files:
        print(f"No CSV files found matching pattern: {pattern}")
        print(f"Directory contents: {os.listdir(txt_files_dir)}")
        return None
        
    files.sort(key=os.path.getmtime, reverse=True)
    print(f"Found {len(files)} files, using: {files[0]}")
    return files[0]

def insert_333_bld_solves(scramble_type_input):
    conn = get_db_connection()
    cursor = conn.cursor()
    
    csv_file = find_latest_csv_file(scramble_type_input)
    if not csv_file:
        print("No CSV file found to insert")
        return
    
    print(f"Inserting data from: {csv_file}")
    try:
        # Open the CSV file and insert data into the database
        with open(csv_file, "r", encoding="utf-8") as file:
            csv_reader = csv.DictReader(file)
            row_count = 0
            for row in csv_reader:
                cursor.execute("""
                INSERT INTO scrambles (
                     scramble_type, scramble, rotations_to_apply, random_key,
                     edge_buffer, edges, edge_length, edges_cycle_breaks, edges_flipped, edges_solved, flips, first_edges,
                     corner_buffer, corners, corner_length, corners_cycle_breaks, twist_clockwise, twist_counterclockwise, corners_twisted, corners_solved, corner_parity, first_corners
                ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                """, (
                    row["scramble_type"],
                    row["scramble"],
                    row["rotations_to_apply"],
                    random.random(),
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
                row_count += 1
                if row_count % 1000 == 0:
                    print(f"Inserted {row_count} rows...")
                    conn.commit()
        
        conn.commit()
        print(f"Successfully inserted {row_count} rows")
    except Exception as e:
        print(f"Error inserting data: {e}")
        print("Row data:", row)  # Print the problematic row
        conn.rollback()
        raise
    finally:
        conn.close()

def insert_444_bld_solves(scramble_type_input):
    conn = get_db_connection()
    cursor = conn.cursor()
    
    csv_file = find_latest_csv_file(scramble_type_input)
    if not csv_file:
        print("No CSV file found to insert")
        return
    
    print(f"Inserting data from: {csv_file}")
    try:
        # Open the CSV file and insert data into the database
        with open(csv_file, "r", encoding="utf-8") as file:
            csv_reader = csv.DictReader(file)
            row_count = 0
            for row in csv_reader:
                cursor.execute("""
                INSERT INTO scrambles (
                     scramble_type, scramble, rotations_to_apply, random_key,
                     corner_buffer, corners, corner_length, corners_cycle_breaks, twist_clockwise, twist_counterclockwise, corners_twisted, corners_solved, corner_parity, first_corners,
                     wing_buffer, wings, wings_length, wings_cycle_breaks, wings_solved, wing_parity, first_wings,
                     xcenter_buffer, xcenters, xcenter_length, xcenters_cycle_breaks, xcenters_solved, xcenter_parity, first_xcenters 
                ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                """, (
                    row["scramble_type"],
                    row["scramble"],
                    row["rotations_to_apply"],
                    random.random(),
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
                row_count += 1
                if row_count % 1000 == 0:
                    print(f"Inserted {row_count} rows...")
                    conn.commit()
        
        conn.commit()
        print(f"Successfully inserted {row_count} rows")
    except Exception as e:
        print(f"Error inserting data: {e}")
        print("Row data:", row)  # Print the problematic row
        conn.rollback()
        raise
    finally:
        conn.close()

def insert_555_bld_solves(scramble_type_input):
    conn = get_db_connection()
    cursor = conn.cursor()
    
    csv_file = find_latest_csv_file(scramble_type_input)
    if not csv_file:
        print("No CSV file found to insert")
        return
    
    print(f"Inserting data from: {csv_file}")
    try:
        # Open the CSV file and insert data into the database
        with open(csv_file, "r", encoding="utf-8") as file:
            csv_reader = csv.DictReader(file)
            row_count = 0
            for row in csv_reader:
                cursor.execute("""
                INSERT INTO scrambles (
                     scramble_type, scramble, rotations_to_apply, random_key,
                     edge_buffer, edges, edge_length, edges_cycle_breaks, edges_flipped, edges_solved, flips, first_edges,
                     corner_buffer, corners, corner_length, corners_cycle_breaks, twist_clockwise, twist_counterclockwise, corners_twisted, corners_solved, corner_parity, first_corners,
                     wing_buffer, wings, wings_length, wings_cycle_breaks, wings_solved, wing_parity, first_wings,
                     xcenter_buffer, xcenters, xcenter_length, xcenters_cycle_breaks, xcenters_solved, xcenter_parity, first_xcenters, 
                     tcenter_buffer, tcenters, tcenter_length, tcenters_cycle_breaks, tcenters_solved, tcenter_parity, first_tcenters
                ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                """, (
                    row["scramble_type"],
                    row["scramble"],
                    row["rotations_to_apply"],
                    random.random(),
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
                row_count += 1
                if row_count % 1000 == 0:
                    print(f"Inserted {row_count} rows...")
                    conn.commit()
        
        conn.commit()
        print(f"Successfully inserted {row_count} rows")
    except Exception as e:
        print(f"Error inserting data: {e}")
        print("Row data:", row)  # Print the problematic row
        conn.rollback()
        raise
    finally:
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