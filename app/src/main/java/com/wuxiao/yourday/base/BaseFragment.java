package com.wuxiao.yourday.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;




/**
 * Created by wuxiaojian on 16/6/26.
 */
public abstract class BaseFragment<T extends IPresenter> extends Fragment {
    protected T mPresenter;
    protected View mView;
    protected Activity mContext;

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof BaseActivity) {
            mContext = activity;
        }
        super.onAttach(activity);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.detachView();
    }

    protected abstract T getPresenter();


}

