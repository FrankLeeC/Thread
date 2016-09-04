package plain.multipro;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * multiple producer-one consumer
 * Created by frank lee on 2016/8/11.
 * Email: frankleecsz@gmail.com
 */
class Test {

    public static void main(String[] args) {
        Object producer = new Object();
        Object consumer = new Object();
        Pool pool = new Pool(10);
        ExecutorService pros = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            pros.submit(new Producer(pool,producer,consumer,i));
        }
        pros.shutdown();
        Thread con = new Thread(new Consumer(pool,producer,consumer,1));
        con.start();
    }
}
