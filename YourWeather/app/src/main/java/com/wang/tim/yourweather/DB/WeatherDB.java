package com.wang.tim.yourweather.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wang.tim.yourweather.model.City;
import com.wang.tim.yourweather.model.County;
import com.wang.tim.yourweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twang on 2015/1/19.
 */
public class WeatherDB {
    private static final String TAG = "WeatherDB";
    private static final String DB_name = "weather.db";
    private static final int version = 2;
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
        return weatherDB;
    }

    public void saveProvince(Province province){
        Log.d(TAG,"saveProvince()");
        ContentValues values = new ContentValues();
        values.put("id",province.getId());
        values.put("name",province.getName());
        db.insert(DBhelper.province_table_name,null,values);
    }
    public void saveCity(City city) {
        Log.d(TAG,"saveCity()");
        ContentValues values = new ContentValues();
        values.put("id",city.getId());
        values.put("name",city.getName());
        values.put("province_id",city.getProvince_id());
        db.insert(DBhelper.city_table_name,null,values);
    }
    public void saveCountry(County country) {
        Log.d(TAG,"saveCountry()");
        ContentValues values = new ContentValues();
        values.put("id", country.getId());
        values.put("name",country.getName());
        values.put("country_code",country.getCountry_code());
        values.put("city_id",country.getCity_id());
        db.insert(DBhelper.country_table_name,null,values);
    }
    public Cursor loadAllProvinceCursor(){
        Log.d(TAG,"loadAllProvinceCursor()");
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query(DBhelper.province_table_name,null,null,null,null,null,null);
        return cursor;
    }
    public List<Province> loadAllProvince() {
        Log.d(TAG,"loadAllProvince()");
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query(DBhelper.province_table_name,null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setName(cursor.getString(cursor.getColumnIndex("name")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        Log.d(TAG,"loadAllProvince()--"+list.size());
        return list;
    }
    public Cursor loadTheProvinceCityCursor(int province_id){
        Log.d(TAG,"loadTheProvinceCityCursor()");
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query(DBhelper.city_table_name,null,"province_id=?",new String[]{province_id+""},null,null,null);
        return cursor;
    }
    public List<City> loadTheProvinceCity(int province_id) {
        Log.d(TAG,"loadTheProvinceCity()");
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query(DBhelper.city_table_name,null,"province_id=?",new String[]{province_id+""},null,null,null);
        if(cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setName(cursor.getString(cursor.getColumnIndex("name")));
                city.setProvince_id(cursor.getInt(cursor.getColumnIndex("province_id")));
                list.add(city);
            } while (cursor.moveToNext());
        }
        return list;
    }
    public Cursor loadTheCityCountyrCursor(int city_id){
        Log.d(TAG,"loadTheCityCountry()");
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query(DBhelper.country_table_name,null,"city_id=?",new String[]{city_id+""},null,null,null);
        return cursor;
    }
    public List<County> loadTheCityCountry(int city_id) {
        Log.d(TAG,"loadTheCityCountry()");
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query(DBhelper.country_table_name,null,"city_id=?",new String[]{city_id+""},null,null,null);
        if(cursor.moveToFirst()) {
            do {
                County country = new County();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setName(cursor.getString(cursor.getColumnIndex("name")));
                country.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
                country.setCountry_code(cursor.getInt(cursor.getColumnIndex("country_code")));
                list.add(country);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public int getWeatherCodeById(int id){
        Log.d(TAG,"getWeatherCodeById");
        Cursor cursor = db.query(DBhelper.country_table_name,null,"id=?",new String[]{id+""},null,null,null);
        if(cursor.moveToFirst()) {
            if (1 == cursor.getCount()) {
                int weatherCode = cursor.getInt(cursor.getColumnIndex("country_code"));
                return weatherCode;
            } else {
                Log.e(TAG, "Get more coutnry_code from the id:" + id);
            }
        }
        return -1;
    }

    public int getCityCodeByName(String name){
        Log.d(TAG,"getCityCodeByName()");
        Cursor cursor =db.query(DBhelper.country_table_name,null,"name=?",new String[]{name},null,null,null);
        if(cursor.moveToFirst()){
            if(1==cursor.getCount()){
                int cityCode = cursor.getInt(cursor.getColumnIndex("id"));
                return cityCode;
            }
        }
        return -1;
    }
}
