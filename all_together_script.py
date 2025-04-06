import subprocess
import glob

def main():
    # Define scramble types with their respective buffers
    # Format: (scramble_type, corner_buffer, edge_buffer, wing_buffer, xcenter_buffer, tcenter_buffer)
    scramble_configs = [
        {
            "type": "333ni", 
            "corner_buffer": "A", 
            "edge_buffer": "C",
            "count": "2000"  # Number of scrambles to generate
        },
        # Uncomment and customize these for other scramble types
        # {
        #     "type": "edges",
        #     "corner_buffer": "C",
        #     "edge_buffer": "C",
        #     "count": "1000"
        # },
        # {
        #     "type": "corners",
        #     "corner_buffer": "C",
        #     "edge_buffer": "C",
        #     "count": "1000"
        # },
        {
            "type": "444bld",
            "corner_buffer": "A",
            "wing_buffer": "A",
            "xcenter_buffer": "A",
            "count": "100"  
        },
        # {
        #     "type": "444cto",
        #     "corner_buffer": "C",
        #     "wing_buffer": "C",
        #     "xcenter_buffer": "C",
        #     "count": "500"
        # },
        # {
        #     "type": "444edo",
        #     "corner_buffer": "C",
        #     "wing_buffer": "C",
        #     "xcenter_buffer": "C",
        #     "count": "500"
        # },
        {
            "type": "555bld",
            "corner_buffer": "C",
            "edge_buffer": "C",
            "wing_buffer": "C",
            "xcenter_buffer": "C",
            "tcenter_buffer": "C",
            "count": "100"  
        },
        # {
        #     "type": "5edge",
        #     "corner_buffer": "C",
        #     "edge_buffer": "C",
        #     "wing_buffer": "C",
        #     "xcenter_buffer": "C",
        #     "tcenter_buffer": "C",
        #     "count": "100"
        # }
    ]

    # Default count if not specified in the config
    default_count = "1000"
    change_base_scheme = "true"
    
    for config in scramble_configs:
        scramble_type = config["type"]
        
        # Get count from config or use default
        count_scramble = config.get("count", default_count)
        
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

        print(f"Generating {count_scramble} {scramble_type} scrambles with buffers: {config}")
        subprocess.run(cmd)

if __name__ == '__main__':
    main()