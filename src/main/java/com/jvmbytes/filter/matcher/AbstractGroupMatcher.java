package com.jvmbytes.filter.matcher;

/**
 * @author luanjia
 */
public abstract class AbstractGroupMatcher implements Matcher {

    protected Matcher[] matcherArray;

    public AbstractGroupMatcher(final Matcher... matcherArray) {
        this.matcherArray = matcherArray;
    }

}
