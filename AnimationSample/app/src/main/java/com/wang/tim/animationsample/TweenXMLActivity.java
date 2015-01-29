package com.wang.tim.animationsample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TweenXMLActivity extends ActionBarActivity implements View.OnClickListener{

    private Button alphaButton;
    private Button scaleButton;
    private Button translateButton;
    private Button rotateButton;
    private Button combinationButton;
    private LinearLayout linearLayout;
    private TextView textView;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tween_code);
        alphaButton = (Button)findViewById(R.id.alpha);
        scaleButton = (Button)findViewById(R.id.scale);
        translateButton = (Button)findViewById(R.id.translate);
        rotateButton = (Button)findViewById(R.id.rotate);
        combinationButton = (Button)findViewById(R.id.combination);

        alphaButton.setOnClickListener(this);
        scaleButton.setOnClickListener(this);
        translateButton.setOnClickListener(this);
        rotateButton.setOnClickListener(this);
        combinationButton.setOnClickListener(this);

        linearLayout = (LinearLayout)findViewById(R.id.group);
        textView = (TextView)findViewById(R.id.text);
        imageView = (ImageView)findViewById(R.id.icon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.alpha:
                toAlpha();
                break;
            case R.id.scale:
                toScale();
                break;
            case R.id.translate:
                toTranslate();
                break;
            case R.id.rotate:
                toRotate();
                break;
            case R.id.combination:
                toCombination();
                break;
            default:
                break;
        }
    }
    //透明度变化
    private void toAlpha(){
        Animation anim= AnimationUtils.loadAnimation(TweenXMLActivity.this, R.anim.anim_alpha);
        linearLayout.startAnimation(anim);
    }
    //缩放变化
    private void toScale(){
        Animation anim=AnimationUtils.loadAnimation(TweenXMLActivity.this, R.anim.anim_scale);
        linearLayout.startAnimation(anim);
    }
    //移动变化
    private void toTranslate(){
        Animation anim=AnimationUtils.loadAnimation(TweenXMLActivity.this, R.anim.anim_translate);
        linearLayout.startAnimation(anim);
    }
    //旋转
    private void toRotate(){
        Animation anim=AnimationUtils.loadAnimation(TweenXMLActivity.this, R.anim.anim_rotate);
        linearLayout.startAnimation(anim);
    }
    //组合
    private void toCombination(){
        Animation anim=AnimationUtils.loadAnimation(TweenXMLActivity.this, R.anim.anim_set);
        linearLayout.startAnimation(anim);
    }



}
