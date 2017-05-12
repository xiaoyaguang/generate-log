package com.mokylin.sink.tools.loggenerator.component;

import java.util.List;

/**
 * 日志模板
 * @author yaguang.xiao
 *
 */
public class LogTemplate {

    public final String logName;
    public final String logDescprition;
    public final String logRemark;
    public final String eventIdGenerateType;
    public final List<LogField> fields;
    public final boolean useSpecialArg;

    /**
     * @param logName    日志名字
     * @param logDescription    日志描述
     * @param logRemark    日志备注
     * @param eventIdGenerateType
     * @param fields    日志字段
     * @param dontUseSpecialArg
     */
    public LogTemplate(String logName, String logDescription, String logRemark,
            String eventIdGenerateType, List<LogField> fields, boolean dontUseSpecialArg) {
        this.logName = logName;
        this.logDescprition = logDescription;
        this.logRemark = logRemark;
        this.eventIdGenerateType = eventIdGenerateType;
        this.fields = fields;
        this.useSpecialArg = !dontUseSpecialArg;
    }

}
