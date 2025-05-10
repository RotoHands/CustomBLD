import subprocess
import argparse
import glob
import os
from datetime import datetime
from concurrent.futures import ThreadPoolExecutor, as_completed

def analyze_solves(scramble_type, change_base_scheme, buffers):
    if scramble_type in ["333ni","corners","edges"]:
        cmd = [
            "java",
            "-cp",
            ".",
            "cubes.ThreeCube",
            scramble_type
        ]
        # Add buffer arguments for 3BLD
        if buffers.get("corner_buffer"):
            cmd.append("--corner_buffer")
            cmd.append(buffers.get("corner_buffer"))
        if buffers.get("edge_buffer"):
            cmd.append("--edge_buffer")
            cmd.append(buffers.get("edge_buffer"))
    if scramble_type in ["555bld", "5edge"]:
        cmd = [
            "java",
            "-cp",
            ".",
            "cubes.FiveCube",
            scramble_type,
            change_base_scheme
        ]
        # Add buffer arguments for 5BLD
        if buffers.get("corner_buffer"):
            cmd.append("--corner_buffer")
            cmd.append(buffers.get("corner_buffer"))
        if buffers.get("edge_buffer"):
            cmd.append("--edge_buffer")
            cmd.append(buffers.get("edge_buffer"))
        if buffers.get("wing_buffer"):
            cmd.append("--wing_buffer")
            cmd.append(buffers.get("wing_buffer"))
        if buffers.get("xcenter_buffer"):
            cmd.append("--xcenter_buffer")
            cmd.append(buffers.get("xcenter_buffer"))
        if buffers.get("tcenter_buffer"):
            cmd.append("--tcenter_buffer")
            cmd.append(buffers.get("tcenter_buffer"))
    
    if scramble_type in ["444bld", "444cto", "444edo"]:
        cmd = [
            "java",
            "-cp",
            ".",
            "cubes.FourCube",
            scramble_type,
            change_base_scheme
        ]
        # Add buffer arguments for 4BLD
        if buffers.get("corner_buffer"):
            cmd.append("--corner_buffer")
            cmd.append(buffers.get("corner_buffer"))
        if buffers.get("wing_buffer"):
            cmd.append("--wing_buffer")
            cmd.append(buffers.get("wing_buffer"))
        if buffers.get("xcenter_buffer"):
            cmd.append("--xcenter_buffer")
            cmd.append(buffers.get("xcenter_buffer"))
    
    print("cmd: ", cmd)
    result = subprocess.run(cmd, cwd=os.path.dirname(os.path.abspath(__file__)))


def delete_csv_files(scramble_type):
    """Delete CSV files for a specific scramble type."""
    csv_files = glob.glob(os.path.join('db_solves', f"{scramble_type}_solves_*.csv"))
    for file_name in csv_files:
        os.remove(file_name)

def run_subprocess(count, scramble_type, process_id, buffers=None):
    """Run the Node.js subprocess for a specific range of scrambles."""
    print(f"Starting process {process_id} for {count} scrambles of type {scramble_type}...")
    try:
        # Get the absolute path to the script
        script_dir = os.path.dirname(os.path.abspath(__file__))
        script_path = os.path.join(script_dir, "scrambles_generator", "scramble_generator.js")
        
        cmd = ["node", script_path, str(count), scramble_type]
        
        # Add buffer parameters if they exist
        if buffers:
            for buffer_type, buffer_value in buffers.items():
                if buffer_value:
                    cmd.append(f"--{buffer_type}")
                    cmd.append(buffer_value)
        
        print("Running command:", cmd)
        print("Working directory:", os.getcwd())
        print("Script exists:", os.path.exists(script_path))
        
        subprocess.run(cmd, check=True, cwd=script_dir)
    except subprocess.CalledProcessError as e:
        print(f"Subprocess {process_id} failed: {e}")
    except Exception as e:
        print(f"Error running subprocess {process_id}: {e}")
        print(f"Current directory: {os.getcwd()}")
        print(f"Script path: {script_path}")
        print(f"Directory contents: {os.listdir(os.path.dirname(script_path))}")

def merge_files(scramble_type):
    """Merge generated scramble files into a single file."""
    input_files = glob.glob(os.path.join('txt_files', f'{scramble_type}*_file*.txt'))
    current_time = datetime.now().strftime("%Y%m%d_%H%M%S")
    output_file = os.path.join('txt_files', f'{scramble_type}_scrambles_{current_time}.txt')

    try:
        with open(output_file, 'wb') as outfile:
            for file_name in input_files:
                with open(file_name, 'rb') as infile:
                    outfile.write(infile.read())
        
        # Remove input files after merging
        for file_name in input_files:
            os.remove(file_name)

    except Exception as e:
        print(f"Error during file merge: {e}")

def generate_scrambles(total_count, scramble_type, num_threads, buffers=None):
    """Generate scrambles using multithreaded subprocesses."""
    scrambles_per_thread = total_count // num_threads
    remaining_scrambles = total_count % num_threads

    try:
        with ThreadPoolExecutor(max_workers=num_threads) as executor:
            futures = []
            for i in range(num_threads):
                count = scrambles_per_thread + (1 if i < remaining_scrambles else 0)
                if count > 0:
                    futures.append(
                        executor.submit(run_subprocess, count, scramble_type, i + 1, buffers)
                    )
            for future in as_completed(futures):
                future.result()  # Wait for all subprocesses to complete

    except Exception as e:
        print(f"Error during scramble generation: {e}")

def delete_txt_csv_files(is_delete):
    """Delete all txt files in txt_files folder."""
    if is_delete:
        txt_files = glob.glob(os.path.join('txt_files', f'*.txt'))
        csv_files = glob.glob(os.path.join('txt_files', f'*.csv'))
        for file_name in txt_files:
            try:
                os.remove(file_name)
                print(f"Deleted: {file_name}")
            except Exception as e:
                print(f"Error deleting {file_name}: {e}")
        
        for file_name in csv_files:
            try:
                os.remove(file_name)
                print(f"Deleted: {file_name}")
            except Exception as e:
                print(f"Error deleting {file_name}: {e}")

def main():
    print("here 0")
    parser = argparse.ArgumentParser(description="Generate scrambles and merge results.")
    parser.add_argument("count", type=int, help="The total number of scrambles to generate.")
    parser.add_argument("scramble_type", type=str, help="The type of scrambles to generate.")
    parser.add_argument("change_base_scheme", type=str, help="change_Base_scheme (for DFr wings buffer)")
    parser.add_argument(
        "--threads", type=int, default=1,
        help="The number of threads (subprocesses) to run concurrently."
    )
    # Add buffer arguments
    parser.add_argument("--corner_buffer", help="Corner buffer position letter (default: C)")
    parser.add_argument("--edge_buffer", help="Edge buffer position letter (default: C)")
    parser.add_argument("--wing_buffer", help="Wing buffer position letter (default: C)")
    parser.add_argument("--xcenter_buffer", help="X-Center buffer position letter (default: C)")
    parser.add_argument("--tcenter_buffer", help="T-Center buffer position letter (default: C)")
    
    args = parser.parse_args()

    # Create a buffer dictionary to pass to analyze_solves
    buffers = {
        "corner_buffer": args.corner_buffer,
        "edge_buffer": args.edge_buffer,
        "wing_buffer": args.wing_buffer,
        "xcenter_buffer": args.xcenter_buffer,
        "tcenter_buffer": args.tcenter_buffer
    }
    print("here 1")
    generate_scrambles(args.count, args.scramble_type, args.threads, buffers)
    merge_files(args.scramble_type)
    print("buffers: ", buffers)
    analyze_solves(args.scramble_type, args.change_base_scheme, buffers)
    subprocess.run(["python", "db_solves/solves_to_csv.py", args.scramble_type], stderr=subprocess.PIPE, stdout=subprocess.PIPE, text=True)
    subprocess.run(["python", "db_solves/create_db_script.py", args.scramble_type], stderr=subprocess.PIPE, stdout=subprocess.PIPE, text=True)
    delete_txt_csv_files(True)

if __name__ == '__main__':
    main()
