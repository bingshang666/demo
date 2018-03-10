package healthChecker;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseHealthCHecker implements Runnable {

    private CountDownLatch latch;
    private String serviceName;
    private AtomicBoolean serviceStatus;


    public BaseHealthCHecker(String serviceName, CountDownLatch latch){
        this.serviceName = serviceName;
        this.latch = latch;
        this.serviceStatus = new AtomicBoolean(false);
    }

    public void run() {
        try{
            this.verifyService();
            this.serviceStatus.compareAndSet(true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (this.latch != null){
                this.latch.countDown();
            }
        }

    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean getServiceStatus() {
        return serviceStatus.get();
    }

    public abstract void verifyService() throws Exception;
}
