
import java.io.*;
import java.util.Arrays;
// import org.json.simple.JSONArray;
// import rotobld.Globals;

public class ThreeCube {

        public long timeStopped3x3Edges = 0;
        public long timeStopped3x3Corners = 0;
        public int switchPieceCounter = 0;
        public long time3x3 = 0;

        public void setUpCube3x3() {

                Globals g = new Globals();
                String[] cornerScheme = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                                "ע", "פ",
                                "צ", "ק", "ר", "צ'", "ת", "ש", "ג'" };
                String[] edgeScheme = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס", "ע",
                                "פ",
                                "צ", "ק", "ר", "צ'", "ת", "ש", "ג'" };
                String[] wingScheme5x5 = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                                "ע", "פ",
                                "צ", "ק", "ר", "ש", "ת", "1", "2" };
                String[] wingScheme4x4 = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                                "ע", "פ",
                                "צ", "ק", "ר", "ש", "ת", "1", "2" };
                String[] XCenterScheme = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                                "ע", "פ",
                                "צ", "ק", "ר", "צ" + '\u05F3', "ת", "ש", "ג" + '\u05F3' };
                String[] TCenterScheme = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                                "ע", "פ",
                                "צ", "ק", "ר", "ש", "ת", "צ" + '\u05F3', "ג" + '\u05F3' };
                String[] midgesScheme = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                                "ע", "פ",
                                "צ", "ק", "ר", "צ" + '\u05F3', "ת", "ש", "ג" + '\u05F3' };

                g.three.setCornerScheme(cornerScheme);
                g.three.setEdgeScheme(edgeScheme);
                g.three.setCornerBuffer("ג");
                g.three.setEdgeBuffer("ג");
                String final_data = "";

                String temp = "";

                for (int j = 0; j < cornerScheme.length; j++) {
                        System.out.println(cornerScheme[j]);

                }
                for (int j = 0; j < 1; j++) {
                        temp = "";
                        for (int i = 1; i < 10; i++) {
                                // System.out.println(i + j * 10000);
                                System.out.println(i);
                                g.three.ScrambleCurrent3x3Cube(i);
                                String scrambleString = g.three.getScramble();
                                String solutionPairs = g.three.getSolutionPairs(false, false);
                                String[] parts = solutionPairs.split("\n");
                                // final_data += scrambleString + "\n";
                                temp += scrambleString + "\n" + Arrays.toString(parts) + "\n";
                                

                        }
                        final_data += temp;
                }

                try {
                        PrintWriter writer = new PrintWriter(
                                        "C:\\פרויקטים\\bld_scrambles\\rotobld\\solves.txt", "UTF-8");
                        System.out.println(final_data);
                        writer.println(final_data);
                        writer.close();
                } catch (FileNotFoundException e) {
                        System.out.println("fail");
                } catch (UnsupportedEncodingException e) {
                        System.out.println("fail");
                }

        }

        public static void main(String[] args) throws FileNotFoundException {
                
                // System.out.println("Hello World");
                ThreeCube c = new ThreeCube();
                c.setUpCube3x3();

        }
}
