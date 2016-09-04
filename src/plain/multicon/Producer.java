package plain.multicon;

/**
 * Created by frank lee on 2016/8/10.
 * Email: frankleecsz@gmail.com
 */
class Producer implements Runnable {

    private Pool pool;
    private Object producer;
    private Object consumer;
    private int id;

    Producer(Pool pool, Object producer, Object consumer, int id) {
        this.pool = pool;
        this.producer = producer;
        this.consumer = consumer;
        this.id = id;
    }

    @Override
    public void run() {
        while(true){
            while(pool.isFull()){
                try {
                    System.out.println("pool is full, producer-" + id + " wait");
                    synchronized (producer){      //need to lock producer
                        producer.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Resource res = produce();
            pool.add(res);
            System.out.println("producer-" + id + " produces a production:"+res.hashCode());
            synchronized (consumer) {             //need to lock consumer
                consumer.notifyAll();
            }
        }
    }

    private Resource produce(){
        try {
            Thread.sleep(3*1000);     //simulate produce
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Resource();
    }
}
