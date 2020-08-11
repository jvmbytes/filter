package com.jvmbytes.filter.matcher;

import com.jvmbytes.commons.structure.ClassStructure;

/**
 * @author luanjia
 */
public final class OrMatcher extends AbstractGroupMatcher {

    public OrMatcher(Matcher... matcherArray) {
        super(matcherArray);
    }

    @Override
    public MatchingResult matching(final ClassStructure classStructure, boolean removeUnsupportedBehavior) {
        if (null == matcherArray) {
            return null;
        }

        final MatchingResult result = new MatchingResult();
        for (final Matcher subMatcher : matcherArray) {
            MatchingResult subResult = subMatcher.matching(classStructure, removeUnsupportedBehavior);
            if (subResult == null || !subResult.isMatched()) {
                continue;
            }
            result.getBehaviorStructures().addAll(subResult.getBehaviorStructures());
        }
        return result;
    }

}