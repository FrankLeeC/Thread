package condition;

/**
 * Created by frank lee on 2016/8/11.
 * Email: frankleecsz@gmail.com
 */
public class Consumer implements Runnable {

    private Pool pool;
    private int id;

    Consumer(Pool pool, int id) {
        this.pool = pool;
        this.id = id;
    }

    @Override
    public void run() {
        while(true){
            consume();
        }
    }

    private void consume(){
        Resource res = pool.poll();
        System.out.println("consumer-"+id+" consumes:"+res.hashCode());
        try {
            Thread.sleep(2*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
