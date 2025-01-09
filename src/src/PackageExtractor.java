package src;
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

    public static String[] getPathFromPackageDeclaration(String directoryPath) {
        String[] packages= getPackageDeclarations(directoryPath);
        if (packages == null || packages.length == 0) {
            return new String[0];
        }

        String[] paths = new String[packages.length];
        for (int i = 0; i < packages.length; i++) {
            String pack = packages[i];
            if (pack != null && !pack.isEmpty()) {
                String packageDeclaration = pack.replace("package ", "").replace(";", "").trim();
                paths[i] = packageDeclaration.replace('.', File.separatorChar);
            } else {
                paths[i] = "";
            }
            System.out.println(paths[i]);
        }
        return paths;
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
