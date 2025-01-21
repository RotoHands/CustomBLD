
/**
 * Created by user on 05/09/2017.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class FiveBldCube extends FourBldCube implements BldCube {

    private static final int T_CENTERS = 5;
    private int[] tCenters = new int[24];
    protected String tcenter_buffer = "C";

    protected String[] tCenterLettering = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X" };
    private String[] tCenterPositions = { "Df", "Dr", "Db", "Dl", "Ub", "Ur", "Uf", "Ul", "Lu", "Lf", "Ld", "Lb", "Fu",
            "Fr", "Fd", "Fl", "Ru", "Rb", "Rd", "Rf", "Bu", "Bl", "Bd", "Br" };

    private Integer[][] tCenterCubies = {
            { Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(X) },
            { Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D) },
            { Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H) },
            { Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L) },
            { Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P) },
            { Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T) } };
    private boolean[] solvedTCenters = { true, true, true, true, true, true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true, true, true, true };
    private boolean[] scrambledStateSolvedTCenters = { true, true, true, true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true, true, true, true, true, true };
    private int tCenterCycleNum = 0;
    private ArrayList<Integer> tCenterCycles = new ArrayList();

    private boolean avoidTBreakIns = true;
    String[] faceNamesScramble5x5 = { "U", "U'", "U2", "F", "F'", "F2", "R", "R'", "R2", "L", "L'", "L2", "B", "B'",
            "B2", "D", "D'", "D2", "Uw", "Uw'", "Uw2", "Fw", "Fw'", "Fw2", "Rw", "Rw'", "Rw2", "Lw", "Lw'", "Lw2", "Bw",
            "Bw'", "Bw2", "Dw", "Dw'", "Dw2" };

    public String getTCenterBuffer() {
        return "'tcenter_buffer':" + "'" + tcenter_buffer + "'";
    }

    public FiveBldCube(String scramble) {
        String scramble1 = scrambleCube5x5(faceNamesScramble5x5);
        initPermutations();
        parseScramble(scramble1);
    }

    public void ScrambleCurrent5x5Cube() {
        String scramble1 = scrambleCube5x5(faceNamesScramble5x5);
        this.scramble = scramble1;
        initPermutations();
        parseScramble(scramble1);
    }

    public String scrambleCube5x5(String[] faceNames) {

        int faceNamesLength = faceNames.length;

        String scrambleString = "";
        int currentRandom = (int) (Math.random() * faceNamesLength);
        int previousRandom = 0;

        for (int i = 0; i < 60; i++) {

            // while(Math.abs(previousRandom - currentRandom) <= 2)
            while (faceNames[currentRandom].charAt(0) == faceNames[previousRandom].charAt(0))
                currentRandom = (int) (Math.random() * faceNamesLength);
            scrambleString = scrambleString + faceNames[currentRandom];

            scrambleString = scrambleString + " ";
            previousRandom = currentRandom;

        }

        return scrambleString;

    }

    protected void initPermutations() {
        super.initPermutations();
        String[] faceNames = { "3Uw", "3Uw'", "3Uw2", "3Fw", "3Fw'", "3Fw2", "3Rw", "3Rw'", "3Rw2", "3Lw", "3Lw'",
                "3Lw2", "3Bw", "3Bw'", "3Bw2", "3Dw", "3Dw'", "3Dw2" };

        Integer[][] cornerFacePerms = {
                { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(Z),
                        Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(J),
                        Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(V),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(Z),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(L),
                        Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(D),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(Z),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(G),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(N),
                        Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(W),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(C),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(Z),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(K),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(Z),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z) },
                { Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(U),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R) },
                { Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(H),
                        Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L) },
                { Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D) },
                { Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(R),
                        Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(O) },
                { Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(T),
                        Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(E) },
                { Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(B) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V) } };

        Integer[][] edgeFacePerms = { { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A),
                Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(E),
                Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(I), Integer.valueOf(J),
                Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(B),
                        Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(J),
                        Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(V),
                        Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(E) },
                { Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(L),
                        Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(D),
                        Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(O) },
                { Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(G),
                        Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(B) },
                { Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(N),
                        Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(W),
                        Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z) },
                { Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U),
                        Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(C),
                        Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z) },
                { Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(K),
                        Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(A),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z) },
                { Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(U),
                        Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R) },
                { Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(H),
                        Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L) },
                { Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(D) },
                { Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(X),
                        Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D),
                        Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(R),
                        Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O) },
                { Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(B),
                        Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(T),
                        Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(Z),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E) },
                { Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(O),
                        Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V) } };

        Integer[][] wingFacePerms = { { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A),
                Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(E),
                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(I), Integer.valueOf(Z),
                Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z),
                Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(M),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Q),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(E),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(M),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Q),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(E),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(B),
                        Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(J),
                        Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E) },
                { Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L),
                        Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O) },
                { Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B) },
                { Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(N),
                        Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U),
                        Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(A),
                        Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(H),
                        Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(D) },
                { Integer.valueOf(H), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D),
                        Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R),
                        Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z) },
                { Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T),
                        Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(Z),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z) },
                { Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V) } };

        Integer[][] xCenterFacePerms = {
                { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(Z),
                        Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(J),
                        Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(V),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(Z),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(L),
                        Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(D),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(Z),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(G),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(N),
                        Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(W),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(C),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(Z),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(K),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(Z),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z) },
                { Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(U),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R) },
                { Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(H),
                        Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L) },
                { Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D) },
                { Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(R),
                        Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(O) },
                { Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(T),
                        Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(E) },
                { Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(B) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V) } };

        Integer[][] centerFacePerms = {
                { Integer.valueOf(Z), Integer.valueOf(BACK), Integer.valueOf(LEFT), Integer.valueOf(FRONT),
                        Integer.valueOf(RIGHT), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(FRONT), Integer.valueOf(RIGHT), Integer.valueOf(BACK),
                        Integer.valueOf(LEFT), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(RIGHT), Integer.valueOf(BACK), Integer.valueOf(LEFT),
                        Integer.valueOf(FRONT), Integer.valueOf(Z) },
                { Integer.valueOf(RIGHT), Integer.valueOf(UP), Integer.valueOf(Z), Integer.valueOf(DOWN),
                        Integer.valueOf(Z), Integer.valueOf(LEFT) },
                { Integer.valueOf(LEFT), Integer.valueOf(DOWN), Integer.valueOf(Z), Integer.valueOf(UP),
                        Integer.valueOf(Z), Integer.valueOf(RIGHT) },
                { Integer.valueOf(DOWN), Integer.valueOf(RIGHT), Integer.valueOf(Z), Integer.valueOf(LEFT),
                        Integer.valueOf(Z), Integer.valueOf(UP) },
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
                { Integer.valueOf(RIGHT), Integer.valueOf(UP), Integer.valueOf(Z), Integer.valueOf(DOWN),
                        Integer.valueOf(Z), Integer.valueOf(LEFT) },
                { Integer.valueOf(DOWN), Integer.valueOf(RIGHT), Integer.valueOf(Z), Integer.valueOf(LEFT),
                        Integer.valueOf(Z), Integer.valueOf(UP) },
                { Integer.valueOf(Z), Integer.valueOf(FRONT), Integer.valueOf(RIGHT), Integer.valueOf(BACK),
                        Integer.valueOf(LEFT), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(BACK), Integer.valueOf(LEFT), Integer.valueOf(FRONT),
                        Integer.valueOf(RIGHT), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(RIGHT), Integer.valueOf(BACK), Integer.valueOf(LEFT),
                        Integer.valueOf(FRONT), Integer.valueOf(Z) } };

        for (int i = 0; i < faceNames.length; i++) {
            HashMap<Integer, Integer[]> tempMap = (HashMap) permutations.get(faceNames[i]);
            if (tempMap == null)
                tempMap = new HashMap();
            tempMap.put(Integer.valueOf(0), cornerFacePerms[i]);
            tempMap.put(Integer.valueOf(1), edgeFacePerms[i]);
            tempMap.put(Integer.valueOf(3), wingFacePerms[i]);
            tempMap.put(Integer.valueOf(4), xCenterFacePerms[i]);
            tempMap.put(Integer.valueOf(2), centerFacePerms[i]);
            permutations.put(faceNames[i], tempMap);
        }
        initPermutationsChain();
    }

    private void initPermutationsChain() {
        String[] faceNames = { "U", "U'", "U2", "F", "F'", "F2", "R", "R'", "R2", "L", "L'", "L2", "B", "B'", "B2", "D",
                "D'", "D2", "Uw", "Uw'", "Uw2", "Fw", "Fw'", "Fw2", "Rw", "Rw'", "Rw2", "Lw", "Lw'", "Lw2", "Bw", "Bw'",
                "Bw2", "Dw", "Dw'", "Dw2", "3Uw", "3Uw'", "3Uw2", "3Fw", "3Fw'", "3Fw2", "3Rw", "3Rw'", "3Rw2", "3Lw",
                "3Lw'", "3Lw2", "3Bw", "3Bw'", "3Bw2", "3Dw", "3Dw'", "3Dw2", "u", "u'", "u2", "f", "f'", "f2", "r",
                "r'", "r2", "l", "l'", "l2", "b", "b'", "b2", "d", "d'", "d2", "M", "M'", "M2", "S", "S'", "S2", "E",
                "E'", "E2", "x", "x'", "x2", "y", "y'", "y2", "z", "z'", "z2" };

        Integer[][] tCenterFacePerms = {
                { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(J),
                        Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L),
                        Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(N),
                        Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(H),
                        Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R),
                        Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T),
                        Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V) },
                { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Q),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(M),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(J),
                        Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L),
                        Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(N),
                        Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(Z),
                        Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(Z),
                        Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(H),
                        Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(L), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D) },
                { Integer.valueOf(H), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R),
                        Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(Z) },
                { Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T),
                        Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z) },
                { Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(H), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V) },
                { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(B),
                        Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(J),
                        Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(V),
                        Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(E) },
                { Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(L),
                        Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(D),
                        Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(O) },
                { Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(G),
                        Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(B) },
                { Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(N),
                        Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(W),
                        Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z) },
                { Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U),
                        Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(C),
                        Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z) },
                { Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(K),
                        Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(A),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z) },
                { Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(U),
                        Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R) },
                { Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(H),
                        Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L) },
                { Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(D) },
                { Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(X),
                        Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D),
                        Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(R),
                        Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O) },
                { Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(B),
                        Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(T),
                        Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(Z),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E) },
                { Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(O),
                        Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(P), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(Z),
                        Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(T), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(Z),
                        Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(L), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D) },
                { Integer.valueOf(H), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(Z) },
                { Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z) },
                { Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(H), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U),
                        Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(Z) },
                { Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W),
                        Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z) },
                { Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(B),
                        Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                        Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(E) },
                { Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(X),
                        Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D),
                        Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(O) },
                { Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(O),
                        Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                        Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(B) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(H),
                        Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(A),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(N),
                        Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L) },
                { Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(U),
                        Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(C),
                        Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R) },
                { Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(A),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D) },
                { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W) },
                { Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U) },
                { Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(W),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V) },
                { Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(B),
                        Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(J),
                        Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(V),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(T),
                        Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(E) },
                { Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(X),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(L),
                        Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(D),
                        Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(R),
                        Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O) },
                { Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(O),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(K),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(G),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(S),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(C),
                        Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(B) } };

        for (int i = 0; i < faceNames.length; i++) {
            HashMap<Integer, Integer[]> tempMap = (HashMap) permutations.get(faceNames[i]);
            if (tempMap == null)
                tempMap = new HashMap();
            tempMap.put(Integer.valueOf(5), tCenterFacePerms[i]);
            permutations.put(faceNames[i], tempMap);
        }
    }

    protected void resetCube(boolean orientationOnly) {
        super.resetCube(orientationOnly);
        for (int i = 0; i < 24; i++) {
            if (!orientationOnly)
                tCenters[i] = i;
            solvedTCenters[i] = false;
        }
    }

    protected void permute(String permutation) {
        super.permute(permutation);

        int[] exchanges = { Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z };
        Integer[] perm = (Integer[]) ((HashMap) permutations.get(permutation)).get(Integer.valueOf(5));
        for (int i = 0; i < 24; i++)
            if (perm[i].intValue() != Z)
                exchanges[perm[i].intValue()] = tCenters[i];
        for (int i = 0; i < 24; i++)
            if (exchanges[i] != Z) {
                tCenters[i] = exchanges[i];
            }
    }

    protected void scrambleCube(String scrambleString) {
        resetCube(false);

        ArrayList<String> validPermutations = new ArrayList(Arrays.asList(new String[] { "U", "U'", "U2", "L", "L'",
                "L2", "F", "F'", "F2", "R", "R'", "R2", "B", "B'", "B2", "D", "D'", "D2", "Uw", "Uw'", "Uw2", "Lw",
                "Lw'", "Lw2", "Fw", "Fw'", "Fw2", "Rw", "Rw'", "Rw2", "Bw", "Bw'", "Bw2", "Dw", "Dw'", "Dw2", "3Uw",
                "3Uw'", "3Uw2", "3Lw", "3Lw'", "3Lw2", "3Fw", "3Fw'", "3Fw2", "3Rw", "3Rw'", "3Rw2", "3Bw", "3Bw'",
                "3Bw2", "3Dw", "3Dw'", "3Dw2", "M", "M'", "M2", "S", "S'", "S2", "E", "E'", "E2", "x", "x'", "x2", "y",
                "y'", "y2", "z", "z'", "z2", "u", "u'", "u2", "f", "f'", "f2", "r", "r'", "r2", "l", "l'", "l2", "b",
                "b'", "b2", "d", "d'", "d2" }));
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
        for (int i = 0; i < 24; i++)
            solvedWings[i] = false;
        for (int i = 0; i < 24; i++)
            solvedXCenters[i] = false;
        for (int i = 0; i < 24; i++) {
            solvedTCenters[i] = false;
        }
    }

    protected void solveCube() {
        reorientCube();
        solveCorners();
        solveEdges();
        solveWings();
        solveXCenters();
        solveTCenters();
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

    public void avoidTBreakIns(boolean avoid) {
        avoidTBreakIns = avoid;
        parseScramble(getScramble());
    }

    protected void solveTCenters() {
        if (!tCentersSolved()) {
            System.arraycopy(solvedTCenters, 0, scrambledStateSolvedTCenters, 0, solvedTCenters.length);
        } else
            scrambledStateSolvedTCenters = new boolean[] { true, true, true, true, true, true, true, true, true, true,
                    true, true, true, true, true, true, true, true, true, true, true, true, true, true };
        resetCube(true);
        tCenterCycles.clear();
        tCenterCycleNum = 0;
        while (!tCentersSolved())
            cycleTCenterBuffer();
    }

    private void cycleTCenterBuffer() {
        boolean tCenterCycled = false;

        if (solvedTCenters[0] != false) {
            tCenterCycleNum += 1;

            for (int i = 0; (i < 23) && (!tCenterCycled); i++) {
                if (solvedTCenters[i] == false) {
                    int centerIndex = i;
                    if ((avoidTBreakIns) && (tCenters[tCenterCubies[(i / 4)][(i % 4)].intValue()] > T)) {
                        for (int j = i; j < i + 4 - i % 4; j++)
                            if ((solvedTCenters[j] == false)
                                    && (tCenters[tCenterCubies[(j / 4)][(j % 4)].intValue()] < U)) {
                                centerIndex = j;
                                break;
                            }
                    }
                    int tempXCenter = tCenters[tCenterCubies[0][0].intValue()];

                    tCenters[tCenterCubies[0][0]
                            .intValue()] = tCenters[tCenterCubies[(centerIndex / 4)][(centerIndex % 4)].intValue()];

                    tCenters[tCenterCubies[(centerIndex / 4)][(centerIndex % 4)].intValue()] = tempXCenter;

                    tCenterCycles.add(tCenterCubies[(centerIndex / 4)][(centerIndex % 4)]);
                    tCenterCycled = true;
                }
            }
        } else {
            for (int i = 0; (i < 6) && (!tCenterCycled); i++) {
                for (int j = 0; (j < 4) && (!tCenterCycled); j++) {
                    if (tCenters[tCenterCubies[0][0].intValue()] == tCenterCubies[i][j].intValue()) {
                        int centerIndex = -1;
                        for (int k = i * 4 + j - j % 4; k < (i + 1) * 4 + j - j % 4; k++)
                            if (solvedTCenters[k] == false) {
                                centerIndex = k % 4;
                                break;
                            }
                        if (centerIndex < 0)
                            centerIndex = j;
                        if ((avoidTBreakIns) && (tCenters[tCenterCubies[i][centerIndex].intValue()] > T)) {
                            for (int l = i * 4 + centerIndex; l < (i + 1) * 4; l++)
                                if ((solvedTCenters[l] == false)
                                        && (tCenters[tCenterCubies[(l / 4)][(l % 4)].intValue()] < U)) {
                                    centerIndex = l % 4;
                                    break;
                                }
                        }
                        tCenters[tCenterCubies[0][0].intValue()] = tCenters[tCenterCubies[i][centerIndex].intValue()];

                        tCenters[tCenterCubies[i][centerIndex].intValue()] = tCenterCubies[i][centerIndex].intValue();

                        tCenterCycles.add(tCenterCubies[i][centerIndex]);
                        tCenterCycled = true;
                    }
                }
            }
        }
    }

    private boolean tCentersSolved() {
        boolean tCentersSolved = true;

        for (int i = 0; i < 24; i++) {
            if ((i == 0) || (solvedTCenters[i] == false)) {
                int j = i / 4;
                if ((tCenters[tCenterCubies[j][(i % 4)].intValue()] == tCenterCubies[j][(i % 4)].intValue())
                        || (tCenters[tCenterCubies[j][(i % 4)].intValue()] == tCenterCubies[j][((i + 1) % 4)]
                                .intValue())
                        || (tCenters[tCenterCubies[j][(i % 4)].intValue()] == tCenterCubies[j][((i + 2) % 4)]
                                .intValue())
                        || (tCenters[tCenterCubies[j][(i % 4)].intValue()] == tCenterCubies[j][((i + 3) % 4)]
                                .intValue())) {

                    solvedTCenters[i] = true;
                } else {
                    solvedTCenters[i] = false;
                    tCentersSolved = false;
                }
            }
        }
        return tCentersSolved;
    }

    public String getTCenterPairs() {
        String tCenterPairs = "'";
        if (tCenterCycles.size() != 0) {
            for (int i = 0; i < tCenterCycles.size(); i++) {
                tCenterPairs = tCenterPairs + tCenterLettering[((Integer) tCenterCycles.get(i)).intValue()];
                if (i % 2 == 1)
                    tCenterPairs = tCenterPairs + " ";
            }
            tCenterPairs += "'";
        }
        if (tCenterPairs == "'")
            tCenterPairs = "";
        return tCenterPairs;
    }

    public String getSolutionPairs(boolean withRotation, boolean isWingSchemeRegular) {
        return (withRotation ? getRotations() + "\n" : "") + "'TCenters': " + getTCenterPairs() + "\n'XCenters': "
                + getXCenterPairs() + "\n'Wings': " + getWingPairs(isWingSchemeRegular) + "\n'Edges': " + getEdgePairs()
                + "\n'Corners': " + getCornerPairs();
    }

    public String getStatstics() {
        return "Corners: " + getCornerLength() + "@" + getCornerBreakInNum() + " w/ " + getNumPreSolvedCorners() + "-"
                + getNumPreTwistedCorners() + " > " + hasCornerParity() + "\nEdges: " + getEdgeLength() + "@"
                + getEdgeBreakInNum() + " w/ " + getNumPreSolvedEdges() + "-" + getNumPreFlippedEdges() + " > "
                + hasCornerParity() + "\nWings: " + getWingLength() + "@" + getWingBreakInNum() + " w/ "
                + getNumPreSolvedWings() + " > " + hasWingParity() + "\nXCenters: " + getXCenterLength() + "@"
                + getXCenterBreakInNum() + " w/ " + getNumPreSolvedXCenters() + " > " + hasXCenterParity()
                + "\nTCenters: " + getTCenterLength() + "@" + getTCenterBreakInNum() + " w/ "
                + getNumPreSolvedTCenters() + " > " + hasTCenterParity();
    }

    public String getNoahtation() {
        return "C:" + getCornerNoahtation() + " / E:" + getEdgeNoahtation() + " / W:" + getWingNoahtation() + " / X:"
                + getXCenterNoahtation() + " / T:" + getTCenterNoahtation();
    }

    public boolean hasTCenterParity() {
        return tCenterCycles.size() % 2 == 1;
    }

    public int getTCenterLength() {
        return tCenterCycles.size();
    }

    public int getTCenterBreakInNum() {
        return tCenterCycleNum;
    }

    public boolean isTCenterSingleCycle() {
        return tCenterCycleNum == 0;
    }

    public int getNumPreSolvedTCenters() {
        int preSolved = 0;
        for (boolean scrambledStateSolvedTCenter : scrambledStateSolvedTCenters)
            if (scrambledStateSolvedTCenter)
                preSolved++;
        return preSolved;
    }

    public String getPreSolvedTCenters() {
        String solvedTCenters = "";
        for (int i = 0; i < scrambledStateSolvedTCenters.length; i++)
            if (scrambledStateSolvedTCenters[i] != false)
                solvedTCenters = solvedTCenters + (solvedTCenters.length() > 0 ? " " : "") + tCenterPositions[i];
        return solvedTCenters;
    }

    public String getTCenterNoahtation() {
        return getTCenterLength() + "";
    }

    public void setTCenterScheme(String scheme) {
        setTCenterScheme(scheme.split(""));
    }

    public void setTCenterScheme(String[] scheme) {
        if (scheme.length == 24)
            tCenterLettering = scheme;
    }

    public void setTCenterBuffer(String bufferAsLetter) {
        if (arrayContains(tCenterLettering, bufferAsLetter)) {
            int speffz = arrayIndex(tCenterLettering, bufferAsLetter);
            int outer = deepArrayOuterIndex(tCenterCubies, Integer.valueOf(speffz));
            int inner = deepArrayInnerIndex(tCenterCubies, Integer.valueOf(speffz));
            for (int i = 0; i < outer; i++)
                cycleArrayLeft(tCenterCubies);
            for (int i = 0; i < inner; i++)
                cycleArrayLeft(tCenterCubies[0]);
            parseScramble(getScramble());
        }
    }
}
