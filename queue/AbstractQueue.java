package queue;

import java.util.function.Function;
import java.util.function.Predicate;

// :NOTE: .class files in repository!
public abstract class AbstractQueue implements Queue {
    protected int size;
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size() == 0;
    }

    // pre: true
    abstract void putInTail(Object element); 
    // post: same as post for enqueue()
    
    public void enqueue(final Object element) {
        putInTail(element);
        size++;
    }

    // pre: true
    abstract Object popHead();
    // post: same as post of dequeue()

    public Object dequeue() {
        assert size > 0;
        size--;
        return popHead();
    }
    
    // pre: true
    abstract Object getHead();
    // post: R = queue[0]

    public Object element() {
        assert size > 0;
        return getHead();
    }

    // pre: true
    abstract void cleaning();
    // post: starter parameters of queue
    
    public void clear() {
        size = 0;
        cleaning();
    }

    // pre: array.size == queue.size
    abstract void fillArray(Object[] array);
    // post: array = [queue[0], ..., queue[size - 1]]

    public Object[] toArray() {
        final Object[] result = new Object[size()];
        if (size != 0) {
            fillArray(result);
        }
        return result;
    }
    
    // pre: true
    abstract Queue emptyQueue();
    // post: R = empty queue

    public Queue filter(Predicate<Object> predicate) {
        assert predicate != null;
        return iterate(ob -> {return predicate.test(ob) ? ob : null;});
    }

    public Queue map(Function<Object, Object> function) {
        assert function != null;
        return iterate(function);
    }

    // pre: true
    private Queue iterate(Function<Object, Object> function) {
        Queue result = emptyQueue();
        for (Object value : toArray()) {
            Object tmp = function.apply(value);
            if (tmp != null) {
                result.enqueue(tmp);
            }   
        }
        return result;
    }
    // post: same as for map(function)


}