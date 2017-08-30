package workflowsuite.kpi.client;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

final class KpiMessageBuffer {

    private final KpiMessage[] buffer;
    private int takeIndex;
    private int putIndex;
    private int count;
    private final ReentrantLock lock;
    private final Condition notEmpty;


    KpiMessageBuffer(int capacity) {
        if (!isPow2(capacity)) {
            throw new IllegalArgumentException("capacity should be pow of 2");
        }
        this.buffer = new KpiMessage[capacity];
        this.takeIndex = 0;
        this.putIndex = 0;
        this.count = 0;
        this.lock = new ReentrantLock(false);
        this.notEmpty = this.lock.newCondition();
    }

    public boolean offer(KpiMessage message) {

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


        return true;
    }

    public KpiMessage poll() throws InterruptedException {
        final ReentrantLock sync = this.lock;
        sync.lockInterruptibly();
        try {
            while (this.count == 0) {
                notEmpty.await();
            }
            return this.buffer[this.takeIndex];
        } finally {
            sync.unlock();
        }
    }

    public void remove(KpiMessage message) {
        final ReentrantLock sync = this.lock;
        sync.lock();
        try {
            final KpiMessage[] items = this.buffer;
            // if not equal - another thread ovewrite message
            if (items[this.takeIndex] == message) {
                items[this.takeIndex] = null;
                this.takeIndex = inc(this.takeIndex);
                count--;
            }
        } finally {
            sync.unlock();
        }
    }

    public int size() {
        final ReentrantLock sync = this.lock;
        sync.lock();
        try {
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
