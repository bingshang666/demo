package queue;

/**
 * Created by eefijjt on 3/16/2018.
 */
public class MyComsumer implements Runnable {

    private MyBlockingQueue queue;
    private boolean status;

    public MyComsumer(MyBlockingQueue queue){
        this.queue = queue;
        status = true;
    }

    public void run() {
        while (status){
            this.queue.take();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
