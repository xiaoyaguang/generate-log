package com.mokylin.sink.tools.loggenerator.tencent;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

import com.mokylin.sink.tools.config.ConfigBuilder;
import com.mokylin.sink.tools.loggenerator.LogTemplateLoader;
import com.mokylin.sink.tools.loggenerator.component.LogField;
import com.mokylin.sink.tools.loggenerator.component.LogTemplate;
import com.mokylin.sink.tools.loggenerator.config.LogGeneratorConfig;
import com.mokylin.sink.tools.util.ExcelOperation;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yaguang.xiao on 2017/5/15.
 */
public class GenerateTencentReflection {

    private static final String SHEET_NAME = "format";

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        LogGeneratorConfig config =
                ConfigBuilder.buildConfigFromFileName(
                        args[0] + "/log_generator.config", LogGeneratorConfig.class);

        List<LogTemplate> logTemplates = LogTemplateLoader.loadLogTemplateFromXml(config.getLogConfigFilePath());

        generate(logTemplates, LoadTencentLogFormat.load());
    }

    public static void generate(List<LogTemplate> logTmpls, ArrayListMultimap<String, String> paramReflection) {
        String path = "generate-log-service/tencent_param_format.xlsx";

        Table<Integer, Integer, Integer> optypeActionid = HashBasedTable.create();


        new File(path).delete();

        ExcelOperation.createExcel(path, new ExcelOperation.ModifyWorkbookOperation() {
            @Override
            public void modify(Workbook wb, CellStyle cellStyle) {
                Sheet sheet = wb.createSheet(SHEET_NAME);

                int row = 0;

                for (LogTemplate logTemplate : logTmpls) {
                    Map<String, Integer> paramIndexMap = Maps.newHashMap();

                    if (logTemplate.useSpeicalArgType == LogTemplateLoader.UseSpeicalArgType.ALL) {
                        continue;
                    }

                    Row rowValue = sheet.createRow(row++);

                    checkArgument(!optypeActionid.contains(logTemplate.ioptype, logTemplate.iactionid),
                            "日志id重复！！【%s, %s】", logTemplate.ioptype, logTemplate.iactionid);
                    optypeActionid.put(logTemplate.ioptype, logTemplate.iactionid, 1);

                    Cell logNameCell = rowValue.createCell(0);
                    logNameCell.setCellStyle(cellStyle);
                    logNameCell.setCellType(Cell.CELL_TYPE_STRING);
                    logNameCell.setCellValue(logTemplate.logName);

                    Cell ioptypeCell = rowValue.createCell(1);
                    ioptypeCell.setCellStyle(cellStyle);
                    ioptypeCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    ioptypeCell.setCellValue(logTemplate.ioptype);

                    Cell iactionidCell = rowValue.createCell(2);
                    iactionidCell.setCellStyle(cellStyle);
                    iactionidCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    iactionidCell.setCellValue(logTemplate.iactionid);

                    int cellIndex = 3;
                    for (LogField field : logTemplate.fields) {

                        if (field.name.equals(LogTemplateLoader.OPERATOR_ID_NAME)) {
                            continue;
                        }

                        String fixParamName = TencentParamFixParamName.getFixParamName(field.name);

                        final String paramName;
                        if (fixParamName != null) {
                            paramName = fixParamName;
                        } else {
                            String tencentParamType = TencentParamFixParamName.convertMolinParamTypeToTencentParamType(field.type);
                            Integer index = paramIndexMap.get(tencentParamType);
                            if (index == null) {
                                index = 0;
                            }

                            if (logTemplate.logName.equals("RoleLogout") && field.name.equals("dtLoginTime")) {
                                System.out.println();
                            }

                            List<String> paramNames = paramReflection.get(tencentParamType);
                            checkArgument(index < paramNames.size(), "%s类型的参数不够, logName:%s, fieldName:%s", tencentParamType, logTemplate.logName, field.name);

                            paramName = paramNames.get(index);

                            paramIndexMap.put(tencentParamType, index + 1);
                        }

                        field.tencentParamName = paramName;

                        Cell paramNameCell = rowValue.createCell(cellIndex++);
                        paramNameCell.setCellStyle(cellStyle);
                        paramNameCell.setCellType(Cell.CELL_TYPE_STRING);
                        paramNameCell.setCellValue(paramName);

                        Cell paramDescCell = rowValue.createCell(cellIndex++);
                        paramDescCell.setCellStyle(cellStyle);
                        paramDescCell.setCellType(Cell.CELL_TYPE_STRING);
                        paramDescCell.setCellValue(field.description);
                    }
                }
            }
        });
    }

}
