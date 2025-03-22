from flask import Flask, request, jsonify
from flask_cors import CORS
import sqlite3

app = Flask(__name__)
CORS(app)

def query_db(query, args=(), one=False):
    con = sqlite3.connect('all_solves.db')
    cur = con.cursor()
    cur.execute(query, args)
    rv = cur.fetchall()
    con.close()
    return (rv[0] if rv else None) if one else rv

@app.route('/query-scarmbels', methods=['POST'])
def generate_scrambles():
    data = request.json
    query = """
    SELECT * FROM solves 
    WHERE scramble_type = ? 
    AND edge_buffer = ? 
    AND edge_length BETWEEN ? AND ?
    AND edges_cycle_breaks BETWEEN ? AND ?
    AND edge_parity = ?
    AND corner_buffer = ?
    AND corner_length BETWEEN ? AND ?
    AND corners_cycle_breaks BETWEEN ? AND ?
    AND corner_parity = ?
    """
    
    args = (
        data['scramble_type'],
        data['edge_buffer'],
        data['edge_length_min'], data['edge_length_max'],
        data['edges_cycle_breaks_min'], data['edges_cycle_breaks_max'],
        data['edge_parity'],
        data['corner_buffer'],
        data['corner_length_min'], data['corner_length_max'],
        data['corners_cycle_breaks_min'], data['corners_cycle_breaks_max'],
        data['corner_parity']
    )

    if data.get('scramble_type') in ['4BLD', '5BLD']:
        # Add wing conditions
        query += """
        AND wing_buffer = ?
        AND wings_length BETWEEN ? AND ?
        AND wings_cycle_breaks BETWEEN ? AND ?
        AND wing_parity = ?
        """
        args += (
            data['wing_buffer'],
            data['wings_length_min'], data['wings_length_max'],
            data['wings_cycle_breaks_min'], data['wings_cycle_breaks_max'],
            data['wing_parity']
        )

        # Add x-center conditions
        query += """
        AND xcenter_buffer = ?
        AND x_centers_length BETWEEN ? AND ?
        AND x_centers_cycle_breaks BETWEEN ? AND ?
        AND xcenter_parity = ?
        """
        args += (
            data['xcenter_buffer'],
            data['x_centers_length_min'], data['x_centers_length_max'],
            data['x_centers_cycle_breaks_min'], data['x_centers_cycle_breaks_max'],
            data['xcenter_parity']
        )

    if data.get('scramble_type') == '5BLD':
        # Add t-center conditions
        query += """
        AND tcenter_buffer = ?
        AND t_centers_length BETWEEN ? AND ?
        AND t_centers_cycle_breaks BETWEEN ? AND ?
        AND tcenter_parity = ?
        """
        args += (
            data['tcenter_buffer'],
            data['t_centers_length_min'], data['t_centers_length_max'],
            data['t_centers_cycle_breaks_min'], data['t_centers_cycle_breaks_max'],
            data['tcenter_parity']
        )

    results = query_db(query, args)
    
    return jsonify({
        "scrambles": [result[0] for result in results[:data['scramble_count']]],
        "solutions": [result[1] for result in results[:data['scramble_count']]] if data['generate_solutions'] == 'yes' else None
    })

@app.route('/query', methods=['POST'])
def query():
    data = request.json
    query = """
    SELECT * FROM solves
    WHERE corners = ? AND wing_buffer = ? AND wings = ? AND wings_length = ? AND wings_cycle_breaks = ?
    AND wings_solved = ? AND wing_parity = ? AND first_wings = ? AND xcenter_buffer = ? AND xcenters = ?
    AND xcenter_length = ? AND xcenters_cycle_breaks = ? AND xcenters_solved = ? AND xcenter_parity = ?
    AND first_xcenters = ?
    """
    args = (
        data['corners'], data['wingBuffer'], data['wings'], data['wingsLength'], data['wingsCycleBreaks'],
        data['wingsSolved'], data['wingParity'], data['firstWings'], data['xcenterBuffer'], data['xcenters'],
        data['xcenterLength'], data['xcentersCycleBreaks'], data['xcentersSolved'], data['xcenterParity'],
        data['firstXcenters']
    )
    result = query_db(query, args)
    return jsonify(result)

if __name__ == '__main__':
    app.run(debug=True, port=5000)