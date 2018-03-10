package healthChecker.impl;

import healthChecker.BaseHealthCHecker;

import java.util.concurrent.CountDownLatch;

public class NetworkHealthChecker extends BaseHealthCHecker {

    public NetworkHealthChecker(CountDownLatch latch) {
        super("Network Service", latch);
    }

    public void verifyService() throws Exception {
        System.out.println("Checking "+ this.getServiceName());
        try {
            Thread.sleep(3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(this.getServiceName() + "is UP");
    }
}
