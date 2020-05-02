package tictac.tictacModification;

import java.util.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Game {
    private final boolean log;
    private final Player[] players;

    public Game(final boolean log, Player[] players) {
        this.log = log;
        this.players = players;
    }
    
    public int play(Settings settings) {
        Board board = settings.createBoard();

        if (settings.getNumberOfPlayers() != players.length) {
            log(gameResult(Integer.MAX_VALUE));
            return Integer.MAX_VALUE;
        }

        List<Integer> elements = new ArrayList<>();
        for(int i = 0; i < settings.getNumberOfPlayers(); i++) {
            elements.add(i + 1);
        }
        Collections.shuffle(elements);
        System.out.println("Player " + elements.get(0) + " starts.");
        for (int i = 1; i < settings.getNumberOfPlayers(); i++) {
            System.out.println("Then Player " + elements.get(i) + ".");
        }
        System.out.println();

        while (true) {
            for (int index : elements) {
                final int result = move(board, players[index - 1], index);
                if (result != -players.length) {
                    log('\n' + gameResult(result));
                    return result;
                }
            }
        }
    }

    private int move(final Board board, final Player player, final int no) {
        final Move move = player.move(board.getPosition(), board.getCell());
        final Result result = board.makeMove(move);
        if (result == Result.LOSE) {
            return -no;
        }
        log('\n' + "Player " + no + " move: " + move);
        log("Position:\n" + board);
        if (result == Result.WIN) {
            return no;
        } else if (result == Result.DRAW) {
            return 0;
        } else {
            return -players.length;
        }
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }

    public String gameResult(int result) { 
        if (result == Integer.MAX_VALUE) {
            return "Incorrect number of players. ";
        } else if (result > 0) {
            return "Player " + result + " won.";
        } else if (result == 0) {
            return "Draw.";
        } else {
            return "Player " + (-result) + " disqualified. Others won." ;
        }
    }

}
