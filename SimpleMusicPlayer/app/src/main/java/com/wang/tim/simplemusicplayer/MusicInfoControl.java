package com.wang.tim.simplemusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by twang on 2014/12/31.
 */
public class MusicInfoControl {
    private static MusicInfoControl instance = null;
    private MusicPlayerApplication musicPlayerApplication = null;

    //single instance
    public static MusicInfoControl getInstance(MusicPlayerApplication musicPlayerApplication){
        if(instance == null){
            instance = new MusicInfoControl(musicPlayerApplication);
        }
        return instance;
    }

    private MusicInfoControl(MusicPlayerApplication app){
        musicPlayerApplication = app;
    }

    public MusicPlayerApplication getMusicPlayerApplication(){
        return musicPlayerApplication;
    }

    private Cursor query(Uri uri,String[] results,String cause,String[] causeArgs,String order){
        ContentResolver resolver = musicPlayerApplication.getContentResolver();
        if (resolver == null) {
            return null;
        }
        return resolver.query(uri, results, cause, causeArgs, order);
    }

    public Cursor getAllSong() {
        return query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }
}
