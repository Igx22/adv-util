package adv.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;
import java.util.List;

public class SocketUtil {
    private static SocketImpl newSocketImpl() {
        try {
            Class<?> defaultSocketImpl = Class.forName("java.net.SocksSocketImpl");
            Constructor<?> constructor = defaultSocketImpl.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (SocketImpl) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Socket getSocket(SocketImpl impl) {
        try {
            Method getSocket = SocketImpl.class.getDeclaredMethod("getSocket");
            getSocket.setAccessible(true);
            return (Socket) getSocket.invoke(impl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ServerSocket getServerSocket(SocketImpl impl) {
        try {
            Method getServerSocket = SocketImpl.class.getDeclaredMethod("getServerSocket");
            getServerSocket.setAccessible(true);
            return (ServerSocket) getServerSocket.invoke(impl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class SpySocketImplFactory implements SocketImplFactory {

        private final List<SocketImpl> spy;

        public SpySocketImplFactory(List<SocketImpl> spy) {
            this.spy = spy;
        }

        @Override
        public SocketImpl createSocketImpl() {
            SocketImpl socket = newSocketImpl();
            spy.add(socket);
            return socket;
        }
    }
}
