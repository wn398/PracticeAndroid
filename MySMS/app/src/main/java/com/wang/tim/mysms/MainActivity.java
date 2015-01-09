package com.wang.tim.mysms;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    BroadcastReceiver receiverSMSReceiver;
    BroadcastReceiver sendSMSReceiver;
    IntentFilter receiverSMSIntentFilter;
    IntentFilter sendSMSIntentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //////////////////////////receiver message
        final TextView from = (TextView)findViewById(R.id.SMSFrom);
        final TextView content = (TextView)findViewById(R.id.SMSContext);

        receiverSMSReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                Object[] pdus = (Object[])bundle.get("pdus");//提取短信消息通过pdu密钥
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for(int i=0;i<messages.length;i++){
                    messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);//将每一个pdu字节数据转换为SmsMessage对象
                }
                String address = messages[0].getOriginatingAddress();//获取发送号码
                String fullMessage = "";
                for(SmsMessage message:messages){
                    fullMessage +=message.getMessageBody();//获取短信内容
                }
                from.setText(address);
                content.setText(fullMessage);
            }
        };

        receiverSMSIntentFilter = new IntentFilter();
        receiverSMSIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //receiverSMSIntentFilter.setPriority(100); by this upper the priority and invoke abortBroadcast() in the method could block the message
        registerReceiver(receiverSMSReceiver, receiverSMSIntentFilter);
//////////////////////////////////////////////////////////////////////send message
        sendSMSReceiver = new BroadcastReceiver() {//check the message whether send successful
            @Override
            public void onReceive(Context context, Intent intent) {
                    if(getResultCode() == RESULT_OK){
                        Toast.makeText(context,"Send successed",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"Send failed",Toast.LENGTH_SHORT).show();
                    }
            }
        };

        Button button = (Button)findViewById(R.id.sendSMS);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText to = (EditText)findViewById(R.id.SMSTo);
                EditText sendContent = (EditText)findViewById(R.id.SMSSendContext);
                String toAddress = to.getText().toString();
                String content = sendContent.getText().toString();
                //注册发送成功广播接收器
                sendSMSIntentFilter = new IntentFilter();
                sendSMSIntentFilter.addAction("SENT_SMS_ACTION");
                registerReceiver(sendSMSReceiver,sendSMSIntentFilter);

                SmsManager smsManager = SmsManager.getDefault();
                Intent sendIntent = new Intent("SENT_SMS_ACTION");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,sendIntent,0);
                smsManager.sendTextMessage(toAddress,null,content,pendingIntent,null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverSMSReceiver);
        unregisterReceiver(sendSMSReceiver);
    }
}
