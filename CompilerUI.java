import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.regex.*;


public class CompilerUI extends JFrame {
    private JTextArea codeEditor, outputArea;
    private JButton runButton, helpButton, themeToggleButton, lexButton;
    private JavaSubsetCompiler compiler = new JavaSubsetCompiler();
    private boolean darkTheme = true;

    public CompilerUI() {
        setTitle("Java Subset Compiler");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeComponents();
        applyTheme();
        setVisible(true);
    }

    private void initializeComponents() {
        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Java Subset Compiler", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.CENTER);
        
        // Button panel in top right
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // Lexical analysis button
        lexButton = new JButton("Lexical");
        lexButton.setToolTipText("Perform lexical analysis");
        lexButton.addActionListener(e -> performLexicalAnalysis());
        buttonPanel.add(lexButton);
        
        // Theme toggle button
        themeToggleButton = new JButton("☀");
        themeToggleButton.setToolTipText("Toggle dark/light theme");
        themeToggleButton.addActionListener(e -> toggleTheme());
        buttonPanel.add(themeToggleButton);
        
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Main Panel (Code Editor + Output)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        codeEditor = new JTextArea();
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        mainPanel.add(createLabeledPanel("CODE", new JScrollPane(codeEditor)));
        mainPanel.add(createLabeledPanel("OUTPUT", new JScrollPane(outputArea)));
        add(mainPanel, BorderLayout.CENTER);

        // Bottom Panel (Run and Help Buttons)
        runButton = new JButton("Run");
        runButton.setFont(new Font("Arial", Font.BOLD, 16));
        runButton.addActionListener(this::executeCode);

        helpButton = new JButton("Help");
        helpButton.setFont(new Font("Arial", Font.BOLD, 16));
        helpButton.addActionListener(e -> openHelpManual());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(runButton);
        bottomPanel.add(helpButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void performLexicalAnalysis() {
        String code = codeEditor.getText();
        List<LexicalAnalyzer.Token> tokens = LexicalAnalyzer.analyze(code);
        
        StringBuilder result = new StringBuilder("=== LEXICAL ANALYSIS ===\n");
        result.append(String.format("%-12s: %s\n", "TOKEN TYPE", "VALUE"));
        result.append("----------------------------\n");
        
        for (LexicalAnalyzer.Token token : tokens) {
            result.append(token).append("\n");
        }
        
        outputArea.setText(result.toString());
    }

    private void toggleTheme() {
        darkTheme = !darkTheme;
        applyTheme();
    }

    private void applyTheme() {
        if (darkTheme) {
            // Dark theme colors
            Color darkBg = new Color(45, 45, 48);
            Color darkText = new Color(241, 241, 241);
            Color darkPanel = new Color(63, 63, 70);
            Color accent = new Color(0, 122, 204);

            getContentPane().setBackground(darkBg);
            codeEditor.setBackground(darkPanel);
            codeEditor.setForeground(darkText);
            codeEditor.setCaretColor(Color.WHITE);
            outputArea.setBackground(darkPanel);
            outputArea.setForeground(darkText);

            runButton.setBackground(accent);
            runButton.setForeground(Color.WHITE);
            helpButton.setBackground(new Color(70, 70, 70));
            helpButton.setForeground(Color.WHITE);
            lexButton.setBackground(new Color(70, 70, 70));
            lexButton.setForeground(Color.WHITE);
            
            themeToggleButton.setText("☀");
            themeToggleButton.setBackground(new Color(70, 70, 70));
            themeToggleButton.setForeground(Color.WHITE);
        } else {
            // Light theme colors
            Color lightBg = new Color(240, 240, 240);
            Color lightText = new Color(50, 50, 50);
            Color lightPanel = Color.WHITE;
            Color accent = new Color(0, 150, 136);

            getContentPane().setBackground(lightBg);
            codeEditor.setBackground(lightPanel);
            codeEditor.setForeground(lightText);
            codeEditor.setCaretColor(Color.BLACK);
            outputArea.setBackground(lightPanel);
            outputArea.setForeground(lightText);

            runButton.setBackground(accent);
            runButton.setForeground(Color.WHITE);
            helpButton.setBackground(new Color(200, 200, 200));
            helpButton.setForeground(lightText);
            lexButton.setBackground(new Color(200, 200, 200));
            lexButton.setForeground(lightText);
            
            themeToggleButton.setText("🌙");
            themeToggleButton.setBackground(new Color(200, 200, 200));
            themeToggleButton.setForeground(lightText);
        }

        // Update UI
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void openHelpManual() {
        try {
            String manualUrl = "file:///C:/Users/DELL-PC/Downloads/Documentation%20part%20Done.pdf";
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(manualUrl));
            } else {
                JOptionPane.showMessageDialog(this,
                    "Cannot open browser automatically. Please visit:\n" + manualUrl,
                    "Help Manual",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException | URISyntaxException ex) {
            JOptionPane.showMessageDialog(this,
                "Error opening help manual: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createLabeledPanel(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(label, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(darkTheme ? new Color(241, 241, 241) : new Color(50, 50, 50));
        panel.setBackground(darkTheme ? new Color(45, 45, 48) : new Color(240, 240, 240));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void executeCode(ActionEvent e) {
        String userCode = codeEditor.getText().trim();
        try {
            String result = compiler.analyzeAndExecute(userCode);
            outputArea.setText(result);
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CompilerUI());
    }
}
class JavaSubsetCompiler {
    private Map<String, Integer> intVariables = new HashMap<>();
    private Map<String, String> stringVariables = new HashMap<>();
    private Map<String, Double> doubleVariables = new HashMap<>();
    private Map<String, Boolean> booleanVariables = new HashMap<>();

    public String analyzeAndExecute(String code) {
        code = code.replaceAll("/\\.?\\*/", "");
        String[] lines = code.split("\\n");
        StringBuilder output = new StringBuilder();

        for (String line : lines) {
            line = line.replaceAll("//.*", "").trim();
            if (line.isEmpty()) continue;

            try {
                if (line.matches("int\\s+\\w+\\s*=\\s*\\w+\\(.\\)\\s;")) {
                    handleFunctionAssignment(line, output, "int");
                }
                else if (line.matches("double\\s+\\w+\\s*=\\s*\\w+\\(.\\)\\s;")) {
                    handleFunctionAssignment(line, output, "double");
                }
                else if (line.matches("boolean\\s+\\w+\\s*=\\s*(true|false)\\s*;")) {
                    processBooleanDeclaration(line);
                }
                else if (line.matches("(int|double|String)\\s+\\w+\\s*=\\s*.+;")) {
                    processVariableDeclaration(line);
                }
                else if (line.matches("(System\\.out\\.println|print)\\(.*\\);")) {
                    output.append(processPrintStatement(line)).append("\n");
                }
                else if (line.matches("\\w+\\(.*\\);")) {
                    String result = executeFunctionCall(line);
                    if (!result.isEmpty()) {
                        output.append(result).append("\n");
                    }
                }
                else if (line.matches("\\w+\\s*=\\s*.+;")) {
                    processAssignment(line);
                }
                else {
                    output.append("Syntax Error: Unsupported statement → ").append(line).append("\n");
                }
            } catch (RuntimeException e) {
                output.append(e.getMessage()).append("\n");
            }
        }
        return output.toString().trim();
    }
     
    private void processBooleanDeclaration(String line) {
        Pattern pattern = Pattern.compile("boolean\\s+(\\w+)\\s*=\\s*(true|false)\\s*;");
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            String varName = matcher.group(1);
            boolean value = Boolean.parseBoolean(matcher.group(2));
            booleanVariables.put(varName, value);
        }
    }
    private String evaluatePrintExpression(String expr) {
        // Handle string concatenation with function calls
        if (expr.contains("+")) {
            String[] parts = expr.split("\\+");
            StringBuilder result = new StringBuilder();
            for (String part : parts) {
                result.append(processPrintStatement("print(" + part.trim() + ");"));
            }
            return result.toString();
        }
        return processPrintStatement("print(" + expr + ");");
    }
    private void handleFunctionAssignment(String line, StringBuilder output, String type) {
        Pattern p = Pattern.compile("(" + type + ")\\s+(\\w+)\\s*=\\s*(\\w+\\(.\\))\\s;");
        Matcher m = p.matcher(line);
        if (m.matches()) {
            String varName = m.group(2);
            String funcCall = m.group(3);
            try {
                Object result = executeFunctionAndGetValue(funcCall + ";");
                if (type.equals("int")) {
                    if (result instanceof Number) {
                        intVariables.put(varName, ((Number)result).intValue());
                    } else {
                        output.append("Error: Function must return a number for int assignment\n");
                    }
                } 
                else if (type.equals("double")) {
                    if (result instanceof Number) {
                        doubleVariables.put(varName, ((Number)result).doubleValue());
                    } else {
                        output.append("Error: Function must return a number for double assignment\n");
                    }
                }
            } catch (Exception e) {
                output.append("Error: ").append(e.getMessage()).append("\n");
            }
        }
    }

    private Object executeFunctionAndGetValue(String line) throws Exception {
        Pattern pattern = Pattern.compile("(\\w+)\\(([^)]*)\\);");
        Matcher matcher = pattern.matcher(line.replaceAll("\\s+", ""));
        
        if (matcher.matches()) {
            String functionName = matcher.group(1);
            String argsString = matcher.group(2);
            String[] rawArgs = argsString.isEmpty() ? new String[0] : argsString.split(",");
            
            Object[] evaluatedArgs = new Object[rawArgs.length];
            Class<?>[] paramTypes = new Class[rawArgs.length];
            
            for (int i = 0; i < rawArgs.length; i++) {
                String arg = rawArgs[i].trim();
                
                if (intVariables.containsKey(arg)) {
                    evaluatedArgs[i] = intVariables.get(arg);
                    paramTypes[i] = int.class;
                }
                else if (doubleVariables.containsKey(arg)) {
                    evaluatedArgs[i] = doubleVariables.get(arg);
                    paramTypes[i] = double.class;
                }
                else if (arg.matches("-?\\d+")) {
                    evaluatedArgs[i] = Integer.parseInt(arg);
                    paramTypes[i] = int.class;
                }
                else if (arg.matches("-?\\d+\\.\\d+")) {
                    evaluatedArgs[i] = Double.parseDouble(arg);
                    paramTypes[i] = double.class;
                }
                else if (arg.startsWith("\"") && arg.endsWith("\"")) {
                    evaluatedArgs[i] = arg.substring(1, arg.length() - 1);
                    paramTypes[i] = String.class;
                }
                else if (stringVariables.containsKey(arg)) {
                    evaluatedArgs[i] = stringVariables.get(arg);
                    paramTypes[i] = String.class;
                }
                else {
                    throw new RuntimeException("Invalid argument: " + arg);
                }
            }
            
            // Try exact parameter types first
            try {
                Method method = CustomFunctions.class.getMethod(functionName, paramTypes);
                return method.invoke(null, evaluatedArgs);
            } catch (NoSuchMethodException e) {
                // Try converting int parameters to double
                Class<?>[] altParamTypes = new Class[paramTypes.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    if (paramTypes[i] == int.class) {
                        altParamTypes[i] = double.class;
                        evaluatedArgs[i] = ((Integer)evaluatedArgs[i]).doubleValue();
                    } else {
                        altParamTypes[i] = paramTypes[i];
                    }
                }
                
                try {
                    Method method = CustomFunctions.class.getMethod(functionName, altParamTypes);
                    return method.invoke(null, evaluatedArgs);
                } catch (NoSuchMethodException ex) {
                    throw new NoSuchMethodException("Function '" + functionName + 
                        "' with these parameter types not found");
                }
            }
        }
        throw new RuntimeException("Invalid function call: " + line);
    }

    private void processVariableDeclaration(String line) {
        Pattern pattern = Pattern.compile("(int|double|String)\\s+(\\w+)\\s*=\\s*(.+);");
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            String type = matcher.group(1);
            String varName = matcher.group(2);
            String value = matcher.group(3).trim();

            if (type.equals("int")) {
                if (value.matches("-?\\d+")) {
                    intVariables.put(varName, Integer.parseInt(value));
                } else {
                    throw new RuntimeException("Invalid integer value: " + value);
                }
            } 
            else if (type.equals("double")) {
                if (value.matches("-?\\d+(\\.\\d+)?")) {
                    doubleVariables.put(varName, Double.parseDouble(value));
                } else {
                    throw new RuntimeException("Invalid double value: " + value);
                }
            }
            else if (type.equals("String")) {
                if (value.matches("\".*\"")) {
                    stringVariables.put(varName, value.substring(1, value.length() - 1));
                } else {
                    throw new RuntimeException("Invalid string value: " + value);
                }
            }
        }
    }

    private String processPrintStatement(String line) {
        Pattern pattern = Pattern.compile("(System\\.out\\.println|print)\\((.+)\\);");
        Matcher matcher = pattern.matcher(line);
        
        if (matcher.matches()) {
            String value = matcher.group(2).trim();
        
            // Handle string literals
            if (value.startsWith("\"") && value.endsWith("\"")) {
                return value.substring(1, value.length() - 1);
            }
            
            // Handle expressions with concatenation
            if (value.contains("+")) {
                return evaluatePrintExpression(value);
            }
            
            // Handle function calls
            if (value.matches("\\w+\\(.*\\)")) {
                try {
                    Object result = executeFunctionAndGetValue(value + ";");
                    if (result instanceof Boolean) {
                        return result.toString();
                    } else if (result instanceof Number) {
                        return String.valueOf(result);
                    }
                    return String.valueOf(result);
                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            }
            
            // Handle variables
            if (booleanVariables.containsKey(value)) {
                return String.valueOf(booleanVariables.get(value));
            }
            if (intVariables.containsKey(value)) {
                return String.valueOf(intVariables.get(value));
            }
            if (doubleVariables.containsKey(value)) {
                return String.valueOf(doubleVariables.get(value));
            }
            if (stringVariables.containsKey(value)) {
                return stringVariables.get(value);
            }
            
            // Handle direct values
            if (value.equals("true") || value.equals("false")) {
                return value;
            }
            
            try {
                return String.valueOf(evaluateExpression(value));
            } catch (Exception e) {
                return "Error: Cannot evaluate " + value;
            }
        }
        return "Error: Invalid print statement";
    }
    
    
    private String executeFunctionCall(String line) {
        try {
            Object result = executeFunctionAndGetValue(line);
            return result != null ? result.toString() : "";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private void processAssignment(String line) {
        Pattern pattern = Pattern.compile("(\\w+)\\s*=\\s*(.+);");
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            String varName = matcher.group(1);
            String value = matcher.group(2).trim();

            if (intVariables.containsKey(varName)) {
                intVariables.put(varName, evaluateExpression(value));
            } 
            else if (doubleVariables.containsKey(varName)) {
                try {
                    doubleVariables.put(varName, Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid double value: " + value);
                }
            }
            else if (stringVariables.containsKey(varName)) {
                if (value.matches("\".*\"")) {
                    stringVariables.put(varName, value.substring(1, value.length() - 1));
                } else {
                    throw new RuntimeException("Invalid string assignment");
                }
            }
            else {
                throw new RuntimeException("Error: Undefined variable → " + varName);
            }
        }
    }

    private int evaluateExpression(String expression) {
        try {
            expression = expression.replaceAll("\\s+", "");

            for (String var : intVariables.keySet()) {
                expression = expression.replace(var, intVariables.get(var).toString());
            }

            return eval(expression);
        } catch (Exception e) {
            throw new RuntimeException("Error: Invalid arithmetic expression → " + expression);
        }
    }

    private int eval(String expr) {
        return new Object() {
            int pos = -1, ch;
    
            void nextChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }
    
            int parse() {
                nextChar();
                return parseExpression();
            }
    
            int parseExpression() {
                int x = parseTerm();
                while (ch == '+' || ch == '-') {
                    int op = ch;
                    nextChar();
                    x = (op == '+') ? x + parseTerm() : x - parseTerm();
                }
                return x;
            }
    
            int parseTerm() {
                int x = parseFactor();
                while (ch == '*' || ch == '/') {
                    int op = ch;
                    nextChar();
                    x = (op == '*') ? x * parseFactor() : x / parseFactor();
                }
                return x;
            }
    
            int parseFactor() {
                int x;
                if (ch == '(') {
                    nextChar();
                    x = parseExpression();
                    if (ch == ')') nextChar();
                } else {
                    x = parseNumber();
                }
                return x;
            }
    
            int parseNumber() {
                int x = 0;
                boolean negative = false;
    
                if (ch == '-') {
                    negative = true;
                    nextChar();
                }
    
                if (Character.isDigit(ch)) {
                    while (Character.isDigit(ch)) {
                        x = x * 10 + (ch - '0');
                        nextChar();
                    }
                }
                return negative ? -x : x;
            }
        }.parse();
    }
}