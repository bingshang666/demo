package queue;

/**
 * Created by eefijjt on 3/16/2018.
 */
public class MyProducer implements Runnable {
    private MyBlockingQueue queue;
    private boolean status;

    public MyProducer(MyBlockingQueue queue){
        this.queue = queue;
        status = true;
    }

    public void run() {
        while (status){
            this.queue.put(new Object());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
