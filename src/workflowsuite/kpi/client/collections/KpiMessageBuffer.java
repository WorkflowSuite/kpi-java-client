package workflowsuite.kpi.client.collections;

import org.jetbrains.annotations.Contract;
import workflowsuite.kpi.client.KpiMessage;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public final class KpiMessageBuffer {

    private final KpiMessage[] buffer;
    private int takeIndex;
    private int putIndex;
    private int count;
    private final ReentrantLock lock;
    private final Condition notEmpty;


    public KpiMessageBuffer(int capacity) {
        if (!isPow2(capacity))
            throw new IllegalArgumentException("capacity sould be pow of 2");
        this.buffer = new KpiMessage[capacity];
        this.takeIndex = 0;
        this.putIndex = 0;
        this.count = 0;
        this.lock = new ReentrantLock(false);
        this.notEmpty = this.lock.newCondition();
    }

    public boolean offer(KpiMessage message) {

        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final Object[] items = this.buffer;
            if (items[this.putIndex] != null) {
                this.takeIndex = inc(this.takeIndex);
                this.count = this.buffer.length;
            }
            else {
                count++;
            }
            items[this.putIndex] = message;
            this.putIndex = inc(this.putIndex);

            notEmpty.signal();
        } finally {
            lock.unlock();
        }


        return true;
    }

    public KpiMessage take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (this.count == 0)
                notEmpty.await();
            final KpiMessage[] items = this.buffer;
            KpiMessage message = items[this.takeIndex];
            items[this.takeIndex] = null;
            this.takeIndex = inc(this.takeIndex);
            count--;
            return message;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    @Contract(pure = true)
    private int inc(int i) {
        i++;
        return ((i == this.buffer.length) ? 0 : i);
    }

    @Contract(pure = true)
    private static boolean isPow2(int x) {
        return ((x != 0) && ((x & (~x + 1)) == x));
    }
}
