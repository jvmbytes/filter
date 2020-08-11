package com.jvmbytes.filter;

import com.jbytes.spy.service.TuserService;
import com.jvmbytes.commons.structure.ClassStructureFactory;
import com.jvmbytes.commons.utils.ClassUtils;
import com.jvmbytes.filter.builder.FilterBuilder;
import com.jvmbytes.filter.matcher.FilterMatcher;
import com.jvmbytes.filter.matcher.Matcher;
import com.jvmbytes.filter.matcher.MatchingResult;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

public class TestServiceFilter {

    TuserService tuserService = new TuserService();

    @Test
    public void TestBuildFilter() throws Exception {
        List<Filter> serviceFilter =
                new FilterBuilder().onClass("com.jbytes.spy.service.*").onAnyBehavior().build();
        Matcher matcher = FilterMatcher.toAndGroupMatcher(serviceFilter);
        Iterator<Class<?>> classes = ClassUtils.iterateCurrentClassLoaderClasses();
        while (classes.hasNext()) {
            Class clazz = classes.next();
            MatchingResult result = matcher.matching(ClassStructureFactory.createClassStructure(clazz));
            if (result.isMatched()) {
                System.out.println(result.getBehaviorSignCodes());
            }
        }
    }
}
