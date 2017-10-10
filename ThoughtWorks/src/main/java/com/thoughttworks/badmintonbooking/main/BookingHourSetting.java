package com.thoughttworks.badmintonbooking.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ZhangJing on 2017/9/9.
 */
public class BookingHourSetting {
    private static InputStream inputStream;
    private static BufferedReader bufferedReader;

    public static ArrayList<String> commStartTime = new ArrayList<String>();
    public static ArrayList<String> commEndTime = new ArrayList<String>();
    public static ArrayList<String> commPrice = new ArrayList<String>();
    public static ArrayList<String> weekStartTime = new ArrayList<String>();
    public static ArrayList<String> weekEndTime = new ArrayList<String>();
    public static ArrayList<String> weekPrice = new ArrayList<String>();

    public static ArrayList<Long> commHour = new ArrayList<Long>();
    public static ArrayList<Long> weekHour = new ArrayList<Long>();

    private static String[] hourSetting = {"commontime.txt", "weekendstime.txt"};

    public static void getCommHourSetting(){
            inputStream = BookingSum.class.getClassLoader().getResourceAsStream(hourSetting[0]);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            readFile(inputStream, bufferedReader, commStartTime, commEndTime, commPrice,commHour);
        }

    public static void getWeekendHourSetting(){
        inputStream = BookingSum.class.getClassLoader().getResourceAsStream(hourSetting[1]);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        readFile(inputStream, bufferedReader, weekStartTime,weekEndTime,weekPrice,weekHour);
    }

    public static void readFile(InputStream inputStream, BufferedReader bufferedReader,
                                ArrayList<String> startTime, ArrayList<String> endTime,
                                ArrayList<String> price, ArrayList<Long> hour){
        String str = null;
        try {
            while((str = bufferedReader.readLine()) != null) {
                String[] s = str.split(" ");
                startTime.add(s[0]);
                endTime.add(s[1]);
                price.add(s[2]);
                hour.add(getHour(s[0],s[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static long getHour(String h1, String h2){
        long result = 0;
        DateFormat df = new SimpleDateFormat("HH:mm");
        try {
          result = (df.parse(h2).getTime() - df.parse(h1).getTime())/1000/60/60;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

}
