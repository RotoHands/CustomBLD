from flask import Flask, request, jsonify, send_file
from flask_cors import CORS
import psycopg2
import os
import logging
import traceback
import json
import time
from pathlib import Path
import random

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

# Path for cached stats
STATS_CACHE_FILE = '/app/db_stats_cache.json'
# How long before cache expires (in seconds)
CACHE_EXPIRY = 86400  # 24 hours

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

# Reverse mapping for display
DB_TO_DISPLAY_MAP = {v: k for k, v in SCRAMBLE_TYPE_MAP.items()}

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

def cache_is_valid():
    """Check if the cache file exists and is still valid (not expired)"""
    try:
        cache_path = Path(STATS_CACHE_FILE)
        
        if not cache_path.exists():
            logger.info(f"Cache file doesn't exist at {STATS_CACHE_FILE}")
            return False
        
        # Check if file is empty
        if cache_path.stat().st_size == 0:
            logger.info(f"Cache file exists but is empty")
            return False
            
        # Check if cache has expired
        cache_age = time.time() - cache_path.stat().st_mtime
        if cache_age >= CACHE_EXPIRY:
            logger.info(f"Cache expired (age: {cache_age:.2f}s, expiry: {CACHE_EXPIRY}s)")
            return False
            
        logger.info(f"Valid cache found (age: {cache_age:.2f}s)")
        return True
    except Exception as e:
        logger.error(f"Error checking cache validity: {str(e)}")
        return False

def get_cached_stats():
    """Get stats from cache if available"""
    try:
        cache_path = Path(STATS_CACHE_FILE)
        if not cache_path.exists() or cache_path.stat().st_size == 0:
            return None
            
        with open(STATS_CACHE_FILE, 'r') as f:
            stats = json.load(f)
            logger.info(f"Retrieved stats from cache file: {STATS_CACHE_FILE}")
            
            # Add cache info for debugging
            current_time = time.time()
            file_mtime = cache_path.stat().st_mtime
            stats['cache_age'] = current_time - file_mtime
            stats['cache_expires_in'] = CACHE_EXPIRY - (current_time - file_mtime)
            stats['cache_timestamp'] = current_time
            
            return stats
    except json.JSONDecodeError as e:
        logger.error(f"Error parsing JSON from cache file: {str(e)}")
        return None
    except Exception as e:
        logger.error(f"Error reading cache: {str(e)}")
        return None

def cache_stats(stats):
    """Cache stats to a JSON file"""
    try:
        with open(STATS_CACHE_FILE, 'w') as f:
            json.dump(stats, f)
        logger.info(f"Stats cached to {STATS_CACHE_FILE}")
    except Exception as e:
        logger.error(f"Error caching stats: {str(e)}")
        logger.error(traceback.format_exc())

def generate_stats():
    """Generate stats by querying the database"""
    try:
        logger.info("Beginning database query for statistics generation")
        start_time = time.time()
        logger.debug("Generating scramble statistics from database")
        
        # Count by scramble type
        scramble_types_query = """
        SELECT scramble_type, COUNT(*) as count
        FROM scrambles
        GROUP BY scramble_type
        ORDER BY count DESC
        """
        scramble_type_stats = query_db(scramble_types_query)
        
        # Stats for 3x3 buffers (edges)
        edge_buffer_query = """
        SELECT edge_buffer, COUNT(*) as count
        FROM scrambles
        WHERE scramble_type IN ('333ni', 'edges')
        GROUP BY edge_buffer
        ORDER BY count DESC
        """
        edge_buffer_stats = query_db(edge_buffer_query)
        
        # Stats for 3x3 buffers (corners)
        corner_buffer_query = """
        SELECT corner_buffer, COUNT(*) as count
        FROM scrambles
        WHERE scramble_type IN ('333ni', 'corners')
        GROUP BY corner_buffer
        ORDER BY count DESC
        """
        corner_buffer_stats = query_db(corner_buffer_query)
        
        # Stats for 4x4 buffers (wings)
        wing_buffer_query = """
        SELECT wing_buffer, COUNT(*) as count
        FROM scrambles
        WHERE scramble_type IN ('444bld', '444edo')
        GROUP BY wing_buffer
        ORDER BY count DESC
        """
        wing_buffer_stats = query_db(wing_buffer_query)
        
        # Stats for 4x4 buffers (x-centers)
        xcenter_buffer_query = """
        SELECT xcenter_buffer, COUNT(*) as count
        FROM scrambles
        WHERE scramble_type IN ('444bld', '444cto')
        GROUP BY xcenter_buffer
        ORDER BY count DESC
        """
        xcenter_buffer_stats = query_db(xcenter_buffer_query)
        
        # Stats for 5x5 buffers (t-centers)
        tcenter_buffer_query = """
        SELECT tcenter_buffer, COUNT(*) as count
        FROM scrambles
        WHERE scramble_type = '555bld'
        GROUP BY tcenter_buffer
        ORDER BY count DESC
        """
        tcenter_buffer_stats = query_db(tcenter_buffer_query)

        # Buffer combination objects
        buffer_combinations = {}

        # 3x3 BLD (edges+corners)
        buffer_combo_3bld_query = """
        SELECT 
            edge_buffer || '-' || corner_buffer AS buffer_combo,
            COUNT(*) as count
        FROM scrambles
        WHERE scramble_type = '333ni'
          AND edge_buffer IS NOT NULL
          AND corner_buffer IS NOT NULL
        GROUP BY buffer_combo
        ORDER BY count DESC
        """
        buffer_combinations['3bld'] = [{'combo': row[0], 'count': row[1]} for row in query_db(buffer_combo_3bld_query)]
        
        # 3x3 Edges Only
        buffer_combo_3bld_edges_query = """
        SELECT 
            edge_buffer AS buffer_combo,
            COUNT(*) as count
        FROM scrambles
        WHERE scramble_type = 'edges'
          AND edge_buffer IS NOT NULL
        GROUP BY buffer_combo
        ORDER BY count DESC
        """
        buffer_combinations['3bld_edges'] = [{'combo': row[0], 'count': row[1]} for row in query_db(buffer_combo_3bld_edges_query)]
        
        # 3x3 Corners Only
        buffer_combo_3bld_corners_query = """
        SELECT 
            corner_buffer AS buffer_combo,
            COUNT(*) as count
        FROM scrambles
        WHERE scramble_type = 'corners'
          AND corner_buffer IS NOT NULL
        GROUP BY buffer_combo
        ORDER BY count DESC
        """
        buffer_combinations['3bld_corners'] = [{'combo': row[0], 'count': row[1]} for row in query_db(buffer_combo_3bld_corners_query)]
        
        # 4x4 BLD (corner+wing+xcenter)
        buffer_combo_4bld_query = """
        SELECT 
            corner_buffer || '-' || wing_buffer || '-' || xcenter_buffer AS buffer_combo,
            COUNT(*) as count
        FROM scrambles
        WHERE scramble_type = '444bld'
          AND corner_buffer IS NOT NULL
          AND wing_buffer IS NOT NULL
          AND xcenter_buffer IS NOT NULL
        GROUP BY buffer_combo
        ORDER BY count DESC
        """
        buffer_combinations['4bld'] = [{'combo': row[0], 'count': row[1]} for row in query_db(buffer_combo_4bld_query)]
        
        # 4x4 Wings Only
        buffer_combo_4bld_wings_query = """
        SELECT 
            wing_buffer AS buffer_combo,
            COUNT(*) as count
        FROM scrambles
        WHERE scramble_type = '444edo'
          AND wing_buffer IS NOT NULL
        GROUP BY buffer_combo
        ORDER BY count DESC
        """
        buffer_combinations['4bld_wings'] = [{'combo': row[0], 'count': row[1]} for row in query_db(buffer_combo_4bld_wings_query)]
        
        # 4x4 Centers Only
        buffer_combo_4bld_centers_query = """
        SELECT 
            xcenter_buffer AS buffer_combo,
            COUNT(*) as count
        FROM scrambles
        WHERE scramble_type = '444cto'
          AND xcenter_buffer IS NOT NULL
        GROUP BY buffer_combo
        ORDER BY count DESC
        """
        buffer_combinations['4bld_centers'] = [{'combo': row[0], 'count': row[1]} for row in query_db(buffer_combo_4bld_centers_query)]
        
        # 5x5 BLD (all piece types)
        buffer_combo_5bld_query = """
        SELECT 
            corner_buffer || '-' || edge_buffer || '-' || wing_buffer || '-' || tcenter_buffer || '-' || xcenter_buffer AS buffer_combo,
            COUNT(*) as count
        FROM scrambles
        WHERE scramble_type = '555bld'
          AND corner_buffer IS NOT NULL
          AND edge_buffer IS NOT NULL
          AND wing_buffer IS NOT NULL
          AND tcenter_buffer IS NOT NULL
          AND xcenter_buffer IS NOT NULL
        GROUP BY buffer_combo
        ORDER BY count DESC
        """
        buffer_combinations['5bld'] = [{'combo': row[0], 'count': row[1]} for row in query_db(buffer_combo_5bld_query)]
        
        # 5x5 Edges/Corners Only
        buffer_combo_5bld_edges_corners_query = """
        SELECT 
            corner_buffer || '-' || edge_buffer AS buffer_combo,
            COUNT(*) as count
        FROM scrambles
        WHERE scramble_type = '5edge'
          AND corner_buffer IS NOT NULL
          AND edge_buffer IS NOT NULL
        GROUP BY buffer_combo
        ORDER BY count DESC
        """
        buffer_combinations['5bld_edges_corners'] = [{'combo': row[0], 'count': row[1]} for row in query_db(buffer_combo_5bld_edges_corners_query)]
        
        # Format results for frontend
        stats = {
            'scramble_types': [
                {'type': DB_TO_DISPLAY_MAP.get(row[0], row[0]), 'db_type': row[0], 'count': row[1]}
                for row in scramble_type_stats
            ],
            'buffer_stats': {
                'edges': [{'buffer': row[0], 'count': row[1]} for row in edge_buffer_stats],
                'corners': [{'buffer': row[0], 'count': row[1]} for row in corner_buffer_stats],
                'wings': [{'buffer': row[0], 'count': row[1]} for row in wing_buffer_stats],
                'xcenters': [{'buffer': row[0], 'count': row[1]} for row in xcenter_buffer_stats],
                'tcenters': [{'buffer': row[0], 'count': row[1]} for row in tcenter_buffer_stats]
            },
            'buffer_combinations': buffer_combinations
        }
        
        # Add total counts
        total_count_query = "SELECT COUNT(*) FROM scrambles"
        total_count = query_db(total_count_query, one=True)[0]
        stats['total_scrambles'] = total_count
        
        # Add timestamp and query time
        stats['timestamp'] = time.time()
        stats['query_time'] = time.time() - start_time
        
        # Cache the stats
        cache_stats(stats)
        
        logger.info(f"Statistics generation complete in {stats['query_time']:.2f} seconds")
        return stats
        
    except Exception as e:
        logger.error(f"Error generating stats: {str(e)}")
        logger.error(traceback.format_exc())
        raise

def get_stats():
    """Get stats from cache if valid, otherwise generate new stats"""
    if cache_is_valid():
        logger.info("Cache is valid and not expired, using cached stats")
        cached_stats = get_cached_stats()
        if cached_stats:
            return cached_stats
    
    logger.info("Cache is invalid or expired (> 24 hours old), generating fresh stats")
    return generate_stats()

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
        
        # Order by random_key instead of using RANDOM() function
        scramble_count = data.get('scramble_count', 1)
        
        # Generate a random value between 0 and 1
        random_value = random.random()
        
        # Extract the filters being used
        has_scramble_type = 'scramble_type' in data
        has_corner_buffer = bool(data.get('corner_buffer') and data.get('corner_buffer') != 'random')
        has_edge_buffer = bool(data.get('edge_buffer') and data.get('edge_buffer') != 'random')
        has_wing_buffer = bool(data.get('wing_buffer') and data.get('wing_buffer') != 'random')
        has_xcenter_buffer = bool(data.get('xcenter_buffer') and data.get('xcenter_buffer') != 'random')
        has_tcenter_buffer = bool(data.get('tcenter_buffer') and data.get('tcenter_buffer') != 'random')
        
        # Log which columns are filtered
        logger.debug(f"Query filters - scramble_type: {has_scramble_type}, corner: {has_corner_buffer}, " +
                    f"edge: {has_edge_buffer}, wing: {has_wing_buffer}, xcenter: {has_xcenter_buffer}, " + 
                    f"tcenter: {has_tcenter_buffer}")
        
        # Identify which index to use based on column order in indexes
        # This ensures we respect the index column order
        index_used = None
        
        # Check for matches with indexes in order of specificity
        if has_scramble_type and has_corner_buffer and has_edge_buffer and has_wing_buffer and has_xcenter_buffer and has_tcenter_buffer:
            index_used = "idx_scramble_all_buffers_random"
        elif has_scramble_type and has_corner_buffer and has_wing_buffer and has_xcenter_buffer:
            index_used = "idx_scramble_corners_wings_xcenters_random"
        elif has_scramble_type and has_edge_buffer and has_wing_buffer and has_corner_buffer:
            index_used = "idx_scramble_edges_wings_corners_random"
        elif has_scramble_type and has_corner_buffer and has_edge_buffer:
            index_used = "idx_scramble_corners_edges_random"
        elif has_scramble_type and has_corner_buffer:
            index_used = "idx_scramble_corners_random"
        elif has_scramble_type and has_edge_buffer:
            index_used = "idx_scramble_edges_random"
        elif has_scramble_type and has_xcenter_buffer:
            index_used = "idx_scramble_xcenters_random"
        else:
            index_used = "generic_random_key"
            
        # Only log the index that would likely be used (but don't add hints to the query)
        logger.info(f"Query pattern matches index: {index_used}")
        
        # Simple approach: just use random_key for efficient random selection
        # Let PostgreSQL's query planner choose the appropriate index
        final_query = "SELECT * FROM scrambles WHERE 1=1"
        
        if query_conditions:
            final_query += " AND " + " AND ".join(query_conditions)
            
        # Add the random filtering and limit
        final_query += f" AND random_key >= {random_value} ORDER BY random_key ASC LIMIT {scramble_count}"
        
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
                    "random_key": result[4] if len(result) > 4 else None,
                    "edge_buffer": result[5] if len(result) > 5 else None,
                    "edges": result[6] if len(result) > 6 else None,
                    "edge_length": result[7] if len(result) > 7 else None,
                    "edges_cycle_breaks": result[8] if len(result) > 8 else None,
                    "edges_flipped": result[9] if len(result) > 9 else None,
                    "edges_solved": result[10] if len(result) > 10 else None,
                    "flips": result[11] if len(result) > 11 else None,
                    "first_edges": result[12] if len(result) > 12 else None,
                    "corner_buffer": result[13] if len(result) > 13 else None,
                    "corners": result[14] if len(result) > 14 else None,
                    "corner_length": result[15] if len(result) > 15 else None,
                    "corners_cycle_breaks": result[16] if len(result) > 16 else None,
                    "twist_clockwise": result[17] if len(result) > 17 else None,
                    "twist_counterclockwise": result[18] if len(result) > 18 else None,
                    "corners_twisted": result[19] if len(result) > 19 else None,
                    "corners_solved": result[20] if len(result) > 20 else None,
                    "corner_parity": result[21] if len(result) > 21 else None,
                    "first_corners": result[22] if len(result) > 22 else None,
                    "wing_buffer": result[23] if len(result) > 23 else None,
                    "wings": result[24] if len(result) > 24 else None,
                    "wings_length": result[25] if len(result) > 25 else None,
                    "wings_cycle_breaks": result[26] if len(result) > 26 else None,
                    "wings_solved": result[27] if len(result) > 27 else None,
                    "wing_parity": result[28] if len(result) > 28 else None,
                    "first_wings": result[29] if len(result) > 29 else None,
                    "xcenter_buffer": result[30] if len(result) > 30 else None,
                    "xcenters": result[31] if len(result) > 31 else None,
                    "xcenter_length": result[32] if len(result) > 32 else None,
                    "xcenters_cycle_breaks": result[33] if len(result) > 33 else None,
                    "xcenters_solved": result[34] if len(result) > 34 else None,
                    "xcenter_parity": result[35] if len(result) > 35 else None,
                    "first_xcenters": result[36] if len(result) > 36 else None,
                    "tcenter_buffer": result[37] if len(result) > 37 else None,
                    "tcenters": result[38] if len(result) > 38 else None,
                    "tcenter_length": result[39] if len(result) > 39 else None,
                    "tcenters_cycle_breaks": result[40] if len(result) > 40 else None,
                    "tcenters_solved": result[41] if len(result) > 41 else None,
                    "tcenter_parity": result[42] if len(result) > 42 else None,
                    "first_tcenters": result[43] if len(result) > 43 else None
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
                    "random_key": result[4],
                    "edge_buffer": result[5],
                    "edges": result[6],
                    "edge_length": result[7],
                    "edges_cycle_breaks": result[8],
                    "edges_flipped": result[9],
                    "edges_solved": result[10],
                    "flips": result[11],
                    "first_edges": result[12],
                    "corner_buffer": result[13],
                    "corners": result[14],
                    "corner_length": result[15],
                    "corners_cycle_breaks": result[16],
                    "twist_clockwise": result[17],
                    "twist_counterclockwise": result[18],
                    "corners_twisted": result[19],
                    "corners_solved": result[20],
                    "corner_parity": result[21],
                    "first_corners": result[22],
                    "wing_buffer": result[23],
                    "wings": result[24],
                    "wings_length": result[25],
                    "wings_cycle_breaks": result[26],
                    "wings_solved": result[27],
                    "wing_parity": result[28],
                    "first_wings": result[29],
                    "xcenter_buffer": result[30],
                    "xcenters": result[31],
                    "xcenter_length": result[32],
                    "xcenters_cycle_breaks": result[33],
                    "xcenters_solved": result[34],
                    "xcenter_parity": result[35],
                    "first_xcenters": result[36],
                    "tcenter_buffer": result[37],
                    "tcenters": result[38],
                    "tcenter_length": result[39],
                    "tcenters_cycle_breaks": result[40],
                    "tcenters_solved": result[41],
                    "tcenter_parity": result[42],
                    "first_tcenters": result[43] if len(result) > 43 else None
                }
                print(row)
                
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

@app.route('/scramble-stats', methods=['GET'])
def get_scramble_stats():
    try:
        logger.debug("Retrieving scramble statistics")
        
        # Check if force refresh is requested
        force_refresh = request.args.get('refresh', 'false').lower() == 'true'
        
        if force_refresh:
            logger.info("Force refresh requested, generating new stats")
            stats = generate_stats()
        else:
            # Try to get stats from cache or generate new stats if cache is invalid
            if cache_is_valid():
                stats = get_cached_stats()
                if not stats:
                    logger.warning("Cache file exists but couldn't be read, generating new stats")
                    stats = generate_stats()
            else:
                logger.info("Cache is invalid or expired, generating fresh stats")
                stats = generate_stats()
        
        # Add cache status to response
        if 'cache_age' not in stats:
            cache_path = Path(STATS_CACHE_FILE)
            if cache_path.exists():
                stats['cache_age'] = time.time() - cache_path.stat().st_mtime
                stats['cache_expires_in'] = CACHE_EXPIRY - stats['cache_age']
        
        # Include file path for debugging but make it relative
        stats['cache_file'] = os.path.basename(STATS_CACHE_FILE)
        
        response = jsonify(stats)
        response.headers.add('Access-Control-Allow-Origin', '*')
        return response
        
    except Exception as e:
        logger.error(f"Error retrieving scramble stats: {str(e)}")
        logger.error(traceback.format_exc())
        return jsonify({
            'error': str(e),
            'traceback': traceback.format_exc()
        }), 500

@app.route('/scramble-stats', methods=['OPTIONS'])
def stats_options():
    response = jsonify({'status': 'success'})
    response.headers.add('Access-Control-Allow-Origin', '*')
    response.headers.add('Access-Control-Allow-Headers', 'Content-Type')
    response.headers.add('Access-Control-Allow-Methods', 'GET, OPTIONS')
    return response

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)