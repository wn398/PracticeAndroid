package com.wang.tim.animationsample;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;


public class LayoutAnimationActivity extends ActionBarActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private ViewGroup viewGroup;
    private GridLayout gridLayout;
    private LayoutTransition layoutTransition;
    private CheckBox appear,changAppear,disAppear,changeDisAppear;
    private Button addButton;
    private int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_animation);

        viewGroup = (ViewGroup)findViewById(R.id.viewGroup);

        appear = (CheckBox)findViewById(R.id.id_appear);
        changAppear = (CheckBox)findViewById(R.id.id_change_appear);
        disAppear = (CheckBox)findViewById(R.id.id_disappear);
        changeDisAppear = (CheckBox)findViewById(R.id.id_change_disappear);

        appear.setOnCheckedChangeListener(this);
        changAppear.setOnCheckedChangeListener(this);
        disAppear.setOnCheckedChangeListener(this);
        changeDisAppear.setOnCheckedChangeListener(this);

        addButton = (Button)findViewById(R.id.addBtn);
        addButton.setOnClickListener(this);

        //创建gridlayout
        gridLayout = new GridLayout(this);
        //设置第行4个
        gridLayout.setColumnCount(4);
        //添加到布局中
        viewGroup.addView(gridLayout);
        //默认动画全部开启
        layoutTransition = new LayoutTransition();
        gridLayout.setLayoutTransition(layoutTransition);

    }
    //处理添加按钮  当点击添加的按键时就移除它
    @Override
    public void onClick(View v) {
        final Button button = new Button(this);
        button.setText(++num+"");
        gridLayout.addView(button,gridLayout.getChildCount()==0?0:1);//总是在第二个加入
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridLayout.removeView(button);
            }
        });
    }
    //处理当checkbox变化时的影响
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        layoutTransition = new LayoutTransition();
        //layoutTransition.getAnimator(LayoutTransition.APPEARING)这是使用系统定义的出现动画
        layoutTransition.setAnimator(LayoutTransition.APPEARING,appear.isChecked()?layoutTransition.getAnimator(LayoutTransition.APPEARING):null);
        //以下也可以用自定义的动画
        //layoutTransition.setAnimator(LayoutTransition.APPEARING, (appear.isChecked() ? ObjectAnimator.ofFloat(this, "scaleX", 0, 1): null));
        layoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING,changAppear.isChecked()?layoutTransition.getAnimator(LayoutTransition.CHANGE_APPEARING):null);
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING,disAppear.isChecked()?layoutTransition.getAnimator(LayoutTransition.DISAPPEARING):null);
        layoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,changeDisAppear.isChecked()?layoutTransition.getAnimator(LayoutTransition.CHANGE_DISAPPEARING):null);

        gridLayout.setLayoutTransition(layoutTransition);
    }
}
