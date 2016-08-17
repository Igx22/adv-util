package adv.logback;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class LogUtil {

    public static volatile boolean stopped = false;

    public static void logPrettyInfoMessage(Logger log, String message) {
        log.info("***********************************************************************");
        log.info(message);
        log.info("***********************************************************************");
    }

    // останавливаем logback и флашим логи
    // see http://mailman.qos.ch/pipermail/logback-user/2013-June/004025.html
    // see http://stackoverflow.com/questions/11829922/logback-file-appender-doesnt-flush-immediately
    public static void stopLogback() {
        if (stopped) {
            return;
        }
        if (LoggerFactory.getILoggerFactory() instanceof LoggerContext) {
            LoggerFactory.getLogger(LogUtil.class).info("stopping logback!");
            ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
        }
        stopped = true;
    }
}
