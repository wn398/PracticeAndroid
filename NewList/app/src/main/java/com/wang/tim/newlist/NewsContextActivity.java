package com.wang.tim.newlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by twang on 2015/1/6.
 */
public class NewsContextActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_context);
        String newContent = getIntent().getStringExtra("context");
        String title = getIntent().getStringExtra("title");
        TextView titleView = (TextView)findViewById(R.id.contextTitle);
        TextView contextView = (TextView)findViewById(R.id.newsContext);
        titleView.setText(title);
        contextView.setText(newContent);
    }
}
