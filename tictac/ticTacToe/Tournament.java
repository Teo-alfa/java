package ticTacToe;

import java.util.List;
import java.util.Random;

public class Tournament {
    private final int circles;
    private final List<Player> list;
    private final boolean log;
    

    public Tournament (final boolean log, final int circles, final List<Player> list) { 
        this.list = list;
        this.circles = circles;
        this.log = log;
    }

    public int[][] play(final Settings settings, final Game[][] games) { 
        int[][] results;
        int n = list.size();
        results = new int[circles][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < circles; j++) {
                results[j][i] = 0;
            }
        }
        int k = 0;
        while (k < circles) {
            if (k != 0) {
                for (int i = 0; i < n; i++) {
                    results[k][i] = results[k - 1][i];
                }
            }
            int[][] circleResults =  new int[n][n];
            for (int i = 0; i < n; i++) {
                circleResults[i][i] = -1;
            }
            Random random = new Random();
            for (int i = 1; i <= n - 1; i++) {
                for (int j = 0; j < n - i; j++) {
                    TicTacToeBoard tmpBoard = settings.createBoard();
                    //System.out.println("lot= " + lots[j][i + j]);
                    int lot = random.nextInt(2);
                    if (games[j][j + i].hasHumans()) {
                        int player = lot == 0 ? j + 1 : j + i + 1;
                        System.out.println("Player " + player + " starts." + '\n');
                    }
                    int result = lot == 0 ? games[j][j + i].play(tmpBoard) : games[j + i][j].play(tmpBoard);
                    if (result == 0) {
                        results[k][j]++;
                        results[k][j + i]++;
                    } else if (result == 1) {
                        results[k][j] += lot == 0 ? 3 : 0;
                        results[k][j + i] += lot == 0 ? 0 : 3;
                    } else {
                        results[k][j + i] += lot == 0 ? 3 : 0;
                        results[k][j] += lot == 0 ? 0 : 3;
                    }
                    
                    result = result == 0 ? result : lot == 0 ? result : 3 - result;
                    if (result != 0) {
                        circleResults[j][j + i] = result;
                        circleResults[j + i][j] = 3 - result; 
                    } else {
                        circleResults[j][j + i] = circleResults[j + i][j] = 0; 
                    }
                    String res = result == 0 ? "Draw" : result == 1 ? "First player won." : "Second player won.";
                    log("Game result of Player " + (j + 1) + " and Player " + (j + i + 1) + ": " + res);
                }
                
            }
            log('\n' + writeResultsOfCircle(circleResults));
            log("End of " + (k + 1) + "'s circle." + '\n');
            k++;
            
        }
        return results;
    }

    public String writeResultsOfCircle(int[][] results) {
        StringBuilder result = new StringBuilder();
        result.append("\\" + " ");
        for (int i = 0; i < list.size(); i++) {
            result.append((i + 1) + " ");
        }
        result.append('\n');
        for (int i = 0; i < list.size(); i++) {
            result.append((i + 1) + " ");
            for (int j = 0; j < list.size(); j++) {
                String str = results[i][j] == -1 ? "x" : results[i][j] == 0 ? "0" : results[i][j] == 1 ? "+" : "-";
                result.append(str + " ");
            }
            result.append('\n');
        }
        return result.toString();
    }

    public static Game[][] set(boolean log1, List<Player> list) {
        Game[][] games = new Game[list.size()][list.size()];
        for (int i = 1; i <= list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i; j++) {
                games[j][j + i] = new Game(log1, list.get(j), list.get(j + i));
                games[j + i][j] = new Game(log1, list.get(j + i), list.get(j));
            }
        }
        return games;
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}