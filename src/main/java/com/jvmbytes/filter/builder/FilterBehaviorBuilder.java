package com.jvmbytes.filter.builder;


import com.jvmbytes.filter.Filter;
import com.jvmbytes.filter.matcher.Matcher;

import java.util.List;

import static com.jvmbytes.filter.util.ClassUtils.getJavaClassNameArray;
import static com.jvmbytes.filter.util.StringUtils.toRegexQuoteArray;

/**
 * 行为匹配器实现
 *
 * @author luanjia
 */
public class FilterBehaviorBuilder implements BehaviorBuilder {

    final FilterClassBuilder bfClass;
    final String pattern;
    int withAccess = 0;
    final PatternGroupList withParameterTypes = new PatternGroupList();
    final PatternGroupList hasExceptionTypes = new PatternGroupList();
    final PatternGroupList hasAnnotationTypes = new PatternGroupList();

    FilterBehaviorBuilder(final FilterClassBuilder bfClass,
                          final String pattern) {
        this.bfClass = bfClass;
        this.pattern = pattern;
    }

    @Override
    public BehaviorBuilder withAccess(final int access) {
        withAccess |= access;
        return this;
    }

    @Override
    public BehaviorBuilder withEmptyParameterTypes() {
        withParameterTypes.add(new String[]{});
        return this;
    }

    @Override
    public BehaviorBuilder withParameterTypes(final String... patterns) {
        withParameterTypes.add(patterns);
        return this;
    }

    @Override
    public BehaviorBuilder withParameterTypes(final Class<?>... classes) {
        switch (bfClass.builder.patternType) {
            case REGEX:
                return withParameterTypes(toRegexQuoteArray(getJavaClassNameArray(classes)));
            case WILDCARD:
            default:
                return withParameterTypes(getJavaClassNameArray(classes));
        }
    }

    @Override
    public BehaviorBuilder hasExceptionTypes(final String... patterns) {
        hasExceptionTypes.add(patterns);
        return this;
    }

    @Override
    public BehaviorBuilder hasExceptionTypes(final Class<?>... classes) {
        switch (bfClass.builder.patternType) {
            case REGEX:
                return hasExceptionTypes(toRegexQuoteArray(getJavaClassNameArray(classes)));
            case WILDCARD:
            default:
                return hasExceptionTypes(getJavaClassNameArray(classes));
        }
    }

    @Override
    public BehaviorBuilder hasAnnotationTypes(final String... patterns) {
        hasAnnotationTypes.add(patterns);
        return this;
    }

    @Override
    public BehaviorBuilder hasAnnotationTypes(final Class<?>... classes) {
        switch (bfClass.builder.patternType) {
            case REGEX:
                return hasAnnotationTypes(toRegexQuoteArray(getJavaClassNameArray(classes)));
            case WILDCARD:
            default:
                return hasAnnotationTypes(getJavaClassNameArray(classes));
        }
    }

    @Override
    public BehaviorBuilder onBehavior(final String pattern) {
        return bfClass.onBehavior(pattern);
    }

    @Override
    public ClassBuilder onClass(final String pattern) {
        return bfClass.builder.onClass(pattern);
    }

    @Override
    public ClassBuilder onClass(final Class<?> clazz) {
        return bfClass.builder.onClass(clazz);
    }

    @Override
    public ClassBuilder onAnyClass() {
        return bfClass.builder.onAnyClass();
    }

    @Override
    public List<Filter> build() {
        return bfClass.build();
    }

    @Override
    public Matcher matcher() {
        return bfClass.matcher();
    }
}