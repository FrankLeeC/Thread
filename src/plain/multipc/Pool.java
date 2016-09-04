package plain.multipc;


import java.util.ArrayDeque;
import java.util.Queue;


/**
 * Created by frank lee on 2016/8/11.
 * Email: frankleecsz@gmail.com
 */
class Pool {

    private Queue<Resource> cache;
    private int current = 0;     //current true resource
    private int size;     //total
    private int proPlan = 0;      //producer reserve place in cache
    private int conPlan = 0;      //consumer reserve resource
    private final Object proLock = new Object();
    private final Object conLock = new Object();


    Pool(int size){
        cache = new ArrayDeque<>(size);
        this.size = size;
    }

    synchronized void add(Resource res){
        cache.add(res);
        current++;
        if(proPlan>0)          //plan > 0, mean this resource corresponds to a plan
            proPlan--;
        System.out.println("in add:"+(current)+"   "+cache.size()+"  "+proPlan);
    }

    synchronized Resource poll(){
        current--;
        System.out.println("in poll:"+(current)+"  "+cache.size());
        if(conPlan>0)
            conPlan--;
        return cache.poll();
    }

    /**
     * return true,if not out of cache after reserving,
     * otherwise, return false
     * @return result
     */
    boolean proReserve(){
        synchronized (proLock){
            proPlan++;
            if(proPlan + current <= size){
//            System.out.println("plan + current ok :" + (plan + current));
                return true;
            }
            else{
                proPlan -= 1;
//            System.out.println("plan + current fail :" + (plan + current));
                return false;
            }
        }
    }

    boolean conReserve(){
        synchronized (conLock){
            conPlan++;
            if(current-conPlan<0){
                conPlan--;
                return false;
            }
            return true;
        }
    }



    synchronized boolean isFull() {
        return cache.size() >= size;
    }

    synchronized boolean isEmpty(){
        return cache.isEmpty();
    }
}
