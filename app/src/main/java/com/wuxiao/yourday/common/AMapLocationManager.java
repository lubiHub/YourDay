package com.wuxiao.yourday.common;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

/**
 * Created by masai on 2016/12/21.
 */

public class AMapLocationManager {
    private static AMapLocationClient mInstance; //单例
    private static AMapLocationClientOption locationOption;



    public static AMapLocationClient getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AMapLocationClient.class) {
                if (mInstance == null) {
                    mInstance = new AMapLocationClient(context);
                    locationOption = new AMapLocationClientOption();
                    locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                    locationOption.setOnceLocation(false);
                    locationOption.setNeedAddress(true);
                    locationOption.setGpsFirst(false);
                    locationOption.setInterval(60000);
                    mInstance.setLocationOption(locationOption);
                }
            }
        }
        return mInstance;
    }
}
