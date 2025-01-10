package creator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        boolean flag=true;
        String[] paths;
        String dir= "C:\\Users\\inan.ozsahin\\IdeaProjects\\integration\\src";

        if(!flag) paths= PackageExtractor.getPathFromPackageDeclaration(dir);
        else paths=readPaths();

        for(String path: paths) {
            try {
                System.out.println("1: "+path);
                //Class<?> clazz = CodeSeparator.loadClass(path);
                //String text = IntegrationTestBuilder.buildText(clazz);
                //createFileWithText(text, clazz.getSimpleName() + "Test");
            }catch (Exception e){
                System.out.println("FAILED TO WRITE TEST!: "+path);
            }
        }
    }

    public static String[] readPaths() {
        String fileName= "C:\\Users\\inan.ozsahin\\IdeaProjects\\integration\\src\\src\\paths.txt";
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Failed to read paths from " + fileName + ": " + e.getMessage());
        }
        return lines.toArray(new String[0]);
    } //reads all the paths and returns them as String[]
    private static String getAbsolutePaths(String pkg) {

        if (pkg.startsWith("package ")) {
            // Remove "package " and the semicolon at the end
            return pkg.substring(8, pkg.length() - 1);
        }
        return null;
    } //given input as package statement, finds it's absolute path
    public static void createFileWithText(String text, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(text);
            System.out.println("File '" + fileName + "' created and text written successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    } //creates the methods name
}
