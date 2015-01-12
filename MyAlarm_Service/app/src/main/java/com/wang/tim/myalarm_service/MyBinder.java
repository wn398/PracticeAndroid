package com.wang.tim.myalarm_service;

import android.os.Binder;
import android.util.Log;

/**
 * Created by twang on 2015/1/12.
 */
public class MyBinder extends Binder {
    private static final String TAG = "MyBinder";
    public void download(){
        Log.e(TAG,"download sth");
    }
    public void finish(){
        Log.e(TAG,"finished");
    }

}
