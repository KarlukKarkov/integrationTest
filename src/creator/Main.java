package creator;

import creator.classpuller.JavaClassLoader;

import java.io.*;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Class<?>[] classes= JavaClassLoader.loadJavaClass("", Main.class);
        System.out.println(classes.length);
        for (Class clazz: classes) {
            String text = IntegrationTestBuilder.buildText(clazz);
            System.out.println(clazz.getSimpleName());
            createFileWithText(text, clazz.getSimpleName() + "Test");
        }
    }

    public static void createFileWithText(String text, String fileName) {
        try (FileWriter fileWriter = new FileWriter("src\\creator\\tests\\"+fileName+".java")) {
            fileWriter.write(text);
            System.out.println("File '" + fileName + "' created and text written successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    } //creates the methods name
}
