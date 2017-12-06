package workflowsuite.kpi.client;

import java.lang.reflect.Array;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

final class CircleBuffer<T> {
    private final Logger logger;
    private int takeIndex;
    private int putIndex;
    private int count;
    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final T[] buffer;

    CircleBuffer(Class<T> c, int capacity, ILoggerFactory loggerFactory) {
        if (!isPow2(capacity)) {
            throw new IllegalArgumentException("capacity should be pow of 2");
        }
        this.logger = loggerFactory.getLogger(this.getClass().getName());
        // Use Array native method to create array
        // of a type only known at run time
        this.buffer = (T[]) Array.newInstance(c, capacity);
        this.takeIndex = 0;
        this.putIndex = 0;
        this.count = 0;
        this.lock = new ReentrantLock(false);
        this.notEmpty = this.lock.newCondition();
    }

    public boolean offer(final T message) {
        this.logger.debug("Entering offer()");
        final ReentrantLock sync = this.lock;
        sync.lock();
        try {
            final Object[] items = this.buffer;
            if (items[this.putIndex] != null) {
                this.takeIndex = inc(this.takeIndex);
                this.count = this.buffer.length;
            } else {
                count++;
            }
            items[this.putIndex] = message;
            this.putIndex = inc(this.putIndex);

            notEmpty.signal();
        } finally {
            sync.unlock();
        }

        this.logger.debug("Leaving offer()");
        return true;
    }

    public T poll() throws InterruptedException {
        this.logger.debug("Entering poll()");
        final ReentrantLock sync = this.lock;
        sync.lockInterruptibly();
        try {
            while (this.count == 0) {
                this.logger.debug("Waiting message...");
                notEmpty.await();
            }
            T message = this.buffer[this.takeIndex];
            this.logger.debug("Leaving poll()");
            return message;
        } finally {
            sync.unlock();
        }

    }

    public void remove(final T message) {
        this.logger.debug("Entering remove()");
        final ReentrantLock sync = this.lock;
        sync.lock();
        try {
            final T[] items = this.buffer;
            // if not equal - another thread overwrite message
            if (items[this.takeIndex] == message) {
                items[this.takeIndex] = null;
                this.takeIndex = inc(this.takeIndex);
                count--;
            }
        } finally {
            sync.unlock();
        }
        this.logger.debug("Leaving remove()");
    }

    public int size() {
        this.logger.debug("Entering size()");
        final ReentrantLock sync = this.lock;
        sync.lock();
        try {
            this.logger.debug("Leaving size():{}", count);
            return count;
        } finally {
            sync.unlock();
        }
    }

    private int inc(final int i) {
        int j = i + 1;
        return (j == this.buffer.length) ? 0 : j;
    }

    private static boolean isPow2(final int x) {
        return (x != 0) && ((x & (~x + 1)) == x);
    }
}
