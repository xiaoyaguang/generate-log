package com.mokylin.sink.tools.loggenerator.tencent;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import com.mokylin.sink.tools.util.ExcelOperation;
import com.mokylin.sink.tools.util.PoiUtils;

import org.apache.poi.ss.usermodel.Row;

import java.util.Collection;
import java.util.Map;

/**
 * Created by yaguang.xiao on 2017/5/15.
 */
public class LoadTencentLogFormat {

    public static ArrayListMultimap<String, String> load(String inputTencentFormatFilePath) {
        ArrayListMultimap<String, String> reflection = ArrayListMultimap.create();

        ExcelOperation.loadExcel(inputTencentFormatFilePath, "Sheet1", new ExcelOperation.LoadRowOperation() {
            @Override
            public void load(Row row) {
                if (row.getRowNum() < 14) {
                    return;
                }

                String paramName = PoiUtils.getStringValue(row.getCell(0));

                String paramType = PoiUtils.getStringValue(row.getCell(1));
                if (paramType.equals("uint32")) {
                    paramType = "int32";
                }

                reflection.put(paramType, paramName);
            }
        });

        return reflection;
    }

}
