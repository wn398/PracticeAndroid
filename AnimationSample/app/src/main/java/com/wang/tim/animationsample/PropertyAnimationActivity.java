package com.wang.tim.animationsample;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class PropertyAnimationActivity extends ActionBarActivity implements View.OnClickListener {
    private RelativeLayout relativeLayout;
    private ImageView testImageView;
    private Button turnButton;//翻转
    private Button sdButton;//缩放淡出
    private Button sd2Button;//缩放淡出2
    private Button downButton;//自由落体
    private Button pwxButton;//抛物线
    private Button moreD;//多动画同时执行
    private Button moreD2;//多动画按次序
    private Button scale2;//以左上角为中心缩放

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        testImageView = (ImageView)findViewById(R.id.testImage);
        turnButton = (Button)findViewById(R.id.turn);
        turnButton.setOnClickListener(this);
        sdButton = (Button)findViewById(R.id.sd);
        sdButton.setOnClickListener(this);
        sd2Button = (Button)findViewById(R.id.sd2);
        sd2Button.setOnClickListener(this);
        downButton =(Button)findViewById(R.id.down);
        downButton.setOnClickListener(this);
        pwxButton = (Button)findViewById(R.id.pwx);
        pwxButton.setOnClickListener(this);
        moreD = (Button)findViewById(R.id.moreD);
        moreD.setOnClickListener(this);
        moreD2 = (Button)findViewById(R.id.moreD2);
        moreD2.setOnClickListener(this);
        scale2 = (Button)findViewById(R.id.scale2);
        scale2.setOnClickListener(this);
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
            case R.id.down://自由落体  ValueAnimator使用
                   ValueAnimator animator = ValueAnimator.ofFloat(0.0f,relativeLayout.getHeight()/2-testImageView.getHeight());
                    animator.setTarget(testImageView);
                    animator.setDuration(1000).start();
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            testImageView.setTranslationY((Float) animation.getAnimatedValue());
                        }
                    });
                break;
            case R.id.pwx://抛物线  ValueAnimator及 TypeEvaluator使用
                ValueAnimator valueAnimator = new ValueAnimator();
                valueAnimator.setDuration(1000);
                valueAnimator.setObjectValues(new PointF(0,0));//设置要变更的属性为PintF对象
                valueAnimator.setInterpolator(new LinearInterpolator());//线性加速度
                valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {//由于不是整形，浮点型，所以自定义Evaluator
                    @Override
                    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                        //x方向100px/2  y方向0.5*10*t
                        PointF pointF = new PointF();
                        pointF.x = 100*fraction*3;
                        pointF.y = 0.5f*100*(fraction*3)*(fraction*3);
                        return pointF;
                    }
                });

                valueAnimator.start();
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PointF pointF = (PointF)animation.getAnimatedValue();
                        testImageView.setX(pointF.x);
                        testImageView.setY(pointF.y);
                    }
                });
                break;
            case R.id.moreD://多动画同时执行
                 ObjectAnimator anim1 = ObjectAnimator.ofFloat(testImageView,"scaleX",1.0f,2f);
                 ObjectAnimator anim2 = ObjectAnimator.ofFloat(testImageView,"scaleY",1.0f,2f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(1000);
                animatorSet.setInterpolator(new LinearInterpolator());
                //两动画同时执行
                animatorSet.playTogether(anim1,anim2);
                animatorSet.start();
                break;
            case R.id.moreD2://多动画，按次序执行
                float cx = testImageView.getX();
                ObjectAnimator anim21 = ObjectAnimator.ofFloat(testImageView,"scaleX",1.0f,2f);
                ObjectAnimator anim22 = ObjectAnimator.ofFloat(testImageView,"scaleY",1.0f,2f);
                ObjectAnimator anim23 = ObjectAnimator.ofFloat(testImageView,"x",  cx ,  0f);//移动左边界
                ObjectAnimator anim24 = ObjectAnimator.ofFloat(testImageView,"x", cx);//移到原来位置
                //1,2,3动画同时执行，4接着执行
                AnimatorSet animatorSet1 = new AnimatorSet();
                animatorSet1.play(anim21).with(anim22);
                animatorSet1.play(anim22).with(anim23);
                animatorSet1.play(anim24).after(anim23);
                animatorSet1.setDuration(1000).start();
                break;
            case R.id.scale2://向左上角缩小  使用xml动画
                Animator animator1 = AnimatorInflater.loadAnimator(this,R.animator.scale_left_up);
                testImageView.setPivotX(0);
                testImageView.setPivotY(0);
                //显示调用invalidate
                testImageView.invalidate();
                animator1.setTarget(testImageView);
                animator1.start();
                break;
        }
    }
}
