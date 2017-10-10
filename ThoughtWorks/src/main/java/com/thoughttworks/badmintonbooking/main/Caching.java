package com.thoughttworks.badmintonbooking.main;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZhangJing on 2017/9/9.
 */
public class Caching {
    private final static char[] stadia = {'A', 'B', 'C', 'D'};
    private static final String DISCOUNT = "discount.txt";
    public static Map<Character, ArrayList<BookingRecord>> allRecordshashMap = new HashMap<Character, ArrayList<BookingRecord>>();
    public static Map<String, ArrayList<BookingRecord>> usersHashMap = new HashMap<String, ArrayList<BookingRecord>>();
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private final String ROOT = "/Users/apple/Desktop/ThoughtWorks/src/main/resources/";


    /**
     * 四个场地的记录
     * @return
     */
    public Map<Character, ArrayList<BookingRecord>> getAllRecords(){
        for (int i = 0; i < stadia.length; i++) {
            String filename = ROOT+stadia[i]+".txt";
            ArrayList<BookingRecord> al = new ArrayList<BookingRecord>();
            try {
                File file = new File(filename);
                if (file.exists() && file.isFile()){
                    inputStream = new FileInputStream(filename);
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String str = null;
                    while((str = bufferedReader.readLine()) != null) {
                        BookingRecord br = new BookingRecord();
                        String[] s = str.split(" ");
                        setBr(br, s);
                        al.add(br);
                    }
                    allRecordshashMap.put(stadia[i], al);
                } else {
                    file.createNewFile();
                }
                inputStream.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return allRecordshashMap;
    }

    public Map<String, ArrayList<BookingRecord>> getAllUsersRecords(){
        String dir = "/Users/apple/Desktop/ThoughtWorks/src/main/resources/User/";
        File folder = new File(dir);
        File[] files = folder.listFiles();
        try {
            for (File f : files){
                inputStream = new FileInputStream(f);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String str = null;
                ArrayList<BookingRecord> al = new ArrayList<BookingRecord>();

                    while((str = bufferedReader.readLine()) != null) {
                        BookingRecord bookingRecord = new BookingRecord();
                        String[] s = str.split(" ");
                        setBr(bookingRecord, s);
                        al.add(bookingRecord);
                    }
                    usersHashMap.put(f.getName().replace(".txt",""), al);
            }
            inputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usersHashMap;
    }

    private void setBr(BookingRecord bookingRecord, String[] infos){
        bookingRecord.setUserID(infos[0]);
        bookingRecord.setBookingDate(infos[1]);
        bookingRecord.setStartTime(infos[2]);
        bookingRecord.setEndTime(infos[3]);
        bookingRecord.setLocation(infos[4].charAt(0));
        bookingRecord.setFlag(infos[5].charAt(0));
        bookingRecord.setMoney(Double.valueOf(infos[6]));
    }

    public void getAllDiscount(){
        try {
            inputStream = new FileInputStream(ROOT+DISCOUNT);
            bufferedReader =



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void readFile(){

    }

}
