import subprocess
import argparse
import glob
import os
from datetime import datetime
from concurrent.futures import ThreadPoolExecutor, as_completed

def analyze_solves(scramble_type, change_base_scheme):
    if scramble_type in ["333ni","corners","edges"]:
        cmd = [
            "java.exe",
            "-cp",
            ".",
            "cubes/ThreeCube",
            scramble_type
        ]
    if scramble_type in ["555bld", "5edge"]:
        cmd = [
            "java.exe",
            "-cp",
            ".",
            "cubes/FiveCube",
            scramble_type,
            change_base_scheme
        ]
    
    if scramble_type in ["444bld", "444cto", "444edo"]:
        cmd = [
            "java.exe",
            "-cp",
            ".",
            "cubes/FourCube",
            scramble_type,
            change_base_scheme
        ]
    result = subprocess.run(cmd)


def delete_csv_files(scramble_type):
    """Delete CSV files for a specific scramble type."""
    csv_files = glob.glob(os.path.join('db_solves', f"{scramble_type}_solves_*.csv"))
    for file_name in csv_files:
        os.remove(file_name)

def run_subprocess(count, scramble_type, process_id):
    """Run the Node.js subprocess for a specific range of scrambles."""
    print(f"Starting process {process_id} for {count} scrambles of type {scramble_type}...")
    try:
        subprocess.run(
            ["node", "scrambles_generator\scramble_generator.js", str(count), scramble_type],
            check=True
        )
    except subprocess.CalledProcessError as e:
        print(f"Subprocess {process_id} failed: {e}")

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

def generate_scrambles(total_count, scramble_type, num_threads):
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
                        executor.submit(run_subprocess, count, scramble_type, i + 1)
                    )
            for future in as_completed(futures):
                future.result()  # Wait for all subprocesses to complete

    except Exception as e:
        print(f"Error during scramble generation: {e}")

def main():
    parser = argparse.ArgumentParser(description="Generate scrambles and merge results.")
    parser.add_argument("count", type=int, help="The total number of scrambles to generate.")
    parser.add_argument("scramble_type", type=str, help="The type of scrambles to generate.")
    parser.add_argument("change_base_scheme", type=str, help="change_Base_scheme (for DFr wings buffer)")
    parser.add_argument(
        "--threads", type=int, default=1,
        help="The number of threads (subprocesses) to run concurrently."
    )
    args = parser.parse_args()

    generate_scrambles(args.count, args.scramble_type, args.threads)
    merge_files(args.scramble_type)
    analyze_solves(args.scramble_type, args.change_base_scheme)
    subprocess.run(["python", "db_solves/solves_to_csv.py", args.scramble_type])
    subprocess.run(["python", "db_solves/create_db_script.py", args.scramble_type])


if __name__ == '__main__':
    main()
