package com.mokylin.sink.tools.log;

import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.mokylin.sink.tools.config.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yaguang.xiao on 2017/7/19.
 */
public class ExceptionJieXi {

    public static void main(String[] args) throws IOException {

        exceptionJieXi("log/logFile.log", "log/ylzt1.log", "log/logFile.2017-12-05.log");

    }

    private static void exceptionJieXi(String... logFileNames) throws IOException {
        StringBuilder contentB = new StringBuilder();

        for (String logFileName : logFileNames) {
            FileInputStream fileInputStream = new FileInputStream(logFileName);
            contentB.append(IOUtils.readAndClose(fileInputStream, Charsets.UTF_8.name()))
                    .append("\n");
        }

        String contentRaw = contentB.toString();

        String content = contentRaw.replace("\n", "");

        Set<String> exceptionSet = Sets.newHashSet();
        String[] dotSplitArr = content.split("\\.");
        for (String dotSplit : dotSplitArr) {

            if (dotSplit.contains("Exception")) {
                String[] maoHaoSplitArr = dotSplit.split(":");
                for (String maoHaoSplit : maoHaoSplitArr) {

                    if (maoHaoSplit.contains("Exception")) {
                        exceptionSet.add(maoHaoSplit);
                    }
                }
            }
        }

        System.out.println("ExceptionCount:" + exceptionSet.size());

        StringBuilder sb = new StringBuilder();
        for (String s : exceptionSet) {
            sb.append(s).append("\n");
        }

        write(sb, "exception2.txt");

        final int relateCount = 40;

        Map<String, ListMultimap<String, String>> map = Maps.newHashMap();

        String[] rows = contentRaw.split("\n");
        for (int i = 0; i < rows.length; i++) {
            for (String exceptionName : exceptionSet) {
                if (rows[i].contains(exceptionName)) {

                    ListMultimap<String, String> listMultimap = map.get(exceptionName);
                    if (listMultimap == null) {
                        listMultimap = ArrayListMultimap.create();
                        map.put(exceptionName, listMultimap);
                    }

                    StringBuilder keyB = new StringBuilder();
                    for (int j = 0; j < 3; j++) {
                        if (i + j < rows.length) {
                            keyB.append(rows[i + j]);
                        }
                    }
                    String secondKey = keyB.toString();

                    for (int j = 0; j < relateCount; j++) {
                        if (i + j < rows.length) {
                            listMultimap.put(secondKey, rows[i + j]);
                        }
                    }
                }
            }
        }

        write(map);
    }

    private static void write(Map<String, ListMultimap<String, String>> map) {
        for (String key : map.keySet()) {
            ListMultimap<String, String> listMultimap = map.get(key);

            int index = 1;

            for (String secondKey : listMultimap.keySet()) {

                if (secondKey.contains("invalid version format")) {
                    continue;
                }

                List<String> contentList = listMultimap.get(secondKey);

                StringBuilder sb = new StringBuilder();

                for (String content : contentList) {
                    sb.append(content).append("\n");
                }

                write(sb, key + "_" + index + ".txt");
                index++;
            }
        }
    }

    //    private static void firstJieXi() throws IOException {
    //        FileInputStream fileInputStream = new FileInputStream("exception.txt");
    //        String content = IOUtils.readAndClose(fileInputStream, Charsets.UTF_8.name());
    //
    //        String[] rows = content.split("\n");
    //
    //        System.out.println("文件行数：" + rows.length);
    //
    //        Set<String> exceptionSet = Sets.newHashSet();
    //        for (String row : rows) {
    //            String[] dotSplitArr = row.split("\\.");
    //            for (String dotSplit : dotSplitArr) {
    //
    //                if (dotSplit.contains("Exception")) {
    //                    String[] maoHaoSplitArr = dotSplit.split(":");
    //                    for (String maoHaoSplit : maoHaoSplitArr) {
    //
    //                        if (maoHaoSplit.contains("Exception")) {
    //                            exceptionSet.add(maoHaoSplit);
    //                        }
    //                    }
    //                }
    //            }
    //        }
    //
    //        System.out.println("ExceptionCount:" + exceptionSet.size());
    //
    //        StringBuilder sb = new StringBuilder();
    //        for (String s : exceptionSet) {
    //            sb.append(s).append("\n");
    //        }
    //
    //        write(sb, "exception1.txt");
    //    }

    private static void write(StringBuilder content, String file) {
        File srcDist = new File("exception/");
        if (!srcDist.exists()) {
            if (!srcDist.mkdirs()) {
                throw new RuntimeException("Can't create dir " + srcDist);
            }
        }

        Writer _fileWriter;
        try {
            _fileWriter =
                    new OutputStreamWriter(new FileOutputStream(new File(srcDist, file)), "UTF-8");

            _fileWriter.write(content.toString());
            _fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
