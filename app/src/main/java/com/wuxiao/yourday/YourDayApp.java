package com.wuxiao.yourday;

import android.app.Application;
import android.content.Context;

import com.wuxiao.yourday.common.AMapLocationManager;
import com.wuxiao.yourday.common.GreenDaoManager;


/**
 * Created by wuxiaojian on 16/12/5.
 */
public class YourDayApp extends Application {


    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        GreenDaoManager.getInstance();
        AMapLocationManager.getInstance(this);
    }

    public static Context getContext() {
        return mContext;
    }


}
