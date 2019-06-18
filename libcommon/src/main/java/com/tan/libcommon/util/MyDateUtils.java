package com.tan.libcommon.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间转换
 */
@SuppressLint("SimpleDateFormat")
public class MyDateUtils {

    public static String sdf1 = "yyyy年M月";
    public static String sdf2 = "yyyy-MM-dd HH:mm";
    public static String sdf3 = "yyyy-MM-dd";
    public static String sdf4 = "yyyy/M/d";
    public static String sdf5 = "MM-dd";
    public static String sdf6 = "HH:mm";
    public static String sdf7 = "yyyy年MM月";
    public static String sdf8 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将毫秒转换为小时：分钟：秒格式
     */
    public static String ms2HMS(int _ms) {
        String HMStime;
        _ms /= 1000;
        int hour = _ms / 3600;
        int mint = (_ms % 3600) / 60;
        int sed = _ms % 60;
        String hourStr = String.valueOf(hour);
        if (hour < 10) {
            hourStr = "0" + hourStr;
        }
        String mintStr = String.valueOf(mint);
        if (mint < 10) {
            mintStr = "0" + mintStr;
        }
        String sedStr = String.valueOf(sed);
        if (sed < 10) {
            sedStr = "0" + sedStr;
        }
        HMStime = hourStr + ":" + mintStr + ":" + sedStr;
        return HMStime;
    }

    /**
     * 将毫秒转换为标准日期格式
     */
    public static String ms2Date(long _ms, String formatType) {
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat(formatType, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 标准时间转换为时间戳
     */
    public static long Date2ms(String _data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(_data);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 显示日期为x小时前、昨天、前天、x天前等
     *
     * @param longTime
     * @return
     * @throws ParseException
     */
    public static String showTimeAhead(long longTime) throws ParseException {
        String resultTime = "";

        //传入的日期
        Date date = null;
        if (isInEasternEightZones()) {
            date = long2Date(longTime);
        } else {
            date = transformTimeZone(long2Date(longTime), TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());
        }

        //当前日期
        Calendar cal = Calendar.getInstance();
        Date nowDate = new Date();
        int days = (int) (nowDate.getDay() - date.getDay());

        //如果日期相同
        if (days == 0) {
            int hour = nowDate.getHours() - date.getHours();
            //如果小时相同
            if (hour == 0) {
                resultTime = (nowDate.getMinutes() - date.getMinutes()) + "分钟前";
            } else {
                resultTime = hour + "小时前";
            }
        } else if (days == 1) {
            resultTime = "昨天";
        } else if (days == 2) {
            resultTime = "前天 ";
        } else if (days > 2 && days < 31) {
            resultTime = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            resultTime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            resultTime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            resultTime = "3个月前";
        } else {
            resultTime = ms2Date(longTime, sdf8);
        }
        return resultTime;
    }

    /**
     * 判断用户的设备时区是否为东八区（中国）
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        boolean defaultVaule = true;
        if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08")) {
            defaultVaule = true;
        } else {
            defaultVaule = false;
        }
        return defaultVaule;
    }


    /**
     * 根据不同时区，转换时间
     */
    public static Date transformTimeZone(Date date, TimeZone oldZone, TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime()) - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }


    public static Date long2Date(long currentTime) throws ParseException {
        Date date = new Date(currentTime);
        return date;
    }

    /**
     * 计算与当前的时间差
     */
    public static String DateDistance2now(long _ms) {
        SimpleDateFormat DateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Long time = new Long(_ms);
            String d = DateF.format(time);
            Date startDate = DateF.parse(d);
            Date nowDate = Calendar.getInstance().getTime();
            return DateDistance(startDate, nowDate);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 计算时间差
     */
    public static String DateDistance(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 0) {
            timeLong = 0;
        }
        if (timeLong < 60 * 1000)
            return timeLong / 1000 + "秒前";
        else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if ((timeLong / 1000 / 60 / 60 / 24) < 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(startDate);
        }
    }

    /**
     * 把时间转换成今天、昨天、前天
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String parseDate2Custom(long time) {
        String result = null;
        try {
            Calendar now = Calendar.getInstance();
            long oneDayMillis = 24 * 60 * 60 * 1000;
            long todayPassMillis = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600 + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));//毫秒数
            long nowMillis = now.getTimeInMillis();
            if (nowMillis - time < todayPassMillis) {
                result = new SimpleDateFormat("HH:mm").format(time);
            } else if (nowMillis - time < (todayPassMillis + oneDayMillis)) {
                result = "昨天";
            } else if (nowMillis - time < (todayPassMillis + oneDayMillis * 2)) {
                result = "前天";
            } else if (now.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                result = new SimpleDateFormat("MM-dd").format(time);
            } else {
                result = new SimpleDateFormat("yyyy-MM-dd").format(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
