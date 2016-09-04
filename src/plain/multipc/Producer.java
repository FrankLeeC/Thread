package plain.multipc;

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
            /*
            after produce, some producer don't go to next loop(due to schedule of system),so it don't block.
            so next time, many producers will produce even if pool only lacks one resource.
            so every time produce resource, producer needs to reserve a place in pool.
            if fail, producer won't produce.
             */
            boolean result = pool.proReserve();
            if(result){
                Resource res = produce();
                pool.add(res);
                System.out.println("producer-" + id + " produces a production"+res.hashCode());
                synchronized (consumer) {             //need to lock consumer
                    consumer.notifyAll();
                }
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
