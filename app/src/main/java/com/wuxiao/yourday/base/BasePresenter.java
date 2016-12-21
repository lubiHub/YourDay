package com.wuxiao.yourday.base;

import android.app.Activity;
import android.util.Log;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wuxiaojian on 16/5/26.
 */
public abstract class BasePresenter<T extends IView> implements IPresenter {


    protected Activity mActivity;
    protected T mView;
    protected CompositeSubscription mCompositeSubscription;


    public BasePresenter(Activity activity, T view) {
        this.mActivity = activity;
        this.mView = view;
    }

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
