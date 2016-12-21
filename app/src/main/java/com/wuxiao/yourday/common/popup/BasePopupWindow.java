/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 razerdp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.wuxiao.yourday.common.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;

import com.wuxiao.yourday.R;

import static android.content.ContentValues.TAG;


/**
 * Created by wuxiaojian on 2016/12/14.
 */
public abstract class BasePopupWindow implements BasePopup {
    private PopupWindow mPopupWindow;
    //popup视图
    private View mPopupView;
    protected Activity mContext;
    protected View mAnimaView;
    protected View mDismissView;
    private OnDismissListener mOnDismissListener;
    private Animation mShowAnimation;
    private boolean needPopupFadeAnima = true;
    private int popupViewWidth;
    private int popupViewHeight;
    private int popupGravity = Gravity.NO_GRAVITY;

    public BasePopupWindow(Activity context) {
        initView(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public BasePopupWindow(Activity context, int w, int h) {
        initView(context, w, h);
    }

    private void initView(Activity context, int w, int h) {
        mContext = context;

        mPopupView = onCreatePopupView();
        if (mPopupView != null) {
            mPopupView.measure(w, h);
            popupViewWidth = mPopupView.getMeasuredWidth();
            popupViewHeight = mPopupView.getMeasuredHeight();
            mPopupView.setFocusableInTouchMode(true);
        }
        //默认占满全屏
        mPopupWindow = new PopupWindow(mPopupView, w, h);
        setDismissWhenTouchOuside(true);
        //默认是渐入动画
        setNeedPopupFade(Build.VERSION.SDK_INT <= 22);

        //=============================================================为外层的view添加点击事件，并设置点击消失
        mAnimaView = initAnimaView();
        mDismissView = getClickToDismissView();
        if (mDismissView != null) {
            mDismissView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        //=============================================================元素获取
        mShowAnimation = initShowAnimation();
    }

    //------------------------------------------抽象-----------------------------------------------

    /**
     * PopupWindow展示出来后，需要执行动画的View.一般为蒙层之上的View
     */
    protected abstract Animation initShowAnimation();

    /**
     * 设置一个点击后触发dismiss PopupWindow的View，一般为蒙层
     */
    public abstract View getClickToDismissView();


    /**
     * popupwindow是否需要淡入淡出
     */
    public void setNeedPopupFade(boolean needPopupFadeAnima) {
        this.needPopupFadeAnima = needPopupFadeAnima;
        setPopupAnimaStyle(needPopupFadeAnima ? R.style.PopupAnimaFade : 0);
    }

    public boolean getNeedPopupFade() {
        return needPopupFadeAnima;
    }

    /**
     * 设置popup的动画style
     */
    public void setPopupAnimaStyle(int animaStyleRes) {
        if (animaStyleRes > 0) {
            mPopupWindow.setAnimationStyle(animaStyleRes);
        }
    }


    /**
     * 调用此方法时，PopupWindow将会显示在DecorView
     */
    public void showPopupWindow() {

            ShowPopup();

    }

    private void ShowPopup() {
        try {

            mPopupWindow.showAtLocation(mContext.findViewById(android.R.id.content), popupGravity, 0, 0);

            if (mShowAnimation != null && mAnimaView != null) {
                mAnimaView.clearAnimation();
                mAnimaView.startAnimation(mShowAnimation);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * PopupWindow是否处于展示状态
     */
    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public OnDismissListener getOnDismissListener() {
        return mOnDismissListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
        mPopupWindow.setOnDismissListener(onDismissListener);
    }

    public Context getContext() {
        return mContext;
    }



    /**
     * 点击外部是否消失
     * <p>
     * dismiss popup when touch ouside from popup
     *
     * @param dismissWhenTouchOuside true for dismiss
     */
    public void setDismissWhenTouchOuside(boolean dismissWhenTouchOuside) {
        if (dismissWhenTouchOuside) {
            //指定透明背景，back键相关
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        } else {
            mPopupWindow.setFocusable(false);
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
        }

    }


    /**
     * 取消一个PopupWindow，如果有退出动画，PopupWindow的消失将会在动画结束后执行
     */
    public void dismiss() {
        if (!checkPerformDismiss()) return;
        try {

            mPopupWindow.dismiss();

        } catch (Exception e) {
            Log.d(TAG, "dismiss error");
        }
    }


    private boolean checkPerformDismiss() {
        boolean callDismiss = true;
        if (mOnDismissListener != null) {
            callDismiss = mOnDismissListener.onBeforeDismiss();
        }
        return callDismiss;
    }



    protected Animation getAnimation() {
        Animation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(300);
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }




    public static abstract class OnDismissListener implements PopupWindow.OnDismissListener {
        /**
         * <b>return ture for perform dismiss</b>
         *
         * @return
         */
        public boolean onBeforeDismiss() {
            return true;
        }
    }
}
