package com.wang.tim.yourweather.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by twang on 2015/1/19.
 */
public class DBhelper extends SQLiteOpenHelper {
    public static final String province_table_name = "province";
    public static final String city_table_name = "city";
    public static final String country_table_name = "country";

    private static final String create_table_province = "create table "+ province_table_name+"(id Integer primary key,name Text)";
    private static final String create_table_city = "create table "+city_table_name+"(id Integer primary key, name Text, city_code Text, province_id Integer)";
    private static final String create_table_country = "create table "+country_table_name+"(id Integer primary key,name Text,country_code Integer,city_id Integer)";

    public DBhelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table_province);
        db.execSQL(create_table_city);
        db.execSQL(create_table_country);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onCreate(db);
    }
}
