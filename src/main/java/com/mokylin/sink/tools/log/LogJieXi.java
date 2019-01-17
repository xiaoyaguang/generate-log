package com.mokylin.sink.tools.log;

import com.google.common.base.Charsets;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import com.mokylin.sink.tools.config.IOUtils;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by yaguang.xiao on 2017/7/4.
 */
public class LogJieXi {
    public static void main(String[] args) throws IOException {
        jieXi5_1();
    }

    private static String getDate(long time) {
        DateTime dateTime = new DateTime(time);
        return dateTime.toString().substring(0, 10);
    }

    private static void jieXi5_1() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("log21/5_1.txt");
        String content = IOUtils.readAndClose(fileInputStream, Charsets.UTF_8.name());

        String[] rows = content.split("\n");

        String firstRow = rows[0];
        String[] params = firstRow.split(",");
        int vuinIndex = 0;
        int eventTimeIndex = 0;
        for (int i = 0; i < params.length; i++) {
            if (params[i].equals("vuin")) {
                vuinIndex = i;
            }

            if (params[i].equals("ieventtime")) {
                eventTimeIndex = i;
            }
        }

        System.out.println("eventTimeIndex:" + eventTimeIndex);
        System.out.println("vuinIndex:" + vuinIndex);

        Map<String, Set<String>> map = Maps.newHashMap();

        for (int i = 1; i < rows.length; i++) {
            String[] paramContents = rows[i].split(",");
            long eventTime = Long.parseLong(paramContents[eventTimeIndex] + "000");
            String date = getDate(eventTime);
            Set<String> set = map.computeIfAbsent(date, k -> Sets.newHashSet());

            String uin = paramContents[vuinIndex];
            set.add(uin);
        }

        for (Entry<String, Set<String>> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().size());
        }
    }

    private static void jieXi() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("御龙在天21.txt");
        String content = IOUtils.readAndClose(fileInputStream, Charsets.UTF_8.name());

        String[] rows = content.split("\n");

        String firstRow = rows[0];
        String[] params = firstRow.split(",");
        int ioptypeIndex = 0;
        int iactionIdIndex = 0;
        for (int i = 0; i < params.length; i++) {
            if (params[i].equals("ioptype")) {
                ioptypeIndex = i;
            }

            if (params[i].equals("iactionid")) {
                iactionIdIndex = i;
            }
        }

        Table<Integer, Integer, StringBuilder> table = HashBasedTable.create();
        for (int i = 1; i < rows.length; i++) {
            String[] paramContents = rows[i].split(",");
            int opType = Integer.parseInt(paramContents[ioptypeIndex]);
            int actionId = Integer.parseInt(paramContents[iactionIdIndex]);

            StringBuilder builder = table.get(opType, actionId);
            if (builder == null) {
                builder = new StringBuilder(firstRow).append("\n");
                table.put(opType, actionId, builder);
            }

            builder.append(rows[i]).append("\n");
        }

        for (Cell<Integer, Integer, StringBuilder> cell : table.cellSet()) {
            write(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }

    private static void write(int opType, int actionId, StringBuilder content) {
        File srcDist = new File("log21/");
        if (!srcDist.exists()) {
            if (!srcDist.mkdirs()) {
                throw new RuntimeException("Can't create dir " + srcDist);
            }
        }

        Writer _fileWriter;
        try {
            _fileWriter = new OutputStreamWriter(
                    new FileOutputStream(new File(srcDist, opType + "_" + actionId + ".txt")),
                    "UTF-8");

            _fileWriter.write(content.toString());
            _fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(opType + "_" + actionId);
        }
    }

}
