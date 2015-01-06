package com.wang.tim.newlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends Activity {
    private static final String TAG="NewsActivity";
    private List<News> list=new ArrayList<News>();
    private NewsAdapter newsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        this.initData();
        newsAdapter = new NewsAdapter(this,R.layout.news_title,list);
        ListView listView = (ListView)findViewById(R.id.list_id);
        listView.setAdapter(newsAdapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View fragmentView = findViewById(R.id.frameLayout);
                News news = list.get(position);
                if (fragmentView == null) {//小屏幕
                    Log.e(TAG,"small screen event");
                    Intent intent = new Intent(parent.getContext(), NewsContextActivity.class);
                    intent.putExtra("title", news.getTitle());
                    intent.putExtra("context", news.getContext());
                    startActivity(intent);
                } else {
                    Log.e(TAG,"bit screen event");
                    TextView titleView = (TextView) fragmentView.findViewById(R.id.contextTitle);
                    TextView contextView = (TextView) fragmentView.findViewById(R.id.newsContext);
                    titleView.setText(news.getTitle());
                    contextView.setText(news.getContext());
                }
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
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

    public void initData(){
        News news1 = new News("title1","context1context1context1context1context1context1");
        list.add(news1);
        News news2 = new News("title2","context2context2context2context2context2context2context2");
        list.add(news2);
        News news3 = new News("title3","context3context3context3context3context3context3");
        list.add(news3);
    }
}
