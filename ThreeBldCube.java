
/**
 * Created by user on 05/09/2017.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ThreeBldCube implements BldCube {
        protected static final int CORNERS = 0;
        protected static final int EDGES = 1;
        protected static final int CENTERS = 2;
        protected static final String NO_SCRAMBLE = "None";
        protected int[] corners = new int[24];
        protected int[] edges = new int[24];
        protected int[] centers = new int[6];

        protected int A = 0;
        protected int B = 1;
        protected int C = 2;
        protected int D = 3;
        protected int E = 4;
        protected int F = 5;
        protected int G = 6;
        protected int H = 7;
        protected int I = 8;
        protected int J = 9;
        protected int K = 10;
        protected int L = 11;
        protected int M = 12;
        protected int N = 13;
        protected int O = 14;
        protected int P = 15;
        protected int Q = 16;
        protected int R = 17;
        protected int S = 18;
        protected int T = 19;
        protected int U = 20;
        protected int V = 21;
        protected int W = 22;
        protected int X = 23;
        protected int Z = -1;
        // protected int א = 0 ; protected int ב = 1; protected int ג = 2; protected int
        // ד = 3; protected int ה = 4; protected int ו = 5; protected int ז = 6;
        // protected int ח = 7; protected int ט = 8; protected int י = 9; protected int
        // כ = 10; protected int ל = 11; protected int מ = 12; protected int נ = 13;
        // protected int ס = 14; protected int ע = 15; protected int פ = 16; protected
        // int צ = 17; protected int ק = 18; protected int ר = 19; protected int ת = 20;
        // protected int ש = 21; protected int Y = 22; protected int ZZ = 23;
        protected int UP = 0;
        protected int LEFT = 1;
        protected int FRONT = 2;
        protected int RIGHT = 3;
        protected int BACK = 4;
        protected int DOWN = 5;
        // protected String [] cornerScheme
        // ={"א","ב","ג","ד","ה","ו","ז","ח","ט","י","כ","ל","מ","נ","ס","ע","פ","צ","ק","ר","צ"
        // + '\u05F3',"ת","ש","ג" + '\u05F3'};
        // protected String [] edgeScheme
        // ={"א","ב","ג","ד","ה","ו","ז","ח","ט","י","כ","ל","מ","נ","ס","ע","פ","צ","ק","ר","צ"
        // + '\u05F3',"ת","ש","ג" + '\u05F3'};

        protected String[] cornerLettering = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X" };
        protected String[] edgeLettering = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
                        "P", "Q", "R", "S", "T", "U", "V", "W", "X" };
        protected String[] cornerPositions = { "UBL", "URB", "UFR", "ULF", "DFL", "DRF", "DBR", "DLB" };
        protected String[] edgePositions = { "DF", "UB", "UR", "UF", "UL", "BR", "BL", "FR", "FL", "DR", "DB", "DL" };

        protected Integer[][] cornerCubies = { { Integer.valueOf(A), Integer.valueOf(E), Integer.valueOf(R) },
                        { Integer.valueOf(B), Integer.valueOf(Q), Integer.valueOf(N) },
                        { Integer.valueOf(C), Integer.valueOf(M), Integer.valueOf(J) },
                        { Integer.valueOf(D), Integer.valueOf(I), Integer.valueOf(F) },
                        { Integer.valueOf(L), Integer.valueOf(U), Integer.valueOf(G) },
                        { Integer.valueOf(P), Integer.valueOf(V), Integer.valueOf(K) },
                        { Integer.valueOf(T), Integer.valueOf(W), Integer.valueOf(O) },
                        { Integer.valueOf(H), Integer.valueOf(X), Integer.valueOf(S) } };
        protected boolean[] solvedCorners = { true, true, true, true, true, true, true, true };
        protected boolean[] scrambledStateSolvedCorners = { false, false, false, false, false, false, false, false };
        protected int cornerCycleNum = 0;
        protected ArrayList<Integer> cornerCycles = new ArrayList();
        protected ArrayList<Integer> cwCorners = new ArrayList();
        protected ArrayList<Integer> ccwCorners = new ArrayList();

        protected Integer[][] edgeCubies = { { Integer.valueOf(U), Integer.valueOf(K) },
                        { Integer.valueOf(A), Integer.valueOf(Q) }, { Integer.valueOf(B), Integer.valueOf(M) },
                        { Integer.valueOf(C), Integer.valueOf(I) }, { Integer.valueOf(D), Integer.valueOf(E) },
                        { Integer.valueOf(R), Integer.valueOf(H) }, { Integer.valueOf(T), Integer.valueOf(N) },
                        { Integer.valueOf(L), Integer.valueOf(F) }, { Integer.valueOf(J), Integer.valueOf(P) },
                        { Integer.valueOf(V), Integer.valueOf(O) }, { Integer.valueOf(W), Integer.valueOf(S) },
                        { Integer.valueOf(X), Integer.valueOf(G) } };
        protected boolean[] solvedEdges = { true, true, true, true, true, true, true, true, true, true, true, true };
        protected boolean[] scrambledStateSolvedEdges = { false, false, false, false, false, false, false, false, false,
                        false, false, false };
        protected int edgeCycleNum = 0;
        protected ArrayList<Integer> edgeCycles = new ArrayList();
        protected ArrayList<Integer> flippedEdges = new ArrayList();

        protected int[] centerCubies = { UP, LEFT, FRONT, RIGHT, BACK, DOWN };
        protected String centerRotations = "";

        protected HashMap<String, HashMap<Integer, Integer[]>> permutations = new HashMap();

        protected String scramble = "None";
        protected String solvingOrPremoves = "";
        String[] faceNamesScramble3x3 = { "U", "U'", "U2", "F", "F'", "F2", "R", "R'", "R2", "L", "L'", "L2", "B", "B'",
                        "B2", "D", "D'", "D2" };

        public String scrambleCube3x3(String[] faceNames) {

                int faceNamesLength = faceNames.length;

                String scrambleString = "";
                int currentRandom = (int) (Math.random() * faceNamesLength);
                int previousRandom = 0;

                for (int i = 0; i < 24; i++) {

                        // while(Math.abs(previousRandom - currentRandom) <= 2)
                        while (faceNames[currentRandom].charAt(0) == faceNames[previousRandom].charAt(0))
                                currentRandom = (int) (Math.random() * faceNamesLength);
                        scrambleString = scrambleString + faceNames[currentRandom];

                        scrambleString = scrambleString + " ";
                        previousRandom = currentRandom;

                }

                return scrambleString;

        }

        public ThreeBldCube(String scramble) {
                String scramble1 = scrambleCube3x3(faceNamesScramble3x3);
                initPermutations();

                parseScramble(scramble1);
        }

        public void ScrambleCurrent3x3Cube(int current_line, String file_name_scramble) {
                BufferedReader reader;
                int count = 0;
                String line="";
		try {
			reader = new BufferedReader(new FileReader(
                                file_name_scramble));
			line = reader.readLine();
			while (line != null && count != current_line) {
                                line = reader.readLine();
                                count ++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
               
                // String scramble1 = scrambleCube3x3(faceNamesScramble3x3);
                this.scramble = line;
                System.out.println("here");
                System.out.println(this.scramble);
                // this.scramble = "U' R U R' D R U' R' D' U";
                // this.scramble = "L U L' D L U' L' D'";
                // this.scramble = "F' D' F U' F' D F F' F U";
                // this.scramble = "L' U L D' L' U' L D D L' U L D' L' U' L";
                // this.scramble = "B' B' U R' L U U R L' B' B' U' B B";
                // this.scramble = "B' U B U' D L' U' L D' U";
                // this.scramble = "B' F B' F L F' B D' D' B' F L B' F B' F";
                // this.scramble = "U' R' L R' L D' R' D R' L R' L U' R U U";
                // this.scramble = "B' F B' F U' F' B L' L' B' F U' B' F B' F";
                // this.scramble = "F' D F U' F' D' F U' F' D F U U F' D' F";
                // this.scramble = "F U F' D' F U' F' D";
                // this.scramble = "D F U F' D F U' F' D D";
                // this.scramble = "R U R' F' R U R' U' R' F R R U' R' U'";
                // this.scramble = "F' D F U U F' D' F U F' F F' D F U F' D' F U U'";
                initPermutations();
                parseScramble(this.scramble);
        }

        protected ThreeBldCube() {
        }

        protected void initPermutations() {
                String[] faceNames = { "U", "U'", "U2", "F", "F'", "F2", "R", "R'", "R2", "L", "L'", "L2", "B", "B'",
                                "B2", "D", "D'", "D2", "Uw", "Uw'", "Uw2", "Fw", "Fw'", "Fw2", "Rw", "Rw'", "Rw2", "Lw",
                                "Lw'", "Lw2", "Bw", "Bw'", "Bw2", "Dw", "Dw'", "Dw2", "M", "M'", "M2", "S", "S'", "S2",
                                "E", "E'", "E2", "x", "x'", "x2", "y", "y'", "y2", "z", "z'", "z2" };

                Integer[][] cornerFacePerms = { { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D),
                                Integer.valueOf(A), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                                Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(Z),
                                Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z),
                                Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                Integer.valueOf(Z) },
                                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(N),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                                                Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                                                Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(V),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(G),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B),
                                                Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(N),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(J),
                                                Integer.valueOf(K), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(P),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N),
                                                Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(B),
                                                Integer.valueOf(C), Integer.valueOf(Z) },
                                { Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L),
                                                Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H),
                                                Integer.valueOf(E), Integer.valueOf(U), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(A),
                                                Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(R) },
                                { Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R),
                                                Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(G), Integer.valueOf(A), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(L) },
                                { Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X),
                                                Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E),
                                                Integer.valueOf(F), Integer.valueOf(S), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(I),
                                                Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(D) },
                                { Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(Z),
                                                Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(N), Integer.valueOf(O) },
                                { Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z),
                                                Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(H), Integer.valueOf(E) },
                                { Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z),
                                                Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q),
                                                Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(A), Integer.valueOf(B) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                                                Integer.valueOf(H), Integer.valueOf(V), Integer.valueOf(W),
                                                Integer.valueOf(X), Integer.valueOf(U) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                                                Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(V), Integer.valueOf(W) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(H),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(W), Integer.valueOf(X),
                                                Integer.valueOf(U), Integer.valueOf(V) },
                                { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A),
                                                Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(N),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                                                Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                                                Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(V),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(G),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B),
                                                Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(N),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(J),
                                                Integer.valueOf(K), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(P),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N),
                                                Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(B),
                                                Integer.valueOf(C), Integer.valueOf(Z) },
                                { Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L),
                                                Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H),
                                                Integer.valueOf(E), Integer.valueOf(U), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(A),
                                                Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(R) },
                                { Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R),
                                                Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(G), Integer.valueOf(A), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(L) },
                                { Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X),
                                                Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E),
                                                Integer.valueOf(F), Integer.valueOf(S), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(I),
                                                Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(D) },
                                { Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(Z),
                                                Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(N), Integer.valueOf(O) },
                                { Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z),
                                                Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(H), Integer.valueOf(E) },
                                { Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z),
                                                Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q),
                                                Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(A), Integer.valueOf(B) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                                                Integer.valueOf(H), Integer.valueOf(V), Integer.valueOf(W),
                                                Integer.valueOf(X), Integer.valueOf(U) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                                                Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(V), Integer.valueOf(W) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(H),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(W), Integer.valueOf(X),
                                                Integer.valueOf(U), Integer.valueOf(V) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(G), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(N),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(V), Integer.valueOf(I), Integer.valueOf(J),
                                                Integer.valueOf(K), Integer.valueOf(L) },
                                { Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H),
                                                Integer.valueOf(E), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(P),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A),
                                                Integer.valueOf(B), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(R) },
                                { Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(X),
                                                Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E),
                                                Integer.valueOf(F), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N),
                                                Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(C), Integer.valueOf(D) },
                                { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A),
                                                Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S),
                                                Integer.valueOf(T), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(V), Integer.valueOf(W) },
                                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(M), Integer.valueOf(N),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Q),
                                                Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(H), Integer.valueOf(V), Integer.valueOf(W),
                                                Integer.valueOf(X), Integer.valueOf(U) },
                                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(E),
                                                Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(W), Integer.valueOf(X),
                                                Integer.valueOf(U), Integer.valueOf(V) },
                                { Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D),
                                                Integer.valueOf(A), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(V),
                                                Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(S), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(H), Integer.valueOf(E) },
                                { Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(W), Integer.valueOf(L), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(D),
                                                Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C),
                                                Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(N), Integer.valueOf(O) },
                                { Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(N), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(G),
                                                Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q),
                                                Integer.valueOf(R), Integer.valueOf(C), Integer.valueOf(D),
                                                Integer.valueOf(A), Integer.valueOf(B) } };

                Integer[][] edgeFacePerms = { { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D),
                                Integer.valueOf(A), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                                Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z),
                                Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(Z),
                                Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z),
                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                Integer.valueOf(Z) },
                                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C),
                                                Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(N),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(J),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(T),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(B),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L),
                                                Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H),
                                                Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(R) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R),
                                                Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(L) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X),
                                                Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E),
                                                Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(D) },
                                { Integer.valueOf(H), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(N), Integer.valueOf(Z) },
                                { Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(H), Integer.valueOf(Z) },
                                { Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(H), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q),
                                                Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(A), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                                                Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(W),
                                                Integer.valueOf(X), Integer.valueOf(U) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                                                Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(V), Integer.valueOf(W) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                                                Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(X),
                                                Integer.valueOf(U), Integer.valueOf(V) },
                                { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A),
                                                Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                                                Integer.valueOf(T), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(L),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                                                Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z),
                                                Integer.valueOf(L), Integer.valueOf(M), Integer.valueOf(N),
                                                Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Q),
                                                Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(T),
                                                Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(Z),
                                                Integer.valueOf(H), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                                                Integer.valueOf(P), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(E),
                                                Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(H),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z),
                                                Integer.valueOf(L), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(V),
                                                Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(Z), Integer.valueOf(E) },
                                { Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(C),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(Z), Integer.valueOf(O) },
                                { Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(G),
                                                Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(B) },
                                { Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(N),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(U),
                                                Integer.valueOf(V), Integer.valueOf(I), Integer.valueOf(J),
                                                Integer.valueOf(K), Integer.valueOf(Z) },
                                { Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(P),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(A),
                                                Integer.valueOf(B), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(Z) },
                                { Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N),
                                                Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(C), Integer.valueOf(Z) },
                                { Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H),
                                                Integer.valueOf(E), Integer.valueOf(U), Integer.valueOf(Z),
                                                Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A),
                                                Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z),
                                                Integer.valueOf(Q), Integer.valueOf(R) },
                                { Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(G), Integer.valueOf(A), Integer.valueOf(Z),
                                                Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(Z),
                                                Integer.valueOf(K), Integer.valueOf(L) },
                                { Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(X),
                                                Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E),
                                                Integer.valueOf(F), Integer.valueOf(S), Integer.valueOf(Z),
                                                Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(I),
                                                Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z),
                                                Integer.valueOf(C), Integer.valueOf(D) },
                                { Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(G),
                                                Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(V),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D),
                                                Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(Z),
                                                Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(M),
                                                Integer.valueOf(N), Integer.valueOf(O) },
                                { Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(M),
                                                Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(D),
                                                Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                                                Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z),
                                                Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(G),
                                                Integer.valueOf(H), Integer.valueOf(E) },
                                { Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(V),
                                                Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(M),
                                                Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                                                Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z),
                                                Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q),
                                                Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(D),
                                                Integer.valueOf(A), Integer.valueOf(B) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(Z), Integer.valueOf(N),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                                                Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(H), Integer.valueOf(V), Integer.valueOf(W),
                                                Integer.valueOf(X), Integer.valueOf(U) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(S),
                                                Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(F),
                                                Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                                                Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(V), Integer.valueOf(W) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(R),
                                                Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                                                Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H),
                                                Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(W), Integer.valueOf(X),
                                                Integer.valueOf(U), Integer.valueOf(V) },
                                { Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(A),
                                                Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z),
                                                Integer.valueOf(Q), Integer.valueOf(Z) },
                                { Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z),
                                                Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(U),
                                                Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(Z),
                                                Integer.valueOf(K), Integer.valueOf(Z) },
                                { Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z),
                                                Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(I),
                                                Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z),
                                                Integer.valueOf(C), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(M),
                                                Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                                                Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                                                Integer.valueOf(Z), Integer.valueOf(E) },
                                { Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(G),
                                                Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(V),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                                                Integer.valueOf(Z), Integer.valueOf(O) },
                                { Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(V),
                                                Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(M),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                                                Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D),
                                                Integer.valueOf(Z), Integer.valueOf(B) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(Z),
                                                Integer.valueOf(L), Integer.valueOf(Z), Integer.valueOf(N),
                                                Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                                                Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(T),
                                                Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(Z),
                                                Integer.valueOf(H), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(Z),
                                                Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(F),
                                                Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z),
                                                Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(L),
                                                Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(Z),
                                                Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(Z),
                                                Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(R),
                                                Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Z),
                                                Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(H),
                                                Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(Z),
                                                Integer.valueOf(L), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(G), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(N),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(V), Integer.valueOf(I), Integer.valueOf(J),
                                                Integer.valueOf(K), Integer.valueOf(L) },
                                { Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H),
                                                Integer.valueOf(E), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(P),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A),
                                                Integer.valueOf(B), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(R) },
                                { Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(X),
                                                Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E),
                                                Integer.valueOf(F), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N),
                                                Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(C), Integer.valueOf(D) },
                                { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A),
                                                Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S),
                                                Integer.valueOf(T), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(V), Integer.valueOf(W) },
                                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(M), Integer.valueOf(N),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Q),
                                                Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(H), Integer.valueOf(V), Integer.valueOf(W),
                                                Integer.valueOf(X), Integer.valueOf(U) },
                                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B),
                                                Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O),
                                                Integer.valueOf(P), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(E),
                                                Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(W), Integer.valueOf(X),
                                                Integer.valueOf(U), Integer.valueOf(V) },
                                { Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D),
                                                Integer.valueOf(A), Integer.valueOf(J), Integer.valueOf(K),
                                                Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(V),
                                                Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U),
                                                Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R),
                                                Integer.valueOf(S), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(H), Integer.valueOf(E) },
                                { Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G),
                                                Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(W), Integer.valueOf(L), Integer.valueOf(I),
                                                Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(D),
                                                Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C),
                                                Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T),
                                                Integer.valueOf(Q), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(N), Integer.valueOf(O) },
                                { Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V),
                                                Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M),
                                                Integer.valueOf(N), Integer.valueOf(K), Integer.valueOf(L),
                                                Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(G),
                                                Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F),
                                                Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q),
                                                Integer.valueOf(R), Integer.valueOf(C), Integer.valueOf(D),
                                                Integer.valueOf(A), Integer.valueOf(B) } };

                Integer[][] centerFacePerms = {
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                                                Integer.valueOf(Z), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(BACK), Integer.valueOf(LEFT),
                                                Integer.valueOf(FRONT), Integer.valueOf(RIGHT), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(FRONT), Integer.valueOf(RIGHT),
                                                Integer.valueOf(BACK), Integer.valueOf(LEFT), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(RIGHT), Integer.valueOf(BACK),
                                                Integer.valueOf(LEFT), Integer.valueOf(FRONT), Integer.valueOf(Z) },
                                { Integer.valueOf(RIGHT), Integer.valueOf(UP), Integer.valueOf(Z),
                                                Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(LEFT) },
                                { Integer.valueOf(LEFT), Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(UP),
                                                Integer.valueOf(Z), Integer.valueOf(RIGHT) },
                                { Integer.valueOf(DOWN), Integer.valueOf(RIGHT), Integer.valueOf(Z),
                                                Integer.valueOf(LEFT), Integer.valueOf(Z), Integer.valueOf(UP) },
                                { Integer.valueOf(BACK), Integer.valueOf(Z), Integer.valueOf(UP), Integer.valueOf(Z),
                                                Integer.valueOf(DOWN), Integer.valueOf(FRONT) },
                                { Integer.valueOf(FRONT), Integer.valueOf(Z), Integer.valueOf(DOWN), Integer.valueOf(Z),
                                                Integer.valueOf(UP), Integer.valueOf(BACK) },
                                { Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(BACK), Integer.valueOf(Z),
                                                Integer.valueOf(FRONT), Integer.valueOf(UP) },
                                { Integer.valueOf(FRONT), Integer.valueOf(Z), Integer.valueOf(DOWN), Integer.valueOf(Z),
                                                Integer.valueOf(UP), Integer.valueOf(BACK) },
                                { Integer.valueOf(BACK), Integer.valueOf(Z), Integer.valueOf(UP), Integer.valueOf(Z),
                                                Integer.valueOf(DOWN), Integer.valueOf(FRONT) },
                                { Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(BACK), Integer.valueOf(Z),
                                                Integer.valueOf(FRONT), Integer.valueOf(UP) },
                                { Integer.valueOf(LEFT), Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(UP),
                                                Integer.valueOf(Z), Integer.valueOf(RIGHT) },
                                { Integer.valueOf(RIGHT), Integer.valueOf(UP), Integer.valueOf(Z),
                                                Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(LEFT) },
                                { Integer.valueOf(DOWN), Integer.valueOf(RIGHT), Integer.valueOf(Z),
                                                Integer.valueOf(LEFT), Integer.valueOf(Z), Integer.valueOf(UP) },
                                { Integer.valueOf(Z), Integer.valueOf(FRONT), Integer.valueOf(RIGHT),
                                                Integer.valueOf(BACK), Integer.valueOf(LEFT), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(BACK), Integer.valueOf(LEFT),
                                                Integer.valueOf(FRONT), Integer.valueOf(RIGHT), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(RIGHT), Integer.valueOf(BACK),
                                                Integer.valueOf(LEFT), Integer.valueOf(FRONT), Integer.valueOf(Z) },
                                { Integer.valueOf(FRONT), Integer.valueOf(Z), Integer.valueOf(DOWN), Integer.valueOf(Z),
                                                Integer.valueOf(UP), Integer.valueOf(BACK) },
                                { Integer.valueOf(BACK), Integer.valueOf(Z), Integer.valueOf(UP), Integer.valueOf(Z),
                                                Integer.valueOf(DOWN), Integer.valueOf(FRONT) },
                                { Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(BACK), Integer.valueOf(Z),
                                                Integer.valueOf(FRONT), Integer.valueOf(UP) },
                                { Integer.valueOf(RIGHT), Integer.valueOf(UP), Integer.valueOf(Z),
                                                Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(LEFT) },
                                { Integer.valueOf(LEFT), Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(UP),
                                                Integer.valueOf(Z), Integer.valueOf(RIGHT) },
                                { Integer.valueOf(DOWN), Integer.valueOf(RIGHT), Integer.valueOf(Z),
                                                Integer.valueOf(LEFT), Integer.valueOf(Z), Integer.valueOf(UP) },
                                { Integer.valueOf(Z), Integer.valueOf(FRONT), Integer.valueOf(RIGHT),
                                                Integer.valueOf(BACK), Integer.valueOf(LEFT), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(BACK), Integer.valueOf(LEFT),
                                                Integer.valueOf(FRONT), Integer.valueOf(RIGHT), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(RIGHT), Integer.valueOf(BACK),
                                                Integer.valueOf(LEFT), Integer.valueOf(FRONT), Integer.valueOf(Z) },
                                { Integer.valueOf(BACK), Integer.valueOf(Z), Integer.valueOf(UP), Integer.valueOf(Z),
                                                Integer.valueOf(DOWN), Integer.valueOf(FRONT) },
                                { Integer.valueOf(FRONT), Integer.valueOf(Z), Integer.valueOf(DOWN), Integer.valueOf(Z),
                                                Integer.valueOf(UP), Integer.valueOf(BACK) },
                                { Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(BACK), Integer.valueOf(Z),
                                                Integer.valueOf(FRONT), Integer.valueOf(UP) },
                                { Integer.valueOf(Z), Integer.valueOf(BACK), Integer.valueOf(LEFT),
                                                Integer.valueOf(FRONT), Integer.valueOf(RIGHT), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(FRONT), Integer.valueOf(RIGHT),
                                                Integer.valueOf(BACK), Integer.valueOf(LEFT), Integer.valueOf(Z) },
                                { Integer.valueOf(Z), Integer.valueOf(RIGHT), Integer.valueOf(BACK),
                                                Integer.valueOf(LEFT), Integer.valueOf(FRONT), Integer.valueOf(Z) },
                                { Integer.valueOf(RIGHT), Integer.valueOf(UP), Integer.valueOf(Z),
                                                Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(LEFT) },
                                { Integer.valueOf(LEFT), Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(UP),
                                                Integer.valueOf(Z), Integer.valueOf(RIGHT) },
                                { Integer.valueOf(DOWN), Integer.valueOf(RIGHT), Integer.valueOf(Z),
                                                Integer.valueOf(LEFT), Integer.valueOf(Z), Integer.valueOf(UP) } };

                for (int i = 0; i < faceNames.length; i++) {
                        HashMap<Integer, Integer[]> tempMap = (HashMap) permutations.get(faceNames[i]);
                        if (tempMap == null)
                                tempMap = new HashMap();
                        tempMap.put(Integer.valueOf(0), cornerFacePerms[i]);
                        tempMap.put(Integer.valueOf(1), edgeFacePerms[i]);
                        tempMap.put(Integer.valueOf(2), centerFacePerms[i]);
                        permutations.put(faceNames[i], tempMap);
                }
        }

        protected void resetCube(boolean orientationOnly) {
                for (int i = 0; i < 24; i++) {
                        if (!orientationOnly) {
                                corners[i] = i;
                                edges[i] = i;
                                if (i < 6)
                                        centers[i] = i;
                        }
                        if (i < 8)
                                solvedCorners[i] = false;
                        if (i < 12)
                                solvedEdges[i] = false;
                }
        }

        protected void permute(String permutation) {
                int[] exchanges = { Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z };

                Integer[] perm = (Integer[]) ((HashMap) permutations.get(permutation)).get(Integer.valueOf(0));
                for (int i = 0; i < 24; i++)
                        if (perm[i].intValue() != Z)
                                exchanges[perm[i].intValue()] = corners[i];
                for (int i = 0; i < 24; i++)
                        if (exchanges[i] != Z) {
                                corners[i] = exchanges[i];
                        }
                exchanges = new int[] { Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z };
                perm = (Integer[]) ((HashMap) permutations.get(permutation)).get(Integer.valueOf(1));
                for (int i = 0; i < 24; i++)
                        if (perm[i].intValue() != Z)
                                exchanges[perm[i].intValue()] = edges[i];
                for (int i = 0; i < 24; i++)
                        if (exchanges[i] != Z) {
                                edges[i] = exchanges[i];
                        }
                exchanges = new int[] { Z, Z, Z, Z, Z, Z };
                perm = (Integer[]) ((HashMap) permutations.get(permutation)).get(Integer.valueOf(2));
                for (int i = 0; i < 6; i++)
                        if (perm[i].intValue() != Z)
                                exchanges[perm[i].intValue()] = centers[i];
                for (int i = 0; i < 6; i++)
                        if (exchanges[i] != Z)
                                centers[i] = exchanges[i];
        }

        protected void scrambleCube(String scrambleString) {

                resetCube(false);

                ArrayList<String> validPermutations = new ArrayList(Arrays.asList(new String[] { "U", "U'", "U2", "L",
                                "L'", "L2", "F", "F'", "F2", "R", "R'", "R2", "B", "B'", "B2", "D", "D'", "D2", "Uw",
                                "Uw'", "Uw2", "Lw", "Lw'", "Lw2", "Fw", "Fw'", "Fw2", "Rw", "Rw'", "Rw2", "Bw", "Bw'",
                                "Bw2", "Dw", "Dw'", "Dw2", "M", "M'", "M2", "S", "S'", "S2", "E", "E'", "E2", "x", "x'",
                                "x2", "y", "y'", "y2", "z", "z'", "z2" }));
                ArrayList<String> permutations = new ArrayList();
                String[] scramble = scrambleString.split("\\s+?");
                for (String scrambleSeq : scramble) {
                        if (validPermutations.contains(scrambleSeq))
                                permutations.add(scrambleSeq);
                }
                this.scramble = joinFromCollection(permutations, " ");

                if (solvingOrPremoves.length() > 0)
                        permutations.addAll(0, Arrays.asList(solvingOrPremoves.split("\\s")));
                String permutation;
                for (Iterator i$ = permutations.iterator(); i$.hasNext(); permute(permutation)) {
                        permutation = (String) i$.next();
                }

                for (int i = 0; i < 8; i++)
                        solvedCorners[i] = false;
                for (int i = 0; i < 12; i++)
                        solvedEdges[i] = false;
        }

        protected void solveCube() {
                reorientCube();
                solveCorners();
                solveEdges();
        }

        protected String getRotationsFromOrientation(int top, int front, int[] checkArray) {
                String[][] reorientation = { { "", "y'", "", "y", "y2", "" }, { "z y", "", "z", "", "z y2", "z y'" },
                                { "x y2", "x y'", "", "x y", "", "x" }, { "z' y'", "", "z'", "", "z' y2", "z' y" },
                                { "x'", "x' y'", "", "x' y", "", "x' y2" },
                                { "", "x2 y'", "z2", "x2 y", "z2 y2", "" } };

                int topPosition = -1;
                int frontPosition = -1;
                for (int i = 0; i < 6; i++) {
                        if ((checkArray[i] == top) && (topPosition == -1))
                                topPosition = i;
                        if ((checkArray[i] == front) && (frontPosition == -1))
                                frontPosition = i;
                }
                if ((topPosition == -1) || (frontPosition == -1) || (topPosition == frontPosition))
                        return "";
                return reorientation[topPosition][frontPosition];
        }

        protected void reorientCube() {
                centerRotations = "";
                String neededRotation = getRotationsFromOrientation(centerCubies[0], centerCubies[2], centers);

                if (neededRotation.length() > 0) {
                        centerRotations = neededRotation;
                        for (String rotation : neededRotation.split("\\s"))
                                permute(rotation);
                }
        }

        public void setSolvingOrientation(BldCube.OrientationColor top, BldCube.OrientationColor front) {
                String neededRotation = getRotationsFromOrientation(top.getNum(), front.getNum(), centerCubies);
                if (neededRotation.length() > 0) {
                        solvingOrPremoves = invertMoves(neededRotation);
                        parseScramble(getScramble());
                }
        }

        private String invertMoves(String moves) {
                String[] singleMoves = moves.split("\\s");
                ArrayList<String> invertedMoves = new ArrayList(singleMoves.length);
                for (int i = singleMoves.length; i > 0; i--) {
                        String move = singleMoves[(i - 1)];
                        if (move.endsWith("'")) {
                                move = move.replace("'", "");
                        } else if (!move.endsWith("2"))
                                move = move + "'";
                        invertedMoves.add(move);
                }
                return joinFromCollection(invertedMoves, " ");
        }

        protected void solveCorners() {
                if (!cornersSolved())
                        System.arraycopy(solvedCorners, 0, scrambledStateSolvedCorners, 0, solvedCorners.length);
                else
                        scrambledStateSolvedCorners = new boolean[] { true, true, true, true, true, true, true, true };
                resetCube(true);
                cornerCycles.clear();
                cwCorners.clear();
                ccwCorners.clear();
                cornerCycleNum = 0;

                while (!cornersSolved()) {
                        cycleCornerBuffer();
                }
        }

        private void cycleCornerBuffer() {
                boolean cornerCycled = false;

                if (solvedCorners[0] != false) {
                        cornerCycleNum += 1;
                        // int[] cornerPref = { 7, 6, 1, 5, 4, 2, 3 };
                        int[] cornerPref = { 7,6,1,3,2,5,4};
                        for (int i = 0; (i < 7) && (!cornerCycled); i++) {
                                int j = cornerCubies[0][0].intValue() == C ? cornerPref[i] : i;
                                if (solvedCorners[j] == false) {
                                        int[] tempCorner = { corners[cornerCubies[0][0].intValue()],
                                                        corners[cornerCubies[0][1].intValue()],
                                                        corners[cornerCubies[0][2].intValue()] };

                                        corners[cornerCubies[0][0].intValue()] = corners[cornerCubies[j][0].intValue()];
                                        corners[cornerCubies[0][1].intValue()] = corners[cornerCubies[j][1].intValue()];
                                        corners[cornerCubies[0][2].intValue()] = corners[cornerCubies[j][2].intValue()];

                                        corners[cornerCubies[j][0].intValue()] = tempCorner[0];
                                        corners[cornerCubies[j][1].intValue()] = tempCorner[1];
                                        corners[cornerCubies[j][2].intValue()] = tempCorner[2];

                                        cornerCycles.add(cornerCubies[j][0]);
                                        cornerCycled = true;
                                }
                        }
                } else {

                        for (int i = 0; (i < 8) && (!cornerCycled); i++) {
                                for (int j = 0; (j < 3) && (!cornerCycled); j++) {
                                        if ((corners[cornerCubies[0][0].intValue()] == cornerCubies[i][(j % 3)]
                                                        .intValue())
                                                        && (corners[cornerCubies[0][1]
                                                                        .intValue()] == cornerCubies[i][((j + 1) % 3)]
                                                                                        .intValue())
                                                        && (corners[cornerCubies[0][2]
                                                                        .intValue()] == cornerCubies[i][((j + 2) % 3)]
                                                                                        .intValue())) {
                                                corners[cornerCubies[0][0]
                                                                .intValue()] = corners[cornerCubies[i][(j % 3)]
                                                                                .intValue()];
                                                corners[cornerCubies[0][1]
                                                                .intValue()] = corners[cornerCubies[i][((j + 1) % 3)]
                                                                                .intValue()];
                                                corners[cornerCubies[0][2]
                                                                .intValue()] = corners[cornerCubies[i][((j + 2) % 3)]
                                                                                .intValue()];

                                                corners[cornerCubies[i][0].intValue()] = cornerCubies[i][0].intValue();
                                                corners[cornerCubies[i][1].intValue()] = cornerCubies[i][1].intValue();
                                                corners[cornerCubies[i][2].intValue()] = cornerCubies[i][2].intValue();

                                                cornerCycles.add(cornerCubies[i][(j % 3)]);
                                                cornerCycled = true;
                                        }
                                }
                        }
                }
        }

        private boolean cornersSolved() {
                boolean cornersSolved = true;

                for (int i = 0; i < 8; i++) {
                        if ((i == 0) || (solvedCorners[i] == false)) {
                                if ((corners[cornerCubies[i][0].intValue()] == cornerCubies[i][0].intValue())
                                                && (corners[cornerCubies[i][1].intValue()] == cornerCubies[i][1]
                                                                .intValue())
                                                && (corners[cornerCubies[i][2].intValue()] == cornerCubies[i][2]
                                                                .intValue())) {
                                        solvedCorners[i] = true;
                                } else if ((corners[cornerCubies[i][0].intValue()] == cornerCubies[i][1].intValue())
                                                && (corners[cornerCubies[i][1].intValue()] == cornerCubies[i][2]
                                                                .intValue())
                                                && (corners[cornerCubies[i][2].intValue()] == cornerCubies[i][0]
                                                                .intValue())) {
                                        solvedCorners[i] = true;
                                        if (i != 0) {
                                                cwCorners.add(cornerCubies[i][0]);
                                        }
                                } else if ((corners[cornerCubies[i][0].intValue()] == cornerCubies[i][2].intValue())
                                                && (corners[cornerCubies[i][1].intValue()] == cornerCubies[i][0]
                                                                .intValue())
                                                && (corners[cornerCubies[i][2].intValue()] == cornerCubies[i][1]
                                                                .intValue())) {
                                        solvedCorners[i] = true;
                                        if (i != 0)
                                                ccwCorners.add(cornerCubies[i][0]);
                                } else {
                                        solvedCorners[i] = false;
                                        cornersSolved = false;
                                }
                        }
                }
                return cornersSolved;
        }

        protected void solveEdges() {
                if (cornerCycles.size() % 2 == 1) {
                        int UF = -1;
                        int UR = -1;

                        for (int i = 0; (i < 12) && ((UF == -1) || (UR == -1)); i++) {
                                if (((edges[edgeCubies[i][0].intValue()] == C)
                                                && (edges[edgeCubies[i][1].intValue()] == I))
                                                || ((edges[edgeCubies[i][1].intValue()] == C)
                                                                && (edges[edgeCubies[i][0].intValue()] == I)))
                                        UF = i;
                                if (((edges[edgeCubies[i][0].intValue()] == B)
                                                && (edges[edgeCubies[i][1].intValue()] == M))
                                                || ((edges[edgeCubies[i][1].intValue()] == B)
                                                                && (edges[edgeCubies[i][0].intValue()] == M))) {
                                        UR = i;
                                }
                        }

                        int[] tempEdge = { edges[edgeCubies[UF][0].intValue()], edges[edgeCubies[UF][1].intValue()] };

                        int index = 0;
                        if (((edges[edgeCubies[UF][0].intValue()] == C) && (edges[edgeCubies[UR][0].intValue()] == M))
                                        || ((edges[edgeCubies[UF][0].intValue()] == I)
                                                        && (edges[edgeCubies[UR][0].intValue()] == B))) {
                                index = 1;
                        }

                        edges[edgeCubies[UF][0].intValue()] = edges[edgeCubies[UR][index].intValue()];
                        edges[edgeCubies[UF][1].intValue()] = edges[edgeCubies[UR][((index + 1) % 2)].intValue()];

                        edges[edgeCubies[UR][0].intValue()] = tempEdge[index];
                        edges[edgeCubies[UR][1].intValue()] = tempEdge[((index + 1) % 2)];
                }
                if (!edgesSolved()) {
                        System.arraycopy(solvedEdges, 0, scrambledStateSolvedEdges, 0, solvedEdges.length);
                } else
                        scrambledStateSolvedEdges = new boolean[] { true, true, true, true, true, true, true, true,
                                        true, true, true, true };
                resetCube(true);
                edgeCycles.clear();
                flippedEdges.clear();
                edgeCycleNum = 0;
                while (!edgesSolved())
                        cycleEdgeBuffer();
        }
        private void cycleEdgeBuffer() {
                boolean edgeCycled = false;

                if (solvedEdges[0] != false) {
                        edgeCycleNum += 1;

                        // int[] edgePref = { 1, 8, 7, 2, 4, 3, 5, 6, 9, 10, 11 };
                        int[] edgePref = {11,10,1,5,4,7,6,3,2,8,9};

                        for (int i = 0; (i < 11) && (!edgeCycled); i++) {
                                int j = edgeCubies[0][0].intValue() == C ? edgePref[i] : i;
                                if (solvedEdges[j] == false) {
                                        int[] tempEdge = { edges[edgeCubies[0][0].intValue()],
                                                        edges[edgeCubies[0][1].intValue()] };

                                        edges[edgeCubies[0][0].intValue()] = edges[edgeCubies[j][0].intValue()];
                                        edges[edgeCubies[0][1].intValue()] = edges[edgeCubies[j][1].intValue()];

                                        edges[edgeCubies[j][0].intValue()] = tempEdge[0];
                                        edges[edgeCubies[j][1].intValue()] = tempEdge[1];

                                        edgeCycles.add(edgeCubies[j][0]);
                                        edgeCycled = true;
                                }
                        }
                } else {
                        for (int i = 0; (i < 12) && (!edgeCycled); i++) {
                                for (int j = 0; (j < 2) && (!edgeCycled); j++) {
                                        if ((edges[edgeCubies[0][0].intValue()] == edgeCubies[i][(j % 2)].intValue())
                                                        && (edges[edgeCubies[0][1]
                                                                        .intValue()] == edgeCubies[i][((j + 1) % 2)]
                                                                                        .intValue())) {
                                                edges[edgeCubies[0][0].intValue()] = edges[edgeCubies[i][(j % 2)]
                                                                .intValue()];
                                                edges[edgeCubies[0][1].intValue()] = edges[edgeCubies[i][((j + 1) % 2)]
                                                                .intValue()];

                                                edges[edgeCubies[i][0].intValue()] = edgeCubies[i][0].intValue();
                                                edges[edgeCubies[i][1].intValue()] = edgeCubies[i][1].intValue();

                                                edgeCycles.add(edgeCubies[i][(j % 2)]);
                                                edgeCycled = true;
                                        }
                                }
                        }
                }
        }

        private boolean edgesSolved() {
                boolean edgesSolved = true;

                for (int i = 0; i < 12; i++) {
                        if ((i == 0) || (solvedEdges[i] == false)) {
                                if ((edges[edgeCubies[i][0].intValue()] == edgeCubies[i][0].intValue())
                                                && (edges[edgeCubies[i][1].intValue()] == edgeCubies[i][1]
                                                                .intValue())) {
                                        solvedEdges[i] = true;

                                } else if ((edges[edgeCubies[i][0].intValue()] == edgeCubies[i][1].intValue())
                                                && (edges[edgeCubies[i][1].intValue()] == edgeCubies[i][0]
                                                                .intValue())) {
                                        solvedEdges[i] = true;
                                        if (i != 0)
                                                flippedEdges.add(edgeCubies[i][0]);
                                } else {
                                        solvedEdges[i] = false;
                                        edgesSolved = false;
                                }
                        }
                }
                return edgesSolved;
        }

        public void parseScramble(String scrambleString) {
                scrambleCube(scrambleString);

                solveCube();
        }

        public String getEdgePairs() {
                String edgePairs = "'";
                if ((edgeCycles.size() != 0) || (flippedEdges.size() != 0)) {
                        for (int i = 0; i < edgeCycles.size(); i++) {
                                edgePairs = edgePairs + edgeLettering[((Integer) edgeCycles.get(i)).intValue()];
                                if (i % 2 == 1)
                                        edgePairs = edgePairs + " ";
                        }
                        edgePairs += "'";
                        if (flippedEdges.size() != 0) {
                                edgePairs = edgePairs + "\n'Flip': '";
                                Integer flippedEdge;
                                for (Iterator i$ = flippedEdges.iterator(); i$.hasNext(); edgePairs = edgePairs
                                                + edgeLettering[flippedEdge.intValue()] + " ")
                                        flippedEdge = (Integer) i$.next();
                                edgePairs += "'";
                        }
                }
                return edgePairs;
        }

        public String getCornerPairs() {
                String cornerPairs = "'";
                if ((cornerCycles.size() != 0) || (cwCorners.size() != 0) || (ccwCorners.size() != 0)) {
                        for (int i = 0; i < cornerCycles.size(); i++) {
                                cornerPairs = cornerPairs + cornerLettering[((Integer) cornerCycles.get(i)).intValue()];
                                if (i % 2 == 1)
                                        cornerPairs = cornerPairs + " ";
                        }
                        cornerPairs += "'";
                        if (cwCorners.size() != 0) {
                                cornerPairs = cornerPairs + "\n'Twist Clockwise':'";
                                int cwCorner;
                                for (Iterator i$ = cwCorners.iterator(); i$
                                                .hasNext(); cornerPairs = cornerPairs + cornerLettering[cwCorner] + " ")
                                        cwCorner = ((Integer) i$.next()).intValue();
                                cornerPairs += "'";
                        }
                        if (ccwCorners.size() != 0) {
                                cornerPairs = cornerPairs + "\n'Twist Counterclockwise':'";
                                int ccwCorner;
                                for (Iterator i$ = ccwCorners.iterator(); i$.hasNext(); cornerPairs = cornerPairs
                                                + cornerLettering[ccwCorner] + " ")
                                        ccwCorner = ((Integer) i$.next()).intValue();
                                cornerPairs += "'";
                        }
                }
                return cornerPairs;
        }

        public String getSolutionPairs(boolean withRotation, boolean neverMind) {
                return (withRotation ? getRotations() + "\n" : "") + "'Edges': " + getEdgePairs() + "\n'Corners': "
                                + getCornerPairs() ;
        }

        public String getStatstics() {
                return "Corners: " + getCornerLength() + "@" + getCornerBreakInNum() + " w/ " + getNumPreSolvedCorners()
                                + "-" + getNumPreTwistedCorners() + " > " + hasCornerParity() + "\nEdges: "
                                + getEdgeLength() + "@" + getEdgeBreakInNum() + " w/ " + getNumPreSolvedEdges() + "-"
                                + getNumPreFlippedEdges() + " > " + hasCornerParity();
        }

        public String getNoahtation() {
                return "C:" + getCornerNoahtation() + " / E:" + getEdgeNoahtation();
        }

        public String getRotations() {
                return centerRotations.length() > 0 ? centerRotations : "/";
        }

        public boolean hasCornerParity() {
                return cornerCycles.size() % 2 == 1;
        }

        public boolean isSingleCycle() {
                return (isEdgeSingleCycle()) && (isCornerSingleCycle());
        }

        public String getScramble() {
                return scramble;
        }

        public int getEdgeLength() {
                return edgeCycles.size();
        }

        public int getEdgeBreakInNum() {
                return edgeCycleNum;
        }

        public boolean isEdgeSingleCycle() {
                return edgeCycleNum == 0;
        }

        public int getNumPreSolvedEdges() {
                return getNumPreEdges(false);
        }

        public String getPreSolvedEdges() {
                return getPreEdges(false);
        }

        public int getNumPreFlippedEdges() {
                return getNumPreEdges(true);
        }

        public String getPreFlippedEdges() {
                return getPreEdges(true);
        }

        public int getNumPrePermutedEdges() {
                return getNumPreSolvedEdges() + getNumPreFlippedEdges();
        }

        public String getPrePermutedEdges() {
                return getPreSolvedEdges() + getPreFlippedEdges();
        }

        public int getNumPreEdges(boolean flipped) {
                int preSolved = 0;
                for (int i = 0; i < scrambledStateSolvedEdges.length; i++) {
                        if (scrambledStateSolvedEdges[i] != false) {
                                if (flipped) {
                                        if (flippedEdges.contains(edgeCubies[i][0]))
                                                preSolved++;
                                } else if (!flippedEdges.contains(edgeCubies[i][0]))
                                        preSolved++;
                        }
                }
                return preSolved;
        }

        public String getPreEdges(boolean flipped) {
                String solvedEdges = "";
                for (int i = 0; i < scrambledStateSolvedEdges.length; i++) {
                        if (scrambledStateSolvedEdges[i] != false) {
                                if (flipped) {
                                        if (flippedEdges.contains(edgeCubies[i][0])) {
                                                solvedEdges = solvedEdges + (solvedEdges.length() > 0 ? " " : "")
                                                                + edgePositions[i];
                                        }
                                } else if (!flippedEdges.contains(edgeCubies[i][0]))
                                        solvedEdges = solvedEdges + (solvedEdges.length() > 0 ? " " : "")
                                                        + edgePositions[i];
                        }
                }
                return solvedEdges;
        }

        public String getEdgeNoahtation() {
                String edgeLength = getEdgeLength() + "";
                int ignored;
                for (Iterator i$ = flippedEdges.iterator(); i$.hasNext(); edgeLength = edgeLength + "'")
                        ignored = ((Integer) i$.next()).intValue();
                return edgeLength;
        }

        public int getCornerLength() {
                return cornerCycles.size();
        }

        public int getCornerBreakInNum() {
                return cornerCycleNum;
        }

        public boolean isCornerSingleCycle() {
                return cornerCycleNum == 0;
        }

        public int getNumPreSolvedCorners() {
                return getNumPreCorners(false);
        }

        public String getPreSolvedCorners() {
                return getPreCorners(false);
        }

        public int getNumPreTwistedCorners() {
                return getNumPreCorners(true);
        }

        public String getPreTwistedCorners() {
                return getPreCorners(true);
        }

        public int getNumPrePermutedCorners() {
                return getNumPreSolvedCorners() + getNumPreTwistedCorners();
        }

        public String getPrePermutedCorners() {
                return getPreSolvedCorners() + getPreTwistedCorners();
        }

        public int getNumPreCorners(boolean flipped) {
                int preSolved = 0;
                for (int i = 0; i < scrambledStateSolvedCorners.length; i++) {
                        if (scrambledStateSolvedCorners[i] != false) {
                                if (flipped) {
                                        if ((cwCorners.contains(cornerCubies[i][0]))
                                                        || (ccwCorners.contains(cornerCubies[i][0]))) {
                                                preSolved++;
                                        }
                                } else if ((!cwCorners.contains(cornerCubies[i][0]))
                                                && (!ccwCorners.contains(cornerCubies[i][0])))
                                        preSolved++;
                        }
                }
                return preSolved;
        }

        public String getPreCorners(boolean flipped) {
                String solvedCorners = "";
                for (int i = 0; i < scrambledStateSolvedCorners.length; i++) {
                        if (scrambledStateSolvedCorners[i] != false) {
                                if (flipped) {
                                        if ((cwCorners.contains(cornerCubies[i][0]))
                                                        || (ccwCorners.contains(cornerCubies[i][0]))) {
                                                solvedCorners = solvedCorners + (solvedCorners.length() > 0 ? " " : "")
                                                                + cornerPositions[i];
                                        }
                                } else if ((!cwCorners.contains(cornerCubies[i][0]))
                                                && (!ccwCorners.contains(cornerCubies[i][0])))
                                        solvedCorners = solvedCorners + (solvedCorners.length() > 0 ? " " : "")
                                                        + cornerPositions[i];
                        }
                }
                return solvedCorners;
        }

        public String getCornerNoahtation() {
                String cornerLength = getCornerLength() + "";
                int ignored;
                for (Iterator i$ = cwCorners.iterator(); i$.hasNext(); cornerLength = cornerLength + "'")
                        ignored = ((Integer) i$.next()).intValue();
                int ignored1;
                for (Iterator i$ = ccwCorners.iterator(); i$.hasNext(); cornerLength = cornerLength + "'")
                        ignored1 = ((Integer) i$.next()).intValue();
                return cornerLength;
        }

        public void setCornerScheme(String scheme) {
                setCornerScheme(scheme.split(""));
        }

        public void setCornerScheme(String[] scheme) {
                if (scheme.length == 24)
                        cornerLettering = scheme;
        }

        public void setEdgeScheme(String scheme) {
                setEdgeScheme(scheme.split(""));
        }

        public void setEdgeScheme(String[] scheme) {
                if (scheme.length == 24)
                        edgeLettering = scheme;
        }

        public void setCornerBuffer(String bufferAsLetter) {
                if (arrayContains(cornerLettering, bufferAsLetter)) {
                        int speffz = arrayIndex(cornerLettering, bufferAsLetter);
                        int outer = deepArrayOuterIndex(cornerCubies, Integer.valueOf(speffz));
                        int inner = deepArrayInnerIndex(cornerCubies, Integer.valueOf(speffz));
                        for (int i = 0; i < outer; i++)
                                cycleArrayLeft(cornerCubies);
                        for (int i = 0; i < inner; i++)
                                cycleArrayLeft(cornerCubies[0]);
                        parseScramble(getScramble());
                }
        }

        public void setEdgeBuffer(String bufferAsLetter) {
                if (arrayContains(edgeLettering, bufferAsLetter)) {
                        int speffz = arrayIndex(edgeLettering, bufferAsLetter);
                        int outer = deepArrayOuterIndex(edgeCubies, Integer.valueOf(speffz));
                        int inner = deepArrayInnerIndex(edgeCubies, Integer.valueOf(speffz));
                        for (int i = 0; i < outer; i++)
                                cycleArrayLeft(edgeCubies);
                        for (int i = 0; i < inner; i++)
                                cycleArrayLeft(edgeCubies[0]);
                        parseScramble(getScramble());
                }
        }

        protected <T> void cycleArrayLeft(T[] toCycle) {
                T tempStore = toCycle[0];
                System.arraycopy(toCycle, 1, toCycle, 0, toCycle.length - 1);
                toCycle[(toCycle.length - 1)] = tempStore;
        }

        protected <T> void cycleArrayRight(T[] toCycle) {
                T tempStore = toCycle[(toCycle.length - 1)];
                System.arraycopy(toCycle, 0, toCycle, 1, toCycle.length - 1);
                toCycle[0] = tempStore;
        }

        protected <T> boolean arrayContains(T[] array, T searchObject) {
                for (T element : array)
                        if (element.equals(searchObject))
                                return true;
                return false;
        }

        protected <T> boolean deepArrayContains(T[][] array, T searchObject) {
                for (T[] subarray : array)
                        for (T element : subarray)
                                if (element.equals(searchObject))
                                        return true;
                return false;
        }

        protected <T> int arrayIndex(T[] array, T searchObject) {
                for (int i = 0; i < array.length; i++)
                        if (array[i].equals(searchObject))
                                return i;
                return -1;
        }

        protected <T> int deepArrayOuterIndex(T[][] array, T searchObject) {
                for (int i = 0; i < array.length; i++)
                        for (T element : array[i])
                                if (element.equals(searchObject))
                                        return i;
                return -1;
        }

        protected <T> int deepArrayInnerIndex(T[][] array, T searchObject) {
                for (T[] subarray : array)
                        for (int i = 0; i < subarray.length; i++)
                                if (subarray[i].equals(searchObject))
                                        return i;
                return -1;
        }

        protected <T> String joinFromCollection(Collection<T> collection, String separator) {
                String joined = "";
                T content;
                for (Iterator<T> i$ = collection.iterator(); i$.hasNext(); joined = joined
                                + (joined.length() > 0 ? separator : "") + content.toString())
                        content = i$.next();
                return joined.trim();
        }

        protected <T> String joinFromArray(T[] array, String separator) {
                String joined = "";
                for (T content : array)
                        joined = joined + (joined.length() > 0 ? separator : "") + content.toString();
                return joined.trim();
        }
}
