package com.wuxiao.yourday.common.photo;

import android.content.Context;
import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;


import com.wuxiao.yourday.R;
import com.wuxiao.yourday.bean.Photo;
import com.wuxiao.yourday.common.RichEditText.WxImageLoader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hitomi on 2016/6/14.
 */
public class PhotoWallAdapter extends BaseAdapterHelper<String> {

    private WxImageLoader imageLoader = WxImageLoader.getInstance(3, WxImageLoader.Type.LIFO);

    private Photo defaultPhotoPack;



    /**
     * 记录该相册中被选中checkbox的照片路径
     */
    private Map<Integer, String> selectMap;

    /**
     * 记录所有相册中所有被选中checkbox的照片路径
     */
    private Map<Photo, Map<Integer, String>> selectionMap;

    public PhotoWallAdapter(Context context, List<String> dataList, int itemLayoutID, Photo defaultPhotoPack) {
        super(context, dataList, itemLayoutID);

        this.defaultPhotoPack = defaultPhotoPack;

        selectMap = new LinkedHashMap<>();
        selectionMap = new LinkedHashMap<>();
        selectionMap.put(defaultPhotoPack, selectMap);
    }

    @Override
    public void convert(ViewHolder viewHolder, final String item, int position) {
        ImageView ivPhoto = viewHolder.getView(R.id.iv_photo);
        CheckBox checkBox = viewHolder.getView(R.id.checkbox);

        // 先设置为默认图片
        ivPhoto.setImageResource(R.drawable.icon_empty_photo);
        // 再根据路径异步加载相册中的照片
        imageLoader.loadImage(item, ivPhoto);

        checkBox.setTag(R.id.tag_position, position);
        checkBox.setTag(R.id.tag_photo, ivPhoto);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (Integer) buttonView.getTag(R.id.tag_position);
                ImageView photo = (ImageView) buttonView.getTag(R.id.tag_photo);
                if (isChecked) {
                    selectMap.put(position, item);
                    photo.setColorFilter(Color.parseColor("#66000000"));
                } else {
                    selectMap.remove(position);
                    photo.setColorFilter(null);
                }
            }
        });
        checkBox.setChecked(item.equals(selectMap.get(position)));
    }

    public void cutoverSelectArray(Photo selectPhotoPack) {
        if (defaultPhotoPack != selectPhotoPack &&
                (selectionMap.get(defaultPhotoPack )!= null &&
                        selectionMap.get(defaultPhotoPack ).isEmpty())) {

            selectionMap.remove(defaultPhotoPack);
        }
        selectMap = selectionMap.get(selectPhotoPack);
        if (selectMap == null) {
            selectMap = new LinkedHashMap<>();
            selectionMap.put(selectPhotoPack, selectMap);
        }
    }

    public Map<Photo, Map<Integer, String>> getSelectPhotoPathMap() {
        return selectionMap;
    }

    /**
     * 获取选中照片的路径数组
     * @return
     */
    public String[] getSelectPhotoPathArray() {
        String[] photoPathArray = null;
        List<String> photoPathList = new ArrayList<>();
        Map<Integer, String> valueMap;

        for (Map.Entry<Photo, Map<Integer, String>> entry : selectionMap.entrySet()) {
            valueMap = entry.getValue();
            for (Map.Entry<Integer, String> valueEntry : valueMap.entrySet()) {
                photoPathList.add(valueEntry.getValue());
            }
        }

        photoPathArray = new String[photoPathList.size()];
        photoPathList.toArray(photoPathArray);

        return photoPathArray;
    }

}
