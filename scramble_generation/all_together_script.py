import subprocess
import glob
import time
import sys
import os
import threading
import yaml

def print_combination_summary():
    """Print a summary of all potential scramble combinations and total counts"""
    print("\n" + "="*80)
    print("SCRAMBLE GENERATION SUMMARY - ALL POSSIBLE COMBINATIONS")
    print("="*80)
    
    # Edge buffers
    edge_buffers = ["UF", "FU", "DF", "UR"]
    print(f"Edge Buffers ({len(edge_buffers)}): {', '.join(edge_buffers)}")
    
    # Corner buffers
    corner_buffers = ["UFR", "UBL", "UFL", "RFD"]
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
    
    # User input to continue
    input("\nPress Enter to continue to configuration...")

def print_enabled_summary(use_edge_buffers, use_corner_buffers, use_wing_buffers, 
                        use_xcenter_buffers, use_tcenter_buffers,
                        generate_3bld, generate_4bld, generate_5bld,
                        generate_edges_only, generate_corners_only,
                        generate_4bld_centers_only, generate_4bld_wings_only,
                        generate_5bld_edges_corners,
                        count_3bld, count_4bld, count_5bld,
                        count_edges_only, count_corners_only,
                        count_4bld_centers_only, count_4bld_wings_only,
                        count_5bld_edges_corners):
    """Print a summary of only enabled scramble configurations"""
    print("\n" + "="*80)
    print("SCRAMBLE COUNTS - ENABLED CONFIGURATIONS ONLY")
    print("="*80)
    
    print("\nENABLED SCRAMBLE COUNTS:")
    enabled_counts = []
    total_scrambles = 0
    total_combinations = 0
    
    # Calculate and display counts only for enabled types
    if generate_3bld:
        combos = len(use_edge_buffers) * len(use_corner_buffers)
        count = count_3bld * combos
        total_scrambles += count
        total_combinations += combos
        enabled_counts.append(f"3BLD:                  {count_3bld} × {combos} = {count:,} scrambles")
    
    if generate_4bld:
        combos = len(use_corner_buffers) * len(use_wing_buffers) * len(use_xcenter_buffers)
        count = count_4bld * combos
        total_scrambles += count
        total_combinations += combos
        enabled_counts.append(f"4BLD:                  {count_4bld} × {combos} = {count:,} scrambles")
    
    if generate_5bld:
        combos = len(use_corner_buffers) * len(use_edge_buffers) * len(use_wing_buffers) * len(use_xcenter_buffers) * len(use_tcenter_buffers)
        count = count_5bld * combos
        total_scrambles += count
        total_combinations += combos
        enabled_counts.append(f"5BLD:                  {count_5bld} × {combos} = {count:,} scrambles")
    
    if generate_edges_only:
        combos = len(use_edge_buffers)
        count = count_edges_only * combos
        total_scrambles += count
        total_combinations += combos
        enabled_counts.append(f"Edges Only:            {count_edges_only} × {combos} = {count:,} scrambles")
    
    if generate_corners_only:
        combos = len(use_corner_buffers)
        count = count_corners_only * combos
        total_scrambles += count
        total_combinations += combos
        enabled_counts.append(f"Corners Only:          {count_corners_only} × {combos} = {count:,} scrambles")
    
    if generate_4bld_centers_only:
        combos = len(use_xcenter_buffers)
        count = count_4bld_centers_only * combos
        total_scrambles += count
        total_combinations += combos
        enabled_counts.append(f"4BLD Centers Only:     {count_4bld_centers_only} × {combos} = {count:,} scrambles")
    
    if generate_4bld_wings_only:
        combos = len(use_wing_buffers)
        count = count_4bld_wings_only * combos
        total_scrambles += count
        total_combinations += combos
        enabled_counts.append(f"4BLD Wings Only:       {count_4bld_wings_only} × {combos} = {count:,} scrambles")
    
    if generate_5bld_edges_corners:
        combos = len(use_edge_buffers) * len(use_corner_buffers)
        count = count_5bld_edges_corners * combos
        total_scrambles += count
        total_combinations += combos
        enabled_counts.append(f"5BLD Edges+Corners:    {count_5bld_edges_corners} × {combos} = {count:,} scrambles")
    
    # Print all enabled counts
    if not enabled_counts:
        print("  No scramble types enabled!")
    else:
        for count_line in enabled_counts:
            print(count_line)
    
    # Print summary
    print(f"\nTOTAL ENABLED COMBINATIONS: {total_combinations}")
    print(f"TOTAL ENABLED SCRAMBLES: {total_scrambles:,}")
    
    # Calculate file size estimate
    est_size_mb = total_scrambles * 8 / 30000  # Based on 30000 solves = 8 MB
    if est_size_mb < 1:
        print(f"ESTIMATED FILE SIZE: {est_size_mb*1024:.1f} KB")
    elif est_size_mb < 1024:
        print(f"ESTIMATED FILE SIZE: {est_size_mb:.1f} MB")
    else:
        print(f"ESTIMATED FILE SIZE: {est_size_mb/1024:.2f} GB")
        
    print("="*80)
    
    # Return values for use in main script
    return total_combinations, total_scrambles

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
            sys.stdout.write('\r' + ' ' * 50 + '\r')
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
    # Load configuration from YAML file
    with open('config_scramble_generation.yaml', 'r') as f:
        config = yaml.safe_load(f)
    
    # Extract configuration values
    USE_EDGE_BUFFERS = config['use_edge_buffers']
    USE_CORNER_BUFFERS = config['use_corner_buffers']
    USE_WING_BUFFERS = config['use_wing_buffers']
    USE_XCENTER_BUFFERS = config['use_xcenter_buffers']
    USE_TCENTER_BUFFERS = config['use_tcenter_buffers']
    
    GENERATE_3BLD = config['generate']['3bld']
    GENERATE_4BLD = config['generate']['4bld']
    GENERATE_5BLD = config['generate']['5bld']
    GENERATE_EDGES_ONLY = config['generate']['edges_only']
    GENERATE_CORNERS_ONLY = config['generate']['corners_only']
    GENERATE_4BLD_CENTERS_ONLY = config['generate']['4bld_centers_only']
    GENERATE_4BLD_WINGS_ONLY = config['generate']['4bld_wings_only']
    GENERATE_5BLD_EDGES_CORNERS = config['generate']['5bld_edges_corners']
    
    COUNT_3BLD = config['counts']['3bld']
    COUNT_4BLD = config['counts']['4bld']
    COUNT_5BLD = config['counts']['5bld']
    COUNT_EDGES_ONLY = config['counts']['edges_only']
    COUNT_CORNERS_ONLY = config['counts']['corners_only']
    COUNT_4BLD_CENTERS_ONLY = config['counts']['4bld_centers_only']
    COUNT_4BLD_WINGS_ONLY = config['counts']['4bld_wings_only']
    COUNT_5BLD_EDGES_CORNERS = config['counts']['5bld_edges_corners']

    # Get specific combinations if they exist
    SPECIFIC_COMBINATIONS = config.get('specific_combinations', {})
    
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

    # Display only enabled scramble counts
    total_active_combos, total_active_scrambles = print_enabled_summary(
        USE_EDGE_BUFFERS, USE_CORNER_BUFFERS, USE_WING_BUFFERS, 
        USE_XCENTER_BUFFERS, USE_TCENTER_BUFFERS,
        GENERATE_3BLD, GENERATE_4BLD, GENERATE_5BLD,
        GENERATE_EDGES_ONLY, GENERATE_CORNERS_ONLY,
        GENERATE_4BLD_CENTERS_ONLY, GENERATE_4BLD_WINGS_ONLY,
        GENERATE_5BLD_EDGES_CORNERS,
        COUNT_3BLD, COUNT_4BLD, COUNT_5BLD,
        COUNT_EDGES_ONLY, COUNT_CORNERS_ONLY,
        COUNT_4BLD_CENTERS_ONLY, COUNT_4BLD_WINGS_ONLY,
        COUNT_5BLD_EDGES_CORNERS
    )
    
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
        if '3bld' in SPECIFIC_COMBINATIONS:
            active_combos["3BLD"] = len(SPECIFIC_COMBINATIONS['3bld'])
        else:
            active_combos["3BLD"] = len(USE_EDGE_BUFFERS) * len(USE_CORNER_BUFFERS)
        active_counts["3BLD"] = active_combos["3BLD"] * COUNT_3BLD
        print(f"  3BLD:                  {active_combos['3BLD']} combinations = {active_counts['3BLD']:,} scrambles")
    
    if GENERATE_4BLD:
        if '4bld' in SPECIFIC_COMBINATIONS:
            active_combos["4BLD"] = len(SPECIFIC_COMBINATIONS['4bld'])
        else:
            active_combos["4BLD"] = len(USE_CORNER_BUFFERS) * len(USE_WING_BUFFERS) * len(USE_XCENTER_BUFFERS)
        active_counts["4BLD"] = active_combos["4BLD"] * COUNT_4BLD
        print(f"  4BLD:                  {active_combos['4BLD']} combinations = {active_counts['4BLD']:,} scrambles")
    
    if GENERATE_5BLD:
        if '5bld' in SPECIFIC_COMBINATIONS:
            active_combos["5BLD"] = len(SPECIFIC_COMBINATIONS['5bld'])
        else:
            active_combos["5BLD"] = len(USE_CORNER_BUFFERS) * len(USE_EDGE_BUFFERS) * len(USE_WING_BUFFERS) * len(USE_XCENTER_BUFFERS) * len(USE_TCENTER_BUFFERS)
        active_counts["5BLD"] = active_combos["5BLD"] * COUNT_5BLD
        print(f"  5BLD:                  {active_combos['5BLD']} combinations = {active_counts['5BLD']:,} scrambles")
    
    if GENERATE_EDGES_ONLY:
        if 'edges_only' in SPECIFIC_COMBINATIONS:
            active_combos["Edges"] = len(SPECIFIC_COMBINATIONS['edges_only'])
        else:
            active_combos["Edges"] = len(USE_EDGE_BUFFERS)
        active_counts["Edges"] = active_combos["Edges"] * COUNT_EDGES_ONLY
        print(f"  Edges Only:            {active_combos['Edges']} combinations = {active_counts['Edges']:,} scrambles")
    
    if GENERATE_CORNERS_ONLY:
        if 'corners_only' in SPECIFIC_COMBINATIONS:
            active_combos["Corners"] = len(SPECIFIC_COMBINATIONS['corners_only'])
        else:
            active_combos["Corners"] = len(USE_CORNER_BUFFERS)
        active_counts["Corners"] = active_combos["Corners"] * COUNT_CORNERS_ONLY
        print(f"  Corners Only:          {active_combos['Corners']} combinations = {active_counts['Corners']:,} scrambles")
    
    if GENERATE_4BLD_CENTERS_ONLY:
        if '4bld_centers_only' in SPECIFIC_COMBINATIONS:
            active_combos["4Centers"] = len(SPECIFIC_COMBINATIONS['4bld_centers_only'])
        else:
            active_combos["4Centers"] = len(USE_XCENTER_BUFFERS)
        active_counts["4Centers"] = active_combos["4Centers"] * COUNT_4BLD_CENTERS_ONLY
        print(f"  4BLD Centers Only:     {active_combos['4Centers']} combinations = {active_counts['4Centers']:,} scrambles")
    
    if GENERATE_4BLD_WINGS_ONLY:
        if '4bld_wings_only' in SPECIFIC_COMBINATIONS:
            active_combos["4Wings"] = len(SPECIFIC_COMBINATIONS['4bld_wings_only'])
        else:
            active_combos["4Wings"] = len(USE_WING_BUFFERS)
        active_counts["4Wings"] = active_combos["4Wings"] * COUNT_4BLD_WINGS_ONLY
        print(f"  4BLD Wings Only:       {active_combos['4Wings']} combinations = {active_counts['4Wings']:,} scrambles")
    
    if GENERATE_5BLD_EDGES_CORNERS:
        if '5bld_edges_corners' in SPECIFIC_COMBINATIONS:
            active_combos["5EC"] = len(SPECIFIC_COMBINATIONS['5bld_edges_corners'])
        else:
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
    
    # Add 3BLD configurations if enabled
    if GENERATE_3BLD:
        if '3bld' in SPECIFIC_COMBINATIONS:
            # Use specific combinations
            for combo in SPECIFIC_COMBINATIONS['3bld']:
                scramble_configs.append({
                    "type": "333ni",
                    "count": str(COUNT_3BLD),
                    "description": f"3BLD {combo['edge_buffer']}-{combo['corner_buffer']}",
                    "edge_buffer": edge_buffers[combo['edge_buffer']],
                    "corner_buffer": corner_buffers[combo['corner_buffer']]
                })
        else:
            # Use all combinations
            for edge_buffer in USE_EDGE_BUFFERS:
                for corner_buffer in USE_CORNER_BUFFERS:
                    scramble_configs.append({
                        "type": "333ni",
                        "count": str(COUNT_3BLD),
                        "description": f"3BLD {edge_buffer}-{corner_buffer}",
                        "edge_buffer": edge_buffers[edge_buffer],
                        "corner_buffer": corner_buffers[corner_buffer]
                    })
    
    # Add 4BLD configurations if enabled
    if GENERATE_4BLD:
        if '4bld' in SPECIFIC_COMBINATIONS:
            # Use specific combinations
            for combo in SPECIFIC_COMBINATIONS['4bld']:
                scramble_configs.append({
                    "type": "444bld",
                    "count": str(COUNT_4BLD),
                    "description": f"4BLD {combo['wing_buffer']}-{combo['corner_buffer']} wings-corners",
                    "wing_buffer": wing_buffers[combo['wing_buffer']],
                    "corner_buffer": corner_buffers[combo['corner_buffer']],
                    "xcenter_buffer": xcenter_buffers[combo['xcenter_buffer']]
                })
        else:
            # Use all combinations
            for corner_buffer in USE_CORNER_BUFFERS:
                for wing_buffer in USE_WING_BUFFERS:
                    for xcenter_buffer in USE_XCENTER_BUFFERS:
                        scramble_configs.append({
                            "type": "444bld",
                            "count": str(COUNT_4BLD),
                            "description": f"4BLD {wing_buffer}-{corner_buffer} wings-corners",
                            "wing_buffer": wing_buffers[wing_buffer],
                            "corner_buffer": corner_buffers[corner_buffer],
                            "xcenter_buffer": xcenter_buffers[xcenter_buffer]
                        })
    
    # Add 5BLD configurations if enabled
    if GENERATE_5BLD:
        if '5bld' in SPECIFIC_COMBINATIONS:
            # Use specific combinations
            for combo in SPECIFIC_COMBINATIONS['5bld']:
                scramble_configs.append({
                    "type": "555bld",
                    "count": str(COUNT_5BLD),
                    "description": f"5BLD {combo['edge_buffer']}-{combo['corner_buffer']}",
                    "edge_buffer": edge_buffers[combo['edge_buffer']],
                    "corner_buffer": corner_buffers[combo['corner_buffer']],
                    "wing_buffer": wing_buffers[combo['wing_buffer']],
                    "xcenter_buffer": xcenter_buffers[combo['xcenter_buffer']],
                    "tcenter_buffer": tcenter_buffers[combo['tcenter_buffer']]
                })
        else:
            # Use all combinations
            for edge_buffer in USE_EDGE_BUFFERS:
                for corner_buffer in USE_CORNER_BUFFERS:
                    for wing_buffer in USE_WING_BUFFERS:
                        for xcenter_buffer in USE_XCENTER_BUFFERS:
                            for tcenter_buffer in USE_TCENTER_BUFFERS:
                                scramble_configs.append({
                                    "type": "555bld",
                                    "count": str(COUNT_5BLD),
                                    "description": f"5BLD {edge_buffer}-{corner_buffer}",
                                    "edge_buffer": edge_buffers[edge_buffer],
                                    "corner_buffer": corner_buffers[corner_buffer],
                                    "wing_buffer": wing_buffers[wing_buffer],
                                    "xcenter_buffer": xcenter_buffers[xcenter_buffer],
                                    "tcenter_buffer": tcenter_buffers[tcenter_buffer]
                                })
    
    # Add edge-only configurations if enabled
    if GENERATE_EDGES_ONLY:
        if 'edges_only' in SPECIFIC_COMBINATIONS:
            # Use specific combinations
            for combo in SPECIFIC_COMBINATIONS['edges_only']:
                scramble_configs.append({
                    "type": "edges",
                    "count": str(COUNT_EDGES_ONLY),
                    "description": f"3BLD Edges Only {combo['edge_buffer']}",
                    "edge_buffer": edge_buffers[combo['edge_buffer']]
                })
        else:
            # Use all combinations
            for edge_buffer in USE_EDGE_BUFFERS:
                scramble_configs.append({
                    "type": "edges",
                    "count": str(COUNT_EDGES_ONLY),
                    "description": f"3BLD Edges Only {edge_buffer}",
                    "edge_buffer": edge_buffers[edge_buffer]
                })
    
    # Add corner-only configurations if enabled
    if GENERATE_CORNERS_ONLY:
        if 'corners_only' in SPECIFIC_COMBINATIONS:
            # Use specific combinations
            for combo in SPECIFIC_COMBINATIONS['corners_only']:
                scramble_configs.append({
                    "type": "corners",
                    "count": str(COUNT_CORNERS_ONLY),
                    "description": f"3BLD Corners Only {combo['corner_buffer']}",
                    "corner_buffer": corner_buffers[combo['corner_buffer']]
                })
        else:
            # Use all combinations
            for corner_buffer in USE_CORNER_BUFFERS:
                scramble_configs.append({
                    "type": "corners",
                    "count": str(COUNT_CORNERS_ONLY),
                    "description": f"3BLD Corners Only {corner_buffer}",
                    "corner_buffer": corner_buffers[corner_buffer]
                })
    
    # Add 4BLD centers-only configurations if enabled
    if GENERATE_4BLD_CENTERS_ONLY:
        if '4bld_centers_only' in SPECIFIC_COMBINATIONS:
            # Use specific combinations
            for combo in SPECIFIC_COMBINATIONS['4bld_centers_only']:
                scramble_configs.append({
                    "type": "444cto",
                    "count": str(COUNT_4BLD_CENTERS_ONLY),
                    "description": f"4BLD Centers Only {combo['xcenter_buffer']}",
                    "xcenter_buffer": xcenter_buffers[combo['xcenter_buffer']]
                })
        else:
            # Use all combinations
            for xcenter_buffer in USE_XCENTER_BUFFERS:
                scramble_configs.append({
                    "type": "444cto",
                    "count": str(COUNT_4BLD_CENTERS_ONLY),
                    "description": f"4BLD Centers Only {xcenter_buffer}",
                    "xcenter_buffer": xcenter_buffers[xcenter_buffer]
                })
    
    # Add 4BLD wings-only configurations if enabled
    if GENERATE_4BLD_WINGS_ONLY:
        if '4bld_wings_only' in SPECIFIC_COMBINATIONS:
            # Use specific combinations
            for combo in SPECIFIC_COMBINATIONS['4bld_wings_only']:
                scramble_configs.append({
                    "type": "444edo",
                    "count": str(COUNT_4BLD_WINGS_ONLY),
                    "description": f"4BLD Wings Only {combo['wing_buffer']}",
                    "wing_buffer": wing_buffers[combo['wing_buffer']]
                })
        else:
            # Use all combinations
            for wing_buffer in USE_WING_BUFFERS:
                scramble_configs.append({
                    "type": "444edo",
                    "count": str(COUNT_4BLD_WINGS_ONLY),
                    "description": f"4BLD Wings Only {wing_buffer}",
                    "wing_buffer": wing_buffers[wing_buffer]
                })
    
    # Add 5BLD edges+corners configurations if enabled
    if GENERATE_5BLD_EDGES_CORNERS:
        if '5bld_edges_corners' in SPECIFIC_COMBINATIONS:
            # Use specific combinations
            for combo in SPECIFIC_COMBINATIONS['5bld_edges_corners']:
                scramble_configs.append({
                    "type": "5edge",
                    "count": str(COUNT_5BLD_EDGES_CORNERS),
                    "description": f"5BLD Edges+Corners {combo['edge_buffer']}-{combo['corner_buffer']}",
                    "edge_buffer": edge_buffers[combo['edge_buffer']],
                    "corner_buffer": corner_buffers[combo['corner_buffer']]
                })
        else:
            # Use all combinations
            for edge_buffer in USE_EDGE_BUFFERS:
                for corner_buffer in USE_CORNER_BUFFERS:
                    scramble_configs.append({
                        "type": "5edge",
                        "count": str(COUNT_5BLD_EDGES_CORNERS),
                        "description": f"5BLD Edges+Corners {edge_buffer}-{corner_buffer}",
                        "edge_buffer": edge_buffers[edge_buffer],
                        "corner_buffer": corner_buffers[corner_buffer]
                    })
    
    # List available scheme files
    print("Available letter schemes:")
    scheme_files = glob.glob("*.csv")
    for i, file in enumerate(scheme_files):
        print(f"{i+1}. {file}")
    
    
    
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
        print(f"Overall progress: {completed_scrambles}/{total_active_scrambles} scrambles ({completed_scrambles/total_active_scrambles*100:.1f}%)")
        
        # Build command with buffer arguments
        cmd = [
            "python",
            "generate_solves.py",
            count_scramble,
            scramble_type,
            change_base_scheme
        ]
        print (config)
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
        print("here 2")
        print(cmd)
        # Start the process with the correct working directory
        process = subprocess.Popen(cmd, 
                                   stdout=subprocess.PIPE, 
                                   stderr=subprocess.PIPE,
                                   bufsize=1,
                                   universal_newlines=True,
                                   cwd=os.path.dirname(os.path.abspath(__file__)))  # Set working directory to script location
        
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
        print(f"Overall progress: {completed_scrambles}/{total_active_scrambles} scrambles ({completed_scrambles/total_active_scrambles*100:.1f}%)")
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
    print(f"GENERATION COMPLETE! Generated {total_active_scrambles} scrambles")
    print(f"Total time: {elapsed_str}")
    print("="*60)

if __name__ == '__main__':
    main()