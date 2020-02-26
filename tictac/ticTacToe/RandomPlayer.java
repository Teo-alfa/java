package ticTacToe;

import java.util.Random;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class RandomPlayer implements Player {
    private final Random random;
    private final Settings settings;

    public RandomPlayer(Settings settings, final Random random) {
        this.random = random;
        this.settings = settings;
    }

    public RandomPlayer() {
        this(new Settings(3, 3, 3), new Random());
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            int r = random.nextInt(settings.getM());
            int c = random.nextInt(settings.getN());
            final Move move = new Move(r, c, cell);
            if (position.isValid(move)) {
                return move;
            }
        }
    }
}
