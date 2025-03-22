from flask import Flask, request, jsonify
from flask_cors import CORS
import sqlite3

app = Flask(__name__)
CORS(app)

def query_db(query, args=(), one=False):
    con = sqlite3.connect(r'db_solves\all_solves.db')
    cur = con.cursor()
    cur.execute(query, args)
    rv = cur.fetchall()
    con.close()
    return (rv[0] if rv else None) if one else rv

# Map frontend scramble types to database scramble types
SCRAMBLE_TYPE_MAP = {
    '3bld': '333ni',
    '4bld': '444bld',
    '4bld_centers': '444cto',
    '4bld_wings': '444edo',
    '5bld': '555bld',
    '5bld_edges': '5edge',
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
    "UBr": "A", "URf": "B", "UFl": "C", "ULb": "D",
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

@app.route('/query-scrambles', methods=['POST'])
def generate_scrambles():
    data = request.json
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
    
    # Edge conditions - only add if not 'random'
    if 'edge_buffer' in data and data['edge_buffer'] != 'random':
        buffer_pos = data['edge_buffer']
        buffer_letter = letter_scheme.get('edges', {}).get(buffer_pos, BUFFER_MAPPINGS['edges'].get(buffer_pos, buffer_pos))
        query_conditions.append("edge_buffer = ?")
        args.append(buffer_letter)
    
    # Edge length range
    if 'edge_length_type' in data and data['edge_length_type'] == 'range':
        query_conditions.append("edge_length BETWEEN ? AND ?")
        args.append(data['edge_length_min'])
        args.append(data['edge_length_max'])
    
    # Edge cycle breaks range
    if 'edges_cycle_breaks_type' in data and data['edges_cycle_breaks_type'] == 'range':
        query_conditions.append("edges_cycle_breaks BETWEEN ? AND ?")
        args.append(data['edges_cycle_breaks_min'])
        args.append(data['edges_cycle_breaks_max'])
    
    # Edges flipped range
    if 'edges_flipped_type' in data and data['edges_flipped_type'] == 'range':
        query_conditions.append("edges_flipped BETWEEN ? AND ?")
        args.append(data['edges_flipped_min'])
        args.append(data['edges_flipped_max'])
    
    # Edges solved range
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
        buffer_letter = letter_scheme.get('corners', {}).get(buffer_pos, BUFFER_MAPPINGS['corners'].get(buffer_pos, buffer_pos))
        query_conditions.append("corner_buffer = ?")
        args.append(buffer_letter)
    
    # Corner length range
    if 'corner_length_type' in data and data['corner_length_type'] == 'range':
        query_conditions.append("corner_length BETWEEN ? AND ?")
        args.append(data['corner_length_min'])
        args.append(data['corner_length_max'])
    
    # Corner cycle breaks range
    if 'corners_cycle_breaks_type' in data and data['corners_cycle_breaks_type'] == 'range':
        query_conditions.append("corners_cycle_breaks BETWEEN ? AND ?")
        args.append(data['corners_cycle_breaks_min'])
        args.append(data['corners_cycle_breaks_max'])
    
    # Clockwise twists range
    if 'corners_cw_twists_type' in data and data['corners_cw_twists_type'] == 'range':
        query_conditions.append("twist_clockwise BETWEEN ? AND ?")
        args.append(data['corners_cw_twists_min'])
        args.append(data['corners_cw_twists_max'])
    
    # Counterclockwise twists range
    if 'corners_ccw_twists_type' in data and data['corners_ccw_twists_type'] == 'range':
        query_conditions.append("twist_counterclockwise BETWEEN ? AND ?")
        args.append(data['corners_ccw_twists_min'])
        args.append(data['corners_ccw_twists_max'])
    
    # Corners solved range
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
        buffer_letter = letter_scheme.get('wings', {}).get(buffer_pos, BUFFER_MAPPINGS['wings'].get(buffer_pos, buffer_pos))
        query_conditions.append("wing_buffer = ?")
        args.append(buffer_letter)
    
    # Wings length range
    if 'wings_length_type' in data and data['wings_length_type'] == 'range':
        query_conditions.append("wings_length BETWEEN ? AND ?")
        args.append(data['wings_length_min'])
        args.append(data['wings_length_max'])
    
    # Wings cycle breaks range
    if 'wings_cycle_breaks_type' in data and data['wings_cycle_breaks_type'] == 'range':
        query_conditions.append("wings_cycle_breaks BETWEEN ? AND ?")
        args.append(data['wings_cycle_breaks_min'])
        args.append(data['wings_cycle_breaks_max'])
    
    # Wings solved range
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
        buffer_letter = letter_scheme.get('xCenters', {}).get(buffer_pos, BUFFER_MAPPINGS['xCenters'].get(buffer_pos, buffer_pos))
        query_conditions.append("xcenter_buffer = ?")
        args.append(buffer_letter)
    
    # X-Center length range
    if 'xcenter_length_type' in data and data['xcenter_length_type'] == 'range':
        query_conditions.append("xcenter_length BETWEEN ? AND ?")
        args.append(data['xcenter_length_min'])
        args.append(data['xcenter_length_max'])
    
    # X-Center cycle breaks range
    if 'xcenters_cycle_breaks_type' in data and data['xcenters_cycle_breaks_type'] == 'range':
        query_conditions.append("xcenters_cycle_breaks BETWEEN ? AND ?")
        args.append(data['xcenters_cycle_breaks_min'])
        args.append(data['xcenters_cycle_breaks_max'])
    
    # X-Center solved range
    if 'xcenters_solved_type' in data and data['xcenters_solved_type'] == 'range':
        query_conditions.append("xcenters_solved BETWEEN ? AND ?")
        args.append(data['xcenters_solved_min'])
        args.append(data['xcenters_solved_max'])
    
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
        buffer_letter = letter_scheme.get('tCenters', {}).get(buffer_pos, BUFFER_MAPPINGS['tCenters'].get(buffer_pos, buffer_pos))
        query_conditions.append("tcenter_buffer = ?")
        args.append(buffer_letter)
    
    # T-Center length range
    if 'tcenter_length_type' in data and data['tcenter_length_type'] == 'range':
        query_conditions.append("tcenter_length BETWEEN ? AND ?")
        args.append(data['tcenter_length_min'])
        args.append(data['tcenter_length_max'])
    
    # T-Center cycle breaks range
    if 'tcenters_cycle_breaks_type' in data and data['tcenters_cycle_breaks_type'] == 'range':
        query_conditions.append("tcenters_cycle_breaks BETWEEN ? AND ?")
        args.append(data['tcenters_cycle_breaks_min'])
        args.append(data['tcenters_cycle_breaks_max'])
    
    # T-Center solved range
    if 'tcenters_solved_type' in data and data['tcenters_solved_type'] == 'range':
        query_conditions.append("tcenters_solved BETWEEN ? AND ?")
        args.append(data['tcenters_solved_min'])
        args.append(data['tcenters_solved_max'])
    
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
    
    # Limit results based on scramble count
    scramble_count = data.get('scramble_count', 1)
    final_query += f" LIMIT {scramble_count}"
    
    # Print query for debugging
    print("Executing query:")
    print(final_query)
    print("With parameters:", args)
    
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
    results = query_db(final_query, args)
    
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
    
    # Return the complete response with scrambles, solutions, and metadata
    return jsonify({
        "metadata": metadata,
        "debug": debug_info if app.debug else None  # Only include debug info in debug mode
    })

if __name__ == '__main__':
    app.run(debug=True, port=5000)