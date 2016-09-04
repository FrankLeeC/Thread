package collection.arrayblockqueue;

import java.util.concurrent.*;

/**
 * poll(long timeout, TimeUnit unit)
 *
 * Created by frank lee on 2016/7/27.
 * Email: frankleecsz@gmail.com
 */

class Producer2 implements Runnable{

    private BlockingQueue<Character> queue;
    private boolean run = true;

    private char c = 97;

    public Producer2(BlockingQueue<Character> queue) {
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer2 implements Runnable{

    private BlockingQueue<Character> queue;

    public Consumer2(BlockingQueue<Character> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true){
            try {
                /**
                 * Retrieves and removes the head of this queue, waiting up to the specified wait time if necessary for an
                 * element to become available.
                 */
                consume(queue.poll(10, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void consume(Character ch){
        System.out.println("consume "+ch);
    }
}

public class ArrayBlockingQueue2{

    public static void main(String[] args) {
        BlockingQueue<Character> queue = new ArrayBlockingQueue<>(1);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.submit(new Producer2(queue));
        exec.submit(new Consumer2(queue));
        exec.shutdown();
    }

}