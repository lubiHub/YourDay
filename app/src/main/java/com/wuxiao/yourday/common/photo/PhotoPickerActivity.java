package com.wuxiao.yourday.common.photo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuxiao.yourday.R;
import com.wuxiao.yourday.bean.Photo;
import com.wuxiao.yourday.common.ThemeManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;

public class PhotoPickerActivity extends Activity implements View.OnClickListener{

    public static final String INTENT_PHOTO_PATHS = "photo_paths";;

    private static final int LATEST_PHOTO_NUM = 100;

    private static final String LATEST_PHOTO_STR = "最近照片";

    private View titleView;

    private TextView tvComplete;
    private LinearLayout tvCacel;

    private GridView gvPhotoWall;

    private List<String> photoPathList;

    private List<String> latestPhotoPathList;

    private List<Photo> photoPackList;

    private PhotoWallAdapter photoWallAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        initView();

        setViewListener();

        inflateViewData();
    }

    private void initView() {
        titleView = findViewById(R.id.relay_title);
        titleView.setBackgroundColor(ThemeManager.getInstance().getThemeColor(this));
        tvCacel = (LinearLayout) findViewById(R.id.tv_cancel);
        tvComplete = (TextView) findViewById(R.id.tv_complete);


        gvPhotoWall = (GridView) findViewById(R.id.gv_photo_wall);
    }

    private void setViewListener() {
        tvCacel.setOnClickListener(this);
        tvComplete.setOnClickListener(this);

    }


    private void inflateViewData() {

        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                photoPathList = getLatestPhotoPaths(LATEST_PHOTO_NUM);
                latestPhotoPathList = new ArrayList<>(photoPathList);
                photoPackList = getAllPhotoPaths();

                Photo LatestPhotoPack = new Photo();
                LatestPhotoPack.setFileCount(photoPathList.size());
                LatestPhotoPack.setPathName(LATEST_PHOTO_STR);
                LatestPhotoPack.setTitle(LATEST_PHOTO_STR);
                LatestPhotoPack.setFirstPhotoPath(photoPathList.get(0));

                photoPackList.add(0, LatestPhotoPack);
                handler.sendEmptyMessage(0);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;

            case R.id.tv_complete:
                Intent dataIntent = new Intent();
                dataIntent.putExtra(INTENT_PHOTO_PATHS, photoWallAdapter.getSelectPhotoPathArray());
                setResult(RESULT_OK, dataIntent);
                finish();
                break;

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            gvPhotoWall.setSelection(0);
            photoWallAdapter = new PhotoWallAdapter(PhotoPickerActivity.this, photoPathList,
                    R.layout.item_photo, photoPackList.get(0));
            gvPhotoWall.setAdapter(photoWallAdapter);


        }
    };

    /**
     * 使用ContentProvider读取SD卡最近图片
     * @param maxCount 读取的最大张数
     * @return
     */
    private List<String> getLatestPhotoPaths(int maxCount) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        // 只查询jpg和png的图片,按最新修改排序
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        List<String> latestImagePaths = null;
        if (cursor != null) {
            //从最新的图片开始读取.
            //当cursor中没有数据时，cursor.moveToLast()将返回false
            if (cursor.moveToLast()) {
                latestImagePaths = new ArrayList<String>();

                while (true) {
                    // 获取图片的路径
                    String path = cursor.getString(0);
                    latestImagePaths.add(path);

                    if (latestImagePaths.size() >= maxCount || !cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestImagePaths;
    }

    /**
     * 获取指定路径下的所有图片文件
     * @param folderPath
     * @return
     */
    private List<String> getPhotosByFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] allFileNames = folder.list();
        if (allFileNames == null || allFileNames.length == 0) {
            return null;
        }

        List<String> photoFilePaths = new ArrayList<String>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (isImage(allFileNames[i])) {
                photoFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }

        return photoFilePaths;
    }

    /**
     * 使用ContentProvider读取SD卡所有图片。
     */
    private List<Photo> getAllPhotoPaths() {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        // 只查询jpg和png的图片
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<Photo> list = null;
        if (cursor != null) {
            if (cursor.moveToLast()) {
                //路径缓存，防止多次扫描同一目录
                HashSet<String> cachePath = new HashSet<String>();
                list = new ArrayList<Photo>();
                Photo photoPack;
                while (true) {
                    // 获取图片的路径
                    String imagePath = cursor.getString(0);

                    File parentFile = new File(imagePath).getParentFile();
                    String parentPath = parentFile.getAbsolutePath();

                    //不扫描重复路径
                    if (!cachePath.contains(parentPath)) {
                        photoPack = new Photo();
                        photoPack.setFileCount(getPhotoCount(parentFile));
                        photoPack.setFirstPhotoPath(getCoverPhotoPath(parentFile));
                        photoPack.setPathName(parentPath);
                        photoPack.setTitle(getPhotoPackTitle(parentPath));
                        list.add(photoPack);
                        cachePath.add(parentPath);
                    }

                    if (!cursor.moveToPrevious()) {
                        break;
                    }
                }
            }

            cursor.close();
        }

        return list;
    }

    private String getPhotoPackTitle(String parentPath) {
        int lastSeparator = parentPath.lastIndexOf(File.separator);
        return parentPath.substring(lastSeparator + 1);
    }

    /**
     * 获取目录中图片的个数。
     */
    private int getPhotoCount(File folder) {
        int count = 0;
        File[] files = folder.listFiles();
        for (File file : files) {
            if (isImage(file.getName())) {
                count++;
            }
        }

        return count;
    }

    /**
     * 获取目录中最新的一张图片的绝对路径
     * @param folder
     * @return
     */
    private String getCoverPhotoPath(File folder) {
        File[] files = folder.listFiles();
        for (int i = files.length - 1; i >= 0; i--) {
            File file = files[i];
            if (isImage(file.getName())) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

    /**
     * 判断该文件是否是一个图片
     * @param fileName
     * @return
     */
    private boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }
}
