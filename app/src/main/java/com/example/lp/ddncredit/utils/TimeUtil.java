package com.example.lp.ddncredit.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaoyuren on 2018/1/26.
 * 项目名称：didano-robot
 * 类描述：
 * company：www.didano.cn
 * email：vin.qin@didano.cn
 * 创建时间：2018/1/26 10:55
 */

public class TimeUtil {

    /**
     * 延时
     *
     * @param ms 毫秒
     */
    public static void delayMs(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
        }
    }

    public static boolean availableTimesTamp(long ms) {
        // Logger.i("diff : " +  Math.abs(System.currentTimeMillis() - ms));
        return (Math.abs(System.currentTimeMillis() - ms) < 500);
    }

    /**
     * 获取格式化后的时间
     *
     * @return 20180101112233
     */
    public static String getYMDHMSDate() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime());
        return dateName;
    }


    /**
     * 获取格式化后的时间
     *
     * @return 11:22:33
     */
    public static String getYMDHMSDate001() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime());
        return dateName;
    }

    public static String getYMDDate() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime());
        return dateName;
    }
    /**
     * 获取格式化后的时间
     *
     * @return 2019:11:19:11:22:33
     */
    public static String getNowDate() {
        DateFormat df = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime());
        return dateName;
    }
    /**
     * 获取系统时间字符串
     *
     * @param format 要获取的系统时间的格式
     * @return 系统时间字符串
     */
    public static String getDateString(String format) {
        DateFormat df = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime());
        return dateName;
    }


    /**
     * 将时间字符串转换成长整型值，单位ms
     *
     * @param time   时间字符串
     * @param format 时间字符串的格式
     * @return 返回时间长整型值
     */
    public static long StringToLong(String time, String format) {
        if(time == null){
            return 0;
        }
        Date date = StringToDate(time, format); // String类型转成date类型
        if (date == null) {
            return 0;
        }
        return DateToLong(date); // date类型转成long类型
    }


    /**
     * 将时间字符串转换成Date类型
     *
     * @param strTime    时间字符串
     * @param formatType 时间格式
     * @return Date类型的时间
     */
    public static Date StringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将date类型的时间转换成字符串
     *
     * @param data       Date类型的时间
     * @param formatType 要转成的字符串时间格式
     * @return 时间字符串
     */
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String DateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    /**
     * 将长整型时间值转化成字符串类型
     *
     * @param currentTime 要转换的长整型时间值
     * @param formatType  要转成的时间格式
     * @return 时间字符串
     */
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String LongToString(long currentTime, String formatType) {
        Date date = LongToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = DateToString(date, formatType); // date类型转成String
        return strTime;
    }

    /**
     * 将长整型时间值转成Date类型
     *
     * @param currentTime 长整型时间值
     * @param formatType  时间格式
     * @return Date类型的时间值
     */
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date LongToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = DateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = StringToDate(sDateTime, formatType); // 把String类型转换为Date类型

        return date;
    }

    /**
     * 将Date类型的时间值转换成long类型值
     *
     * @param date Date类型的时间值
     * @return long类型值
     */
    public static long DateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 将时间字符串进行格式转换
     *
     * @param time       时间字符串
     * @param srcFormat  字符串原格式
     * @param destFormat 要转成的格式
     * @return 新格式的时间字符串
     */
    public static String CovertFormat(String time, String srcFormat, String destFormat) {
        Date date = StringToDate(time, srcFormat);
        return DateToString(date, destFormat);
    }

    /**
     * 判断时间格式是否正确
     *
     * @param timeStr 2016-5-2 08:02:02
     * @return
     */
    public static boolean isValidTimeFormat(String timeStr) {
        String format = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) "
                + "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(timeStr);
        if (matcher.matches()) {
            pattern = Pattern.compile("(\\d{4})-(\\d+)-(\\d+).*");
            matcher = pattern.matcher(timeStr);
            if (matcher.matches()) {
                int y = Integer.valueOf(matcher.group(1));
                int m = Integer.valueOf(matcher.group(2));
                int d = Integer.valueOf(matcher.group(3));
                if (d > 28) {
                    Calendar c = Calendar.getInstance();
                    c.set(y, m - 1, 1);
                    int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    return (lastDay >= d);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 属于星期几
     *
     * @param day
     * @return
     */
    public static boolean isWeekDay(String day) {
        Calendar cal = Calendar.getInstance();
        //星期日=1 星期一=2 因此需要-1操作
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day.contains(day_of_week + "")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将时间字符串转换成Calendar
     * @param src
     * @param format
     * @return
     */
    public static Calendar StringToCalendar(String src, String format){
        // 需要解析的日期字符串
       // 解析格式，yyyy表示年，MM(大写M)表示月,dd表示天，HH表示小时24小时制，小写的话是12小时制
       // mm，小写，表示分钟，ss表示秒
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        // 获取日期实例
        Calendar calendar = Calendar.getInstance();
        try {
            // 用parse方法，可能会异常，所以要try-catch
            Date date = simpleDateFormat.parse(src);
            // 将日历设置为指定的时间
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }
}
