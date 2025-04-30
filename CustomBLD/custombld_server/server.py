from flask import Flask, request, jsonify
from flask_cors import CORS
import psycopg2
import os
import logging
import traceback

# Configure logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})  # More specific CORS configuration

# Database connection parameters
DB_PARAMS = {
    'dbname': os.getenv('DB_NAME', 'all_solves_db'),
    'user': os.getenv('DB_USER', 'postgres'),
    'password': os.getenv('DB_PASSWORD', 'postgres'),
    'host': os.getenv('DB_HOST', 'db'),
    'port': os.getenv('DB_PORT', '5432')
}

def get_db_connection():
    try:
        logger.debug(f"Attempting to connect to database with params: {DB_PARAMS}")
        conn = psycopg2.connect(**DB_PARAMS)
        logger.debug("Database connection successful")
        return conn
    except Exception as e:
        logger.error(f"Database connection error: {str(e)}")
        logger.error(traceback.format_exc())
        raise

def query_db(query, one=False):
    try:
        con = get_db_connection()
        cur = con.cursor()
        logger.debug(f"Executing query: {query}")
        cur.execute(query)
        rv = cur.fetchall()
        con.close()
        return (rv[0] if rv else None) if one else rv
    except Exception as e:
        logger.error(f"Query execution error: {str(e)}")
        logger.error(traceback.format_exc())
        raise

# Map frontend scramble types to database scramble types
SCRAMBLE_TYPE_MAP = {
    '3bld': '333ni',
    '4bld': '444bld',
    '4bld_centers': '444cto',
    '4bld_wings': '444edo',
    '5bld': '555bld',
    '5bld_edges_corners': '5edge',
    '3bld_corners': 'corners',
    '3bld_edges': 'edges'
}

# Add buffer mappings at the top of the file
BUFFER_MAPPINGS = {
  "corners": {
    "UBL": "A", "UBR": "B", "UFR": "C", "UFL": "D",
    "LUB": "E", "LFU": "F", "LDF": "G", "LBD": "H",
    "FUL": "I", "FRU": "J", "FDR": "K", "FLD": "L",
    "RUF": "M", "RBU": "N", "RDB": "O", "RFD": "P",
    "BUR": "Q", "BLU": "R", "BDL": "S", "BRD": "T",
    "DFL": "U", "DRF": "V", "DBR": "W", "DLB": "X"
  },
  "edges": {
    "UB": "A", "UR": "B", "UF": "C", "UL": "D",
    "LU": "E", "LF": "F", "LD": "G", "LB": "H",
    "FU": "I", "FR": "J", "FD": "K", "FL": "L",
    "RU": "M", "RB": "N", "RD": "O", "RF": "P",
    "BU": "Q", "BL": "R", "BD": "S", "BR": "T",
    "DF": "U", "DR": "V", "DB": "W", "DL": "X"
  },
   "wings": {
    "UBl": "A", "URb": "B", "UFr": "C", "ULf": "D",
    "LUf": "E", "LFd": "F", "LDb": "G", "LBu": "H",
    "FUr": "I", "FRd": "J", "FDl": "K", "FLu": "L",
    "RUb": "M", "RBd": "N", "RDf": "O", "RFu": "P",
    "BUl": "Q", "BLd": "R", "BDr": "S", "BRu": "T",
    "DFr": "U", "DRb": "V", "DBl": "W", "DLf": "X"
  },
  "xCenters": {
    "Ubl": "A", "Urb": "B", "Ufr": "C", "Ulf": "D",
    "Lub": "E", "Lfu": "F", "Ldf": "G", "Lbd": "H",
    "Ful": "I", "Fru": "J", "Fdr": "K", "Fld": "L",
    "Ruf": "M", "Rbu": "N", "Rdb": "O", "Rfd": "P",
    "Bur": "Q", "Blu": "R", "Bdl": "S", "Brd": "T",
    "Dfl": "U", "Drf": "V", "Dbr": "W", "Dlb": "X"
  },
  "tCenters": {
    "Ub": "A", "Ur": "B", "Uf": "C", "Ul": "D",
    "Lu": "E", "Lf": "F", "Ld": "G", "Lb": "H",
    "Fu": "I", "Fr": "J", "Fd": "K", "Fl": "L",
    "Ru": "M", "Rb": "N", "Rd": "O", "Rf": "P",
    "Bu": "Q", "Bl": "R", "Bd": "S", "Br": "T",
    "Df": "U", "Dr": "V", "Db": "W", "Dl": "X"
  }
}

# Add a new function to map letters from database to user's letter scheme
def map_letters(sequence, piece_type, letter_scheme):
    if not sequence:
        return ""
    
    # Create reverse mapping (letter to position)
    reverse_mapping = {}
    for pos, letter in BUFFER_MAPPINGS[piece_type].items():
        reverse_mapping[letter] = pos
    
    result = []
    for letter in sequence:
        # Skip non-letter characters
        if not letter.isalpha():
            result.append(letter)
            continue
            
        # Find the position in base mapping that corresponds to this letter
        pos = reverse_mapping.get(letter)
        if pos:
            # Get the user's letter for this position
            user_letter = letter_scheme.get(piece_type, {}).get(pos, letter)
            result.append(user_letter)
        else:
            # If position not found, keep the original letter
            result.append(letter)
   
    return ''.join(result)

@app.route('/query-scrambles', methods=['POST'])
def generate_scrambles():
    try:
        data = request.json
        logger.debug(f"Received request data: {data}")
        query_conditions = []
        args = []
        
        # Get letter scheme from request if available, otherwise use default
        letter_scheme = data.get('letterScheme', BUFFER_MAPPINGS)
        
        # Base query
        base_query = "SELECT * FROM scrambles WHERE 1=1"
        
        # Add scramble type condition with mapping
        if 'scramble_type' in data:
            frontend_type = data['scramble_type']
            db_type = SCRAMBLE_TYPE_MAP.get(frontend_type, frontend_type)
            query_conditions.append("scramble_type = ?")
            args.append(db_type)
        
        # Check for letters to practice
        # Edges letters to practice
        if 'practiceLetters' in data and 'edges' in data['practiceLetters'] and data['practiceLetters']['edges'] and len(data['practiceLetters']['edges']) > 0:
            if len(data['practiceLetters']['edges']) < 24:  # If not all letters are selected
                # Map the position names to their letter scheme values
                mapped_letters = []
                for position in data['practiceLetters']['edges']:
                    # Get the letter from standard mapping
                    letter = BUFFER_MAPPINGS['edges'].get(position)
                    if letter:
                        mapped_letters.append(letter)
                    else:
                        mapped_letters.append(position)  # Fallback to the position name
                
                if mapped_letters:
                    placeholders = ', '.join(['?'] * len(mapped_letters))
                    
                    # Rest of the code as before, but using mapped_letters
                    like_conditions = []
                    for letter in mapped_letters:
                        like_conditions.append(f"first_edges LIKE '%{letter}%'")
                        
                    query_conditions.append("(" + " OR ".join(like_conditions) + ")")
                    query_conditions.append(f"NOT EXISTS (SELECT 1 FROM (SELECT substr(first_edges, i, 1) as char FROM (SELECT first_edges), (SELECT 1 as i UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) WHERE i <= length(first_edges)) WHERE char NOT IN ({placeholders}))")
                    
                    for letter in mapped_letters:
                        args.append(letter)

        # Corners letters to practice
        if 'practiceLetters' in data and 'corners' in data['practiceLetters'] and data['practiceLetters']['corners'] and len(data['practiceLetters']['corners']) > 0:
            if len(data['practiceLetters']['corners']) < 24:  # If not all letters are selected
                # Map the position names (like "UBL") to their letter scheme values (like "A")
                mapped_letters = []
                for position in data['practiceLetters']['corners']:
                    # Get the letter from standard mapping
                    letter = BUFFER_MAPPINGS['corners'].get(position)
                    if letter:
                        mapped_letters.append(letter)
                    else:
                        mapped_letters.append(position)  # Fallback to the position name
                
                if mapped_letters:
                    placeholders = ', '.join(['?'] * len(mapped_letters))
                    
                    # Technique 1: Using LIKE for substring matching
                    like_conditions = []
                    for letter in mapped_letters:
                        like_conditions.append(f"first_corners LIKE '%{letter}%'")
                        
                    # Join with OR to ensure at least one of the practice letters is in first_corners
                    query_conditions.append("(" + " OR ".join(like_conditions) + ")")
                    
                    # Technique 2: Using a subquery to check that all characters in first_corners are in the practice list
                    query_conditions.append(f"NOT EXISTS (SELECT 1 FROM (SELECT substr(first_corners, i, 1) as char FROM (SELECT first_corners), (SELECT 1 as i UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) WHERE i <= length(first_corners)) WHERE char NOT IN ({placeholders}))")
                    
                    # Add the mapped letters as parameters for the second condition
                    for letter in mapped_letters:
                        args.append(letter)

        # Wings letters to practice
        if 'practiceLetters' in data and 'wings' in data['practiceLetters'] and data['practiceLetters']['wings'] and len(data['practiceLetters']['wings']) > 0:
            if len(data['practiceLetters']['wings']) < 24:  # If not all letters are selected
                # Map the position names to their letter scheme values
                mapped_letters = []
                for position in data['practiceLetters']['wings']:
                    # Get the letter from standard mapping
                    letter = BUFFER_MAPPINGS['wings'].get(position)
                    if letter:
                        mapped_letters.append(letter)
                    else:
                        mapped_letters.append(position)  # Fallback to the position name
                
                if mapped_letters:
                    placeholders = ', '.join(['?'] * len(mapped_letters))
                    
                    # Technique 1: Using LIKE for substring matching
                    like_conditions = []
                    for letter in mapped_letters:
                        like_conditions.append(f"first_wings LIKE '%{letter}%'")
                        
                    query_conditions.append("(" + " OR ".join(like_conditions) + ")")
                    query_conditions.append(f"NOT EXISTS (SELECT 1 FROM (SELECT substr(first_wings, i, 1) as char FROM (SELECT first_wings), (SELECT 1 as i UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20 UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24) WHERE i <= length(first_wings)) WHERE char NOT IN ({placeholders}))")
                    
                    for letter in mapped_letters:
                        args.append(letter)

        # X-Centers letters to practice
        if 'practiceLetters' in data and 'xCenters' in data['practiceLetters'] and data['practiceLetters']['xCenters'] and len(data['practiceLetters']['xCenters']) > 0:
            if len(data['practiceLetters']['xCenters']) < 24:  # If not all letters are selected
                # Map the position names to their letter scheme values
                mapped_letters = []
                for position in data['practiceLetters']['xCenters']:
                    # Get the letter from standard mapping
                    letter = BUFFER_MAPPINGS['xCenters'].get(position)
                    if letter:
                        mapped_letters.append(letter)
                    else:
                        mapped_letters.append(position)  # Fallback to the position name
                
                if mapped_letters:
                    placeholders = ', '.join(['?'] * len(mapped_letters))
                    
                    # Technique 1: Using LIKE for substring matching
                    like_conditions = []
                    for letter in mapped_letters:
                        like_conditions.append(f"first_xcenters LIKE '%{letter}%'")
                        
                    query_conditions.append("(" + " OR ".join(like_conditions) + ")")
                    query_conditions.append(f"NOT EXISTS (SELECT 1 FROM (SELECT substr(first_xcenters, i, 1) as char FROM (SELECT first_xcenters), (SELECT 1 as i UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20 UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24) WHERE i <= length(first_xcenters)) WHERE char NOT IN ({placeholders}))")
                    
                    for letter in mapped_letters:
                        args.append(letter)

        # T-Centers letters to practice
        if 'practiceLetters' in data and 'tCenters' in data['practiceLetters'] and data['practiceLetters']['tCenters'] and len(data['practiceLetters']['tCenters']) > 0:
            if len(data['practiceLetters']['tCenters']) < 24:  # If not all letters are selected
                # Map the position names to their letter scheme values
                mapped_letters = []
                for position in data['practiceLetters']['tCenters']:
                    # Get the letter from standard mapping 
                    letter = BUFFER_MAPPINGS['tCenters'].get(position)
                    if letter:
                        mapped_letters.append(letter)
                    else:
                        mapped_letters.append(position)  # Fallback to the position name
                
                if mapped_letters:
                    placeholders = ', '.join(['?'] * len(mapped_letters))
                    
                    # Technique 1: Using LIKE for substring matching
                    like_conditions = []
                    for letter in mapped_letters:
                        like_conditions.append(f"first_tcenters LIKE '%{letter}%'")
                        
                    query_conditions.append("(" + " OR ".join(like_conditions) + ")")
                    query_conditions.append(f"NOT EXISTS (SELECT 1 FROM (SELECT substr(first_tcenters, i, 1) as char FROM (SELECT first_tcenters), (SELECT 1 as i UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20 UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24) WHERE i <= length(first_tcenters)) WHERE char NOT IN ({placeholders}))")
                    
                    for letter in mapped_letters:
                        args.append(letter)

        # Edge conditions - only add if not 'random'
        if 'edge_buffer' in data:
            buffer_pos = data['edge_buffer']
            # buffer_letter = buffer_pos
            buffer_letter = BUFFER_MAPPINGS['edges'].get(buffer_pos, buffer_pos)
            query_conditions.append("edge_buffer = ?")
            args.append(buffer_letter)
        
        # Edge length range
        if 'edge_length_empty' in data and data['edge_length_empty']:
            query_conditions.append("edge_length IS NULL")
        elif 'edge_length_type' in data and data['edge_length_type'] == 'range':
            query_conditions.append("edge_length BETWEEN ? AND ?")
            args.append(data['edge_length_min'])
            args.append(data['edge_length_max'])
        
        # Edge cycle breaks range
        if 'edge_cycle_breaks_empty' in data and data['edge_cycle_breaks_empty']:
            query_conditions.append("edges_cycle_breaks IS NULL")
        elif 'edge_cycle_breaks_type' in data and data['edge_cycle_breaks_type'] == 'range':
            query_conditions.append("edges_cycle_breaks BETWEEN ? AND ?")
            args.append(data['edge_cycle_breaks_min'])
            args.append(data['edge_cycle_breaks_max'])
        
        # Edges flipped range - always use BETWEEN, even for 0-0
        if 'edges_flipped_type' in data and data['edges_flipped_type'] == 'range':
            query_conditions.append("edges_flipped BETWEEN ? AND ?")
            args.append(data['edges_flipped_min'])
            args.append(data['edges_flipped_max'])
        
        # Edges solved range - always use BETWEEN, even for 0-0
        if 'edges_solved_type' in data and data['edges_solved_type'] == 'range':
            query_conditions.append("edges_solved BETWEEN ? AND ?")
            args.append(data['edges_solved_min'])
            args.append(data['edges_solved_max'])
        
        # Edge parity
        if 'edge_parity' in data and data['edge_parity'] != 'random':
            query_conditions.append("edge_parity = ?")
            # Convert yes/no to string 'true'/'false'
            if data['edge_parity'] == 'yes':
                args.append('true')
            elif data['edge_parity'] == 'no':
                args.append('false')
            else:
                args.append(data['edge_parity'])
        
        # Corner conditions
        if 'corner_buffer' in data and data['corner_buffer'] != 'random':
            buffer_pos = data['corner_buffer']
            # buffer_letter = buffer_pos
            buffer_letter = BUFFER_MAPPINGS['corners'].get(buffer_pos, buffer_pos)
            query_conditions.append("corner_buffer = ?")
            args.append(buffer_letter)
        
        # Corner length range
        if 'corner_length_empty' in data and data['corner_length_empty']:
            query_conditions.append("corner_length IS NULL")
        elif 'corner_length_type' in data and data['corner_length_type'] == 'range':
            query_conditions.append("corner_length BETWEEN ? AND ?")
            args.append(data['corner_length_min'])
            args.append(data['corner_length_max'])
        
        # Corner cycle breaks range
        if 'corners_cycle_breaks_empty' in data and data['corners_cycle_breaks_empty']:
            query_conditions.append("corners_cycle_breaks IS NULL")
        elif 'corners_cycle_breaks_type' in data and data['corners_cycle_breaks_type'] == 'range':
            query_conditions.append("corners_cycle_breaks BETWEEN ? AND ?")
            args.append(data['corners_cycle_breaks_min'])
            args.append(data['corners_cycle_breaks_max'])
        
        # Clockwise twists range - check the length of data
        if 'corners_cw_twists_type' in data and data['corners_cw_twists_type'] == 'range':
            if 'corners_cw_twists_length' in data and data['corners_cw_twists_length']:
                # Query the length of the twist_clockwise field
                query_conditions.append("length(twist_clockwise) BETWEEN ? AND ?")
                if data['corners_cw_twists_min'] == 0:
                    args.append(0)
                else :
                    args.append(2*(data['corners_cw_twists_min']-1)+1)
                if data['corners_cw_twists_max'] == 0:
                    args.append(0)
                else:
                    args.append(2*(data['corners_cw_twists_max']-1)+1)
        

        # Counterclockwise twists range - check the length of data
        if 'corners_ccw_twists_type' in data and data['corners_ccw_twists_type'] == 'range':
            if 'corners_ccw_twists_length' in data and data['corners_ccw_twists_length']:
                # Query the length of the twist_counterclockwise field
                query_conditions.append("length(twist_counterclockwise) BETWEEN ? AND ?")
                if data['corners_ccw_twists_min'] == 0:
                    args.append(0)
                else:
                    args.append(2*(data['corners_ccw_twists_min']-1)+1)
                if data['corners_ccw_twists_max'] == 0:
                    args.append(0)
                else:
                    args.append(2*(data['corners_ccw_twists_max']-1)+1)
           

        # Corners solved range - always use BETWEEN, even for 0-0
        if 'corners_solved_type' in data and data['corners_solved_type'] == 'range':
            query_conditions.append("corners_solved BETWEEN ? AND ?")
            args.append(data['corners_solved_min'])
            args.append(data['corners_solved_max'])
        
        # Corner parity
        if 'corner_parity' in data and data['corner_parity'] != 'random':
            query_conditions.append("corner_parity = ?")
            # Convert yes/no to string 'true'/'false'
            if data['corner_parity'] == 'yes':
                args.append('true')
            elif data['corner_parity'] == 'no':
                args.append('false')
            else:
                args.append(data['corner_parity'])
        
        # Wing conditions
        if 'wing_buffer' in data and data['wing_buffer'] != 'random':
            buffer_pos = data['wing_buffer']
            buffer_letter = BUFFER_MAPPINGS['wings'].get(buffer_pos, buffer_pos)
            query_conditions.append("wing_buffer = ?")
            args.append(buffer_letter)
        
        # Wings length range
        if 'wings_length_empty' in data and data['wings_length_empty']:
            query_conditions.append("wings_length IS NULL")
        elif 'wings_length_type' in data and data['wings_length_type'] == 'range':
            query_conditions.append("wings_length BETWEEN ? AND ?")
            args.append(data['wings_length_min'])
            args.append(data['wings_length_max'])
        
        # Wings cycle breaks range
        if 'wings_cycle_breaks_empty' in data and data['wings_cycle_breaks_empty']:
            query_conditions.append("wings_cycle_breaks IS NULL")
        elif 'wings_cycle_breaks_type' in data and data['wings_cycle_breaks_type'] == 'range':
            query_conditions.append("wings_cycle_breaks BETWEEN ? AND ?")
            args.append(data['wings_cycle_breaks_min'])
            args.append(data['wings_cycle_breaks_max'])
        
        # Wings solved range - always use BETWEEN, even for 0-0
        if 'wings_solved_type' in data and data['wings_solved_type'] == 'range':
            query_conditions.append("wings_solved BETWEEN ? AND ?")
            args.append(data['wings_solved_min'])
            args.append(data['wings_solved_max'])
        
        # Wing parity
        if 'wing_parity' in data and data['wing_parity'] != 'random':
            query_conditions.append("wing_parity = ?")
            # Convert yes/no to string 'true'/'false'
            if data['wing_parity'] == 'yes':
                args.append('true')
            elif data['wing_parity'] == 'no':
                args.append('false')
            else:
                args.append(data['wing_parity'])
        
        # X-Center conditions
        if 'xcenter_buffer' in data and data['xcenter_buffer'] != 'random':
            buffer_pos = data['xcenter_buffer']
            buffer_letter = BUFFER_MAPPINGS['xCenters'].get(buffer_pos, buffer_pos)
            query_conditions.append("xcenter_buffer = ?")
            args.append(buffer_letter)
        
        # X-Center length range
        if 'x_centers_length_empty' in data and data['x_centers_length_empty']:
            query_conditions.append("xcenter_length IS NULL")
        elif 'x_centers_length_type' in data and data['x_centers_length_type'] == 'range':
            query_conditions.append("xcenter_length BETWEEN ? AND ?")
            args.append(data['x_centers_length_min'])
            args.append(data['x_centers_length_max'])
        
        # X-Center cycle breaks range
        if 'x_centers_cycle_breaks_empty' in data and data['x_centers_cycle_breaks_empty']:
            query_conditions.append("xcenters_cycle_breaks IS NULL")
        elif 'x_centers_cycle_breaks_type' in data and data['x_centers_cycle_breaks_type'] == 'range':
            query_conditions.append("xcenters_cycle_breaks BETWEEN ? AND ?")
            args.append(data['x_centers_cycle_breaks_min'])
            args.append(data['x_centers_cycle_breaks_max'])
        
        # X-Centers solved range - always use BETWEEN, even for 0-0
        if 'x_centers_solved_type' in data and data['x_centers_solved_type'] == 'range':
            query_conditions.append("xcenters_solved BETWEEN ? AND ?")
            args.append(data['x_centers_solved_min'])
            args.append(data['x_centers_solved_max'])
        
        # X-Center parity
        if 'xcenter_parity' in data and data['xcenter_parity'] != 'random':
            query_conditions.append("xcenter_parity = ?")
            # Convert yes/no to string 'true'/'false'
            if data['xcenter_parity'] == 'yes':
                args.append('true')
            elif data['xcenter_parity'] == 'no':
                args.append('false')
            else:
                args.append(data['xcenter_parity'])
        
        # T-Center conditions
        if 'tcenter_buffer' in data and data['tcenter_buffer'] != 'random':
            buffer_pos = data['tcenter_buffer']
            buffer_letter = BUFFER_MAPPINGS['tCenters'].get(buffer_pos, buffer_pos)
            query_conditions.append("tcenter_buffer = ?")
            args.append(buffer_letter)
        
        # T-Center length range
        if 't_centers_length_empty' in data and data['t_centers_length_empty']:
            query_conditions.append("tcenter_length IS NULL")
        elif 't_centers_length_type' in data and data['t_centers_length_type'] == 'range':
            query_conditions.append("tcenter_length BETWEEN ? AND ?")
            args.append(data['t_centers_length_min'])
            args.append(data['t_centers_length_max'])
        
        # T-Center cycle breaks range
        if 't_centers_cycle_breaks_empty' in data and data['t_centers_cycle_breaks_empty']:
            query_conditions.append("tcenters_cycle_breaks IS NULL")
        elif 't_centers_cycle_breaks_type' in data and data['t_centers_cycle_breaks_type'] == 'range':
            query_conditions.append("tcenters_cycle_breaks BETWEEN ? AND ?")
            args.append(data['t_centers_cycle_breaks_min'])
            args.append(data['t_centers_cycle_breaks_max'])
        
        # T-Centers solved range - always use BETWEEN, even for 0-0
        if 't_centers_solved_type' in data and data['t_centers_solved_type'] == 'range':
            query_conditions.append("tcenters_solved BETWEEN ? AND ?")
            args.append(data['t_centers_solved_min'])
            args.append(data['t_centers_solved_max'])
        
        # T-Center parity
        if 'tcenter_parity' in data and data['tcenter_parity'] != 'random':
            query_conditions.append("tcenter_parity = ?")
            # Convert yes/no to string 'true'/'false'
            if data['tcenter_parity'] == 'yes':
                args.append('true')
            elif data['tcenter_parity'] == 'no':
                args.append('false')
            else:
                args.append(data['tcenter_parity'])
        
        # Build the final query
        final_query = base_query
        if query_conditions:
            final_query += " AND " + " AND ".join(query_conditions)
        
        # Order by RANDOM() and limit results
        scramble_count = data.get('scramble_count', 1)
        # final_query += f" Order by RANDOM() LIMIT {scramble_count}"
        final_query += f" LIMIT {scramble_count}"
        
        # Print query for debugging
        # print("Executing query:")
        # print(final_query)
        # print("With parameters:", args)
        
        # Create a SQL statement with parameters already substituted for SQL viewer
        sql_for_viewer = final_query
        for param in args:
            if isinstance(param, str):
                # Replace the first ? with quoted string
                sql_for_viewer = sql_for_viewer.replace('?', f"'{param}'", 1)
            else:
                # Replace the first ? with number or boolean
                sql_for_viewer = sql_for_viewer.replace('?', str(param), 1)
        
        print("\nReady-to-use SQL query for viewer:")
        print(sql_for_viewer)
        
        # Execute query
        results = query_db(sql_for_viewer)
        
        # Return results
        scrambles = []
        solutions = []
        metadata = []
        
        if results:
            for result in results:
               
                # Add the scramble
                scrambles.append(result[2])  # Index 2 is scramble column
                
                # Add the solution if requested
                if data.get('generate_solutions') == True:
                    solutions.append(result[3] if result[3] else "No solution available")  # Index 3 is rotations_to_apply
                
                # Extract and add metadata for this scramble based on your table structure
                scramble_metadata = {
                    "id": result[0] if len(result) > 0 else None,
                    "scramble_type": result[1] if len(result) > 1 else None,
                    "scramble": result[2] if len(result) > 2 else None,
                    "rotations_to_apply": result[3] if len(result) > 3 else None,
                    "edge_buffer": result[4] if len(result) > 4 else None,
                    "edges": result[5] if len(result) > 5 else None,
                    "edge_length": result[6] if len(result) > 6 else None,
                    "edges_cycle_breaks": result[7] if len(result) > 7 else None,
                    "edges_flipped": result[8] if len(result) > 8 else None,
                    "edges_solved": result[9] if len(result) > 9 else None,
                    "flips": result[10] if len(result) > 10 else None,
                    "first_edges": result[11] if len(result) > 11 else None,
                    "corner_buffer": result[12] if len(result) > 12 else None,
                    "corners": result[13] if len(result) > 13 else None,
                    "corner_length": result[14] if len(result) > 14 else None,
                    "corners_cycle_breaks": result[15] if len(result) > 15 else None,
                    "twist_clockwise": result[16] if len(result) > 16 else None,
                    "twist_counterclockwise": result[17] if len(result) > 17 else None,
                    "corners_twisted": result[18] if len(result) > 18 else None,
                    "corners_solved": result[19] if len(result) > 19 else None,
                    "corner_parity": result[20] if len(result) > 20 else None,
                    "first_corners": result[21] if len(result) > 21 else None,
                    "wing_buffer": result[22] if len(result) > 22 else None,
                    "wings": result[23] if len(result) > 23 else None,
                    "wings_length": result[24] if len(result) > 24 else None,
                    "wings_cycle_breaks": result[25] if len(result) > 25 else None,
                    "wings_solved": result[26] if len(result) > 26 else None,
                    "wing_parity": result[27] if len(result) > 27 else None,
                    "first_wings": result[28] if len(result) > 28 else None,
                    "xcenter_buffer": result[29] if len(result) > 29 else None,
                    "xcenters": result[30] if len(result) > 30 else None,
                    "xcenter_length": result[31] if len(result) > 31 else None,
                    "xcenters_cycle_breaks": result[32] if len(result) > 32 else None,
                    "xcenters_solved": result[33] if len(result) > 33 else None,
                    "xcenter_parity": result[34] if len(result) > 34 else None,
                    "first_xcenters": result[35] if len(result) > 35 else None,
                    "tcenter_buffer": result[36] if len(result) > 36 else None,
                    "tcenters": result[37] if len(result) > 37 else None,
                    "tcenter_length": result[38] if len(result) > 38 else None,
                    "tcenters_cycle_breaks": result[39] if len(result) > 39 else None,
                    "tcenters_solved": result[40] if len(result) > 40 else None,
                    "tcenter_parity": result[41] if len(result) > 41 else None,
                    "first_tcenters": result[42] if len(result) > 42 else None
                }
                metadata.append(scramble_metadata)
        
        # Also return a debug object with the query information
        debug_info = {
            "query": final_query,
            "parameters": args,
            "sql_for_viewer": sql_for_viewer,
            "result_count": len(results) if results else 0
        }
        
        # Prepare the response
        results_to_return = []
        
        if results:
            for result in results:
                # Convert tuple to dictionary for easier access
                row = {
                    "id": result[0],
                    "scramble_type": result[1],
                    "scramble": result[2],
                    "solution": result[3],
                    "edge_buffer": result[4],
                    "edges": result[5],
                    "edge_length": result[6],
                    "edges_cycle_breaks": result[7],
                    "edges_flipped": result[8],
                    "edges_solved": result[9],
                    "flips": result[10],
                    "first_edges": result[11],
                    "corner_buffer": result[12],
                    "corners": result[13],
                    "corner_length": result[14],
                    "corners_cycle_breaks": result[15],
                    "twist_clockwise": result[16],
                    "twist_counterclockwise": result[17],
                    "corners_twisted": result[18],
                    "corners_solved": result[19],
                    "corner_parity": result[20],
                    "first_corners": result[21],
                    "wing_buffer": result[22],
                    "wings": result[23],
                    "wings_length": result[24],
                    "wings_cycle_breaks": result[25],
                    "wings_solved": result[26],
                    "wing_parity": result[27],
                    "first_wings": result[28],
                    "xcenter_buffer": result[29],
                    "xcenters": result[30],
                    "xcenter_length": result[31],
                    "xcenters_cycle_breaks": result[32],
                    "xcenters_solved": result[33],
                    "xcenter_parity": result[34],
                    "first_xcenters": result[35],
                    "tcenter_buffer": result[36],
                    "tcenters": result[37],
                    "tcenter_length": result[38],
                    "tcenters_cycle_breaks": result[39],
                    "tcenters_solved": result[40],
                    "tcenter_parity": result[41],
                    "first_tcenters": result[42] if len(result) > 42 else None
                }
                
                # Map letter sequences to the user's letter scheme
                if row.get('edges'):
                    row['edges'] = map_letters(row['edges'], 'edges', letter_scheme)
                if row.get('corners'):
                    row['corners'] = map_letters(row['corners'], 'corners', letter_scheme)
                if row.get('wings'):
                    row['wings'] = map_letters(row['wings'], 'wings', letter_scheme)
                if row.get('xcenters'):
                    row['xcenters'] = map_letters(row['xcenters'], 'xCenters', letter_scheme)
                if row.get('tcenters'):
                    row['tcenters'] = map_letters(row['tcenters'], 'tCenters', letter_scheme)
                if row.get('first_edges'):
                    row['first_edges'] = map_letters(row['first_edges'], 'edges', letter_scheme)
                if row.get('first_corners'):
                    row['first_corners'] = map_letters(row['first_corners'], 'corners', letter_scheme)
                if row.get('first_wings'):
                    row['first_wings'] = map_letters(row['first_wings'], 'wings', letter_scheme)
                if row.get('first_xcenters'):
                    row['first_xcenters'] = map_letters(row['first_xcenters'], 'xCenters', letter_scheme)
                if row.get('first_tcenters'):
                    row['first_tcenters'] = map_letters(row['first_tcenters'], 'tCenters', letter_scheme)
                
                # Map flips and twists
                if row.get('flips'):
                    row['flips'] = map_letters(row['flips'], 'edges', letter_scheme)
                if row.get('twist_clockwise'):
                    row['twist_clockwise'] = map_letters(row['twist_clockwise'], 'corners', letter_scheme)
                if row.get('twist_counterclockwise'):
                    row['twist_counterclockwise'] = map_letters(row['twist_counterclockwise'], 'corners', letter_scheme)
                    
                # Format the result for the frontend with safer access to keys
                formatted_result = {
                    'id': row['id'] or '',
                    'scramble_type': row.get('scramble_type', ''),
                    'scramble': row['scramble'] or '',
                    'solution': row['solution'] or '',
                    
                    # Edge data
                    'edge_buffer': row.get('edge_buffer', ''),
                    'edges': row.get('edges', ''),
                    'edge_length': row.get('edge_length', ''),
                    'edges_cycle_breaks': row.get('edges_cycle_breaks', ''),
                    'edges_flipped': row.get('edges_flipped', ''),
                    'edges_solved': row.get('edges_solved', ''),
                    'flips': row.get('flips', ''),
                    'first_edges': row.get('first_edges', ''),
                    'edge_parity': row.get('edge_parity', ''),
                    
                    # Corner data
                    'corner_buffer': row.get('corner_buffer', ''),
                    'corners': row.get('corners', ''),
                    'corner_length': row.get('corner_length', ''),
                    'corners_cycle_breaks': row.get('corners_cycle_breaks', ''),
                    'twist_clockwise': row.get('twist_clockwise', ''),
                    'twist_counterclockwise': row.get('twist_counterclockwise', ''),
                    'corners_twisted': row.get('corners_twisted', ''),
                    'corners_solved': row.get('corners_solved', ''),
                    'corner_parity': row.get('corner_parity', ''),
                    'first_corners': row.get('first_corners', ''),
                    
                    # Wing data
                    'wing_buffer': row.get('wing_buffer', ''),
                    'wings': row.get('wings', ''),
                    'wings_length': row.get('wings_length', ''),
                    'wings_cycle_breaks': row.get('wings_cycle_breaks', ''),
                    'wings_solved': row.get('wings_solved', ''),
                    'wing_parity': row.get('wing_parity', ''),
                    'first_wings': row.get('first_wings', ''),
                    
                    # X-Center data
                    'xcenter_buffer': row.get('xcenter_buffer', ''),
                    'xcenters': row.get('xcenters', ''),
                    'xcenter_length': row.get('xcenter_length', ''),
                    'xcenters_cycle_breaks': row.get('xcenters_cycle_breaks', ''),
                    'xcenters_solved': row.get('xcenters_solved', ''),
                    'xcenter_parity': row.get('xcenter_parity', ''),
                    'first_xcenters': row.get('first_xcenters', ''),
                    
                    # T-Center data
                    'tcenter_buffer': row.get('tcenter_buffer', ''),
                    'tcenters': row.get('tcenters', ''),
                    'tcenter_length': row.get('tcenter_length', ''),
                    'tcenters_cycle_breaks': row.get('tcenters_cycle_breaks', ''),
                    'tcenters_solved': row.get('tcenters_solved', ''),
                    'tcenter_parity': row.get('tcenter_parity', ''),
                    'first_tcenters': row.get('first_tcenters', '')
                }

                results_to_return.append(formatted_result)
        
        # print("\nFinal results:")
        for result in results_to_return:
            for key, value in result.items():
                if isinstance(value, str):
                    value = value.replace('\n', ' ').replace('\r', '')
                # print(f"{key}: {value}")
        # Return the formatted results along with debug info
        response = jsonify({
            'results': results_to_return,
            'debug': debug_info if data.get('debug') else None,
            'count': len(results_to_return)
        })
        
        # Add explicit CORS headers
        response.headers.add('Access-Control-Allow-Origin', '*')
        response.headers.add('Access-Control-Allow-Headers', 'Content-Type')
        response.headers.add('Access-Control-Allow-Methods', 'GET, POST, OPTIONS')
        
        return response
    except Exception as e:
        logger.error(f"Error in generate_scrambles: {str(e)}")
        logger.error(traceback.format_exc())
        return jsonify({
            'error': str(e),
            'traceback': traceback.format_exc()
        }), 500

# Add an OPTIONS route handler for preflight requests
@app.route('/query-scrambles', methods=['OPTIONS'])
def options():
    response = jsonify({'status': 'success'})
    response.headers.add('Access-Control-Allow-Origin', '*')
    response.headers.add('Access-Control-Allow-Headers', 'Content-Type')
    response.headers.add('Access-Control-Allow-Methods', 'GET, POST, OPTIONS')
    return response

@app.route('/health', methods=['GET'])
def health_check():
    try:
        # Test database connection
        conn = get_db_connection()
        conn.close()
        return jsonify({'status': 'healthy', 'database': 'connected'}), 200
    except Exception as e:
        logger.error(f"Health check failed: {str(e)}")
        return jsonify({'status': 'unhealthy', 'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)