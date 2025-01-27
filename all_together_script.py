import subprocess
import glob


def main():

    scramble_types = ["333ni", "edges", "corners", "555bld", "5edge", "444bld", "444cto", "444edo"]
    scramble_types = ["333ni", "555bld", "444bld"]#, "edges", "corners"]#, "edges", "corners", "555bld", "5edge", "444bld", "444cto", "444edo"]
    count_scramble = "1"

    change_base_scheme = "true"
    for s in scramble_types:
        cmd = [
            "python.exe",
            "generate_solves.py",
            count_scramble,
            s,
            change_base_scheme
        ]

        subprocess.run(cmd)
   

if __name__ == '__main__':
    main()