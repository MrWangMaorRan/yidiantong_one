package com.yidiantong.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间转换器
 * Created by Administrator on 2018/1/5.
 */

public class TimeFormat {
    // 格式
    private static String stringTimeyyyyMMddHHmmss = "yyyyMMddHHmmss";
    private static String stringTimeyyyyMMdd_HHmmss = "yyyy-MM-dd HH:mm:ss";
    private static String stringTimeYYYYMMddHHmm = "yyyy年MM月dd日 HH:mm";
    private static String stringTimeYyyyMMddHHmm = "yyyy-MM-dd HH:mm";
    private static String stringTimeYyyyMMdd = "yyyy-MM-dd";
    private static String stringTimeMMddHHmm = "MM月dd日 HH:mm";
    private static String hhmm = "HH:mm";


    /**
     * 获取当前时间
     */
    public static Date getNowTime() {
        Date curDate = new Date(System.currentTimeMillis());
        return curDate;
    }

    /**
     * 获取某日期的前第x天 / 后第x天
     * past 为负数则获取当前日期的前x天，past为正数则获取当前日期的后x天
     *
     * @param past
     * @return
     */
    public static String[] getFutureOrPastXDate(Calendar calendar, int past) {
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(date);
        String[] dateArr = result.split("-");
        return dateArr;
    }


    /**
     * 刚刚，xx秒前，xx分钟前，xx小时前，xx天前，xx月前，xx年前
     *
     * @param time
     * @return
     */
    public static String friendlyTime(Date time) {
        //获取time距离当前的秒数
        int ct = (int) ((System.currentTimeMillis() - time.getTime()) / 1000);

        if (ct == 0) {
            return "刚刚";
        }

        if (ct > 0 && ct < 60) {
            return ct + "秒前";
        }

        if (ct >= 60 && ct < 3600) {
            return Math.max(ct / 60, 1) + "分钟前";
        }
        if (ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if (ct >= 86400 && ct < 2592000) { //86400 * 30
            int day = ct / 86400;
            return day + "天前";
        }
        if (ct >= 2592000 && ct < 31104000) { //86400 * 30
            return ct / 2592000 + "月前";
        }
        return ct / 31104000 + "年前";
    }

    /**
     * 2018-05-06 转格式为 2018年05月06日
     */
    public static String stringToString(String time) {
        String[] strings = time.split("-");
        String str = "";
        for (int i = 0; i < strings.length; i++) {
            if (i == 0) {
                if (strings[i].length() == 4) {
                    strings[i] = strings[i] + "年";
                } else {
                    strings[i] = strings[i] + "月";
                }
            } else if (i == 1) {
                if (str.length() > 4) {
                    strings[i] = strings[i] + "月";
                } else {
                    strings[i] = strings[i] + "日";
                }
            } else if (i == 2) {
                strings[i] = strings[i] + "日";
            }
            str = str + strings[i];
        }

        return str;
    }

    /**
     * 2018-01-01 16:23:35
     * 时间转格式
     */
    public static String timeToMMddHHmm(String time) {
        if (time != null && !time.isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(stringTimeyyyyMMdd_HHmmss);
            Date date = null;
            try {
                date = simpleDateFormat.parse(time);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            SimpleDateFormat format = new SimpleDateFormat(stringTimeMMddHHmm);
            String strDate = format.format(date);
            return strDate;
        } else {
            return null;
        }
    }

    /**
     * 00:00 - 24:00
     *
     * @param hourMinToString
     * @return
     */
    public static String hourMinTimeToString(String hourMinToString) {
        String hourMinText = "";
        if (hourMinToString.length() > 9) {
            String firstHour = hourMinToString.substring(0, 2);
            String secendHour = hourMinToString.substring(8, 10); // 00:00 - 06:00
            hourMinText = firstHour + "时-" + secendHour + "时";
        }
        return hourMinText;
    }

    /**
     * 2018-01-01
     * long time格式转为String格式
     */
    public static String dateToStringYyyyMMdd(long time) {//可根据需要自行截取数据显示
        if (time > 0) {
            Date date = new Date();
            date.setTime(time);
            SimpleDateFormat format = new SimpleDateFormat(stringTimeYyyyMMdd);
            return format.format(date);
        } else {
            return "";
        }
    }

    /**
     * 2018-01-01 00:00
     * long time格式转为String格式
     */
    public static String dateToStringYyyyMMddHHmm(long time) {//可根据需要自行截取数据显示
        if (time > 0) {
            Date date = new Date();
            date.setTime(time);
            SimpleDateFormat format = new SimpleDateFormat(stringTimeYyyyMMddHHmm);
            return format.format(date);
        } else {
            return "";
        }
    }

    /**
     * 2018-01-01 00:00:00
     * long time格式转为String格式
     */
    public static String dateToStringYyyyMMddHHmmss(long time) {//可根据需要自行截取数据显示
        if (time > 0) {
            Date date = new Date();
            date.setTime(time);
            SimpleDateFormat format = new SimpleDateFormat(stringTimeyyyyMMdd_HHmmss);
            return format.format(date);
        } else {
            return "";
        }
    }

    /**
     * 2018-01-01
     * date格式转为String格式
     */
    public static String dateToStringYyyyMMdd(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat(stringTimeYyyyMMdd);
        return format.format(date);
    }

    /**
     * 15:00
     * date格式转为String格式
     */
    public static String dateToStringHHmm(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat(hhmm);
        return format.format(date);
    }

    /**
     * 2018年01月01日 15:00
     * date格式转为String格式
     */
    public static String dateToStringYYYYMMddHHmm(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(stringTimeYYYYMMddHHmm);
        String str = formatter.format(date);
        return str;
    }

    /**
     * 2019年04月02日 15:00
     * String格式转为Timestamp格式
     */
    public static String date2TimeStamp(String date, String format) {
        if (format.isEmpty()) {
            format = stringTimeYyyyMMdd;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 时间戳的前后distanceDay天
     * 返回格式：2019-04-10
     *
     * @param time
     * @param distanceDay 输入负值则是前distanceDay天，输入正值则是后distanceDay天
     * @return
     */
    public static String getFutureOrPastDate(long time, int distanceDay) {
        long mmDistanceTime = distanceDay * 24 * 60 * 60 * 1000;
        long newTime = time + mmDistanceTime;
        return dateToStringYyyyMMdd(newTime);
    }

    /**
     * 时间戳的前后distanceHours天
     * 返回格式：2019-04-10 14:20
     *
     * @param time
     * @param distanceHours 输入负值则是前distanceHours小时，输入正值则是后distanceHours小时
     * @return
     */
    public static String getFutureOrPastHours(long time, int distanceHours) {
        long mmDistanceTime = distanceHours * 60 * 60 * 1000;
        long newTime = time + mmDistanceTime;
        return dateToStringYyyyMMddHHmm(newTime);
    }
}
