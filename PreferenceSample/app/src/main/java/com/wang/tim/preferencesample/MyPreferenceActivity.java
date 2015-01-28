package com.wang.tim.preferencesample;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by twang on 2015/1/28.
 */
public class MyPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mypreference);

    }
}
