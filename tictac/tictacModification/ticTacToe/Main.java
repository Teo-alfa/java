package ticTacToe;

import java.util.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println('\n' + "Let's start" + '\n');
        Random random = new Random();
        Player[][] players = {
                                {new HumanPlayer(), 
                                    new HumanPlayer(), 
                                    new SequentialPlayer()},
                                {new RandomPlayer(random), 
                                    new RandomPlayer(random), 
                                    new RandomPlayer(random), 
                                    new RandomPlayer(random)},
                                {new SequentialPlayer(),
                                    new SequentialPlayer(),
                                    new SequentialPlayer(),
                                    new SequentialPlayer()},
                                {new RandomPlayer(random),
                                    new RandomPlayer(random)}
                                };
        int value = 1;
        Settings settings = new Settings(5, 5, 4, 4);
        final Game game = new Game(true, players[value]);
        int result = game.play(settings);
        in.close();
    }
}
