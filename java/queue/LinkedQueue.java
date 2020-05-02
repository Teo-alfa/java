package queue;

public class LinkedQueue extends AbstractQueue {
    public static class Node {
        // :NOTE: are references mutable? where is `final` modifier?
        Node next;
        final Object object;
        public Node(final Object object) {
            next = null;
            this.object = object;
        }

        public String toString() {
            return object.toString();
        }
    }

    private Node start, end;
    // I: queue = (size > 2 && start -> ... -> end -> null)
    // || (size == 2 && start -> end -> null)
    // || (size == 1 && start -> null)
    // || (size == 0 && null = start = end)
    // && size >= 0
    // Здесь "->" обозначает, что объект имеет сслылку на следующий

    @Override
    protected void putInTail(final Object element) {
        if (size == 0) {
            start = new Node(element);
        } else {
            final Node tmp = new Node(element);
            if (size > 1) {
                end.next = tmp;
                end = tmp;
            } else {
                end = tmp;
                start.next = end;
            }
        }
    }

    @Override
    protected Object popHead() {
        final Node tmp = start;
        if (size == 0) {
            end = start = null;
        } else {
            start = start.next;
            if (size == 1) {
                start.next = end = null;
            }
        }
        return tmp.object;
    }

    @Override
    protected Object getHead() {
        return start.object;
    }

    @Override
    protected void cleaning() {
        start = end = null;
    }

    @Override
    public String toStr() {
        final StringBuilder str = new StringBuilder();
        Node tmp = start;
        str.append('[');
        if (tmp != null) {
            while (tmp.next != null) {
                str.append(tmp.object).append(", ");
                tmp = tmp.next;
            }
            str.append(tmp.object);
        }
        str.append(']');
        return str.toString();
    }

    @Override
    protected void fillArray(final Object[] array) {
        Node tmp = start;
        for (int i = 0; tmp!= null; i++) {
            array[i] = tmp.object;
            tmp = tmp.next;
        }
    }

    @Override
    Queue emptyQueue() {
        return new LinkedQueue();
    }

}