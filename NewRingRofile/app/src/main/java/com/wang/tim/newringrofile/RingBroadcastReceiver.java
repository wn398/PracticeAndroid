package com.wang.tim.newringrofile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by twang on 2014/12/18.
 */
public class RingBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "RingBroadcastReceiver";
    public static final String VIBRATE_CHANGED = "VIBRATE_CHANGED";
    public static final String SILENT_CHANGED	= "SILENT_CHANGED";
    public static final String RV_CHANGED	 = "RV_CHANGED";
    public static final String RING_CHANGED = "RING_CHANGED";
    public static final int REQUEST_CODE	= 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioM = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int checkedId = intent.getIntExtra("checkedId",0);
        Log.e(TAG,checkedId+intent.getAction());
        switch (checkedId){
            case R.id.ringAndVibrate:
                ringAndVibrate(audioM);
                break;
            case R.id.ring:
                ring(audioM);
                break;
            case R.id.vibrate:
                vibrate(audioM);
                break;
            case R.id.silenceMode:
                silence(audioM);
                break;
            default:
                ring(audioM);
                break;
        }
    }

    protected void ringAndVibrate(AudioManager audioM){
        audioM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioM.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_ON);
        audioM.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,AudioManager.VIBRATE_SETTING_ON);
    }

    protected void ring(AudioManager audioM){
        audioM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioM.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_OFF);
        audioM.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,AudioManager.VIBRATE_SETTING_OFF);
    }
    protected void vibrate(AudioManager audioM){
        audioM.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        audioM.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_ON);
        audioM.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,AudioManager.VIBRATE_SETTING_ON);
    }
    protected void silence(AudioManager audioM){
        audioM.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        audioM.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_OFF);
        audioM.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,AudioManager.VIBRATE_SETTING_OFF);
    }
}
