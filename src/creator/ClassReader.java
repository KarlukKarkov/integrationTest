package creator;

import java.lang.reflect.Method;

public class ClassReader {

    public String readClass(){
        return "package creator;\n" +
                "\n" +
                "import java.lang.reflect.Method;\n" +
                "import java.lang.reflect.Modifier;\n" +
                "import java.lang.reflect.Parameter;\n" +
                "import java.util.Scanner;\n" +
                "\n" +
                "public class AIManipuation {\n" +
                "\n" +
                "    private static String getMethodSignature(Method method) {\n" +
                "        StringBuilder signature = new StringBuilder();\n" +
                "\n" +
                "        // Add modifiers (e.g., public, static)\n" +
                "        int modifiers = method.getModifiers();\n" +
                "        if (modifiers != 0) {\n" +
                "            signature.append(Modifier.toString(modifiers)).append(\" \");\n" +
                "        }\n" +
                "\n" +
                "        // Add return type\n" +
                "        Class<?> returnType = method.getReturnType();\n" +
                "        signature.append(returnType.getSimpleName()).append(\" \");\n" +
                "\n" +
                "        // Add method name\n" +
                "        signature.append(method.getName()).append(\"(\");\n" +
                "\n" +
                "        // Add parameters\n" +
                "        Parameter[] parameters = method.getParameters();\n" +
                "        for (int i = 0; i < parameters.length; i++) {\n" +
                "            if (i > 0) {\n" +
                "                signature.append(\", \");\n" +
                "            }\n" +
                "            signature.append(parameters[i].getType().getSimpleName()).append(\" \").append(parameters[i].getName());\n" +
                "        }\n" +
                "\n" +
                "        // Add closing parenthesis and opening brace\n" +
                "        signature.append(\"){\");\n" +
                "        return signature.toString();\n" +
                "    } //exactly same except param names changed with args0,args1 ext.\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        ClassReader reader= new ClassReader();\n" +
                "        getMethodBody(reader.getDeclaredMethod(),reader.readClass());\n" +
                "\n" +
                "    }\n" +
                "    public static String getMethodBody(Method method,String clazz){\n" +
                "        int[] indexes=findMethodStartFinishLine(method,clazz);\n" +
                "        System.out.println(indexes[0]+\" \"+indexes[1]);\n" +
                "        StringBuilder builder= new StringBuilder();\n" +
                "        Scanner scanner= new Scanner(clazz);\n" +
                "        int counter=1;\n" +
                "        while (scanner.hasNextLine()){\n" +
                "            String currentLine=scanner.nextLine();\n" +
                "            if((counter>=indexes[0])&&(counter<=indexes[1])){\n" +
                "                builder.append(currentLine).append(\"\\n\");\n" +
                "            }\n" +
                "            counter++;\n" +
                "        }\n" +
                "        return builder.toString();\n" +
                "    }\n" +
                "    private static int[] findMethodStartFinishLine(Method method, String clazz){\n" +
                "        int[] indexes= {-1,-1};\n" +
                "        String signature= getMethodSignature(method);\n" +
                "        System.out.println(signature);\n" +
                "        indexes[0]= findMethodStartLine(clazz,signature);\n" +
                "        indexes[1]=findMethodFinishLine(clazz,indexes[0]);\n" +
                "        return indexes;\n" +
                "    }\n" +
                "    private static int findMethodFinishLine(String clazz,int startLine){\n" +
                "        String[] lines = clazz.split(\"\\n\");\n" +
                "        for (int i = 0; i < lines.length; i++) {\n" +
                "            if (compareMethodSignatures(lines[i],\"\\s\\s\\s\\s}\")) {\n" +
                "                if(i>startLine)\n" +
                "                    return i + 1; // Line numbers start from 1\n" +
                "            }\n" +
                "        }\n" +
                "        return -1; // Return -1 if the method signature is not found\n" +
                "    }\n" +
                "    private static int findMethodStartLine(String clazz, String line) {\n" +
                "        String[] lines = clazz.split(\"\\n\");\n" +
                "        for (int i = 0; i < lines.length; i++) {\n" +
                "            String trimmedLine = lines[i].trim();\n" +
                "            //System.out.println(i+\" \"+trimmedLine);\n" +
                "            if (compareMethodSignatures(line.trim(),trimmedLine)) {\n" +
                "                return i + 1; // Line numbers start from 1\n" +
                "            }\n" +
                "        }\n" +
                "        return -1; // Return -1 if the method signature is not found\n" +
                "    }\n" +
                "    private static boolean compareMethodSignatures(String generatedSignature, String javaFileLine)  { //works correctly\n" +
                "        // Normalize parameter names in both signatures\n" +
                "        String normalizedGeneratedSignature = normalizeParameterNames(generatedSignature);\n" +
                "        String normalizedJavaFileLine = normalizeParameterNames(javaFileLine);\n" +
                "\n" +
                "        if(normalizedJavaFileLine.startsWith(\"public\")){\n" +
                "            System.out.println(normalizedGeneratedSignature);\n" +
                "\n" +
                "        }\n" +
                "        // Compare normalized signatures\n" +
                "        return normalizedGeneratedSignature.equals(normalizedJavaFileLine);\n" +
                "    }\n" +
                "\n" +
                "    private static String normalizeParameterNames(String methodSignature) {\n" +
                "        if (methodSignature.matches(\"^(public|private|static)\\\\s+.*\")) {\n" +
                "            // Find the position of the opening and closing parentheses for the parameter list\n" +
                "            int startIdx = methodSignature.indexOf('(');\n" +
                "            int endIdx = methodSignature.indexOf(')');\n" +
                "            // Extract the method signature excluding the parameter list\n" +
                "            if(startIdx!=-1) {\n" +
                "                String methodHeader = methodSignature.substring(0, startIdx + 1) + methodSignature.substring(endIdx);\n" +
                "                // Extract the parameter list and split by commas\n" +
                "                String parameterList = methodSignature.substring(startIdx + 1, endIdx);\n" +
                "                String[] parameters = parameterList.split(\",\");\n" +
                "                StringBuilder normalizedParameters = new StringBuilder();\n" +
                "                for (String param : parameters) {\n" +
                "                    // For each parameter, split it by space to get the type and remove the name\n" +
                "                    String[] parts = param.trim().split(\"\\\\s+\");\n" +
                "                    if (parts.length > 0) {\n" +
                "                        normalizedParameters.append(parts[0]).append(\",\"); // Add only the type\n" +
                "                    }\n" +
                "                }\n" +
                "\n" +
                "                // Remove the last comma if there are parameters\n" +
                "                if (normalizedParameters.length() > 0) {\n" +
                "                    normalizedParameters.deleteCharAt(normalizedParameters.length() - 1);\n" +
                "                }\n" +
                "                StringBuilder builder = new StringBuilder();\n" +
                "                builder.append(methodHeader.substring(0, startIdx + 1));\n" +
                "                builder.append(normalizedParameters);\n" +
                "                builder.append(\"){\");\n" +
                "                // Rebuild the method signature with normalized parameters\n" +
                "                return builder.toString();\n" +
                "            }\n" +
                "        }\n" +
                "        return methodSignature;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "}";
    }

    public Method getDeclaredMethod(){
        return AIManipuation.class.getDeclaredMethods()[0];
    }
}