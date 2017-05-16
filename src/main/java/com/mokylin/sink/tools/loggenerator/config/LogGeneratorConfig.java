package com.mokylin.sink.tools.loggenerator.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.mokylin.sink.tools.loggenerator.component.ReplaceArg;
import com.mokylin.sink.tools.loggenerator.component.SpecialArg;
import com.mokylin.sink.tools.loggenerator.component.TypeInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by yaguang.xiao on 2016/11/12.
 */
public class LogGeneratorConfig {

    private String logConfigFilePath;

    private String logTypeDefaultValue;

    private String logServiceDir;
    private String logServiceClassFileName;
    private String logServiceTemplateFilePath;

    private String molinLogServiceDir;
    private String molinLogServiceClassFileName;
    private String molinLogServiceTemplateFilePath;

    private String tencentLogServiceDir;
    private String tencentLogServiceClassFileName;
    private String tencentLogServiceTemplateFilePath;

    private String inputTencentFormatFilePath;
    private String outputTencentFormatFilePath;

    private Map<String, SpecialArgConfig> specialArgs;
    private Map<String, ReplaceArgConfig> replaceArgs;

    public String getLogConfigFilePath() {
        return logConfigFilePath;
    }

    public String getLogTypeDefaultValue() {
        return logTypeDefaultValue;
    }

    public String getLogServiceDir() {
        return logServiceDir;
    }

    public String getLogServiceClassFileName() {
        return logServiceClassFileName;
    }

    public String getLogServiceTemplateFilePath() {
        return logServiceTemplateFilePath;
    }

    public String getMolinLogServiceDir() {
        return molinLogServiceDir;
    }

    public String getMolinLogServiceClassFileName() {
        return molinLogServiceClassFileName;
    }

    public String getMolinLogServiceTemplateFilePath() {
        return molinLogServiceTemplateFilePath;
    }

    public String getTencentLogServiceDir() {
        return tencentLogServiceDir;
    }

    public String getTencentLogServiceClassFileName() {
        return tencentLogServiceClassFileName;
    }

    public String getTencentLogServiceTemplateFilePath() {
        return tencentLogServiceTemplateFilePath;
    }

    public String getInputTencentFormatFilePath() {
        return inputTencentFormatFilePath;
    }

    public String getOutputTencentFormatFilePath() {
        return outputTencentFormatFilePath;
    }

    public List<SpecialArg> toSpecialArgList() {
        List<SpecialArg> list = Lists.newArrayListWithCapacity(this.specialArgs.size());
        for (Entry<String, SpecialArgConfig> entry : this.specialArgs.entrySet()) {
            SpecialArgConfig value = entry.getValue();

            String fromTypeStr = value.getFromType();
            final TypeInfo fromType;
            if (isNullOrEmpty(fromTypeStr)) {
                fromType = null;
            } else {
                fromType = new TypeInfo(fromTypeStr);
            }

            String importClassesStr = value.getImportClasses();
            String[] importClasses = isNullOrEmpty(importClassesStr) ? new String[0] :
                    importClassesStr.split("\\|");

            list.add(new SpecialArg(entry.getKey(), fromType, value.getHowToGet(),
                    Sets.newHashSet(importClasses)));
        }

        return list;
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public List<ReplaceArg> toReplaceArgList() {
        List<ReplaceArg> list = Lists.newArrayListWithCapacity(this.replaceArgs.size());

        for (Entry<String, ReplaceArgConfig> entry : this.replaceArgs.entrySet()) {
            ReplaceArgConfig value = entry.getValue();

            TypeInfo sourceType = new TypeInfo(value.getSourceType());
            TypeInfo targetType = new TypeInfo(value.getTargetType());

            list.add(new ReplaceArg(sourceType, targetType, value.getHowToGet(), value.getIndex()));
        }

        Collections.sort(list, new Comparator<ReplaceArg>() {
            @Override
            public int compare(ReplaceArg o1, ReplaceArg o2) {
                return o1.index - o2.index;
            }
        });

        return list;
    }
}
