package com.wang.tim.newlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by twang on 2015/1/6.
 */
public class NewsAdapter extends ArrayAdapter<News> {
    private int resourceId;
    private List<News> list;
    public NewsAdapter(Context context,int resourceId,List<News> newsList){
        super(context,resourceId,newsList);
        this.resourceId = resourceId;
        this.list = newsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position);
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            String title = news.getTitle();
            TextView textView = (TextView)view.findViewById(R.id.newsTitle);
            textView.setText(title);
        }else{
            view = convertView;
        }

        return view;
    }
}
