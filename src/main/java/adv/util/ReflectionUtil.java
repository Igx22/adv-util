package adv.util;

import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;
import java.lang.reflect.Modifier;

public class ReflectionUtil {

    private static final Logger log = LoggerFactory.getLogger(ReflectionUtil.class);

    public static Class getFieldGenericType(Field f) {
        return (Class) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
    }

    public static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    public static <T> T getDeclaredFieldValue(Object targetObject, String privateFieldName, Class<T> fieldType) {
        return getDeclaredFieldValue(targetObject, targetObject.getClass(), privateFieldName, fieldType);
    }

    public static <T> T getDeclaredFieldValue(Object targetObject, Class objType, String privateFieldName, Class<T> fieldType) {
        try {
            Field f = objType.getDeclaredField(privateFieldName);
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            Object value = f.get(targetObject);
            return (T) value;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void setDeclaredFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Object createInstanceWithoutConstructor(Class clazz) {
        try {
            ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
            final Constructor constructor =
                    rf.newConstructorForSerialization(
                            clazz, Object.class.getDeclaredConstructor(new Class[0]));
            final Object o = constructor.newInstance(new Object[0]);
            return o;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }

    }

    public static Class copyAllDeclaredFieldsAndMethods(String srcClassName, String dstClass) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("copyAllDeclaredFieldsAndMethods(): srcClassName: {}, dstClass: {}", srcClassName, dstClass);
            }
            CtClass sourceClazz = ClassPool.getDefault().get(srcClassName);
            CtClass targetClazz = ClassPool.getDefault().get(dstClass);
            for (CtField ctField : sourceClazz.getFields()) {
                CtField copy = new CtField(ctField, targetClazz);
                targetClazz.addField(copy);
                log.debug("adding field: {}", copy);
            }
            for (CtMethod ctMethod : sourceClazz.getDeclaredMethods()) {
                log.debug("adding method: {}", ctMethod);
                CtMethod existingMethod = null;
                try {
                    existingMethod = targetClazz.getDeclaredMethod(ctMethod.getName(), ctMethod.getParameterTypes());
                } catch (NotFoundException ignored) {
                }
                if (existingMethod != null) {
                    log.debug("..removing existing method: {}", existingMethod);
                    targetClazz.removeMethod(existingMethod);
                }
                CtMethod copy = CtNewMethod.copy(ctMethod, targetClazz, null);
                log.debug("..copying method: {}", copy);
                targetClazz.addMethod(copy);

            }
            return targetClazz.toClass();
        } catch (NotFoundException | CannotCompileException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Class subClass(String parentClassName, String prototypeClassName, String newSubclassName) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("subClass(): parentClassName: {}, dstClass: {}", parentClassName, prototypeClassName);
            }
            CtClass parentClazz = ClassPool.getDefault().get(parentClassName);
            parentClazz.setModifiers(parentClazz.getModifiers() & ~Modifier.FINAL);
            CtClass prototypeClazz = ClassPool.getDefault().get(prototypeClassName);
            prototypeClazz.setName(newSubclassName);
            prototypeClazz.setSuperclass(parentClazz);
            return prototypeClazz.toClass();
        } catch (NotFoundException | CannotCompileException e) {
            throw new IllegalStateException(e);
        }
    }
}
