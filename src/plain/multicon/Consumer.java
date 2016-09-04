package plain.multicon;

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
            while (pool.isEmpty()){ //use while, not if, after activated, pool may be empty again, so must wait again
                try {
                    System.out.println("pool is empty, consumer-" + id + " wait");
                    synchronized (consumer) {            //need to lock consumer
                        consumer.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(pool.reserve()){
                Resource res = consume();
                System.out.println("consumer-" + id + " consumes a production:"+res.hashCode());
                synchronized (producer) {                   //need to lock producer
                    producer.notifyAll();
                }
            }
        }
    }

    private Resource consume(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pool.poll();
    }
}
