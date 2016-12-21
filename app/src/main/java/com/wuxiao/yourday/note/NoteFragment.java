package com.wuxiao.yourday.note;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuxiao.yourday.R;
import com.wuxiao.yourday.base.BaseFragment;
import com.wuxiao.yourday.base.BasePresenter;
import com.wuxiao.yourday.bean.Note;
import com.wuxiao.yourday.common.GreenDaoManager;
import com.wuxiao.yourday.common.ThemeManager;
import com.wuxiao.yourday.diary.DiaryActivity;
import com.wuxiao.yourday.gen.NoteDao;
import com.wuxiao.yourday.home.HomeActivity;
import com.wuxiao.yourday.recyclerview.BaseQuickAdapter;
import com.wuxiao.yourday.recyclerview.listener.OnItemClickListener;
import com.wuxiao.yourday.setting.SettingActivity;
import com.wuxiao.yourday.viewpager.FragmentVisibilityListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by wuxiaojian on 16/12/4.
 */
public class NoteFragment extends BaseFragment<NotePresenter> implements NoteContract.View, FragmentVisibilityListener, View.OnClickListener {

    private LinearLayout note_linear;
    private TextView textView;
    private RecyclerView note_list;

    private LinearLayout buttom_toolbar;
    private TextView note_title;
    private ImageView compile;
    private ImageView set;
    private NoteAdapter noteAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_note, container, false);
        note_linear = (LinearLayout) fragment.findViewById(R.id.note_linear);
        note_list = (RecyclerView) fragment.findViewById(R.id.note_list);
        note_title = (TextView) fragment.findViewById(R.id.note_title);
        note_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        note_linear.setBackground(ThemeManager.getInstance().getBgDrawable(getActivity()));
        compile = (ImageView) fragment.findViewById(R.id.compile);
        compile.setVisibility(View.VISIBLE);
        compile.setOnClickListener(this);
        set = (ImageView) fragment.findViewById(R.id.set);
        set.setVisibility(View.VISIBLE);
        set.setOnClickListener(this);
        buttom_toolbar = (LinearLayout) fragment.findViewById(R.id.buttom_toolbar);
        buttom_toolbar.setBackgroundColor(ThemeManager.getInstance().getThemeColor(getActivity()));
        note_title.setTextColor(ThemeManager.getInstance().getThemeColor(getActivity()));
        mPresenter.getNoteList();

        return fragment;
    }

    public static NoteFragment newInstance() {
        NoteFragment fragment = new NoteFragment();
        return fragment;
    }


    @Override
    public void onFragmentVisible() {
        if (NoteAdapter.clickItem) {
            mPresenter.getNoteList();
            NoteAdapter.clickItem = false;
        }
    }

    @Override
    public void onFragmentInvisible() {

    }


    @Override
    protected NotePresenter getPresenter() {
        return new NotePresenter(getActivity(), this);
    }

    @Override
    public void loadView(Throwable e) {

    }


    @Override
    public void responseNoteList(List<Note> noteList) {
        noteAdapter = new NoteAdapter(noteList, getActivity());
        note_list.setAdapter(noteAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.compile:
                HomeActivity home = (HomeActivity) getActivity();
                home.setHomeCurrentItem(2);
                break;
            case R.id.set:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }


    }
}
