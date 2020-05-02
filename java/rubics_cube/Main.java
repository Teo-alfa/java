package rubics_cube;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        Cube cube = new Cube(3);
        System.out.println(cube);
        cube.readCommands(args[0]);
        System.out.println(cube);
    }
}
