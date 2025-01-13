package creator;

public class IntegrationTestBuilder {
    public static String text="";
    private static void addNewLine(String str) {
        text=text+str+"\n";
    }

    private static void addTabNewLine(String str) {
        addNewLine("\t"+str);
    }
    public static String buildText(Class<?> clazz) {
        text="";
        addNewLine(CodeSeparator.createPackageStatement(clazz));
        addNewLine("");
        addNewLine(CodeSeparator.getAllImportStatements(clazz)); //written imported methods
        addNewLine("");
        addNewLine("public class "+clazz.getSimpleName()+"Test{");
        addTabNewLine("");
        addTabNewLine("@Autowired");
        addTabNewLine("private "+clazz.getSimpleName()+" service;");
        addTabNewLine("");
        addNewLine(CodeSeparator.createAllMethods(CodeSeparator.getAllMethods(clazz)));
        addNewLine("}");
        return text;
    }
}