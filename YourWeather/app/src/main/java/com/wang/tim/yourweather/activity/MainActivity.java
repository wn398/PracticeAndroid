package com.wang.tim.yourweather.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
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
import com.wang.tim.yourweather.util.DBinitUtil;
import com.wang.tim.yourweather.util.NetWorkUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    private static final int UPDATE_VIEW = 1;
    private ImageButton refresh;
    private ImageButton setting;
    private ListView weatherList;
    private Handler handler;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private List<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor=preferences.edit();
        if(!preferences.getBoolean("DB",false)){
            DBinitUtil.initDB(WeatherDB.getInstance(MainActivity.this),getResources().openRawResource(R.raw.citycode));
            editor.putBoolean("DB",true);
        }

        refresh = (ImageButton)findViewById(R.id.refresh);
        setting = (ImageButton)findViewById(R.id.setting);
        weatherList = (ListView)findViewById(R.id.weatherList);
        final MyWeatherInfoAdapter myWeatherInfoAdapter = new MyWeatherInfoAdapter(MainActivity.this,list);
        weatherList.setAdapter(myWeatherInfoAdapter);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case UPDATE_VIEW:
                        myWeatherInfoAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };
        setting.setOnClickListener(new View.OnClickListener() {
            int province_id;
            int city_id;
            int country_id;
            int weather_code;
            @Override

            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(v.getContext());
                builder.setTitle("设置区域");
                final LayoutInflater lif = MainActivity.this.getLayoutInflater();
                View view =lif.inflate(R.layout.choose_area,null);
                builder.setView(view);
                Spinner province = (Spinner)view.findViewById(R.id.province);
                final Spinner city = (Spinner)view.findViewById(R.id.city);
                final Spinner country = (Spinner)view.findViewById(R.id.country);

                MyPlaceAdapter prinvinceAdapter = new MyPlaceAdapter(MainActivity.this,WeatherDB.getInstance(MainActivity.this).loadAllProvince());
//                if(preferences.getInt("province_id",1)!=1){
//                    province.setSelection(prinvinceAdapter.getItemId());
//                }
                province.setAdapter(prinvinceAdapter);
                province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView idView = (TextView)view.findViewById(R.id.id);
                        int selected_province_id = Integer.parseInt(idView.getText().toString());
                        city.setAdapter(new MyPlaceAdapter(MainActivity.this,WeatherDB.getInstance(MainActivity.this).loadTheProvinceCity(selected_province_id)));
                        province_id = selected_province_id;
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
                        country.setAdapter(new MyPlaceAdapter(MainActivity.this, WeatherDB.getInstance(MainActivity.this).loadTheCityCountry(selected_city_id)));
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
                        weather_code = WeatherDB.getInstance(MainActivity.this).getWeatherCodeById(selected_country_id);
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
                                list.add(result);
                                Message message = new Message();
                                message.what = UPDATE_VIEW;
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
    }
}
