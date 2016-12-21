package com.wuxiao.yourday.common.popup;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wuxiao.yourday.R;
import com.wuxiao.yourday.bean.WeatherItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masai on 2016/12/19.
 */

public class WeatherPopup extends BasePopupWindow implements AdapterView.OnItemClickListener {

    private View popupView;
    private GridView gv_weather;
    private WeatherAdapter weatherAdapter;
    private List<WeatherItem> weatherList;
    private WeatherCallBack callBack;

    public WeatherPopup(Activity context, WeatherCallBack callBack) {
        super(context);
        this.callBack = callBack;


    }

    public static List<WeatherItem> getMenu(Context context) {

        TypedArray icons = context.getResources().obtainTypedArray(R.array.weather_icon);

        List<WeatherItem> list = new ArrayList<>();
        WeatherItem item;
        for (int i = 0; i < icons.length(); i++) {
            item = new WeatherItem();
            item.icon = icons.getResourceId(i, 0);
            list.add(item);
        }

        return list;
    }

    @Override
    protected Animation initShowAnimation() {
        return getAnimation();
    }


    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_normal, null);

        gv_weather = (GridView) popupView.findViewById(R.id.gv_weather);
        weatherList = getMenu(getContext());
        weatherAdapter = new WeatherAdapter(getContext(), weatherList);
        gv_weather.setAdapter(weatherAdapter);
        gv_weather.setOnItemClickListener(this);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.gv_weather);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        callBack.weatherPosition(i);
    }
}

