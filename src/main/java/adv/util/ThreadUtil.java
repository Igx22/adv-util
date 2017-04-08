package adv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 */
public class ThreadUtil {
    private static final Logger log = LoggerFactory.getLogger(ThreadUtil.class);

    public static void awaitForTermination(List<Thread> threads, long timeout, TimeUnit timeUnit) {
        try {
            long finishTime = System.currentTimeMillis() + timeUnit.toMillis(timeout);
            for (Thread t : threads) {
                long now = System.currentTimeMillis();
                if (now > finishTime) {
                    throw new IllegalStateException("failed to stop all threads");
                } else {
                    t.join(finishTime - now);
                    if (t.isAlive()) {
                        throw new IllegalStateException("failed to stop all threads");
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean awaitForConditionEachSecond(long timeout, TimeUnit unit, Check check) {
        return awaitForCondition(timeout, unit, 1, TimeUnit.SECONDS, check);
    }

    public static boolean awaitForConditionMsEachSecond(long timeout, Check check) {
        return awaitForCondition(timeout, TimeUnit.MILLISECONDS, 1, TimeUnit.SECONDS, check);
    }

    public static boolean awaitForConditionMs(long timeout, long checkDelay, Check check) {
        return awaitForCondition(timeout, TimeUnit.MILLISECONDS, checkDelay, TimeUnit.MILLISECONDS, check);
    }

    public static boolean awaitForCondition(long timeout, TimeUnit unit,
                                            long checkDelay, TimeUnit unitForCheck,
                                            Check check) {
        if (check.isReady()) {
            return true;
        } else {
            long millisForDelay = unitForCheck.toMillis(checkDelay);
            long millisLeft = unit.toMillis(timeout);
            try {
                while (millisLeft > 0) {
                    Thread.sleep(millisForDelay);
                    millisLeft -= millisForDelay;
                    if (check.isReady()) {
                        return true;
                    }
                }
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return false;
        }
    }

    /**
     * @param timeout
     * @param checkDelay
     * @param callable
     * @param <T>
     * @return null on timeout, value if callable returs smth before timeout expires
     */
    public static <T> T awaitNotNullResult(long timeout,
                                           long checkDelay,
                                           boolean failOnError,
                                           Callable<T> callable) {
        try {
            T res = null;
            try {
                res = callable.call();
            } catch (Exception e) {
                if (failOnError) {
                    throw e;
                }
            }
            if (res != null) {
                return res;
            }
            while (timeout > 0) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                log.debug("timeout: {}", timeout);
                Thread.sleep(checkDelay);
                timeout -= checkDelay;
                try {
                    res = callable.call();
                } catch (Exception e) {
                    if (failOnError) {
                        throw e;
                    }
                }
                if (res != null) {
                    return res;
                }
            }
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    //NOTE: this is slow, use only if needed
    public String getCallerMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public interface Check {
        boolean isReady();
    }
}
