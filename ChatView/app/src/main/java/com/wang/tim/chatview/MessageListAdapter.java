package com.wang.tim.chatview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by twang on 2015/1/5.
 */
public class MessageListAdapter extends ArrayAdapter<Message> {
    public int resourceId;
    public List<Message> list;
    public MessageListAdapter(Context context,int resourceId,List list){
        super(context,resourceId,list);
        this.resourceId = resourceId;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        Message message = getItem(position);
        int type = message.getType();
        if(convertView == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(this.getContext()).inflate(resourceId,null);
            viewHolder.leftLayout = (LinearLayout)view.findViewById(R.id.leftLayout);
            viewHolder.rightLayout = (LinearLayout)view.findViewById(R.id.rightLayout);
            viewHolder.leftMsg =(TextView)view.findViewById(R.id.recieve_message_text);
            viewHolder.rightMsg = (TextView)view.findViewById(R.id.send_message_text);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        if(type == message.SEND){
           viewHolder.rightMsg.setText(message.getMessage());
           viewHolder.rightLayout.setVisibility(View.VISIBLE);
           viewHolder.leftLayout.setVisibility(View.GONE);
        } else if(type == message.RECEIVE){
            viewHolder.leftMsg.setText(message.getMessage());
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
        }
        return view;
    }

    class ViewHolder{
        LinearLayout leftLayout;

        LinearLayout rightLayout;

        TextView leftMsg;

        TextView rightMsg;
    }

}
