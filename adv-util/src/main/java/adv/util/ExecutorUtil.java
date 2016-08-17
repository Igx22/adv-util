package adv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class ExecutorUtil {
    private static final Logger log = LoggerFactory.getLogger(ExecutorUtil.class);

    public static ExecutorService newCachedThreadPool(String threadName) {
        return Executors.newCachedThreadPool(new NamedThreadFactory(threadName));
    }

    public static ExecutorService newFixedThreadPool(String threadName, int size) {
        return Executors.newFixedThreadPool(size, new NamedThreadFactory(threadName));
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

    public static ThreadFactory createNamedThreadFactory(String threadName) {
        return new NamedThreadFactory(threadName);
    }

    public static Thread newThread(String name, Runnable r) {
        Thread t = new Thread(r);
        t.setUncaughtExceptionHandler(new NamedThreadFactory(null));
        t.setName(name);
        return t;
    }


    public static class NamedThreadFactory implements ThreadFactory, Thread.UncaughtExceptionHandler {
        private String threadName;
        private AtomicLong threadId = new AtomicLong(0);

        public NamedThreadFactory(String threadName) {
            this.threadName = threadName;
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
            log.error("Exception in thread \"{}\"", t.getName(), e);
        }
    }
}
