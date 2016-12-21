package com.wuxiao.yourday.diary;

import com.wuxiao.yourday.base.ILoadView;
import com.wuxiao.yourday.base.IPresenter;
import com.wuxiao.yourday.bean.Note;

/**
 * Created by wuxiaojian on 16/12/5.
 */
public interface DiaryContract {
    interface DiaryView extends ILoadView {
        void saveStatus();

        void responseNoteDetail(Note note);


    }

    interface DiaryPresenter extends IPresenter {
        void insertNote(String noteTitle, String noteContent, long creatTime, int weatherPosition,String location);

        void updateNote(Long id ,String noteTitle, String noteContent, long creatTime, int weatherPosition,String location);

        void getNote(long noteId);

        void delNote(long noteId);
    }
}
