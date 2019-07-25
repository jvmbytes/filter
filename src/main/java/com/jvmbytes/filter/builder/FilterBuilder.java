package com.jvmbytes.filter.builder;

import com.jvmbytes.filter.Filter;
import com.jvmbytes.filter.matcher.FilterMatcher;
import com.jvmbytes.filter.matcher.Matcher;
import com.jvmbytes.util.PatternType;

import java.util.ArrayList;
import java.util.List;

import static com.jvmbytes.util.ClassUtils.getJavaClassName;
import static com.jvmbytes.util.CollectionUtils.add;
import static com.jvmbytes.util.StringUtils.patternMatching;
import static java.util.regex.Pattern.quote;

/**
 * 事件观察者类构建器
 * <p>
 * 方便构建事件观察者，原有的{@link Filter}是一个比较原始、暴力、直接的接口，虽然很万能，但要精巧的构造门槛很高！
 * 这里设计一个Builder对是为了降低实现的门槛
 * </p>
 *
 * @author luanjia
 */
public class FilterBuilder implements Builder {

    PatternType patternType;

    private List<FilterClassBuilder> bfClasses = new ArrayList<FilterClassBuilder>();

    public FilterBuilder() {
        this(PatternType.WILDCARD);
    }

    public FilterBuilder(PatternType patternType) {
        this.patternType = patternType;
    }


    /**
     * 匹配任意类
     * <p>
     * 等同于{@code onClass("*")}
     * </p>
     *
     * @return ClassBuilder
     */
    public ClassBuilder onAnyClass() {
        switch (patternType) {
            case REGEX:
                return onClass(".*");
            case WILDCARD:
            default:
                return onClass("*");
        }
    }

    /**
     * 匹配指定类
     * <p>
     * 等同于{@code onClass(clazz.getCanonicalName())}
     * </p>
     *
     * @param clazz 指定Class，这里的Class可以忽略ClassLoader的差异。
     *              这里主要取Class的类名
     * @return ClassBuilder
     */
    public ClassBuilder onClass(final Class<?> clazz) {
        switch (patternType) {
            case REGEX: {
                return onClass(quote(getJavaClassName(clazz)));
            }
            case WILDCARD:
            default:
                return onClass(getJavaClassName(clazz));
        }

    }

    /**
     * 模版匹配类名称(包含包名)
     * <p>
     * 例子：
     * <ul>
     * <li>"com.alibaba.*"</li>
     * <li>"java.util.ArrayList"</li>
     * </ul>
     *
     * @param pattern 类名匹配模版
     * @return ClassBuilder
     */
    public ClassBuilder onClass(final String pattern) {
        return add(bfClasses, new FilterClassBuilder(this, pattern));
    }

    @Override
    public List<Filter> build() {
        final List<Filter> filters = new ArrayList<Filter>();
        for (final FilterClassBuilder bfClass : bfClasses) {
            final Filter filter = new Filter() {
                @Override
                public boolean doClassFilter(final int access,
                                             final String javaClassName,
                                             final String superClassTypeJavaClassName,
                                             final String[] interfaceTypeJavaClassNameArray,
                                             final String[] annotationTypeJavaClassNameArray) {
                    return (access & bfClass.withAccess) == bfClass.withAccess
                            && patternMatching(patternType, javaClassName, bfClass.pattern)
                            && bfClass.hasInterfaceTypes.patternHas(patternType, interfaceTypeJavaClassNameArray)
                            && bfClass.hasAnnotationTypes.patternHas(patternType, annotationTypeJavaClassNameArray);
                }

                @Override
                public boolean doMethodFilter(final int access,
                                              final String javaMethodName,
                                              final String[] parameterTypeJavaClassNameArray,
                                              final String[] throwsTypeJavaClassNameArray,
                                              final String[] annotationTypeJavaClassNameArray) {
                    // nothing to matching
                    if (bfClass.bfBehaviors.isEmpty()) {
                        return false;
                    }

                    // matching any behavior
                    for (final FilterBehaviorBuilder bfBehavior : bfClass.bfBehaviors) {
                        if ((access & bfBehavior.withAccess) == bfBehavior.withAccess
                                && patternMatching(patternType, javaMethodName, bfBehavior.pattern)
                                && bfBehavior.withParameterTypes.patternWith(patternType, parameterTypeJavaClassNameArray)
                                && bfBehavior.hasExceptionTypes.patternHas(patternType, throwsTypeJavaClassNameArray)
                                && bfBehavior.hasAnnotationTypes.patternHas(patternType, annotationTypeJavaClassNameArray)) {
                            return true;
                        }//if
                    }//for

                    // non matched
                    return false;
                }

                @Override
                public boolean isIncludeSubClasses() {
                    return bfClass.includeSubClasses;
                }

                @Override
                public boolean isIncludeBootstrap() {
                    return bfClass.includeBootstrap;
                }
            };


            filters.add(filter);
        }
        return filters;
    }

    @Override
    public Matcher matcher() {
        List<Filter> filters = build();
        switch (filters.size()) {
            case 0:
                return null;
            case 1:
                return new FilterMatcher(filters.get(0));
            default:
                return FilterMatcher.toOrGroupMatcher(filters);
        }
    }


}
