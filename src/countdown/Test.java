package countdown;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A synchronization aid that allows one or more threads to wait until a set of operations being performed
 * in other threads completes.
 * Created by frank lee on 2016/8/12.
 * Email: frankleecsz@gmail.com
 */

class Driver implements Runnable{
    private CountDownLatch go;
    private CountDownLatch ready;
    Driver(CountDownLatch go,CountDownLatch ready){
        this.go = go;
        this.ready = ready;
    }

    @Override
    public void run() {
        System.out.println("driver is wait for passengers");
        try {
            ready.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        go.countDown();
        System.out.println("driver is driving");
    }
}

class Passenger implements Runnable{
    private CountDownLatch ready;
    private CountDownLatch go;
    private int id;

    Passenger(CountDownLatch ready, CountDownLatch go, int id) {
        this.ready = ready;
        this.go = go;
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("passenger-"+id+" get on");
        ready.countDown();
        try {
            go.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("passenger-"+id+" is on the way to home");
    }
}

public class Test {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch ready = new CountDownLatch(20);
        CountDownLatch go = new CountDownLatch(1);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.submit(new Driver(go,ready));
        Thread.sleep(1000);
        for(int i=0;i<20;i++){
            exec.submit(new Passenger(ready,go,i));
        }
        exec.shutdown();
    }
}

