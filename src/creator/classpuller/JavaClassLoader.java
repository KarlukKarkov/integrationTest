package creator.classpuller;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaClassLoader {

    private static Class<?>[] loadAllClasses(String directoryPath) throws IOException {
        List<Class<?>> classes = new ArrayList<>();

        // Traverse the directory recursively
        Files.walk(Paths.get(directoryPath))
                .filter(path -> path.toString().endsWith(".java")) // Filter for .java files
                .forEach(path -> {
                    try {
                        // Convert file path to class name
                        String className = getClassNameFromFilePath(path, directoryPath);

                        // Load the class using Class.forName
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace(); // Handle class not found exception
                    }
                });

        return classes.toArray(new Class<?>[0]);
    }

    // Helper method to convert file path to class name
    private static String getClassNameFromFilePath(Path filePath, String rootDir) {
        // Convert file path to package name
        Path relativePath = rootDir == null ? filePath : Paths.get(rootDir).relativize(filePath);
        String packagePath = relativePath.toString()
                .replace(File.separator, ".") // Replace file separators with dots
                .replace(".java", ""); // Remove the ".java" extension

        return packagePath; // Fully qualified class name
    }

    public static Class<?>[] loadJavaClass(String pathUnderSrc,Class anno) { //for ex src.creator.ai will pull files under ai
        try {
            // Replace with your directory path
            String directoryPath = "src";
            // Get all the classes from the entire directory
            Class<?>[] allClasses = loadAllClasses(directoryPath);

            // Filter to only include classes from the "model" package
            List<Class<?>> modelClasses = new ArrayList<>();
            for (Class<?> clazz : allClasses) {
                // Check if the class is in the "model" package
                if (clazz.getPackageName().startsWith(pathUnderSrc)) {
                    for(Annotation annotation: clazz.getAnnotations()){
                        if (annotation.getClass().equals(anno)){
                            modelClasses.add(clazz);
                        }
                    }
                }
            }
            return modelClasses.toArray(new Class<?>[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
