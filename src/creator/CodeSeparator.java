package creator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;

public class CodeSeparator {
    public static Class<?> findClass(String classFilePath) {
        String fullyQualifiedName= extractPackageName(classFilePath);
        try {
            // Load the class using the fully qualified name
            return Class.forName(fullyQualifiedName);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + fullyQualifiedName);
            return null;
        }
    }
    public static String extractPackageName(String packageDeclaration) {
        if (packageDeclaration == null || packageDeclaration.isEmpty()) {
            return "";
        }

        // Remove "package " prefix and trailing ";"
        return packageDeclaration.replace("package ", "").replace(";", "").trim();
    }
    public static Method[] getAllMethods(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    } //given Class, returns all methods it contains
    public static void getImportStatement(Class<?> clazz) {
        Package pack = clazz.getPackage();
        String className = clazz.getName().replaceFirst(".*\\.", "");

        if (pack != null) {
            CodeSeparator.set.add( "import " + pack.getName() + "." + className + ";\n");
        }
    } //given Class, finds it's import statement
    public static void getImportStatementsForFields(Field[] fields) {

        for (Field field : fields) {
            Package pack = field.getType().getPackage();
            String className = field.getType().getName().replaceFirst(".*\\.", "");

            if (pack != null) {
                CodeSeparator.set.add(("import "+pack.getName()+"."+className+";\n"));
            }
        }
    } //given fields, finds import statements for all of them
    public static void getImportStatementsForMethodParameters(Method method) {
        Parameter[] params = method.getParameters();

        for (Parameter param : params) {
            Package pack = param.getType().getPackage();
            String className = param.getType().getName().replaceFirst(".*\\.", "");

            if (pack != null) {
                CodeSeparator.set.add(new StringBuilder().append("import ").append(pack.getName()).append(".").append(className).append(";\n").toString());
            }
        }
    } //given method, finds import statement for it's parameters

    public static void getImportStatementsForMethodParameters(Method[] methods) {
        for(Method m: methods) {
            getImportStatementsForMethodParameters(m);

        }
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
        StringBuilder str= new StringBuilder();
        CodeSeparator.getImportStatementsForFields(clazz.getDeclaredFields());
        CodeSeparator.getImportStatementsForMethodParameters(clazz.getDeclaredMethods());
        CodeSeparator.getImportStatement(clazz);
        //import class
        for (String importStatement: CodeSeparator.set){
            str.append(importStatement);
        }

        return str.toString();
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
        text=text+"\tpublic void "+(method.getName())+"IntegrationTest(){\n";
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
    static HashSet<String> set= new HashSet<>();
}
