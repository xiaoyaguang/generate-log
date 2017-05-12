package com.mokylin.sink.tools.loggenerator.component;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by yaguang.xiao on 2016/9/14.
 */
public class SpecialArgManager {

    private final Map<String, SpecialArg> values = Maps.newHashMap();

    public SpecialArgManager(List<SpecialArg> specialArgs) {

        for (SpecialArg specialArg : specialArgs) {
            values.put(specialArg.name(), specialArg);
        }
    }

    public SpecialArg get(String name) {
        return values.get(name);
    }
}
