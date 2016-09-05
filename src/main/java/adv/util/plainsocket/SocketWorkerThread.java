package adv.util.plainsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 */
public class SocketWorkerThread extends Thread {
    public static final int SHUTDOWN_THREAD_TIMEOUT = 5000;
    public static final int SOCKET_READ_BUFFER = 8000;
    private AtomicBoolean active = new AtomicBoolean(false);
    private final Socket socket;
    private final String logPrefix;
    private OutputStream out;


    public SocketWorkerThread(Socket socket) {
        this.logPrefix = String.format("@[%s:%s->%s:%s]",
                socket.getLocalAddress().getHostName(), socket.getLocalPort(),
                socket.getInetAddress().getHostName(), socket.getPort());
        this.socket = socket;
    }

    public static String toBase16(byte[] data, int offset, int size) {
        if (data == null || data.length == 0 || size == 0) {
            return "";
        }
        if (offset >= size || size + offset > data.length) {
            throw new IndexOutOfBoundsException("" + offset + "," + size + "," + data.length);
        }
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final char[] hexChars = new char[size * 2];
        int v;
        for (int j = offset; j < size; j++) {
            v = data[j] & 0xFF;
            hexChars[j * 2] = hexArray[v / 16];
            hexChars[j * 2 + 1] = hexArray[v % 16];
        }
        return new String(hexChars);
    }

    public void shutdownNow() throws InterruptedException {
        try {
            socket.close();
            active.compareAndSet(false, true);
        } catch (IOException ignored) {
        }
        this.interrupt();
        this.join(SHUTDOWN_THREAD_TIMEOUT);
    }

    public void writeAndFlush(byte[] data) throws IOException {
        if (!active.get()) {
            throw new IllegalStateException();
        }
        System.out.printf("%s sending %4d bytes: %s\n", logPrefix, data.length, toBase16(data, 0, data.length));
        out.write(data);
        out.flush();
    }

    public void run() {
        if (!active.compareAndSet(false, true)) {
            throw new IllegalStateException();
        }
        out = null;
        InputStream in = null;
        try {
            socket.setSoTimeout(DummyClient.workerThreadTimeout);
            System.out.printf("%s connection opened from %s\n", logPrefix, socket.getInetAddress().getHostName());
            out = socket.getOutputStream();
            in = socket.getInputStream();
            synchronized (this) {
                this.notifyAll(); // уведомляем всех кто ждет что чз нас можно писать
            }
            byte[] buf = new byte[SOCKET_READ_BUFFER];
            int size = 0;
            do {
                try {
                    size = in.read(buf, 0, buf.length);
                    if (size == -1) {
                        System.out.printf("%s stream closed, socket status: %s\n", logPrefix, socket.isConnected());
                    } else {
                        onDataReceived(buf, size);
                    }
                } catch (SocketTimeoutException ste) {
                    // timeout чтения
                    System.out.printf("%s waiting for data\n", logPrefix);
                }
            } while (!socket.isClosed() && !isInterrupted() && size >= 0);
        } catch (SocketException se) {
            // разрыв соединения
            System.out.printf("%s connection closed\n", logPrefix);
            //se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s error!", logPrefix);
        } finally {
            System.out.printf("%s closing socket\n", logPrefix);
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                socket.close();
                if (!active.compareAndSet(true, false)) {
                    throw new IllegalStateException();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void onDataReceived(byte[] buf, int size) {
        System.out.printf("%s readed %4d bytes: %s\n", logPrefix, size, toBase16(buf, 0, size));
    }
}
