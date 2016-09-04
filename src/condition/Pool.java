package condition;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by frank lee on 2016/8/11.
 * Email: frankleecsz@gmail.com
 */
class Pool {

    private Queue<Resource> cache;
    private int current = 0;     //current true resource
    private int size;     //total
    private Lock lock;
    private final Object proLock = new Object();
    private final Object conLock = new Object();
    private Condition producer;
    private Condition consumer;
    private int proPlan = 0;
    private int conPlan = 0;


    Pool(int size, Lock lock, Condition producer, Condition consumer){
        cache = new ArrayDeque<>(size);
        this.size = size;
        this.lock = lock;
        this.producer = producer;
        this.consumer = consumer;
    }

    Resource add(Resource res, int producerId){
        lock.lock();
        try {
            if(current >= size) {
                System.out.println("pool is full, wait...");
                producer.await();
            }
            if(proReserve()){
                System.out.println("producer-"+producerId+" produces resource:"+res.hashCode());
                cache.add(res);
                current++;
                if(proPlan > 0)
                    proPlan--;
                System.out.println("in add:"+(current)+"   "+cache.size());
                consumer.signalAll();
                return null;
            }
            else         //if fail to reserve place in cache, return resource and next time add it
                return res;
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    /**
     * after consume, consumers signalAll producers,so next time, many producers will
     * produce even if pool only lacks one resource.
     * so every time produce resource, producer needs to reserve a place in pool.
     * if fail, producer won't produce.
     * @return  permission to produce
     */
    boolean proReserve(){
        synchronized (proLock){
            proPlan++;
            if(proPlan+current>size){
                proPlan--;
                return false;
            }
            return true;
        }
    }

    boolean conReserve(){
        synchronized (conLock){
            conPlan++;
            if(current-conPlan>=0)
                return true;
            else{
                conPlan--;
                return false;
            }
        }
    }



    Resource poll(){
        lock.lock();
        try{
            if(current <= 0) {
                System.out.println("pool is empty, wait...");
                consumer.await();
            }
            if(conReserve()){
                current--;
                Resource res = cache.poll();
                if(conPlan>0)
                    conPlan--;
                System.out.println("in poll:"+(current)+"  "+cache.size());
                producer.signalAll();
                return res;
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }
}
