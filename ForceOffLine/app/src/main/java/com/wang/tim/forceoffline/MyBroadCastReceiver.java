package com.wang.tim.forceoffline;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by twang on 2015/1/7.
 */
public class MyBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadCastReceiver";
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e(TAG,context.getPackageName());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("WARNING");
        builder.setMessage("You will be force off line");
        builder.setCancelable(false);
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BaseActivity.finishAll();
                Intent intent = new Intent(context,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        AlertDialog alertDialog = builder.create();
        //make sure it could show
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        //Note:::::::::here not use builder.show(); This will result in the program crash.
        alertDialog.show();

    }
}

