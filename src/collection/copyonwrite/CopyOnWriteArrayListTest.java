package collection.copyonwrite;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A thread-safe variant of ArrayList in which all mutative operations (add, set, and so on) are implemented
 * by making a fresh copy of the underlying array.
 *
 * every write needs lock
 * reader needn't
 *
 * before write, acquire lock, copy old data and add new data, then change reference point to new data, then release lock
 *
 * Created by frank lee on 2016/7/27.
 * Email: frankleecsz@gmail.com
 */
class Writer implements Runnable{

    private CopyOnWriteArrayList<Integer> copyOnWriteArrayList;
    private int id;

    public Writer(CopyOnWriteArrayList<Integer> copyOnWriteArrayList, int id) {
        this.copyOnWriteArrayList = copyOnWriteArrayList;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1100);
            System.out.println("writer-"+id+" writes "+id*100);
            copyOnWriteArrayList.add(id*100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Reader implements Runnable{

    private CopyOnWriteArrayList<Integer> copyOnWriteArrayList;
    private int id;

    public Reader(CopyOnWriteArrayList<Integer> copyOnWriteArrayList, int id) {
        this.copyOnWriteArrayList = copyOnWriteArrayList;
        this.id = id;
    }

    @Override
    public void run() {
        Iterator<Integer> iterator = copyOnWriteArrayList.iterator();
        while(iterator.hasNext()){
            System.out.println("reader-"+id+" read " + iterator.next());
        }
    }
}
public class CopyOnWriteArrayListTest {

    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add(1234);
        copyOnWriteArrayList.add(5678);
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0;i<5;i++) {
            exec.submit(new Writer(copyOnWriteArrayList, i));
            exec.submit(new Reader(copyOnWriteArrayList,i));
        }
        System.out.println("=================");
        for(int i=5;i<10;i++){
            try {
                Thread.sleep(1000-i*100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            exec.submit(new Reader(copyOnWriteArrayList,i));
        }
        exec.shutdown();
    }
}
