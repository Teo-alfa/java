package ticTacToe;

public class ServerBoard implements Position {
    private TicTacToeBoard board;

    public ServerBoard(TicTacToeBoard board) {
        this.board = board;
    }
    
    @Override
    public boolean isValid(Move move) {
        return board.isValid(move);
    }

    @Override
    public Cell getCell(int r, int c) {
        return board.getCell(r, c);
    }

    @Override
    public String toString() {
        return board.toString();
    }
    
    @Override
    public Settings getSettings() {
        return board.getSettings();
    }
}