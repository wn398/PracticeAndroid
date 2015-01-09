package com.wang.tim.mynotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.sendNotificationBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                //this intent define the behave when custom click the detail notification
                Intent intent = new Intent(v.getContext(),NotificationActivity.class);
                intent.setAction("com.wang.tim.mynotification.NotificationActivity");
                PendingIntent pendingIntent = PendingIntent.getActivity(v.getContext(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                Notification notification = new Notification(R.drawable.notice,"This is ticket test",System.currentTimeMillis());
                notification.setLatestEventInfo(v.getContext(),"This is content title","this is content text",pendingIntent);

//                Notification.Builder builder = new Notification.Builder(v.getContext());
//                builder.addAction(R.drawable.ic_action_phone,"phone call come",pendingIntent);
//                //create the sound when notification appear
//               // builder.setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Basic_tone.ogg")));
//                builder.setVibrate(new long[]{0l, 1000l, 1000l,1000l});
//                builder.setLights(Color.GREEN, 1000, 1000);
//                builder.setContentText("This is content text");
//                builder.setContentTitle("This is content title");
//                builder.setWhen(System.currentTimeMillis());
//                builder.setTicker("this is ticker");
//                Notification notification = builder.build();

               // builder.setDefaults(Notification.DEFAULT_ALL);
                notificationManager.notify(1,notification);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
