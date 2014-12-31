package com.wang.tim.simplemusicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

/**
 * Created by twang on 2014/12/31.
 */
public class MusicPlayService extends Service {
    private final IBinder mBinder = new LocalBinder();
    private MediaPlayer mediaPlayer = null;

    public static final String PLAYER_PREPARE_END = "com.wang.tim.musicplayservice.prepared";
    public static final String PLAY_COMPLETED = "com.wang.tim.musicplayservice.playcompleted";

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener(){

        @Override
        public void onCompletion(MediaPlayer mp) {
            broadcastEvent(PLAY_COMPLETED);
        }
    };

    MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener(){
        @Override
        public void onPrepared(MediaPlayer mp) {
            broadcastEvent(PLAYER_PREPARE_END);
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    private void broadcastEvent(String event) {
        Intent intent = new Intent(event);
        sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(onPreparedListener);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
    }

    //内部类
    public class LocalBinder extends Binder {
        public MusicPlayService getService(){
            return MusicPlayService.this;
        }
    }

    public void setDataSource(String path){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        mediaPlayer.start();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public long seek(long target){
        mediaPlayer.seekTo((int)target);
        return target;
    }
}
