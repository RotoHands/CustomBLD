import subprocess
import glob


def main():

    scramble_types = ["333ni", "edges", "corners", "555bld", "5edge", "444bld", "444cto", "444edo"]
    count_scramble = "10"
    for s in scramble_types:
        cmd = [
            "python.exe",
            "scramble_generator/generate_all_scrambles.py",
            count_scramble,
            s
        ]
        subprocess.run(cmd)

   

if __name__ == '__main__':
    main()