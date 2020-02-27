package queue;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.Random;

import expression.Const;
import expression.Variable;

public class ArrayQueueTestDemo {
    static int LINESIZE = 33;
    static DecimalFormat FORMAT = new DecimalFormat("\u001B[3m\u001B[33m00\u001B[0m");

    private static Object expected;
    private static Object actual;

    public static void fill(ArrayQueue queue, Object[] elements) {
        for (Object object : elements) {
            queue.enqueue(object);
        }
    }

    public static void fill(ArrayDeque<Object> queue, Object[] elements) {
        for (Object object : elements) {
            queue.add(object);
        }
    }

    public static Object[] randomArray(int size) {
        int length = size;
        Object[] tmpObjects = new Object[length];
        for (int i = 0; i < tmpObjects.length; i++) {
            tmpObjects[i] = randomObject();
        }

        return tmpObjects;
    }

    public static Object randomObject() {
        Random random = new Random();
        int mode = random.nextInt(4);
        switch (mode) {
            case 0: 
                return random.nextInt(10);
            case 1:
                return random.nextDouble();
            case 2:
                return new Const(random.nextDouble());
            case 3: 
                return new Variable(random.nextInt(1) == 0 ? "x" : random.nextInt(1) == 0 ? "y" : "z");
            default: 
                return null;
        }
    }

    public static boolean simpleRandomTest(int size, int numberOfAdd, int numberOfPop) {
        ArrayDeque<Object> deque = new ArrayDeque<Object>();
        ArrayQueue queue = new ArrayQueue();
        Object[] objects = randomArray(size);
        fill(queue, objects);
        fill(deque, objects);
        ArrayDeque<Integer> stackOperation = new ArrayDeque<Integer>();
        Random random = new Random();
        for (int i = 0; i < numberOfAdd + numberOfPop; i++) {
            while(true) {
                int p = random.nextInt(2);
                if ((p == 0 && numberOfPop != 0) 
                    || (p == 1 && numberOfAdd != 0)) {
                    if (p == 0) {
                        numberOfPop--;
                    } else {
                        numberOfAdd--;
                    }
                    stackOperation.add(p);
                    break;
                } 
                if (numberOfAdd == 0 && numberOfPop == 0) {
                    break;
                } 
            }
        }

        while(!stackOperation.isEmpty()) {
            int mode = stackOperation.pop();
            if (mode == 0) {
                queue.dequeue();
                deque.pollFirst();
            } else {
                Object object = randomObject();
                queue.enqueue(object);
                deque.add(object);
            }
        }

        boolean failed = false;
        while(!queue.isEmpty()) {
            Object a1 = queue.dequeue(), a2 = deque.pollFirst();
            if(!a1.equals(a2)) {
                failed = true;
                expected = a2;
                actual = a1;
                break;
            } 
        }
        return !failed;
    }

    public static void test() {
        int number = 0;
        int maxSize = 15;
        boolean failed = false;
        long time0 = System.currentTimeMillis();
        System.out.println(line('=', LINESIZE));
        for (int size = 1; size <= maxSize; size++) {
            number+= size - 1;
            for (int delete = 1; delete < size + 1; delete++) {
                Exception exp = null;
                try {
                    failed = !simpleRandomTest(size, size + 1 - delete, delete - 1);
                } catch (Exception e) {
                    failed = true;
                    exp = e;
                }
                System.out.println("=== testRandom #" + FORMAT.format(number + delete) + " running: " + boldItalicString(!failed ? "Done" : "Failed", !failed ? 32 : 31));
                if (failed) {
                    System.out.println(line('-', LINESIZE));
                    System.out.println("Information about test: ");
                    System.out.println("Size: " + size + ", number of adds: " + (size + 1 - delete) + ", number of deletions: " + (delete - 1));
                    System.out.print("Cause of breakdown: ");
                    if (exp != null) {
                        exp.printStackTrace();
                    } else {
                        System.out.println("\nIn dequeue() -" + " expected: " + expected.toString());
                        System.out.println("               actual: " + actual.toString());
                    }
                    break;
                }
            }
            if (failed) {
                break;
            }
        }
        if (!failed) {
            System.out.println(line('=', LINESIZE));
            System.out.println("Test run: "+ (number + maxSize) + ", failed: 0\nFinished in " + (System.currentTimeMillis() - time0) + " ms");
        }
    }

    private static String line(char example, int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) { result.append(example); }
        return result.toString();
    }

    private static String boldItalicString(String sourse, int color) {
        return "\u001B[3m\u001B["+ color + "m\u001B[1m" + sourse + "\u001B[0m";
    }

    public static void main(String[] args) {
        test();
    }
}