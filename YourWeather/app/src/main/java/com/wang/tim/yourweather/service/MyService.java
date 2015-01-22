package com.wang.tim.yourweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.wang.tim.yourweather.activity.MainActivity;
import com.wang.tim.yourweather.receiver.MyBroadCastReceiver;
import com.wang.tim.yourweather.util.Consts;
import com.wang.tim.yourweather.util.NetWorkUtil;

import java.util.Map;

/**
 * Created by twang on 2015/1/22.
 */
public class MyService extends Service {
    private final static String TAG = "MyService";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() begin update data");
        final Map<Integer,String> map = MainActivity.map;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i : map.keySet()) {
                    String result = NetWorkUtil.getDataFromNet("http://www.weather.com.cn/data/cityinfo/" + i + ".html");
                    map.put(i, result);
                }
                Message message = new Message();
                message.what = Consts.UPDATE_WEATHER_VIEW_SERVICE;
                MainActivity.handler.sendMessage(message);
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 10 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent receive = new Intent(this, MyBroadCastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, receive, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
