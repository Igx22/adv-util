package adv.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 */
public class ThreadUtil {

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

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public interface Check {
        boolean isReady();
    }
}
