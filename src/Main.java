import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        String dir= "write/service/class/directory/here";
        PackageExtractor.getPackageDeclarations(dir);
        String[] paths= readPaths();
        for(String path: paths) {
            try {
                Class<?> clazz = CodeSeparator.loadClass(path);
                String text = IntegrationTestBuilder.buildText(clazz);
                createFileWithText(text, clazz.getSimpleName() + "Test");
            }catch (Exception e){
                System.out.println("Failed to write test");
            }
        }
    }

    public static String[] readPaths(){
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("paths.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(getAbsolutePaths(line));
            }
        } catch (IOException e) {
            System.out.println("Failed to read paths from paths.txt");
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