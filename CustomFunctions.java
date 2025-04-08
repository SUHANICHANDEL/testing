import java.io.*;
import java.lang.Math;
import java.awt.Desktop;


public class CustomFunctions {

    // Print function
    public static void print(Object message) {
        System.out.println(message);
    }
   
    
    // String operations
    public static String concat(String s1, String s2) { return s1 + s2; }
    public static int len(String s) { return s.length(); }
    public static String toUpperCase(String s) { return s.toUpperCase(); }
    public static String toLowerCase(String s) { return s.toLowerCase(); }
    public static String replace(String s, String oldChar, String newChar) { return s.replace(oldChar, newChar); }
    public static char charAt(String s, int index) { return s.charAt(index); }
    public static boolean equals(String s1, String s2) { return s1.equals(s2); }
    public static boolean equalsIgnoreCase(String s1, String s2) { return s1.equalsIgnoreCase(s2); }
    public static boolean contains(String s, String substring) { return s.contains(substring); }
    public static boolean startsWith(String s, String prefix) { return s.startsWith(prefix); }
    public static boolean endsWith(String s, String suffix) { return s.endsWith(suffix); }
    public static String[] split(String s, String regex) { return s.split(regex); }
    public static String toString(Object obj) { return String.valueOf(obj); }
    public static String reverseString(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    // Math operations
    public static int add(int a, int b) { return a + b; }
    public static int subtract(int a, int b) { return a - b; }
    public static int multiply(int a, int b) { return a * b; }
    
    public static double divide(int a, int b) {
        return (b == 0) ? (a == 0 ? Double.NaN : Double.POSITIVE_INFINITY) : (double)a / b;
    }
    public static int modulus(int a, int b) { return a % b; }
    public static int max(int a, int b) { return Math.max(a, b); }
    public static int min(int a, int b) { return Math.min(a, b); }
    public static double power(double base, double exp) { return Math.pow(base, exp); }
    public static double absoluteValue(double num) { return Math.abs(num); }
    public static double round(double num) { return Math.round(num); }
    public static double floor(double num) { return Math.floor(num); }
    public static double ceil(double num) { return Math.ceil(num); }
    public static double log(double num) { return Math.log(num); }
    public static double sqrt(double num) { return Math.sqrt(num); }
    public static double cbrt(double num) { return Math.cbrt(num); }
    public static double sin(double num) { return Math.sin(num); }
    public static double cos(double num) { return Math.cos(num); }
    public static double tan(double num) { return Math.tan(num); }

    // File operations
    public static boolean exists(String filePath) { return new File(filePath).exists(); }
    public static String readFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) content.append(line).append("\n");
        reader.close();
        return content.toString();
    }
    public static void fileWriter(String filePath, String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(data);
        writer.close();
    }
    public static void openFile(String filePath) throws IOException {
        Desktop.getDesktop().open(new File(filePath));
    }
    public static void closeFile() { System.gc(); }  // Simulated close
    public static boolean isEmpty(String filePath) throws IOException {
        return new File(filePath).length() == 0;
    }
    public static long fileLength(String filePath) { return new File(filePath).length(); }
    public static String[] listFiles(String dirPath) { return new File(dirPath).list(); }

    // System properties & file permissions
    public static String getProperty(String key) { return System.getProperty(key); }
    public static void setReadable(String filePath, boolean readable) { new File(filePath).setReadable(readable); }
    public static void setWritable(String filePath, boolean writable) { new File(filePath).setWritable(writable); }
    public static int availableProcessors() { return Runtime.getRuntime().availableProcessors(); }

    // Miscellaneous
    public static void destroy() { System.exit(0); }
}
