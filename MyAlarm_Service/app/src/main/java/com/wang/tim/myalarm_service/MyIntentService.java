package com.wang.tim.myalarm_service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by twang on 2015/1/12.
 */
public class MyIntentService extends IntentService {
    private static final String TAG = "MyIntentService";
    public MyIntentService(){
        super("MyIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG,"onHandleIntent()");
        Log.e(TAG,"Thread id:"+Thread.currentThread().getId());
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy()");
        super.onDestroy();
    }
}
