package com.jvmbytes.filter.matcher;

import com.jvmbytes.commons.structure.BehaviorStructure;
import com.jvmbytes.commons.structure.ClassStructure;
import com.jvmbytes.commons.structure.Feature;
import com.jvmbytes.filter.annotation.Stealth;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import static com.jvmbytes.commons.structure.ClassStructureFactory.createClassStructure;


/**
 * 不支持的类匹配
 *
 * @author luanjia
 */
public class UnsupportedMatcher {

    /**
     * 是否因本身缺陷所暂时无法支持的类
     */
    public static boolean isUnsupportedClass(String className) {
        return isJvmBytesClass(className)
                || containsAny(className,
                "$$Lambda$",
                "$$FastClassBySpringCGLIB$$",
                "$$EnhancerBySpringCGLIB$$"
        );
    }

    private static boolean containsAny(String s, final String... checks) {
        for (final String c : checks) {
            if (s.contains(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是本身的类
     * 因为多命名空间的原因，所以这里不能简单的用ClassLoader来进行判断
     */
    private static boolean isJvmBytesClass(String className) {
        return className.startsWith("com.jvmbytes.");
    }

    private static Set<String> takeJavaClassNames(final Set<ClassStructure> classStructures) {
        final Set<String> javaClassNames = new LinkedHashSet<String>();
        for (final ClassStructure classStructure : classStructures) {
            javaClassNames.add(classStructure.getJavaClassName());
        }
        return javaClassNames;
    }

    /**
     * 判断是否隐形类
     */
    public static boolean isStealthClass(final ClassStructure classStructure) {
        return takeJavaClassNames(classStructure.getFamilyTypeClassStructures())
                .contains(Stealth.class.getName());
    }


    /**
     * 存放classloader是否支持缓存信息
     */
    private static final WeakHashMap<ClassLoader, Boolean> classLoaderSupportedMap
            = new WeakHashMap<ClassLoader, Boolean>();

    /**
     * 判断是否ClassLoader家族中是否有隐形基因
     *
     * @param loader
     * @param isEnableUnsafe
     */
    public static boolean isFromStealthClassLoader(ClassLoader loader, boolean isEnableUnsafe) {
        if (null == loader) {
            return !isEnableUnsafe;
        }
        Boolean supported = classLoaderSupportedMap.get(loader);
        if (supported != null) {
            return supported;
        }
        supported = takeJavaClassNames(createClassStructure(loader.getClass()).getFamilyTypeClassStructures())
                .contains(Stealth.class.getName());
        classLoaderSupportedMap.put(loader, supported);
        return supported;
    }

    /**
     * 是否是负责启动的main函数
     * 这个函数如果被增强了会引起错误,所以千万不能增强,嗯嗯
     * public static void main(String[]);
     */
    private static boolean isJavaMainBehavior(final BehaviorStructure behaviorStructure) {
        final Feature feature = behaviorStructure.getFeature();
        final List<ClassStructure> parameterTypeClassStructures = behaviorStructure.getParameterTypeClassStructures();
        return feature.isPublic()
                && feature.isStatic()
                && "void".equals(behaviorStructure.getReturnTypeClassStructure().getJavaClassName())
                && "main".equals(behaviorStructure.getName())
                && parameterTypeClassStructures.size() == 1
                && "java.lang.String[]".equals(parameterTypeClassStructures.get(0).getJavaClassName());
    }

    /**
     * 是否不支持的方法修饰
     * 1. 是否main方法
     * 2. abstract的方法没有实现，没有必要增强
     * 3. native的方法暂时无法支持
     */
    public static boolean isUnsupportedBehavior(final BehaviorStructure behaviorStructure) {
        return isJavaMainBehavior(behaviorStructure)
                || behaviorStructure.getFeature().isAbstract()
                || behaviorStructure.getFeature().isNative();
    }

}
