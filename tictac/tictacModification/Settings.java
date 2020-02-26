package tictac.tictacModification;

import java.util.*;

public class Settings {
    private int m, n, k, numberOfPlayers;

    public Settings() {
        int m = 0, n = 0, k = 1, numberOfPlayers = 1;
        Scanner setScanner = new Scanner(System.in);
        while (!isValidSet(m, n, k, numberOfPlayers)) {
            System.out.println("Please, set values of m, n, k and number of players.");
            this.m = readInt(setScanner);
            this.n = readInt(setScanner);
            this.k = readInt(setScanner);
            numberOfPlayers = readInt(setScanner);
            if (!isValidSet(m, n, k, numberOfPlayers)) {
                System.out.println("Invalid settings: m = " + m + ", n = " + n + ", k = " + k + ", number of players = " + numberOfPlayers);
                System.out.println("Try again.");
            }
        }
        this.numberOfPlayers = numberOfPlayers;
    }
    
    public Settings(int m, int n, int k, int numberOfPlayers) {
        if (isValidSet(m, n, k, numberOfPlayers)) {
            this.m = m;
            this.n = n;
            this.k = k;
            this.numberOfPlayers = numberOfPlayers;
        } else {
            System.out.println("Invalid settings: m = " + m + ", n = " + n + ", k = " + k + ", number of players = " + numberOfPlayers);
            System.out.println("Default settings (3, 3, 3, 2) selected.");
            this.m = this.n = this.k = 3;
            this.numberOfPlayers = 2;
        }   
    }

    private static boolean isValidSet(int m, int n, int k, int numberOfPlayers) {
        if (numberOfPlayers < 2 || numberOfPlayers > 4) {
            return false;
        }
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

    public int getNumberOfPlayers() {
        return numberOfPlayers;
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