package search;

public class BinarySearchMissing {
    // Pre: a[] : (a[i] >= a[i + 1], при i: i >= 0 && i < a.length - 1 && a.length >= 2) || (a.length < 2)
    public static int iterativeSearch(int a[], int key) {
        int middle = 0;
        int left = -1, right = a.length;
        // a'[] = {+inf, a[0], ..., a[a.length - 1], -inf}
        // --> a'[left] <= a[0] && a[a.length - 1] <= a'[right], так как a невозрастающий, то a' невозрастающий
        // I1 : a'[left] <= key && key <= a'[right]  
        // middle >= left && middle <= right && I1
        if (a.length == 0) {
            // middle >= left && middle <= right && a.length == 0 && I1
            right = 0;
        } else {
            // middle >= left && middle <= right && a.length > 0
            // distance = right - left
            // cond = distance > 1;
            while (right - left > 1) {
                // cond
                // P2 = middle >= left && middle <= right && a.length > 0
                // P2 && cond
                middle = (right + left) / 2;
                if (a[middle] > key) {
                    // P2 && cond && a[middle] > key
                    left = middle;
                    // P2 && a[middle] > key
                } else {
                    // P2 && cond && a[middle] <= key
                    right = middle;
                    // P2 && a[middle] <= key
                }
                // (right == middle || left == middle) -->  (distance` = right` - left` <= (right - left) - 1 && distance >= 2) || (distance < 2 == !(distance > 1))
                // --> !cond || сходимость
                // (right == middle || left == middle) && middle >= left && middle <= right && (distance` <= distance - 1 || distance < 1)
            }
            // P2 && !cond
        }
        // (P2 || a.length == 0) && !cond
        if (a.length == 0 || (right < 0 || right >= a.length) || (a[right] != key)) {
            // P3 : a.length == 0 || (right < 0 || right >= a.length) || (a[right] != key)
            // (P2 || a.length == 0) && !cond && (a.length == 0 || (right < 0 || right >= a.length) || (a[right] != key))          
            right = -right -1;
        }
        return right;
    }
    // Post: (!P3 --> R = i: a[i] == key) || (P3 --> R : a[-R - 1] <= key && key <= a[-R])

    // Pre: (a[i] >= a[i + 1], при i: i >= 0 && i < a.length - 1 && a.length >= 2) || (a.length < 2)
    //        && -1 <= left && left < a.length && left < right && right <= a.length
    public static int recurrentSearch(int a[], int key, int left, int right, int level) {
        int middle = (right + left) / 2;
        // I1 : a'[left] <= key && key <= a'[right], где, если 0 <= right, left < a.length, то a`[] = a[], иначе a'[] = {+inf, a[0], ..., a[a.length - 1], -inf}  
        // left <= middle && middle <= right && I1
        if (a.length == 0) {
            // left <= middle && middle <= right && a.length == 0
            right = 0;
        } else {
            // distance = right - left
            // I2 : left <= middle && middle <= right && a.length > 0
            if (a[middle] > key) {
                // I2 && a[middle] > key
                left = middle;
                // left` = middle >= left
            } else {
                // I2 && a[middle] <= key
                right = middle;
                // right` = middle <= right
            }
            // I2 && (a[middle] <= key || a[middle] > key)
            // distance` = right` - left` < distance || (distance` = right` - left` = distance && distance == 1)
            // --> сходимость || !(distance > 1)
            if (right - left > 1) {
                // I2 && (a[middle] <= key || a[middle] > key) && distance > 1
                // Pre2: Pre && (left < left` || right` < right)
                right = recurrentSearch(a, key, left, right, level + 1);
                // Post:  right = R : a'[R] <= key && key <= a'[R + 1]
            }
            //Post && !(right - left > 1)
        }
        // ((a.length == 0) || (Post && !(right - left > 1)))
        if (level == 0) {
            // ((a.length == 0) || (Post && !(right - left > 1))) && level == 0
            if (a.length == 0 || (right < 0 || right >= a.length) || (a[right] != key)) {  
                // P3 : a.length == 0 || (right < 0 || right >= a.length) || (a[right] != key)
                // ((a.length == 0) || (Post && !(right - left > 1))) && level == 0 && P3     
                right = -right -1;
            }  
        }
        
        return right;        
    }
    // Post: (!P3 --> R = i: a[i] == key) || (P3 --> R : a[-R - 1] <= key && key <= a[-R])

    // Pre: a[] : (a[i] >= a[i + 1], при i: i >= 0 && i < a.length - 1 && a.length >= 2) || (a.length < 2)
    public static int recurrentSearch(int a[], int key) {
        // Pre2: Pre
        return recurrentSearch(a, key, -1, a.length, 0);
        // P3 : a.length == 0 || (right < 0 || right >= a.length) || (a[right] != key)
        // Post2 : (!P3 --> R = i: a[i] == key) || (P3 --> R : a[-R - 1] <= key && key <= a[-R])
    }
    // Post : (!P3 --> R = i: a[i] == key) || (P3 --> R : a[-R - 1] <= key && key <= a[-R])
    
    // Pre : args.length > 0 && Integer.parseInt(arg[i]) for i < [0, args.length]
    public static void main(String[] args) {
        if (args.length < 1) {
            // args.length < 1
            return;
        }
        // args.length > 0
        // args.length > 0 && args[0] - integer
        int key = Integer.parseInt(args[0]);
        // args.length > 0 && key != null
        int a[] = new int[args.length - 1];
        // args.length > 0 && key != null
        for (int i = 1; i < args.length; i++) {
            // args.length > 1 && key != null && args[i] - integer
            a[i - 1] = Integer.parseInt(args[i]);
        }
        // args.length > 0 && key != null
        System.out.println(recurrentSearch(a, key));
        // args.length > 0 && key != null
    }
    //post: post of reccurentSearch
}