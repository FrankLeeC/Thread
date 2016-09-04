package plain.procon;

/**
 * producer and consumer, use plain {@link Object#wait()} and {@link Object#notifyAll()}
 * one producer and one consumer
 * Created by frank lee on 2016/8/11.
 * Email: frankleecsz@gmail.com
 */
class Test {

    public static void main(String[] args) {
        Object producer = new Object();
        Object consumer = new Object();
        Pool pool = new Pool(10);
        Thread tp = new Thread(new Producer(pool,producer,consumer));
        tp.start();
        Thread tc = new Thread(new Consumer(pool,producer,consumer));
        tc.start();
    }
}
