package com.mokylin.sink.tools.loggenerator.component;

import java.util.Set;

/**
 * 给模板使用的日志实体类
 *
 * @author yaguang.xiao
 */
public class LogEntityForTemplate {

    private String logName;
    private String description;
    private String remark;
    private String fieldDescription;
    private String args;
    private String eventIdGenerateType;
    private String useSpecialArgType;
    private String noEventIdArgs;
    private String containEventIdMethodCall;
    private String prefix;
    private String setter;
    private Set<String> importClasses;
    private String howToGetOperator;
    private String howToGetServerId;
    private String logType;
    private String methodCallArgs;

    private String molinArgs;
    private String molinSetter;

    private String tencentArgs;
    private String tencentSetter;
    private String ioptype;
    private String iactionid;

    public LogEntityForTemplate(String logName, String description,
                                String remark, String fieldDescription, String args, String prefix, String setter,
                                Set<String> importClasses, String howToGetOperator, String noEventIdArgs,
                                String containEventIdMethodCall, String howToGetServerId,
                                String eventIdGenerateType, String logType, String methodCallArgs, String molinArgs,
                                String molinSetter, String tencentArgs, String tencentSetter,
                                String ioptype, String iactionid, String useSpecialArgType) {
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
        this.logType = logType;
        this.methodCallArgs = methodCallArgs;
        this.molinArgs = molinArgs;
        this.molinSetter = molinSetter;
        this.tencentArgs = tencentArgs;
        this.tencentSetter = tencentSetter;
        this.ioptype = ioptype;
        this.iactionid = iactionid;
        this.useSpecialArgType = useSpecialArgType;
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

    public String getLogType() {
        return logType;
    }

    public String getMethodCallArgs() {
        return methodCallArgs;
    }

    public String getMolinArgs() {
        return molinArgs;
    }

    public String getMolinSetter() {
        return molinSetter;
    }

    public String getTencentArgs() {
        return tencentArgs;
    }

    public String getTencentSetter() {
        return tencentSetter;
    }

    public String getIoptype() {
        return ioptype;
    }

    public String getIactionid() {
        return iactionid;
    }

    public String getUseSpecialArgType() {
        return useSpecialArgType;
    }
}
