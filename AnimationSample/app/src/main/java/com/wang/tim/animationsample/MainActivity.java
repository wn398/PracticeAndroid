package com.wang.tim.animationsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
    private Button toCodeButton;
    private Button toXMLButton;
    private Button frameAnimationButton;
    private Button propertyAnimationButton;
    private Button layoutAnimationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toCodeButton = (Button)findViewById(R.id.toCodeTween);
        toXMLButton = (Button)findViewById(R.id.toXMLTween);
        frameAnimationButton = (Button)findViewById(R.id.frameAnimation);
        propertyAnimationButton = (Button)findViewById(R.id.propertyAnimation);
        layoutAnimationButton = (Button)findViewById(R.id.layoutAnimation);
        toCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TweenCodeActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        toXMLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TweenXMLActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        frameAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FrameAnimationActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        propertyAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PropertyAnimationActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        layoutAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LayoutAnimationActivity.class);
                startActivity(intent);
            }
        });
    }



}
