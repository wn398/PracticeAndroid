package com.wang.tim.newringrofile;


import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DefineFragment extends Fragment {
    private int volume;
    private ImageView imageView;

    public DefineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.define,container,false);

        //取得手机初始音量，并初始化进度条
        final AudioManager audioM = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        volume = audioM.getStreamVolume(AudioManager.STREAM_RING);
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.volumeBar);
        progressBar.setProgress(volume);
        //Get the now model and set the icon 取得初始模式，并分别设置图标
        int mode = audioM.getRingerMode();
        imageView = (ImageView)view.findViewById(R.id.profile);
        if(mode==AudioManager.RINGER_MODE_NORMAL){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.normal));
        }else if(mode==AudioManager.RINGER_MODE_SILENT){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.silence));
        }else if(mode==AudioManager.RINGER_MODE_VIBRATE){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.vibrate));
        }

        ImageButton down = (ImageButton)view.findViewById(R.id.down);
        //按降低音量键
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adjustVolume可以增加和降低音量
                audioM.adjustVolume(AudioManager.ADJUST_LOWER,0);
                volume = audioM.getStreamVolume(AudioManager.STREAM_RING);
                //设置进度条
                progressBar.setProgress(volume);
                //设置图标
                int mode = audioM.getRingerMode();
                if(mode==AudioManager.RINGER_MODE_NORMAL){
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.normal));
                }else if(mode==AudioManager.RINGER_MODE_SILENT){
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.silence));
                }else if(mode==AudioManager.RINGER_MODE_VIBRATE){
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.vibrate));
                }
            }
        });
        //提高音量
        ImageButton up = (ImageButton)view.findViewById(R.id.up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioM.adjustVolume(AudioManager.ADJUST_RAISE,0);
                volume = audioM.getStreamVolume(AudioManager.STREAM_RING);
                progressBar.setProgress(volume);
                int mode = audioM.getRingerMode();
                if(mode==AudioManager.RINGER_MODE_NORMAL){
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.normal));
                }else if(mode==AudioManager.RINGER_MODE_SILENT){
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.silence));
                }else if(mode==AudioManager.RINGER_MODE_VIBRATE){
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.vibrate));
                }else if(mode==AudioManager.VIBRATE_TYPE_RINGER){
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ringandvibrate));
                }
            }
        });

        ImageButton normalButton = (ImageButton)view.findViewById(R.id.normalButton);
        ImageButton silenceButton = (ImageButton)view.findViewById(R.id.silenceButton);
        ImageButton vibrateButton = (ImageButton)view.findViewById(R.id.vibrateButton);
        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                volume=audioM.getStreamVolume(AudioManager.STREAM_RING);
                progressBar.setProgress(volume);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.normal));
            }
        });
        silenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioM.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                volume = audioM.getStreamVolume(AudioManager.STREAM_RING);
                progressBar.setProgress(volume);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.silence));
            }
        });
        vibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioM.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                volume = audioM.getStreamVolume(AudioManager.STREAM_RING);
                progressBar.setProgress(volume);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.vibrate));
            }
        });
        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        final AudioManager audioM = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        int mode = audioM.getRingerMode();
        imageView = (ImageView)this.getView().findViewById(R.id.profile);
        if(mode==AudioManager.RINGER_MODE_NORMAL){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.normal));
        }else if(mode==AudioManager.RINGER_MODE_SILENT){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.silence));
        }else if(mode==AudioManager.RINGER_MODE_VIBRATE){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.vibrate));
        }
    }
}
