package search;

public class BinarySearch {
    public static int binarySearch(int a[], int key) {
        int result = 0;
        int left = -1, right = a.length;
        if (a.length == 0) {
            right = 0;
        } else {
            while (right - left > 1) {
                result = (right + left) / 2;
                if (a[result] > key) {
                    left = result;
                } else {
                    right = result;
                }
            }
        }
        return right;
    }
    public static int iterativeSearch(int a[], int key, int left, int right) {
        int result = (right + left) / 2;;
        if (a.length == 0) {
            right = 0;
        } else {
            if (a[result] > key) {
                left = result;
            } else {
                right = result;
            }
            if (right - left > 1) {
                right = iterativeSearch(a, key, left, right);
            }
        }
        
        return right;        
    }

    public static int iterativeSearch(int a[], int key) {
        return iterativeSearch(a, key, -1, a.length);
    }
    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        int key = Integer.parseInt(args[0]);
        int a[] = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        System.out.println(binarySearch(a, key));
    }
}