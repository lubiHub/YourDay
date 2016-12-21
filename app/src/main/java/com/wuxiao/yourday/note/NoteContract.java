package com.wuxiao.yourday.note;


import com.wuxiao.yourday.base.ILoadView;
import com.wuxiao.yourday.base.IPresenter;
import com.wuxiao.yourday.bean.Note;

import java.util.List;

public interface NoteContract {
    interface View extends ILoadView {
        void responseNoteList(List<Note> noteList);
    }

    interface Presenter extends IPresenter {
        void getNoteList();
    }
}
