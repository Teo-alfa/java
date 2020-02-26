package md2html;
import java.io.*;
import java.util.*;

public class UltraScanner {
    public BufferedReader input;
    private int mark = 0;
    private String tmpThing;
    private String tmpWord = "";
    private int tmpNumber = 0;
    private String tmpLine = "";
    private boolean wasUsedHasNext = false;
    private boolean wasUsedHasNextWord = false;
    private boolean wasUsedHasNextInt = false;
    private boolean hasStartedReading = false;
    private boolean end = false;
    private int endWord1 = 0; 
    private boolean endLine = false;
    private int endChar = -1;

    public UltraScanner(BufferedReader input) throws FileNotFoundException {
        this.input = input;
    }
    
    public UltraScanner(InputStream input) throws FileNotFoundException, UnsupportedEncodingException {
        this.input = new BufferedReader(new InputStreamReader(input, "UTF-8")); // cпросить про utf-16
    }
    
    public UltraScanner(String tmpLine) {
        this.input = new BufferedReader(new StringReader(tmpLine));
    } 
    
    public UltraScanner(File file) throws FileNotFoundException, UnsupportedEncodingException{
        this.input = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), "UTF-8")); // cпросить про utf-16
    }
        
    private static boolean charCheker(int index, String tmpLine) {
        return Character.isLetter(tmpLine.charAt(index)) 
                    || (Character.getType(tmpLine.charAt(index)) == Character.DASH_PUNCTUATION) 
                    || (tmpLine.charAt(index) == '\'') ; 
    }

    public String nextLine() throws IOException {
        StringBuilder line = new StringBuilder();
        if (endChar!= -1) {
            line.append((char)endChar);
        }
        int charCode = input.read();
        if (charCode == 10 || charCode == -1) {
            return "";
        } else {
            while (!(charCode == 10 || charCode == -1)) {
                line.append((char)(charCode));
                charCode = input.read();
            }
            // уточение для этой задачки 
            if (charCode == 10 || charCode == -1) {}
            while (charCode == 10 && charCode!= -1) {
                input.mark(1);
                line.append((char)(charCode));
                charCode = input.read();
            }
            if (charCode != -1) {
                endChar = charCode;
            }
            // закончилось уточнение
            return line.substring(0);
        }
    }
    
    public boolean hasNextLine() {
        try {
            input.mark(1);    
            if (input.read() != -1) {
                input.reset();
                return true;
            } else {
                input.reset();
                return false;
            }
        } catch (IOException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String next() throws IOException {
        if (!wasUsedHasNext) {
            StringBuilder thing = new StringBuilder();
            int thisChar = input.read();
            mark++;
            if (thisChar == -1) {
                end = true;
            } else if (thisChar == 10) {
                endLine = true;
            }
            if (end == true || endLine == true) {
                return ""; 
            }
            while (Character.isWhitespace(thisChar)) {
                thisChar = input.read();
                //System.out.print((char)thisChar + " ");
                if (thisChar == -1) {
                    end = true;
                    break;
                } else if (thisChar == 10) {
                    endLine = true;
                    break;
                }
            }
            //System.out.print("find ");
            if (end == true || endLine == true) {
                return ""; 
            }
            while(!Character.isWhitespace(thisChar)) {
                
                //System.out.print((char)thisChar + " ");
                thing.append((char)thisChar);
                thisChar = input.read();
                if (thisChar == -1) {
                    end = true;
                    break;
                } else if (thisChar == 10) {
                    endLine = true;
                    break;
                }
            }
            //System.out.println("<" + thing.toString() + ">");
            return thing.toString();
        } else {
            wasUsedHasNext = false;
            return tmpThing;
        }
    } 

    public boolean hasNext() {
        try {
            //System.out.println("we are here!");
            String thing = next();
            if (thing.length() == 0) {
                return false;
            }
            //System.out.println("flag1");
            tmpThing = thing;
            wasUsedHasNext = true;
            return true;
        } catch (IOException e) {
            return false;
        }  
    }

    public int nextInt() throws IOException, NumberFormatException {
        if (!wasUsedHasNextInt) {    
            if (hasNext()) {
                return Integer.parseInt(next());
            } else {
                throw new IOException("NaN");
            }
        } else {
            wasUsedHasNextInt = false;
            return tmpNumber;
        }
    }

    public boolean hasNextInt() {
        try {
            int number = nextInt();
            wasUsedHasNextInt = true;
            tmpNumber = number;
            return true;
        } catch (IOException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }

    }

  
    public String nextWord() throws IOException {
        if (wasUsedHasNextWord) {
            wasUsedHasNextWord = false;
            return tmpWord;
        }
        
        int start = 0;
        while (endWord1 < tmpLine.length()) {             
            while (endWord1 < tmpLine.length() && !charCheker(endWord1, tmpLine)) {
                endWord1++;
            }
            start = endWord1;
            while (endWord1 < tmpLine.length() && charCheker(endWord1, tmpLine)) {
                endWord1++;
            }
            if (start != endWord1) {
                break;
            }
        }

        int end2 = endWord1;
        if (endWord1 == tmpLine.length()) {
            endWord1 = 0;
            endLine = true; 
        } 
        if (start != end2) {
            return tmpLine.substring(start, end2).toLowerCase();
        } else {
            throw new IOException("No words");
        }
    }

    public boolean hasNextWord() {
        if (endLine != true) {
            try {
                if (hasStartedReading == false) {
                    hasStartedReading = true;
                    tmpLine = nextLine();
                }
                tmpWord = nextWord();
                wasUsedHasNextWord = true;
                return true;
            } catch (IOException e) {
                hasStartedReading = false;
                return false;
            }
        } else {
            hasStartedReading = false;
            endLine = false;
            return false;
        }
    }
    public void close() throws IOException {
        input.close();
    }
    public static void main(String[] args) {
    }
}