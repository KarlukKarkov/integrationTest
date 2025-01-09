package src;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Scanner;

public class AIManipuation {

    private static String getMethodSignature(Method method) {
        StringBuilder signature = new StringBuilder();

        // Add modifiers (e.g., public, static)
        int modifiers = method.getModifiers();
        if (modifiers != 0) {
            signature.append(Modifier.toString(modifiers)).append(" ");
        }

        // Add return type
        Class<?> returnType = method.getReturnType();
        signature.append(returnType.getSimpleName()).append(" ");

        // Add method name
        signature.append(method.getName()).append("(");

        // Add parameters
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                signature.append(", ");
            }
            signature.append(parameters[i].getType().getSimpleName()).append(" ").append(parameters[i].getName());
        }

        // Add closing parenthesis and opening brace
        signature.append("){");

        return signature.toString();
    } //exactly same except param names changed with args0,args1 ext.

    public static void main(String[] args) throws NoSuchMethodException {

    }
    public static String getMethodBody(Method method,String clazz){
        int[] indexes=findMethodStartFinishLine(method,clazz);
        StringBuilder builder= new StringBuilder();
        Scanner scanner= new Scanner(clazz);
        int counter=1;
        while (scanner.hasNextLine()){
            String currentLine=scanner.nextLine();
            if((counter>=indexes[0])&&(counter<=indexes[1])){
                builder.append(currentLine).append("\n");
            }
        }
        return builder.toString();
    }
    private static int[] findMethodStartFinishLine(Method method, String clazz){
        int[] indexes= {-1,-1};
        String signature= getMethodSignature(method);
        indexes[0]= findMethodStartLine(clazz,signature);
        indexes[1]=findMethodFinishLine(clazz,indexes[0]);
        return indexes;
    }
    private static int findMethodFinishLine(String clazz,int startLine){
        String[] lines = clazz.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String trimmedLine = lines[i].trim();
            if (compareMethodSignatures(trimmedLine,"}")) {
                if(i>startLine)
                    return i + 1; // Line numbers start from 1
            }
        }
        return -1; // Return -1 if the method signature is not found
    }
    private static int findMethodStartLine(String clazz, String line) {
        String[] lines = clazz.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String trimmedLine = lines[i].trim();
            if (compareMethodSignatures(line.trim(),trimmedLine)) {
                return i + 1; // Line numbers start from 1
            }
        }
        return -1; // Return -1 if the method signature is not found
    }
    private static boolean compareMethodSignatures(String generatedSignature, String javaFileLine) { //works correctly
        // Normalize parameter names in both signatures
        String normalizedGeneratedSignature = normalizeParameterNames(generatedSignature);
        String normalizedJavaFileLine = normalizeParameterNames(javaFileLine);

        // Compare normalized signatures
        return normalizedGeneratedSignature.equals(normalizedJavaFileLine);
    }

    private static String normalizeParameterNames(String methodSignature) {
        return methodSignature.replaceAll("\\b\\w+\\s+(\\w+)\\b", "$1").replaceAll("\\s+\\w+\\s*,", ",").replaceAll("\\s+\\w+\\s*\\)", ")");
    }

}



