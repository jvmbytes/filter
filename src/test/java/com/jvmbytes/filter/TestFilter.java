package com.jvmbytes.filter;

import com.jbytes.spy.enhance.T1;
import com.jbytes.spy.enhance.T2;
import com.jbytes.spy.enhance.TI;
import com.jvmbytes.commons.structure.BehaviorStructure;
import com.jvmbytes.commons.structure.ClassStructure;
import com.jvmbytes.commons.structure.ClassStructureFactory;
import com.jvmbytes.commons.utils.ClassUtils;
import com.jvmbytes.filter.builder.FilterBuilder;
import com.jvmbytes.filter.manager.ClassDataSource;
import com.jvmbytes.filter.matcher.FilterMatcher;
import com.jvmbytes.filter.matcher.MatchHandler;
import com.jvmbytes.filter.matcher.Matcher;
import com.jvmbytes.filter.matcher.MatchingResult;
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
                .onClass("com.jvmbytes.commons.utils.*").onAnyBehavior().build();
        Assert.assertEquals(2, filters.size());

        Matcher matcher = FilterMatcher.toOrGroupMatcher(filters);
        Iterator<Class<?>> classes = ClassUtils.iterateCurrentClassLoaderClasses();
        while (classes.hasNext()) {
            Class clazz = classes.next();
            MatchingResult result = matcher.matching(ClassStructureFactory.createClassStructure(clazz), true);
            if (result != null && result.isMatched()) {
                System.out.println(clazz.getName());
            }
        }
    }

    @Test
    public void TestMatcher() {
        Matcher matcher = new FilterBuilder().onClass(Matcher.class)
                .includeSubClasses().includeBootstrap().onAnyBehavior().matcher();

        ClassStructure classStructure = ClassStructureFactory.createClassStructure(FilterMatcher.class);
        MatchingResult result = matcher.matching(classStructure, true);

        Assert.assertTrue(result != null && result.isMatched());

        System.out.println("--------> behavior sign codes");
        LinkedHashSet<String> behaviorSignCodes = result.getBehaviorSignCodes();
        for (String code : behaviorSignCodes) {
            System.out.println(code);
        }
    }

    @Test
    public void TestClassBehaviorStructures() {
        ClassStructure classStructure = ClassStructureFactory.createClassStructure(ClassDataSource.class);
        System.out.println("--------> behavior structures");
        List<BehaviorStructure> behaviorStructures = classStructure.getBehaviorStructures();
        for (BehaviorStructure structure : behaviorStructures) {
            System.out.println(structure);
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
