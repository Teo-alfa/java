package tictac.ticTacToe;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in));
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            out.println("Position");
            out.println(position);
            out.println(cell + "'s move");
            out.println("Enter row and column");
            int i, j;
            i = readInt();
            j = readInt();
            final Move move = new Move(i, j, cell);
            if (position.isValid(move) || i == Integer.MIN_VALUE || j == Integer.MIN_VALUE) {
                return move;
            }
            
            out.println("Move " + move + " is invalid");
        }
    }

    public int readInt() {
        while (!in.hasNextInt()) {        
            System.out.println("Fix this");
            // in.next();
            if (in.hasNext()) {
                in.next(); 
            } else {
                System.out.println("you lose cheater...");
                return Integer.MIN_VALUE;
            }
            
        }  
        return in.nextInt();
    }
}
