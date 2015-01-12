package com.wang.tim.myalarm_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by twang on 2015/1/11.
 */
public class MyService extends Service {
    private static final String TAG = "MyService";
    private MyBinder myBinder;
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind()");
        myBinder = new MyBinder();
        return myBinder;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
