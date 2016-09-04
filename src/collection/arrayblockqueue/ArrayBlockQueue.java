package collection.arrayblockqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * add Inserts the specified element at the tail of this queue if it is possible to do so immediately without
 * exceeding the queue's capacity, returning true upon success and throwing an IllegalStateException if this
 * queue is full.
 *
 * {@link java.util.AbstractQueue#add(Object)}
 * public boolean add(E e) {
 *    if (offer(e))
 *    return true;
 *    else
 *    throw new IllegalStateException("Queue full");
 *   }
 *
 * we should explicitly wrap add() with try-catch, if queue is full, add() will fail, and element
 * will be ignored.
 *
 * put() will wait until queue is not full.
 * add() won't be blocked
 *
 *
 * Created by frank lee on 2016/8/11.
 * Email: frankleecsz@gmail.com
 */

class Producer implements Runnable{

    private BlockingQueue<Character> queue;
    private boolean run = true;

    private char c = 97;

    public Producer(BlockingQueue<Character> queue) {
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
                boolean result = false;
                try{
                    result = queue.add(ch);
                } catch(IllegalStateException e){
                    System.out.println("full");
                }
                System.out.println(result);
                if(c == 123)
                    run = false;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable{

    private BlockingQueue<Character> queue;

    public Consumer(BlockingQueue<Character> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true){
            try {
                consume(queue.take());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void consume(Character ch){
        System.out.println("consume "+ch);
    }
}

public class ArrayBlockQueue{

    public static void main(String[] args) {
        BlockingQueue<Character> queue = new ArrayBlockingQueue<>(1);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.submit(new Producer(queue));
        exec.submit(new Consumer(queue));
        exec.shutdown();
    }

}
