package com.mokylin.sink.tools.log;

import com.mokylin.collection.LongHashSet;
import com.mokylin.sink.tools.util.ExcelOperation;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by yaguang.xiao on 2017/9/19.
 */
public class TestLog {

    public static void main(String[] args) throws ParseException {
        LongHashSet heroIdSet = new LongHashSet();

        ExcelOperation.loadExcel("log/guo_zhan.xlsx", sheetIt -> {
            while (sheetIt.hasNext()) {
                XSSFSheet sheet = sheetIt.next();

                int lastRowNum = sheet.getLastRowNum();
                System.out.println(lastRowNum);

                for (int i = 1; i <= lastRowNum; i++) {
                    XSSFRow row = sheet.getRow(i);

                    long id = Long.valueOf(row.getCell(5).getRawValue());

                    heroIdSet.add(id);
                }
            }
        });

        System.out.println(heroIdSet.size());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long startTime = format.parse("2017-09-11 20:00:00").getTime();
        long endTime = format.parse("2017-09-11 21:30:00").getTime();

        LongHashSet loginHeroIdSet = new LongHashSet();

        ExcelOperation.loadExcel("log/role_login.xlsx", sheetIt -> {
            while (sheetIt.hasNext()) {
                XSSFSheet sheet = sheetIt.next();

                int lastRowNum = sheet.getLastRowNum();

                for (int i = 1; i <= lastRowNum; i++) {
                    XSSFRow row = sheet.getRow(i);

                    Date dateCellValue = row.getCell(4).getDateCellValue();
                    long time = dateCellValue.getTime();

                    //                    if (time >= startTime && time <= endTime) {
                    long id = Long.valueOf(row.getCell(5).getRawValue());
                    String name = row.getCell(7).getStringCellValue();
                    if (heroIdSet.contains(id)) {
                        loginHeroIdSet.add(id);
                        System.out.println(
                                "时间：" + format.format(dateCellValue) + ",姓名：" + name + ",角色ID：" +
                                        id);
                    }
                    //                    }

                }
            }
        });

        System.out.println("登录玩家个数：" + loginHeroIdSet.size());
    }

}
