// Online Java Compiler
// Use this editor to write, compile and run your Java code online
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;
class Counter {
    // Suggestion: Instead of lock we can use AtomicInteger
    static int count = 0;
    static final Object lock = new Object();
    public static void increment() {
        synchronized (lock) {
            System.out.println("incrementing counter from "+count);
            count++;
        }
    }
}

class ExecuteThreadsAlternatively {
    
    public static void main(String[] args) {
        List<Callable<Void>> threads = new ArrayList<>();
        for (int i=0;i<100;i++){
            threads.add(() -> {
                Counter.increment();
                return null;
            });
        };
        ExecutorService pool = Executors.newFixedThreadPool(100);
        try {
            pool.invokeAll(threads);
        } catch (InterruptedException ex) {
            System.out.print(ex.getMessage());    
        } finally {
            pool.shutdown();
        }
        
    }
}