package queue;
// same but ArrayQueue queue with in argument shouldn't be null
public class ArrayQueueADT {
    private int size, start;
    private Object[] elements = new Object[2];

    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert queue != null;
        queue.size++;
        increaseSize(queue);
        queue.elements[(queue.size + queue.start - 1) % queue.elements.length] = element;
    }

    public static Object dequeue(ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        Object tmp = queue.elements[queue.start];
        queue.elements[queue.start] = null;
        queue.start = (queue.start + 1) % queue.elements.length;
        if (--queue.size == 0) {
            queue.start = 0;
        }
        return tmp;
    }

    public static Object element(ArrayQueueADT queue) {
        assert queue != null;
        assert queue.size > 0;
        return queue.elements[queue.start]; 
    }

    public static int size(ArrayQueueADT queue) {
        assert queue != null;
        return queue.size;
    }

    public static boolean isEmpty(ArrayQueueADT queue) {
        assert queue != null;
        return queue.size == 0;
    }

    public static void clear(ArrayQueueADT queue) {
        assert queue != null;
        queue.elements = new Object[2];
        queue.size = queue.start = 0;
    }

    private static void increaseSize(ArrayQueueADT queue) {
        assert queue != null;
        if (queue.size > queue.elements.length ) {
            Object[] tmpObjects = new Object[2 * queue.elements.length];
            if (queue.start == 0) {
                // P3 && start == 0 <=> queue.toStr() == elements.toStr() (if toStr() to elemenets same)
                System.arraycopy(queue.elements, 0, tmpObjects, 0, queue.size - 1);
            } else {
                // P3 && start != 0 <=> queue = [elements[start], ... , elements[elements.length - 1], elements[0], ... , elements[start - 1]] && start > 0
                System.arraycopy(queue.elements, queue.start, tmpObjects, 0, queue.elements.length - queue.start);
                System.arraycopy(queue.elements, 0, tmpObjects, queue.elements.length - queue.start, queue.start);
            }
            queue.elements = tmpObjects;
            queue.start = 0;
        }
    }

    public static String toStr(ArrayQueueADT queue) {
        assert queue != null;
        StringBuilder result = new StringBuilder();
        result.append('[');
        for (int i = 0; i < queue.size - 1; i++) {
            result.append(queue.elements[(i + queue.start) % queue.elements.length].toString()).append(", ");
        }
        return queue.size > 0 
                ? result.append(queue.elements[((queue.size + queue.start - 1) % queue.elements.length)]).append(']').toString()
                : result.append(']').toString();
    }
}