package com.mokylin.sink.tools.loggenerator.component;

import java.util.Set;

/**
 *
 * Created by yaguang.xiao on 2016/9/14.
 */
public class SpecialArg {

    private final String name;
    private final TypeInfo fromType;
    private final String howToGet;
    private final Set<String> importClasses;

    public SpecialArg(String name, TypeInfo fromType, String howToGet, Set<String> importClasses) {
        this.name = name;
        this.fromType = fromType;
        this.howToGet = howToGet;
        this.importClasses = importClasses;
    }

    String name() {
        return this.name;
    }

    public TypeInfo fromType() {
        return this.fromType;
    }

    public String howToGet() {
        return this.howToGet;
    }

    public Set<String> importClasses() {
        return this.importClasses;
    }

}
