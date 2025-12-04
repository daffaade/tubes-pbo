package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String DATA_DIR = "data/";
    
    public static void buatDirektori() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public static void simpanKeFile(String namaFile, String data) {
        buatDirektori();
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(DATA_DIR + namaFile, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error menyimpan ke file: " + e.getMessage());
        }
    }
    
    public static List<String> bacaDariFile(String namaFile) {
        List<String> data = new ArrayList<>();
        buatDirektori();
        File file = new File(DATA_DIR + namaFile);
        
        if (!file.exists()) {
            return data;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    data.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error membaca file: " + e.getMessage());
        }
        
        return data;
    }
    
    public static void simpanSemuaKeFile(String namaFile, List<String> data) {
        buatDirektori();
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(DATA_DIR + namaFile, false))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error menyimpan ke file: " + e.getMessage());
        }
    }
}