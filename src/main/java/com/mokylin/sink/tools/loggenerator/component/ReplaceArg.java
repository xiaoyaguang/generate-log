package com.mokylin.sink.tools.loggenerator.component;

/**
 *
 * Created by yaguang.xiao on 2016/9/14.
 */
public class ReplaceArg {

    private final TypeInfo sourceType;
    private final TypeInfo targetType;
    private final String howToGet;
    public final int index;

    public ReplaceArg(TypeInfo sourceType, TypeInfo targetType, String howToGet, int index) {
        this.sourceType = sourceType;

        this.targetType = targetType;
        this.howToGet = howToGet;
        this.index = index;
    }

    public TypeInfo sourceType() {
        return sourceType;
    }

    public TypeInfo targetType() {
        return targetType;
    }

    public String howToGet() {
        return howToGet;
    }
}
