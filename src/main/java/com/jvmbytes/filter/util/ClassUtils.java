package com.jvmbytes.filter.util;

import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Type;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author wongoo
 */
public class ClassUtils {

    public static Iterator<Class<?>> iteratorForLoadedClasses(final Instrumentation inst) {
        return new Iterator<Class<?>>() {

            final Class<?>[] loaded = inst.getAllLoadedClasses();
            int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < loaded.length;
            }

            @Override
            public Class<?> next() {
                return loaded[pos++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    public static Iterator<Class<?>> iterateCurrentClassLoaderClasses() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return iterateClassLoaderClasses(classLoader);
    }

    public static Iterator<Class<?>> iterateClassLoaderClasses(final ClassLoader classLoader) {
        return new Iterator<Class<?>>() {
            ClassLoader iterateClassLoader = classLoader;
            Iterator<Class<?>> classes = getClassLoaderClasses(iterateClassLoader);

            @Override
            public boolean hasNext() {
                boolean has = classes.hasNext();
                if (!has) {
                    if (iterateClassLoader.getParent() != null) {
                        iterateClassLoader = iterateClassLoader.getParent();
                        classes = getClassLoaderClasses(iterateClassLoader);
                        has = classes.hasNext();
                    }
                }
                return has;
            }

            @Override
            public Class<?> next() {
                return classes.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    public static Iterator<Class<?>> getClassLoaderClasses(ClassLoader classLoader) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        try {
            Field f = ClassLoader.class.getDeclaredField("classes");

            f.setAccessible(true);
            Vector<Class<?>> vector = (Vector<Class<?>>) f.get(classLoader);
            /* copy classes to avoid concurrent modify exception */
            classes.addAll(vector);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes.iterator();
    }

    /**
     * java's classname to internal's classname
     *
     * @param javaClassName java's classname
     * @return internal's classname
     */
    public static String toInternalClassName(String javaClassName) {
        return StringUtils.replace(javaClassName, ".", "/");
    }

    /**
     * internal's classname to java's classname
     * java/lang/String to java.lang.String
     *
     * @param internalClassName internal's classname
     * @return java's classname
     */
    public static String toJavaClassName(String internalClassName) {
        return StringUtils.replace(internalClassName, "/", ".");
    }

    public static String[] toJavaClassNameArray(String[] internalClassNameArray) {
        if (null == internalClassNameArray) {
            return new String[]{};
        }
        final String[] javaClassNameArray = new String[internalClassNameArray.length];
        for (int index = 0; index < internalClassNameArray.length; index++) {
            javaClassNameArray[index] = toJavaClassName(internalClassNameArray[index]);
        }
        return javaClassNameArray;
    }

    public static String[] toJavaClassNameArray(Type[] asmTypeArray) {
        if (null == asmTypeArray) {
            return new String[]{};
        }
        final String[] javaClassNameArray = new String[asmTypeArray.length];
        for (int index = 0; index < asmTypeArray.length; index++) {
            javaClassNameArray[index] = asmTypeArray[index].getClassName();
        }
        return javaClassNameArray;
    }

    /**
     * 获取异常的原因描述
     *
     * @param t 异常
     * @return 异常原因
     */
    public static String getCauseMessage(Throwable t) {
        if (null != t.getCause()) {
            return getCauseMessage(t.getCause());
        }
        return t.getMessage();
    }


    /**
     * 提取Class的类名
     * <p>
     * 默认情况下提取{@link Class#getCanonicalName()}，但在遇到不可见类时提取{@link Class#getName()}
     * </p>
     *
     * @param clazz Class类
     * @return Class类名
     */
    public static String getJavaClassName(final Class<?> clazz) {
        return clazz.isArray()
                ? clazz.getCanonicalName()
                : clazz.getName();
    }

    /**
     * 提取Class数组的类名数组
     *
     * @param classArray Class数组
     * @return 类名数组
     */
    public static String[] getJavaClassNameArray(final Class<?>[] classArray) {
        if (ArrayUtils.isEmpty(classArray)) {
            return null;
        }
        final String[] javaClassNameArray = new String[classArray.length];
        for (int index = 0; index < classArray.length; index++) {
            javaClassNameArray[index] = getJavaClassName(classArray[index]);
        }
        return javaClassNameArray;
    }
}
