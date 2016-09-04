package condition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition
 * Created by frank lee on 2016/8/11.
 * Email: frankleecsz@gmail.com
 */
class Test {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock(true);
        Condition producer = lock.newCondition();
        Condition consumer = lock.newCondition();
        Pool pool = new Pool(10,lock,producer,consumer);
        ExecutorService pros = Executors.newCachedThreadPool();
        ExecutorService cons = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            pros.submit(new Producer(pool,i));
        }
        pros.shutdown();
        for (int i = 0; i < 3; i++) {
            cons.submit(new Consumer(pool,i));
        }
        cons.shutdown();
    }
}
