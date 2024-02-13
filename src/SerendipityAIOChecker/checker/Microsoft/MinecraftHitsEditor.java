package SerendipityAIOChecker.checker.Microsoft;

import SerendipityAIOChecker.checker.CUI.CUI;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MinecraftHitsEditor {

    public static void starting() {
        String filePath = "./files/Hits.txt"; // ???????��??
        String delimiter = "HEXHNB"; // ????
        String outputFolder = "EditorOutput"; // ????????

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            StringBuilder currentSegment = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                // ??"N/A"?�I?"Unknown"
                line = line.replace("N/A", "Unknown");

                if (line.contains(delimiter)) {
                    processSegment(currentSegment.toString(), outputFolder);
                    currentSegment.setLength(0); // ?????????????
                } else {
                    currentSegment.append(line).append(System.lineSeparator());
                }
            }

            // ??????????????
            if (!currentSegment.isEmpty()) {
                processSegment(currentSegment.toString(), outputFolder);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSegment(String segment, String outputFolder) {
        String time = CUI.time();
        String[] lines = segment.split(System.lineSeparator());
        String firstLine = lines[0].trim();
        String fileName = firstLine.replaceAll("\\s+", "---").replace("MinecraftUsername:", "")
                .replace("HypixelLevel:", "").replace("HypixelRanked:", "") + ".txt";
        String outputPath = null;
        String[] levelEditor = fileName.split("---");
        double level = 0;
        if (!levelEditor[1].equals("Unknown")) {
            level = Double.parseDouble(levelEditor[1]);
        }

        if (level >= 0 & level < 10) {
            String oneToTenLevel = "Hypixel_1+_Level_Player";
            outputFolder = outputFolder + "/" + time + "/" + oneToTenLevel;
            outputPath = outputFolder + "/" + fileName;
        } else if (level >= 10 & level < 21) {
            String oneToTenLevel = "Hypixel_10+_Level_Player";
            outputFolder = outputFolder + "/" + time + "/" + oneToTenLevel;
            outputPath = outputFolder + "/" + fileName;
        } else if (level >= 21) {
            String oneToTenLevel = "Hypixel_21+_Level_Player";
            outputFolder = outputFolder + "/" + time + "/" + oneToTenLevel;
            outputPath = outputFolder + "/" + fileName;
        }
        try {
            Path folderPath = Paths.get(outputFolder);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            BufferedWriter writer = null;
            if (outputPath != null) {
                writer = new BufferedWriter(new FileWriter(outputPath));
            }
            if (writer != null) {
                writer.write(segment);
            }
            if (writer != null) {
                writer.close();
            }
            System.out.println("Created file: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
