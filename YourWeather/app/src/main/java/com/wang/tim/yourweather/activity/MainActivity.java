package com.wang.tim.yourweather.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wang.tim.yourweather.Adapter.MyPlaceAdapter;
import com.wang.tim.yourweather.Adapter.MyWeatherInfoAdapter;
import com.wang.tim.yourweather.DB.WeatherDB;
import com.wang.tim.yourweather.R;
import com.wang.tim.yourweather.service.MyService;
import com.wang.tim.yourweather.util.Consts;
import com.wang.tim.yourweather.util.DBinitUtil;
import com.wang.tim.yourweather.util.NetWorkUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private ImageButton refresh;
    private ImageButton setting;
    private ListView weatherList;
    public static Handler handler;
    private  SharedPreferences preferences;
    private  SharedPreferences.Editor editor;
    public  static Map<Integer,String> map = new HashMap<Integer,String>();
    private List<String> jsonList = new ArrayList<String>();
    private ProgressDialog progressDialog;
    private WeatherDB weatherDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor=preferences.edit();
        weatherDB = WeatherDB.getInstance(MainActivity.this);
        if(weatherDB.loadAllProvince().size()==0) {
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("加载数据中...........");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            DBinitUtil.initDB(weatherDB, getResources().openRawResource(R.raw.citycode));
            progressDialog.dismiss();
        }
        final MyWeatherInfoAdapter myWeatherInfoAdapter = new MyWeatherInfoAdapter(MainActivity.this,jsonList);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case Consts.UPDATE_WEATHER_VIEW_REFRESH:
                        jsonList.clear();
                        jsonList.addAll(map.values());
                        myWeatherInfoAdapter.notifyDataSetChanged();
                        break;
                    case Consts.UPDATE_WEATHER_VIEW_SERVICE:
                        jsonList.clear();
                        jsonList.addAll(map.values());
                        myWeatherInfoAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };
        FileInputStream fileInputStream=null;
        try {
            fileInputStream = openFileInput("data.properties");
            if(fileInputStream!=null){
                Properties properties = new Properties();
                properties.load(fileInputStream);
                for(Object key:properties.keySet()){
                    String key1 = (String)key;
                    map.put(Integer.parseInt(key1),(String)properties.get(key1));
                }
                if(map.size()>0){
                    Message message = new Message();
                    message.what = Consts.UPDATE_WEATHER_VIEW_REFRESH;
                    handler.sendMessage(message);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(fileInputStream!=null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        refresh = (ImageButton)findViewById(R.id.refresh);
        setting = (ImageButton)findViewById(R.id.setting);
        weatherList = (ListView)findViewById(R.id.weatherList);

        weatherList.setAdapter(myWeatherInfoAdapter);

        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("要删除这个区域吗？");
                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String delectedInfo =jsonList.get(position);
                        jsonList.remove(position);
                        int cityId=-1;
                        for(Map.Entry<Integer,String> entry: map.entrySet()){
                            if(entry.getValue().equals(delectedInfo)){
                                cityId = entry.getKey();
                                break;
                            }
                        }
                        map.remove(cityId);
                        myWeatherInfoAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            int weather_code;
            @Override

            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(v.getContext());
                builder.setTitle("设置区域");
                final LayoutInflater lif = MainActivity.this.getLayoutInflater();
                View view =lif.inflate(R.layout.choose_area,null);
                builder.setView(view);
                final Spinner province = (Spinner)view.findViewById(R.id.province);
                final Spinner city = (Spinner)view.findViewById(R.id.city);
                final Spinner country = (Spinner)view.findViewById(R.id.country);

                MyPlaceAdapter provinceAdapter = new MyPlaceAdapter(MainActivity.this,weatherDB.loadAllProvince());

                province.setAdapter(provinceAdapter);
                if(preferences.getInt("province_position",-1)!=-1){
                    province.setSelection(preferences.getInt("province_position", -1));
                }

                province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView idView = (TextView)view.findViewById(R.id.id);
                        int selected_province_id = Integer.parseInt(idView.getText().toString());
                        city.setAdapter(new MyPlaceAdapter(MainActivity.this,weatherDB.loadTheProvinceCity(selected_province_id)));
                        if(preferences.getInt("city_position",-1)!=-1 && city.getSelectedItemPosition() == 0&&province.getSelectedItemPosition()==preferences.getInt("province_position",-1)){
                            if(preferences.getInt("city_position",-1)<city.getCount()) {
                                city.setSelection(preferences.getInt("city_position", -1));
                            }
                        }
                        editor.putInt("province_position",position);
                        editor.apply();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView idView = (TextView) view.findViewById(R.id.id);
                        int selected_city_id = Integer.parseInt(idView.getText().toString());
                        country.setAdapter(new MyPlaceAdapter(MainActivity.this, weatherDB.loadTheCityCountry(selected_city_id)));
                        if(preferences.getInt("county_position",-1)!=-1 && country.getSelectedItemPosition()==0 &&city.getSelectedItemPosition() ==preferences.getInt("city_position",-1)){
                            if(preferences.getInt("county_position",-1)<country.getCount()) {
                                country.setSelection(preferences.getInt("county_position", -1));
                            }
                        }

                        editor.putInt("city_position",position);
                        editor.apply();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView idView = (TextView)view.findViewById(R.id.id);
                        int selected_country_id = Integer.parseInt(idView.getText().toString());
                        weather_code = weatherDB.getWeatherCodeById(selected_country_id);

                        editor.putInt("county_position",position);
                        editor.apply();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = NetWorkUtil.getDataFromNet("http://www.weather.com.cn/data/cityinfo/"+weather_code+".html");
                                map.put(weather_code,result);
                                Message message = new Message();
                                message.what = Consts.UPDATE_WEATHER_VIEW_REFRESH;
                                handler.sendMessage(message);
                            }
                        }).start();
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("更新数据........");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for(int i:map.keySet()){
                            String result = NetWorkUtil.getDataFromNet("http://www.weather.com.cn/data/cityinfo/"+i+".html");
                            map.put(i,result);
                        }
                        Message message = new Message();
                        message.what = Consts.UPDATE_WEATHER_VIEW_REFRESH;
                        handler.sendMessage(message);
                        progressDialog.dismiss();
                    }
                }).start();
            }
        });

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onStop();
        Properties properties = new Properties();
        for(int key:map.keySet()){
            properties.put(key+"",map.get(key));
        }
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream = openFileOutput("data.properties", MODE_PRIVATE);
            properties.store(fileOutputStream,"catch the data");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
