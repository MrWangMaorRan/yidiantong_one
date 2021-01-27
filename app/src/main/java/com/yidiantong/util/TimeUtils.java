package com.yidiantong.util;

public class TimeUtils {


    /**
     * time 格式
     */
    public static String timeFormat(int time) {
        int min = time / 60;
        int second = time % 60;

        String minStr = "";
        if (min < 10) {
            minStr = "0" + min;
        } else {
            minStr = "" + min;
        }

        String secondStr = "";
        if (second < 10) {
            secondStr = "0" + second;
        } else {
            secondStr = "" + second;
        }

        return minStr + ":" + secondStr;
    }

    /**
     * 格式化 xx分xx秒
     *
     * @param duration
     * @return
     */
    public static String getDuration(int duration) {
        if (duration == -1) {
            return "";
        } else {
            int sec = duration / 1000;
            int m = sec / 60;
            int s = sec % 60;
//            String min = "";
//            String ss = "";
//            if (m < 10) {
//                min = "0" + m;
//            } else {
//                min = m + "";
//            }
//            if (s < 10) {
//                ss = "0" + s;
//            } else {
//                ss = s + "";
//            }
            if (m <= 0) {
                return s + "秒";
            } else if (m <= 59) {
                return m + "分" + s + "秒";
            } else {
                return (m / 60) + "时" + (m % 60) + "分" + s + "秒";
            }
        }
    }
}
