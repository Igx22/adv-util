package adv.util;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ExecutorUtil {
    private static final Logger log = LoggerFactory.getLogger(ExecutorUtil.class);

    public static ExecutorService newCachedThreadPool(String threadName) {
        return Executors.newCachedThreadPool(new NamedThreadFactory(threadName));
    }

    // фиксированного размера пул, бесконечная очередь задач
    public static ExecutorService newFixedThreadPoolWithUnlimitedQueue(String threadName, int threadCount) {
        return new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new NamedThreadFactory(threadName));
    }

    public static ExecutorService newFixedThreadPoolWithUnlimitedQueue(String threadName, int threadCount, Thread.UncaughtExceptionHandler handler) {
        return new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new NamedThreadFactory(threadName, handler),
                new RejectedExecutionLogger());
    }

    public static ExecutorService newFixedThreadPoolWithLimitedQueue(String threadName, int threadCount, int queueSize) {
        return new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory(threadName));
    }

    public static ExecutorService newFixedThreadPool(String threadName, int size) {
        return Executors.newFixedThreadPool(size, new NamedThreadFactory(threadName));
    }

    public static ExecutorService newCachedThreadPoolWithUnlimitedQueue(String threadName, int coreSize, int maxSize, int timeoutSeconds, Thread.UncaughtExceptionHandler handler) {
        return new ThreadPoolExecutor(coreSize, maxSize, timeoutSeconds, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new NamedThreadFactory(threadName, handler),
                new RejectedExecutionLogger());
    }

    public static ExecutorService newCachedThreadPool(String threadName, int coreSize, int maxSize, int timeoutSeconds, Thread.UncaughtExceptionHandler handler) {
        return new ThreadPoolExecutor(coreSize, maxSize, timeoutSeconds, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new NamedThreadFactory(threadName, handler),
                new RejectedExecutionLogger());
    }

    public static ExecutorService newCachedThreadPool(String threadName, int coreSize, int maxSize, int timeoutSeconds) {
        return new ThreadPoolExecutor(coreSize, maxSize, timeoutSeconds, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new NamedThreadFactory(threadName),
                new RejectedExecutionLogger());
    }

    public static ScheduledExecutorService newScheduledThreadPool(String threadName, int size) {
        return Executors.newScheduledThreadPool(size, new NamedThreadFactory(threadName));
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(String threadName) {
        return Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(threadName));
    }

    public static ExecutorService newSingleThreadExecutor(String threadName) {
        return Executors.newSingleThreadExecutor(new NamedThreadFactory(threadName));
    }

    public static ThreadPoolExecutor newObservableSingleThreadExecutor(String threadName) {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new NamedThreadFactory(threadName)){
            @Override
            public void execute(Runnable command) {
                super.execute(() -> {
                    try {
                        command.run();
                    } catch (Exception e) {
                        log.error("execute(): {} {}", threadName, e);
                    }
                });
            }
        };
    }

    public static ThreadFactory createNamedThreadFactory(String threadName) {
        return new NamedThreadFactory(threadName);
    }

    public static Thread newThread(String name, Runnable r) {
        Thread t = new Thread(r);
        t.setUncaughtExceptionHandler(new NamedThreadFactory(null, null));
        t.setName(name);
        return t;
    }


    public static class NamedThreadFactory implements ThreadFactory, Thread.UncaughtExceptionHandler {
        private String threadName;
        private AtomicLong threadId = new AtomicLong(0);
        private final Thread.UncaughtExceptionHandler exceptionHandler;

        public NamedThreadFactory(String threadName, Thread.UncaughtExceptionHandler exceptionHandler) {
            this.threadName = threadName;
            this.exceptionHandler = exceptionHandler;
        }

        public NamedThreadFactory(String threadName) {
            this.threadName = threadName;
            this.exceptionHandler = null;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(this);
            t.setName(threadName + threadId.getAndIncrement());
            return t;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            if (exceptionHandler != null) {
                exceptionHandler.uncaughtException(t, e);
            } else {
                log.error("Exception in thread \"{}\"", t.getName(), e);
            }
        }
    }

    public static class CurrentThreadExecutor extends AbstractExecutorService {

        @Override
        public void shutdown() {

        }

        @NotNull
        @Override
        public List<Runnable> shutdownNow() {
            return Collections.emptyList();
        }

        @Override
        public boolean isShutdown() {
            return false;
        }

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
            return false;
        }

        public void execute(Runnable r) {
            r.run();
        }
    }

    private static class RejectedExecutionLogger implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.error("executor: rejected task from pool {}", executor);
        }
    }

    public class MeteredExecutor extends ThreadPoolExecutor {


        public MeteredExecutor(int corePoolSize,
                               int maximumPoolSize,
                               long keepAliveTime,
                               TimeUnit unit,
                               BlockingQueue<Runnable> workQueue,
                               ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        @Override
        public int getActiveCount() {
            return super.getActiveCount();
        }

        @Override
        public int getLargestPoolSize() {
            return super.getLargestPoolSize();
        }

        @Override
        public long getCompletedTaskCount() {
            return super.getCompletedTaskCount();
        }

        @Override
        public long getTaskCount() {
            return super.getTaskCount();
        }

        @Override
        public BlockingQueue<Runnable> getQueue() {
            return super.getQueue();
        }
    }

}
