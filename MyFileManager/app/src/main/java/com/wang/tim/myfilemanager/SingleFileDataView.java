package com.wang.tim.myfilemanager;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by twang on 2014/12/19.
 */
public class SingleFileDataView extends LinearLayout {
    private SingleFileData fileData;
    private TextView fileNameView;
    private ImageView fileIconView;

    public TextView getFileNameView() {
        return fileNameView;
    }

    public void setFileNameView(TextView fileNameView) {
        this.fileNameView = fileNameView;
    }

    public ImageView getFileIconView() {
        return fileIconView;
    }


    public void setFileIconView(ImageView fileIconView) {
        this.fileIconView = fileIconView;
    }

    public SingleFileDataView(Context context,SingleFileData fileData) {

        super(context);
        this.fileData = fileData;
        this.setOrientation(HORIZONTAL);
        //创建文件标签图片
        this.fileIconView = new ImageView(context);
        fileIconView.setImageDrawable(fileData.getFileIcon());
        fileIconView.setPadding(5,5,5,5);
        addView(fileIconView,new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        //创建文件名
        this.fileNameView = new TextView(context);
        fileNameView.setText(fileData.getFileName());
        fileNameView.setTextSize(25);
        addView(fileNameView,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

}
