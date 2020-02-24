package search;

public class BinarySearchShift {
    public static int iterativeSearch(int a[]) {
        int k = 0;
        int delta = a[0] > a[a.length - 1] ? 1 : -1; //(34512 / 21543)
        int right = a.length, left = -1, middle = (left + right) / 2;
        if (a.length > 2) {
            k = middle;
            while (!(a[middle - 1] < a[middle] && a[middle] > a[middle + 1])) {

            }
        }
        return k;
    }
    public static int recurentSearch(int a[], int key, int left, int right) {
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
                right = recurentSearch(a, key, left, right);
            }
        }
        
        return right;        
    }

    public static int recurentSearch(int a[], int key) {
        return recurentSearch(a, key, -1, a.length);
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
        System.out.println(iterativeSearch(a));
    }
}