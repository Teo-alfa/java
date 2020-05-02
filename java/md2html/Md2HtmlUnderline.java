package md2html;
import java.io.*;
import java.util.*;

public class Md2HtmlUnderline {
    public static Map<String, String> converter = new HashMap<>();    
    static {
        converter.put("*", "em");
        converter.put("**", "strong");
        converter.put("_", "em");
        converter.put("__", "strong");
        converter.put("--", "s");
        converter.put("`", "code");
        converter.put("++", "u");
    }

    public static Map<Character, String> converterForBadSymbols = new HashMap<>();    
    static {
        converterForBadSymbols.put('<', "&lt;");
        converterForBadSymbols.put('>', "&gt;");
        converterForBadSymbols.put('&', "&amp;");
    }
    
    public static Set<Character> singleOnly = new HashSet<>();
    static {
        singleOnly.add('`');
    }

    public static Set<Character> doubleOnly = new HashSet<>();
    static {
        doubleOnly.add('+');
        doubleOnly.add('-');
    }
    
    public static boolean isGoodSymbol (Character c) {
        return c == '+' || c == '`' || c == '*' || c == '_' || c == '-' ? true : false;
    }

    public static boolean isBadSymbol (Character c) {
        return c == '<' || c == '>' || c == '&' ? true : false;
    }

    public static int rangOfHead (String line) {
        int rang = 0;
        while (rang < line.length() && line.charAt(rang) == '#') {
            rang++;
        }
        if (rang < line.length() && rang < 7) {
            return line.charAt(rang) == ' ' ? rang : 0;
        } else {
            return 0;
        }
    }

    public static void makeEnd (StringBuilder tmpLine, int rang) {
        int len = tmpLine.length();
        if (rang > 0) {
            tmpLine.insert(len, "</h" + rang + ">");
        } else {
            tmpLine.insert(len, "</p>");
        }
    }
    
    public static void makeStart (StringBuilder tmpLine, int rang) {
        if (rang > 0) {
            tmpLine.replace(0, rang + 1, "<h" + rang + ">");
        } else {
            tmpLine.replace(0, rang, "<p>");
        }
    }
    
    public static void makeLineHTML (StringBuilder tmpLine, Deque<String> symbolStack) {
        int j = 0;
        while (j < tmpLine.length()) {
            char ch = tmpLine.charAt(j);
            if (isGoodSymbol(ch)) {
                StringBuilder string = new StringBuilder();
                string.append(ch); 
                if (j + 1 < tmpLine.length() && isGoodSymbol(tmpLine.charAt(j + 1)) && ch == tmpLine.charAt(j + 1)) {
                    string.append(tmpLine.charAt(j + 1));
                }
                if (j > 0 && tmpLine.charAt(j - 1) == '\\') {
                    tmpLine.delete(j - 1, j);
                    j += string.length() - 1;
                    continue;
                }
                if (j > 0 
                    && tmpLine.charAt(j - 1) == ' ' 
                    && (( j + string.length() < tmpLine.length() 
                        && tmpLine.charAt(j + string.length()) == ' ') 
                        || j + string.length() == tmpLine.length())) {
                    j += string.length();
                    continue;
                }
                if ((!singleOnly.contains(string.charAt(0)) && string.length() == 2) 
                    || (!doubleOnly.contains(string.charAt(0)) && string.length() == 1)) {
                    
                    if (symbolStack.size() != 0 && symbolStack.peekFirst().equals(string.toString()) ) {
                        symbolStack.removeFirst();
                        tmpLine.replace(j, j + string.length(), "</" + converter.get(string.toString()) + ">");
                        j += 3 + converter.get(string.toString()).length();
                    } else {
                        symbolStack.push(string.toString());
                        tmpLine.replace(j, j + string.length(), "<" + converter.get(string.toString()) + ">");  
                        j += 2 + converter.get(string.toString()).length();
                    }
                    continue;
                }
                j += string.length();
                
            } else if (isBadSymbol(ch)) {
                tmpLine.replace(j, j + 1, converterForBadSymbols.get(ch));
                j++;
            } else {
                j++;
            }
        }
    }
    public static void main(String[] args) {
        if (args.length < 2) {
            return;
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[0])), "UTF-8"));
            try {
                PrintWriter out = new PrintWriter(
                    new BufferedWriter(
                        new OutputStreamWriter(
                            new FileOutputStream(new File(args[1])),
                            "UTF-8"
                        )
                    )
                );
                try {
                    int rang = 0;
                    boolean hasStarted = false;
                    boolean isStartOfHead = true;
                    String line = in.readLine();
                    Deque<String> symbolStack = new ArrayDeque<>();
                    while (line != null) {
                        if (line.length() != 0) {
                            if (hasStarted && isStartOfHead) {
                                out.print('\n');
                            } 
                            if (!hasStarted) { hasStarted = !hasStarted; }
                            StringBuilder tmpLine = new StringBuilder(line);
                            makeLineHTML(tmpLine, symbolStack);
                            if (isStartOfHead) {
                                rang = rangOfHead(line);
                                makeStart(tmpLine, rang);
                                isStartOfHead = false;
                            }
                            line = in.readLine();
                            if (line == null || line.length() == 0) {
                                makeEnd(tmpLine, rang);
                                isStartOfHead = true;
                                symbolStack.clear();
                            }
                            out.print(tmpLine.toString());
                            if (line != null && line.length() > 0) {
                                out.print('\n');
                            }
                        } else {
                            line = in.readLine();
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.err.println("Output file not found" + e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    System.err.println("Wrong output encoding " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("Problems with output " + e.getMessage()); 
                } finally {
                    out.close();
                    if (out.checkError()) {
                        System.err.println("Print stream has encountered an error");
                    } 
                } 
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Wrong input encoding " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Problems with input " + e.getMessage()); 
        }
    }
}