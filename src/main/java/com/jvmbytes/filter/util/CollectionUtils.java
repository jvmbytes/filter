package com.jvmbytes.filter.util;

import java.util.Collection;

/**
 * 集合操作工具类
 *
 * @author luanjia
 */
public class CollectionUtils {

    /**
     * 链式调用集合封装
     *
     * @param collection 集合类
     * @param e          追加元素
     * @param <E>        元素类型
     * @return Collection:this
     */
    public static <E> E add(Collection<E> collection, E e) {
        collection.add(e);
        return e;
    }

}
