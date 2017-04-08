package adv.logback;

import adv.util.CharsetUtil;
import adv.util.DateUtil;
import ch.qos.logback.classic.pattern.TargetLengthBasedClassNameAbbreviator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.encoder.EncoderBase;
import org.slf4j.MDC;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

// todo можно добавить цвета в консоль http://logback.qos.ch/manual/layouts.html # coloring
// ru.tpp.util.ColorTest
//
public class FastLogbackFormatter extends EncoderBase<ILoggingEvent> {
    public static final int MAX_CLASS_FQN_LENGTH = 40;
    public static final int MAX_THREAD_NAME_LENGTH = 40;
    private Charset charset = CharsetUtil.UTF8;
    // в logback есть 2 удобных класса для сокращения имени пакета
    // @see TargetLengthBasedClassNameAbbreviator
    // @see ClassNameOnlyAbbreviator
    // @see http://logback.qos.ch/manual/layouts.html#conversionWord
    private TargetLengthBasedClassNameAbbreviator classNameAbbreviator = new TargetLengthBasedClassNameAbbreviator(MAX_CLASS_FQN_LENGTH);


    private static String getShortThreadName(String threadName) {
        if (threadName == null) {
            return threadName;
        } else if (threadName.length() > MAX_THREAD_NAME_LENGTH) {
            int cutPosition = threadName.length() - 40;
            int dotPosition = threadName.lastIndexOf('.');
            //точка после места обрезки - обрежем и точку тоже
            if (dotPosition > 0 && dotPosition > cutPosition) {
                cutPosition = dotPosition + 1;
            }
            return threadName.substring(cutPosition);
        } else if (threadName.contains("nioEventLoopGroup")) {
            threadName = threadName.replace("nioEventLoopGroup", "nio");
        }
        return threadName;
    }

    @Override
    public void doEncode(ILoggingEvent event) throws IOException {
        IThrowableProxy exProxy = event.getThrowableProxy();
        final StringBuilder buf = new StringBuilder(exProxy == null ? 150 : 2000);
        buf.append(event.getLevel().toString().charAt(0))
                .append(' ');
        DateUtil.formatShort(buf, event.getTimeStamp());
        String fqClassName = event.getLoggerName();
        fqClassName = classNameAbbreviator.abbreviate(fqClassName);
        buf.append(' ')
                .append('[')
                .append(getShortThreadName(event.getThreadName()))
                .append(']');

        // Печатаем MDC в виде
        // {value}
        // или {key:value,key:value}

        final Map contexts = event.getMDCPropertyMap();
        if (contexts != null && contexts.size() > 0) {
            buf.append(" {");
            int count = 0;
            for (Object o : contexts.entrySet()) {
                if (count != 0) {
                    buf.append(",");
                }
                Map.Entry entry = (Map.Entry) o;
                buf.append(entry.getKey()).append(":").append(entry.getValue());
                count++;
            }
            buf.append("} ");
        } else {
            buf.append(' ');
        }
        buf.append(fqClassName)
                .append(" - ")
                .append(event.getFormattedMessage())
                .append('\n');
        // Печатаем эксепшены в том же стиле что и e.printStackTrace(), но в конце добавим имя jar файла:
        //
        // org.apache.tomcat.dbcp.dbcp.SQLNestedException: Cannot load JDBC driver class 'com.ibm.db2.jcc.DB2Driver'
        //   at org.apache.tomcat.dbcp.dbcp.BasicDataSource.createDataSource(BasicDataSource.java:1136)
        //   ...
        // Caused by: java.lang.ClassNotFoundException: COM.ibm.db2.jcc.DB2Driver
        //   at java.net.URLClassLoader$1.run(Unknown Source)
        //   ...
        if (exProxy != null) {
            int nestedExceptionCounter = 0;
            do {
                if (nestedExceptionCounter != 0) {
                    buf.append("Caused by: ");
                }
                buf.append(exProxy.getClassName()).append(": ")
                        .append(exProxy.getMessage()).append("\n");
                final StackTraceElementProxy[] stackTraceRecords = exProxy.getStackTraceElementProxyArray();
                if (stackTraceRecords != null) {
                    for (int i = 0; i < stackTraceRecords.length; i++) {
                        final StackTraceElementProxy record = stackTraceRecords[i];
                        buf.append(" at ").append(record.getStackTraceElement().toString());
                        ThrowableProxyUtil.subjoinPackagingData(buf, record);
                        buf.append('\n');
                    }
                }
                exProxy = exProxy.getCause();
                nestedExceptionCounter++;
            } while (exProxy != null);
        }
        outputStream.write(buf.toString().getBytes("UTF8"));
    }


    @Override
    public void close() throws IOException {
    }
}
