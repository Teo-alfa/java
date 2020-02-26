package search;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            return;
        }
        int key = Integer.parseInt(args[0]);
        int a[] = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        System.out.println("key " + key + " should be a[" + BinarySearch.iterativeSearch(a, key) + "]");
    }
}
