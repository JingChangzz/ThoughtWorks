package com.thoughttworks.badmintonbooking.utils;

import com.thoughttworks.badmintonbooking.main.BookingHourSetting;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ZhangJing on 2017/9/9.
 */
public class InputValueCheck {
    private final static char[] stadia = {'A', 'B', 'C', 'D'};
    public static boolean isWeekend;

    private static boolean isValidUserID(String userid){
        boolean result = false;
        if (userid == null || userid.length() == 0) {
            return result;
        }
        result = true;
        return result;
    }

    private static boolean isValidDate(String date){
        boolean result = false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            Date in = format.parse(date);
            Date now = new Date();
            if (now.after(in)){
                System.out.println(date+"不是有效日期！");
                return false;       //订昨天之前的
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        isWeekend = Util.isWeekEnd(date);
        result = true;
        return result;
    }

    private static boolean isValidHour(String date){
        boolean result = false;
        String[] time = date.split("~");
        String t = time[0].substring(time[0].length()-2, time[0].length());
        if (!time[0].substring(time[0].length()-2, time[0].length()).equals("00") ||
                !time[1].substring(time[1].length()-2, time[1].length()).equals("00")){
            System.out.println("输入的时间'"+date+"'不是整点！");
            return result;
        }
        try {
            DateFormat df = new SimpleDateFormat("HH:mm");
            if (df.parse(time[0]).getTime() > df.parse(time[1]).getTime()){
                System.out.println("输入时间段有误：'"+date+"'起始时间大于终止时间！");
                return result;
            }

            if (isWeekend){
                BookingHourSetting.getWeekendHourSetting();

                if (df.parse(BookingHourSetting.weekStartTime.get(0)).getTime() <= df.parse(time[0]).getTime() &&
                        df.parse(BookingHourSetting.weekEndTime.get(BookingHourSetting.weekEndTime.size()-1)).getTime() >= df.parse(time[1]).getTime()){
                }else {
                    System.out.println("输入时间段有误，'"+date+"'不在可预定时间段内！");
                    return result;
                }
            }else{
                BookingHourSetting.getCommHourSetting();

                if (df.parse(BookingHourSetting.commStartTime.get(0)).getTime() <= df.parse(time[0]).getTime() &&
                        df.parse(BookingHourSetting.commEndTime.get(BookingHourSetting.commEndTime.size()-1)).getTime() >= df.parse(time[1]).getTime()){
                }else{
                    System.out.println("输入时间段有误，请查看可预定时间段！");
                    return result;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result = true;
        return result;
    }

    private static boolean isValidStadia(String in){
        boolean result = false;
        int i = 0;
        for (; i < stadia.length; i++) {
            if (stadia[i] == in.charAt(0)){
                result = true;
                break;
            }
        }
        if (i == stadia.length)
            System.out.println("要预定的场馆输入有误！");

        return result;
    }

    private static boolean isValidFlag(String in){
        boolean result = false;
        if (in != null && in.length() == 1 && in.charAt(0) == 'C'){
            result = true;
        }
        return result;
    }

    public static boolean isBookingInputValid(String[] infos){
        boolean result = false;
        if (infos.length != 4) {
            return result;
        }
        if (!isValidUserID(infos[0])){
            return result;
        }
        if (!isValidDate(infos[1])){
            return result;
        }
        if (!isValidStadia(infos[3])){
            return result;
        }if (!isValidHour(infos[2])){
            return result;
        }
        result = true;
        return result;
    }

    public static boolean isCanclingInputValid(String[] infos){
        boolean result = false;
        if (infos.length != 5) {
            return result;
        }
        if (!isValidUserID(infos[0])){
            return result;
        }
        if (!isValidStadia(infos[3])){
            return result;
        }if (!isValidHour(infos[2])){
            return result;
        }
        if (!isValidFlag(infos[4])){
            return result;
        }
        result = true;
        return result;
    }

}
