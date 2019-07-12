package com.jvmbytes.filter.util.collection;

/**
 * 堆栈
 *
 * @param <E> 堆栈元素类型
 * @author luanjia
 */
public interface Stack<E> {

    E pop();

    void push(E e);

    E peek();

    boolean isEmpty();

    boolean isLast();

    E peekLast();

    int deep();

}
