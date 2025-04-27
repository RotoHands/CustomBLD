package cubes;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThreeCube {

        public long timeStopped3x3Edges = 0;
        public long timeStopped3x3Corners = 0;
        public int switchPieceCounter = 0;
        public long time3x3 = 0;

        private static final int THREAD_COUNT = 8; // Number of threads
        private static final int BATCH_SIZE = 5000; // Scrambles per thread

        public void setUpCube3x3(String scramble_file_name, String scramble_type, String solve_file_name,
                        String cornerBuffer, String edgeBuffer) {

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

                // g.three.setCornerScheme(cornerScheme);
                // g.three.setEdgeScheme(edgeScheme);
                g.three.setCornerBuffer(cornerBuffer);
                g.three.setEdgeBuffer(edgeBuffer);
                String corner_buffer_str = "'Corner_buffer':" + "'" + cornerBuffer + "'";
                String edge_buffer_str = "'Edge_buffer':" + "'" + edgeBuffer + "'";
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
                        String[] parts_sol;
                        String[] parts_stats;

                        // edge_buffer = g.three.getEdgeBuffer();
                        // corner_buffer = g.three.getCornerBuffer();
                        // System.out.println(edge_buffer);
                        // System.out.println(corner_buffer);
                        int i = 0;
                        try {
                                reader = new BufferedReader(new FileReader(scramble_file_name));
                                while ((currline = reader.readLine()) != null) { // Loop until the end of the file
                                        scrambleString = currline;
                                        g.three.initPermutations();
                                        g.three.parseScramble(scrambleString);

                                        solutionPairs = g.three.getSolutionPairs(true, false);
                                        parts_sol = solutionPairs.split("\n");
                                        parts_stats = g.three.getStatstics().split("\n");
                                        temp.append(scramble_type)
                                                        .append(",")
                                                        .append(scrambleString)
                                                        .append(",")
                                                        .append(edge_buffer_str)
                                                        .append(",")
                                                        .append(corner_buffer_str)
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

        public void setUpCube3x3(String scramble_file_name, String scramble_type, String solve_file_name) {
                // Call the parameterized version with default buffers
                setUpCube3x3(scramble_file_name, scramble_type, solve_file_name, "C", "C");
        }

        public static void main(String[] args) throws FileNotFoundException {

                String scramble_type = args[0];

                // Default buffers
                String cornerBuffer = "C";
                String edgeBuffer = "C";
                System.out.println("args: " + Arrays.toString(args));
                for (int i = 1; i < args.length; i++) {
                        if (args[i].equals("--corner_buffer") && i + 1 < args.length) {
                                cornerBuffer = args[i + 1];
                                i++; // Skip the next argument (the value)
                        } else if (args[i].equals("--edge_buffer") && i + 1 < args.length) {
                                edgeBuffer = args[i + 1];
                                i++; // Skip the next argument (the value)
                        }
                }

                // Create txt_files directory if it doesn't exist
                String folderPath = "txt_files";
                File folder = new File(folderPath);
                if (!folder.exists()) {
                        System.out.println("Creating txt_files directory...");
                        folder.mkdirs();
                }

                File newestFile = null;
                File[] files = folder.listFiles(
                                (dir, name) -> name.startsWith(scramble_type) && name.contains("_scrambles"));

                if (files == null || files.length == 0) {
                        System.out.println("No matching files found in " + folderPath);
                        return;
                }

                for (File file : files) {
                        if (newestFile == null || file.lastModified() > newestFile.lastModified()) {
                                newestFile = file;
                        }
                }

                if (newestFile == null) {
                        System.out.println("No files found matching pattern: " + scramble_type + "*_scrambles*");
                        return;
                }

                String scrambleFileName = newestFile.getAbsolutePath();
                System.out.println("Using scramble file: " + scrambleFileName);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                String currentTime = LocalDateTime.now().format(formatter);

                String solveFileName = folderPath + "/" + scramble_type + "_solves_" + currentTime + ".txt";
                System.out.println("Will write solutions to: " + solveFileName);

                ThreeCube c = new ThreeCube();

                long startTime = System.nanoTime();

                // Set the buffers on the Globals instance
                Globals g = new Globals();

                // Call the method
                c.setUpCube3x3(scrambleFileName, scramble_type,
                                solveFileName, cornerBuffer, edgeBuffer);

                // Measure the end time
                long endTime = System.nanoTime();

                // Calculate and print the elapsed time in milliseconds
                long elapsedTime = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
                System.out.println("Execution time: " + elapsedTime + " ms");
        }
}

// how to run the program from cmd
// cd
// "C:\Users\rotem\AppData\Roaming\Code\User\workspaceStorage\b42ee5f7ca190946e798fbd2338ac8d4\redhat.java\jdt_ws\rotobld_8d89bd7a\bin"
// && "C:\Program Files\Eclipse Foundation\jdk-11.0.12.7-hotspot\bin\java.exe"
// -cp . ThreeCube