package com.wang.tim.myfilemanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twang on 2014/12/22.
 */
public class FileManagerAdapter extends BaseAdapter {
    private Context context;
    private List<SingleFileData> items = new ArrayList<SingleFileData>();

    public void setItems(List<SingleFileData> list) {
        this.items = list;
    }

    public FileManagerAdapter(Context context){
        this.context = context;
    }
    //增加项目
    public void addItem(SingleFileData fileData){
        items.add(fileData);
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SingleFileDataView dataView;
        if(convertView==null){
            dataView = new SingleFileDataView(context,items.get(position));
        }else{
            dataView = (SingleFileDataView)convertView;
            dataView.getFileIconView().setImageDrawable(items.get(position).getFileIcon());
            dataView.getFileNameView().setText(items.get(position).getFileName());
        }
        return dataView;
    }
    //判断指定文件是否被选中
    public boolean isSelected(int position){
        return items.get(position).isSelected();
    }
}
