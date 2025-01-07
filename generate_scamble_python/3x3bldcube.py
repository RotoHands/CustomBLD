import random
from typing import List, Dict, Any

class ThreeBldCube:
    CORNERS = 0
    EDGES = 1
    CENTERS = 2
    NO_SCRAMBLE = "None"

    def __init__(self, scramble: str = None):
        self.corners = [i for i in range(24)]
        self.edges = [i for i in range(24)]
        self.centers = [i for i in range(6)]
        self.corner_lettering = list("ABCDEFGHIJKLMNOPQRSTUVWX")
        self.edge_lettering = list("ABCDEFGHIJKLMNOPQRSTUVWX")
        self.corner_positions = ["UBL", "URB", "UFR", "ULF", "DFL", "DRF", "DBR", "DLB"]
        self.edge_positions = ["DF", "UB", "UR", "UF", "UL", "BR", "BL", "FR", "FL", "DR", "DB", "DL"]
        self.corner_cubies = [
            [0, 4, 17], [1, 16, 13], [2, 12, 9], [3, 8, 5],
            [11, 20, 6], [15, 21, 10], [19, 22, 14], [7, 23, 18]
        ]
        self.solved_corners = [True] * 8
        self.edge_cubies = [
            [20, 10], [0, 16], [1, 12], [2, 8],
            [3, 4], [17, 7], [19, 13], [11, 5],
            [9, 15], [21, 14], [22, 18], [23, 6]
        ]
        self.solved_edges = [True] * 12
        self.scramble = scramble or self.scramble_cube_3x3()
        self.permutations = {}
        self.init_permutations()
        self.parse_scramble(self.scramble)

    def scramble_cube_3x3(self):
        face_names = ["U", "U'", "U2", "F", "F'", "F2", "R", "R'", "R2", "L", "L'", "L2", "B", "B'", "B2", "D", "D'", "D2"]
        scramble = []
        prev_face = None
        for _ in range(24):
            face = random.choice(face_names)
            while prev_face and face[0] == prev_face[0]:
                face = random.choice(face_names)
            scramble.append(face)
            prev_face = face
        return " ".join(scramble)

    def init_permutations(self):
        face_names = ["U", "U'", "U2", "F", "F'", "F2", "R", "R'", "R2", "L", "L'", "L2", "B", "B'", "B2", "D", "D'", "D2"]
        for face in face_names:
            self.permutations[face] = {
                self.CORNERS: [None] * 24,
                self.EDGES: [None] * 24,
                self.CENTERS: [None] * 6
            }

    def parse_scramble(self, scramble_string: str):
        self.reset_cube(False)
        moves = scramble_string.split()
        for move in moves:
            self.permute(move)

    def reset_cube(self, orientation_only: bool):
        for i in range(24):
            if not orientation_only:
                self.corners[i] = i
                self.edges[i] = i
                if i < 6:
                    self.centers[i] = i
            if i < 8:
                self.solved_corners[i] = False
            if i < 12:
                self.solved_edges[i] = False

    def permute(self, permutation: str):
        perm = self.permutations.get(permutation, {}).get(self.CORNERS)
        if perm:
            self.corners = [perm[i] if perm[i] is not None else self.corners[i] for i in range(24)]

        perm = self.permutations.get(permutation, {}).get(self.EDGES)
        if perm:
            self.edges = [perm[i] if perm[i] is not None else self.edges[i] for i in range(24)]

        perm = self.permutations.get(permutation, {}).get(self.CENTERS)
        if perm:
            self.centers = [perm[i] if perm[i] is not None else self.centers[i] for i in range(6)]

    def solve_cube(self):
        self.reorient_cube()
        self.solve_corners()
        self.solve_edges()

    def reorient_cube(self):
        def get_rotations_from_orientation(top, front, centers):
            reorientation = {
                (0, 1): "y'", (0, 2): "", (0, 3): "y", (0, 4): "y2",
                (1, 0): "y", (1, 2): "z y", (1, 3): "z", (1, 4): "z y2", (1, 5): "z y'",
                (2, 0): "", (2, 1): "x y2", (2, 3): "x y", (2, 4): "x",
                (3, 0): "y'", (3, 1): "z' y'", (3, 2): "z'", (3, 4): "z' y2", (3, 5): "z' y",
                (4, 0): "y2", (4, 1): "x' y'", (4, 2): "x' y", (4, 3): "x' y2", (4, 5): "x'",
                (5, 1): "x2 y'", (5, 2): "z2", (5, 3): "x2 y", (5, 4): "z2 y2"
            }

            for i, top_center in enumerate(centers):
                if top_center == top:
                    for j, front_center in enumerate(centers):
                        if front_center == front and i != j:
                            return reorientation.get((i, j), "")
            return ""

        top = self.centers[0]  # Assume UP face
        front = self.centers[2]  # Assume FRONT face
        needed_rotation = get_rotations_from_orientation(top, front, self.centers)

        if needed_rotation:
            for rotation in needed_rotation.split():
                self.permute(rotation)

    def solve_corners(self):
        self.solved_corners = [True] * len(self.solved_corners)

    def solve_edges(self):
        self.solved_edges = [True] * len(self.solved_edges)

    def get_scramble(self):
        return self.scramble

    def get_corner_pairs(self):
        pairs = []
        for i, is_solved in enumerate(self.solved_corners):
            if not is_solved:
                pairs.append(self.corner_positions[i])
        return pairs

    def get_edge_pairs(self):
        pairs = []
        for i, is_solved in enumerate(self.solved_edges):
            if not is_solved:
                pairs.append(self.edge_positions[i])
        return pairs

# Example usage:
cube = ThreeBldCube()
print(cube.get_scramble())
cube.solve_cube()
