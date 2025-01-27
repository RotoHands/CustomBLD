package cubes;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FiveCube {

    public long timeStopped5x5XCenters = 0;
    public long timeStopped5x5TCenters = 0;
    public long timeStopped5x5Midges = 0;
    public long timeStopped5x5Wings = 0;
    public long timeStopped5x5Corners = 0;
    public int switchPieceCounter = 0;
    public long time5x5 = 0;

    public void setUpCube5x5(String scramble_file_name, String scramble_type, String solve_file_name,
            Boolean changeSchemeBase) {

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
                "צ" + '\u05F3', "א", "ג" + '\u05F3', "ח", "ת", "ד", "ש" };
        String[] XCenterScheme = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                "ע", "פ",
                "צ", "ק", "ר", "צ" + '\u05F3', "ת", "ש", "ג" + '\u05F3' };
        String[] TCenterScheme = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                "ע", "פ",
                "צ", "ק", "ר", "ש", "ת", "צ" + '\u05F3', "ג" + '\u05F3' };
        String[] midgesScheme = { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ", "ס",
                "ע", "פ",
                "צ", "ק", "ר", "צ" + '\u05F3', "ת", "ש", "ג" + '\u05F3' };

        // g.four.setCornerScheme(cornerScheme);
        // g.four.setWingScheme(wingScheme4x4);
        // g.four.setXCenterScheme(XCenterScheme);
        g.five.setCornerBuffer("C");
        g.five.setWingBuffer("C", true);
        g.five.setXCenterBuffer("C");
        g.five.setTCenterBuffer("C");
        g.five.setEdgeBuffer("C");

        StringBuilder temp = new StringBuilder();
        try {
            PrintWriter writer = new PrintWriter(solve_file_name, "UTF-8");
            System.out.println("solve_file_name " + solve_file_name);
            BufferedReader reader;
            String currline;
            String scrambleString;
            String edge_buffer;
            String corner_buffer;
            String solutionPairs;
            String xcenter_buffer;
            String tcenter_buffer;
            String wing_buffer;
            String[] parts_sol;
            String[] parts_stats;

            edge_buffer = g.five.getEdgeBuffer();
            corner_buffer = g.five.getCornerBuffer();
            xcenter_buffer = g.five.getXCenterBuffer();
            tcenter_buffer = g.five.getTCenterBuffer();
            wing_buffer = g.five.getWingsBuffer();
            int i = 0;
            try {
                reader = new BufferedReader(new FileReader(scramble_file_name));
                while ((currline = reader.readLine()) != null) { // Loop until the end of the file
                    scrambleString = currline;
                    g.five.initPermutations();
                    g.five.parseScramble(scrambleString);

                    if (changeSchemeBase)
                        solutionPairs = g.five.getSolutionPairs(true, false);
                    else
                        solutionPairs = g.five.getSolutionPairs(true, true);
                    parts_sol = solutionPairs.split("\n");
                    parts_stats = g.five.getStatstics().split("\n");
                    temp.append(scramble_type)
                            .append(",")
                            .append(scrambleString)
                            .append(",")
                            .append(edge_buffer)
                            .append(",")
                            .append(corner_buffer)
                            .append(",")
                            .append(wing_buffer)
                            .append(",")
                            .append(xcenter_buffer)
                            .append(",")
                            .append(tcenter_buffer)
                            .append(",")
                            .append(String.join(",", parts_sol))
                            .append(",")
                            .append(String.join(",", parts_stats))
                            .append("\n");

                    if (i % 10000 == 0) {
                        System.out.println(i);
                        writer.print(temp.toString()); // Write accumulated data
                        writer.flush(); // Ensure data is written to the file
                        temp.setLength(0); // Clear the StringBuilder
                        System.out.println("Written up to scramble: " + i);
                    }
                    i++;

                }
                reader.close();

            } catch (IOException e) {
                System.out.println("fail");
            }
            if (temp.length() > 0) {
                writer.print(temp.toString());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("fail");
        } catch (UnsupportedEncodingException e) {
            System.out.println("fail");
        }

    }

    public static void main(String[] args) throws FileNotFoundException {

        String scramble_type = args[0];
        String arg_bool = args[1];
        Boolean changeSchemeBase = Boolean.parseBoolean(arg_bool);

        String folderPath = "txt_files\\";
        File folder = new File(folderPath);
        File newestFile = null;
        for (File file : folder.listFiles(
                (dir, name) -> name.startsWith(scramble_type) && name.contains("_scrambles"))) {
            if (newestFile == null || file.lastModified() > newestFile.lastModified()) {
                newestFile = file;
            }
        }

        String scrambleFileName = newestFile.getAbsolutePath();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String currentTime = LocalDateTime.now().format(formatter);

        String solveFileName = folderPath + scramble_type + "_solves_" + currentTime + ".txt";

        FiveCube c = new FiveCube();

        long startTime = System.nanoTime();

        // Call the method
        c.setUpCube5x5(scrambleFileName, scramble_type,
                solveFileName, changeSchemeBase);

        // Measure the end time
        long endTime = System.nanoTime();

        // Calculate and print the elapsed time in milliseconds
        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
        System.out.println("Execution time: " + elapsedTime + " ms");

    }
}
