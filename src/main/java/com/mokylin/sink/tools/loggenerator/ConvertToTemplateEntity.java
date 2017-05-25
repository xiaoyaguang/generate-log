package com.mokylin.sink.tools.loggenerator;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.mokylin.sink.tools.loggenerator.LogTemplateLoader.EventIdGenerateType;
import com.mokylin.sink.tools.loggenerator.component.LogEntityForTemplate;
import com.mokylin.sink.tools.loggenerator.component.LogField;
import com.mokylin.sink.tools.loggenerator.component.LogTemplate;
import com.mokylin.sink.tools.loggenerator.component.ReplaceArg;
import com.mokylin.sink.tools.loggenerator.component.SpecialArg;
import com.mokylin.sink.tools.loggenerator.component.TypeInfo;
import com.mokylin.sink.tools.loggenerator.tencent.TencentParamFixParamName;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ConvertToTemplateEntity {

    static List<LogEntityForTemplate> convertToTemplateEntities(
            List<LogTemplate> logTemplates) {
        List<LogEntityForTemplate> logEntities = Lists.newLinkedList();
        for (LogTemplate logTemplate : logTemplates) {
            logEntities.add(convertToTemplateEntity(logTemplate));
        }

        return logEntities;
    }

    private static LogEntityForTemplate convertToTemplateEntity(
            LogTemplate logTemplate) {
        String logName = logTemplate.logName;
        String logDescription = logTemplate.logDescprition;
        String logRemark = logTemplate.logRemark;
        StringBuilder argsB = new StringBuilder();
        StringBuilder molinMethodCallArgsB = new StringBuilder();
        StringBuilder tencentMethodCallArgsB = new StringBuilder();
        StringBuilder molinArgsB = new StringBuilder();
        StringBuilder molinSetterB = new StringBuilder();
        StringBuilder tencentArgsB = new StringBuilder();
        StringBuilder tencentSetterB = new StringBuilder();
        StringBuilder noEventIdArgsB = new StringBuilder();
        StringBuilder containEventIdMethodCallB = new StringBuilder();
        containEventIdMethodCallB.append("log").append(logName).append("(");
        StringBuilder fieldDescB = new StringBuilder();
        Set<String> importClasses = Sets.newHashSet();
        String howToGetOperator = LogTemplateLoader.OPERATOR_ID_NAME;
        String howToGetServerId = LogTemplateLoader.IWORLD_ID;
        boolean needHaveOperatorIdArg = true;
        SpecialArg operateIDSArg =
                LogServiceGenerator.specialArgManager.get(LogTemplateLoader.OPERATOR_ID_NAME);
        if (operateIDSArg != null && operateIDSArg.fromType() == null &&
                logTemplate.useSpecialArg) {
            howToGetOperator = operateIDSArg.howToGet();
            needHaveOperatorIdArg = false;
        }
        SpecialArg worldIdSArg =
                LogServiceGenerator.specialArgManager.get(LogTemplateLoader.IWORLD_ID);
        if (worldIdSArg != null && worldIdSArg.fromType() == null && logTemplate.useSpecialArg) {
            howToGetServerId = worldIdSArg.howToGet();
        }

        // 用Set排重
        Set<TypeInfo> argsClasses = Sets.newHashSet();
        Set<SpecialArg> specialArgs = getSpecialArgs(logTemplate);
        for (SpecialArg specialArg : specialArgs) {
            if (specialArg.fromType() != null) {
                importClasses.add(specialArg.fromType().getName());
                argsClasses.add(specialArg.fromType());
            }

            importClasses.addAll(specialArg.importClasses());

            if (operateIDSArg != null && operateIDSArg.fromType() != null &&
                    operateIDSArg.fromType().equals(specialArg.fromType())) {
                howToGetOperator = StringUtil
                        .firstLetterToLowerCase(operateIDSArg.fromType().getSimpleName()) + "." +
                        operateIDSArg.howToGet();
                needHaveOperatorIdArg = false;
            }

            if (worldIdSArg != null && worldIdSArg.fromType() != null &&
                    worldIdSArg.fromType().equals(specialArg.fromType())) {
                howToGetServerId =
                        StringUtil.firstLetterToLowerCase(worldIdSArg.fromType().getSimpleName()) +
                                "." + worldIdSArg.howToGet();
            }
        }

        Map<TypeInfo, String> removedArgClasses = Maps.newHashMap();
        ListMultimap<TypeInfo, TypeInfo> sourceTargetMap = ArrayListMultimap.create();
        for (ReplaceArg replaceArg : LogServiceGenerator.replaceArgManager.values()) {
            if (argsClasses.contains(replaceArg.sourceType())) {
                argsClasses.remove(replaceArg.targetType());
                String howToGetTargetType =
                        StringUtil.firstLetterToLowerCase(
                                replaceArg.sourceType().getSimpleName()) +
                                "." + replaceArg.howToGet();
                removedArgClasses.put(replaceArg.targetType(), howToGetTargetType);

                for (TypeInfo targetType : sourceTargetMap.get(replaceArg.targetType())) {
                    String howToGet = removedArgClasses.get(targetType);
                    if (howToGet != null) {
                        howToGet = howToGetTargetType + howToGet.substring(howToGet.indexOf("."));
                        removedArgClasses.put(targetType, howToGet);
                    }
                }

                sourceTargetMap.put(replaceArg.sourceType(), replaceArg.targetType());
            }
        }

        if (operateIDSArg != null && operateIDSArg.fromType() != null &&
                logTemplate.useSpecialArg) {
            String howToGet = removedArgClasses.get(operateIDSArg.fromType());
            if (howToGet != null) {
                howToGetOperator = howToGet + "." + operateIDSArg.howToGet();
            }
        }

        if (worldIdSArg != null && worldIdSArg.fromType() != null && logTemplate.useSpecialArg) {
            String howToGet = removedArgClasses.get(worldIdSArg.fromType());
            if (howToGet != null) {
                howToGetServerId = howToGet + "." + worldIdSArg.howToGet();
            }
        }

        molinSetterB.append("e.data.append(\"").append(logName).append("\").append(\"|\")");

        for (int i = 0; i < logTemplate.fields.size(); i++) {
            LogField field = logTemplate.fields.get(i);
            String fieldName = field.name;

            String type = field.type;
            boolean isDate = false;
            if (type.equals("datetime")) {
                type = "long";
                isDate = true;
            }

            type = convertType(type, field.isBoolValue);

            if (needShowInArgs(fieldName, needHaveOperatorIdArg, logTemplate)) {
                fieldDescB.append("* @param ").append(fieldName).append("\t")
                        .append(field.description).append("\t").append(field.remark).append("\n");

                argsB.append(type).append(" ").append(fieldName).append(", ");

                if (logTemplate.eventIdGenerateType
                        .equals(EventIdGenerateType.AUTO_GENERATE_AND_PASS_IN.name())) {
                    if (!fieldName.equals(LogTemplateLoader.IEVENT_ID)) {
                        noEventIdArgsB.append(type).append(" ").append(fieldName).append(", ");
                        containEventIdMethodCallB.append(fieldName).append(", ");
                    } else {
                        containEventIdMethodCallB.append(fieldName).append(", ");
                    }
                }
            }

            if (!fieldName.equals(LogTemplateLoader.OPERATOR_ID_NAME)) {
                final String value;

                if (isSpecialArg(fieldName) && logTemplate.useSpecialArg) {
                    SpecialArg sArg = LogServiceGenerator.specialArgManager.get(fieldName);
                    if (removedArgClasses.containsKey(sArg.fromType())) {
                        String howToGet = removedArgClasses.get(sArg.fromType());

                        value = howToGet + "." + sArg.howToGet();
                    } else {
                        if (sArg.fromType() != null) {
                            value = StringUtil
                                    .firstLetterToLowerCase(sArg.fromType().getSimpleName()) + "." + sArg.howToGet();
                        } else {
                            value = sArg.howToGet();
                        }
                    }
                } else {
                    if (field.isBoolValue) {
                        value = fieldName + "? 1:0";
                    } else {
                        value = fieldName;
                    }
                }


                boolean isEventId = fieldName.equals(LogTemplateLoader.IEVENT_ID);

                molinSetterB.append(".append(");
                molinArgsB.append("String ");
                molinMethodCallArgsB.append("(").append(value).append(") + \"\"");
                if (isDate) {
                    molinSetterB.append("logManager.formatter.print(Long.parseLong(").append(fieldName).append("))");
                } else {
                    molinSetterB.append(fieldName);
                }
                molinArgsB.append(fieldName);
                molinSetterB.append(")");


                if (!isEventId) {
                    tencentArgsB.append("String ");
                    tencentSetterB.append("paramBuilder.append(\"&").append(field.tencentParamName).append("=\").append(");
                    if (isDate || field.isTime) {
                        tencentMethodCallArgsB.append("(").append(value).append(" / 1000) + \"\"");
                    } else if (TencentParamFixParamName.iroleId.name().equals(field.tencentParamName)) {
                        tencentMethodCallArgsB.append("(IDUtils.getUserID(").append(value).append(")) + \"\"");
                    } else {
                        tencentMethodCallArgsB.append("(").append(value).append(") + \"\"");
                    }

                    tencentArgsB.append(fieldName);
                    tencentSetterB.append(fieldName);
                    tencentSetterB.append(");");
                }


                if (!isLastField(i, logTemplate.fields)) {
                    molinMethodCallArgsB.append(", ");
                    molinArgsB.append(", ");
                    if (!isEventId) {
                        tencentArgsB.append(", ");
                        tencentMethodCallArgsB.append(", ");
                    }
                    molinSetterB.append(".append(\"|\")");
                } else {
                    molinSetterB.append(".append(\"\\n\");");
                }
            }
        }

        List<TypeInfo> argsClassList = Lists.newArrayList(argsClasses);
        Collections.sort(argsClassList, new Comparator<TypeInfo>() {
            @Override
            public int compare(TypeInfo info1, TypeInfo info2) {
                return info1.getName().compareTo(info2.getName());
            }
        });

        for (TypeInfo clazz : argsClassList) {
            String typeName = clazz.getSimpleName();
            String variableName = StringUtil.firstLetterToLowerCase(typeName);
            argsB.append(typeName).append(" ").append(variableName).append(", ");

            if (logTemplate.eventIdGenerateType
                    .equals(EventIdGenerateType.AUTO_GENERATE_AND_PASS_IN.name())) {
                noEventIdArgsB.append(typeName).append(" ").append(variableName).append(", ");
                containEventIdMethodCallB.append(variableName).append(", ");
            }
        }

        String args = argsB.toString();
        args = args.substring(0, args.length() - 2);
        String noEventIdArgs = noEventIdArgsB.toString();
        String containEventIdMethodCall = containEventIdMethodCallB.toString();
        if (logTemplate.eventIdGenerateType
                .equals(EventIdGenerateType.AUTO_GENERATE_AND_PASS_IN.name())) {
            noEventIdArgs = noEventIdArgs.substring(0, noEventIdArgs.length() - 2);
            containEventIdMethodCall =
                    containEventIdMethodCall.substring(0, containEventIdMethodCall.length() - 2);
        }

        return new LogEntityForTemplate(logName, logDescription, logRemark, fieldDescB.toString(),
                args, "", molinSetterB.toString(), importClasses, howToGetOperator, noEventIdArgs,
                containEventIdMethodCall + ");", howToGetServerId, logTemplate.eventIdGenerateType,
                logTemplate.logType, molinMethodCallArgsB.toString(), molinArgsB.toString(),
                molinSetterB.toString(), tencentMethodCallArgsB.toString(), tencentArgsB.toString(), tencentSetterB.toString(),
                logTemplate.ioptype + "", logTemplate.iactionid + "", logTemplate.useSpeicalArgType.name());
    }

    private static boolean needShowInArgs(String fieldName, boolean needHaveOperatorIdArg,
                                          LogTemplate logTemplate) {

        return (!fieldName.equals(LogTemplateLoader.IEVENT_ID) || logTemplate.eventIdGenerateType
                .equals(EventIdGenerateType.AUTO_GENERATE_AND_PASS_IN.name()) ||
                logTemplate.eventIdGenerateType.equals(EventIdGenerateType.PASS_IN.name()))
                && (!logTemplate.useSpecialArg || !isSpecialArg(fieldName) &&
                (!fieldName.equals(LogTemplateLoader.OPERATOR_ID_NAME) || needHaveOperatorIdArg));

    }

    private static boolean isSpecialArg(String name) {
        return LogServiceGenerator.specialArgManager.get(name) != null;
    }

    /**
     * 获取特殊参数
     */
    private static Set<SpecialArg> getSpecialArgs(LogTemplate logTemplate) {
        Set<SpecialArg> specialArgs = Sets.newHashSet();
        if (!logTemplate.useSpecialArg) {
            return specialArgs;
        }

        for (LogField field : logTemplate.fields) {
            SpecialArg sArg = LogServiceGenerator.specialArgManager.get(field.name);
            if (sArg != null) {
                specialArgs.add(sArg);
            }
        }

        return specialArgs;
    }

    private static boolean isLastField(int i, List<LogField> fields) {
        return i == fields.size() - 1;
    }

    private static String convertType(String type, boolean isBoolValue) {
        if (isBoolValue) {
            return "boolean";
        }

        switch (type) {
            case "uint":
            case "utinyint":
                return "int";
            case "string":
                return "String";
            case "bigint":
                return "long";
            default:
                return type;
        }
    }
}
