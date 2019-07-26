package com.jvmbytes.filter.matcher;

import com.jvmbytes.commons.structure.ClassStructure;

/**
 * 匹配处理器
 *
 * @author wongoo
 */
public interface MatchHandler {

    /**
     * 匹配处理
     *
     * @param clazz          类
     * @param classStructure 类结构
     */
    void handle(Class<?> clazz, ClassStructure classStructure);
}
