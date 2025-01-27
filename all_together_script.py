import subprocess
import glob


def main():

    scramble_types = ["333ni", "edges", "corners", "555bld", "5edge", "444bld", "444cto", "444edo"]
    scramble_types = ["444cto", "444edo"]#, "edges", "corners", "555bld", "5edge", "444bld", "444cto", "444edo"]
    count_scramble = "1"

    change_base_scheme = "true"
    for s in scramble_types:
        cmd = [
            "python.exe",
            "scramble_generator/generate_all_scrambles.py",
            count_scramble,
            s,
            change_base_scheme
        ]

        subprocess.run(cmd)
   

if __name__ == '__main__':
    main()