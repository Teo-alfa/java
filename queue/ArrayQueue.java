package queue;

public class ArrayQueue {
    private int size, start;
    private Object[] elements = new Object[2];
    // !end = (start + size - 1) % elements.length
    // I: (queue = (end < start && [a[start], ... ,a[elementd.length - 1],a[0], ... ,a[end]])
    //           || (end >= start && [a[start], ... ,a[end]]) || (queue = [] && size == 0)) && size <= elements.length 
    // P1: 0 <= end, start && end, start < elements.length

    // pre: 0 <= size < elements.length && P1
    public void enqueue(Object element) {
        // pre
        size++;
        // pre1: P1
        increaseSize();
        // n = elements.length;
        // post1 : elements' : (elements'.length == 2 * n || (elements'.length == n && size < n)
        //                     && ∀ i ∈ [0, n) & Z : elements'[i] = elements[(i + start) % n] && start' = 0 && end' = (size - 1 || 0)) 
        //                      || (elements' = elements && size' = size && end' = end)
        elements[(size + start - 1) % elements.length] = element;
        // end < elements.length
    }
    // post: elements = elements' && elements[(end - 1 + elements.length) %  elements.length] = element;

    // pre: size < elements.length && P1
    public Object dequeue() {
        assert size > 0;
        // (P2 : size > 0) && pre
        Object tmp = elements[start];
        elements[start] = null;
        start = (start + 1) % elements.length;
        // start' = (start + 1 || 0)
        if (--size == 0) {
            start = 0;
        }
        // size' = size - 1 && 0 <= size'
        return tmp;
    }
    // post: R = elements[(start - 1 + elements.length) % elements.length]

    // pre: true
    public Object element() {
        assert size > 0;
        // size > 0 --> P1
        return elements[start]; 
    }
    // R : (size > 0 && R = elements[start]) || assertion

    // pre: true
    public int size() {
        return size;
    }
    // post: R = size

    // pre: true
    public boolean isEmpty() {
        return size == 0;
    }
    // post: R = size == 0

    // pre: true
    public void clear() {
        elements = new Object[2];
        size = start = 0;
    }
    // post: elementselements = new Object[2], size = start = 0;

    // pre: true
    private void increaseSize() {
        if (size > elements.length) {
            // P3: size == elements.length
            Object[] tmpObjects = new Object[2 * elements.length];
            for (int i = 0; i < size; i++) {
                // P3 && size > 0 && i < size
                tmpObjects[i] = elements[(i + start) % elements.length];
            }
            elements = tmpObjects;
            start = 0;
        }
    }
    // post: elements' : (elements'.length == 2 * n || (elements'.length == n && size < n)
    //                     && ∀ i ∈ [0, n) & Z : elements'[i] = elements[(i + start) % n] && start' = 0 && end' = (size - 1 || 0)) 
    //                      || (elements' = elements && size' = size && end' = end)

    // pre: P1 
    public String toStr() {
        StringBuilder result = new StringBuilder();
        result.append('[');
        for (int i = 0; i < size - 1; i++) {
            //P1:  size > 1
            result.append(elements[(i + start) % elements.length].toString()).append(", ");
        }
        return size > 0 
                ? result.append(elements[(size + start - 1) % elements.length]).append(']').toString()
                : result.append(']').toString();
    }
    // Post : R = ( "[" + elements'[start] + ", " + ... + elements'[end] + "]" ) || "[]"
}