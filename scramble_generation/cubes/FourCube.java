package cubes;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FourCube {

    public void setUpCube4x4(String scramble_file_name, String scramble_type, String solve_file_name,
            Boolean changeSchemeBase, String cornerBuffer, String wingBuffer, String xcenterBuffer) {

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
                "צ", "ק", "ר", "צ" + '\u05F3', "ת", "ש", "ג" + '\u05F3' };
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
        g.four.setCornerBuffer(cornerBuffer);
        g.four.setWingBuffer(wingBuffer, changeSchemeBase);
        g.four.setXCenterBuffer(xcenterBuffer);

        String corner_buffer_str = "'Corner_buffer':" + "'" + cornerBuffer + "'";
        String wing_buffer_str = "'Wings_buffer':" + "'" + wingBuffer + "'";
        String xcenter_buffer_str = "'XCenter_buffer':" + "'" + xcenterBuffer + "'";

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
            String wing_buffer;
            String[] parts_sol;
            String[] parts_stats;

            // corner_buffer = g.four.getCornerBuffer();
            // xcenter_buffer = g.four.getXCenterBuffer();
            // wing_buffer = g.four.getWingsBuffer();

            int i = 0;
            try {
                reader = new BufferedReader(new FileReader(scramble_file_name));
                while ((currline = reader.readLine()) != null) { // Loop until the end of the file
                    scrambleString = currline;
                    g.four.initPermutations();
                    g.four.parseScramble(scrambleString);

                    if (changeSchemeBase)
                        solutionPairs = g.four.getSolutionPairs(true, false);
                    else
                        solutionPairs = g.four.getSolutionPairs(true, true);
                    parts_sol = solutionPairs.split("\n");
                    parts_stats = g.four.getStatstics().split("\n");
                    temp.append(scramble_type)
                            .append(",")
                            .append(scrambleString)
                            .append(",")
                            .append(corner_buffer_str)
                            .append(",")
                            .append(wing_buffer_str)
                            .append(",")
                            .append(xcenter_buffer_str)
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

    public void setUpCube4x4(String scramble_file_name, String scramble_type, String solve_file_name,
            Boolean changeSchemeBase) {
        // Call the parameterized version with default buffers
        setUpCube4x4(scramble_file_name, scramble_type, solve_file_name, changeSchemeBase, "C", "C", "C");
    }

    public static void main(String[] args) throws FileNotFoundException {

        String scramble_type = args[0];
        String arg_bool = args[1];
        Boolean changeSchemeBase = Boolean.parseBoolean(arg_bool);

        // Default buffers
        String cornerBuffer = "C";
        String wingBuffer = "C";
        String xcenterBuffer = "C";

        // Parse buffer arguments
        for (int i = 2; i < args.length; i++) {
            if (args[i].equals("--corner_buffer") && i + 1 < args.length) {
                cornerBuffer = args[i + 1];
                i++; // Skip the next argument (the value)
            } else if (args[i].equals("--wing_buffer") && i + 1 < args.length) {
                wingBuffer = args[i + 1];
                i++; // Skip the next argument (the value)
            } else if (args[i].equals("--xcenter_buffer") && i + 1 < args.length) {
                xcenterBuffer = args[i + 1];
                i++; // Skip the next argument (the value)
            }
        }

        System.out.println("Args: " + Arrays.toString(args));
        System.out
                .println("Buffers - Corner: " + cornerBuffer + ", Wing: " + wingBuffer + ", XCenter: " + xcenterBuffer);

        String folderPath = "txt_files" + File.separator;
        File folder = new File(folderPath);
        System.out.println("Looking for files in: " + folder.getAbsolutePath());

        if (!folder.exists()) {
            System.out.println("Creating txt_files directory...");
            folder.mkdirs();
        }

        // Check if the directory exists now
        if (!folder.exists()) {
            System.out.println("ERROR: Failed to create directory: " + folder.getAbsolutePath());
            return;
        }

        System.out.println("Directory exists: " + folder.exists() + ", isDirectory: " + folder.isDirectory());

        // Get all files in the directory and print them for debugging
        File[] allFiles = folder.listFiles();
        if (allFiles != null) {
            System.out.println("All files in directory (" + allFiles.length + "):");
            for (File f : allFiles) {
                System.out.println(
                        "  - " + f.getName() + " (last modified: " + new java.util.Date(f.lastModified()) + ")");
            }
        } else {
            System.out.println("WARNING: folder.listFiles() returned null");
        }

        File[] files = folder.listFiles(
                (dir, name) -> name.startsWith(scramble_type) && name.contains("_scrambles"));

        if (files == null || files.length == 0) {
            System.out.println("No matching files found in " + folderPath);
            System.out.println("Looking for pattern: " + scramble_type + "*_scrambles*");
            return;
        }

        System.out.println("Found " + files.length + " matching files:");
        for (File f : files) {
            System.out.println("  - " + f.getName() + " (last modified: " + new java.util.Date(f.lastModified()) + ")");
        }

        File newestFile = null;
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

        String solveFileName = folderPath + scramble_type + "_solves_" + currentTime + ".txt";
        System.out.println("Will write solutions to: " + solveFileName);

        FourCube c = new FourCube();

        long startTime = System.nanoTime();

        // Set the buffers on the Globals instance
        Globals g = new Globals();
        g.four.setCornerBuffer(cornerBuffer);
        g.four.setWingBuffer(wingBuffer, changeSchemeBase);
        g.four.setXCenterBuffer(xcenterBuffer);

        // Call the method
        try {
            c.setUpCube4x4(scrambleFileName, scramble_type,
                    solveFileName, changeSchemeBase, cornerBuffer, wingBuffer, xcenterBuffer);
        } catch (Exception e) {
            System.out.println("ERROR during setUpCube4x4: " + e.getMessage());
            e.printStackTrace();
        }

        // Measure the end time
        long endTime = System.nanoTime();

        // Calculate and print the elapsed time in milliseconds
        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
        System.out.println("Execution time: " + elapsedTime + " ms");
    }
}
