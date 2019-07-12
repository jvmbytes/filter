package com.jvmbytes.filter.matcher.structure;

/**
 * 成员结构
 *
 * @author luanjia
 */
public class MemberStructure {

    private final Feature feature;
    private final String name;
    private final ClassStructure declaringClassStructure;

    public MemberStructure(final Feature feature,
                           final String name,
                           final ClassStructure declaringClassStructure) {
        this.feature = feature;
        this.name = name;
        this.declaringClassStructure = declaringClassStructure;
    }

    public String getName() {
        return name;
    }

    public ClassStructure getDeclaringClassStructure() {
        return declaringClassStructure;
    }

    public Feature getFeature() {
        return feature;
    }
}
