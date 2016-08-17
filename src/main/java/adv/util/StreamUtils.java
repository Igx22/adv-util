package adv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;

public final class StreamUtils {

    private static final Logger log = LoggerFactory.getLogger(StreamUtils.class);

    private StreamUtils() {
    }

    private static final int READ_BUFFER_SIZE = 8192;

    public static void copyStream(InputStream input, OutputStream out) throws IOException {
        copyStream(input, out, READ_BUFFER_SIZE);
    }

    /**
     * Перекачивает байты из одного потока во второй
     *
     * @param input              Поток, из которого читаем
     * @param out                Поток, в который пишем
     * @param transferBufferSize Размер буфера обмена
     * @throws IOException Если произошла ошибка чтения
     */
    public static void copyStream(InputStream input, OutputStream out, int transferBufferSize) throws IOException {
        byte[] buffer = new byte[transferBufferSize];
        int read;
        while ((read = input.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
        out.flush();
    }

    public static void copyStream(Reader input, Writer out) throws IOException {
        copyStream(input, out, READ_BUFFER_SIZE);
    }

    public static void copyStream(Reader input, Writer out, int transferBufferSize) throws IOException {
        char[] buffer = new char[transferBufferSize];
        int read;
        while ((read = input.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
        out.flush();
    }

    public static byte[] read(InputStream input) throws IOException {
        return read(input, READ_BUFFER_SIZE);
    }

    public static byte[] read(InputStream input, int readBufferSize) throws IOException {
        if (input == null) {
            throw new NullPointerException("Input stream cannot be null");
        }

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        byte[] commBuffer = new byte[readBufferSize];
        int bytesRead;

        while ((bytesRead = input.read(commBuffer)) > 0) {
            data.write(commBuffer, 0, bytesRead);
        }

        return data.toByteArray();
    }

    public static void read(InputStream input, byte[] buffer) throws IOException {
        int total = 0;
        do {
            int read = input.read(buffer, total, buffer.length - total);
            if (read <= 0) {
                throw new IOException("Unexpected end of stream reached");
            }
            total += read;
        } while (total < buffer.length);
    }

    public static char[] read(Reader reader) throws IOException {
        return read(reader, READ_BUFFER_SIZE);
    }

    public static char[] read(Reader reader, int readBufferSize) throws IOException {
        CharArrayWriter writer = new CharArrayWriter();
        char[] readBuffer = new char[readBufferSize];
        int charsRead;

        while ((charsRead = reader.read(readBuffer)) > 0) {
            writer.write(readBuffer, 0, charsRead);
        }

        return writer.toCharArray();
    }

    public static String readString(Reader reader) throws IOException {
        return readString(reader, READ_BUFFER_SIZE);
    }

    public static String readString(Reader reader, int readBufferSize) throws IOException {
        CharArrayWriter writer = new CharArrayWriter();
        char[] readBuffer = new char[readBufferSize];
        int charsRead;

        while ((charsRead = reader.read(readBuffer)) > 0) {
            writer.write(readBuffer, 0, charsRead);
        }

        return writer.toString();
    }

    public static void read(Reader reader, char[] buffer) throws IOException {
        int total = 0;
        do {
            int read = reader.read(buffer, total, buffer.length - total);
            if (read <= 0) {
                throw new IOException("Unexpected end of stream reached");
            }
            total += read;
        } while (total < buffer.length);
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            log.error("Exception while closing resource ", e);
        }
    }

    public static void close(Object closeable) {
        try {
            if (closeable != null) {
                Method method = closeable.getClass().getMethod("close");
                method.invoke(closeable);
            }
        } catch (Exception e) {
            log.error("Exception while closing resource ", e);
        }
    }
}
