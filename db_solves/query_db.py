import sqlite3

# File path to the database
db_file = "db_solves\\333_solve.db"

# User input: allowed characters for edges and corners
allowed_edges = "ABCD"
allowed_corners = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

# Function to generate SQL condition for checking allowed characters
def generate_filter_condition(column, allowed_chars):
    allowed_set = "".join(set(allowed_chars))  # Remove duplicates
    allowed_set_sql = ",".join(f"'{char}'" for char in allowed_set)  # Format for SQL
    return f"""
    NOT EXISTS (
        SELECT 1
        FROM (
            WITH RECURSIVE split(c, rest) AS (
                VALUES('', {column})
                UNION ALL
                SELECT substr(rest, 1, 1), substr(rest, 2)
                FROM split
                WHERE rest != ''
            )
            SELECT c
            FROM split
            WHERE c != ''
        )
        WHERE c NOT IN ({allowed_set_sql})
    )
    """

# Create conditions for edges and corners
edges_condition = generate_filter_condition("first_lp_edges_join", allowed_edges)
corners_condition = generate_filter_condition("first_lp_corners_join", allowed_corners)

# Final query combining both conditions
query = f"""
SELECT * 
FROM scrambles
WHERE {edges_condition}
  AND {corners_condition};
"""

# Connect to the database and execute the query
conn = sqlite3.connect(db_file)
cursor = conn.cursor()

# Execute the query
cursor.execute(query)
results = cursor.fetchall()

# print(query)
# Display results
if results:
    print(f"Found {len(results)} scrambles matching your criteria:")
    for row in results:
        print(row)
else:
    print("No scrambles found matching your criteria.")

# Close the connection

cursor.execute("PRAGMA table_info(scrambles);")
columns = cursor.fetchall()

# Extract column names
column_names = [column[1] for column in columns]  # Column names are in the second position
print("Column Names:", column_names)
conn.close()
