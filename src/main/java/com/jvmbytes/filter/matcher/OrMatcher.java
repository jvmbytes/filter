package com.jvmbytes.filter.matcher;

import com.jvmbytes.filter.matcher.structure.ClassStructure;

/**
 * @author luanjia
 */
public final class OrMatcher extends AbstractGroupMatcher {

    public OrMatcher(Matcher... matcherArray) {
        super(matcherArray);
    }

    @Override
    public MatchingResult matching(final ClassStructure classStructure) {
        final MatchingResult result = new MatchingResult();
        if (null == matcherArray) {
            return result;
        }
        for (final Matcher subMatcher : matcherArray) {
            result.getBehaviorStructures().addAll(subMatcher.matching(classStructure).getBehaviorStructures());
        }
        return result;
    }

}