package com.jvmbytes.filter.builder;

import com.jvmbytes.filter.Filter;
import com.jvmbytes.filter.matcher.Matcher;

import java.util.ArrayList;
import java.util.List;

import static com.jvmbytes.filter.util.ClassUtils.getJavaClassNameArray;
import static com.jvmbytes.filter.util.CollectionUtils.add;
import static com.jvmbytes.filter.util.StringUtils.toRegexQuoteArray;


/**
 * 类匹配器实现
 *
 * @author luanjia
 */
public class FilterClassBuilder implements ClassBuilder {

    FilterBuilder builder;
    final String pattern;
    int withAccess = 0;
    boolean includeSubClasses = false;
    boolean includeBootstrap = false;
    final PatternGroupList hasInterfaceTypes = new PatternGroupList();
    final PatternGroupList hasAnnotationTypes = new PatternGroupList();
    final List<FilterBehaviorBuilder> bfBehaviors = new ArrayList<FilterBehaviorBuilder>();

    /**
     * 构造类构建器
     *
     * @param builder
     * @param pattern 类名匹配模版
     */
    FilterClassBuilder(FilterBuilder builder, final String pattern) {
        this.builder = builder;
        this.pattern = pattern;
    }

    @Override
    public ClassBuilder includeBootstrap() {
        this.includeBootstrap = true;
        return this;
    }

    @Override
    public ClassBuilder isIncludeBootstrap(boolean isIncludeBootstrap) {
        if (isIncludeBootstrap) {
            includeBootstrap();
        }
        return this;
    }

    @Override
    public ClassBuilder includeSubClasses() {
        this.includeSubClasses = true;
        return this;
    }

    @Override
    public ClassBuilder isIncludeSubClasses(boolean isIncludeSubClasses) {
        if (isIncludeSubClasses) {
            includeSubClasses();
        }
        return this;
    }

    @Override
    public ClassBuilder withAccess(final int access) {
        withAccess |= access;
        return this;
    }

    @Override
    public ClassBuilder hasInterfaceTypes(final String... patterns) {
        hasInterfaceTypes.add(patterns);
        return this;
    }

    @Override
    public ClassBuilder hasAnnotationTypes(final String... patterns) {
        hasAnnotationTypes.add(patterns);
        return this;
    }

    @Override
    public ClassBuilder hasInterfaceTypes(final Class<?>... classes) {
        switch (builder.patternType) {
            case REGEX:
                return hasInterfaceTypes(toRegexQuoteArray(getJavaClassNameArray(classes)));
            case WILDCARD:
            default:
                return hasInterfaceTypes(getJavaClassNameArray(classes));
        }
    }

    @Override
    public ClassBuilder hasAnnotationTypes(final Class<?>... classes) {
        switch (builder.patternType) {
            case REGEX:
                return hasAnnotationTypes(toRegexQuoteArray(getJavaClassNameArray(classes)));
            case WILDCARD:
            default:
                return hasAnnotationTypes(getJavaClassNameArray(classes));
        }
    }

    @Override
    public BehaviorBuilder onBehavior(final String pattern) {
        return add(bfBehaviors, new FilterBehaviorBuilder(this, pattern));
    }

    @Override
    public BehaviorBuilder onAnyBehavior() {
        switch (builder.patternType) {
            case REGEX:
                return onBehavior(".*");
            case WILDCARD:
            default:
                return onBehavior("*");
        }
    }

    @Override
    public List<Filter> build() {
        return builder.build();
    }

    @Override
    public Matcher matcher() {
        return builder.matcher();
    }
}