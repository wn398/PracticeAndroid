package com.wang.tim.animationsample;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class PropertyAnimationActivity extends ActionBarActivity implements View.OnClickListener {
    private ImageView testImageView;
    private Button turnButton;//翻转
    private Button sdButton;//缩放淡出
    private Button sd2Button;//缩放淡出2
    private Button downButton;//自由落体
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);
        testImageView = (ImageView)findViewById(R.id.testImage);
        turnButton = (Button)findViewById(R.id.turn);
        turnButton.setOnClickListener(this);
        sdButton = (Button)findViewById(R.id.sd);
        sdButton.setOnClickListener(this);
        sd2Button = (Button)findViewById(R.id.sd2);
        sd2Button.setOnClickListener(this);
        downButton =(Button)findViewById(R.id.down);
        downButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.turn://翻转图像
                ObjectAnimator.ofFloat(testImageView,"rotationX",0.0F,360.0F)//ObjectAnimator是比较简单的，它是ValueAnimator的子类
                        .setDuration(500)
                        .start();
                break;
            case R.id.sd://缩小淡出
                final ObjectAnimator anim = ObjectAnimator.ofFloat(testImageView,"haajaja",1.0F,0.0F)
                        .setDuration(800);//故意给一个未知属性，从而调用自己的UpdateListener,这其实实现了一个动画组
                anim.start();
                //当属性值变化时调用
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float val = (Float)animation.getAnimatedValue();
                        testImageView.setAlpha(val);
                        testImageView.setScaleX(val);
                        testImageView.setScaleY(val);
                    }
                });
                break;
            case R.id.sd2://缩小淡出 用另一种方式，更规范化的
                PropertyValuesHolder phx = PropertyValuesHolder.ofFloat("alpha",1f,0f,1f);
                PropertyValuesHolder phy = PropertyValuesHolder.ofFloat("scaleX",1f,0f,1f);
                PropertyValuesHolder phz = PropertyValuesHolder.ofFloat("scaleY",1f,0f,1f);
                ObjectAnimator.ofPropertyValuesHolder(testImageView,phx,phy,phz).setDuration(1000).start();
                break;
            case R.id.down://自由落体
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                 int screenHeight =dm.heightPixels;
                    ValueAnimator animator = ValueAnimator.ofFloat(screenHeight/2,screenHeight-testImageView.getHeight());
                    animator.setTarget(testImageView);
                    animator.setDuration(1000).start();
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            testImageView.setTranslationY((Float)animation.getAnimatedValue());
                        }
                    });
                break;

        }
    }
}
