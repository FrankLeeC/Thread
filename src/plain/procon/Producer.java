package plain.procon;

/**
 * Created by frank lee on 2016/8/10.
 * Email: frankleecsz@gmail.com
 */
class Producer implements Runnable {

    private Pool pool;
    private Object producer;
    private Object consumer;

    Producer(Pool pool, Object producer, Object consumer) {
        this.pool = pool;
        this.producer = producer;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        while(true){
            if(pool.isFull()){
                try {
                    System.out.println("pool is full, producer wait");
                    synchronized (producer){      //need to lock producer
                        producer.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Resource res = produce();
            pool.add(res);
            System.out.println("producer produces a production"+res.hashCode());
            synchronized (consumer) {             //need to lock consumer
                consumer.notifyAll();
            }
        }
    }

    private Resource produce(){
        try {
            Thread.sleep(1000);     //simulate produce
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Resource();
    }
}
