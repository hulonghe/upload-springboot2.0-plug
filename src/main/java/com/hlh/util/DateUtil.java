package com.hlh.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具
 */

public class DateUtil {

    public static String getNow() {

        Date date = new Date();
        String str = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        return sdf.format(date);
    }


    public static String getNow(Date date) {
        String str = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        return sdf.format(date);
    }

    /**
     * 将指定的时间按照特定的格式转化
     *
     * @param date
     * @param fat
     * @return
     */
    public static String getDate(Date date, String fat) {
        SimpleDateFormat sdf = new SimpleDateFormat(fat);
        return sdf.format(date);
    }


    /**
     * 字符串时间转Date
     *
     * @param date0
     * @return
     */
    public static Date str2Date(String date0, String fat) {
        //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
        SimpleDateFormat sDateFormat = new SimpleDateFormat(fat); //加上时间
        //必须捕获异常
        try {
            Date date = sDateFormat.parse(date0);
            return date;
        } catch (ParseException px) {
//            px.printStackTrace();
            return null;
        }
    }

    /**
     * 获取今天
     *
     * @return
     */
    public static String getToday() {
        Date date = new Date();
        String str = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        return sdf.format(date);
    }

    /**
     * 根据指定的时间戳获取当前凌晨的时间戳
     *
     * @param second
     * @return
     */
    public static long getITSH(long second) {
        long now = second / 1000l;
        long daySecond = 60 * 60 * 24;
        long dayTime = now - (now + 8 * 3600) % daySecond;
        return dayTime;
    }

    /**
     * 计算年龄
     *
     * @param birth 出生日志
     * @return
     */
    public static int calcAge(Date birth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int nowYear = calendar.get(Calendar.YEAR);
        calendar.setTime(birth);
        return nowYear - calendar.get(Calendar.YEAR);
    }
}
