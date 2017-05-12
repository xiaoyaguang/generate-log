package com.mokylin.sink.tools.loggenerator.component;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * Created by yaguang.xiao on 2016/11/12.
 */
public class TypeInfo {

    private final String name;
    private final String simpleName;

    public TypeInfo(String name) {
        this.name = name;
        checkNotNull(this.name, "FromType构造函数中typeFullName不能为null");

        this.simpleName = this.name.substring(this.name.lastIndexOf(".") + 1);
    }

    public String getName() {
        return this.name;
    }

    public String getSimpleName() {
        return this.simpleName;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof TypeInfo &&
                this.name.equals(((TypeInfo) obj).name);
    }
}
