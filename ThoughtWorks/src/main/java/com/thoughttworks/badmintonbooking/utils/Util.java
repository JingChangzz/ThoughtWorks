package com.thoughttworks.badmintonbooking.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ZhangJing on 2017/9/9.
 */
public class Util {


    public static boolean isWeekEnd(String date){
        boolean result = false;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdw = new SimpleDateFormat("E");
        Date d = null;
        try {
            d = sd.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String day = sdw.format(d);
        if (day.equals("星期六") || day.equals("星期日")){
            result = true;
        }
        return result;
    }

}
