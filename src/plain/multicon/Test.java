package plain.multicon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * multiple consumer-one producer
 * Created by frank lee on 2016/8/11.
 * Email: frankleecsz@gmail.com
 */
class Test {

    public static void main(String[] args) {
        Object producer = new Object();
        Object consumer = new Object();
        Pool pool = new Pool(10);
        ExecutorService cons = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            cons.submit(new Consumer(pool,producer,consumer,i));
        }
        cons.shutdown();
        Thread pro = new Thread(new Producer(pool,producer,consumer,1));
        pro.start();
    }
}
