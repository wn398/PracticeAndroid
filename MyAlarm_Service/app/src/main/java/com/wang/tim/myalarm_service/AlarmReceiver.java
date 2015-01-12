package com.wang.tim.myalarm_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by twang on 2015/1/12.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //每次执行一次广播，就启动一次service
        Intent intent2 = new Intent(context,AlarmService.class);
        context.startService(intent2);
    }
}
