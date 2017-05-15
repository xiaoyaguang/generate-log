package com.mokylin.sink.tools.util;

import com.google.common.base.Strings;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 使用Poi实现的Excel解析工具类
 */
public class PoiUtils {

    private static final Logger log = LoggerFactory.getLogger(PoiUtils.class);

    private static final NumberFormat FMT_NUMBER = new DecimalFormat("0.#########");

    public static String collectCellInfo(Cell cell) {
        StringBuffer sb = new StringBuffer();
        Sheet sheet = cell.getSheet();
        sb.append("sheet.name=" + sheet.getSheetName());
        sb.append(", row=" + (cell.getRowIndex() + 1));
        sb.append(", col=" + convertColNum2Alphabet(cell.getColumnIndex() + 1));
        sb.append(", type=" + cell.getCellType());
        sb.append(", val=" + cell.toString());
        return sb.toString();
    }

    /**
     * 按照整形int读取cell中的值
     *
     * @return 0, 当cell为空时;否则返回其内容所表示的值
     */
    public static int getIntValue(Cell cell) {
        if (cell == null || cell.toString().trim().length() == 0) {
            return 0;
        }
        try {
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            log.error("Template.Parse.Value.Error!" + collectCellInfo(cell));
            return (int) Double.parseDouble(cell.toString());
        }
    }

    /**
     * 按照整形short读取cell中的值
     *
     * @return 0, 当cell为空时;否则返回其内容所表示的值
     */
    public static short getShortValue(Cell cell) {
        if (cell == null || cell.toString().length() == 0) {
            return 0;
        }
        try {
            return (short) cell.getNumericCellValue();
        } catch (Exception e) {
            log.error("Template.Parse.Value.Error!" + collectCellInfo(cell));
            return (short) Double.parseDouble(cell.toString());
        }
    }

    /**
     * 按照boolean读取cell中的值
     *
     * @return false, 当cell为空时;否则返回其内容所表示的值
     */
    public static boolean getBooleanValue(Cell cell) {
        if (cell == null || cell.toString().length() == 0) {
            return false;
        }
        try {
            return cell.getBooleanCellValue();
        } catch (Exception e) {
            log.error("Template.Parse.Value.Error!" + collectCellInfo(cell));
            return false;
        }
    }

    /**
     * @param cell
     * @return
     */
    public static byte getByteValue(Cell cell) {
        if (cell == null || cell.toString().length() == 0) {
            return 0;
        }
        try {
            return (byte) cell.getNumericCellValue();
        } catch (Exception e) {
            log.error("Template.Parse.Value.Error!" + collectCellInfo(cell));
            return (byte) Double.parseDouble(cell.toString());
        }
    }

    /**
     * 按照浮点型double读取cell中的值
     *
     * @return 0.0, 当cell为空时;否则返回其内容所表示的值
     */
    public static double getDoubleValue(Cell cell) {
        if (cell == null || cell.toString().length() == 0) {
            return 0.0;
        }
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            log.error("Template.Parse.Value.Error!" + collectCellInfo(cell));
            return Double.parseDouble(cell.toString());
        }
    }

    public static float getFloatValue(Cell cell) {
        if (cell == null || cell.toString().length() == 0) {
            return 0;
        }
        try {
            return (float) cell.getNumericCellValue();
        } catch (Exception e) {
            log.error("Template.Parse.Value.Error!" + collectCellInfo(cell));
            return Float.parseFloat(cell.toString());
        }
    }

    @SuppressWarnings("unchecked")
    public static Enum<?> getEnumValue(Class<?> fieldType, int offset, Row row, Cell cell) {
        String v = PoiUtils.getStringValue(cell);
        if (Strings.isNullOrEmpty(v)) {
            if (cell == null) {
                throw new RuntimeException("Template.Parse.Value.Error! Enum can't be empty! Row: " + (row.getRowNum() + 1) + " Column number: " + convertColNum2Alphabet(offset + 1));
            } else {
                throw new RuntimeException("Template.Parse.Value.Error! Enum can't be empty!" + collectCellInfo(cell));
            }
        }
        return Enum.valueOf(fieldType.asSubclass(Enum.class), getStringValue(cell));
    }

    public static String getIntAsString(Cell cell) {
        return String.valueOf(getIntValue(cell));
    }

    /**
     * 按照日期型读取cell中的值
     *
     * @return null, 当cell为空时;否则返回其内容所表示的值
     */
    public static Date getDateValue(Cell cell, String pattern) {
        if (cell != null && cell.toString().length() > 0) {
            return cell.getDateCellValue();
        }
        return null;

    }

    public static Calendar getCalendarValue(Cell cell) {

        if (cell == null || cell.toString().length() == 0) return null;

        double numDate = getDoubleValue(cell);
        Date date = DateUtil.getJavaDate(numDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * 将单元格内的内容转换为字符串。<p>
     *
     * 如果单元格内为xxxx.0的数字，则该方法会去除.0，只返回xxxx。
     */
    public static String getStringValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING: {
                return cell.toString();
            }
            case Cell.CELL_TYPE_NUMERIC: {
                String str = FMT_NUMBER.format(cell.getNumericCellValue());
                if (str.endsWith(".0")) {
                    return str.substring(0, str.length() - 2);
                } else {
                    return str;
                }
            }
            default: {
                return cell.toString();
            }
        }
    }

    public static String convertColNum2Alphabet(int colNum) {
        String str = "";
        int first = colNum / 26;
        int sec = colNum % 26;

        if (sec == 0) {
            first = first - 1;
            str = first == 0 ? "" : "" + (char) ('A' + first - 1);
            str = str + "Z";
            return str;
        }

        str = first == 0 ? "" : "" + (char) ('A' + first - 1);
        str = str + (char) ('A' + sec - 1);

        return str;
    }

}
