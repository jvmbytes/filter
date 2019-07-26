package com.jvmbytes.filter.builder;

import com.jvmbytes.commons.utils.PatternType;

import java.util.ArrayList;
import java.util.List;


/**
 * 模式匹配组列表
 *
 * @author luanjia
 */
public class PatternGroupList {

    final List<PatternGroup> patternGroups = new ArrayList<PatternGroup>();

    /**
     * 添加模式匹配组
     */
    public void add(String... patternArray) {
        patternGroups.add(new PatternGroup(patternArray));
    }

    /**
     * 模式匹配With
     */
    public boolean patternWith(PatternType patternType, final String[] stringArray) {

        // 如果模式匹配组为空，说明不参与本次匹配
        if (patternGroups.isEmpty()) {
            return true;
        }

        for (final PatternGroup patternGroup : patternGroups) {
            if (patternGroup.matchingWith(patternType, stringArray)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 模式匹配Has
     */
    public boolean patternHas(PatternType patternType, final String[] stringArray) {

        // 如果模式匹配组为空，说明不参与本次匹配
        if (patternGroups.isEmpty()) {
            return true;
        }

        for (final PatternGroup patternGroup : patternGroups) {
            if (patternGroup.matchingHas(patternType, stringArray)) {
                return true;
            }
        }
        return false;
    }

}