package queue;

import expression.operations.Const;

public class Main {
    public static void main(String[] args) {
        ArrayQueueModule.enqueue(2);
        ArrayQueueModule.dequeue();
        ArrayQueueModule.enqueue(new Const(233));
        System.out.println(ArrayQueueModule.element());
        ArrayQueueModule.dequeue();
        ArrayQueueModule.enqueue(2);
        ArrayQueueModule.dequeue();
        System.out.println(ArrayQueueModule.element());
        ArrayQueueModule.dequeue();
        System.out.println(ArrayQueueModule.element());

        // for (int i = 0; i < 78; i++) {
        //     ArrayQueueModule.dequeue();
        //     System.out.println("delete: " + ArrayQueueModule.toStr());
        // }
        
    }
}