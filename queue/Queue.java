package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {
    // pre: true;
    void enqueue(Object element);
    // post: (queue` = [queue[0], ..., queue[size - 1], element], size` = 1 + size && size > 0) 
    //           || (queue` = [element], size` = 1);
    
    // pre: size > 0;
    Object dequeue();
    // post: R = queue[size - 1], size` = size - 1; (queue` = [queue[0], ..., queue[size` - 1]] && size` > 0) || queue` = [];

    // pre: size > 0;
    Object element();
    // post: R = queue[size - 1];

    // pre: true;
    int size();
    // post: R = size;

    // pre: true;
    boolean isEmpty();
    // post: R = size() == 0;

    // pre: true;
    void clear();
    // post: queue` = []; size`= 0;

    // pre: true;
    String toStr();
    // post: ((R = "[" + queue[0] + ", " + ... + queue[size - 1] + "]") && size > 1) 
    //     || (R = "[]" && size == 0) 
    //     || (R = "[" + queue[0] + "]" && size == 1);

    // pre: true;
    Object[] toArray();
    // post: ((R = [queue[0], ... , queue[size - 1]]) && size > 0) 
    //     || (R = [] && size == 0)
    
    // pre: filter != null
    Queue filter(Predicate<Object> predicate);
    // post: R = queue` && queue` = [queue[i(0)], queue[i(1)], ... , queue[i(m)]] : 
    //                  i(0) < i(1) < ... < i(m) and ∀ j ∈ [0, m] m < n : i(j) ∈ [0, n)
    //                  and ∀ i(j) : predicate.test(queue[i(j)]) == true;
    
    // pre: function != null
    Queue map(Function<Object, Object> function);
    // post: R = queue` && queue` = [function.apply(queue[0]), ... , function.apply(queue[n])]
}