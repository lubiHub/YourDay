package com.wuxiao.yourday.common;

import android.content.Context;

import com.wuxiao.yourday.R;


public class TimeUtils {

    private String[] months;
    private String[] days;

    private static TimeUtils instance = null;

    public static TimeUtils getInstance(Context context) {
        if (instance == null) {
            instance = new TimeUtils(context);
        }
        return instance;
    }

    private TimeUtils(Context context) {
        months = context.getResources().getStringArray(R.array.months_name);
        days = context.getResources().getStringArray(R.array.days_name);
    }

    public String[] getMonth() {
        return months;
    }

    public String[] getDays() {
        return days;
    }
}
