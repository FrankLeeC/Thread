package atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * How does AtomicInteger works:
 *
 * public final int getAndSetInt(Object var1, long var2, int var4) {
 *    int var5;
 *    do {
 *    var5 = this.getIntVolatile(var1, var2);
 *    } while(!this.compareAndSwapInt(var1, var2, var5, var4));
 *    return var5;
 *   }
 *
 *
 * Created by frank lee on 2016/8/14 13:29.
 * Email: frankleecsz@gmail.com
 */

class Atomic implements Runnable{

    private int id;
    private AtomicInteger i;

    public Atomic(int id, AtomicInteger i) {
        this.id = id;
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println("atomic-"+id+" result="+i.getAndIncrement());
    }
}
public class AtomicInt {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger integer = new AtomicInteger(0);
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 1000; i++) {
            exec.submit(new Atomic(i,integer));
        }
        exec.shutdown();
        Thread.sleep(1000);
        System.out.println("atomic value:"+integer.get());
    }
}
