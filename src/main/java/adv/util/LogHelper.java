package adv.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

/**
 * Log utils.
 */
@SuppressWarnings("unchecked")
public final class LogHelper {
    /**
     * Log intance.
     */
    private static final Logger log = LoggerFactory.getLogger(LogHelper.class);


    /**
     * Constructor.
     */
    private LogHelper() {
    }

    /**
     * Обходим графы объектов не глубже вот такого
     */
    public static final int MAX_RECURSIVE_DEPTH = 100;

    /**
     * Переносим строки длиннее этого(только для коллекций)
     * Это нужно т.к. notepad++ сильно тормозит в зависимости от длины строки лога
     */
    public static final int SOFT_WRAP_LINE_SIZE = 5000;

    /**
     * Для обычных объектов вызываем toString
     *
     * @param bean
     * @return
     */
    public static boolean isLibaryObject(Object bean) {
        if (bean == null) {
            return false;
        }
        String className = bean.getClass().getName();
        return className.startsWith("java.")
                || className.startsWith("com.sun")
                || className.startsWith("org.apache");
    }

    /**
     * Converts bean to string using get/set/is java bean properties.
     * As a side effect some business methods might be executed.
     *
     * @param bean      source object
     * @param showNulls if true, skips null properties
     * @return string representation of a source object
     */
    public static String toStrProps(Object bean, boolean showNulls) {
        if (bean == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder(bean.getClass().getName()).append("[");
        try {
            final BeanInfo bi = Introspector.getBeanInfo(bean.getClass());
            final PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            for (int i = 0; i < pd.length; i++) {
                if (!"class".equals(pd[i].getName())) {
                    final Object result = pd[i].getReadMethod().invoke(bean);
                    if (showNulls || result != null) {
                        sb.append(pd[i].getDisplayName()).append("=").append(result);
                        if (i == pd.length - 1) continue;
                        sb.append(",");
                    }
                }
            }
        } catch (Exception ex) {
            log.error(null, ex);
        }

        return sb.append("]").toString();
    }

    /**
     * Converts bean to string using its fields and all internal objects/structures including primitive values.
     * This conversion is a bit tricky but provides a generic toString() representation for any domain object.
     *
     * @param bean source object
     * @return string representation of a source object
     */
    public static String toStrFields(Object bean) {
        StringBuilder sb = new StringBuilder(2000);
        toStrFields(sb, bean, 1);
        return sb.toString();
    }

    public static void toStrFields(StringBuilder buf, Object bean, int depth) {
        try {
            if (depth > MAX_RECURSIVE_DEPTH) {
                buf.append("ERROR");
                return;
            }
            if (bean == null) {
                /* null values */
                buf.append("null");
                return;
            }

            if (bean.getClass().isPrimitive() || String.class.isInstance(bean)) {
                /* strings and primitives */
                buf.append("'").append(bean.toString()).append("'");
            } else if (Enum.class.isInstance(bean)) {
                buf.append("").append(getClassName(bean.getClass())).append(".").append(bean.toString()).append("");
            } else if (bean.getClass().isArray()) {
                /* arrays of primitives */
                if (byte[].class.isInstance(bean)) {
                    buf.append("{byte[").append(((byte[]) bean).length).append("]@")
                            .append(System.identityHashCode(bean)).append("}");
                } else if (char[].class.isInstance(bean)) {
                    buf.append("{char[").append(((char[]) bean).length).append("]@")
                            .append(System.identityHashCode(bean)).append("}");
                } else if (short[].class.isInstance(bean)) {
                    buf.append("{short[").append(((short[]) bean).length).append("]@")
                            .append(System.identityHashCode(bean)).append("}");
                } else if (int[].class.isInstance(bean)) {
                    buf.append("{int[").append(((int[]) bean).length).append("]@")
                            .append(System.identityHashCode(bean)).append("}");
                } else if (long[].class.isInstance(bean)) {
                    buf.append("{long[").append(((long[]) bean).length).append("]@")
                            .append(System.identityHashCode(bean)).append("}");
                } else if (float[].class.isInstance(bean)) {
                    buf.append("{float[").append(((float[]) bean).length).append("]@")
                            .append(System.identityHashCode(bean)).append("}");
                } else if (double[].class.isInstance(bean)) {
                    buf.append("{double[").append(((double[]) bean).length).append("]@")
                            .append(System.identityHashCode(bean)).append("}");
                } else if (boolean[].class.isInstance(bean)) {
                    buf.append("{boolean[").append(((boolean[]) bean).length).append("]@")
                            .append(System.identityHashCode(bean)).append("}");
                } else {
                    /* arrays of object*/
                    buf.append("[");
                    final Object[] arr = (Object[]) bean;
                    int oldLength = buf.length();
                    for (int i = 0; i < arr.length; i++) {
                        if (i != 0) {
                            buf.append(", ");
                        }
                        buf.append(" ");
                        buf.append("(").append(i).append(")");
                        final Object itemValue = arr[i];
                        toStrFields(buf, itemValue, depth + 1);
                        oldLength = addSoftWrap(buf, oldLength);
                    }
                    buf.append("]");
                }
            } else if (Collection.class.isInstance(bean)) {
                /* collections of objects */
                int oldLength = buf.length();
                final Collection c = (Collection) bean;
                buf.append("L[").append(c.size()).append("]{");
                int i = 0;
                for (Object itemValue : c) {
                    if (i != 0) {
                        buf.append(", ");
                    }
                    buf.append(" ");
                    buf.append("(").append(i).append(")");
                    toStrFields(buf, itemValue, depth + 1);
                    i++;
                    oldLength = addSoftWrap(buf, oldLength);
                }
                buf.append("}");
            } else if (Map.class.isInstance(bean)) {
                /* map object->object */
                addObjectHeader(buf, bean);
                final Map<Object, Object> map = (Map<Object, Object>) bean;
                buf.append("[").append(map.size()).append("]{");
                int i = 0;
                int oldLength = buf.length();
                for (Map.Entry entry : map.entrySet()) {
                    if (i != 0) {
                        buf.append(", ");
                    }
                    buf.append(" ");
                    buf.append("(").append(i).append(")")
                            .append("[");
                    toStrFields(buf, entry.getKey(), depth + 1);
                    buf.append("]")
                            .append("->")
                            .append("[");
                    toStrFields(buf, entry.getValue(), depth + 1);
                    buf.append("]");
                    i++;
                    oldLength = addSoftWrap(buf, oldLength);
                }
                buf.append("}");
                buf.append(bean.toString());
            } else if (isLibaryObject(bean)) {
                /* other jdk classes */
                buf.append(bean.toString());
            } else {
                /* domain model classes */
                addObjectHeader(buf, bean);
                buf.append(": {");
                /* all fields without inherited one */
                final Field[] declaredFields = bean.getClass().getDeclaredFields();
                boolean isFirstField = true;
                for (int i = 0; i < declaredFields.length; i++) {
                    final Field f = declaredFields[i];
                    if (f.isEnumConstant() || Modifier.isStatic(f.getModifiers())) {
                        continue;
                    }
                    if (!isFirstField) {
                        buf.append(",");
                    }
                    isFirstField = false;
                    final boolean accessble = f.isAccessible();
                    f.setAccessible(true);
                    buf.append(f.getName()).append("=");
                    toStrFields(buf, f.get(bean), depth + 1);
                    f.setAccessible(accessble);
                }
                buf.append("}");
            }
        } catch (Throwable e) {
            /* ignore convertion errors */
            log.error("", e);
            buf.append("TOSTRING ERROR");
        }
    }

    private static int addSoftWrap(StringBuilder buf, int oldLength) {
        int length = buf.length();
        if (length - oldLength > SOFT_WRAP_LINE_SIZE) {
            buf.append("\n");
            oldLength = length;
        }
        return oldLength;
    }

    /**
     * Adds BeanClassname@HashCode to string buffer.
     *
     * @param buf  target string buffer
     * @param bean source object
     */
    public static void addObjectHeader(StringBuilder buf, Object bean) {
        final String clazzName = getClassName(bean.getClass());
        buf.append(clazzName).append("@").append(Integer.toHexString(System.identityHashCode(bean)));
    }


    /**
     * Returns class name without package prefix.
     * todo class.getSimpleName?
     *
     * @param c class
     * @return class name
     */
    public static String getClassName(Class c) {
        String fqclassname = c.getName();
        final int firstChar;
        firstChar = fqclassname.lastIndexOf('.') + 1;
        if (firstChar > 0) {
            fqclassname = fqclassname.substring(firstChar);
        }
        return fqclassname;
    }

    /**
     * Returns full class name.
     *
     * @param c class
     * @return fully qualified class name
     */
    public static String getFullClassName(Class c) {
        return c.getName();
    }

    /**
     * Returns java package name for a class.
     *
     * @param c class
     * @return package name
     */
    public static String getPackageName(Class c) {
        final String fullyQualifiedName = c.getName();
        final int lastDot = fullyQualifiedName.lastIndexOf('.');
        if (lastDot == -1) {
            return "";
        }
        return fullyQualifiedName.substring(0, lastDot);
    }

}
