package queue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by eefijjt on 3/16/2018.
 */
public class MyBlockingQueue {

    private Object[] items;
    private final int compacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    private AtomicInteger count = new AtomicInteger(0);

    public MyBlockingQueue(int size){
        this.items = new Object[size];
        this.compacity = size;
    }

    public boolean put(Object item) {
        try{
            this.lock.lock();
            if (count.get() == this.compacity - 1){
                System.out.println("queue is full, waiting...");
                notFull.await();
            }
            this.items[count.get()] = item;
            count.getAndIncrement();
            if (count.get() >= 1){
                this.notEmpty.signal();
            }
            System.out.println("["+Thread.currentThread().getName()+"] put item into queue, and queue size is "+ this.count.get());
            return true;
        } catch (InterruptedException e) {
            System.out.println("found error in put "+ e);
            return false;
        }finally {
            this.lock.unlock();
        }
    }

    public Object take(){
        Object item;
        try{
            this.lock.lock();
            if (count.get() == 0){
                notEmpty.await();
            }
            item = items[count.getAndDecrement()];
            if (count.get() <= this.compacity - 1){
                notFull.signal();
            }
            System.out.println("["+Thread.currentThread().getName()+"] take item from queue, and queue size is "+ this.count.get());
            return item;
        } catch (InterruptedException e) {
            System.out.println("found error in take "+ e);
            return null;
        }finally {
            this.lock.unlock();
        }

    }
}
