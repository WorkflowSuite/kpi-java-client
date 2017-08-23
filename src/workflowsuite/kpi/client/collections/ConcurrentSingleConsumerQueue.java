package workflowsuite.kpi.client.collections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class ConcurrentSingleConsumerQueue<T> {

    private final AtomicReferenceArray<T> _items;
    private final int _size;

    private final AtomicLong _tail;
    private final AtomicLong _head;

    private final int _mask;

    Node<T> _headNode;
    final AtomicReference<Node<T>> _tailNode;

    public ConcurrentSingleConsumerQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }

        _size = capacity;
        _items = new AtomicReferenceArray(capacity);
        _mask = capacity - 1;
        _head = new AtomicLong();
        _tail = new AtomicLong();

        _headNode = new Node(null);
        _tailNode = new AtomicReference(_headNode);
    }

    public boolean offer(T item) {
        if (item == null) {
            throw new NullPointerException();
        }
        long t = _tail.getAndIncrement();
        long h = _head.get();
        if ((t - h) < _items.length()) {
            int index = (int) (t & _mask);
            _items.lazySet(index, item);
        } else {
            Node<T> node = new Node<T>(item);
            _tailNode.getAndSet(node).lazySet(node);
        }
        return true;
    }

    public T poll() {

        long h = _head.get();
        long t = _tail.get();
        if (h == t) {
            return null;
        }
        int index = (int) h & _mask;
        T e = _items.get(index);
        if (e == null) {
            Node<T> next = _headNode.get();
            _headNode = next;
            e = next.value;
        } else {
            _items.lazySet(index, null);
        }
        _head.lazySet(h + 1);
        return e;
    }

    static final class Node<T> extends AtomicReference<Node<T>> {
        private static final long serialVersionUID = 1L;

        final T value;

        Node(T value) {
            this.value = value;
        }
    }
}
