package plain.procon;

/**
 * Created by frank lee on 2016/8/10.
 * Email: frankleecsz@gmail.com
 */
class Consumer implements Runnable {

    private Pool pool;
    private Object producer;
    private Object consumer;

    Consumer(Pool pool, Object producer, Object consumer) {
        this.pool = pool;
        this.producer = producer;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        while(true){
            if (pool.isEmpty()){
                try {
                    System.out.println("pool is empty, consumer wait");
                    synchronized (consumer) {            //need to lock consumer
                        consumer.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Resource res = consume();
            System.out.println("consumer consumes a production:"+res.hashCode());
            synchronized (producer) {                   //need to lock producer
                producer.notifyAll();
            }
        }
    }

    private Resource consume(){
        try {
            Thread.sleep(2*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pool.poll();
    }
}
