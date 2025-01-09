package src;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class CodeSeparator {
    public static Class<?> loadClass(String classFilePath) {
        try {
            String path= extractPackageName(classFilePath);
            File classFile = new File(path);
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{classFile.getParentFile().toURI().toURL()});

            // Load the .class file into a Class object using the custom class loader
            return urlClassLoader.loadClass(findClassName(path));
        } catch (Exception e) {
            System.out.println("Failed to load file");
            return null;
        }
    } //given absolute path, outputs Class<?> of the path
    public static String extractPackageName(String packageDeclaration) {
        if (packageDeclaration == null || packageDeclaration.isEmpty()) {
            return "";
        }

        // Remove "package " prefix and trailing ";"
        return packageDeclaration.replace("package ", "").replace(";", "").trim();
    }

    private static String findClassName(String path){
        if (path == null || path.isEmpty()) {
            return "";
        }
        int lastDotIndex = path.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == path.length() - 1) {
            return "";
        }
        return path.substring(lastDotIndex + 1);
    } //given absolute path, outputs name of the class
    public static Method[] getAllMethods(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    } //given Class, returns all methods it contains
    public static Field[] getInstanceFields(Class<?> clazz) {
        return clazz.getDeclaredFields();

    } //given Class, returns all fields it contains
    public static String getImportStatement(Class<?> clazz) {
        Package pack = clazz.getPackage();
        String className = clazz.getName().replaceFirst(".*\\.", "");

        if (pack != null) {
            return "import " + pack.getName() + "." + className + ";";
        } else {
            return "";
        }
    } //given Class, finds it's import statement
    public static String getImportStatementsForFields(Field[] fields) {
        ArrayList<String> imports= new ArrayList<>();

        for (Field field : fields) {
            Package pack = field.getType().getPackage();
            String className = field.getType().getName().replaceFirst(".*\\.", "");

            if (pack != null) {
                imports.add(("import "+pack.getName()+"."+className+";"));
            }
        }

        return imports.toString();
    } //given fields, finds import statements for all of them
    public static String getImportStatementsForMethodParameters(Method method) {
        Parameter[] params = method.getParameters();
        StringBuilder imports = new StringBuilder();

        for (Parameter param : params) {
            Package pack = param.getType().getPackage();
            String className = param.getType().getName().replaceFirst(".*\\.", "");

            if (pack != null) {
                imports.append("import ").append(pack.getName()).append(".").append(className).append(";\n");
            }
        }

        return imports.toString();
    } //given method, finds import statement for it's parameters
    public static String getImportStatementsForMethodParameters(Method[] methods) {
        StringBuilder str= new StringBuilder();
        for(Method m: methods) {
            str.append(getImportStatementsForMethodParameters(m));
        }
        return str.toString();
    } //given methods, finds import statement for all of their parameters
    public static String createPackageStatement(Class<?> clazz) { //change this one according to your own package standardization
        Package pkg = clazz.getPackage();
        if (pkg != null) {
            return "package " + pkg.getName() + ";";
        } else {
            return "No package information available.";
        }
    } //given class, creates it's package statement
    public static String getAllImportStatements(Class<?> clazz){
        String str= "";
        //import class
        str=str+ CodeSeparator.getImportStatement(clazz)+"\n";
        //import Fields
        str=str+ CodeSeparator.getImportStatementsForFields(CodeSeparator.getInstanceFields(clazz))+"\n";
        //import Method parameters
        str=str+ CodeSeparator.getImportStatementsForMethodParameters(CodeSeparator.getAllMethods(clazz))+"\n";

        return str;
    } //given class, creates all of it's import statements
    //Method initializing
    public static String createAllMethods(Method[] methods){
        StringBuilder text= new StringBuilder();
        text.append("\t//Tests\n\n");
        for(Method method:methods) {
            text.append(createMethod(method.getName(), method));
        }
        return text.append("\n").toString();
    }
    public static String createMethod(String methodName,Method method){
        String text="";
        text=text+"\tpublic void "+(method.getName())+"IntegrationTest()\n";
        switch (methodName){
            case "save": {
                text =text+ createSaveMethod(method);
                break;
            }
            case "update":{
                text=text+ createUpdateMethod(method);
                break;
            }
            default:
                text=text+createDefaultMethod(method);
        }
        text=text+"\t}\n";
        return text;
    } //creates methods
    private static String createDefaultMethod(Method method){
        String text="";

        //implement empty method here
        text=text+"\t\t/// IMPLEMENT THE CODE HERE ///\n";
        return text;
    } //empty method created
    private static String createSaveMethod(Method method){
        String text="";

        //implement the text here

        return text;
    } //"save" method created
    private static String createUpdateMethod(Method method){
        String text="";

        //implement the text here

        return text;
    }
}
