package ticTacToe;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class TicTacToeBoard implements Board, Position {
    private final Settings settings;
    private int empty;
    private static final Map<Cell, String> SYMBOLS = Map.of(
            Cell.X, "\u001B[31mX",
            Cell.O, "\u001B[34mO",
            Cell.E, ".",
            Cell.Y, "\u001B[32m|",
            Cell.Z, "\u001B[33m-"
    );

    private final Cell[][] cells;
    private Cell turn;

    public TicTacToeBoard(final Settings settings) {
        this.settings = settings;
        this.empty = settings.getM() * settings.getN();
        this.cells = new Cell[settings.getM()][settings.getN()];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    @Override
    public Position getPosition() {
        return new ServerBoard(this);
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }
        cells[move.getRow()][move.getColumn()] = move.getValue();
        if (hasWinOnDirection(move, 0, 1) 
            || hasWinOnDirection(move, 1, 0)
            || hasWinOnDirection(move, 1, 1)
            || hasWinOnDirection(move, 1, -1)) {
                return Result.WIN;
        } 
        if (--empty == 0) {
            return Result.DRAW;
        }
        turn = Cell.values()[(turn.ordinal() + 1) % settings.getNumberOfPlayers()];
        return Result.UNKNOWN;
    }
    
    public int numberOfSameCells(Move move, int dx, int dy) {
        int result = 0;
        int ir = 0;
        while (isValid(move.getRow() + dx * ir, move.getColumn() + dy * ir)
                && cells[move.getRow() + dx * ir][move.getColumn() + dy * ir] == turn) {
            result++;
            ir++;
        }
        return result;   
    }
    
    public boolean hasWinOnDirection (Move move, int dx, int dy) {
        return settings.getK() == numberOfSameCells(move, dx, dy) + numberOfSameCells(move, -dx, -dy) - 1;
    }

    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < settings.getM()
                && 0 <= move.getColumn() && move.getColumn() < settings.getN()
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == getCell();
    }

    public boolean isValid(int x, int y) {
        return 0 <= x && x < settings.getM()
                && 0 <= y && y < settings.getN();
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("  0 1 ");
        for (int i = 2; i < settings.getN(); i++) {
            sb.append(i + " ");
        }
        for (int r = 0; r < settings.getM(); r++) {
            sb.append("\n");
            sb.append(r + " ");
            for (int c = 0; c < settings.getN(); c++) {
                sb.append(SYMBOLS.get(cells[r][c]) + " ");
                sb.append("\u001B[0m");
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

}
