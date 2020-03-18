package queue;

public class ArrayQueue {
    private int size, start;
    private Object[] elements = new Object[2];
    // !end = (start + size - 1) % elements.length
    // I: (size == 0 && queue = []) 
    //    || (size == 1 && queue = [elements[start]])
    //    || (size > 1 && 
    //            (end < start && queue = [a[start], ... ,a[elements.length - 1],a[0], ... ,a[end])
    //            || (end >= start && [a[start], ... ,a[end - 1]])
    //        ) && 0 <= start, size < elements.length   
    // pre: true
    public void enqueue(Object element) {
        size++;
        increaseSize();
        // n = elements.length;
        // post1 : elements' : (elements'.length == 2 * n || (elements'.length == n && size < n)
        //                     && ∀ i ∈ [0, n) & Z : elements'[i] = elements[(i + start) % n] && start' = 0) 
        //                      || (elements' = elements)
        elements[(size + start - 1) % elements.length] = element;
    }
    // post: queue` = [queue[0], ..., queue[size - 1], element], size` = size + 1;

    // pre: true
    public void push(Object element) {
        size++;
        increaseSize();
        start = (start - 1 + elements.length) % elements.length;
        elements[start] = element;
    }
    // post: queue` = [element, queue[0], ..., queue[size - 1]], size` = size + 1;

    // pre: size > 0
    public Object dequeue() {
        assert size > 0;
        Object tmp = elements[start];
        elements[start] = null;
        start = (start + 1) % elements.length;
        // start' = start + 1 || start` = 0;
        if (--size == 0) {
            start = 0;
        }
        // size' = size - 1 && 0 <= size'
        return tmp;
    }
    // post: R = queue[0], queue` = [queue[1], ..., queue[size - 1]] || [] , size` = size - 1;

    // pre: size > 0
    public Object remove() {
        assert size > 0;
        Object tmp = elements[(start + size - 1) % elements.length];
        elements[(start + size - 1) % elements.length] = null;
        if (--size == 0) {
            start = 0;
        }
        return tmp;
    }
    // post: R = queue[size - 1], queue` = [queue[0], ..., queue[size - 2]] || [] , size` = size - 1;


    // pre: size > 0
    public Object element() {
        assert size > 0;
        // size > 0 --> P1
        return elements[start]; 
    }
    // post: R = queue[0]

    // pre: size > 0
    public Object peek() {
        assert size > 0;
        return elements[(start + size - 1) % elements.length];
    }
    // post: R = queue[size - 1]

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
    // post: queue = [], size = 0 (elements = new Object[2], size = start = 0);

    // pre: true
    private void increaseSize() {
        if (size > elements.length) {
            // P3: size = elements.length + 1, because we increase size only in enqueue() and only by one
            Object[] tmpObjects = new Object[2 * elements.length];
            if (start == 0) {
                // P3 && start == 0 <=> queue.toStr() == elements.toStr() (if toStr() to elemenets same)
                System.arraycopy(elements, 0, tmpObjects, 0, size - 1);
            } else {
                // P3 && start != 0 <=> queue = [elements[start], ... , elements[elements.length - 1], elements[0], ... , elements[start - 1]] && start > 0
                System.arraycopy(elements, start, tmpObjects, 0, elements.length - start);
                System.arraycopy(elements, 0, tmpObjects, elements.length - start, start);
            }
            elements = tmpObjects;
            start = 0;
        }
    }
    // post: elements' : (elements'.length == 2 * n || (elements'.length == n && size < n)
    //                     && ∀ i ∈ [0, n) & Z : elements'[i] = elements[(i + start) % n] && start' = 0 && end' = (size - 1 || 0)) 
    //                      || (elements' = elements && size' = size && start' = start)

    // pre: true
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
    // Post : R = ("[" + queue[0] + ", " + ... + queue[size - 1] + "]"  && size > 1) || ("[]" && size == 0) || ("[" + queue[0 + "]" && size == 1)
}