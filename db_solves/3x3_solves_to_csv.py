import csv
import re

# Input and output file paths
input_file = "txt_files\\all_333_solves.txt"  # Replace with your input file path
output_file = "db_solves\\all_333_solves.csv"

# Define the columns for the CSV
columns = ["scramble","edge_buffer", "corner_buffer", "edges", "flip", "corners", "Twist Clockwise", "Twist Counterclockwise", "first_lp_edges_join", "first_lp_corners_join"]

# Initialize an empty list to store parsed rows
data = []

# Regular expression patterns for parsing the lines
scramble_pattern = r"^(.*?)\,"  # Extract scramble
key_value_pattern = r"'(\w[\w\s]+)':\s*'([^']*)'"  # Extract key-value pairs

# Read and parse the input file
with open(input_file, "r", encoding="utf-8") as file:
    for line in file:
        # Parse the scramble
        scramble_match = re.search(scramble_pattern, line)
        scramble = scramble_match.group(1).strip() if scramble_match else ""

        # Parse the key-value pairs (edges, flip, etc.)
        key_values = dict(re.findall(key_value_pattern, line))
        edges = key_values.get("Edges", "").strip()
        flip = key_values.get("Flip", "").strip()
        corners = key_values.get("Corners", "").strip()
        twist_clockwise = key_values.get("Twist Clockwise", "").strip()
        twist_counterclockwise = key_values.get("Twist Counterclockwise", "").strip()

        # Generate first_edges by taking the first letter of each letter pair in edges
        first_edges = "".join([pair[0] for pair in edges.split() if len(pair) > 1])

        # Generate first_corners by taking the first letter of each letter pair in corners
        first_corners = "".join([pair[0] for pair in corners.split() if len(pair) > 1])
        edge_buffer = key_values.get("edge_buffer", "").strip()
        corner_buffer = key_values.get("corner_buffer", "").strip()
        # Add the parsed row to the data list
        data.append([scramble,edge_buffer,corner_buffer, edges, flip, corners, twist_clockwise, twist_counterclockwise, first_edges, first_corners])

# Write the parsed data to a CSV file
with open(output_file, "w", newline="", encoding="utf-8") as csv_file:
    writer = csv.writer(csv_file)

    # Write the header
    writer.writerow(columns)

    # Write the data rows
    writer.writerows(data)

print(f"CSV file saved to {output_file}")
