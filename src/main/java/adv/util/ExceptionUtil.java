package adv.util;

import java.io.IOException;

/**
 */
@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public class ExceptionUtil {

    public static void runUnchecked(CheckedRunnable r) {
        try {
            r.run();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String stackTraceToString(Throwable e) {
        final int stackTraceDepthToUse = 5;
        final boolean useNewLine = true;
        StringBuilder sb = new StringBuilder(300);
        sb.append(e.getClass().getName());
        sb.append(": ");
        sb.append(e.getMessage());
        sb.append(useNewLine ? "\n" : "   //   ");
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        int actualDepth = Math.min(stackTraceElements.length, stackTraceDepthToUse);
        for (int i = 0; i < actualDepth; i++) {
            sb.append("    at ");
            sb.append(stackTraceElements[i].toString());
            if (i < actualDepth - 1) {
                sb.append(useNewLine ? "\n" : "   //   ");
            }
        }
        if (e.getCause() != null) {
            sb.append("\n");
            sb.append(stackTraceToString(e.getCause()));
        }
        return sb.toString();
    }


    public static Throwable getRootCause(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        if (throwable.getCause() != null)
            return getRootCause(throwable.getCause());
        return throwable;
    }

    public static String getRootCauseMessage(Throwable throwable) {
        Throwable rootCause = getRootCause(throwable);
        return rootCause == null ? "" : rootCause.getMessage();
    }

    public interface CheckedRunnable {
        void run() throws Exception;
    }

    public static void main(String[] args) {
        runUnchecked(() -> {
            throw new IOException();
        });
        System.out.println(stackTraceToString(new IllegalStateException("test")));
        System.out.println();
        System.out.println();
        System.out.println();
        new IllegalStateException("test").printStackTrace();
    }

}
