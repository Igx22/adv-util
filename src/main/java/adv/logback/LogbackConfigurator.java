package adv.logback;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class LogbackConfigurator {

    public static void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(new
                LogbackShutdownHook(), "LogbackShutdownHook"));
    }

    private static class LogbackShutdownHook implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
            LoggerContext loggerContext = (LoggerContext)
                    LoggerFactory.getILoggerFactory();
            loggerContext.stop();
        }
    }
}
