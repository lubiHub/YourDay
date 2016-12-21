package com.wuxiao.yourday.common.RichEditText;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wuxiao.yourday.R;


public class RichTextView extends ScrollView {
    private LayoutInflater inflater;
    private LinearLayout parentLayout;
    private LinearLayout titleLayout;
    private final int DEFAULT_MARGING = dip2px(15);
    private TextView tvTextLimit;
    private int viewTagID = 1;
    private final int DEFAULT_IMAGE_HEIGHT = dip2px(300);
    private WxImageLoader imageLoader;
    private LinearLayout containerLayout;
    public RichTextView(Context context) {
        this(context, null);
    }
    private Context context;

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context =context;
        inflater = LayoutInflater.from(context);
        initParentLayout();
        initTitleLayout();
        initContainerLayout();
        imageLoader = WxImageLoader.getInstance(3, WxImageLoader.Type.LIFO);
    }

    private void initParentLayout() {
        // 因为ScrollView的子view只能有一个，并且是ViewGroup,所以先创建一个Linearlayout父容器，用来放置所有其他ViewGroup
        parentLayout = new LinearLayout(getContext());
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        parentLayout.setLayoutParams(layoutParams);

        addView(parentLayout);
    }


    public void addContent(String content, final int position,boolean isSave){

        if(isSave && position==0){

            containerLayout.removeAllViews();
        }

        if (isImage(content) ){
            final RelativeLayout imageLayout = createImageLayout();
            ImageView imageView = (ImageView) imageLayout.getChildAt(0);
            PointF pointF = new PointF();
            pointF.x = getWidth() - 2 * DEFAULT_MARGING;
            pointF.y = DEFAULT_IMAGE_HEIGHT;
            imageLoader.loadImage(content, imageView, pointF);
            imageView.setTag(content);
            containerLayout.addView(imageLayout, position);


        }else {
            insertTextViewAtIndex(position,content);
        }





    }

    public void setTitle(String title) {
        tvTextLimit.setText(title);
    }

    public void setTitleColor(int color) {
        tvTextLimit.setTextColor(color);
    }

    public  TextView insertTextViewAtIndex(final int index, String editStr) {
        TextView textView = createEditText("");
        textView.setText(editStr);
        // 请注意此处，EditText添加、或删除不触动Transition动画
        containerLayout.setLayoutTransition(null);
        containerLayout.addView(textView, index);
        return textView;
    }

    private TextView createEditText(String hint) {
        TextView textView = new TextView(getContext());
        textView.setTag(viewTagID++);

        textView.setGravity(Gravity.TOP);
        textView.setCursorVisible(true);
        textView.setBackgroundResource(android.R.color.transparent);
        textView.setTextColor(Color.parseColor("#333333"));
        textView.setTextSize(14);

        // 调整EditText的外边距
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = DEFAULT_MARGING;
        lp.leftMargin = DEFAULT_MARGING;
        lp.rightMargin= DEFAULT_MARGING;
        textView.setLayoutParams(lp);

        return textView;
    }

    private RelativeLayout createImageLayout() {
        RelativeLayout.LayoutParams contentImageLp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(contentImageLp);
        imageView.setImageResource(R.drawable.icon_empty_photo);

        RelativeLayout.LayoutParams closeImageLp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        closeImageLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        closeImageLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        closeImageLp.setMargins(0, dip2px(10), dip2px(10), 0);
        RelativeLayout layout = new RelativeLayout(getContext());
        layout.addView(imageView);
        layout.setTag(viewTagID++);
        setFocusOnView(layout, true);


        // 调整imageView的外边距
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, DEFAULT_IMAGE_HEIGHT);
        lp.bottomMargin = DEFAULT_MARGING;
        lp.leftMargin = DEFAULT_MARGING;
        lp.rightMargin= DEFAULT_MARGING;
        layout.setLayoutParams(lp);

        return layout;
    }
    private boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }
    /**
     * 初始化ContainerLayout文本内容容器
     */
    private void initContainerLayout() {
        containerLayout = createContaniner();
        parentLayout.addView(containerLayout);
    }

    private void initTitleLayout() {

        // 创建标题栏的ViewGroup
        titleLayout = new LinearLayout(getContext());
        titleLayout.setOrientation(LinearLayout.VERTICAL);
        titleLayout.setPadding(0, DEFAULT_MARGING, 0, DEFAULT_MARGING);

        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        titleLayout.setLayoutParams(titleLayoutParams);
        titleLayout.setGravity(Gravity.CENTER_VERTICAL);
        parentLayout.addView(titleLayout);

        tvTextLimit = new TextView(getContext());
        tvTextLimit.setTextSize(context.getResources().getDimension(R.dimen.y10));
        tvTextLimit.setGravity(Gravity.CENTER);
        titleLayout.addView(tvTextLimit);


    }

    private int dip2px(float dipValue) {
        float m = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    @NonNull
    private LinearLayout createContaniner() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        final LinearLayout containerLayout = new LinearLayout(getContext()) {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                return true;
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {

                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }
                return true;
            }

            @Override
            public boolean dispatchTouchEvent(MotionEvent ev) {

                getParent().requestDisallowInterceptTouchEvent(false);

                return super.dispatchTouchEvent(ev);
            }


        };
        containerLayout.setPadding(0, DEFAULT_MARGING, 0, DEFAULT_MARGING);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setBackgroundColor(Color.WHITE);
        containerLayout.setLayoutParams(layoutParams);
        return containerLayout;
    }
    private void setFocusOnView(View view, boolean isFocusable) {
        view.setClickable(isFocusable);
        view.setFocusable(isFocusable);
        view.setFocusableInTouchMode(isFocusable);

    }
}
