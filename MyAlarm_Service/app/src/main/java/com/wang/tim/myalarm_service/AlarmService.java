package com.wang.tim.myalarm_service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Date;

/**
 * Created by twang on 2015/1/12.
 */
public class AlarmService extends Service {
    private static final String TAG = "AlarmService";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        //这个方法里可以开启新线程,每次调用startService,它都会执行
        new Thread() {
            @Override
            public void run() {
                Log.e(TAG, "service is executed on " + new Date().toGMTString());
            }
        }.start();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int tenMinute = 1 * 60 * 1000;//10分钟毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + tenMinute;
        //用广播形式通知执行
        Intent intent2 = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent2, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestroy()");
        super.onDestroy();
    }
}
