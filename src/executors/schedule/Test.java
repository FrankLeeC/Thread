package executors.schedule;

import java.util.concurrent.*;

/**
 * execute timer
 * Created by frank lee on 2016/8/13.
 * Email: frankleecsz@gmail.com
 */
public class Test {

    public static void main(String[] args) {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
        Runnable beep = new Runnable() {
            int i = 0;
            @Override
            public void run() {
                System.out.println("beep..."+(++i));
                try {
                    Thread.sleep(20*1000);        //longer than period
                } catch (InterruptedException e) {
                    System.out.println("beep interrupted......");
                }
            }
        };
        Runnable moo = new Runnable() {
            int i = 0;
            @Override
            public void run() {
                System.out.println("moo..."+(++i));
            }
        };

        /**
         * from API:
         * that is executions will commence after initialDelay then initialDelay+period,
         * then initialDelay + 2 * period, and so on.
         *
         * from source code:
         * If any execution of the task
         * encounters an exception, subsequent executions are suppressed.
         * Otherwise, the task will only terminate via cancellation or
         * termination of the executor.  If any execution of this task
         * takes longer than its period, then subsequent executions
         * may start late, but will not concurrently execute.
         */
        ScheduledFuture beepFuture = ses.scheduleAtFixedRate(beep,5,10, TimeUnit.SECONDS);
        ScheduledFuture mooFuture = ses.scheduleAtFixedRate(moo,6,3,TimeUnit.SECONDS);

        ses.schedule(new Runnable() {
            @Override
            public void run() {
                beepFuture.cancel(true);
                System.out.println("beep cancle");
            }
        },1,TimeUnit.MINUTES);


        /**
         * //Zero and negative delays (but not periods) are also allowed in schedule methods,
         * and are treated as requests for immediate execution.
         *
         * so, if delay is <=0, then ses will shutdown immediately after start
         */
        ses.schedule(new Runnable() {
            @Override
            public void run() {
                mooFuture.cancel(true);
                System.out.println("moo cancle");
                ses.shutdownNow();
            }
        },2,TimeUnit.MINUTES);


    }

}
