package com.jvmbytes.filter.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 位操作工具类
 *
 * @author luanjia
 */
public class BitUtils {

    /**
     * 判断目标数是否在掩码范围内
     *
     * @param target    目标数
     * @param maskArray 掩码数组
     * @return true:在掩码范围内;false:不在掩码范围内
     */
    public static boolean isIn(int target, int... maskArray) {
        if (ArrayUtils.isEmpty(maskArray)) {
            return false;
        }
        for (int mask : maskArray) {
            if ((target & mask) == mask) {
                return true;
            }
        }
        return false;
    }

}
