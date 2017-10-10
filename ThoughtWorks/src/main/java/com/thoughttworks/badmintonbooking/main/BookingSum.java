package com.thoughttworks.badmintonbooking.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangJing on 2017/9/9.
 */
public class BookingSum {
    private Map<Character, ArrayList<BookingRecord>> hashMap = new HashMap<Character, ArrayList<BookingRecord>>();

    public void calAllBooking(){
        hashMap = new Caching().getAllRecords();

        double sum = 0;
        for(char key:hashMap.keySet()){
            System.out.println("场地" + key + ":");
            List<BookingRecord> list = hashMap.get(key);
            double psum = 0;
            for (int i = 0; i < list.size(); i++) {
                String date = list.get(i).getBookingDate();
                String st = list.get(i).getStartTime();
                String et = list.get(i).getEndTime();
                char flag = list.get(i).getFlag();
                String f = "";
                if (flag == 'C'){
                    f = "违约金 ";
                }
                double m = list.get(i).getMoney();
                System.out.println(date + " " + st + "~" + et + " " + f + "" + m +"元");
                psum = psum + m;
            }
            System.out.println("小计：" + psum + "元 \n");
            sum = sum + psum;
        }
        System.out.println("----------");
        System.out.println("总计：" + sum + "元 \n");
    }
}
