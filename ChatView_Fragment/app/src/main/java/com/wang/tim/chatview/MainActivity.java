package com.wang.tim.chatview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    public List<Message> list = new ArrayList<Message>();
    private ListView listView;
    private MessageListAdapter adapter;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.messageListView);
        if(list.size()==0) {
            init();
        }
        adapter = new MessageListAdapter(this,R.layout.message_view,list);
        listView.setAdapter(adapter);

        editText = (EditText)findViewById(R.id.messageEdit);
        Button button = (Button)findViewById(R.id.sendBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if(message.trim()!=""){
                    list.add(new Message(message,Message.SEND));
                    adapter.notifyDataSetChanged();
                    listView.setSelection(list.size());
                    //listView.se
                    editText.setText("");
                }
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

    public void init(){
        Message message1 = new Message("hello", Message.RECEIVE);
        list.add(message1);
        Message message2 = new Message("world",Message.SEND);
        list.add(message2);
        Message message3 = new Message("I'm han mie mie,what's your name?",Message.RECEIVE);
        list.add(message3);
    }
}
