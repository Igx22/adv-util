package adv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

/**
 */
public class JDKUtil {
    private static final Logger log = LoggerFactory.getLogger(JDKUtil.class);
    private static final char PID_SEPERATOR = '@';
    private static File JSTACK_FILE;
    private static Integer jvmPid;

    public static synchronized Integer getJvmPID() {
        if (jvmPid != null) {
            return jvmPid;
        }
        log.debug("getJvmPID()");
        String mxName = ManagementFactory.getRuntimeMXBean().getName();
        log.debug("detecting jvm pid, mxName: {}", mxName);
        int index = mxName.indexOf(PID_SEPERATOR);
        String result;
        if (index != -1) {
            result = mxName.substring(0, index);
        } else {
            throw new IllegalStateException("Could not acquire pid using " + mxName);
        }
        jvmPid = Integer.parseInt(result);
        log.debug("parsing jvm pid from: {} result: {}", result, jvmPid);
        return jvmPid;
    }

    private static synchronized File getJStackTool() {
        if (JSTACK_FILE != null) {
            return JSTACK_FILE;
        }
        boolean isWindows = SysPropUtils.isWindows();
        final String javaHome = SysPropUtils.getEnv("JAVA_HOME");
        final File jstackFile = new File(javaHome, String.format("%sbin%sjstack%s", File.separator, File.separator, isWindows ? ".exe" : ""));
        log.debug("getJStackTool()");
        log.debug("isWindows: {} javaHome: {} jstackFile: {}", isWindows, javaHome, jstackFile.getAbsolutePath());
        if (!FileUtil.isFile(jstackFile)) {
            log.error("jstack tool not found");
            throw new IllegalStateException("JDK not detected, missing file: " + jstackFile.getAbsolutePath());
        }
        log.debug("jstack tool found");
        JSTACK_FILE = jstackFile;
        return jstackFile;
    }

    public static void checkJava17VersionOrFail() {
        Double version = getJavaVersionAsDouble();
        if (version < 1.7d) {
            throw new IllegalStateException("Unsupported java.version: " + version);
        } else {
            log.debug("using java.specification.versin: {}", version);
        }
    }

    public static double getJavaVersionAsDouble() {
        return Double.parseDouble(SysPropUtils.getStringOrFail("java.specification.version"));
    }

    public static String runJStack() {
        final ByteArrayOutputStream os = new ByteArrayOutputStream(40000);
        ProcessWrapper pi = new ProcessWrapper();
        int exitCode = -1;
        try {
            final String[] params = {getJStackTool().getAbsolutePath(), "-l", Integer.toString(getJvmPID())};
            if (log.isTraceEnabled()) {
                log.trace("runJStack(): {}", Arrays.toString(params));
            }
            long start = System.currentTimeMillis();
            exitCode = pi.run(params, os);
            long delta = System.currentTimeMillis() - start;
            log.trace("runJStack(): completed in {}ms, exit code {}", delta, exitCode);
            final String outputStr = os.toString(CharsetUtil.UTF8.name());
            if (exitCode != 0) {
                log.debug("runJStack(): {}", Arrays.toString(params));
                log.debug("runJStack(): output size {}", outputStr.length());
                log.debug("runJStack() output: {}", outputStr);
                log.warn("Bad jstack exit code {}", exitCode);
                return null;
            }
            log.trace("runJStack(): output size {}", outputStr.length());
            return outputStr;
        } catch (Exception e) {
            log.error("Error invoking jstack", e);
            return null;
        }
    }
}
