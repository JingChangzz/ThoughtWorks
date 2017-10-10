package com.thoughttworks.badmintonbooking.main;

import com.thoughttworks.badmintonbooking.utils.InputValueCheck;
import java.util.Scanner;

/**
 * Created by ZhangJing on 2017/9/9.
 */
public class BadmintonBookingSystem {

    public static void main(String[] args) {

        String type = "-1";
        System.out.println("    欢迎使用羽毛球预定管理系统~");

        while(!type.equals("0")) {
            System.out.println("------------------------------------");
            System.out.println("#使用指南：");
            System.out.println("#查询收入汇总，请输入1");
            System.out.println("#预定场馆，请输入2");
            System.out.println("#取消场馆预定，请输入3");
            System.out.println("#结束使用，请输入0");
            System.out.print("请输入：");

            Scanner scanner = new Scanner(System.in);
            type = scanner.next();

            switch (type) {
                case "1":
                    System.out.println("------------------------------------");
                    System.out.println("收入汇总：");
                    new BookingSum().calAllBooking();

                    break;
                case "2":
                    System.out.println("------------------------------------");
                    System.out.println("    欢迎进行场馆预定~");
                    System.out.println("可预订场馆有：A B C D");
                    System.out.println("周⼀到周五：\n" +
                            "9:00 ~ 12:00 30元/时;" +
                            "12:00 ~ 18:00 50元/时;" +
                            "18:00 ~ 20:00 80元/时;" +
                            "20:00 ~ 22:00 60元/时");
                    System.out.println("周六及周⽇\n" +
                            "9:00 ~ 12:00 40元/时;" +
                            "12:00 ~ 18:00 50元/时;" +
                            "18:00 ~ 22:00 60元/时");
                    System.out.println("输入提示：");
                    System.out.println("格式为：{⽤户ID} {预订⽇期 yyyy-MM-dd} {预订时间段 HH:mm~HH:mm} {场地}");
                    System.out.println("示例：U000 2016-06-02 20:00~22:00 A");
                    boolean flag = true;
                    while (flag) {
                        System.out.println("请输入预定场馆信息：");
                        Scanner scanner2 = new Scanner(System.in);
                        String input = scanner2.nextLine();
                        String[] info = input.split(" ");
                        if (!InputValueCheck.isBookingInputValid(info)) {
                            System.out.println("Error：the booking is invalid! \n");
                            continue;
                        }
                        new Booking(info).book();
                        flag = false;
                    }
                    break;
                case "3":
                    System.out.println("------------------------------------");
                    System.out.println("    进行场馆预定取消~");
                    System.out.println("输入提示：");
                    System.out.println("格式为：{⽤户ID} {预订⽇期 yyyy-MM-dd} {预订时间段 HH:mm~HH:mm} {场地} {取消标记}");
                    System.out.println("示例：U000 2016-06-02 20:00~22:00 A C");
                    boolean cancle = true;
                    while (cancle) {
                        System.out.println("请输入场馆信息：");
                        Scanner scanner2 = new Scanner(System.in);
                        String input = scanner2.nextLine();
                        String[] info = input.split(" ");
                        if (!InputValueCheck.isCanclingInputValid(info)){
                            System.out.println("Error:输入有误! \n");
                        }
                        new Booking(info).cancel();
                        cancle = false;
                    }
                    break;
                case "0":
                    System.out.println("结束使用~ \n");
                    return;
                default:
                    System.out.println("输入有误！\n");
            }
        }

    }
}
