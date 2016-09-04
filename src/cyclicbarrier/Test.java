package cyclicbarrier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * CyclicBarrier can be reused
 * CountDownLatch can not be reused
 * Created by frank lee on 2016/8/12.
 * Email: frankleecsz@gmail.com
 */

class Count implements Runnable{

    private int[] counts;
    private int id;
    private CyclicBarrier barrier;
    private final List<Integer> list;

    Count(int[] counts, int id, CyclicBarrier barrier, List<Integer> list) {
        this.counts = counts;
        this.id = id;
        this.barrier = barrier;
        this.list = list;
    }

    @Override
    public void run() {
        System.out.println("count-"+id+" is counting...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int sum = 0;
        for(int i : counts){
            sum += i;
        }
        System.out.println("count-"+id+" finishes, result is:"+sum+", and count-"+id+" is wait...");
        synchronized (list){
            list.add(sum);
        }
        try {
            barrier.await();
            System.out.println("all count finish");
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("after await");
    }
}

class Wait implements Runnable{

    private int[] counts;
    private int id;
    private CyclicBarrier barrier;
    private final List<Integer> list;

    Wait(int[] counts, int id, CyclicBarrier barrier, List<Integer> list) {
        this.counts = counts;
        this.id = id;
        this.barrier = barrier;
        this.list = list;
    }

    @Override
    public void run() {
        System.out.println("wait count-"+id+" is counting...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int sum = 0;
        for(int i : counts){
            sum += i;
        }
        System.out.println("wait count-"+id+" finishes, result is:"+sum+", and wait count-"+id+" is wait...");
        synchronized (list){
            list.add(sum);
        }
        //the first task throw TimeOutException, following tasks will throw BrokenBarrierException instead of TimeOutException
        try {
            barrier.await(1000,TimeUnit.MILLISECONDS);
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("wait count-"+id+" is broken");
        } catch (TimeoutException e) {
            System.out.println("wait count-"+id+" is out of time");
        }
        System.out.println("after await");
    }
}

public class Test {

    public static void main(String[] args) {
        final List<Integer> list = new ArrayList<>();
        CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                int sum = 0;
                for(Integer i:list){
                    sum += i;
                }
                System.out.println("sum is:"+sum);
            }
        });
        int[][] count = new int[][]{
                {1,2,3},
                {4,5,6},
                {7,8,9}
        };
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0;i<3;i++){
            exec.submit(new Count(count[i],i,barrier,list));
        }
        exec.shutdown();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("=========reuse barrier=========");
        ExecutorService exec2 = Executors.newCachedThreadPool();
        for(int i=3;i<6;i++){
            exec2.submit(new Count(count[i-3],i,barrier,list));
        }
        exec2.shutdown();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("=========time out=========");
        List<Integer> waitList = new ArrayList<>();
        CyclicBarrier waitBarrier = new CyclicBarrier(3, new Runnable() {   //this task won't be executed due to broken
            @Override
            public void run() {
                int sum = 0;
                for(Integer i:waitList){
                    sum += i;
                }
                System.out.println("wait sum is:"+sum);
            }
        });
        ExecutorService exec3 = Executors.newCachedThreadPool();
        for(int i=0;i<2;i++){
            exec3.submit(new Wait(count[i],i,barrier,waitList));
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exec3.submit(new Wait(count[2],2,barrier,waitList));
        exec3.shutdown();
    }
}
