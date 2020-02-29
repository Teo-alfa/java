package queue;

public class ArrayQueueModule {
    private static int size, start;
    private static Object[] elements = new Object[2];

    public static void enqueue(Object element) {
        size++;
        increaseSize();
        elements[(size + start - 1) % elements.length] = element;
    }

    public static Object dequeue() {
        assert size > 0;
        Object tmp = elements[start];
        elements[start] = null;
        start = (start + 1) % elements.length;
        if (--size == 0) {
            start = 0;
        }
        return tmp;
    }

    public static Object element() {
        assert size > 0;
        return elements[start]; 
    }

    public static int size() {
        return size;
    }

    public static boolean isEmpty() {
        return size == 0;
    }

    public static void clear() {
        elements = new Object[2];
        size = start = 0;
    }

    private static void increaseSize() {
        if (size == elements.length) {
            Object[] tmpObjects = new Object[2 * elements.length];
            for (int i = 0; i < size; i++) {
                tmpObjects[i] = elements[(i + start) % elements.length];
            }
            elements = tmpObjects;
            start = 0;
        }
    }

    public static String toStr() {
        StringBuilder result = new StringBuilder();
        result.append('[');
        for (int i = 0; i < size - 1; i++) {
            result.append(elements[(i + start) % elements.length]).append(", ");
        }
        return size > 0 
                ? result.append(elements[(size + start - 1) % elements.length]).append(']').toString()
                : result.append(']').toString();
    }
}