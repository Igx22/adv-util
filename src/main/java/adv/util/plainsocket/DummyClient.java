package adv.util.plainsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * для тестов
 * -отдельный поток на каждое клиентское соединение
 * -пишем в лог вс
 */
public class DummyClient {
    public static final int workerThreadTimeout = 30000;
    private final String host;
    private final int port;
    private OutputStream out;
    private InputStream in;
    private SocketWorkerThread workerThread;


    public DummyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() {
        try {
            System.out.printf("connecting to %s:%d\n", host, port);
            workerThread = new SocketWorkerThread(new Socket(host, port));
            workerThread.start();
            synchronized (workerThread) {
                workerThread.wait(10000);  // ждем notify о соединении или падаем
            }
        } catch (InterruptedException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void shutdown() {
        try {
            workerThread.shutdownNow();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public void writeAndFlush(byte[] data) throws IOException {
        workerThread.writeAndFlush(data);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Integer port = Integer.getInteger("DummyClient.port", 9999);
        DummyClient client = new DummyClient("localhost", port);
        client.init();
        client.writeAndFlush(new byte[]{(byte) 0xAA, 0x01, (byte) 0xAB, 0x14, 0x44, 0x22, (byte) 0xC0, 0x03, 0x00, 0x6F, 0x0D, 0x00,
                0x0A, 0x01, 0x00, 0x00, 0x02, 0x00, (byte) 0xF8, (byte) 0x9A, (byte) 0x89, 0x2F, 0x13, 0x03, 0x4C});
        Thread.sleep(600000);
        client.shutdown();
    }
}

