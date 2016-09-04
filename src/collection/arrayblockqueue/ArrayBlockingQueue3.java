package collection.arrayblockqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * drainTo(Collection<? super E> c)
 * Removes all available elements from this queue and adds them to the given collection
 *
 *  drainTo(Collection<? super E> c, int maxElements)
 *  Removes at most the given number of available elements from this queue and adds them to the given collection.
 *
 * Created by frank lee on 2016/7/27.
 * Email: frankleecsz@gmail.com
 */

class Producer3 implements Runnable{

    private BlockingQueue<Character> queue;
    private boolean run = true;

    private char c = 97;

    public Producer3(BlockingQueue<Character> queue) {
        this.queue = queue;
    }

    private Character produce(){
        Character ch = c++;
        System.out.println("produce "+ch);
        return ch;
    }

    @Override
    public void run() {
        while(run){
            try {
                Character ch = produce();
                queue.put(ch);
                if(c == 123)
                    run = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer3 implements Runnable{

    private BlockingQueue<Character> queue;
    private List<Character> list = new ArrayList<>();

    public Consumer3(BlockingQueue<Character> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
                int count = queue.drainTo(list,5);
                System.out.println("count:"+count+" "+list.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void consume(Character ch){
        System.out.println("consume "+ch);
    }
}
public class ArrayBlockingQueue3 {

    public static void main(String[] args) {
        BlockingQueue<Character> queue = new ArrayBlockingQueue<>(27);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.submit(new Producer3(queue));
        exec.submit(new Consumer3(queue));
        exec.shutdown();
    }

}
