package com.thoughttworks.badmintonbooking.main;

import com.thoughttworks.badmintonbooking.utils.InputValueCheck;
import com.thoughttworks.badmintonbooking.utils.Util;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ZhangJing on 2017/9/9.
 */
public class Booking {

    private BookingRecord bookingRecord;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private final String ROOT = "/Users/apple/Desktop/ThoughtWorks/src/main/resources/";
    public Booking(String[] infos){
        this.bookingRecord = new BookingRecord();

        bookingRecord.setUserID(infos[0]);
        bookingRecord.setBookingDate(infos[1]);

        String[] ss = infos[2].split("~");
        bookingRecord.setStartTime(ss[0]);
        bookingRecord.setEndTime(ss[1]);

        bookingRecord.setLocation(infos[3].charAt(0));
        if (infos.length == 4) {
            bookingRecord.setFlag('0');
        }else{
            bookingRecord.setFlag(infos[4].charAt(0));
        }
    }

    /**
     * 在之前已经判断过输入是否合法，合法才会到这里
     * 预定场馆
     * 1、计算预定的费用
     * 2、写入到对应的文件中
     *    场馆记录、用户记录
     */
    public void book(){
        //判断有优惠


        int money = (int)calPrice(bookingRecord.getStartTime(), bookingRecord.getEndTime());

        bookingRecord.setMoney(money);
        if (hasConflict(bookingRecord)){
            System.out.println(" Error: the booking conflicts with existing bookings!");
            return;
        }
        String filename = ROOT+bookingRecord.getLocation()+".txt";
        String filename2 = ROOT+"User/"+bookingRecord.getUserID()+".txt";
        try {
            //写进对应的场馆预定文件
            fileWriter = new FileWriter(new File(filename), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(bookingRecord.getUserID()+" ");
            bufferedWriter.write(bookingRecord.getBookingDate()+" ");
            bufferedWriter.write(bookingRecord.getStartTime()+" ");
            bufferedWriter.write(bookingRecord.getEndTime()+" ");
            bufferedWriter.write(bookingRecord.getLocation()+" ");
            bufferedWriter.write(bookingRecord.getFlag()+" ");
            bufferedWriter.write(bookingRecord.getMoney()+"\n");
            bufferedWriter.close();

            File file = new File(filename2);
            if (!file.exists()){
                file.createNewFile();
            }
            //写进对应的用户记录文件
            fileWriter = new FileWriter(new File(filename2), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(bookingRecord.getUserID()+" ");
            bufferedWriter.write(bookingRecord.getBookingDate()+" ");
            bufferedWriter.write(bookingRecord.getStartTime()+" ");
            bufferedWriter.write(bookingRecord.getEndTime()+" ");
            bufferedWriter.write(bookingRecord.getLocation()+" ");
            bufferedWriter.write(bookingRecord.getFlag()+" ");
            bufferedWriter.write(bookingRecord.getMoney()+"\n");
            fileWriter.flush();
            bufferedWriter.flush();
            fileWriter.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Success: the booking is accepted! \n");
    }

    /**
     * 在之前已经判断过输入是否合法，合法才会到这里
     * 取消预定
     * 1.取消项是否存在
     * 2.修改预定的flag为C
     *   用户记录、场馆记录 2处
     */
    public void cancel(){
        new Caching().getAllUsersRecords();

        List<BookingRecord> cur = Caching.usersHashMap.get(bookingRecord.getUserID());
        boolean ex = false;
        for (BookingRecord b : cur){
            if (b.getUserID().equals(bookingRecord.getUserID())
                    && b.getBookingDate().equals(bookingRecord.getBookingDate())
                    && b.getStartTime().equals(bookingRecord.getStartTime())
                    && b.getEndTime().equals(bookingRecord.getEndTime())
                    && b.getLocation() == bookingRecord.getLocation()
                    && b.getFlag() == '0'){     //取消预订，
                if (Util.isWeekEnd(b.getBookingDate())){
                    bookingRecord.setMoney(b.getMoney() * 0.25);
                } else{
                    bookingRecord.setMoney(b.getMoney() * 0.5);
                }
                bookingRecord.setFlag('C');
                ex = true;
                break;
            }
        }
        if (ex == false){
            System.out.println("Error: the booking being cancelled does not exist!");
        } else {
            // 修改写回文件
            writeBackToFile(bookingRecord, ROOT+bookingRecord.getLocation()+".txt");
            writeBackToFile(bookingRecord, ROOT+"User/"+bookingRecord.getUserID()+".txt");
            System.out.println("Success: 成功取消！");
        }
   }

    /**
     * 计算 预定费用
     * @param startTime
     * @param endTime
     * @return
     */
    private long calPrice(String startTime, String endTime){
        long result = 0;
        DateFormat df = new SimpleDateFormat("HH:mm");//创建日期转换对象HH:mm为时分
        try {
            if (InputValueCheck.isWeekend) {
                Date inS = df.parse(startTime);
                Date inE = df.parse(endTime);
                for (int i = 0; i < BookingHourSetting.weekHour.size(); i++) {
                    Date setS = df.parse(BookingHourSetting.weekStartTime.get(i));
                    Date setE = df.parse(BookingHourSetting.weekEndTime.get(i));

                    if(inS.getTime() >= setS.getTime() && inE.getTime() <= setE.getTime()){  //刚好在
                        result = result + (getHours(inS, setE) - getHours(inE, setE))* Integer.parseInt(BookingHourSetting.weekPrice.get(i));
                        break;
                    } else if (inS.getTime() >= setS.getTime() && inS.getTime() <= setE.getTime()
                            && inE.getTime() > setE.getTime()){
                        result = result + getHours(inS, setE) * Integer.parseInt(BookingHourSetting.weekPrice.get(i));
                    } else if (inS.getTime() <= setS.getTime() && setE.getTime() <= inE.getTime()){
                        result += BookingHourSetting.weekHour.get(i)*Integer.parseInt(BookingHourSetting.weekPrice.get(i));
                    } else if (inS.getTime() < setS.getTime() && inE.getTime() >= setS.getTime()
                            && inE.getTime() <= setE.getTime()){
                        result += getHours(setS, inE)*Integer.parseInt(BookingHourSetting.weekPrice.get(i));
                    }
                }
            }else{
                Date inS = df.parse(startTime);
                Date inE = df.parse(endTime);
                for (int i = 0; i < BookingHourSetting.commHour.size(); i++) {
                    Date setS = df.parse(BookingHourSetting.commStartTime.get(i));
                    Date setE = df.parse(BookingHourSetting.commEndTime.get(i));

                    if(inS.getTime() >= setS.getTime() && inE.getTime() <= setE.getTime()){  //刚好在
                        result = result + (getHours(inS, setE) - getHours(inE, setE))* Integer.parseInt(BookingHourSetting.commPrice.get(i));
                        break;
                    } else if (inS.getTime() >= setS.getTime() && inS.getTime() <= setE.getTime()
                            && inE.getTime() > setE.getTime()){
                        result = result + getHours(inS, setE) * Integer.parseInt(BookingHourSetting.commPrice.get(i));
                    } else if (inS.getTime() <= setS.getTime() && setE.getTime() <= inE.getTime()){
                        result += BookingHourSetting.commHour.get(i)*Integer.parseInt(BookingHourSetting.commPrice.get(i));
                    } else if (inS.getTime() < setS.getTime() && inE.getTime() >= setS.getTime()
                            && inE.getTime() <= setE.getTime()){
                        result += getHours(setS, inE)*Integer.parseInt(BookingHourSetting.commPrice.get(i));
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    private long getHours(Date d1, Date d2){
        return (d2.getTime() - d1.getTime())/1000/60/60;
    }

    private void writeBackToFile(BookingRecord cur, String path){
        try {
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            ArrayList<BookingRecord> al = new ArrayList<BookingRecord>();

            while((str = bufferedReader.readLine()) != null) {
                BookingRecord br = new BookingRecord();
                String[] s = str.split(" ");
                if (cur.getBookingDate().equals(s[1])
                        && cur.getStartTime().equals(s[2])
                        && cur.getEndTime().equals(s[3])
                        && cur.getLocation() == s[4].charAt(0)
                        && s[5].charAt(0) == '0'){
                    br.setMoney(cur.getMoney());
                    br.setFlag('C');
                } else {
                    br.setFlag(s[5].charAt(0));
                    br.setMoney(Double.valueOf(s[6]));
                }
                br.setUserID(s[0]);
                br.setBookingDate(s[1]);
                br.setStartTime(s[2]);
                br.setEndTime(s[3]);
                br.setLocation(s[4].charAt(0));
                al.add(br);
                //System.out.println(str);
            }

            fileWriter = new FileWriter(file, false);
            bufferedWriter = new BufferedWriter(fileWriter);
            for (int i = 0; i < al.size(); i++) {
                bufferedWriter.write(al.get(i).getUserID()+" ");
                bufferedWriter.write(al.get(i).getBookingDate()+" ");
                bufferedWriter.write(al.get(i).getStartTime()+" ");
                bufferedWriter.write(al.get(i).getEndTime()+" ");
                bufferedWriter.write(al.get(i).getLocation()+" ");
                bufferedWriter.write(al.get(i).getFlag()+" ");
                bufferedWriter.write(al.get(i).getMoney()+"\n");
            }

            inputStream.close();
            bufferedReader.close();
            fileWriter.flush();
            bufferedWriter.flush();
            fileWriter.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断与已经存在的预定信息是否有冲突
     * @return
     */
    public boolean hasConflict(BookingRecord booking){
        boolean result = false;
        try {
            new Caching().getAllRecords();
            List<BookingRecord> list = Caching.allRecordshashMap.get(booking.getLocation());
            DateFormat df = new SimpleDateFormat("HH:mm");//创建日期转换对象HH:mm为时分
            Date inS = df.parse(booking.getStartTime());
            Date inE = df.parse(booking.getEndTime());

            for (BookingRecord b : list) {
                if (b.getFlag() == 'C') continue;   //取消了，可以再被预定

                Date reS = df.parse(b.getStartTime());
                Date reE = df.parse(b.getEndTime());
                if (b.getBookingDate().equals(booking.getBookingDate())){
                    if (inS.getTime() <= reS.getTime()
                            && inE.getTime() > reS.getTime()){
                        result = true;
                    }else if (inS.getTime() >= reS.getTime()
                            && inE.getTime() <= reE.getTime()){
                        result = true;
                    }else if (inS.getTime() >= reS.getTime()
                            && inE.getTime() >= reE.getTime()){
                        result = true;
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }



}
