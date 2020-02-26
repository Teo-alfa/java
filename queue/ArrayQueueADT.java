package queue;

public class ArrayQueueADT {
    private int size, start, end;
    private Object[] elements = new Object[2];

    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;
        increaseSize(queue);
        queue.end = queue.size++ != 0 ? (queue.end + 1) % queue.elements.length : queue.end;
        queue.elements[queue.end] = element;
    }

    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;
        Object tmp = queue.elements[queue.start];
        queue.elements[queue.start] = null;
        queue.start = (queue.start + 1) % queue.elements.length;
        queue.size--;
        return tmp;
    }

    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[queue.start]; 
    }

    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[2];
        queue.size = queue.start = queue.end = 0;
    }

    private static void increaseSize(ArrayQueueADT queue) {
        if (queue.size == queue.elements.length || (queue.end + 1 == queue.start)) {
            Object[] tmpObjects = new Object[(queue.size == queue.elements.length ? 2 : 1) * queue.elements.length];
            for (int i = 0; i < queue.size; i++) {
                tmpObjects[i] = queue.elements[(i + queue.start) % queue.elements.length];
            }
            queue.elements = tmpObjects;
            if (queue.end + 1 == queue.start) {
                queue.end = queue.size > 0 ? queue.size - 1 : 0;
                queue.start = 0;
            }
        }
    }

    public static String toStr(ArrayQueueADT queue) {
        StringBuilder result = new StringBuilder();
        result.append('[');
        for (int i = 0; i < queue.size - 1; i++) {
            result.append(queue.elements[(i + queue.start) % queue.elements.length].toString()).append(", ");
        }
        return queue.size > 0 
                ? result.append(queue.elements[queue.end]).append(']').toString()
                : result.append(']').toString();
    }
}