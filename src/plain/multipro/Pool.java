package plain.multipro;


import java.util.*;


/**
 * Created by frank lee on 2016/8/11.
 * Email: frankleecsz@gmail.com
 */
class Pool {

    private Queue<Resource> cache;
    private int current = 0;     //current true resource
    private int size;     //total
    private int plan = 0;      //producer reserve place in cache


    Pool(int size){
        cache = new ArrayDeque<>(size);
        this.size = size;
    }

    synchronized void add(Resource res){
        cache.add(res);
        current++;
        if(plan>0)          //plan > 0, mean this resource corresponds to a plan
            plan--;
        System.out.println("in add:"+(current)+"   "+cache.size()+"  "+plan);
    }

    synchronized Resource poll(){
        current--;
        System.out.println("in poll:"+(current)+"  "+cache.size());
        return cache.poll();
    }

    /**
     * return true,if not out of cache after reserving,
     * otherwise, return false
     * @return result
     */
    synchronized boolean reserve(){
        plan++;
        if(plan + current <= size){
//            System.out.println("plan + current ok :" + (plan + current));
            return true;
        }
        else{
            plan -= 1;
//            System.out.println("plan + current fail :" + (plan + current));
            return false;
        }
    }

    synchronized boolean isFull() {
        return cache.size() >= size;
    }

    synchronized boolean isEmpty(){
        return cache.isEmpty();
    }
}
