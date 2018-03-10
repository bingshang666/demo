package healthChecker.impl;

import healthChecker.BaseHealthCHecker;

import java.util.concurrent.CountDownLatch;

public class DatabaseHealthChecker extends BaseHealthCHecker {
    public DatabaseHealthChecker(CountDownLatch latch) {
        super("Database Service", latch);
    }

    public void verifyService() throws Exception {
        System.out.println("Checking "+ this.getServiceName());
        try {
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(this.getServiceName() + "is UP");
    }
}
