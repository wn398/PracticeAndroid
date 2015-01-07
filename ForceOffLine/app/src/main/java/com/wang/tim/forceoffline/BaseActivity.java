package com.wang.tim.forceoffline;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twang on 2015/1/7.
 */
public class BaseActivity extends Activity {
    public static List<Activity> activities = new ArrayList<Activity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
    }


    public static void finishAll(){
        for(Activity activity:activities){
           activity.finish();
        }
    }
}
