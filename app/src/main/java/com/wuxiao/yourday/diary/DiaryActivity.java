package com.wuxiao.yourday.diary;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.base.BaseActivity;
import com.wuxiao.yourday.bean.Note;
import com.wuxiao.yourday.bean.WeatherItem;
import com.wuxiao.yourday.common.AMapLocationManager;
import com.wuxiao.yourday.common.RichEditText.EditTextData;
import com.wuxiao.yourday.common.RichEditText.RichEditText;
import com.wuxiao.yourday.common.RichEditText.RichTextView;
import com.wuxiao.yourday.common.ThemeManager;
import com.wuxiao.yourday.common.photo.PhotoPickerActivity;
import com.wuxiao.yourday.common.popup.WeatherCallBack;
import com.wuxiao.yourday.common.popup.WeatherPopup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

import static com.wuxiao.yourday.common.popup.WeatherPopup.getMenu;

/**
 * Created by wuxiaojian on 16/12/5.
 */
public class DiaryActivity extends BaseActivity<DiaryPresenter> implements DiaryContract.DiaryView, DatePickerDialog.OnDateSetListener, View.OnClickListener
        , TimePickerDialog.OnTimeSetListener, WeatherCallBack, AMapLocationListener {
    public static final int REQUEST_PHOTO = 0x0023;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private LinearLayout diary_time_information;
    private TextView diary_month;
    private TextView diary_date;
    private TextView diary_day;
    private TextView diary_time;
    private TextView diary_location;
    private LinearLayout buttom_toolbar;

    private RichTextView diary_content;
    private LinearLayout diary_location_status;
    private ImageView save;
    private ImageView input;
    private ImageView photo;
    private ImageView location;
    private ImageView del;
    private long noteid;
    private RichEditText note_rich;
    private FrameLayout note_back;
    private int weatherPisition;
    private List<WeatherItem> weatherList;
    private WeatherPopup weatherPopup;
    private ImageView weather_icon;
    private boolean enabled;
    private AMapLocationClient client;
    private StringBuilder update_location;
    private String lastLocation;
    private Subscription subscribe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        findViewById(R.id.shadow_view).setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        noteid = bundle.getLong("noteId");
        weatherList = getMenu(this);
        weatherPopup = new WeatherPopup(this, this);
        diary_time_information = (LinearLayout) findViewById(R.id.diary_time_information);
        diary_month = (TextView) findViewById(R.id.diary_month);
        diary_date = (TextView) findViewById(R.id.diary_date);
        diary_day = (TextView) findViewById(R.id.diary_day);
        diary_time = (TextView) findViewById(R.id.diary_time);
        note_rich = (RichEditText) findViewById(R.id.note_rich);
        note_back = (FrameLayout) findViewById(R.id.note_back);
        note_back.setVisibility(View.VISIBLE);
        note_back.setOnClickListener(this);
        diary_location = (TextView) findViewById(R.id.diary_location);
        diary_content = (RichTextView) findViewById(R.id.diary_content);
        weather_icon = (ImageView) findViewById(R.id.weather_icon);
        weather_icon.setOnClickListener(this);
        weather_icon.setImageResource(weatherList.get(0).icon);
        diary_location_status = (LinearLayout) findViewById(R.id.diary_location_status);
        save = (ImageView) findViewById(R.id.save);
        save.setOnClickListener(this);
        location = (ImageView) findViewById(R.id.location);
        location.setVisibility(View.VISIBLE);
        location.setOnClickListener(this);
        photo = (ImageView) findViewById(R.id.photo);
        photo.setVisibility(View.VISIBLE);
        photo.setOnClickListener(this);
        input = (ImageView) findViewById(R.id.input);
        input.setVisibility(View.VISIBLE);
        input.setOnClickListener(this);
        del = (ImageView) findViewById(R.id.del);
        del.setVisibility(View.VISIBLE);
        del.setOnClickListener(this);
        buttom_toolbar = (LinearLayout) findViewById(R.id.buttom_toolbar);
        buttom_toolbar.setBackgroundColor(ThemeManager.getInstance().getThemeColor(this));
        diary_time_information.setOnClickListener(this);
        diary_content.setTitleColor(ThemeManager.getInstance().getThemeColor(this));
        diary_time_information.setBackgroundColor(ThemeManager.getInstance().getThemeColor(this));
        diary_location_status.setBackgroundColor(ThemeManager.getInstance().getThemeColor(this));
        viewEnabled(false);
        client = AMapLocationManager.getInstance(this.getApplication());
        client.setLocationListener(this);
        client.startLocation();
        mPresenter.getNote(noteid);


    }


    private void setCurrentTime(boolean updateCurrentTime, Note note) {
        if (updateCurrentTime) {
            long time = note.getCreateTime();
            calendar.setTimeInMillis(time);
        }
        diary_month.setText(timeUtils.getMonth()[calendar.get(Calendar.MONTH)]);
        diary_date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        diary_day.setText(timeUtils.getDays()[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        diary_time.setText(sdf.format(calendar.getTime()));
    }

    private void viewEnabled(boolean enabled) {
        this.enabled = enabled;
        diary_time_information.setEnabled(enabled);
        photo.setEnabled(enabled);
        weather_icon.setEnabled(enabled);
        if (!enabled) {
            input.setImageDrawable(getResources().getDrawable(R.drawable.icon_uninput));
            note_rich.setVisibility(View.GONE);
            diary_content.setVisibility(View.VISIBLE);
            photo.setImageDrawable(getResources().getDrawable(R.drawable.icon_photo_n));
            del.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
        } else {
            del.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            photo.setImageDrawable(getResources().getDrawable(R.drawable.icon_photo));
            note_rich.setVisibility(View.VISIBLE);
            diary_content.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.diary_time_information:
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(calendar.getTimeInMillis());
                datePickerFragment.setOnDateSetListener(this);
                datePickerFragment.show(getSupportFragmentManager(), "datePickerFragment");
                break;
            case R.id.save:
                save();
                break;
            case R.id.input:
                input.setImageDrawable(getResources().getDrawable(R.drawable.icon_input));
                viewEnabled(true);
                break;
            case R.id.location:
                break;
            case R.id.photo:
                startActivityForResult(new Intent(this, PhotoPickerActivity.class), REQUEST_PHOTO);
                break;
            case R.id.del:
                showDialog("删除日记", "确定删除这篇日记", R.id.del);
                break;
            case R.id.weather_icon:
                weatherPopup.showPopupWindow();
                break;
            case R.id.note_back:
                if (enabled) {
                    showDialog("内容修改", "内容已修改是否保存", R.id.note_back);
                } else {
                    onBackPressed();
                }
                break;
            default:
                break;
        }
    }


    private void save() {
        List<EditTextData> editList = note_rich.GetEditData();

        final StringBuilder content = new StringBuilder();
        subscribe = Observable.from(editList).subscribe(new Action1<EditTextData>() {
            @Override
            public void call(EditTextData data) {
                if (data.getInputStr() != null) {

                    content.append(data.getInputStr()).append("*");
                } else if (data.getImagePath() != null) {

                    content.append(data.getImagePath()).append("*");
                }
            }
        });
        String title = note_rich.getTitleData();
        long createTime =   System.currentTimeMillis();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            if (update_location != null) {
                mPresenter.updateNote(noteid, title, content.toString(), createTime, weatherPisition, update_location.toString());
            } else {
                mPresenter.updateNote(noteid, title, content.toString(), createTime, weatherPisition, lastLocation);

            }
            diary_content.setTitle(title);


            String contentDate = content.toString();
            String sp = "\\*";
            String[] contentList = contentDate.split(sp);

            for (int i = 0; i < contentList.length; i++) {
                diary_content.addContent(contentList[i], i, true);
            }
        } else {
            Toast.makeText(this, getString(R.string.diary_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        viewEnabled(false);
    }

    @Override
    public void loadView(Throwable e) {

    }

    @Override
    protected DiaryPresenter getPresenter() {
        return new DiaryPresenter(this, this);
    }


    @Override
    public void saveStatus() {

    }

    @Override
    public void responseNoteDetail(Note note) {
        setCurrentTime(true, note);
        note_rich.setTitle(note.getTitle());
        note_rich.setContent(note.getContent());
        weather_icon.setImageResource(weatherList.get(note.getWeatherPosition()).icon);
        diary_content.setTitle(note.getTitle());
        lastLocation = note.getLocaltion();
        diary_location.setText(lastLocation);
        String content = note.getContent();

        String sp = "\\*";
        String[] contentList = content.split(sp);

        for (int i = 0; i < contentList.length; i++) {
            note_rich.addContent(contentList[i], i);
            diary_content.addContent(contentList[i], i, false);
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (view.isShown()) {
            calendar.set(year, monthOfYear, dayOfMonth);
            setCurrentTime(false, null);
            TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(calendar.getTimeInMillis());
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(getSupportFragmentManager(), "timePickerFragment");
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            setCurrentTime(false, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_PHOTO) {
            String[] photoPaths = data.getStringArrayExtra(PhotoPickerActivity.INTENT_PHOTO_PATHS);
            note_rich.addImageArray(photoPaths);
        }
    }

    @Override
    public void weatherPosition(int position) {
        weatherPisition = position;
        weather_icon.setImageResource(weatherList.get(position).icon);
        weatherPopup.dismiss();
    }

    private void showDialog(String title, String content, final int id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (id == R.id.note_back) {
                    finish();
                }
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (id == R.id.del) {
                    mPresenter.delNote(noteid);
                    finish();
                } else if (id == R.id.note_back) {

                    save();
                    finish();
                }
            }
        });
        builder.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
            if (enabled) {
                showDialog("内容修改", "内容已修改是否保存", R.id.note_back);
            } else {
                onBackPressed();
            }
        return false;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            update_location = new StringBuilder();
            update_location.append(aMapLocation.getCity());
            update_location.append(aMapLocation.getDistrict());
        }
    }


}
