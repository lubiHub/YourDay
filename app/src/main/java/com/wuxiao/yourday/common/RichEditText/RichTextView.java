package com.wuxiao.yourday.common.RichEditText;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.common.ImageLoadFresco;


public class RichTextView extends ScrollView {
    private LayoutInflater inflater;
    private LinearLayout parentLayout;
    private LinearLayout titleLayout;
    private final int DEFAULT_MARGING = dip2px(15);
    private TextView tvTextLimit;
    private int viewTagID = 1;
    private final int DEFAULT_IMAGE_HEIGHT = dip2px(300);

    private LinearLayout containerLayout;
    public RichTextView(Context context) {
        this(context, null);
    }
    private Context context;
    private int width;
    private int height;

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context =context;
        inflater = LayoutInflater.from(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        initParentLayout();
        initTitleLayout();
        initContainerLayout();
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
            final RelativeLayout imageLayout = createImageLayout(content);
            SimpleDraweeView imageView = (SimpleDraweeView) imageLayout.getChildAt(0);
            new ImageLoadFresco.LoadImageFrescoBuilder(context, imageView, "file:///" + content).setIsRadius(true).build();
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

    private RelativeLayout createImageLayout(String content) {

        RelativeLayout.LayoutParams contentImageLp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        SimpleDraweeView  simpleDraweeView = (SimpleDraweeView) inflater.inflate(R.layout.drawee_view,
                null);
        simpleDraweeView.setLayoutParams(contentImageLp);

        RelativeLayout layout = new RelativeLayout(getContext());
        layout.addView(simpleDraweeView);
        layout.setTag(viewTagID++);
        setFocusOnView(layout, true);

        int  bitmapWidth = getbitmapWidth(content);
        int  bitmapHeight =getbitmapHeight(content);
        if (getbitmapWidth(content)>width -width/3){
            bitmapWidth =bitmapWidth -bitmapWidth/3;
            bitmapHeight =bitmapHeight -bitmapHeight/3;
        }
        // 调整imageView的外边距
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                bitmapWidth, bitmapHeight);
        lp.bottomMargin = DEFAULT_MARGING;
        lp.leftMargin = DEFAULT_MARGING;
        lp.rightMargin = DEFAULT_MARGING;
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

    public int getbitmapHeight(String content){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(content, options);

        return options.outHeight;
    }
    public int getbitmapWidth(String content){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(content, options);
        return options.outWidth;
    }
}
