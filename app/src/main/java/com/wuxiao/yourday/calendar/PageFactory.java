/**
 * Copyright 2016 JustWayward Team
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wuxiao.yourday.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


import com.wuxiao.yourday.R;
import com.wuxiao.yourday.common.Global;
import com.wuxiao.yourday.common.TimeUtils;

import java.util.Calendar;

public class PageFactory {
    private Context mContext;
    /**
     * 屏幕宽高
     */
    private int mHeight, mWidth;
    private Calendar calendar;
    private TimeUtils timeTools;
    private Paint mMonthPaint, mDayPaint, mWeekPaint;
    private int dateChange = 0;
    private Rect targetRect;
    private int baseline;

    public PageFactory(Context context) {
        this(context, Global.getScreenWidth(context), Global.getScreenHeight(context));
    }

    public PageFactory(Context context, int width, int height) {
        calendar = Calendar.getInstance();
        timeTools = TimeUtils.getInstance(context);
        mContext = context;
        mWidth = width;
        mHeight = height;
        targetRect = new Rect(0, 0, mWidth, mHeight / 2);

        mMonthPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMonthPaint.setTextSize(context.getResources().getDimension(R.dimen.y60));
        mMonthPaint.setColor(Color.BLACK);
        mMonthPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mMonthPaint.getFontMetricsInt();
        baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;

        mDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDayPaint.setTextSize(context.getResources().getDimension(R.dimen.y200));
        mDayPaint.setColor(Color.BLACK);
        mDayPaint.setTextAlign(Paint.Align.CENTER);
        mWeekPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWeekPaint.setTextSize(context.getResources().getDimension(R.dimen.y60));
        mWeekPaint.setColor(Color.BLACK);
        mWeekPaint.setTextAlign(Paint.Align.CENTER);

    }


    public synchronized void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        updateCalendar(canvas);
    }

    public synchronized void onDraw(Canvas canvas, int type) {
        canvas.drawColor(Color.WHITE);
        if (type == 0) {
            updateCalendar(canvas);
        } else if (type == 2) {
            dateChange = 1;
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + dateChange);
            updateCalendar(canvas);
        } else {
            dateChange = -1;
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + dateChange);
            updateCalendar(canvas);
        }
    }

    private void updateCalendar(Canvas canvas) {
        canvas.drawText(timeTools.getMonth()[calendar.get(Calendar.MONTH)], baseline, targetRect.centerX() / 2, mMonthPaint);
        canvas.drawText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), baseline, targetRect.centerX(), mDayPaint);
        canvas.drawText(timeTools.getDays()[calendar.get(Calendar.DAY_OF_WEEK) - 1], baseline, targetRect.centerX() + mContext.getResources().getDimension(R.dimen.y100), mWeekPaint);
    }


    /**
     * 设置字体颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        mDayPaint.setColor(textColor);
        mWeekPaint.setColor(textColor);
        mMonthPaint.setColor(textColor);

    }


}
