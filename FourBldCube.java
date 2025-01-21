
/**
 * Created by user on 05/09/2017.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class FourBldCube extends ThreeBldCube implements BldCube {

    protected static final int WINGS = 3;
    protected static final int X_CENTERS = 4;
    protected int[] wings = new int[24];
    protected int[] xCenters = new int[24];
    protected String wings_buffer = "C";
    protected String xcenter_buffer = "C";

    protected String[] wingLettering = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X" };
    // protected String[] wingLettering = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח",
    // "ט", "י", "כ", "ל", "מ", "נ", "ס", "ע", "פ", "צ", "ק", "ר", "ת", "ש","ZZ","Y"
    // };
    protected String[] xCenterLettering = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X" };
    protected String[] xCenterPositions = { "Ubl", "Urb", "Ufr", "Ulf", "Lub", "Lfu", "Ldf", "Lbd", "Ful", "Fru", "Fdr",
            "Fld", "Ruf", "Rbu", "Rdb", "Rfd", "Bur", "Blu", "Bdl", "Brd", "Dfl", "Drf", "Dbr", "Dlb" };
    // protected String[] wingPositions = { "DFr", "UBr", "URf", "UFl", "ULb",
    // "LUf", "LFd", "LDb", "LBu", "FUr", "FRd", "FDl", "FLu", "RUb", "RBd", "RDf",
    // "RFu", "BUl", "BLd", "BDr", "BRu", "DRb", "DBl", "DLf" };
    protected String[] wingPositions = { "UBl", "URb", "UFr", "ULf", "LUb", "LFu", "LDf", "LBd", "FUl", "FRu", "FDr",
            "FLd", "RUf", "RBu", "RDb", "RFd", "BUr", "BLu", "BDl", "BRd", "DRf", "DBr", "DLb", "DFl" };
    String[] correspondingWingScheme = { "פ", "מ", "ש", "ה", "ד", "ל", "ג" + '\u05F3', "צ", "ג", "ע", "צ" + '\u05F3',
            "ו", "ב", "ר", "ת", "י", "א", "ח", "ש", "נ", "כ", "ס", "ק", "ז" };

    // protected Integer[] wingCubies = { Integer.valueOf(U), Integer.valueOf(A),
    // Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D),
    // Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G),
    // Integer.valueOf(H), Integer.valueOf(I), Integer.valueOf(J),
    // Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(M),
    // Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P),
    // Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S),
    // Integer.valueOf(T), Integer.valueOf(V), Integer.valueOf(W),
    // Integer.valueOf(X) };

    protected Integer[] wingCubies = { Integer.valueOf(U), Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C),
            Integer.valueOf(D), Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H),
            Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(M),
            Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Q), Integer.valueOf(R),
            Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(X) };
    // protected Integer[] wingCubies = { Integer.valueOf(ג), Integer.valueOf(א),
    // Integer.valueOf(ב), Integer.valueOf(ד), Integer.valueOf(ה),
    // Integer.valueOf(ו), Integer.valueOf(ז), Integer.valueOf(ח),
    // Integer.valueOf(ט), Integer.valueOf(י), Integer.valueOf(כ),
    // Integer.valueOf(ל), Integer.valueOf(מ), Integer.valueOf(נ),
    // Integer.valueOf(ס), Integer.valueOf(ע), Integer.valueOf(פ),
    // Integer.valueOf(צ), Integer.valueOf(ק), Integer.valueOf(ר),
    // Integer.valueOf(ת), Integer.valueOf(ש), Integer.valueOf(ZZ),
    // Integer.valueOf(Y) };
    protected boolean[] solvedWings = { true, true, true, true, true, true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true, true, true, true };
    protected boolean[] scrambledStateSolvedWings = { false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
    protected int wingCycleNum = 0;
    protected ArrayList<Integer> wingCycles = new ArrayList<Integer>();

    protected Integer[][] xCenterCubies = {
            { Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D) },
            { Integer.valueOf(E), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(H) },
            { Integer.valueOf(I), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(L) },
            { Integer.valueOf(M), Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(P) },
            { Integer.valueOf(Q), Integer.valueOf(R), Integer.valueOf(S), Integer.valueOf(T) },
            { Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(X) } };
    protected boolean[] solvedXCenters = { true, true, true, true, true, true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true, true, true, true };
    protected boolean[] scrambledStateSolvedXCenters = { true, true, true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true, true, true, true, true, true, true };
    protected int xCenterCycleNum = 0;
    protected ArrayList<Integer> xCenterCycles = new ArrayList();

    private boolean optimizeCenters = true;
    private boolean avoidXBreakIns = true;

    String[] faceNamesScramble4x4 = { "U", "U'", "U2", "F", "F'", "F2", "R", "R'", "R2", "L", "L'", "L2", "B", "B'",
            "B2", "D", "D'", "D2", "Uw", "Uw'", "Uw2", "Fw", "Fw'", "Fw2", "Rw", "Rw'", "Rw2", "Lw", "Lw'", "Lw2", "Bw",
            "Bw'", "Bw2", "Dw", "Dw'", "Dw2" };

    public FourBldCube(String scramble) {
        initPermutations();
        String scramble1 = scrambleCube4x4(faceNamesScramble4x4);
        parseScramble(scramble1);
    }

    public void ScrambleCurrent4x4Cube() {
        String scramble1 = scrambleCube4x4(faceNamesScramble4x4);
        this.scramble = scramble1;
        initPermutations();
        parseScramble(scramble1);
    }

    public String scrambleCube4x4(String[] faceNames) {

        int faceNamesLength = faceNames.length;

        String scrambleString = "";
        int currentRandom = (int) (Math.random() * faceNamesLength);
        int previousRandom = 0;

        for (int i = 0; i < 40; i++) {

            // while(Math.abs(previousRandom - currentRandom) <= 2)
            while (faceNames[currentRandom].charAt(0) == faceNames[previousRandom].charAt(0))
                currentRandom = (int) (Math.random() * faceNamesLength);
            scrambleString = scrambleString + faceNames[currentRandom];

            scrambleString = scrambleString + " ";
            previousRandom = currentRandom;

        }

        return scrambleString;

    }

    protected FourBldCube() {
    }

    protected void initPermutations() {
        super.initPermutations();
        String[] faceNames = { "Uw", "Uw'", "Uw2", "Fw", "Fw'", "Fw2", "Rw", "Rw'", "Rw2", "Lw", "Lw'", "Lw2", "Bw",
                "Bw'", "Bw2", "Dw", "Dw'", "Dw2", "u", "u'", "u2", "f", "f'", "f2", "r", "r'", "r2", "l", "l'", "l2",
                "b", "b'", "b2", "d", "d'", "d2" };
        Integer[][] edgeFacePerms = { { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A),
                Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(Z),
                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z),
                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
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
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) } };
        Integer[][] centerFacePerms = {
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z) } };
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
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) } };

        for (int i = 0; i < faceNames.length; i++) {
            HashMap<Integer, Integer[]> tempMap = (HashMap) permutations.get(faceNames[i]);
            if (tempMap == null)
                tempMap = new HashMap();
            tempMap.put(Integer.valueOf(0), cornerFacePerms[i]);
            tempMap.put(Integer.valueOf(1), edgeFacePerms[i]);
            tempMap.put(Integer.valueOf(2), centerFacePerms[i]);
            permutations.put(faceNames[i], tempMap);
        }
        initPermutationsChain();
    }

    private void initPermutationsChain() {
        String[] faceNames = { "U", "U'", "U2", "F", "F'", "F2", "R", "R'", "R2", "L", "L'", "L2", "B", "B'", "B2", "D",
                "D'", "D2", "Uw", "Uw'", "Uw2", "Fw", "Fw'", "Fw2", "Rw", "Rw'", "Rw2", "Lw", "Lw'", "Lw2", "Bw", "Bw'",
                "Bw2", "Dw", "Dw'", "Dw2", "u", "u'", "u2", "f", "f'", "f2", "r", "r'", "r2", "l", "l'", "l2", "b",
                "b'", "b2", "d", "d'", "d2", "M", "M'", "M2", "S", "S'", "S2", "E", "E'", "E2", "x", "x'", "x2", "y",
                "y'", "y2", "z", "z'", "z2" };
        Integer[][] wingFacePerms = { { Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(A),
                Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(Z),
                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z),
                Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
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
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(E),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(M),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
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
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E) },
                { Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O) },
                { Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B) },
                { Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
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

        Integer[][] xCenterFacePerms = {
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
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(V) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(M),
                        Integer.valueOf(N), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Q),
                        Integer.valueOf(R), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(E),
                        Integer.valueOf(F), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(J), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(Z),
                        Integer.valueOf(C), Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F),
                        Integer.valueOf(G), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(G), Integer.valueOf(Z),
                        Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(P),
                        Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U), Integer.valueOf(V), Integer.valueOf(Z),
                        Integer.valueOf(P), Integer.valueOf(M), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(G),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(F), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(D), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(Z),
                        Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(K), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(C),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B), Integer.valueOf(Z),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(V), Integer.valueOf(W), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(T), Integer.valueOf(Q), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(K),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(J), Integer.valueOf(Z),
                        Integer.valueOf(B), Integer.valueOf(C), Integer.valueOf(Z) },
                { Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(U),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(D), Integer.valueOf(A), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R) },
                { Integer.valueOf(S), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(X), Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(I),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(L) },
                { Integer.valueOf(U), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(S),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(R), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(L), Integer.valueOf(I), Integer.valueOf(Z), Integer.valueOf(A),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(D) },
                { Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(X),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(W), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(A), Integer.valueOf(B), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(O) },
                { Integer.valueOf(N), Integer.valueOf(O), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(B),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(H), Integer.valueOf(E) },
                { Integer.valueOf(W), Integer.valueOf(X), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(O),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(N), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(H), Integer.valueOf(E), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(A), Integer.valueOf(B) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(O), Integer.valueOf(P), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(S), Integer.valueOf(T), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(G), Integer.valueOf(H), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(K), Integer.valueOf(L), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z) },
                { Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
                        Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z), Integer.valueOf(Z),
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
            tempMap.put(Integer.valueOf(3), wingFacePerms[i]);
            tempMap.put(Integer.valueOf(4), xCenterFacePerms[i]);
            permutations.put(faceNames[i], tempMap);
        }
    }

    protected void resetCube(boolean orientationOnly) {
        super.resetCube(orientationOnly);

        for (int i = 0; i < 24; i++) {
            if (!orientationOnly) {
                wings[i] = i;
                xCenters[i] = i;
            }
            solvedWings[i] = false;
            solvedXCenters[i] = false;
        }
    }

    protected void permute(String permutation) {
        super.permute(permutation);

        int[] exchanges = { Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z };
        Integer[] perm = (Integer[]) ((HashMap) permutations.get(permutation)).get(Integer.valueOf(3));
        for (int i = 0; i < 24; i++)
            if (perm[i].intValue() != Z)
                exchanges[perm[i].intValue()] = wings[i];
        for (int i = 0; i < 24; i++)
            if (exchanges[i] != Z) {
                wings[i] = exchanges[i];
            }
        exchanges = new int[] { Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z };
        perm = (Integer[]) ((HashMap) permutations.get(permutation)).get(Integer.valueOf(4));
        for (int i = 0; i < 24; i++)
            if (perm[i].intValue() != Z)
                exchanges[perm[i].intValue()] = xCenters[i];
        for (int i = 0; i < 24; i++)
            if (exchanges[i] != Z) {
                xCenters[i] = exchanges[i];
            }
    }

    protected void scrambleCube(String scrambleString) {
        resetCube(false);

        ArrayList<String> validPermutations = new ArrayList(Arrays.asList(new String[] { "U", "U'", "U2", "L", "L'",
                "L2", "F", "F'", "F2", "R", "R'", "R2", "B", "B'", "B2", "D", "D'", "D2", "Uw", "Uw'", "Uw2", "Lw",
                "Lw'", "Lw2", "Fw", "Fw'", "Fw2", "Rw", "Rw'", "Rw2", "Bw", "Bw'", "Bw2", "Dw", "Dw'", "Dw2", "x", "x'",
                "x2", "y", "y'", "y2", "z", "z'", "z2", "u", "u'", "u2", "f", "f'", "f2", "r", "r'", "r2", "l", "l'",
                "l2", "b", "b'", "b2", "d", "d'", "d2" }));
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
        for (int i = 0; i < 24; i++)
            solvedWings[i] = false;
        for (int i = 0; i < 24; i++) {
            solvedXCenters[i] = false;
        }
    }

    protected void solveCube() {
        if (optimizeCenters)
            reorientCube();
        solveCorners();
        solveWings();
        solveXCenters();
    }

    protected void reorientCube() {
        centerRotations = "";
        String[] possRotations = { "", "y'", "y", "y2", "z y", "z", "z y2", "z y'", "x y2", "x y'", "x y", "x", "z' y'",
                "z'", "z' y2", "z' y", "x'", "x' y'", "x' y", "x' y2", "x2 y'", "z2", "x2 y", "z2 y2" };
        int[] copyXCenters = new int[24];
        double max = Double.MIN_VALUE;
        int maxIndex = 0;
        for (int i = 0; i < possRotations.length; i++) {
            System.arraycopy(xCenters, 0, copyXCenters, 0, 24);
            if (i > 0)
                for (String rotation : possRotations[i].split("\\s")) {
                    int[] exchanges = { Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z, Z };
                    Integer[] perm = (Integer[]) ((HashMap) permutations.get(rotation)).get(Integer.valueOf(4));
                    for (int j = 0; j < 24; j++)
                        if (perm[j].intValue() != Z)
                            exchanges[perm[j].intValue()] = copyXCenters[j];
                    for (int j = 0; j < 24; j++)
                        if (exchanges[j] != Z) {
                            copyXCenters[j] = exchanges[j];
                        }
                }
            double solvedCenters = 0.0D;
            double solvedBadCenters = 0.0D;
            for (int j = 0; j < copyXCenters.length; j++)
                if (copyXCenters[j] / 4 == j / 4) {
                    solvedCenters += 1.0D;
                    if (j > 15)
                        solvedBadCenters += 1.0D;
                }
            solvedCenters /= 24.0D;
            solvedBadCenters /= 8.0D;
            double solvedCoeff = (2.0D * solvedCenters + solvedBadCenters) / 3.0D;
            if (solvedCoeff > max) {
                max = solvedCoeff;
                maxIndex = i;
            }
        }

        if (maxIndex > 0) {
            String rotation = possRotations[maxIndex];
            centerRotations = rotation;
            for (String singleRotation : rotation.split("\\s"))
                permute(singleRotation);
        }
    }

    public void optimizeCenters(boolean optimize) {
        optimizeCenters = optimize;
        parseScramble(getScramble());
    }

    public void avoidXBreakIns(boolean avoid) {
        avoidXBreakIns = avoid;
        parseScramble(getScramble());
    }

    protected void solveWings() {
        if (!wingsSolved()) {
            System.arraycopy(solvedWings, 0, scrambledStateSolvedWings, 0, solvedWings.length);
        } else
            scrambledStateSolvedWings = new boolean[] { true, true, true, true, true, true, true, true, true, true,
                    true, true, true, true, true, true, true, true, true, true, true, true, true, true };
        resetCube(true);
        wingCycles.clear();
        wingCycleNum = 0;
        while (!wingsSolved())
            cycleWingBuffer();
    }

    private void cycleWingBuffer() {
        boolean wingCycled = false;

        if (solvedWings[0] != false) {
            wingCycleNum += 1;
            int[] wingPref = { 1, 10, 12, 2, 4, 18, 20, 21, 23, 9, 5, 6, 7, 13, 14, 15, 19, 11, 3, 8, 16, 17, 22 };

            for (int i = 0; (i < 23) && (!wingCycled); i++) {
                int j = wingCubies[0].intValue() == U ? wingPref[i] : i;
                if (solvedWings[j] == false) {
                    int tempWing = wings[wingCubies[0].intValue()];

                    wings[wingCubies[0].intValue()] = wings[wingCubies[j].intValue()];

                    wings[wingCubies[j].intValue()] = tempWing;

                    wingCycles.add(wingCubies[j]);
                    wingCycled = true;
                }
            }
        } else {
            for (int i = 0; (i < 24) && (!wingCycled); i++) {
                if (wings[wingCubies[0].intValue()] == wingCubies[i].intValue()) {
                    wings[wingCubies[0].intValue()] = wings[wingCubies[i].intValue()];

                    wings[wingCubies[i].intValue()] = wingCubies[i].intValue();

                    wingCycles.add(wingCubies[i]);
                    wingCycled = true;
                }
            }
        }
    }

    private boolean wingsSolved() {
        boolean wingsSolved = true;

        for (int i = 0; i < 24; i++) {
            if ((i == 0) || (solvedWings[i] == false)) {
                if (wings[wingCubies[i].intValue()] == wingCubies[i].intValue()) {
                    solvedWings[i] = true;
                } else {
                    solvedWings[i] = false;
                    wingsSolved = false;
                }
            }
        }
        return wingsSolved;
    }

    protected void solveXCenters() {
        if (!xCentersSolved()) {
            System.arraycopy(solvedXCenters, 0, scrambledStateSolvedXCenters, 0, solvedXCenters.length);
        } else
            scrambledStateSolvedXCenters = new boolean[] { true, true, true, true, true, true, true, true, true, true,
                    true, true, true, true, true, true, true, true, true, true, true, true, true, true };
        resetCube(true);
        xCenterCycles.clear();
        xCenterCycleNum = 0;
        while (!xCentersSolved())
            cycleXCenterBuffer();
    }

    private void cycleXCenterBuffer() {
        boolean xCenterCycled = false;

        if (solvedXCenters[0] != false) {
            xCenterCycleNum += 1;

            for (int i = 0; (i < 23) && (!xCenterCycled); i++) {
                if (solvedXCenters[i] == false) {
                    int centerIndex = i;
                    if ((avoidXBreakIns) && (xCenters[xCenterCubies[(i / 4)][(i % 4)].intValue()] < E))
                        for (int j = i; j < i + 4 - i % 4; j++) {
                            if ((solvedXCenters[j] == false)
                                    && (xCenters[xCenterCubies[(j / 4)][(j % 4)].intValue()] > D)) {
                                centerIndex = j;
                                break;
                            }
                        }
                    int tempXCenter = xCenters[xCenterCubies[0][0].intValue()];

                    xCenters[xCenterCubies[0][0]
                            .intValue()] = xCenters[xCenterCubies[(centerIndex / 4)][(centerIndex % 4)].intValue()];

                    xCenters[xCenterCubies[(centerIndex / 4)][(centerIndex % 4)].intValue()] = tempXCenter;

                    xCenterCycles.add(xCenterCubies[(centerIndex / 4)][(centerIndex % 4)]);
                    xCenterCycled = true;
                }
            }
        } else {
            for (int i = 0; (i < 6) && (!xCenterCycled); i++) {
                for (int j = 0; (j < 4) && (!xCenterCycled); j++) {
                    if (xCenters[xCenterCubies[0][0].intValue()] == xCenterCubies[i][j].intValue()) {
                        int centerIndex = -1;
                        for (int k = i * 4 + j - j % 4; k < (i + 1) * 4 + j - j % 4; k++)
                            if (solvedXCenters[k] == false) {
                                centerIndex = k % 4;
                                break;
                            }
                        if (centerIndex < 0)
                            centerIndex = j;
                        if ((avoidXBreakIns) && (xCenters[xCenterCubies[i][centerIndex].intValue()] < E))
                            for (int l = i * 4 + centerIndex; l < (i + 1) * 4; l++) {
                                if ((solvedXCenters[l] == false)
                                        && (xCenters[xCenterCubies[(l / 4)][(l % 4)].intValue()] > D)) {
                                    centerIndex = l % 4;
                                    break;
                                }
                            }
                        xCenters[xCenterCubies[0][0].intValue()] = xCenters[xCenterCubies[i][centerIndex].intValue()];

                        xCenters[xCenterCubies[i][centerIndex].intValue()] = xCenterCubies[i][centerIndex].intValue();

                        xCenterCycles.add(xCenterCubies[i][centerIndex]);
                        xCenterCycled = true;
                    }
                }
            }
        }
    }

    private boolean xCentersSolved() {
        boolean xCentersSolved = true;

        for (int i = 0; i < 24; i++) {
            if ((i == 0) || (solvedXCenters[i] == false)) {
                int j = i / 4;
                if ((xCenters[xCenterCubies[j][(i % 4)].intValue()] == xCenterCubies[j][(i % 4)].intValue())
                        || (xCenters[xCenterCubies[j][(i % 4)].intValue()] == xCenterCubies[j][((i + 1) % 4)]
                                .intValue())
                        || (xCenters[xCenterCubies[j][(i % 4)].intValue()] == xCenterCubies[j][((i + 2) % 4)]
                                .intValue())
                        || (xCenters[xCenterCubies[j][(i % 4)].intValue()] == xCenterCubies[j][((i + 3) % 4)]
                                .intValue())) {

                    solvedXCenters[i] = true;
                } else {
                    solvedXCenters[i] = false;
                    xCentersSolved = false;
                }
            }
        }
        return xCentersSolved;
    }

    public String getWingPairs(boolean isSchemeRegualar) {
        String wingPairs = "'";
        if (wingCycles.size() != 0) {
            for (int i = 0; i < wingCycles.size(); i++) {
                wingPairs = wingPairs + wingLettering[((Integer) wingCycles.get(i)).intValue()];
                if (i % 2 == 1)
                    wingPairs = wingPairs + " ";
            }
            wingPairs += "'";
        }
        if (!isSchemeRegualar)
            return correctWingPairs(wingPairs, wingLettering, correspondingWingScheme);
        if (wingPairs == "'")
            return "";
        return wingPairs;
    }

    public String getXCenterBuffer() {
        return "'xcenter_buffer':" + "'" + xcenter_buffer + "'";
    }
    
    public String getWingsBuffer() {
        return "'wings_buffer':" + "'" + wings_buffer + "'";
    }
    public String getXCenterPairs() {

        String xCenterPairs = "'";
        if (xCenterCycles.size() != 0) {
            for (int i = 0; i < xCenterCycles.size(); i++) {
                xCenterPairs = xCenterPairs + xCenterLettering[((Integer) xCenterCycles.get(i)).intValue()];
                if (i % 2 == 1)
                    xCenterPairs = xCenterPairs + " ";
            }
            xCenterPairs += "'";
        }
        if (xCenterPairs == "'")
            return "";
        return xCenterPairs;
    }

    public String getSolutionPairs(boolean withRotation, boolean isWingSchemeRegular) {
        return (withRotation ? getRotations() + "\n" : "") + "'XCenters': " + getXCenterPairs() + "\n'Wings': "
                + getWingPairs(isWingSchemeRegular) + "\n'Corners': " + getCornerPairs();
    }

    public String getStatstics() {
        return "Corners: " + getCornerLength() + "@" + getCornerBreakInNum() + " w/ " + getNumPreSolvedCorners() + "-"
                + getNumPreTwistedCorners() + " > " + hasCornerParity() + "\nWings: " + getWingLength() + "@"
                + getWingBreakInNum() + " w/ " + getNumPreSolvedWings() + " > " + hasWingParity() + "\nXCenters: "
                + getXCenterLength() + "@" + getXCenterBreakInNum() + " w/ " + getNumPreSolvedXCenters() + " > "
                + hasXCenterParity();
    }

    public String getNoahtation() {
        return "C:" + getCornerNoahtation() + " / W:" + getWingNoahtation() + " / X:" + getXCenterNoahtation();
    }

    public boolean hasWingParity() {
        return wingCycles.size() % 2 == 1;
    }

    public int getWingLength() {
        return wingCycles.size();
    }

    public int getWingBreakInNum() {
        return wingCycleNum;
    }

    public boolean isWingSingleCycle() {
        return wingCycleNum == 0;
    }

    public int getNumPreSolvedWings() {
        int preSolved = 0;
        for (boolean scrambledStateSolvedWing : scrambledStateSolvedWings)
            if (scrambledStateSolvedWing)
                preSolved++;
        return preSolved;
    }

    public String getPreSolvedWings() {
        String solvedEdges = "";
        for (int i = 0; i < scrambledStateSolvedWings.length; i++)
            if (scrambledStateSolvedWings[i] != false)
                solvedEdges = solvedEdges + (solvedEdges.length() > 0 ? " " : "") + wingPositions[i];
        return solvedEdges;
    }

    public String getWingNoahtation() {
        return getWingLength() + "";
    }

    public boolean hasXCenterParity() {
        return xCenterCycles.size() % 2 == 1;
    }

    public int getXCenterLength() {
        return xCenterCycles.size();
    }

    public int getXCenterBreakInNum() {
        return xCenterCycleNum;
    }

    public boolean isXCenterSingleCycle() {
        return xCenterCycleNum == 0;
    }

    public int getNumPreSolvedXCenters() {
        int preSolved = 0;
        for (boolean scrambledStateSolvedXCenter : scrambledStateSolvedXCenters)
            if (scrambledStateSolvedXCenter)
                preSolved++;
        return preSolved;
    }

    public String getPreSolvedXCenters() {
        String solvedXCenters = "";
        for (int i = 0; i < scrambledStateSolvedXCenters.length; i++)
            if (scrambledStateSolvedXCenters[i] != false)
                solvedXCenters = solvedXCenters + (solvedXCenters.length() > 0 ? " " : "") + xCenterPositions[i];
        return solvedXCenters;
    }

    public String getXCenterNoahtation() {
        return getXCenterLength() + "";
    }

    public void setWingScheme(String scheme) {
        setWingScheme(scheme.split(""));
    }

    public void setWingScheme(String[] scheme) {
        if (scheme.length == 24)
            wingLettering = scheme;
    }

    public void setXCenterScheme(String scheme) {
        setXCenterScheme(scheme.split(""));
    }

    public void setXCenterScheme(String[] scheme) {
        if (scheme.length == 24)
            xCenterLettering = scheme;
    }

    public void setWingBuffer(String bufferAsLetter) {
        if (arrayContains(wingLettering, bufferAsLetter)) {
            int speffz = arrayIndex(wingLettering, bufferAsLetter);
            int index = arrayIndex(wingCubies, Integer.valueOf(speffz));
            for (int i = 0; i < index; i++)
                cycleArrayLeft(wingCubies);
            parseScramble(getScramble());
        }
    }

    public int findSrting(String string, String[] arrayString) {

        for (int i = 0; i < arrayString.length; i++) {
            if (arrayString[i].equals(string))
                return i;
        }

        return -1;
    }

    public void ScrambleCurrent4x4Cube(int current_line, String file_name_scramble) {
        BufferedReader reader;
        int count = 0;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(
                    file_name_scramble));
            line = reader.readLine();
            while (line != null && count != current_line) {
                line = reader.readLine();
                count++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // String scramble1 = scrambleCube3x3(faceNamesScramble3x3);
        this.scramble = line;
        System.out.println("here");
        System.out.println(this.scramble);
        initPermutations();
        parseScramble(this.scramble);
    }

    public String correctWingPairs(String wingPairs, String currentScheme[], String[] correspondingScheme) {

        String currentLetter = "";
        String correctedWingLetterPairs = "";
        int place;
        int j = 0;
        for (int i = 0; i < wingPairs.length(); i++) {
            currentLetter = currentLetter + wingPairs.charAt(i);
            place = findSrting(currentLetter, currentScheme);
            if (place != -1) {

                correctedWingLetterPairs += correspondingScheme[place];
            }

            else
                correctedWingLetterPairs = correctedWingLetterPairs + " ";

            currentLetter = "";

        }

        return correctedWingLetterPairs;

    }

    public void setXCenterBuffer(String bufferAsLetter) {
        if (arrayContains(xCenterLettering, bufferAsLetter)) {
            int speffz = arrayIndex(xCenterLettering, bufferAsLetter);
            int outer = deepArrayOuterIndex(xCenterCubies, Integer.valueOf(speffz));
            int inner = deepArrayInnerIndex(xCenterCubies, Integer.valueOf(speffz));
            for (int i = 0; i < outer; i++)
                cycleArrayLeft(xCenterCubies);
            for (int i = 0; i < inner; i++)
                cycleArrayLeft(xCenterCubies[0]);
            parseScramble(getScramble());
        }
    }
}
