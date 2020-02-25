package rubics_cube;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cube {
    private List<Side> sides;
    private int size;
    // [up, left, down, right] sides
    private List<List<Integer>> sidesDirections = List.of(
        List.of(2, 3, 2, 1, 2, 2),
        List.of(5, 5, 6, 5, 3, 1),
        List.of(4, 1, 4, 3, 4, 4),
        List.of(6, 6, 5, 6, 1, 3)
    );

    private List<List<Integer>> angles = List.of(
        List.of(0, 0, 0, 0),
        List.of(180, 90, 0, -90),
        List.of(180, 0, -180, 0),
        List.of(0, -90, 180, 90),
        List.of(270, 0, -270, 0),
        List.of(90, 0, -90, 0)
    );
    
    private Map<Cell, String> symbolMap= Map.of(
        Cell.W, "W",
        Cell.Y, "Y", 
        Cell.B, "B", 
        Cell.G, "G", 
        Cell.R, "R", 
        Cell.O, "O"
    );
    
    public Cube(int size) {
        this.size = size;
        sides = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            List<List<Cell>> values = new ArrayList<>();
            Cell cell = (Cell.values())[i];
            for (int j = 0; j < size; j++) {
                List<Cell> line = new ArrayList<>();
                for (int k = 0; k < size; k++) {
                    line.add(cell);
                }
                values.add(line);
            }
            sides.add(new Side(size, values)); 
        }       
    }

    public void rotate(int number, int angle) {
        sides.get(number - 1).rotate(angle);
        for (int i = 0; i < 4; i++) {
            int numberOfSide = sidesDirections.get(i).get(number - 1) - 1;
            sides.get(numberOfSide).rotate(angles.get(number - 1).get(i));
        }
        normalRotate(angle, sides.get(sidesDirections.get(0).get(number - 1) - 1),
                            sides.get(sidesDirections.get(1).get(number - 1) - 1),
                            sides.get(sidesDirections.get(2).get(number - 1) - 1),
                            sides.get(sidesDirections.get(3).get(number - 1) - 1));
        for (int i = 0; i < 4; i++) {
            int numberOfSide = sidesDirections.get(i).get(number - 1) - 1;
            sides.get(numberOfSide).rotate(-angles.get(number - 1).get(i));
        }
    }

    private void normalRotate(int angle, Side up, Side left, Side down, Side right) {
        if (angle == 90) {
            for (int i = 0; i < size; i++) {
                Cell tmp = left.get(size - 1 - i, size - 1); 
                left.set(size - 1 - i, size - 1, down.get(0, size - 1 - i));
                down.set(0, size - 1 - i, right.get(i, 0));
                right.set(i, 0, up.get(size - 1, i));
                up.set(size - 1, i, tmp);
            }
        } else if (angle == 180) {
            normalRotate(90, up, left, down, right);
            normalRotate(90, up, left, down, right);
        } else if (angle == -90) {
            normalRotate(90, up, left, down, right);
            normalRotate(90, up, left, down, right);
            normalRotate(90, up, left, down, right);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size * 4; j++) {
                if (j >= size && j < size * 2) {
                    result.append(symbolMap.get(sides.get(1).get(i, j % size)));
                } else {
                    result.append(" ");
                }
            }
            result.append("\n");
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size * 4; j++) {
                if (j >= 0 && j < size) {
                    result.append(symbolMap.get(sides.get(4).get(i, j % size)));
                } else if (j >= size && j < size * 2) {
                    result.append(symbolMap.get(sides.get(0).get(i, j % size)));
                } else if (j >= size * 2 && j < size * 3){
                    result.append(symbolMap.get(sides.get(5).get(i, j % size)));
                } else {
                    result.append(symbolMap.get(sides.get(2).get(i, j % size)));
                }
            }
            result.append("\n");
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size * 4; j++) {
                if (j >= size && j < size * 2) {
                    result.append(symbolMap.get(sides.get(3).get(i, j % size)));
                } else {
                    result.append(" ");
                }
            }
            result.append("\n");
        }

        return result.toString();

    }

    public static int number(char ch) {
        switch (ch) {
            case 'F' : 
                return 1;
            case 'U' :
                return 2;
            case 'R' :
                return 6;
            case 'L' :
                return 5;
            case 'D' : 
                return 4;
            case 'B' :
                return 3;
            default:
                return 0;
        }
    }

    public void readCommands(String commands) {
        boolean wait = false;
        int side = 0;
        for (int i = 0; i < commands.length(); i++) {
            char ch = commands.charAt(i);
            if (number(ch) != 0 && wait == false) {
                side = number(ch);
                wait = true;
            } else if (number(ch) != 0 && wait == true) {
                rotate(side, 90);
                side = number(ch);
            } else if (number(ch) == 0) {
                if (ch == '\'') {
                    rotate(side, -90);
                } else {
                    rotate(side, 180);
                }
                wait = false;
            }

            if (commands.length() == 1) {
                rotate(side, 90);
                break;
            }

            if (i == commands.length() - 1 && wait == true) {
                rotate(side, 90);
                break;
            }
        }
    }
}
