package ticTacToe;

import java.util.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Main {
    public static void main(String[] args) {
        Settings settings = new Settings();
        Scanner in = new Scanner(System.in);
        System.out.println("Set number of circles");
        int circles = 0;
        while (true) {
            while (true) {
                if (in.hasNextInt()) {
                    circles = in.nextInt();
                    break;
                } else {
                    System.out.println("Try again");
                    in.next();
                }
            }
            if (circles < 1) {
                System.out.println("Try again");
            } else {
                break;  
            }
        }
        System.out.println('\n' + "Let's start" + '\n');
        List<List<Player>> lists = List.of(
            List.of(
                new RandomPlayer(settings, new Random()), 
                new RandomPlayer(settings, new Random()), 
                new SequentialPlayer(settings),
                new SequentialPlayer(settings),
                new RandomPlayer(settings, new Random())
            ),
            List.of(new HumanPlayer(), new HumanPlayer())
        );
        
        final Game[][] games = Tournament.set(true, lists.get(0));
        boolean log;
        System.out.println("Do you want to see interim results? (yes / no)");
        String choice = in.next();
        while (!(choice.equals("yes") || choice.equals("no"))) {
            System.out.println("try again.");
            choice = in.next();
        }
        System.out.println();

        log = choice.equals("yes") ? true : false;
        final Tournament tournament = new Tournament(log, circles, lists.get(0));
        //final Tournament tournament1 = new Tournament(log, circles, lists.get(1));
        int[][] result = tournament.play(settings, games);
        for (int j = 0; j < circles; j++) {
            /*
            System.out.print((j + 1) + ": " + '\t');
            for (int i = 0; i < result[circles - 1].length; i++) {
                System.out.print("[" + (i + 1) + "]: " + result[j][i] + ", " + '\t');
            }
            System.out.println();  
            */

            System.out.printf("Results of %2d'th circle: ",(j + 1));
            for (int i = 0; i < result[circles - 1].length; i++) {
                System.out.printf("[%d]:%3d ", (i + 1), result[j][i]);
                //System.out.print("[" + (i + 1) + "]: " + result[j][i] + ", " + '\t');
            }
            System.out.println();  
        }

        in.close();
        /*
        final Game game = new Game(false, new HumanPlayer(), new HumanPlayer());
        int result;
        
        do {
            result = game.play(new TicTacToeBoard(settings));
            System.out.println("Game result: " + result);
        } while (result != 0);
        */
        /*
        int p[] = new int[2];
        p[0] = p[1] = 0;
        for (int i = 0; i < 100; i++) {
            result = game.play(new TicTacToeBoard(settings));
            System.out.print("Game result[" + (i + 1) + "]: " + result);
            if (result == 0) {
                p[0]++;
                p[1]++;
            } else {
                p[result - 1]+= 2;
            }
            System.out.print(" " + p[0] + " : " + p[1] + "\n");
        }
        */
    }
}
