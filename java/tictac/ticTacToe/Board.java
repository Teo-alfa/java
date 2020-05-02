package tictac.ticTacToe;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Board {
    Position getPosition();
    Cell getCell();
    Result makeMove(Move move);
    Settings getSettings();
    Board resetOf();
}
