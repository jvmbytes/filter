package com.jvmbytes.filter.builder;

/**
 * 构建方法匹配器
 *
 * @author luanjia
 */
public interface BehaviorBuilder extends Builder {

    BehaviorBuilder withAccess(int access);

    BehaviorBuilder withEmptyParameterTypes();

    BehaviorBuilder withParameterTypes(String... patterns);

    BehaviorBuilder withParameterTypes(Class<?>... classes);

    BehaviorBuilder hasExceptionTypes(String... patterns);

    BehaviorBuilder hasExceptionTypes(Class<?>... classes);

    BehaviorBuilder hasAnnotationTypes(String... patterns);

    BehaviorBuilder hasAnnotationTypes(Class<?>... classes);

    BehaviorBuilder onBehavior(String pattern);

    ClassBuilder onClass(String pattern);

    ClassBuilder onClass(Class<?> clazz);

    ClassBuilder onAnyClass();

}
