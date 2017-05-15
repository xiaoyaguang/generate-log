package com.mokylin.sink.tools.loggenerator.component;

import com.mokylin.sink.tools.loggenerator.LogTemplateLoader;

import java.util.List;

/**
 * 日志模板
 *
 * @author yaguang.xiao
 */
public class LogTemplate {

    public final int ioptype;
    public final int iactionid;
    public final String logName;
    public final String logDescprition;
    public final String logRemark;
    public final String eventIdGenerateType;
    public final List<LogField> fields;
    public final boolean useSpecialArg;
    public final String logType;
    public final LogTemplateLoader.UseSpeicalArgType useSpeicalArgType;

    /**
     * @param logName        日志名字
     * @param logDescription 日志描述
     * @param logRemark      日志备注
     * @param fields         日志字段
     */
    public LogTemplate(int ioptype, int iactionid, String logName, String logDescription, String logRemark,
                       String eventIdGenerateType, List<LogField> fields, boolean dontUseSpecialArg, String logType, LogTemplateLoader.UseSpeicalArgType useSpeicalArgType) {
        this.ioptype = ioptype;
        this.iactionid = iactionid;
        this.logName = logName;
        this.logDescprition = logDescription;
        this.logRemark = logRemark;
        this.eventIdGenerateType = eventIdGenerateType;
        this.fields = fields;
        this.useSpecialArg = !dontUseSpecialArg;
        this.logType = logType;
        this.useSpeicalArgType = useSpeicalArgType;
    }

    public LogTemplate copyUseSpeicalArgTemplate() {
        return new LogTemplate(ioptype, iactionid, logName, logDescprition, logRemark, eventIdGenerateType, fields, false, logType, LogTemplateLoader.UseSpeicalArgType.USE);
    }
}
