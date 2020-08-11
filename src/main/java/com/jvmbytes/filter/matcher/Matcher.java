package com.jvmbytes.filter.matcher;

import com.jvmbytes.commons.structure.ClassStructure;

/**
 * 匹配器
 * <p>
 * 可以判断当前类结构是否符合要求
 * </p>
 *
 * @author luanjia
 */
public interface Matcher {

    /**
     * 匹配类结构
     *
     * @param classStructure            类结构
     * @param removeUnsupportedBehavior 是否剔除不支持的方法
     * @return 匹配结果
     */
    MatchingResult matching(ClassStructure classStructure, boolean removeUnsupportedBehavior);


}
