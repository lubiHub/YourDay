package com.wuxiao.yourday.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;


import com.wuxiao.yourday.common.TimeUtils;

import java.util.Calendar;


/**
 * Created by wuxiaojian on 2016/6/13.
 */
public abstract class BaseActivity<T extends IPresenter> extends FragmentActivity {
    protected T mPresenter;
    protected Activity mContext;
    protected Calendar calendar;
    protected TimeUtils timeUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        timeUtils = TimeUtils.getInstance(this.getApplicationContext());
        mContext = this;
        mPresenter = getPresenter();
    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroy();
    }

    protected abstract T getPresenter();


}
