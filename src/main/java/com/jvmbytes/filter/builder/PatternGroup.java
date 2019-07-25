package com.jvmbytes.filter.builder;


import com.jvmbytes.util.ArrayUtils;
import com.jvmbytes.util.PatternType;

import static com.jvmbytes.util.StringUtils.patternMatching;

/**
 * 模式匹配组
 *
 * @author luanjia
 */
public class PatternGroup {

    final String[] patternArray;

    PatternGroup(String[] patternArray) {
        this.patternArray = ArrayUtils.isEmpty(patternArray)
                ? new String[0]
                : patternArray;
    }

    /**
     * stringArray中任意字符串能匹配上匹配模式
     */
    boolean anyMatching(PatternType patternType, final String[] stringArray,
                        final String pattern) {
        if (ArrayUtils.isEmpty(stringArray)) {
            return false;
        }
        for (final String string : stringArray) {
            if (patternMatching(patternType, string, pattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 匹配模式组中所有匹配模式都在目标中存在匹配通过的元素
     * 要求匹配组中每一个匹配项都在stringArray中存在匹配的字符串
     */
    boolean matchingHas(PatternType patternType, final String[] stringArray) {

        for (final String pattern : patternArray) {
            if (anyMatching(patternType, stringArray, pattern)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 匹配模式组中所有匹配模式都在目标中对应数组位置存在匹配通过元素
     * 要求字符串数组每一个位对应模式匹配组的每一个模式匹配表达式
     * stringArray[0] matching wildcardArray[0]
     * stringArray[1] matching wildcardArray[1]
     * stringArray[2] matching wildcardArray[2]
     * ...
     * stringArray[n] matching wildcardArray[n]
     */
    boolean matchingWith(PatternType patternType, final String[] stringArray) {

        // 长度不一样就不用不配了
        int length;
        if ((length = ArrayUtils.getLength(stringArray)) != ArrayUtils.getLength(patternArray)) {
            return false;
        }
        // 长度相同则逐个位置比较，只要有一个位置不符，则判定不通过
        for (int index = 0; index < length; index++) {
            if (!patternMatching(patternType, stringArray[index], patternArray[index])) {
                return false;
            }
        }
        // 所有位置匹配通过，判定匹配成功
        return true;
    }

}