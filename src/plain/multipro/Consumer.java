package plain.multipro;

/**
 * Created by frank lee on 2016/8/10.
 * Email: frankleecsz@gmail.com
 */
class Consumer implements Runnable {

    private Pool pool;
    private Object producer;
    private Object consumer;
    private int id;

    Consumer(Pool pool, Object producer, Object consumer, int id) {
        this.pool = pool;
        this.producer = producer;
        this.consumer = consumer;
        this.id = id;
    }

    @Override
    public void run() {
        while(true){
            while (pool.isEmpty()){
                try {
                    System.out.println("pool is empty, consumer-" + id + " wait");
                    synchronized (consumer) {            //need to lock consumer
                        consumer.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Resource res = consume();
            System.out.println("consumer-" + id + " consumes a production:"+res.hashCode());
            synchronized (producer) {                   //need to lock producer
                producer.notify();  //Wakes up a single thread that is waiting on this object's monitor. If any threads are waiting on this object, one of them is chosen to be awakened.
            }
        }
    }

    private Resource consume(){
        //if sleep before pool, producer will produce, after producer blocked, consumer will consume, so result is not clear, sleep after pool, result is clear
        Resource res = pool.poll();
        try {
            Thread.sleep(2*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }
}
