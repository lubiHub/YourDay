package com.wuxiao.yourday.common;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.media.RatingCompat;

import com.wuxiao.yourday.R;

public class ThemeManager {

    public final static int THEME_ONE = 0;
    public final static int THEME_TWO = 1;

    private static ThemeManager instance = null;

    private ThemeManager() {
    }


    public static void setCurrentTheme(Context context, int theme) {
        Global.setTheme(context, theme);
    }

    public int getCurrentTheme(Context context) {
        return Global.getTheme(context);
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            synchronized (ThemeManager.class) {
                if (instance == null) {
                    instance = new ThemeManager();
                }
            }
        }
        return instance;
    }

    public int getThemeColor(Context context) {
        int mainColor;
        switch (getCurrentTheme(context)) {
            case THEME_ONE:
                mainColor = Global.getColor(context, R.color.themeColor_one);
                break;
            case THEME_TWO:
                mainColor = Global.getColor(context, R.color.themeColor_two);
                break;
            default:
                mainColor = Global.getColor(context, R.color.themeColor_default);
                break;
        }
        return mainColor;
    }

    public Drawable getBgDrawable(Context context) {
        Drawable bgDrawable;
        switch (getCurrentTheme(context)) {
            case THEME_ONE:
                bgDrawable = Global.getDrawable(context, R.drawable.theme_bg_one);
                break;
            case THEME_TWO:
                bgDrawable = Global.getDrawable(context, R.drawable.theme_bg_two);
                break;
            default:
                bgDrawable = Global.getDrawable(context, R.drawable.theme_bg_one);
                break;

        }
        return bgDrawable;
    }


    @RatingCompat.Style
    public int getPickerStyle(Context context) {
        int style;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            style = AlertDialog.THEME_HOLO_LIGHT;
        } else {
            switch (getCurrentTheme(context)) {
                case THEME_ONE:
                    style = R.style.TakiPickerDialogTheme;
                    break;
                case THEME_TWO:
                    style = R.style.MistuhaPickerDialogTheme;
                    break;
                default:
                    //Use the system color
                    style = R.style.CustomPickerDialogTheme;
            }
        }
        return style;
    }


}
