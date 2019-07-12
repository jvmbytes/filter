package com.jvmbytes.filter.builder;

import com.jvmbytes.filter.AccessFlags;

/**
 * 构建类匹配器
 *
 * @author luanjia
 */
public interface ClassBuilder extends Builder {

    /**
     * 是否包含被Bootstrap所加载的类
     * <p>
     * 类似如："java.lang.String"等，都是来自BootstrapClassLoader所加载的类。
     * </p>
     *
     * @return ClassBuilder
     */
    ClassBuilder includeBootstrap();

    /**
     * 是否包含被Bootstrap所加载的类
     *
     * @param isIncludeBootstrap TRUE:包含Bootstrap;FALSE:不包含Bootstrap;
     * @return ClassBuilder
     * @see #includeBootstrap()
     */
    ClassBuilder isIncludeBootstrap(boolean isIncludeBootstrap);

    /**
     * {@link FilterBuilder#onClass}所指定的类，检索路径是否包含子类（实现类）
     * <ul>
     * <li>如果onClass()了一个接口，则匹配时会搜索这个接口的所有实现类</li>
     * <li>如果onClass()了一个类，则匹配时会搜索这个类的所有子类</li>
     * </ul>
     *
     * @return ClassBuilder
     */
    ClassBuilder includeSubClasses();

    /**
     * 是否包含被Bootstrap所加载的类
     *
     * @param isIncludeSubClasses TRUE:包含子类（实现类）;FALSE:不包含子类（实现类）;
     * @return ClassBuilder
     * @see #includeSubClasses()
     */
    ClassBuilder isIncludeSubClasses(boolean isIncludeSubClasses);

    /**
     * 类修饰匹配
     *
     * @param access access flag
     * @return ClassBuilder
     * @see AccessFlags
     */
    ClassBuilder withAccess(int access);

    /**
     * 类是否声明实现了某一组接口
     *
     * @param classes 接口组类型数组
     * @return ClassBuilder
     * @see #hasInterfaceTypes(String...)
     */
    ClassBuilder hasInterfaceTypes(Class<?>... classes);

    /**
     * 类是否声明实现了某一组接口
     * <p>
     * 接口组是一个可变参数组，匹配关系为"与"。即：当前类必须同时实现接口模式匹配组的所有接口才能匹配通过
     * </p>
     *
     * @param patterns 接口组匹配模版
     * @return ClassBuilder
     */
    ClassBuilder hasInterfaceTypes(String... patterns);

    /**
     * 类是否拥有某一组标注
     *
     * @param classes 标注组类型数组
     * @return ClassBuilder
     * @see #hasAnnotationTypes(String...)
     */
    ClassBuilder hasAnnotationTypes(Class<?>... classes);

    /**
     * 类是否拥有某一组标注
     * <p>
     * 标注组是一个可变参数组，匹配关系为"与"。即：当前类必须同时满足所有标注匹配条件！
     * </p>
     *
     * @param patterns 标注组匹配模版
     * @return ClassBuilder
     */
    ClassBuilder hasAnnotationTypes(String... patterns);

    /**
     * 构建行为匹配器，匹配任意行为
     * <p>
     * 等同于{@code onBehavior("*")}
     * </p>
     *
     * @return BehaviorBuilder
     */
    BehaviorBuilder onAnyBehavior();

    /**
     * 构建行为匹配器，匹配符合模版匹配名称的行为
     *
     * @param pattern 行为名称
     * @return BehaviorBuilder
     */
    BehaviorBuilder onBehavior(String pattern);

}
