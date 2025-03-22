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
    
    # Edge buffer mapping
    if 'edge_buffer' in data:
        buffer_pos = data['edge_buffer']
        # Try to get letter from letter scheme or fallback to default mapping
        buffer_letter = letter_scheme.get('edges', {}).get(buffer_pos, BUFFER_MAPPINGS['edges'].get(buffer_pos, buffer_pos))
        query_conditions.append("edge_buffer = ?")
        args.append(buffer_letter)
    
    if 'edge_length_type' in data:
        if data['edge_length_type'] == 'range':
            query_conditions.append("edge_length BETWEEN ? AND ?")
            args.append(data['edge_length_min'])
            args.append(data['edge_length_max'])
    
    if 'edge_cycle_breaks_type' in data:
        if data['edge_cycle_breaks_type'] == 'range':
            query_conditions.append("edges_cycle_breaks BETWEEN ? AND ?")
            args.append(data['edge_cycle_breaks_min'])
            args.append(data['edge_cycle_breaks_max'])
    
    if 'edges_flipped_type' in data:
        if data['edges_flipped_type'] == 'range':
            query_conditions.append("edges_flipped BETWEEN ? AND ?")
            args.append(data['edges_flipped_min'])
            args.append(data['edges_flipped_max'])
    
    if 'edges_solved_type' in data:
        if data['edges_solved_type'] == 'range':
            query_conditions.append("edges_solved BETWEEN ? AND ?")
            args.append(data['edges_solved_min'])
            args.append(data['edges_solved_max'])
    
    if 'edge_parity' in data:
        query_conditions.append("corner_parity = ?")  # Note: Changed from edge_parity to corner_parity as in your schema
        args.append(data['edge_parity'])
    
    # Corner buffer mapping
    if 'corner_buffer' in data:
        buffer_pos = data['corner_buffer']
        # Try to get letter from letter scheme or fallback to default mapping
        buffer_letter = letter_scheme.get('corners', {}).get(buffer_pos, BUFFER_MAPPINGS['corners'].get(buffer_pos, buffer_pos))
        query_conditions.append("corner_buffer = ?")
        args.append(buffer_letter)
    
    if 'corner_length_type' in data:
        if data['corner_length_type'] == 'range':
            query_conditions.append("corner_length BETWEEN ? AND ?")
            args.append(data['corner_length_min'])
            args.append(data['corner_length_max'])
    
    if 'corners_cycle_breaks_type' in data:
        if data['corners_cycle_breaks_type'] == 'range':
            query_conditions.append("corners_cycle_breaks BETWEEN ? AND ?")
            args.append(data['corners_cycle_breaks_min'])
            args.append(data['corners_cycle_breaks_max'])
    
    if 'corners_cw_twists_type' in data:
        if data['corners_cw_twists_type'] == 'range':
            query_conditions.append("twist_clockwise BETWEEN ? AND ?")
            args.append(data['corners_cw_twists_min'])
            args.append(data['corners_cw_twists_max'])
    
    if 'corners_ccw_twists_type' in data:
        if data['corners_ccw_twists_type'] == 'range':
            query_conditions.append("twist_counterclockwise BETWEEN ? AND ?")
            args.append(data['corners_ccw_twists_min'])
            args.append(data['corners_ccw_twists_max'])
    
    if 'corners_solved_type' in data:
        if data['corners_solved_type'] == 'range':
            query_conditions.append("corners_solved BETWEEN ? AND ?")
            args.append(data['corners_solved_min'])
            args.append(data['corners_solved_max'])
    
    if 'corner_parity' in data:
        query_conditions.append("corner_parity = ?")
        args.append(data['corner_parity'])
    
    # Wing buffer mapping
    if 'wing_buffer' in data:
        buffer_pos = data['wing_buffer']
        # Try to get letter from letter scheme or fallback to default mapping
        buffer_letter = letter_scheme.get('wings', {}).get(buffer_pos, BUFFER_MAPPINGS['wings'].get(buffer_pos, buffer_pos))
        query_conditions.append("wing_buffer = ?")
        args.append(buffer_letter)
    
    if 'wings_length_type' in data:
        if data['wings_length_type'] == 'range':
            query_conditions.append("wings_length BETWEEN ? AND ?")
            args.append(data['wings_length_min'])
            args.append(data['wings_length_max'])
    
    if 'wings_cycle_breaks_type' in data:
        if data['wings_cycle_breaks_type'] == 'range':
            query_conditions.append("wings_cycle_breaks BETWEEN ? AND ?")
            args.append(data['wings_cycle_breaks_min'])
            args.append(data['wings_cycle_breaks_max'])
    
    if 'wings_solved_type' in data:
        if data['wings_solved_type'] == 'range':
            query_conditions.append("wings_solved BETWEEN ? AND ?")
            args.append(data['wings_solved_min'])
            args.append(data['wings_solved_max'])
    
    if 'wing_parity' in data:
        query_conditions.append("wing_parity = ?")
        args.append(data['wing_parity'])
    
    # X-Center buffer mapping
    if 'xcenter_buffer' in data:
        buffer_pos = data['xcenter_buffer']
        # Try to get letter from letter scheme or fallback to default mapping
        buffer_letter = letter_scheme.get('xCenters', {}).get(buffer_pos, BUFFER_MAPPINGS['xCenters'].get(buffer_pos, buffer_pos))
        query_conditions.append("xcenter_buffer = ?")
        args.append(buffer_letter)
    
    if 'x_centers_length_type' in data:
        if data['x_centers_length_type'] == 'range':
            query_conditions.append("xcenter_length BETWEEN ? AND ?")
            args.append(data['x_centers_length_min'])
            args.append(data['x_centers_length_max'])
    
    if 'x_centers_cycle_breaks_type' in data:
        if data['x_centers_cycle_breaks_type'] == 'range':
            query_conditions.append("xcenters_cycle_breaks BETWEEN ? AND ?")
            args.append(data['x_centers_cycle_breaks_min'])
            args.append(data['x_centers_cycle_breaks_max'])
    
    if 'x_centers_solved_type' in data:
        if data['x_centers_solved_type'] == 'range':
            query_conditions.append("xcenters_solved BETWEEN ? AND ?")
            args.append(data['x_centers_solved_min'])
            args.append(data['x_centers_solved_max'])
    
    if 'xcenter_parity' in data:
        query_conditions.append("xcenter_parity = ?")
        args.append(data['xcenter_parity'])
    
    # T-Center buffer mapping
    if 'tcenter_buffer' in data:
        buffer_pos = data['tcenter_buffer']
        # Try to get letter from letter scheme or fallback to default mapping
        buffer_letter = letter_scheme.get('tCenters', {}).get(buffer_pos, BUFFER_MAPPINGS['tCenters'].get(buffer_pos, buffer_pos))
        query_conditions.append("tcenter_buffer = ?")
        args.append(buffer_letter)
    
    if 't_centers_length_type' in data:
        if data['t_centers_length_type'] == 'range':
            query_conditions.append("tcenter_length BETWEEN ? AND ?")
            args.append(data['t_centers_length_min'])
            args.append(data['t_centers_length_max'])
    
    if 't_centers_cycle_breaks_type' in data:
        if data['t_centers_cycle_breaks_type'] == 'range':
            query_conditions.append("tcenters_cycle_breaks BETWEEN ? AND ?")
            args.append(data['t_centers_cycle_breaks_min'])
            args.append(data['t_centers_cycle_breaks_max'])
    
    if 't_centers_solved_type' in data:
        if data['t_centers_solved_type'] == 'range':
            query_conditions.append("tcenters_solved BETWEEN ? AND ?")
            args.append(data['t_centers_solved_min'])
            args.append(data['t_centers_solved_max'])
    
    if 'tcenter_parity' in data:
        query_conditions.append("tcenter_parity = ?")
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
            # Replace the first ? with number
            sql_for_viewer = sql_for_viewer.replace('?', str(param), 1)
    
    print("\nReady-to-use SQL query for viewer:")
    print(sql_for_viewer)
    
    # Execute query
    results = query_db(final_query, args)
    
    # Print results for debugging
    print(f"Query returned {len(results) if results else 0} results")
    if results:
        return results    
    

if __name__ == '__main__':
    app.run(debug=True, port=5000)