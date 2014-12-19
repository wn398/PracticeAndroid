package com.wang.tim.newringrofile;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import java.util.Date;


public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG ="RingToggle";
    private View normalMode;
    private View timeMode;
    private View defineMode;
    private Fragment normalFragment;
    private Fragment timeFragment;
    private Fragment defineFragment;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        normalMode = findViewById(R.id.normalMode);
        timeMode = findViewById(R.id.timeMode);
        defineMode = findViewById(R.id.defineMode);
        normalMode.setOnClickListener(this);
        timeMode.setOnClickListener(this);
        defineMode.setOnClickListener(this);
        fragmentManager = getFragmentManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (v.getId()){
            case R.id.normalMode:{
                v.setBackgroundColor(Color.GREEN);
                if(normalFragment==null) {
                    normalFragment = new NormalFragment();
                    transaction.add(R.id.frameLayout, normalFragment);
                }else{
                    transaction.show(normalFragment);
                    normalFragment.onResume();
                }
            };
                break;
            case R.id.timeMode :{
                v.setBackgroundColor(Color.GREEN);
                if(timeFragment==null) {
                    timeFragment = new TimeFragment();
                    transaction.add(R.id.frameLayout, timeFragment);
                }else{
                    transaction.show(timeFragment);
                    timeFragment.onResume();
                }
            };
                break;
            case R.id.defineMode:{
                v.setBackgroundColor(Color.GREEN);
                if(defineFragment==null){
                    defineFragment = new DefineFragment();
                    transaction.add(R.id.frameLayout,defineFragment);
                }else{
                    transaction.show(defineFragment);
                    defineFragment.onResume();
                }
            };
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){
        if(normalFragment!=null){
            normalMode.setBackgroundColor(Color.GRAY);
            transaction.hide(normalFragment);
        }
        if(timeFragment!=null){
            timeMode.setBackgroundColor(Color.GRAY);
            transaction.hide(timeFragment);
        }
        if(defineFragment!=null){
            defineMode.setBackgroundColor(Color.GRAY);
            transaction.hide(defineFragment);
        }
    }



}
