package ticTacToe;

import java.util.*;

public class Settings {
    private int m, n, k;
    public Settings() {
        int m = 0, n = 0, k = 1;
        Scanner setScanner = new Scanner(System.in);
        while (!isValidSet(m, n, k)) {
            System.out.println("Please, set values of m, n and k.");
            m = readInt(setScanner);
            n = readInt(setScanner);
            k = readInt(setScanner);
            if (!isValidSet(m, n, k)) {
                System.out.println("Invalid settings: m = " + m + ", n = " + n + ", k = " + k);
                System.out.println("Try again.");
            }
        }
        this.m = m;
        this.n = n;
        this.k = k;
    }
    
    public Settings(int m, int n, int k) {
        if (isValidSet(m, n, k)) {
            this.m = m;
            this.n = n;
            this.k = k;
        } else {
            System.out.println("Invalid settings: m = " + m + ", n = " + n + ", k = " + k);
            System.out.println("Default settings (3, 3, 3) selected.");
            this.m = this.n = this.k = 3;
        }   
    }

    private static boolean isValidSet(int m, int n, int k) {
        if (m < 2 || n < 2) {
            return false;
        }
        int max = n > m ? n : m;
        if (k > max || k < 2) {
            return false;
        }
        return true;
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }
    
    public int getK() {
        return k;
    }

    public int readInt(Scanner in) {
        while (true) {
            if (in.hasNextInt()) {
                return in.nextInt();
            } else {
                System.out.println("Fix this");
                in.next();
            }
        }  
    }

    public TicTacToeBoard createBoard() {
        return new TicTacToeBoard(this);
    }
}