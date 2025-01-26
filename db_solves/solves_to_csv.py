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
    columns = ["scramble_type","scramble","rotations_to_apply", "edge_buffer", "corner_buffer", "edges", "flip", "corners", "Twist Clockwise", "Twist Counterclockwise", "first_lp_edges_join", "first_lp_corners_join"]
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
            rotations_to_apply = re.findall(r"\{([^}]*)\}", line)[0]


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
            data.append([scramble_type,scramble,rotations_to_apply,edge_buffer,corner_buffer, edges, flip, corners, twist_clockwise, twist_counterclockwise, first_edges, first_corners])

    # Write the parsed data to a CSV file
    with open(output_file, "w", newline="", encoding="utf-8") as csv_file:
        writer = csv.writer(csv_file)

        # Write the header
        writer.writerow(columns)

        # Write the data rows
        writer.writerows(data)


def convert_444_solves_to_csv(scramble_type_input):
    
    input_file = glob.glob(os.path.join('txt_files', "{}*_solves_*.txt".format(scramble_type_input)))[0]
    current_time = datetime.now().strftime("%Y%m%d_%H%M%S")
    output_file = os.path.join('txt_files', "{}_solves_{}.csv".format(scramble_type_input, current_time)) 
    columns = ["scramble_type","scramble","rotations_to_apply", "corner_buffer", "corners", "Twist Clockwise", "Twist Counterclockwise","first_lp_corners_join", "wing_buffer", "wings", "first_lp_wings_join", "xcenter_buffer","xcenters", "first_lp_xcenters_join"]
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
            rotations_to_apply = re.findall(r"\{([^}]*)\}", line)[0]

            corners = key_values.get("Corners", "").strip()
            twist_clockwise = key_values.get("Twist Clockwise", "").strip()
            twist_counterclockwise = key_values.get("Twist Counterclockwise", "").strip()
            wings = key_values.get("Wings", "").strip()
            xcenters = key_values.get("XCenters", "").strip()


            first_corners = "".join([pair[0] for pair in corners.split() if len(pair) > 1])
            first_wings = "".join([pair[0] for pair in wings.split() if len(pair) > 1])
            first_xcenters = "".join([pair[0] for pair in xcenters.split() if len(pair) > 1])

            corner_buffer = key_values.get("corner_buffer", "").strip()
            wing_buffer = key_values.get("wing_buffer", "").strip()
            xcenter_buffer = key_values.get("xcenter_buffer", "").strip()
            # Add the parsed row to the data list
            
            

            data.append([scramble_type,scramble,rotations_to_apply,corner_buffer, corners, twist_clockwise, twist_counterclockwise, first_corners, wing_buffer, wings, first_wings, xcenter_buffer,xcenters, first_xcenters])

    # Write the parsed data to a CSV file
    with open(output_file, "w", newline="", encoding="utf-8") as csv_file:
        writer = csv.writer(csv_file)

        # Write the header
        writer.writerow(columns)

        # Write the data rows
        writer.writerows(data)







def convert_555_solves_to_csv(scramble_type_input):
    
    input_file = glob.glob(os.path.join('txt_files', "{}*_solves_*.txt".format(scramble_type_input)))[0]
    current_time = datetime.now().strftime("%Y%m%d_%H%M%S")
    output_file = os.path.join('txt_files', "{}_solves_{}.csv".format(scramble_type_input, current_time)) 
    columns = ["scramble_type", "scramble","rotations_to_apply","edge_buffer","edges","edge_length","edges_cycle_breaks","edges_flipped","edges_solved","flips","first_edges","corner_buffer","corners","corner_length","corners_cycle_breaks","twist_clockwise","twist_counterclockwise","corners_twisted","corners_solved","corner_parity","first_corners","wing_buffer","wings","wings_length","wings_cycle_breaks","wings_solved","wing_parity","first_wings","xcenter_buffer","xcenters","xcenter_length","xcenters_cycle_breaks","xcenters_solved","xcenter_parity","first_xcenters","tcenter_buffer","tcenters","tcenter_length","tcenters_cycle_breaks","tcenters_solved","tcenter_parity","first_tcenters"]
    data = []

    # Regular expression patterns for parsing the lines
    key_value_pattern = r"'(\w[\w\s]+)':\s*'([^']*)'"  # Extract key-value pairs
    

# Step 2: Extract key-value pairs

    # Read and parse the input file
    with open(input_file, "r", encoding="utf-8") as file:
        for line in file:
            items = line.split(',')
            key_values = {}
            print(items)
            for item in items:
                match = re.match(r"([^:]+):'([^']*)'", item)
                if match:
                    key, value = match.groups()
                    key_values[key.strip("'")] = value.strip()

            scramble = line.split(",")[1]
            scramble_type = line.split(",")[0]
            # Parse the key-value pairs (edges, flip, etc.)
            # key_values = dict(re.findall(key_value_pattern, line))
            print(key_values)
            print(key_values.keys())
            rotations_to_apply = re.findall(r"\{([^}]*)\}", line)[0]
            
            edge_buffer = key_values.get("edge_buffer", "").strip()
            edges = key_values.get("Edges", "").strip()
            edge_length = key_values.get("Edges_len", "").strip()
            edges_cycle_breaks = key_values.get("Edges_cycle_breaks", "").strip()
            edges_flipped = key_values.get("Edges_flipped", "").strip()
            edges_solved = key_values.get("Edges_solved", "").strip()
            flip = key_values.get("Flip", "").strip()
            first_edges = "".join([pair[0] for pair in edges.split() if len(pair) > 1])
            
            corner_buffer = key_values.get("corner_buffer", "").strip()
            corners = key_values.get("Corners", "").strip()
            corner_length = key_values.get("Corners_len", "").strip()
            corners_cycle_breaks = key_values.get("Corners_cycle_breaks", "").strip()
            twist_clockwise = key_values.get("Twist Clockwise", "").strip()
            twist_counterclockwise = key_values.get("Twist Counterclockwise", "").strip()
            corners_twisted = key_values.get("Corners_twists", "").strip()
            corners_solved = key_values.get("Corners_solved", "").strip()
            corner_parity = key_values.get("Corners_parity", "").strip()
            first_corners = "".join([pair[0] for pair in corners.split() if len(pair) > 1])
            
            wing_buffer = key_values.get("wing_buffer", "").strip()
            wings = key_values.get("Wings", "").strip()
            wings_length = key_values.get("Wings_len", "").strip()
            wings_cycle_breaks = key_values.get("Wings_cycle_breaks", "").strip()
            wings_solved = key_values.get("Wings_solved", "").strip()
            wing_parity = key_values.get("Wings_parity", "").strip()
            first_wings = "".join([pair[0] for pair in wings.split() if len(pair) > 1])
            
            xcenter_buffer = key_values.get("xcenter_buffer", "").strip()
            xcenters = key_values.get("XCenters", "").strip()
            xcenter_length = key_values.get("XCenters_len", "").strip()
            xcenters_cycle_breaks = key_values.get("XCenters_cycle_breaks", "").strip()
            xcenters_solved = key_values.get("XCenters_solved", "").strip()
            print("here" + xcenters_solved)
            xcenter_parity = key_values.get("XCenters_parity", "").strip()
            first_xcenters = "".join([pair[0] for pair in xcenters.split() if len(pair) > 1])
            
            tcenter_buffer = key_values.get("tcenter_buffer", "").strip()
            tcenters = key_values.get("TCenters", "").strip()
            tcenter_length = key_values.get("TCenters_len", "").strip()
            tcenters_cycle_breaks = key_values.get("TCenters_cycle_breaks", "").strip()
            tcenters_solved = key_values.get("TCenters_solved", "").strip()
            tcenter_parity = key_values.get("TCenters_parity", "").strip()
            first_tcenters = "".join([pair[0] for pair in tcenters.split() if len(pair) > 1])

            all_data = [scramble_type , scramble ,  rotations_to_apply , edge_buffer , edges , edge_length , edges_cycle_breaks , edges_flipped , edges_solved , flip , first_edges , corner_buffer , corners , corner_length , corners_cycle_breaks , twist_clockwise , twist_counterclockwise , corners_twisted , corners_solved , corner_parity , first_corners , wing_buffer , wings , wings_length , wings_cycle_breaks , wings_solved , wing_parity , first_wings , xcenter_buffer , xcenters , xcenter_length , xcenters_cycle_breaks , xcenters_solved , xcenter_parity , first_xcenters , tcenter_buffer , tcenters , tcenter_length , tcenters_cycle_breaks , tcenters_solved , tcenter_parity , first_tcenters ]
            data.append(all_data)
            print(edges_flipped)

    # Write the parsed data to a CSV file
    with open(output_file, "w", newline="", encoding="utf-8") as csv_file:
        writer = csv.writer(csv_file)

        # Write the header
        writer.writerow(columns)

        # Write the data rows
        writer.writerows(data)




def main():
    parser = argparse.ArgumentParser(description="convert results to csv")
    parser.add_argument("scramble_type", type=str, help="The type of scrambles to generate.")
    args = parser.parse_args()
    if args.scramble_type in ["333ni", "edges", "corners"]:
        convert_333_solves_to_csv(args.scramble_type)
    if args.scramble_type in ["444bld", "444cto", "444edo"]:
        convert_444_solves_to_csv(args.scramble_type)

    if args.scramble_type in["555bld", "5edge"]:
        convert_555_solves_to_csv(args.scramble_type)
    
if __name__ == '__main__':
    main()