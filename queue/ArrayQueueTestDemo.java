package queue;

import java.util.Random;

import expression.Const;
import expression.Variable;

public class ArrayQueueTestDemo {
    public static void fill(ArrayQueue queue, Object[] elements) {
        for (Object object : elements) {
            queue.enqueue(object);
        }
    }

    public static Object[] randomArray() {
        int length = new Random().nextInt(1 << 8 - 1) + 1;
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

    public static void test(ArrayQueue queue) {
        while(!queue.isEmpty()) {
            System.out.println(queue.toStr() + "(size = " + queue.size() + ") = [" + queue.dequeue() + "] + " + queue.toStr());
        }
    }

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        fill(queue, randomArray());
        test(queue);
    }
}