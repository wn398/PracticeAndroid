package com.wang.tim.yourweather.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wang.tim.yourweather.R;
import com.wang.tim.yourweather.model.City;
import com.wang.tim.yourweather.model.County;
import com.wang.tim.yourweather.model.Province;

import java.util.List;

/**
 * Created by twang on 2015/1/20.
 */
public class MyPlaceAdapter extends BaseAdapter {
    private List list;
    private Context context;
    public MyPlaceAdapter(Context context, List list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.adapter_view,null);
        TextView id = (TextView)myView.findViewById(R.id.id);
        TextView name = (TextView)myView.findViewById(R.id.name);
        Object obj = list.get(position);
        if(obj instanceof Province){
            Province p = (Province)obj;
            id.setText(String.valueOf(p.getId()));
            name.setText(p.getName());
        }else if(obj instanceof City){
            City city = (City)obj;
            id.setText(String.valueOf(city.getId()));
            name.setText(city.getName());
        }else if(obj instanceof County){
            County country = (County)obj;
            id.setText(String.valueOf(country.getId()));
            name.setText(country.getName());
        }
        return myView;
    }
}
