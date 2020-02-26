package queue;

import expression.Const;

public class Main {
    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(new Const(233));
        System.out.println(queue.element());
        queue.dequeue();
        queue.dequeue();
        queue.enqueue(2);
        queue.dequeue();
        System.out.println(queue.element());
        queue.dequeue();
        System.out.println(queue.element());

        // for (int i = 0; i < 78; i++) {
        //     ArrayQueueModule.dequeue();
        //     System.out.println("delete: " + ArrayQueueModule.toStr());
        // }
        
    }
}