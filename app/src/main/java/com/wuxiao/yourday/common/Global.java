package com.wuxiao.yourday.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

/**
 * Created by wuxiaojian on 16/12/7.
 */
public class Global {
    private static final String CURRENTTHEME = "currentTheme";
    private static final String NOTE_SP = "note";

    //获取颜色
    public static int getColor(Context context, @ColorRes int color) {

        int returnColor;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            returnColor = context.getResources().getColor(color, null);
        } else {
            returnColor = context.getResources().getColor(color);

        }
        return returnColor;
    }

    public static ColorStateList getColorStateList(Context context, @ColorRes int resId) {

        ColorStateList colorStateList;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorStateList = context.getResources().getColorStateList(resId, null);
        } else {
            colorStateList = context.getResources().getColorStateList(resId);

        }
        return colorStateList;
    }

    public static Drawable getDrawable(Context context, @DrawableRes int drawRes) {

        Drawable returnDrawable;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            returnDrawable = context.getResources().getDrawable(drawRes, null);
        } else {
            returnDrawable = context.getResources().getDrawable(drawRes);

        }
        return returnDrawable;
    }


    public static int getTheme(Context context) {
        SharedPreferences settings = context.getSharedPreferences(NOTE_SP, 1);
        return settings.getInt(CURRENTTHEME, ThemeManager.THEME_ONE);
    }

    public static void setTheme(Context context, int theme) {
        SharedPreferences settings = context.getSharedPreferences(NOTE_SP, 1);
        SharedPreferences.Editor PE = settings.edit();
        PE.putInt(CURRENTTHEME, theme);
        PE.commit();
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }


    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


}
