package creator;

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

    public static void main(String[] args) {
        ClassReader reader= new ClassReader();
        getMethodBody(reader.getDeclaredMethod(),reader.readClass());

    }
    public static String getMethodBody(Method method,String clazz){
        int[] indexes=findMethodStartFinishLine(method,clazz);
        //System.out.println(indexes[0]+" "+indexes[1]);
        StringBuilder builder= new StringBuilder();
        Scanner scanner= new Scanner(clazz);
        int counter=1;
        while (scanner.hasNextLine()){
            String currentLine=scanner.nextLine();
            if((counter>=indexes[0])&&(counter<=indexes[1])){
                builder.append(currentLine).append("\n");
            }
            counter++;
        }
        return builder.toString();
    }
    private static int[] findMethodStartFinishLine(Method method, String clazz){
        int[] indexes= {-1,-1};
        String signature= getMethodSignature(method);
        System.out.println(signature);
        indexes[0]= findMethodStartLine(clazz,signature);
        indexes[1]=findMethodFinishLine(clazz,indexes[0]);
        return indexes;
    }
    private static int findMethodFinishLine(String clazz,int startLine){
        String[] lines = clazz.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (compareMethodSignatures(lines[i],"\s\s\s\s}")) {
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
            //System.out.println(i+" "+trimmedLine);
            if (compareMethodSignatures(line.trim(),trimmedLine)) {
                return i + 1; // Line numbers start from 1
            }
        }
        return -1; // Return -1 if the method signature is not found
    }
    private static boolean compareMethodSignatures(String generatedSignature, String javaFileLine)  { //works correctly
        // Normalize parameter names in both signatures
        String normalizedGeneratedSignature = normalizeParameterNames(generatedSignature);
        String normalizedJavaFileLine = normalizeParameterNames(javaFileLine);

        if(normalizedJavaFileLine.startsWith("public")){
            System.out.println(normalizedGeneratedSignature);

        }
        // Compare normalized signatures
        return normalizedGeneratedSignature.equals(normalizedJavaFileLine);
    }

    private static String normalizeParameterNames(String methodSignature) {
        if (methodSignature.matches("^(public|private|static)\\s+.*")) {
            // Find the position of the opening and closing parentheses for the parameter list
            int startIdx = methodSignature.indexOf('(');
            int endIdx = methodSignature.indexOf(')');
            // Extract the method signature excluding the parameter list
            if(startIdx!=-1) {
                String methodHeader = methodSignature.substring(0, startIdx + 1) + methodSignature.substring(endIdx);
                // Extract the parameter list and split by commas
                String parameterList = methodSignature.substring(startIdx + 1, endIdx);
                String[] parameters = parameterList.split(",");
                StringBuilder normalizedParameters = new StringBuilder();
                for (String param : parameters) {
                    // For each parameter, split it by space to get the type and remove the name
                    String[] parts = param.trim().split("\\s+");
                    if (parts.length > 0) {
                        normalizedParameters.append(parts[0]).append(","); // Add only the type
                    }
                }

                // Remove the last comma if there are parameters
                if (normalizedParameters.length() > 0) {
                    normalizedParameters.deleteCharAt(normalizedParameters.length() - 1);
                }
                StringBuilder builder = new StringBuilder();
                builder.append(methodHeader.substring(0, startIdx + 1));
                builder.append(normalizedParameters);
                builder.append("){");
                // Rebuild the method signature with normalized parameters
                return builder.toString();
            }
        }
        return methodSignature;
    }


}