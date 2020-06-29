package adv.util;

public class TestUtil {

    private static final long step = 50;

    public static void polledWait(long millis, ExceptionUtil.CheckedRunnable r) {
        for (long i = 0; i < millis; i+= step) {
            ThreadUtil.sleep(step);
            try {
                r.run();
                return;
            } catch (Throwable t) {
            }
        }
        ExceptionUtil.runUnchecked(r);
    }
}
