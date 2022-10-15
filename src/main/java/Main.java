

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    private static int counter = 0;
    private static final Date date = new Date();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    private static StringBuilder encodeCsvString(byte[] byteArray) {
        StringBuilder hexStringBuilder = new StringBuilder();
        hexStringBuilder.append(counter++);
        hexStringBuilder.append(";DS;П;0000");
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuilder.append(byteToHex(byteArray[i]).toUpperCase());
        }
        hexStringBuilder.append(";;" + dateFormat.format(date) + ";;\n");
        return hexStringBuilder;
    }


    private static void writeToFile(String path, String text) throws IOException {
        File file = new File(path);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(text);
        fileWriter.close();
    }

    private static void writeToFile(String path, StringBuilder text) throws IOException {
        writeToFile(path, text.toString());
    }

    public static void main(String[] args) throws IOException {
        //source file containing keys
        File inputFile = new File("dump.bin");
        if (!inputFile.exists()) {
            writeToFile("readme.txt", "Source file not found. Please put source file in this directory. File name should be \"dump.bin\".");
            System.exit(0);
        }
        StringBuilder text = new StringBuilder("№;Тип;Аттр;Номер;Короткий;Добавлен;Описание;\n");
        byte[] buffer = new byte[8]; // 8 bytes of key
        byte[] shortBuffer = new byte[4]; // 4 bytes for short key
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        while ((fileInputStream.read(buffer)) == 8) {
            for (int j = 0; j < 4; j++) {
                shortBuffer[j] = buffer[3 - j];
            }
            text.append(encodeCsvString(shortBuffer));
        }
        writeToFile("output.csv", text);
        fileInputStream.close();
    }
}
