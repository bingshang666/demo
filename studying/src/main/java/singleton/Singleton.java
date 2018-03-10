package singleton;

/**
 * This is special singleton
 *
 * 1. why need double check singleton
 *      multi-thread exception
 * 2. why use volatile in instance
 *      指令重排
 *
 */
public class Singleton {
    private static volatile Singleton instance;

    private Singleton(){}

    public static Singleton getInstance(){
        if (instance == null){
            synchronized (Singleton.class){
                if (instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

}
