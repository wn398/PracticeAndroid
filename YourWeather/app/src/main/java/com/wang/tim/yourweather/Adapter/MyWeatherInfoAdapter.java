package com.wang.tim.yourweather.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.tim.yourweather.R;
import com.wang.tim.yourweather.util.NetWorkUtil;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by twang on 2015/1/20.
 */
public class MyWeatherInfoAdapter extends BaseAdapter {
    private static final String TAG ="MyWeatherInfoAdapter";
    private static final int SHOW_PIC = 1;
    private List<String> jsonList;
    private Context context;

    public MyWeatherInfoAdapter(Context context,List jsonList){
        this.context = context;
        this.jsonList = jsonList;
    }

    @Override
    public int getCount() {
        return jsonList.size();
    }

    @Override
    public Object getItem(int position) {
        return jsonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View myView = layoutInflater.inflate(R.layout.weather_info, null);
        if(jsonList.size()>0) {
            TextView place = (TextView) myView.findViewById(R.id.place);
            TextView time = (TextView) myView.findViewById(R.id.time);
            TextView temperature = (TextView) myView.findViewById(R.id.temperature);
            TextView description = (TextView) myView.findViewById(R.id.description);
            final ImageView pic = (ImageView) myView.findViewById(R.id.pic);
            try {
                JSONObject jsonObject = new JSONObject(jsonList.get(position));
                JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
                String city = weatherInfo.getString("city");
                String temp1 = weatherInfo.getString("temp1");
                String temp2 = weatherInfo.getString("temp2");
                String weather = weatherInfo.getString("weather");
                final String img1 = weatherInfo.getString("img1");//.replaceFirst("[n,d]", "b");
                final String img2 = weatherInfo.getString("img2");//.replaceFirst("[n,d]", "b");

                String pTime = weatherInfo.getString("ptime");

                place.setText(city);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(new Date());
                time.setText(dateStr + ":" + pTime);
                temperature.setText(temp1 + "~" + temp2);
                description.setText(weather);
                File file = new File(context.getCacheDir() + "/" + img1);
                if (file.exists()) {
                    pic.setImageBitmap(BitmapFactory.decodeFile(context.getCacheDir() + "/" + img1));
                } else {
                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case SHOW_PIC:
                                    pic.setImageBitmap(BitmapFactory.decodeFile(context.getCacheDir() + "/" + img1));
                            }
                        }
                    };
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File catchedDir = context.getCacheDir();
                            File newFile = new File(catchedDir, img1);
                            NetWorkUtil.getFileFromNet("http://m.weather.com.cn/img/" + img1, newFile);
                            Message message = new Message();
                            message.what = SHOW_PIC;
                            handler.sendMessage(message);
                        }
                    }).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Log.d(TAG,"list is null");
        }
        return myView;
    }
}
