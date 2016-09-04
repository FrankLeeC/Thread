package condition;

/**
 * Created by frank lee on 2016/8/11.
 * Email: frankleecsz@gmail.com
 */
public class Producer implements Runnable {

    private Pool pool;
    private int id;
    private Resource back = null;

    public Producer(Pool pool, int id) {

        this.pool = pool;
        this.id = id;
    }

    @Override
    public void run() {
        while(true) {
            if(back == null)
                produce();
            else
                reAdd();
        }
    }

    private void produce(){
        try {
            Thread.sleep(1000);     //simulate produce
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Resource res = new Resource();
        back = pool.add(res,id);
    }

    private void reAdd(){
        System.out.println("reAdd:"+back.hashCode());
        back = pool.add(back,id);
    }
}
