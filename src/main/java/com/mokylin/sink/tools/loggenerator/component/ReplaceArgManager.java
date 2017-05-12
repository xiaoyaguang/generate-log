package com.mokylin.sink.tools.loggenerator.component;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 *
 * Created by yaguang.xiao on 2016/9/14.
 */
public class ReplaceArgManager {

    private final List<ReplaceArg> replaceArgs;

    public ReplaceArgManager(List<ReplaceArg> replaceArgs) {
        this.replaceArgs = replaceArgs;

        Set<TypeInfo> targetTypes = Sets.newHashSet();
        for (ReplaceArg replaceArg : replaceArgs) {
            if (targetTypes.contains(replaceArg.sourceType())) {
                throw new RuntimeException(
                        "targetType为" + replaceArg.sourceType().getName() +
                                "的枚举下面不能再配置sourceType为" +
                                replaceArg.sourceType().getName() + "的枚举");
            }

            targetTypes.add(replaceArg.targetType());
        }
    }

    public List<ReplaceArg> values() {
        return replaceArgs;
    }
}
