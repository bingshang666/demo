package queue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by eefijjt on 3/16/2018.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        MyBlockingQueue queue = new MyBlockingQueue(10);

        MyComsumer comsumer1 = new MyComsumer(queue);
        MyProducer producer1 = new MyProducer(queue);

        (new Thread(producer1)).start();
        Thread.sleep(300);
        (new Thread(comsumer1)).start();


        ArrayBlockingQueue q = new ArrayBlockingQueue(10);
    }
}
