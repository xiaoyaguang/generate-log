package com.mokylin.sink.tools.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yaguang.xiao on 2017/9/29.
 */
public class LoadCsv {

    public static void main(String[] args) {
        File csv = new File("log/wu_pin.csv");  // CSV文件路径
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        boolean isFirstLine = true;

        String line = "";
        try {
            while ((line = br.readLine()) != null)  //读取到的内容给line变量
            {

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                handleLine(line);
            }

            for (Info info : map.values()) {
                if (info.count < 400) {
                    continue;
                }

                System.out.println(
                        "openId:" + info.openId + ",name:" + info.name + ",count:" + info.count);
            }

            System.out.println("数量：" + count.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final AtomicInteger count = new AtomicInteger();

    private static final Map<String, Info> map = new HashMap<>();

    private static void handleLine(String line) {
        count.incrementAndGet();

        String[] split = line.split(",");
        String openId = split[3];
        String name = split[6];
        int operateId = Integer.parseInt(split[7]);

        if (operateId != 101) {
            return;
        }

        Info info = map.get(openId);
        if (info == null) {
            info = new Info(openId, name);
            map.put(openId, info);
        }

        info.count++;
    }

    private static class Info {
        public final String openId;
        public final String name;
        public int count;

        private Info(String openId, String name) {
            this.openId = openId;
            this.name = name;
        }
    }
}
