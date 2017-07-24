package adv.util.plainsocket;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * для тестов
 * <p>
 * -отдельный поток для установления соединения
 * -отдельный поток на каждое клиентское соединение
 * -принимаем все входящие соединения и печатаем то что нам шлют в system.out
 * -без зависимостей на внешние библиотеки
 * -обработка данных идет в {@link DummyServer.WorkerThread#run()}
 * -запус и остановка всех потоков чз init и shutdown
 */
public class DummyServer extends Thread {
    private static final int bossThreadTimeout = 30000;
    private static final int workerThreadTimeout = 30000;
    private final int port;
    private final String interfaceHost;
    ServerSocket providerSocket;
    BossThread bossThread;
    List<SocketWorkerThread> workers = new ArrayList<>();

    public DummyServer(int port, String interfaceHost) {
        this.port = port;
        this.interfaceHost = interfaceHost;
        System.out.printf("using iface: %s port: %d \n", interfaceHost, port);
    }


    private class BossThread extends Thread {

        public void run() {
            try {
                providerSocket = new ServerSocket(port, 10, InetAddress.getByName(interfaceHost));
                providerSocket.setSoTimeout(bossThreadTimeout);
                Socket socket = null;
                System.out.printf("waiting for connection on %s:%d\n", interfaceHost, port);
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        socket = providerSocket.accept();
                        System.out.printf("accepted connection: %s\n", socket);
                        SocketWorkerThread worker = new SocketWorkerThread(socket);
                        workers.add(worker);
                        worker.start();
                    } catch (SocketTimeoutException ste) {
                        System.out.printf("waiting for connection\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("error!\n");
            } finally {
                try {
                    System.out.println("closing socket");
                    providerSocket.close();
                    for (SocketWorkerThread worker : workers) {
                        worker.shutdownNow();
                    }
                } catch (IOException | InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

    }

    public void init() {
        bossThread = new BossThread();
        bossThread.start();
    }

    public void shutdown() {
        try {
            bossThread.interrupt();
            bossThread.join(30000);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        System.out.println(">starting server");
        Integer port = Integer.getInteger("DummyServer.port", 10051);
        String iface = System.getProperties().getProperty("DummyServer.interfaceHost", "localhost");
        DummyServer server = new DummyServer(port, iface);
        server.init();
        System.out.println(">press <enter> to stop");
        new Scanner(System.in).nextLine();
        System.out.println(">stopping server");
        server.shutdown();
        System.out.println(">stopped server");
    }
}
