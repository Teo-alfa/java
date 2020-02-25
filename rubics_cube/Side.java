package rubics_cube;

import java.util.List;
import java.util.Map;

public class Side {
    private int size;
    public List<List<Cell>> cells;

    private Map<Cell, String> symbolMap= Map.of(
        Cell.W, "W",
        Cell.Y, "Y", 
        Cell.B, "B", 
        Cell.G, "G", 
        Cell.R, "R", 
        Cell.O, "O"
    );

    public Side(int n, List<List<Cell>> cells) {
        size = n;
        this.cells = cells;
    }

	public void rotate(int angle) {
        if (angle == 90 || angle == -270) {
            for (int i = 0; i < size - 1; i++) {
                Cell tmp = cells.get(size - 1 - i).get(0);
                cells.get(size - 1 - i).set(0, cells.get(size - 1).get(size - 1 - i));
                cells.get(size - 1).set(size - 1 - i, cells.get(i).get(size - 1));
                cells.get(i).set(size - 1, cells.get(0).get(i));
                cells.get(0).set(i, tmp);
            }
        } else if (angle == -90 || angle == 270) {
            mirrorReflection();
            rotate(90);
            mirrorReflection();
        } else if (angle == 180 || angle == -180) {
            rotate(90);
            rotate(90);
        } else if (angle == 0) {

        }
        
    }

    public void mirrorReflection() {
        for (int i = 0; i < size; i++) {
            Cell tmp = cells.get(i).get(0);
            cells.get(i).set(0, cells.get(i).get(size - 1));
            cells.get(i).set(size - 1, tmp);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (List<Cell> line : cells) {
            for(Cell cell : line) {
                result.append(symbolMap.get(cell));
            }
            result.append("\n");
        }
        return result.toString();   
    }

    public Cell get(int i, int j) {
        return cells.get(i).get(j);
    }

    public void set(int i, int j, Cell value) {
        cells.get(i).set(j, value);
    }
}
