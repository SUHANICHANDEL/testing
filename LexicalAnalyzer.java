import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class LexicalAnalyzer {
    public static class Token {
        public final String type;
        public final String value;
        
        public Token(String type, String value) {
            this.type = type;
            this.value = value;
        }
        
        @Override
        public String toString() {
            return String.format("%-12s: %s", type, value);
        }
    }

    // List of all functions that should be treated as keywords
    private static final String[] FUNCTION_KEYWORDS = {
        "print", "concat", "len", "toUpperCase", "toLowerCase", "replace",
        "charAt", "equals", "equalsIgnoreCase", "contains", "startsWith",
        "endsWith", "split", "toString", "add", "subtract", "multiply",
        "divide", "modulus", "max", "min", "power", "absoluteValue", "round",
        "floor", "ceil", "log", "sqrt", "cbrt", "sin", "cos", "tan", "exists",
        "readFile", "fileWriter", "openFile", "closeFile", "isEmpty", "fileLength",
        "listFiles", "getProperty", "setReadable", "setWritable", "availableProcessors",
        "destroy"
    };

    public static List<Token> analyze(String code) {
        List<Token> tokens = new ArrayList<>();
        
        // Remove comments
        code = code.replaceAll("//.*|/\\*.*?\\*/", "");
        
        // Build keyword pattern
        String keywordPattern = String.join("|", FUNCTION_KEYWORDS);
        
        // Token patterns - now matches function calls specifically
        String regex = "(?<FUNCTION>\\b(?:" + keywordPattern + ")\\b(?=\\s*\\())" +  // Function calls
                     "|(?<KEYWORD>\\b(?:int|String|double|boolean|if|else|while|return)\\b)" +
                     "|(?<LITERAL>\"[^\"]*\"|\\d+\\.\\d+|\\d+|true|false)" +
                     "|(?<IDENTIFIER>[a-zA-Z_]\\w*)" +
                     "|(?<OPERATOR>[+\\-*/%=<>!&|]+)" +
                     "|(?<SEPARATOR>[();{},])" +
                     "|(?<WHITESPACE>\\s+)" +
                     "|(?<UNKNOWN>.)";
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);
        
        while (matcher.find()) {
            if (matcher.group("FUNCTION") != null) {
                tokens.add(new Token("FUNCTION", matcher.group("FUNCTION")));
            } else if (matcher.group("KEYWORD") != null) {
                tokens.add(new Token("KEYWORD", matcher.group("KEYWORD")));
            } else if (matcher.group("LITERAL") != null) {
                tokens.add(new Token("LITERAL", matcher.group("LITERAL")));
            } else if (matcher.group("IDENTIFIER") != null) {
                tokens.add(new Token("IDENTIFIER", matcher.group("IDENTIFIER")));
            } else if (matcher.group("OPERATOR") != null) {
                tokens.add(new Token("OPERATOR", matcher.group("OPERATOR")));
            } else if (matcher.group("SEPARATOR") != null) {
                tokens.add(new Token("SEPARATOR", matcher.group("SEPARATOR")));
            } else if (matcher.group("WHITESPACE") == null) {
                tokens.add(new Token("UNKNOWN", matcher.group()));
            }
        }
        
        return tokens;
    }
}