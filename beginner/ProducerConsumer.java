// Online Java Compiler
// Use this editor to write, compile and run your Java code online
import java.util.Queue;
import java.util.concurrent.*;

class ProducerConsumer {
    static final Queue<String> q = new ConcurrentLinkedQueue<String>();
    public static void main(String[] args) {
        Callable<Boolean> producer = () -> {
            for (int i=0;i<=100;i++) {
                q.add("event: "+i);
                Thread.sleep(2);
            }
            return true;
        };
        Callable<Void> consumer = () -> {
            while(true) {
                String item = q.poll();
                if (item != null) {
                    System.out.println(item);
                } else if (Thread.currentThread().isInterrupted()){
                    break;
                }
            }
            return null;
        };
        
        ExecutorService pool = Executors.newFixedThreadPool(2);
        
        Future<Boolean> producerFuture = pool.submit(producer);
        Future<Void> consumerFuture = pool.submit(consumer);
        
        boolean isProducerFinished = false;
        
        while (!isProducerFinished) {
            try {
                isProducerFinished = producerFuture.get();
            } catch (InterruptedException | ExecutionException ex) {
                System.out.println("Thread producer interrupted"+ ex.getMessage());
            }
        }
        if (isProducerFinished && q.isEmpty()) {
            consumerFuture.cancel(true);
        }
        pool.shutdown();
    }
}