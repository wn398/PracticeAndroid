package com.wang.tim.simplemusicplayer;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MusicPlayerActivity extends ListActivity {
    private final String TAG = "MusicPlayerActivity";
    private MusicPlayService musicPlayService = null;
    private MusicInfoControl musicInfoControl = null;
    private Cursor cursor = null;

    private TextView textView = null;
    private Button playPauseBtn = null;
    private Button stopButton = null;

    private ServiceConnection playBackConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicPlayService = ((MusicPlayService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicPlayService = null;
        }
    };

    protected BroadcastReceiver playEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MusicPlayService.PLAYER_PREPARE_END)){
                //begin to play
                textView.setVisibility(View.INVISIBLE);
                playPauseBtn.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                playPauseBtn.setText(R.string.pause);
            }else if(action.equals(MusicPlayService.PLAY_COMPLETED)){
                playPauseBtn.setText(R.string.play);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        final MusicPlayerApplication musicPlayerApplication = (MusicPlayerApplication)getApplication();
        musicInfoControl = musicPlayerApplication.getMusicInfoControl();
        //bind playback service
        startService(new Intent(this,MusicPlayService.class));
        bindService(new Intent(this,MusicPlayService.class),playBackConnection,Context.BIND_AUTO_CREATE);

        textView = (TextView)findViewById(R.id.clickToPlayView);
        playPauseBtn = (Button) findViewById(R.id.playPauseBtn);
        stopButton = (Button) findViewById(R.id.stopBtn);

        playPauseBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicPlayService!=null && musicPlayService.isPlaying()){
                    musicPlayService.pause();
                    playPauseBtn.setText(R.string.play);
                }else if(musicPlayService != null){
                    musicPlayService.start();
                    playPauseBtn.setText(R.string.pause);
                }
            }
        });

        stopButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(musicPlayService!=null){
                    textView.setVisibility(View.VISIBLE);
                    playPauseBtn.setVisibility(View.INVISIBLE);
                    stopButton.setVisibility(View.INVISIBLE);
                    musicPlayService.stop();
                }
            }
        });


        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicPlayService.PLAYER_PREPARE_END);
        filter.addAction(MusicPlayService.PLAY_COMPLETED);
        registerReceiver(playEventReceiver,filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor = musicInfoControl.getAllSong();
        Log.e(TAG," onResume:songs:"+cursor.getCount());
        ListAdapter adapter = new MusicListAdapter(this,android.R.layout.simple_expandable_list_item_2,cursor,new String[]{},new int[]{});
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(cursor == null || cursor.getCount() ==0){
            return;
        }
        cursor.moveToPosition(position);
        String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        musicPlayService.setDataSource(url);
        musicPlayService.start();
    }
}
class MusicListAdapter extends SimpleCursorAdapter{
    private final String TAG = "MusicListAdapter";
    public MusicListAdapter(Context context,int layout,Cursor cursor,String[] from,int[] to){
        super(context,layout,cursor,from,to);
    }

    public void bindView(View view,Context context,Cursor cursor){
        super.bindView(view,context,cursor);
        TextView titleView = (TextView)view.findViewById(android.R.id.text1);
        TextView artistView = (TextView)view.findViewById(android.R.id.text2);

        try {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            Log.e(TAG,title+"="+title);
            Log.e(TAG,artist+"="+artist);
            titleView.setText(title);
            artistView.setText(artist);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String makeTimeString(long milliSecs){
        StringBuffer sb = new StringBuffer();
        long m = milliSecs/(60*1000);
        sb.append(":");
        long s = (milliSecs % (60*1000))/1000;
        sb.append(s<10?"0"+s:s);
        return sb.toString();
    }
}