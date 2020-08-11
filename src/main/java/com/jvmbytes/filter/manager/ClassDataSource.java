package com.jvmbytes.filter.manager;

import com.jvmbytes.commons.structure.ClassStructure;
import com.jvmbytes.commons.structure.ClassStructureFactory;
import com.jvmbytes.commons.utils.ClassUtils;
import com.jvmbytes.filter.matcher.MatchHandler;
import com.jvmbytes.filter.matcher.Matcher;
import com.jvmbytes.filter.matcher.MatchingResult;
import com.jvmbytes.filter.matcher.UnsupportedMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wongoo
 */
public class ClassDataSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Instrumentation inst;
    private boolean isEnableUnsafe;

    public ClassDataSource(Instrumentation inst, boolean isEnableUnsafe) {
        this.inst = inst;
        this.isEnableUnsafe = isEnableUnsafe;
    }

    public void matchHandle(final Matcher matcher, final boolean isRemoveUnsupported, final MatchHandler handler) {
        if (null == matcher || handler == null) {
            return;
        }
        final Iterator<Class<?>> itForLoaded = inst != null ?
                ClassUtils.iteratorForLoadedClasses(inst)
                : ClassUtils.iterateCurrentClassLoaderClasses();

        while (itForLoaded.hasNext()) {
            final Class<?> clazz = itForLoaded.next();
            // 过滤掉对于JVM认为不可修改的类
            if (isRemoveUnsupported
                    && inst != null
                    && !inst.isModifiableClass(clazz)) {
                continue;
            }

            try {
                if (isRemoveUnsupported) {
                    if (UnsupportedMatcher.isUnsupportedClass(clazz.getName()) ||
                            UnsupportedMatcher.isFromStealthClassLoader(clazz.getClassLoader(), isEnableUnsafe)) {
                        continue;
                    }
                }

                ClassStructure classStructure = ClassStructureFactory.createClassStructure(clazz);

                if (isRemoveUnsupported && UnsupportedMatcher.isStealthClass(classStructure)) {
                    continue;
                }

                MatchingResult result = matcher.matching(classStructure, isRemoveUnsupported);
                if (result != null && result.isMatched()) {
                    handler.handle(clazz, classStructure);
                }
            } catch (Throwable cause) {
                // 在这里可能会遇到非常坑爹的模块卸载错误
                // 当一个URLClassLoader被动态关闭之后，但JVM已经加载的类并不知情（因为没有GC）
                // 所以当尝试获取这个类更多详细信息的时候会引起关联类的ClassNotFoundException等未知的错误（取决于底层ClassLoader的实现）
                // 这里没有办法穷举出所有的异常情况，所以catch Throwable来完成异常容灾处理
                // 当解析类出现异常的时候，直接简单粗暴的认为根本没有这个类就好了
                logger.debug("remove from findForReTransform, because loading class:{} occur an exception", clazz.getName(), cause);
            }
        }
    }

    public List<Class<?>> find(final Matcher matcher) {
        return find(matcher, true);
    }

    public List<Class<?>> find(final Matcher matcher,
                               final boolean isRemoveUnsupported) {
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        matchHandle(matcher, isRemoveUnsupported, new MatchHandler() {
            @Override
            public void handle(Class<?> clazz, ClassStructure classStructure) {
                classes.add(clazz);
            }
        });
        return classes;
    }
}
