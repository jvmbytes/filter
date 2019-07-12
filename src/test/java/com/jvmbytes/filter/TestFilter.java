package com.jvmbytes.filter;

import com.jvmbytes.filter.builder.FilterBuilder;
import com.jvmbytes.filter.manager.ClassDataSource;
import com.jvmbytes.filter.matcher.FilterMatcher;
import com.jvmbytes.filter.matcher.MatchHandler;
import com.jvmbytes.filter.matcher.Matcher;
import com.jvmbytes.filter.matcher.MatchingResult;
import com.jvmbytes.filter.matcher.structure.BehaviorStructure;
import com.jvmbytes.filter.matcher.structure.ClassStructure;
import com.jvmbytes.filter.matcher.structure.ClassStructureFactory;
import com.jvmbytes.filter.util.ClassUtils;
import com.jvmtest.T1;
import com.jvmtest.T2;
import com.jvmtest.TI;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author wongoo
 */
public class TestFilter {

    @Test
    public void TestBuildFilter() throws Exception {
        List<Filter> filters = new FilterBuilder().onClass("com.jvmbytes.*").onAnyBehavior().build();
        Assert.assertEquals(1, filters.size());

        filters = new FilterBuilder().onClass("com.jvmbytes.filter.Filter")
                .includeSubClasses().includeBootstrap().onAnyBehavior()
                .onClass("com.jvmbytes.filter.util.*").onAnyBehavior().build();
        Assert.assertEquals(2, filters.size());

        Matcher matcher = FilterMatcher.toOrGroupMatcher(filters);
        Iterator<Class<?>> classes = ClassUtils.iterateCurrentClassLoaderClasses();
        while (classes.hasNext()) {
            Class clazz = classes.next();
            boolean matched = matcher.matching(ClassStructureFactory.createClassStructure(clazz)).isMatched();
            if (matched) {
                System.out.println(clazz.getName());
            }
        }
    }

    @Test
    public void TestMatcher() {
        Matcher matcher = new FilterBuilder().onClass(Matcher.class)
                .includeSubClasses().includeBootstrap().onAnyBehavior().matcher();

        ClassStructure classStructure = ClassStructureFactory.createClassStructure(FilterMatcher.class);
        MatchingResult result = matcher.matching(classStructure);

        Assert.assertTrue(result.isMatched());

        System.out.println("--------> behavior sign codes");
        LinkedHashSet<String> behaviorSignCodes = result.getBehaviorSignCodes();
        for (String code : behaviorSignCodes) {
            System.out.println(code);
        }
    }

    @Test
    public void TestFilterInterface() {
        System.out.println(T1.class);
        System.out.println(T2.class);
        Matcher matcher = new FilterBuilder().onClass(TI.class).includeSubClasses().includeBootstrap().onAnyBehavior().matcher();

        ClassDataSource dataSource = new ClassDataSource(null, true);
        List<Class<?>> classes = dataSource.find(matcher);
        Assert.assertEquals(2, classes.size());
    }

    @Test
    public void TestGetSignCode() {
        System.out.println(T1.class);
        System.out.println(T2.class);
        Matcher matcher = new FilterBuilder().onClass(TI.class).includeSubClasses().includeBootstrap().onAnyBehavior().matcher();

        ClassDataSource dataSource = new ClassDataSource(null, true);

        dataSource.matchHandle(matcher, false, new MatchHandler() {
            @Override
            public void handle(Class<?> clazz, ClassStructure classStructure) {
                List<BehaviorStructure> behaviorStructures = classStructure.getBehaviorStructures();
                for (BehaviorStructure structure : behaviorStructures) {
                    System.out.println(structure.getSignCode());
                }
            }
        });
    }
}
