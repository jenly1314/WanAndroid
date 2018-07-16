package com.king.wanandroid.util;

import android.content.Context;
import android.content.res.Resources;

import com.king.wanandroid.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public final class TimeUtils {

    /** 时间格式：yyyyMMddHHmmss */
    public static final String FORMAT_Y_TO_S = "yyyyMMddHHmmss";

    /** 时间格式：yyyy-MM-dd HH:mm:ss */
    public static final String FORMAT_Y_TO_S_EN = "yyyy-MM-dd HH:mm:ss";

    /** 时间格式：yyyy-MM-dd */
    public static final String FORMAT_Y_TO_D = "yyyy-MM-dd";


    //------------------------------------
    /**
     * 异常时间
     */
    private static final int EXCEPTION_TIME = -1;

    /**
     * 一分钟的毫秒值
     */
    public static final long ONE_MINUTE = 60 * 1000;

    /**
     * 一小时的毫秒值
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;

    /**
     * 一天的毫秒值
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;

    /**
     * 一月的毫秒值
     */
    public static final long ONE_MONTH = 30 * ONE_DAY;

    /**
     * 一年的毫秒值
     */
    public static final long ONE_YEAR = 12 * ONE_MONTH;

    private TimeUtils(){
    }


    /**
     * 获得格式化后的时间
     * @param time
     * @return
     */
    public static String getTime(long time){
        return getTime(time,FORMAT_Y_TO_D);
    }

    /**
     * 获取格式化后的时间
     * @param time
     * @param format
     * @return
     */
    public static String getTime(long time,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 获得相对时间
     * @param context
     * @param lastUpdateTime
     * @return
     */
    public static String getUpdatedRelativeTime(Context context, long lastUpdateTime){
        Resources resources = context.getResources();
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastUpdateTime;
        long timeIntoFormat;
        if (lastUpdateTime == EXCEPTION_TIME) {
            return "";
        }else if (timePassed < ONE_MINUTE) {
            return resources.getString(R.string.updated_just_now);
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            int value = (int)timeIntoFormat;
            return String.format(resources.getString(R.string.updated_before_n_min), value);
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            int value = (int)timeIntoFormat;
            return String.format(resources.getString(R.string.updated_before_n_hour), value);
        } else if (timePassed < 3 * ONE_DAY) {
            timeIntoFormat = timePassed / ONE_DAY;
            int value = (int)timeIntoFormat;
            return String.format(resources.getString(R.string.updated_before_n_day), value);
        }

        return getTime(lastUpdateTime);

    }
}
