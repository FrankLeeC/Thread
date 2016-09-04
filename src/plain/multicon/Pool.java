package plain.multicon;


import java.util.ArrayDeque;
import java.util.Queue;


/**
 * Created by frank lee on 2016/8/11.
 * Email: frankleecsz@gmail.com
 */
class Pool {

    private Queue<Resource> cache;
    private int count;
    private int plan = 0;
    private int current = 0;
    private final Object conLock = new Object();

    Pool(int count){
        cache = new ArrayDeque<>();
        this.count = count;
    }

    synchronized void add(Resource res){
        current++;
        cache.add(res);
    }

    synchronized Resource poll(){
        current--;
        if(plan>0)
            plan--;
        return cache.poll();
    }

    synchronized boolean isFull() {
        return cache.size() >= count;
    }

    synchronized boolean isEmpty(){
        return cache.isEmpty();
    }

    boolean reserve(){
        synchronized (conLock){
            plan++;
            if(current-plan>=0)
                return true;
            else{
                plan--;
                return false;
            }
        }
    }
}
