package com.wang.tim.widgetsample;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.widget.RemoteViews;

/**
 * Created by twang on 2015/1/22.
 */
public class MyWidget extends AppWidgetProvider {
    private String[] months = {"一月", "二月", "三月", "四月",
            "五月", "六月", "七月", "八月",
            "九月", "十月", "十一月", "十二月"};
    private String[] days = {"星期日", "星期一", "星期二", "星期三",
            "星期四", "星期五", "星期六"};
    //当onReceive方法没有被重载时，onUpdate方法会接收到广播信息
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = buildView(context);
        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews buildView(Context context){
        RemoteViews remoteViews = null;
        Time time = new Time();
        time.setToNow();
        String month = months[time.month]+ " "+ time.year;
        remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.month,month);
        remoteViews.setTextViewText(R.id.day,new Integer(time.monthDay).toString());
        remoteViews.setTextViewText(R.id.weekDay,days[time.weekDay]);

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity"));
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        remoteViews.setOnClickPendingIntent(R.id.Base,pendingIntent);
        return remoteViews;

    }
}
