package com.mokylin.sink.tools.util;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Iterator;

/**
 * 语言Excel操作的封装
 *
 * @author yaguang.xiao
 */
public class ExcelOperation {

    private static final Logger logger = LoggerFactory.getLogger(ExcelOperation.class);

    /**
     * 加载所有表格操作
     *
     * @author yaguang.xiao
     */
    public interface LoadSheetsOperation {
        /**
         * 加载所有表格
         */
        void load(Iterator<XSSFSheet> sheetIt);
    }

    /**
     * 加载指定excel文件
     */
    public static void loadExcel(String path, LoadSheetsOperation operation) {
        if (!(new File(path).exists())) {
            return;
        }

        XSSFWorkbook workbook = null;
        InputStream ins = null;
        try {
            ins = new FileInputStream(path);
            workbook = new XSSFWorkbook(ins);
            Iterator<XSSFSheet> sheetIt = workbook.iterator();
            operation.load(sheetIt);
        } catch (FileNotFoundException e) {
            logger.error(MessageFormat.format("load error!filePath:{0}", path), e);
        } catch (IOException e) {
            logger.error(MessageFormat.format("load error!filePath:{0}", path), e);
        } catch (Exception e) {
            logger.error(MessageFormat.format("load error!filePath:{0}", path), e);
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * 加载Excel行的操作
     *
     * @author yaguang.xiao
     */
    public interface LoadRowOperation {
        /**
         * 加载行
         */
        void load(Row row);
    }

    /**
     * 加载指定excel文件
     */
    public static void loadExcel(String path, String sheetName, LoadRowOperation operation) {
        if (!(new File(path).exists())) {
            return;
        }

        Workbook workbook = null;
        InputStream ins = null;
        try {
            ins = new FileInputStream(path);
            workbook = new XSSFWorkbook(ins);
            Sheet sheet = workbook.getSheet(sheetName);
            int rowNumber = sheet.getLastRowNum();
            for (int rowIdxForExcel = 0; rowIdxForExcel <= rowNumber; rowIdxForExcel++) {
                Row row = sheet.getRow(rowIdxForExcel);
                if (row == null) {
                    continue;
                } else {
                    operation.load(row);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("load error!", e);
        } catch (IOException e) {
            logger.error("load error!", e);
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * 修改操作
     *
     * @author yaguang.xiao
     */
    public interface ModifyWorkbookOperation {
        /**
         * 创建Sheet
         */
        void modify(Workbook wb, CellStyle cellStyle);
    }

    /**
     * 创建excel表格
     */
    public static void createExcel(String path, ModifyWorkbookOperation operation) {
        OutputStream fout = null;
        try {
            Workbook wb = new XSSFWorkbook();
            // 设置格式
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment((short) 2);
            Font cellFont = wb.createFont();
            cellFont.setFontName("宋体");
            cellFont.setFontHeightInPoints((short) 12);
            cellStyle.setFont(cellFont);
            cellStyle.setAlignment((short) 0);
            operation.modify(wb, cellStyle);
            fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
        } catch (FileNotFoundException e) {
            logger.error("Exception", e);
        } catch (IOException e) {
            logger.error("Exception", e);
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    logger.error("IOException", e);
                }
            }
        }
    }

    /**
     * 修改excel表格，此方法生成的文件不会覆盖之前的文件，而是在之前的文件上修改
     */
    public static void modifyExcel(String path, ModifyWorkbookOperation operation) {
        OutputStream fout = null;
        InputStream ins = null;
        try {
            Workbook wb = null;
            if (!(new File(path).exists())) {
                wb = new XSSFWorkbook();
            } else {
                ins = new FileInputStream(path);
                wb = new XSSFWorkbook(ins);
            }
            // 设置格式
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment((short) 2);
            Font cellFont = wb.createFont();
            cellFont.setFontName("宋体");
            cellFont.setFontHeightInPoints((short) 12);
            cellStyle.setFont(cellFont);
            cellStyle.setAlignment((short) 0);
            operation.modify(wb, cellStyle);
            fout = new FileOutputStream(path);
            wb.write(fout);
            fout.flush();
        } catch (FileNotFoundException e) {
            logger.error("Exception", e);
        } catch (IOException e) {
            logger.error("Exception", e);
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    logger.error("IOException", e);
                }
            }
        }
    }
}
