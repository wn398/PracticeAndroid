package com.wang.tim.animationsample;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class FrameAnimationActivity extends ActionBarActivity implements View.OnClickListener {
    private Button beginButton;
    private Button endButton;
    private ImageView imageView;
    private AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animation);

        beginButton = (Button)findViewById(R.id.begin);
        endButton = (Button)findViewById(R.id.end);
        imageView = (ImageView)findViewById(R.id.imageView);
         //通过在代码中或在配置文件中设置都可以
       // imageView.setBackgroundResource(R.drawable.frame_animation);
        beginButton.setOnClickListener(this);
        endButton.setOnClickListener(this);
        animationDrawable = (AnimationDrawable)imageView.getBackground();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.begin:
                animationDrawable.start();
                break;
            case R.id.end:
                animationDrawable.stop();
                break;
            default:
                break;
        }
    }
}
