package com.qfedu.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *@Author feri
 *@Date Created in 2019/6/12 16:32
 */
public class TimeUtil {
    public static String getTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    public static String getDate(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
    //计算时间  获取多少天之后
    public static Date getDays(int days){
        //日历类
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,days);
        return calendar.getTime();
    }
    // 计算获得多少月之后
    public static Date getMonths(int months){
        //日历类
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH,months);
        return calendar.getTime();
    }

    // 计算获得多少分钟之后
    public static Date getMinutes(int minutes){
        //日历类
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MINUTE,minutes);
        return calendar.getTime();
    }

    // 计算今天剩余秒
    public static int getLastSeconds(){
        Date currdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date lastdate = sdf.parse(getDate() + " 23:59:59");
            return (int) ((lastdate.getTime() - currdate.getTime())/1000); // 得到了毫秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 获取当前时间距离指定日期的天数
    public static int getDistanceDays(Date date){
        Calendar calendar = Calendar.getInstance();
        return (int)(calendar.getTime().getTime()/1000/24/3600 - date.getTime()/1000/24/3600);
    }

    public static String getFormat(Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
//    public static void main(String[] args) {
//        System.out.println(getLastSeconds());
//    }
}
