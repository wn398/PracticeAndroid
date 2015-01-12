package com.wang.tim.myalarm_service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements Button.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button startServiceBtn;
    private Button stopServiceBtn;
    private Button bindServiceBtn;
    private Button unBindServiceBtn;
    private Button startIntentServicBtn;
    private MyServiceConnection myServiceConnection;
    private Intent alarmIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startServiceBtn = (Button)findViewById(R.id.startServiceBtn);
        stopServiceBtn = (Button)findViewById(R.id.stopServiceBtn);
        bindServiceBtn = (Button)findViewById(R.id.bind_service);
        unBindServiceBtn = (Button) findViewById(R.id.unBind_service);
        startIntentServicBtn = (Button) findViewById(R.id.start_IntentService);
        startServiceBtn.setOnClickListener(this);
        stopServiceBtn.setOnClickListener(this);
        bindServiceBtn.setOnClickListener(this);
        unBindServiceBtn.setOnClickListener(this);
        startIntentServicBtn.setOnClickListener(this);
        myServiceConnection = new MyServiceConnection();
        //程序启动时，就开启alarm的service
        alarmIntent = new Intent(this,AlarmService.class);
        startService(alarmIntent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startServiceBtn:
                //开启service
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);
                break;
            case R.id.stopServiceBtn:
                //停止service
                Intent intent2 = new Intent(MainActivity.this,MyService.class);
                stopService(intent2);
                break;
            case R.id.start_IntentService:
                Log.e(TAG,"Thread id:"+Thread.currentThread().getId());
                //开启IntentService
                Intent intent3 = new Intent(this,MyIntentService.class);
                startService(intent3);
                break;
            case R.id.bind_service:
                //绑定service
                Intent intent4 = new Intent(this,MyService.class);
                bindService(intent4,myServiceConnection,BIND_AUTO_CREATE);
                break;
            case R.id.unBind_service:
                //解绑service
                unbindService(myServiceConnection);
        }
    }

    class MyServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //这里当执行bindService方法是会调用
            MyBinder binder = (MyBinder)service;
            binder.download();
            binder.finish();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //这里当执行unBindService方法时会调用

        }
    }

    @Override
    protected void onDestroy() {
        stopService(alarmIntent);
        super.onDestroy();

    }
}
