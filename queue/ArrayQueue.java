package queue;

public class ArrayQueue extends AbstractQueue {
    private int start;
    private Object[] elements = new Object[2];
    // !end = (start + size - 1) % elements.length
    // I: (size == 0 && queue = []) 
    //    || (size == 1 && queue = [elements[start]])
    //    || (size > 1 && 
    //            (end < start && queue = [a[start], ... ,a[elements.length - 1],a[0], ... ,a[end])
    //            || (end >= start && [a[start], ... ,a[end]])
    // 
    
    // :NOTE: copy-paste of code for getting current head/tail

    private int end() {
        return start + size - 1;
    }
    
    // pre: true
    public void push(Object element) {
        increaseSize();
        start = (start - 1 + elements.length) % elements.length;
        elements[start] = element;
        size++;
    }
    // post: queue` = [element, queue[0], ..., queue[size - 1]], size` = size + 1;
    
    @Override
    protected void putInTail(Object element) {
        // pre1: P1
        increaseSize();
        // n = elements.length;
        // post1 : elements' : (elements'.length == 2 * n || (elements'.length == n && size < n)
        //                     && ∀ i ∈ [0, n) & Z : elements'[i] = elements[(i + start) % n] && start' = 0 && end' = (size - 1 || 0)) 
        //                      || (elements' = elements && size' = size && end' = end)
        elements[(end() + 1) % elements.length] = element;
        // end < elements.length
    }

    @Override
    protected Object popHead() {
        // (P2 : size > 0) && pre
        Object tmp = elements[start];
        elements[start] = null;
        start = (start + 1) % elements.length;
        // start' = (start + 1 || 0)
        if (size == 0) {
            start = 0;
        }
        // size' = size - 1 && 0 <= size'
        return tmp;
    }

    // pre: size > 0
    public Object remove() {
        assert size > 0;
        Object tmp = elements[end() % elements.length];
        elements[(end()) % elements.length] = null;
        if (--size == 0) {
            start = 0;
        }
        return tmp;
    }
    // post: R = queue[size - 1], queue` = [queue[0], ..., queue[size - 2]] || [] , size` = size - 1;


    @Override
    protected Object getHead() {
        // size > 0 --> P1
        return elements[start]; 
    }

    // pre: size > 0
    public Object peek() {
        assert size > 0;
        return elements[end() % elements.length];
    }
    // post: R = queue[size - 1]

    @Override
    protected void cleaning() {
        elements = new Object[2];
        start = 0;
    }

    // pre: true
    protected void increaseSize() {
        if (size == elements.length) {
            // P3: size = elements.length + 1, because we increase size only in enqueue() and only by one
            Object[] tmpObjects = new Object[2 * elements.length];
            fillArray(tmpObjects);
            elements = tmpObjects;
            start = 0;
        }
    }
    // post: elements' : (elements'.length == 2 * n || (elements'.length == n && size < n)
    //                     && ∀ i ∈ [0, n) & Z : elements'[i] = elements[(i + start) % n] && start' = 0 && end' = (size - 1 || 0)) 
    //                      || (elements' = elements && size' = size && end' = end)

    @Override
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

    @Override
    protected void fillArray(Object[] array) {
        if (size + start >= elements.length) {
            System.arraycopy(elements, start, array, 0, elements.length - start);
            System.arraycopy(elements, 0, array, elements.length - start, size - elements.length + start);
        } else {
            System.arraycopy(elements, start, array, 0, size);
        }                
    }

    @Override
    Queue emptyQueue() {
        return new ArrayQueue();
    }
}