package com.mokylin.sink.tools.loggenerator.component;

import java.util.Set;

/**
 * 给模板使用的日志实体类
 * @author yaguang.xiao
 *
 */
public class LogEntityForTemplate {

    private String logName;
    private String description;
    private String remark;
    private String fieldDescription;
    private String args;
    private String eventIdGenerateType;
    private String noEventIdArgs;
    private String containEventIdMethodCall;
    private String prefix;
    private String setter;
    private Set<String> importClasses;
    private String howToGetOperator;
    private String howToGetServerId;

    public LogEntityForTemplate(String logName, String description,
            String remark, String fieldDescription, String args, String prefix, String setter,
            Set<String> importClasses, String howToGetOperator, String noEventIdArgs,
            String containEventIdMethodCall, String howToGetServerId,
            String eventIdGenerateType) {
        this.logName = logName;
        this.description = description;
        this.remark = remark;
        this.fieldDescription = fieldDescription;
        this.args = args;
        this.prefix = prefix;
        this.setter = setter;
        this.importClasses = importClasses;
        this.howToGetOperator = howToGetOperator;
        this.noEventIdArgs = noEventIdArgs;
        this.containEventIdMethodCall = containEventIdMethodCall;
        this.howToGetServerId = howToGetServerId;
        this.eventIdGenerateType = eventIdGenerateType;
    }

    public String getLogName() {
        return logName;
    }

    public String getDescription() {
        return description;
    }

    public String getRemark() {
        return remark;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public String getArgs() {
        return args;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSetter() {
        return setter;
    }

    public Set<String> getImportClasses() {
        return importClasses;
    }

    public String getHowToGetOperator() {
        return howToGetOperator;
    }

    public String getNoEventIdArgs() {
        return noEventIdArgs;
    }

    public String getContainEventIdMethodCall() {
        return containEventIdMethodCall;
    }

    public String getHowToGetServerId() {
        return howToGetServerId;
    }

    public String getEventIdGenerateType() {
        return eventIdGenerateType;
    }
}
