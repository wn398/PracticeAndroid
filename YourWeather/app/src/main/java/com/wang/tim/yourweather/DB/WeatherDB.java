package com.wang.tim.yourweather.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wang.tim.yourweather.model.City;
import com.wang.tim.yourweather.model.Country;
import com.wang.tim.yourweather.model.Province;

/**
 * Created by twang on 2015/1/19.
 */
public class WeatherDB {

    private static final String DB_name = "weather.db";
    private static final int version = 1;
    private static WeatherDB weatherDB;
    private static SQLiteDatabase db;
    private WeatherDB(Context context) {
        DBhelper dBhelper = new DBhelper(context, DB_name, null, version);
        db = dBhelper.getWritableDatabase();
    }

    public synchronized static WeatherDB getInstance(Context context){
           if(null == weatherDB){
               weatherDB = new WeatherDB(context);
           }else{
               return weatherDB;
           }
        return null;
    }

    public void saveProvince(Province province){
        ContentValues values = new ContentValues();
        values.put("id",province.getId());
        values.put("name",province.getName());
        db.insert(DBhelper.province_table_name,null,values);
    }
    public void saveCity(City city) {
        ContentValues values = new ContentValues();
        values.put("id",city.getId());
        values.put("name",city.getName());
        values.put("city_code",city.getCity_code());
        values.put("province_id",city.getProvince_id());
        db.insert(DBhelper.city_table_name,null,values);
    }
    public void saveCountry(Country country) {
        ContentValues values = new ContentValues();
        values.put("id",country.getId());
        values.put("name",country.getName());
        values.put("country_code",country.getCountry_code());
        values.put("city_id",country.getCity_id());
    }



}
