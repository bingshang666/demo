package healthChecker;

import healthChecker.impl.DatabaseHealthChecker;
import healthChecker.impl.NetworkHealthChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ApplicationServicesStarter {

    private static List<BaseHealthCHecker> services;

    private static CountDownLatch latch;

    private static volatile ApplicationServicesStarter instance;

    public static ApplicationServicesStarter getInstance(){
        if (instance == null){
            instance = new ApplicationServicesStarter();
        }
        return instance;
    }

    public static boolean checkExternalServices() throws InterruptedException {
        latch = new CountDownLatch(2);
        services = new ArrayList<BaseHealthCHecker>();

        services.add(new NetworkHealthChecker(latch));
        services.add(new DatabaseHealthChecker(latch));

        Executor executor = Executors.newFixedThreadPool(services.size());
        for (final BaseHealthCHecker v : services){
            executor.execute(v);
        }
        long startTime = System.currentTimeMillis();

        System.out.println("begin to wait time");
        latch.await();
        System.out.println("start services in time "+ (System.currentTimeMillis()-startTime)/1000 + "s");


        for ( final BaseHealthCHecker v : services){
            if (!v.getServiceStatus()){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationServicesStarter.checkExternalServices();
    }

}
