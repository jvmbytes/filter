package com.jvmbytes.filter.matcher;

import com.jvmbytes.commons.structure.BehaviorStructure;
import com.jvmbytes.commons.structure.ClassStructure;

import java.util.LinkedHashSet;

/**
 * @author luanjia
 */
public final class AndMatcher extends AbstractGroupMatcher {

    public AndMatcher(Matcher... matcherArray) {
        super(matcherArray);
    }

    @Override
    public MatchingResult matching(ClassStructure classStructure) {
        boolean isFirst = true;
        final MatchingResult result = new MatchingResult();
        final LinkedHashSet<BehaviorStructure> found = new LinkedHashSet<BehaviorStructure>();
        if (null == matcherArray) {
            return result;
        }
        for (final Matcher subMatcher : matcherArray) {
            final MatchingResult subResult = subMatcher.matching(classStructure);

            // 只要有一次匹配失败，剩下的是取交集运算，所以肯定也没戏，就不用花这个计算了
            if (!subResult.isMatched()) {
                return result;
            }

            if (isFirst) {
                found.addAll(subResult.getBehaviorStructures());
                isFirst = false;
            } else {
                found.retainAll(subResult.getBehaviorStructures());
            }
        }
        if (!found.isEmpty()) {
            result.getBehaviorStructures().addAll(found);
        }
        return result;
    }

}