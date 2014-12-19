package com.wang.tim.newringrofile;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.zip.Inflater;

public class NormalFragment extends Fragment {
    private static final String TAG="NormalFragment";
    private AlarmManager alarmManager;
    private RadioGroup mRadioGroup;
    public NormalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.normal,container,false);
        alarmManager= (AlarmManager) this.getActivity().getSystemService(Context.ALARM_SERVICE);
         mRadioGroup = (RadioGroup)view.findViewById(R.id.modleRadioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    recoverNormal(mRadioGroup);
                switch (checkedId){
                    case R.id.ringAndVibrate:
                        ringAndVibrate();
                        break;
                    case R.id.vibrate:
                        vibrate();
                        break;
                    case R.id.ring:
                        ring();
                        break;
                    case R.id.silenceMode:
                        silence();
                        break;
                }
                RadioButton radioButton = (RadioButton)getView().findViewById(checkedId);
                if(radioButton!=null){
                    radioButton.setTextSize(30);
                }
            }
        });
        //add the monitor for the RadioButton
        for(int i=0;i<mRadioGroup.getChildCount();i++){
            RadioButton radioButton = (RadioButton)mRadioGroup.getChildAt(i);
            radioButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    RadioButton radio = (RadioButton) v;
                    if (!radio.isChecked()) {
                        radio.setTextSize(15);
                        return false;
                    }else {
                        radio.setTextSize(30);
                        return false;
                    }
                }
            });
        }
        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        if(mRadioGroup!=null) {
            inicialGroup(mRadioGroup);
        }else{
            mRadioGroup = (RadioGroup)getView().findViewById(R.id.modleRadioGroup);
            inicialGroup(mRadioGroup);
        }
    }

    protected void recoverNormal(RadioGroup group){
        for(int i=0;i<group.getChildCount();i++){
            RadioButton button = (RadioButton)group.getChildAt(i);
            button.setTextSize(15);
        }
    }
    protected void inicialGroup(RadioGroup group){
        for(int i=0;i<group.getChildCount();i++){
            RadioButton button = (RadioButton)group.getChildAt(i);
            if(button.getId()==currentMode()){
                button.setChecked(true);
                button.setTextSize(30);
            }
        }
    }
    //ring and vibrate
    protected void ringAndVibrate(){
        Intent intent = new Intent(RingBroadcastReceiver.RV_CHANGED);
        intent.putExtra("checkedId",R.id.ringAndVibrate);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this.getActivity(),RingBroadcastReceiver.REQUEST_CODE,intent,0);
        Log.e(TAG, "" + intent);
        alarmManager.set(AlarmManager.RTC_WAKEUP,0,alarmIntent);
    }
    //ring
    protected void ring(){
        Intent intent = new Intent(RingBroadcastReceiver.RING_CHANGED);
        intent.putExtra("checkedId",R.id.ring);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this.getActivity(),RingBroadcastReceiver.REQUEST_CODE,intent,0);
        Log.e(TAG,""+intent);
        alarmManager.set(AlarmManager.RTC_WAKEUP,0,alarmIntent);
    }
    //vibrate
    protected void vibrate(){
        Intent intent = new Intent(RingBroadcastReceiver.VIBRATE_CHANGED);
        intent.putExtra("checkedId",R.id.vibrate);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this.getActivity(),RingBroadcastReceiver.REQUEST_CODE,intent,0);
        Log.e(TAG,""+intent);
        alarmManager.set(AlarmManager.RTC_WAKEUP,0,alarmIntent);
    }
    //silence
    protected void silence(){
        Intent intent = new Intent(RingBroadcastReceiver.SILENT_CHANGED);
        intent.putExtra("checkedId",R.id.silenceMode);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this.getActivity(),RingBroadcastReceiver.REQUEST_CODE,intent,0);
        Log.e(TAG,""+intent);
        alarmManager.set(AlarmManager.RTC_WAKEUP,0,alarmIntent);
    }
    //取得当前情景模式
    protected int currentMode(){
        AudioManager audioManager= (AudioManager)this.getActivity().getSystemService(Context.AUDIO_SERVICE);
        switch (audioManager.getRingerMode()){
            case AudioManager.RINGER_MODE_SILENT:
                return R.id.silenceMode;
            case AudioManager.RINGER_MODE_VIBRATE:
                return R.id.vibrate;
        }

        if(audioManager.shouldVibrate(AudioManager.VIBRATE_TYPE_RINGER)){
            return R.id.ringAndVibrate;
        }else{
            return R.id.ring;
        }
    }
}
