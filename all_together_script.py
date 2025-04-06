import subprocess
import glob
import time
import sys
import os
import threading

def print_combination_summary():
    """Print a summary of all potential scramble combinations and total counts"""
    print("\n" + "="*80)
    print("SCRAMBLE GENERATION SUMMARY - ALL POSSIBLE COMBINATIONS")
    print("="*80)
    
    # Edge buffers
    edge_buffers = ["UF", "FU", "DF", "UR"]
    print(f"Edge Buffers ({len(edge_buffers)}): {', '.join(edge_buffers)}")
    
    # Corner buffers
    corner_buffers = ["UFR", "UBL", "UFL", "RDF"]
    print(f"Corner Buffers ({len(corner_buffers)}): {', '.join(corner_buffers)}")
    
    # Wing buffers
    wing_buffers = ["UFr", "DFr", "FUr"]
    print(f"Wing Buffers ({len(wing_buffers)}): {', '.join(wing_buffers)}")
    
    # X-center buffers
    xcenter_buffers = ["Ufr", "Ubl", "Ubr", "Ufl"]
    print(f"X-Center Buffers ({len(xcenter_buffers)}): {', '.join(xcenter_buffers)}")
    
    # T-center buffers
    tcenter_buffers = ["Uf", "Ub", "Ur", "Ul"]
    print(f"T-Center Buffers ({len(tcenter_buffers)}): {', '.join(tcenter_buffers)}")
    
    print("\nMAXIMUM COMBINATIONS PER SCRAMBLE TYPE:")
    
    # 3BLD combinations
    bld3_combos = len(edge_buffers) * len(corner_buffers)
    print(f"3BLD:                  {bld3_combos} combinations (edges × corners)")
    
    # 4BLD combinations
    bld4_combos = len(corner_buffers) * len(wing_buffers) * len(xcenter_buffers)
    print(f"4BLD:                  {bld4_combos} combinations (corners × wings × xcenters)")
    
    # 5BLD combinations
    bld5_combos = len(corner_buffers) * len(edge_buffers) * len(wing_buffers) * len(xcenter_buffers) * len(tcenter_buffers)
    print(f"5BLD:                  {bld5_combos} combinations (all buffers)")
    
    # Edge only combinations
    edge_combos = len(edge_buffers)
    print(f"Edges Only:            {edge_combos} combinations (edges)")
    
    # Corner only combinations
    corner_combos = len(corner_buffers)
    print(f"Corners Only:          {corner_combos} combinations (corners)")
    
    # 4BLD centers only combinations
    center4_combos = len(xcenter_buffers)
    print(f"4BLD Centers Only:     {center4_combos} combinations (xcenters)")
    
    # 4BLD wings only combinations
    wings4_combos = len(wing_buffers)
    print(f"4BLD Wings Only:       {wings4_combos} combinations (wings)")
    
    # 5BLD Edges+Corners combinations
    ec5_combos = len(corner_buffers) * len(edge_buffers)
    print(f"5BLD Edges+Corners:    {ec5_combos} combinations (corners × edges)")
    
    print("\nSCRAMBLE COUNTS PER TYPE (if all combinations enabled):")
    
    # Define default counts
    counts = {
        "3BLD": 10000,
        "4BLD": 100,
        "5BLD": 50,
        "Edges Only": 5000,
        "Corners Only": 5000,
        "4BLD Centers Only": 300,
        "4BLD Wings Only": 300,
        "5BLD Edges+Corners": 100
    }
    
    # Calculate totals
    print(f"3BLD:                  {counts['3BLD']} × {bld3_combos} = {counts['3BLD']*bld3_combos:,} scrambles")
    print(f"4BLD:                  {counts['4BLD']} × {bld4_combos} = {counts['4BLD']*bld4_combos:,} scrambles")
    print(f"5BLD:                  {counts['5BLD']} × {bld5_combos} = {counts['5BLD']*bld5_combos:,} scrambles")
    print(f"Edges Only:            {counts['Edges Only']} × {edge_combos} = {counts['Edges Only']*edge_combos:,} scrambles")
    print(f"Corners Only:          {counts['Corners Only']} × {corner_combos} = {counts['Corners Only']*corner_combos:,} scrambles")
    print(f"4BLD Centers Only:     {counts['4BLD Centers Only']} × {center4_combos} = {counts['4BLD Centers Only']*center4_combos:,} scrambles")
    print(f"4BLD Wings Only:       {counts['4BLD Wings Only']} × {wings4_combos} = {counts['4BLD Wings Only']*wings4_combos:,} scrambles")
    print(f"5BLD Edges+Corners:    {counts['5BLD Edges+Corners']} × {ec5_combos} = {counts['5BLD Edges+Corners']*ec5_combos:,} scrambles")
    
    grand_total = (
        counts['3BLD']*bld3_combos +
        counts['4BLD']*bld4_combos +
        counts['5BLD']*bld5_combos +
        counts['Edges Only']*edge_combos +
        counts['Corners Only']*corner_combos +
        counts['4BLD Centers Only']*center4_combos +
        counts['4BLD Wings Only']*wings4_combos +
        counts['5BLD Edges+Corners']*ec5_combos
    )
    
    print(f"\nMAXIMUM TOTAL (all combinations): {grand_total:,} scrambles")
    print("="*80)
    
    # User input to continue
    input("\nPress Enter to continue to configuration...")

def progress_bar(process, total_time=None):
    """Display a progress bar while a subprocess is running"""
    chars = '|/-\\'
    delay = 0.1
    elapsed = 0
    
    # Check if we're running in a notebook or terminal 
    is_terminal = sys.stdout.isatty()
    
    try:
        i = 0
        while process.poll() is None:
            if is_terminal:
                sys.stdout.write(f"\r{chars[i%4]} Processing... {elapsed:.1f}s elapsed")
            else:
                # For non-terminal environments like notebooks
                if i % 10 == 0:  # Only print occasionally to avoid flooding output
                    print(f"Processing... {elapsed:.1f}s elapsed")
                        
            sys.stdout.flush()
            time.sleep(delay)
            elapsed += delay
            i += 1
    except KeyboardInterrupt:
        return
    finally:
        # Clear the line when done
        if is_terminal:
            sys.stdout.write("\r" + " " * 100 + "\r")
            sys.stdout.flush()

# Function to estimate time for a given scramble count based on puzzle type
def estimate_time(scramble_type, count):
    # Estimated time in seconds per scramble (calibrate these values for your system)
    time_per_scramble = {
        "333ni": 0.010,    # 3BLD - 10ms per scramble 
        "444bld": 0.015,   # 4BLD - 15ms per scramble
        "555bld": 0.025,   # 5BLD - 25ms per scramble
        "edges": 0.008,    # Edges only - 8ms per scramble
        "corners": 0.008,  # Corners only - 8ms per scramble
        "444cto": 0.012,   # 4BLD centers only - 12ms per scramble
        "444edo": 0.012,   # 4BLD wings only - 12ms per scramble
        "5edge": 0.015     # 5BLD edges+corners - 15ms per scramble
    }
    
    # Processing overhead time in seconds
    overhead = 5.0  
    
    # Calculate estimated time
    base_time = float(count) * time_per_scramble.get(scramble_type, 0.015)
    return base_time + overhead

def main():
    # Display summary of all possible combinations
    print_combination_summary()
    
    # Define buffer options for each piece type
    edge_buffers = {
        "UF": "C",
        "FU": "I", 
        "DF": "U",
        "UR": "B"
    }
    
    corner_buffers = {
        "UFR": "C",
        "UBL": "A",
        "UFL": "D",
        "RDF": "P"  # Equivalent to RFD in the mapping
    }
    
    wing_buffers = {
        "UFr": "C",
        "DFr": "U",
        "FUr": "I"
    }
    
    xcenter_buffers = {
        "Ufr": "C",
        "Ubl": "A",
        "Ubr": "B",
        "Ufl": "D"
    }
    
    tcenter_buffers = {
        "Uf": "C",
        "Ub": "A",
        "Ur": "B",
        "Ul": "D"
    }

    # =====================================================================
    # CONFIGURATION: Uncomment the buffer combinations you want to generate
    # =====================================================================
    
    # The buffers to use - uncomment the ones you want
    USE_EDGE_BUFFERS = [
        "UF",       # Standard UF buffer
        "FU",     # Uncomment to use FU buffer
        "DF",     # Uncomment to use DF buffer
        "UR",     # Uncomment to use UR buffer
    ]
    
    USE_CORNER_BUFFERS = [
        "UFR",      # Standard UFR buffer
        "UBL",    # Uncomment to use UBL buffer
        "UFL",    # Uncomment to use UFL buffer
        "RDF",    # Uncomment to use RDF buffer
    ]
    
    USE_WING_BUFFERS = [
        "UFr",      # Standard UFr buffer
        "DFr",    # Uncomment to use DFr buffer
        # "FUr",    # Uncomment to use FUr buffer
    ]
    
    USE_XCENTER_BUFFERS = [
        "Ufr",      # Standard Ufr buffer
        "Ubl",    # Uncomment to use Ubl buffer
        # "Ubr",    # Uncomment to use Ubr buffer
        # "Ufl",    # Uncomment to use Ufl buffer
    ]
    
    USE_TCENTER_BUFFERS = [
        "Uf",       # Standard Uf buffer
        "Ub",     # Uncomment to use Ub buffer
        # "Ur",     # Uncomment to use Ur buffer
        # "Ul",     # Uncomment to use Ul buffer
    ]
    
    # Scramble types to generate - set True/False for each type
    GENERATE_3BLD = True              # Full 3x3 BLD scrambles
    GENERATE_4BLD = False              # Full 4x4 BLD scrambles
    GENERATE_5BLD = False             # Full 5x5 BLD scrambles
    GENERATE_EDGES_ONLY = False       # 3x3 edges-only scrambles
    GENERATE_CORNERS_ONLY = False     # 3x3 corners-only scrambles
    GENERATE_4BLD_CENTERS_ONLY = False # 4x4 centers-only scrambles
    GENERATE_4BLD_WINGS_ONLY = False   # 4x4 wings-only scrambles
    GENERATE_5BLD_EDGES_CORNERS = False # 5x5 edges+corners (5edge) scrambles
    
    # Scramble counts for each type
    COUNT_3BLD = 10000                  # Scrambles per 3BLD combo
    COUNT_4BLD = 100                  # Scrambles per 4BLD combo
    COUNT_5BLD = 50                   # Scrambles per 5BLD combo
    COUNT_EDGES_ONLY = 5000           # Scrambles per edges-only combo
    COUNT_CORNERS_ONLY = 5000        # Scrambles per corners-only combo
    COUNT_4BLD_CENTERS_ONLY = 300     # Scrambles per 4BLD centers combo
    COUNT_4BLD_WINGS_ONLY = 300       # Scrambles per 4BLD wings combo
    COUNT_5BLD_EDGES_CORNERS = 100    # Scrambles per 5BLD edges+corners combo

    # =====================================================================
    # END OF CONFIGURATION
    # =====================================================================
    
    # Display active configuration summary
    print("\n" + "="*80)
    print("ACTIVE CONFIGURATION SUMMARY")
    print("="*80)
    
    print("\nENABLED SCRAMBLE TYPES:")
    enabled_types = []
    if GENERATE_3BLD: enabled_types.append(f"3BLD ({COUNT_3BLD} per combo)")
    if GENERATE_4BLD: enabled_types.append(f"4BLD ({COUNT_4BLD} per combo)")
    if GENERATE_5BLD: enabled_types.append(f"5BLD ({COUNT_5BLD} per combo)")
    if GENERATE_EDGES_ONLY: enabled_types.append(f"Edges Only ({COUNT_EDGES_ONLY} per combo)")
    if GENERATE_CORNERS_ONLY: enabled_types.append(f"Corners Only ({COUNT_CORNERS_ONLY} per combo)")
    if GENERATE_4BLD_CENTERS_ONLY: enabled_types.append(f"4BLD Centers Only ({COUNT_4BLD_CENTERS_ONLY} per combo)")
    if GENERATE_4BLD_WINGS_ONLY: enabled_types.append(f"4BLD Wings Only ({COUNT_4BLD_WINGS_ONLY} per combo)")
    if GENERATE_5BLD_EDGES_CORNERS: enabled_types.append(f"5BLD Edges+Corners ({COUNT_5BLD_EDGES_CORNERS} per combo)")
    
    if not enabled_types:
        print("  No scramble types enabled")
    else:
        for type_name in enabled_types:
            print(f"  • {type_name}")
    
    print("\nACTIVE BUFFER CONFIGURATIONS:")
    print(f"  Edge buffers:     {len(USE_EDGE_BUFFERS)} active ({', '.join(USE_EDGE_BUFFERS)})")
    print(f"  Corner buffers:   {len(USE_CORNER_BUFFERS)} active ({', '.join(USE_CORNER_BUFFERS)})")
    print(f"  Wing buffers:     {len(USE_WING_BUFFERS)} active ({', '.join(USE_WING_BUFFERS)})")
    print(f"  X-center buffers: {len(USE_XCENTER_BUFFERS)} active ({', '.join(USE_XCENTER_BUFFERS)})")
    print(f"  T-center buffers: {len(USE_TCENTER_BUFFERS)} active ({', '.join(USE_TCENTER_BUFFERS)})")
    
    # Calculate active combinations
    print("\nACTIVE COMBINATIONS:")
    active_combos = {}
    active_counts = {}
    
    if GENERATE_3BLD:
        active_combos["3BLD"] = len(USE_EDGE_BUFFERS) * len(USE_CORNER_BUFFERS)
        active_counts["3BLD"] = active_combos["3BLD"] * COUNT_3BLD
        print(f"  3BLD:                  {active_combos['3BLD']} combinations = {active_counts['3BLD']:,} scrambles")
    
    if GENERATE_4BLD:
        active_combos["4BLD"] = len(USE_CORNER_BUFFERS) * len(USE_WING_BUFFERS) * len(USE_XCENTER_BUFFERS)
        active_counts["4BLD"] = active_combos["4BLD"] * COUNT_4BLD
        print(f"  4BLD:                  {active_combos['4BLD']} combinations = {active_counts['4BLD']:,} scrambles")
    
    if GENERATE_5BLD:
        active_combos["5BLD"] = len(USE_CORNER_BUFFERS) * len(USE_EDGE_BUFFERS) * len(USE_WING_BUFFERS) * len(USE_XCENTER_BUFFERS) * len(USE_TCENTER_BUFFERS)
        active_counts["5BLD"] = active_combos["5BLD"] * COUNT_5BLD
        print(f"  5BLD:                  {active_combos['5BLD']} combinations = {active_counts['5BLD']:,} scrambles")
    
    if GENERATE_EDGES_ONLY:
        active_combos["Edges"] = len(USE_EDGE_BUFFERS)
        active_counts["Edges"] = active_combos["Edges"] * COUNT_EDGES_ONLY
        print(f"  Edges Only:            {active_combos['Edges']} combinations = {active_counts['Edges']:,} scrambles")
    
    if GENERATE_CORNERS_ONLY:
        active_combos["Corners"] = len(USE_CORNER_BUFFERS)
        active_counts["Corners"] = active_combos["Corners"] * COUNT_CORNERS_ONLY
        print(f"  Corners Only:          {active_combos['Corners']} combinations = {active_counts['Corners']:,} scrambles")
    
    if GENERATE_4BLD_CENTERS_ONLY:
        active_combos["4Centers"] = len(USE_XCENTER_BUFFERS)
        active_counts["4Centers"] = active_combos["4Centers"] * COUNT_4BLD_CENTERS_ONLY
        print(f"  4BLD Centers Only:     {active_combos['4Centers']} combinations = {active_counts['4Centers']:,} scrambles")
    
    if GENERATE_4BLD_WINGS_ONLY:
        active_combos["4Wings"] = len(USE_WING_BUFFERS)
        active_counts["4Wings"] = active_combos["4Wings"] * COUNT_4BLD_WINGS_ONLY
        print(f"  4BLD Wings Only:       {active_combos['4Wings']} combinations = {active_counts['4Wings']:,} scrambles")
    
    if GENERATE_5BLD_EDGES_CORNERS:
        active_combos["5EC"] = len(USE_CORNER_BUFFERS) * len(USE_EDGE_BUFFERS)
        active_counts["5EC"] = active_combos["5EC"] * COUNT_5BLD_EDGES_CORNERS
        print(f"  5BLD Edges+Corners:    {active_combos['5EC']} combinations = {active_counts['5EC']:,} scrambles")
    
    # Calculate total active combinations and scrambles
    total_active_combos = sum(active_combos.values())
    total_active_scrambles = sum(active_counts.values())
    
    print("\nTOTAL ACTIVE COMBINATIONS: " + str(total_active_combos))
    print(f"TOTAL SCRAMBLES TO GENERATE: {total_active_scrambles:,}")
    
    # Add file size estimate
    est_size_mb = total_active_scrambles * 8 / 30000  # Based on 30000 solves = 8 MB
    if est_size_mb < 1:
        print(f"ESTIMATED FILE SIZE: {est_size_mb*1024:.1f} KB")
    elif est_size_mb < 1024:
        print(f"ESTIMATED FILE SIZE: {est_size_mb:.1f} MB")
    else:
        print(f"ESTIMATED FILE SIZE: {est_size_mb/1024:.2f} GB")
    
    print("="*80)
    print()
    
    # Generate all scramble configurations based on enabled settings
    scramble_configs = []
    
    # 3BLD configurations - cartesian product of selected edge and corner buffers
    if GENERATE_3BLD:
        count = str(COUNT_3BLD)
        for edge_pos in USE_EDGE_BUFFERS:
            for corner_pos in USE_CORNER_BUFFERS:
                edge_letter = edge_buffers[edge_pos]
                corner_letter = corner_buffers[corner_pos]
                scramble_configs.append({
                    "type": "333ni",
                    "corner_buffer": corner_letter,
                    "edge_buffer": edge_letter,
                    "count": count,
                    "description": f"3BLD {edge_pos}-{corner_pos}"
                })
    
    # 4BLD configurations - cartesian product of selected corner, wing, and xcenter buffers
    if GENERATE_4BLD:
        count = str(COUNT_4BLD)
        for corner_pos in USE_CORNER_BUFFERS:
            for wing_pos in USE_WING_BUFFERS:
                for xcenter_pos in USE_XCENTER_BUFFERS:
                    corner_letter = corner_buffers[corner_pos]
                    wing_letter = wing_buffers[wing_pos]
                    xcenter_letter = xcenter_buffers[xcenter_pos]
                    scramble_configs.append({
                        "type": "444bld",
                        "corner_buffer": corner_letter,
                        "wing_buffer": wing_letter,
                        "xcenter_buffer": xcenter_letter,
                        "count": count,
                        "description": f"4BLD {corner_pos}-{wing_pos}-{xcenter_pos}"
                    })
    
    # 5BLD configurations - cartesian product of all selected buffer types
    if GENERATE_5BLD:
        count = str(COUNT_5BLD)
        for corner_pos in USE_CORNER_BUFFERS:
            for edge_pos in USE_EDGE_BUFFERS:
                for wing_pos in USE_WING_BUFFERS:
                    for xcenter_pos in USE_XCENTER_BUFFERS:
                        for tcenter_pos in USE_TCENTER_BUFFERS:
                            corner_letter = corner_buffers[corner_pos]
                            edge_letter = edge_buffers[edge_pos]
                            wing_letter = wing_buffers[wing_pos]
                            xcenter_letter = xcenter_buffers[xcenter_pos]
                            tcenter_letter = tcenter_buffers[tcenter_pos]
                            scramble_configs.append({
                                "type": "555bld",
                                "corner_buffer": corner_letter,
                                "edge_buffer": edge_letter,
                                "wing_buffer": wing_letter,
                                "xcenter_buffer": xcenter_letter,
                                "tcenter_buffer": tcenter_letter,
                                "count": count,
                                "description": f"5BLD {corner_pos}-{edge_pos}-{wing_pos}-{xcenter_pos}-{tcenter_pos}"
                            })
    
    # Edges-only configurations
    if GENERATE_EDGES_ONLY:
        count = str(COUNT_EDGES_ONLY)
        for edge_pos in USE_EDGE_BUFFERS:
            edge_letter = edge_buffers[edge_pos]
            scramble_configs.append({
                "type": "edges",
                "edge_buffer": edge_letter,
                "count": count,
                "description": f"Edges Only - {edge_pos}"
            })
    
    # Corners-only configurations
    if GENERATE_CORNERS_ONLY:
        count = str(COUNT_CORNERS_ONLY)
        for corner_pos in USE_CORNER_BUFFERS:
            corner_letter = corner_buffers[corner_pos]
            scramble_configs.append({
                "type": "corners",
                "corner_buffer": corner_letter,
                "count": count,
                "description": f"Corners Only - {corner_pos}"
            })
    
    # 4BLD Centers-only configurations
    if GENERATE_4BLD_CENTERS_ONLY:
        count = str(COUNT_4BLD_CENTERS_ONLY)
        for xcenter_pos in USE_XCENTER_BUFFERS:
            xcenter_letter = xcenter_buffers[xcenter_pos]
            scramble_configs.append({
                "type": "444cto",
                "xcenter_buffer": xcenter_letter,
                "count": count,
                "description": f"4BLD Centers Only - {xcenter_pos}"
            })
    
    # 4BLD Wings-only configurations
    if GENERATE_4BLD_WINGS_ONLY:
        count = str(COUNT_4BLD_WINGS_ONLY)
        for wing_pos in USE_WING_BUFFERS:
            wing_letter = wing_buffers[wing_pos]
            scramble_configs.append({
                "type": "444edo",
                "wing_buffer": wing_letter,
                "count": count,
                "description": f"4BLD Wings Only - {wing_pos}"
            })
            
    # 5BLD Edges+Corners configurations (5edge)
    if GENERATE_5BLD_EDGES_CORNERS:
        count = str(COUNT_5BLD_EDGES_CORNERS)
        for corner_pos in USE_CORNER_BUFFERS:
            for edge_pos in USE_EDGE_BUFFERS:
                corner_letter = corner_buffers[corner_pos]
                edge_letter = edge_buffers[edge_pos]
                scramble_configs.append({
                    "type": "5edge",
                    "corner_buffer": corner_letter,
                    "edge_buffer": edge_letter,
                    "count": count,
                    "description": f"5BLD Edges+Corners - {corner_pos}-{edge_pos}"
                })

    # Print summary of what will be generated
    if len(scramble_configs) == 0:
        print("No scramble configurations selected. Please enable at least one scramble type.")
        return
    
    # Calculate total scrambles
    total_scrambles = sum(int(config["count"]) for config in scramble_configs)
    
    print("\n" + "="*60)
    print(f"=== SCRAMBLE GENERATION PLAN: {total_scrambles} TOTAL SCRAMBLES ===")
    print("="*60)
    
    print(f"\nWill generate {len(scramble_configs)} different scramble sets:")
    
    # Group by type for nicer display
    type_groups = {}
    for config in scramble_configs:
        scramble_type = config["type"]
        if scramble_type not in type_groups:
            type_groups[scramble_type] = []
        type_groups[scramble_type].append(config)
    
    # Calculate scrambles by type
    type_totals = {}
    for scramble_type, configs in type_groups.items():
        scrambles = sum(int(config["count"]) for config in configs)
        type_totals[scramble_type] = scrambles
    
    for scramble_type, configs in type_groups.items():
        type_name = {
            "333ni": "3BLD",
            "444bld": "4BLD",
            "555bld": "5BLD",
            "edges": "Edges Only",
            "corners": "Corners Only",
            "444cto": "4BLD Centers Only",
            "444edo": "4BLD Wings Only",
            "5edge": "5BLD Edges+Corners"
        }.get(scramble_type, scramble_type)
        
        scrambles = type_totals[scramble_type]
        
        print(f"\n{type_name} ({len(configs)} configurations, {scrambles} scrambles):")
        for i, config in enumerate(configs):
            print(f"  {i+1}. {config['count']} {config['description']} scrambles")
    
    print("\n=== BUFFER USAGE SUMMARY ===")
    print("Edge buffers:     " + ", ".join(USE_EDGE_BUFFERS))
    print("Corner buffers:   " + ", ".join(USE_CORNER_BUFFERS))
    print("Wing buffers:     " + ", ".join(USE_WING_BUFFERS))
    print("X-center buffers: " + ", ".join(USE_XCENTER_BUFFERS))
    print("T-center buffers: " + ", ".join(USE_TCENTER_BUFFERS))
    
    print("\n" + "="*60)
    print(f"TOTAL SCRAMBLES TO GENERATE: {total_scrambles}")
    print("="*60)
    
    # Ask user to confirm
    confirm = input("\nGenerate these scrambles? (y/n): ")
    if confirm.lower() != 'y':
        print("Scramble generation cancelled.")
        return
    
    # Generate all scrambles
    change_base_scheme = "true"
    
    # Track completed scrambles for overall progress
    completed_scrambles = 0
    start_time = time.time()
    
    for i, config in enumerate(scramble_configs):
        scramble_type = config["type"]
        count_scramble = config["count"]
        description = config.get("description", scramble_type)
        
        # Progress header for this scramble set
        print(f"\n[{i+1}/{len(scramble_configs)}] Generating {count_scramble} {description} scrambles")
        print(f"Overall progress: {completed_scrambles}/{total_scrambles} scrambles ({completed_scrambles/total_scrambles*100:.1f}%)")
        
        # Build command with buffer arguments
        cmd = [
            "python.exe",
            "generate_solves.py",
            count_scramble,
            scramble_type,
            change_base_scheme
        ]

        # Add buffer parameters if they exist in the config
        if "corner_buffer" in config:
            cmd.extend(["--corner_buffer", config["corner_buffer"]])
        if "edge_buffer" in config:
            cmd.extend(["--edge_buffer", config["edge_buffer"]])
        if "wing_buffer" in config:
            cmd.extend(["--wing_buffer", config["wing_buffer"]])
        if "xcenter_buffer" in config:
            cmd.extend(["--xcenter_buffer", config["xcenter_buffer"]])
        if "tcenter_buffer" in config:
            cmd.extend(["--tcenter_buffer", config["tcenter_buffer"]])

        # Start the process
        process = subprocess.Popen(cmd, 
                                   stdout=subprocess.PIPE, 
                                   stderr=subprocess.PIPE,
                                   bufsize=1,
                                   universal_newlines=True)
        
        # Run the progress bar in a separate thread
        progress_thread = threading.Thread(target=progress_bar, args=(process,))
        progress_thread.daemon = True
        progress_thread.start()
        
        # Wait for process to complete
        stdout, stderr = process.communicate()
        
        # Wait for progress thread to finish
        progress_thread.join()
        
        # Update completed count
        completed_scrambles += int(count_scramble)
        
        # Calculate elapsed time
        elapsed = time.time() - start_time
        
        # Show completion message and overall progress
        print(f"✓ Completed {description} scrambles")
        print(f"Overall progress: {completed_scrambles}/{total_scrambles} scrambles ({completed_scrambles/total_scrambles*100:.1f}%)")
        print(f"Elapsed time: {int(elapsed//60)}m {int(elapsed%60)}s")
        print("-"*60)
    
    # Show completion message
    total_elapsed = time.time() - start_time
    hours, remainder = divmod(total_elapsed, 3600)
    minutes, seconds = divmod(remainder, 60)
    elapsed_str = ""
    if hours > 0:
        elapsed_str += f"{int(hours)}h "
    if minutes > 0 or hours > 0:
        elapsed_str += f"{int(minutes)}m "
    elapsed_str += f"{int(seconds)}s"
    
    print("\n" + "="*60)
    print(f"GENERATION COMPLETE! Generated {total_scrambles} scrambles")
    print(f"Total time: {elapsed_str}")
    print("="*60)

if __name__ == '__main__':
    main()