import java.io.*;
import java.util.Arrays;


public class FourCube  {


    public long timeStopped4x4XCenters = 0;
    public long timeStopped4x4Wings = 0;
    public long timeStopped4x4Corners = 0;
    public int switchPieceCounter = 0;
    public long time4x4 = 0;


    public void setUpCube4x4(String scramble_file_name,String solve_file_name, int scrambleCount) {

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
        String[] wingScheme4x4 = { "ב", "ל", "ג", "מ", "י", "ת", "כ", "ח", "ק", "י", "פ", "ס", "ש", "נ", "ב",
                        "נ", "פ",
                        "צ" + '\u05F3', "א", "ג" + '\u05F3',"ח", "ת", "ד","ש" };
        String[] XCenterScheme = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                        "ע", "פ",
                        "צ", "ק", "ר", "צ" + '\u05F3', "ת", "ש", "ג" + '\u05F3' };
        String[] TCenterScheme = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                        "ע", "פ",
                        "צ", "ק", "ר", "ש", "ת", "צ" + '\u05F3', "ג" + '\u05F3' };
        String[] midgesScheme = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                        "ע", "פ",
                        "צ", "ק", "ר", "צ" + '\u05F3', "ת", "ש", "ג" + '\u05F3' };

        g.four.setCornerScheme(cornerScheme);
        g.four.setWingScheme(wingScheme4x4);
        g.four.setXCenterScheme(XCenterScheme);
        g.four.setCornerBuffer("ג");
        g.four.setWingBuffer("ג");
        g.four.setXCenterBuffer("ג");
        String final_data = "";

        String temp = "";
        for (int j = 0; j < cornerScheme.length; j++) {
                System.out.println(cornerScheme[j]);

        }
        for (int j = 0; j < 1; j++) {
                temp = "";
                for (int i = 1; i < 10; i++) {
                        g.four.ScrambleCurrent4x4Cube(i, scramble_file_name);
                        String scrambleString = g.four.getScramble();
                        String solutionPairs = g.four.getSolutionPairs(true, true);
                        String[] parts = solutionPairs.split("\n");
                        // final_data += scrambleString + "\n";
                        temp += scrambleString + "\n" + Arrays.toString(parts) + "\n";
                        

                }
                final_data += temp;
        }

        try {
                PrintWriter writer = new PrintWriter(solve_file_name, "UTF-8");
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
        FourCube c = new FourCube();
        String solve_file_name =  "C:\\פרויקטים\\bld_scrambles\\rotobld\\444bld_solves.txt";
        String scramble_file_name = "C:\\פרויקטים\\bld_scrambles\\rotobld\\444bld_scrambles.txt";
        c.setUpCube4x4(scramble_file_name,solve_file_name, 10);

}
}
