package com.wang.tim.yourweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wang.tim.yourweather.service.MyService;

/**
 * Created by twang on 2015/1/22.
 */
public class MyBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //启动service
        Intent intent1 = new Intent(context, MyService.class);
        context.startService(intent1);
    }
}
