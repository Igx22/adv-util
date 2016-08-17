package adv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Logging proxy implementation.
 * Used for debbugging and verbose logging of any interface invocations.
 */
public final class LogProxy implements java.lang.reflect.InvocationHandler {
    /**
     * Logger instance.
     * todo можно брать log от класса объекта который "оборачиваем"
     */
    private static final Logger log = LoggerFactory.getLogger(LogProxy.class);
    /**
     * Wrapped instance on which methods should be actually invoked.
     */
    private Object wrappedObject;

    /**
     * Constructor.
     *
     * @param wrappedObject wrapped instance of a target object
     */
    private LogProxy(Object wrappedObject) {
        this.wrappedObject = wrappedObject;
    }


    /**
     * /**
     * Creates new logproxy instance.
     * All interfaces implemented by object are handled.
     *
     * @param targetObject proxied object
     * @param <T>          targetObject type
     * @return proxy object which implements the same interfaces as original targetObject
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T newInstance(T targetObject) {
        return (T) java.lang.reflect.Proxy.newProxyInstance(
                targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(),
                new LogProxy(targetObject));
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        final long startTime = System.nanoTime();
        if (log.isDebugEnabled()) {
            log.debug(">>> Calling " + m.getDeclaringClass().getSimpleName() + "->" + m.getName()
                    + " ( " + LogHelper.toStrFields(args) + " ) ");
        }
        Object result = null;
        try {
            result = m.invoke(wrappedObject, args);
            return result;
        } catch (InvocationTargetException e) {
            final Throwable ex = e.getTargetException();
            log.error("!!!! Invocation failed", ex);
            throw ex;
        } catch (Throwable e) {
            log.error("!!!! Invocation failed", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                long elapsed = (System.nanoTime() - startTime) / 1000;
                log.debug("<<< Exiting; invocation took " + elapsed + " ms "
                        + "% result: " + LogHelper.toStrFields(result));
            }
        }
    }
}

