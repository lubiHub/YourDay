package com.wuxiao.yourday.note;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.wuxiao.yourday.R;
import com.wuxiao.yourday.bean.Note;
import com.wuxiao.yourday.common.ThemeManager;
import com.wuxiao.yourday.diary.DiaryActivity;
import com.wuxiao.yourday.recyclerview.BaseQuickAdapter;
import com.wuxiao.yourday.recyclerview.BaseViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by wuxiaojian on 16/12/10.
 */
public class NoteAdapter extends BaseQuickAdapter<Note, BaseViewHolder> {

    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private String[] weeksName;
    private ThemeManager themeManager;
    private List<Note> data;
    public static boolean clickItem = false;

    public NoteAdapter(List<Note> data, Context context) {
        super(R.layout.note_item, data);
        this.data = data;
        weeksName = context.getResources().getStringArray(R.array.weeks_name);
        ThemeManager.getInstance();
    }


    @Override
    protected void convert(BaseViewHolder helper, final Note item) {

        Calendar calendar = Calendar.getInstance();
        Date date = new Date(item.getCreateTime());
        calendar.setTime(date);
        if (showHeader(helper.getPosition())) {
            helper.setVisible(R.id.note_header, true);
            helper.setText(R.id.note_header, String.valueOf(calendar.get(Calendar.MONTH) + 1));
        } else {

            helper.setVisible(R.id.note_header, false);
        }
        helper.setTextColor(R.id.note_content, ThemeManager.getInstance().getThemeColor(mContext));
        String content = item.getContent();

        String sp = "\\*";
        String[] contentList = content.split(sp);
        StringBuilder note_content = new StringBuilder();
        for (int i = 0; i < contentList.length; i++) {
            if (isImage(contentList[i])) {
                note_content.append("[picture]");
            } else {
                note_content.append(contentList[i]);
            }
        }
        helper.setText(R.id.note_content, note_content.toString());
        helper.setTextColor(R.id.note_title, ThemeManager.getInstance().getThemeColor(mContext));
        helper.setText(R.id.note_title, item.getTitle());
        helper.setTextColor(R.id.note_date, ThemeManager.getInstance().getThemeColor(mContext));
        helper.setText(R.id.note_date, String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        helper.setTextColor(R.id.note_day, ThemeManager.getInstance().getThemeColor(mContext));
        helper.setText(R.id.note_day, weeksName[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        helper.setTextColor(R.id.note_time, ThemeManager.getInstance().getThemeColor(mContext));
        helper.setText(R.id.note_time, String.valueOf(dateFormat.format(calendar.getTime())));
        helper.setOnClickListener(R.id.note_card, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItem = true;
                Intent intent = new Intent(mContext, DiaryActivity.class);
                intent.putExtra("noteId", item.getId());
                mContext.startActivity(intent);
            }
        });

    }

    private boolean showHeader(int position) {
        if (position == 0) {
            return true;
        } else {

            Calendar previousCalendar = new GregorianCalendar();
            previousCalendar.setTime(new Date(data.get(position - 1).getCreateTime()));
            Calendar currentCalendar = new GregorianCalendar();
            currentCalendar.setTime(new Date(data.get(position).getCreateTime()));
            if (previousCalendar.get(Calendar.YEAR) != currentCalendar.get(Calendar.YEAR)) {
                return true;
            } else {
                if (previousCalendar.get(Calendar.MONTH) != currentCalendar.get(Calendar.MONTH)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }
}
