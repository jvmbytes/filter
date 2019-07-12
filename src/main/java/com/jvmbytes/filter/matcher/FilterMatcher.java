package com.jvmbytes.filter.matcher;

import com.jvmbytes.filter.AccessFlags;
import com.jvmbytes.filter.Filter;
import com.jvmbytes.filter.matcher.structure.BehaviorStructure;
import com.jvmbytes.filter.matcher.structure.ClassStructure;
import com.jvmbytes.filter.matcher.structure.Feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.jvmbytes.filter.AccessFlags.*;


/**
 * 过滤器实现的匹配器
 *
 * @author luanjia
 */
public class FilterMatcher implements Matcher {

    private final Filter filter;

    public FilterMatcher(final Filter filter) {
        this.filter = filter;
    }

    /**
     * 获取需要匹配的类结构
     * 如果要匹配子类就需要将这个类的所有家族成员找出
     */
    private Collection<ClassStructure> getWaitingMatchClassStructures(final ClassStructure classStructure) {
        final Collection<ClassStructure> waitingMatchClassStructures = new ArrayList<ClassStructure>();
        waitingMatchClassStructures.add(classStructure);
        if (filter.isIncludeSubClasses()) {
            waitingMatchClassStructures.addAll(classStructure.getFamilyTypeClassStructures());
        }
        return waitingMatchClassStructures;
    }

    private String[] toJavaClassNameArray(final Collection<ClassStructure> classStructures) {
        if (null == classStructures) {
            return null;
        }
        final List<String> javaClassNames = new ArrayList<String>();
        for (final ClassStructure classStructure : classStructures) {
            javaClassNames.add(classStructure.getJavaClassName());
        }
        return javaClassNames.toArray(new String[0]);
    }

    private boolean matchingClassStructure(ClassStructure classStructure) {
        for (final ClassStructure wmCs : getWaitingMatchClassStructures(classStructure)) {

            // 匹配类结构
            if (filter.doClassFilter(
                    toFilterAccess(wmCs.getFeature()),
                    wmCs.getJavaClassName(),
                    null == wmCs.getSuperClassStructure()
                            ? null
                            : wmCs.getSuperClassStructure().getJavaClassName(),
                    toJavaClassNameArray(wmCs.getFamilyInterfaceClassStructures()),
                    toJavaClassNameArray(wmCs.getFamilyAnnotationTypeClassStructures())
            )) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MatchingResult matching(final ClassStructure classStructure) {
        final MatchingResult result = new MatchingResult();

        // 1. 匹配ClassStructure
        if (!matchingClassStructure(classStructure)) {
            return result;
        }

        // 如果不开启加载Bootstrap的类，遇到就过滤掉
        if (!filter.isIncludeBootstrap()
                && classStructure.getClassLoader() == null) {
            return result;
        }

        // 2. 匹配BehaviorStructure
        for (final BehaviorStructure behaviorStructure : classStructure.getBehaviorStructures()) {
            if (filter.doMethodFilter(
                    toFilterAccess(behaviorStructure.getFeature()),
                    behaviorStructure.getName(),
                    toJavaClassNameArray(behaviorStructure.getParameterTypeClassStructures()),
                    toJavaClassNameArray(behaviorStructure.getExceptionTypeClassStructures()),
                    toJavaClassNameArray(behaviorStructure.getAnnotationTypeClassStructures())
            )) {
                result.getBehaviorStructures().add(behaviorStructure);
            }
        }

        return result;
    }

    /**
     * 转换为{@link AccessFlags}的Access体系
     *
     * @param feature feature flag
     * @return 部分兼容ASM的access flag
     */
    private static int toFilterAccess(final Feature feature) {
        int flag = 0;
        if (feature.isPublic()) {
            flag |= ACF_PUBLIC;
        }
        if (feature.isPrivate()) {
            flag |= ACF_PRIVATE;
        }
        if (feature.isProtected()) {
            flag |= ACF_PROTECTED;
        }
        if (feature.isStatic()) {
            flag |= ACF_STATIC;
        }
        if (feature.isFinal()) {
            flag |= ACF_FINAL;
        }
        if (feature.isInterface()) {
            flag |= ACF_INTERFACE;
        }
        if (feature.isNative()) {
            flag |= ACF_NATIVE;
        }
        if (feature.isAbstract()) {
            flag |= ACF_ABSTRACT;
        }
        if (feature.isEnum()) {
            flag |= ACF_ENUM;
        }
        if (feature.isAnnotation()) {
            flag |= ACF_ANNOTATION;
        }
        return flag;
    }

    /**
     * 将{@link Filter}数组转换为Or关系的Matcher
     *
     * @param filterArray 过滤器数组
     * @return Or关系Matcher
     */
    public static Matcher toOrGroupMatcher(final List<Filter> filterArray) {
        final Matcher[] matcherArray = new Matcher[filterArray.size()];
        for (int index = 0; index < matcherArray.length; index++) {
            matcherArray[index] = new FilterMatcher(filterArray.get(index));
        }
        return new OrMatcher(matcherArray);
    }

}
