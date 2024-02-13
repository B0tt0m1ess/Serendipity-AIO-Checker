package SerendipityAIOChecker.checker.Microsoft;

import SerendipityAIOChecker.checker.CUI.CUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MicrosoftComboFilter{
    public static void Start() {
        String inputFilePath = "./files/Combos.txt";
        String outputFilePath = "./files/Output.txt";
        Set<String> output = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            int out = 0;
            int input = 0;
            while ((line = br.readLine()) != null) {
                input++;
                try {
                    String[] parts = line.split(":");
                    if (parts.length == 2 && parts[1].trim().length() >= 8 & Microsoft.checkRequirements(parts[1])) {
                        out++;
                        output.add(line);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            for (String c:output){
                bw.write(c);
                bw.newLine();
            }
            System.out.println(CUI.ANSI_PURPLE + "[Input]" + CUI.ANSI_YELLOW + input);
            System.out.println(CUI.ANSI_PURPLE + "[Out]" + CUI.ANSI_YELLOW + out);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}