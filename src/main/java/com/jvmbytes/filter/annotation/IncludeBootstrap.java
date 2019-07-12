package com.jvmbytes.filter.annotation;


import com.jvmbytes.filter.Filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拥有此标记的{@link Filter}，
 * 将能匹配到来自{@code BootstrapClassLoader}所加载的类
 *
 * @author luanjia
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IncludeBootstrap {
}
