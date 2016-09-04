package collection.concurrentlinkedqueue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by frank lee on 2016/8/14.
 * Email: frankleecsz@gmail.com
 */

class Reader implements Runnable{

    private ConcurrentLinkedQueue<Character> queue;
    private int id;

    public Reader(ConcurrentLinkedQueue<Character> queue, int id) {
        this.queue = queue;
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("reader-"+id+" read "+queue.poll());
    }
}

public class Test {

    public static void main(String[] args) {
        ConcurrentLinkedQueue<Character> queue = new ConcurrentLinkedQueue<>();
        char a = 97;
        for(int i=0;i<26;i++)
            queue.add((char) (a+i));
        ExecutorService exec = Executors.newFixedThreadPool(26);
        for (int i = 0; i < 100; i++) {
            exec.submit(new Reader(queue,i));
        }
        exec.shutdown();
    }
}
