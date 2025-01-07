import math
import re


def calc():
    edges = ['א', 'ב', 'ד', 'י', 'ר', 'ח', 'ל', 'ש', 'ת', "כ", "ג'"]
    corners = [ ['א' , 'צ',], ['ב', 'פ'] , ['ד', 'ט'], ['ז', "צ'"] ,['ח', "ג'"], ["ס", "ש"],["ת", "כ"] ]

    list_edges = []
    list_corners = []
    for i in range(len(edges)):
        for j in range(i):
            list_edges.append("{}{}".format(edges[i], edges[j]))

    for i in range(len(corners)):
        for j in range(i):
            list_corners.append("{}{}".format(corners[i][0], corners[j][0]))
            list_corners.append("{}{}".format(corners[i][0], corners[j][1]))
            list_corners.append("{}{}".format(corners[i][1], corners[j][0]))
            list_corners.append("{}{}".format(corners[i][1], corners[j][1]))

    all_list = []
    for x in list_edges:
        all_list.append(x)
    for x in list_corners:
        all_list.append(x)

    print(len(list(set(all_list))))
    print(sorted(all_list))
    # print (len(list_edges))
    # print (len(list_corners))
    #sum of lists before removing dups is 139
    #after removing dumps 116
    # so i need to find 116 unique letter pairs
class Solve():
    def __init__(self, solve_str):
        self.solve = solve_str
        self.edges_bool = 1 if "Flip" in self.solve else 0
        self.twist_bool = 1 if "Twist Clockwise" in self.solve else 0
        self.twist_counter_bool = 1 if "Twist Counterclockwise" in self.solve else 0
        self.edges_flip = self.solve.split(",")[1].split(":")[1].split(" ") if "Flip" in self.solve else 0
        self.twist = self.solve.split(",")[self.edges_bool + 2].split(":")[1].split(" ") if "Twist Clockwise" in self.solve else 0
        self.twist_counter = self.solve.split(",")[self.edges_bool + self.twist_bool+ 2].split(":")[1 ].split(" ") if "Twist Counterclockwise" in self.solve else 0
        if self.twist_counter != 0:
            counter = 0
            for x in self.twist_counter:
                if x != "" and len(x) == 1:
                    counter += 1
            self.twist_counter = counter
        if self.edges_flip != 0:
            counter = 0
            for x in self.edges_flip:
                if x != "" and len(x) == 1:
                    counter += 1
            self.edges_flip = counter
        if self.twist != 0:
            counter = 0
            for x in self.twist:
                if x != "" and len(x) == 1:
                    counter += 1
            self.twist = counter



def solves():
    str_ex = "[Edges: וש מע הח אנ תA זפ , Corners: לצ בק תב , Twist Counterclockwise: ד ר ]"
    ex = Solve(str_ex)
    solves_final = []
    edges_flip = {0:0, 1:0 , 2:0, 3:0, 4:0, 5:0, 6:0, 7:0}
    corner_twist = {0:0, 1:0 , 2:0, 3:0, 4:0, 5:0, 6:0, 7:0}
    at_least_one = 0

    with open ("solves.txt", "r", encoding="utf-8") as f:
        solves = f.readlines()
    import tqdm
    for s in tqdm.tqdm(solves):
        cur_solve = Solve(s)
        solves_final.append(cur_solve)
        edges_flip[cur_solve.edges_flip] += 1
        corner_twist[cur_solve.twist + cur_solve.twist_counter] += 1
        if cur_solve.edges_flip + cur_solve.twist + cur_solve.twist_counter != 0:
            at_least_one +=1
    print(edges_flip)
    print(corner_twist)
    print(at_least_one)
    print(len(solves))

def main():
    solves()

if __name__ == '__main__':
    main()