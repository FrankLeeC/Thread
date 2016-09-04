package plain.procon;

import java.util.*;


/**
 * Created by frank lee on 2016/8/10.
 * Email: frankleecsz@gmail.com
 */
class Pool {

    private Queue<Resource> cache;
    private int count;

    Pool(int count){
        cache = new ArrayDeque<>();
        this.count = count;
    }

    synchronized void add(Resource res){
        cache.add(res);
    }

    synchronized Resource poll(){
        return cache.poll();
    }

    synchronized boolean isFull() {
        return cache.size() >= count;
    }

    synchronized boolean isEmpty(){
        return cache.isEmpty();
    }
}
