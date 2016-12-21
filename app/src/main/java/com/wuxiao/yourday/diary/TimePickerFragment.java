package com.wuxiao.yourday.diary;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.wuxiao.yourday.common.ThemeManager;

import java.util.Calendar;

/**
 * Created by wuxiaojian on 16/12/7.
 */
public class TimePickerFragment extends DialogFragment {


    private long savedTime;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;


    public static TimePickerFragment newInstance(long savedTime) {
        Bundle args = new Bundle();
        TimePickerFragment fragment = new TimePickerFragment();
        args.putLong("savedTime", savedTime);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        savedTime = getArguments().getLong("savedTime", -1);
        Calendar calendar = Calendar.getInstance();
        if (savedTime != -1) {
            calendar.setTimeInMillis(savedTime);
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //Note:
        //Error/TimePickerDelegate: Unable to find keycodes for AM and PM.
        //The bug was triggered only on Chinese.
        return new TimePickerDialog(getActivity(), ThemeManager.getInstance().getPickerStyle(getActivity()),
                onTimeSetListener, hour, minute, true);
    }
}