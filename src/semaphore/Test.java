package semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * semaphore
 * Created by frank lee on 2016/8/12.
 * Email: frankleecsz@gmail.com
 */

class Person implements Runnable{
    private Semaphore semaphore;
    private int id;

    public Person(Semaphore semaphore, int id) {
        this.semaphore = semaphore;
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("person-"+id+" is competing for a key");
        try {
            semaphore.acquire();
            System.out.println("person-"+id+" get a key");
            Thread.sleep(2000);
            semaphore.release();
            System.out.println("person-"+id+" release a key");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
public class Test {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0;i<8;i++)
            exec.submit(new Person(semaphore,i));
        exec.shutdown();
    }
}
