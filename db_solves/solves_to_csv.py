import csv
import re
import argparse
import glob
import os
from datetime import datetime
# Input and output file paths

def convert_333_solves_to_csv(scramble_type_input):

    input_file = glob.glob(os.path.join('txt_files', "{}*_solves_*.txt").format(scramble_type_input))[0]
    current_time = datetime.now().strftime("%Y%m%d_%H%M%S")
    output_file = os.path.join('txt_files', "{}_solves_{}.csv".format(scramble_type_input, current_time)) 
    columns = ["scramble_type","scramble","edge_buffer", "corner_buffer", "edges", "flip", "corners", "Twist Clockwise", "Twist Counterclockwise", "first_lp_edges_join", "first_lp_corners_join"]
    data = []

    # Regular expression patterns for parsing the lines
    key_value_pattern = r"'(\w[\w\s]+)':\s*'([^']*)'"  # Extract key-value pairs

    # Read and parse the input file
    with open(input_file, "r", encoding="utf-8") as file:
        for line in file:
          
            scramble = line.split(",")[1]
            scramble_type = line.split(",")[0]
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
            data.append([scramble_type,scramble,edge_buffer,corner_buffer, edges, flip, corners, twist_clockwise, twist_counterclockwise, first_edges, first_corners])

    # Write the parsed data to a CSV file
    with open(output_file, "w", newline="", encoding="utf-8") as csv_file:
        writer = csv.writer(csv_file)

        # Write the header
        writer.writerow(columns)

        # Write the data rows
        writer.writerows(data)

    print(f"CSV file saved to {output_file}")

def convert_555_solves_to_csv(scramble_type_input):
    
    input_file = glob.glob(os.path.join('txt_files', "{}*_solves_*.txt".format(scramble_type_input)))[0]
    current_time = datetime.now().strftime("%Y%m%d_%H%M%S")
    output_file = os.path.join('txt_files', "{}_solves_{}.csv".format(scramble_type_input, current_time)) 
    print(input_file)
    columns = ["scramble_type","scramble","edge_buffer", "corner_buffer", "edges", "flip", "corners", "Twist Clockwise", "Twist Counterclockwise", "first_lp_edges_join", "first_lp_corners_join", "wing_buffer", "wings", "first_lp_wings_join", "xcenter_buffer","xcenters", "first_lp_xcenters_join", "tcenter_buffer", "tcenters", "first_lp_tcenters_join"]
    data = []

    # Regular expression patterns for parsing the lines
    key_value_pattern = r"'(\w[\w\s]+)':\s*'([^']*)'"  # Extract key-value pairs

    # Read and parse the input file
    with open(input_file, "r", encoding="utf-8") as file:
        for line in file:
        
            scramble = line.split(",")[1]
            scramble_type = line.split(",")[0]
            # Parse the key-value pairs (edges, flip, etc.)
            key_values = dict(re.findall(key_value_pattern, line))
            edges = key_values.get("Edges", "").strip()
            flip = key_values.get("Flip", "").strip()
            corners = key_values.get("Corners", "").strip()
            twist_clockwise = key_values.get("Twist Clockwise", "").strip()
            twist_counterclockwise = key_values.get("Twist Counterclockwise", "").strip()
            wings = key_values.get("Wings", "").strip()
            xcenters = key_values.get("XCenters", "").strip()
            tcenters = key_values.get("TCenters", "").strip()


            first_edges = "".join([pair[0] for pair in edges.split() if len(pair) > 1])
            first_corners = "".join([pair[0] for pair in corners.split() if len(pair) > 1])
            first_wings = "".join([pair[0] for pair in wings.split() if len(pair) > 1])
            first_xcenters = "".join([pair[0] for pair in xcenters.split() if len(pair) > 1])
            first_tcenters = "".join([pair[0] for pair in tcenters.split() if len(pair) > 1])

            edge_buffer = key_values.get("edge_buffer", "").strip()
            corner_buffer = key_values.get("corner_buffer", "").strip()
            wing_buffer = key_values.get("wing_buffer", "").strip()
            xcenter_buffer = key_values.get("xcenter_buffer", "").strip()
            tcenter_buffer = key_values.get("tcenter_buffer", "").strip()
            # Add the parsed row to the data list

            

            data.append([scramble_type,scramble,edge_buffer, corner_buffer, edges, flip, corners, twist_clockwise, twist_counterclockwise, first_edges, first_corners, wing_buffer, wings, first_wings, xcenter_buffer,xcenters, first_xcenters, tcenter_buffer, tcenters, first_tcenters])

    # Write the parsed data to a CSV file
    with open(output_file, "w", newline="", encoding="utf-8") as csv_file:
        writer = csv.writer(csv_file)

        # Write the header
        writer.writerow(columns)

        # Write the data rows
        writer.writerows(data)

    print(f"CSV file saved to {output_file}")



def main():
    parser = argparse.ArgumentParser(description="convert results to csv")
    parser.add_argument("scramble_type", type=str, help="The type of scrambles to generate.")
    args = parser.parse_args()
    if args.scramble_type in ["333ni", "edges", "corners"]:
        convert_333_solves_to_csv(args.scramble_type)
    if args.scramble_type in["555bld", "5edge"]:
        convert_555_solves_to_csv(args.scramble_type)
    
if __name__ == '__main__':
    main()