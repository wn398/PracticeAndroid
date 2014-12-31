package com.wang.tim.simplemusicplayer;

import android.app.Application;

/**
 * Created by twang on 2014/12/31.
 */
public class MusicPlayerApplication extends Application {
    private MusicInfoControl musicInfoControl = null;
    @Override
    public void onCreate() {
        super.onCreate();
        musicInfoControl = MusicInfoControl.getInstance(this);
    }

    public MusicInfoControl getMusicInfoControl() {
        return musicInfoControl;
    }
}
