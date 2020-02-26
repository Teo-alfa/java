package queue;

public class ArrayQueue {
    private int size, start, end;
    private Object[] elements = new Object[2];

    public void enqueue(Object element) {
        assert element != null;
        increaseSize();
        end = size++ != 0 ? (end + 1) % elements.length : end;
        elements[end] = element;
    }

    public Object dequeue() {
        assert size > 0;
        Object tmp = elements[start];
        elements[start] = null;
        start = (start + 1) % elements.length;
        size--;
        return tmp;
    }

    public Object element() {
        assert size > 0;
        return elements[start]; 
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        elements = new Object[2];
        size = start = end = 0;
    }

    private void increaseSize() {
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

    public String toStr() {
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