import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PackageExtractor {

    public static String[] getPackageDeclarations(String directoryPath) {
        List<String> packageDeclarations = new ArrayList<>();
        processDirectory(new File(directoryPath), packageDeclarations);
        return packageDeclarations.toArray(new String[0]);
    }

    private static void processDirectory(File directory, List<String> packageDeclarations) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        processDirectory(file, packageDeclarations);
                    } else if (file.getName().endsWith(".java")) {
                        processFile(file, packageDeclarations);
                    }
                }
            }
        }
    }

    private static void processFile(File file, List<String> packageDeclarations) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean packageFound = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("package ")) {
                    packageDeclarations.add(line);
                    packageFound = true;
                    break;
                }
            }
            if (!packageFound) {
                packageDeclarations.add("No package declaration found in " + file.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}