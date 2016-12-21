package com.wuxiao.yourday.note;

import android.app.Activity;

import com.wuxiao.yourday.base.BasePresenter;
import com.wuxiao.yourday.bean.Note;
import com.wuxiao.yourday.common.GreenDaoManager;
import com.wuxiao.yourday.gen.NoteDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by wuxiaojian on 16/12/5.
 */
public class NotePresenter extends BasePresenter<NoteContract.View> implements NoteContract.Presenter {
    public NotePresenter(Activity activity, NoteContract.View view) {
        super(activity, view);
    }

    @Override
    public void getNoteList() {

        NoteDao noteDao = GreenDaoManager.getInstance().getSession().getNoteDao();
        QueryBuilder<Note> queryBuilder = noteDao.queryBuilder();
        List<Note> noteList = queryBuilder.orderDesc(NoteDao.Properties.CreateTime).list();
        mView.responseNoteList(noteList);

    }
}
