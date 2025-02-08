from flask import Flask, request, jsonify
import sqlite3

app = Flask(__name__)

def query_db(query, args=(), one=False):
    con = sqlite3.connect('path/to/your/database.db')
    cur = con.cursor()
    cur.execute(query, args)
    rv = cur.fetchall()
    con.close()
    return (rv[0] if rv else None) if one else rv

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
    app.run(debug=True)