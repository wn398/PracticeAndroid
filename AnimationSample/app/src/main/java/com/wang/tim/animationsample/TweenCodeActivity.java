package com.wang.tim.animationsample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TweenCodeActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG = "TweenCodeActivity";
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
        // 动画从透明变为不透明
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.5f);
        // 动画单次播放时长为2秒
        anim.setDuration(2000);
        // 动画播放次数
        anim.setRepeatCount(2);
        // 动画播放模式为REVERSE
        anim.setRepeatMode(Animation.REVERSE);
        // 设定动画播放结束后保持播放之后的效果
        anim.setFillAfter(true);
        //imageView控件和textView控件
        imageView.startAnimation(anim);
        textView.startAnimation(anim);
    }
    //缩放动画
    private void toScale(){
        // 以图片的中心位置，从原图的20%开始放大到原图的2倍
        ScaleAnimation anim = new ScaleAnimation(0.2f, 2.0f, 0.2f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setDuration(2000);
        anim.setRepeatCount(2);
        anim.setRepeatMode(Animation.REVERSE);
        imageView.startAnimation(anim);
        textView.startAnimation(anim);
    }
    //移动变化
    private void toTranslate(){
        // 从父窗口的（0.1,0.1）的位置移动父窗口X轴20%Y轴20%的距离
        TranslateAnimation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.1f,
                Animation.RELATIVE_TO_PARENT, 0.2f,
                Animation.RELATIVE_TO_PARENT, 0.1f,
                Animation.RELATIVE_TO_PARENT, 0.2f);
        anim.setDuration(2000);
        anim.setRepeatCount(2);
        anim.setRepeatMode(Animation.REVERSE);
        imageView.startAnimation(anim);
        textView.startAnimation(anim);
    }
    //旋转变化
    private void toRotate(){
        // 依照图片的中心，从0°旋转到360°
        RotateAnimation anim = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setDuration(2000);
        anim.setRepeatCount(2);
        anim.setRepeatMode(Animation.REVERSE);
        imageView.startAnimation(anim);
        textView.startAnimation(anim);
        //为动画添加监听
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e(TAG, "动画开始前");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e(TAG,"动画结束");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.e(TAG,"动画重复时");
            }
        });
    }
    //组合动画
    private void toCombination(){
        AnimationSet animSet = new AnimationSet(false);
        // 依照图片的中心，从0°旋转到360°
        RotateAnimation ra = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setDuration(2000);
        ra.setRepeatCount(2);
        ra.setRepeatMode(Animation.REVERSE);

        // 以图片的中心位置，从原图的20%开始放大到原图的2倍
        ScaleAnimation sa = new ScaleAnimation(0.2f, 2.0f, 0.2f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        sa.setDuration(2000);
        sa.setRepeatCount(2);
        sa.setRepeatMode(Animation.REVERSE);

        // 动画从透明变为不透明
        AlphaAnimation aa = new AlphaAnimation(1.0f, 0.5f);
        // 动画单次播放时长为2秒
        aa.setDuration(2000);
        // 动画播放次数
        aa.setRepeatCount(2);
        // 动画播放模式为REVERSE
        aa.setRepeatMode(Animation.REVERSE);
        // 设定动画播放结束后保持播放之后的效果
        aa.setFillAfter(true);

        animSet.addAnimation(sa);
        animSet.addAnimation(aa);
        animSet.addAnimation(ra);
        imageView.startAnimation(animSet);
        textView.startAnimation(animSet);

    }

}
