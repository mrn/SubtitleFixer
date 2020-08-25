import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SubtitleFixer {
    public static void main(String[] args) {
        System.out.println("Enter full file path:");
        Scanner userInput = new Scanner(System.in);
        String filePath = userInput.nextLine();
        userInput.close();
        String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.lastIndexOf("."));
        String fileDir = filePath.substring(0, filePath.lastIndexOf("\\") + 1);

        File outputFile = new File(fileDir + "FIX_" + fileName + ".srt");

        Map<String, String> charMap = fillMap();

        try (OutputStreamWriter writer =
                new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), StandardCharsets.UTF_8));
            String line;

            while ((line = reader.readLine()) != null) {
                byte[] lineBytes = line.getBytes(StandardCharsets.UTF_8);
                String encodedLine = new String(lineBytes, StandardCharsets.UTF_8);
                String repairedLine = replaceChars(encodedLine, charMap);
                System.out.println(repairedLine);
                writer.write(repairedLine + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> fillMap() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("È", "Č");
        map.put("è", "č");

        map.put("Æ", "Ć");
        map.put("æ", "ć");

        map.put("Ð", "Đ");
        map.put("ð", "đ");

        // reserved
        // map.put("X", "Š");
        // map.put("x", "š");

        // map.put("X", "Ž");
        // map.put("x", "ž");

        return map;
    }

    private static String replaceChars(String str, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (str.contains(entry.getKey())) {
                str = str.replace(entry.getKey(), entry.getValue());
            }
        }
        return str;
    }
}
