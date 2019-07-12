package com.jvmbytes.filter.builder;

import com.jvmbytes.filter.Filter;
import com.jvmbytes.filter.matcher.Matcher;

import java.util.List;

/**
 * 构建器
 *
 * @author wongoo
 */
public interface Builder {

    /**
     * build filters
     *
     * @return filters
     */
    List<Filter> build();

    /**
     * build matcher
     *
     * @return matcher
     */
    Matcher matcher();
}
