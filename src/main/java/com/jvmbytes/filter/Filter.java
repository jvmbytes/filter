package com.jvmbytes.filter;

/**
 * 类和方法过滤器
 *
 * @author luanjia
 */
public interface Filter {

    /**
     * 过滤出匹配的类
     *
     * @param access                           access flag
     * @param javaClassName                    类名(全路径名称)
     * @param superClassTypeJavaClassName      父类(全路径名称)
     * @param interfaceTypeJavaClassNameArray  接口类型名称数组
     * @param annotationTypeJavaClassNameArray 标注原数据类型名称数组（注意，此参数尚未支持，只是预留一个API占位）
     * @return true:匹配;false:不匹配;
     */
    boolean doClassFilter(int access,
                          String javaClassName,
                          String superClassTypeJavaClassName,
                          String[] interfaceTypeJavaClassNameArray,
                          String[] annotationTypeJavaClassNameArray);

    /**
     * 过滤出匹配的方法
     * <p>
     * 严格意义上来说，方法{@link Filter#doMethodFilter(int, String, String[], String[], String[])}被调用的时候，
     * 一定是{@link Filter#doClassFilter(int, String, String, String[], String[])}上一次返回true的调用。
     * 所以可以通过简单的引用就可以在doMethodFilter执行的时候拿到doClassFilter的信息
     * </p>
     *
     * @param access                           access flag
     * @param javaMethodName                   方法名称(包括类名称,静态方法名,普通方法名和构造函数)
     * @param parameterTypeJavaClassNameArray  参数类型名称数组
     * @param throwsTypeJavaClassNameArray     声明异常类型名称数组
     * @param annotationTypeJavaClassNameArray 标注原数据类型名称数组（注意，此参数尚未支持，只是预留一个API占位）
     * @return true:匹配;false:不匹配
     */
    boolean doMethodFilter(int access,
                           String javaMethodName,
                           String[] parameterTypeJavaClassNameArray,
                           String[] throwsTypeJavaClassNameArray,
                           String[] annotationTypeJavaClassNameArray);

    /**
     * 是否搜索子类或实现类
     *
     * @return TRUE:搜索子类或实现类;FALSE:不搜索
     */
    boolean isIncludeSubClasses();

    /**
     * 是否搜索来自BootstrapClassLoader所加载的类
     *
     * @return TRUE:搜索；FALSE：不搜索；最终容器是否会对BootstrapClassLoader所加载的类进行处理，
     */
    boolean isIncludeBootstrap();

}
