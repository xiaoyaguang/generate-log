package com.mokylin.sink.tools.log;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.mokylin.sink.tools.util.ExcelOperation;
import com.mokylin.sink.tools.util.ExcelOperation.LoadRowOperation;
import com.mokylin.sink.tools.util.PoiUtils;

import org.apache.poi.ss.usermodel.Row;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by yaguang.xiao on 2017/7/17.
 */
public class TestJiMaoXin {

    public static void main(String[] args) {

        //        Set<Long> playerIdSet = Sets.newHashSet();
        //
        //        ExcelOperation.loadExcel("jimaoxin.xlsx", "1", new LoadRowOperation() {
        //            @Override
        //            public void load(Row row) {
        //                String time = PoiUtils.getStringValue(row.getCell(0));
        //                String idStr = PoiUtils.getStringValue(row.getCell(4));
        //
        //                if (time.trim().equals("42928")) {
        //                    long id = Long.parseLong(idStr);
        //                    playerIdSet.add(id);
        //                }
        //            }
        //        });
        //
        //        System.out.println(playerIdSet.size());

        String param =
                "ID=3A1C24A26D8E4D0593165CC84F59514C|pfkey=89FC18B9ED0C540AF19D0A7FC8FC6AB9|ADId=3200001-1|X5Sig=0|ModelID=QQMGBModelID1106030041|PID=8|X5Pid=10002-8|Key=12BDED02387C94FE60DEE9E4C21C8B2E|";

        Map<String, String> map = decode(param);

        for (Entry<String, String> entry : map.entrySet()) {
            System.out.println("key=" + entry.getKey());
            System.out.println("value=" + entry.getValue());
        }
    }

    private static Map<String, String> decode(String param) {
        String[] pairArr = param.split("\\|");

        Map<String, String> params =
                Maps.newHashMapWithExpectedSize(((int) (pairArr.length / 0.75f)) + 1);

        for (String pair : pairArr) {
            int index = pair.indexOf("=");
            String key = pair.substring(0, index);
            String value = pair.substring(index + 1);

            params.put(key, value);
        }

        return params;
    }

}
