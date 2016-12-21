package com.wuxiao.yourday.common.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wuxiao.yourday.R;
import com.wuxiao.yourday.bean.WeatherItem;

import java.util.List;

/**
 * Created by masai on 2016/12/19.
 */

public class WeatherAdapter extends MyBaseAdapter<WeatherItem> {

    private ViewHolder viewHolder;

    public WeatherAdapter(Context context, List<WeatherItem> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.weather_item, null);
            viewHolder.weather_icon = (ImageView) convertView.findViewById(R.id.weather_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        WeatherItem model = data.get(position);
        if (model != null) {
            viewHolder.weather_icon.setImageResource(model.icon);
        }
        return convertView;
    }

    public class ViewHolder {
        private ImageView weather_icon;
    }
}
