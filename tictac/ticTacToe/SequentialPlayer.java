package tictac.ticTacToe;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SequentialPlayer implements Player {
    private final Settings settings;
    public SequentialPlayer() {
        this.settings = new Settings(3, 3, 3);
    }
    public SequentialPlayer(final Settings settings) {
        this.settings = settings;
    }
    @Override
    public Move move(final Position position, final Cell cell) {
//        Board board = (Board) position;
//        board.makeMove()
        for (int r = 0; r < settings.getM(); r++) {
            for (int c = 0; c < settings.getN(); c++) {
                final Move move = new Move(r, c, cell);
                if (position.isValid(move)) {
                    return move;
                }
            }
        }
        throw new IllegalStateException("No valid moves");
    }
}
