package com.wang.tim.myfilemanager;

import android.graphics.drawable.Drawable;

/**
 * Created by twang on 2014/12/19.
 */
public class SingleFileData implements Comparable {

    public SingleFileData(String fileName, Drawable fileIcon) {
        this.fileName = fileName;
        this.fileIcon = fileIcon;
    }

    //文件名
    private String fileName;
    //文件图标
    private Drawable fileIcon;
    //文件是否被选中
    private boolean isSelected;

    public String getFileName() {
        return fileName;
    }

    public Drawable getFileIcon() {
        return fileIcon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void sqetFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileIcon(Drawable fileIcon) {
        this.fileIcon = fileIcon;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int compareTo(Object fileData) {
        SingleFileData data = (SingleFileData)fileData;
        if(this.fileName !=null){
            return this.fileName.compareTo(data.getFileName());
        }else{
            throw new IllegalArgumentException();
        }
    }
}
