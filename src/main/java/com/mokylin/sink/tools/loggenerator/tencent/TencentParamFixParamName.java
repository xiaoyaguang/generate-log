package com.mokylin.sink.tools.loggenerator.tencent;

/**
 * Created by yaguang.xiao on 2017/5/15.
 */
public enum TencentParamFixParamName {

    iversion("iVersion"), iuserip("vClientIp"), iworldid("iWorldId"), vuin("iUin"),
    ieventId("iEventId"), ieventTime("dtEventTime"), iroleId("iRoleId"), ijobId("iJobId"),
    ilevel("iRoleLevel"),;

    public final String molinParamName;

    TencentParamFixParamName(String molinParamName) {
        this.molinParamName = molinParamName;
    }

    public static String getFixParamName(String molinParamName) {
        for (TencentParamFixParamName tencentParamFixParamName : TencentParamFixParamName
                .values()) {
            if (molinParamName.equals(tencentParamFixParamName.molinParamName)) {
                return tencentParamFixParamName.name();
            }
        }

        return null;
    }

    public static String convertMolinParamTypeToTencentParamType(String molinParamType) {
        switch (molinParamType) {
            case "utinyint":
            case "int":
            case "uint":
            case "bigint":
            case "datetime":
                return "int32";
            case "string":
                return "string(32)";
        }

        throw new RuntimeException("无法识别的类型" + molinParamType);
    }
}
