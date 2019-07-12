package com.jvmbytes.filter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 隐形屏障
 * <ul>
 * <li>被标注的类及其子类将不会被感知</li>
 * <li>被标注的ClassLoader所加载的类都不会被感知</li>
 * </ul>
 *
 * @author luanjia
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Stealth {

}
