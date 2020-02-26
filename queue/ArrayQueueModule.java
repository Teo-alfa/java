package queue;

public class ArrayQueueModule {
    private static int size, start, end;
    private static Object[] elements = new Object[2];

    public static void enqueue(Object element) {
        assert element != null;
        increaseSize();
        end = size++ != 0 ? (end + 1) % elements.length : end;
        elements[end] = element;
    }

    public static Object dequeue() {
        assert size > 0;
        Object tmp = elements[start];
        elements[start] = null;
        start = (start + 1) % elements.length;
        size--;
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
        size = start = end = 0;
    }

    private static void increaseSize() {
        if (size == elements.length || (end + 1 == start)) {
            Object[] tmpObjects = new Object[(size == elements.length ? 2 : 1) * elements.length];
            for (int i = 0; i < size; i++) {
                tmpObjects[i] = elements[(i + start) % elements.length];
            }
            elements = tmpObjects;
            if (end + 1 == start) {
                end = size > 0 ? size - 1 : 0;
                start = 0;
            }
        }
    }

    public static String toStr() {
        StringBuilder result = new StringBuilder();
        result.append('[');
        for (int i = 0; i < size - 1; i++) {
            result.append(elements[(i + start) % elements.length].toString()).append(", ");
        }
        return size > 0 
                ? result.append(elements[end]).append(']').toString()
                : result.append(']').toString();
    }
}