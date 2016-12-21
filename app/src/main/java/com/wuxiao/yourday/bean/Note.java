package com.wuxiao.yourday.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wuxiaojian on 16/12/4.
 */
@Entity
public class Note {
    @Id
    private Long id;
    @NotNull
    private long createTime;
    private String content;
    private String title;
    private int weatherPosition;
    private String localtion;











    @Generated(hash = 346208569)
    public Note(Long id, long createTime, String content, String title,
            int weatherPosition, String localtion) {
        this.id = id;
        this.createTime = createTime;
        this.content = content;
        this.title = title;
        this.weatherPosition = weatherPosition;
        this.localtion = localtion;
    }

    @Generated(hash = 1272611929)
    public Note() {
    }











    public int getWeatherPosition() {
        return weatherPosition;
    }

    public void setWeatherPosition(int weatherPosition) {
        this.weatherPosition = weatherPosition;
    }


    public String getLocaltion() {
        return localtion;
    }

    public void setLocaltion(String localtion) {
        this.localtion = localtion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
