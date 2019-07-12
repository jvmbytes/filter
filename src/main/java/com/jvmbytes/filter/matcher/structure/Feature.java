package com.jvmbytes.filter.matcher.structure;

/**
 * 类结构、行为的特征统一的描述
 * <p>
 * 其中一些特征是另外一些没有的，比如：行为结构中就不会存在Enum的类型。不过没关系，在行为上，{@link #isEnum()}返回的一定是false
 * </p>
 *
 * @author luanjia
 */
public interface Feature {

    boolean isPublic();

    boolean isPrivate();

    boolean isProtected();

    boolean isStatic();

    boolean isFinal();

    boolean isInterface();

    boolean isNative();

    boolean isAbstract();

    boolean isEnum();

    boolean isAnnotation();

}
