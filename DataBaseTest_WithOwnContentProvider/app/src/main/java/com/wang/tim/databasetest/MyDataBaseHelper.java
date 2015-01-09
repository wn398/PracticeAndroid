package com.wang.tim.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by twang on 2015/1/8.
 */
public class MyDataBaseHelper extends SQLiteOpenHelper {
    public static String create_table = "create table book("+
            "_id Integer primary key autoincrement,"
            +"name text,"
            +"pages Integer,"
            +"price real)";

    public MyDataBaseHelper(Context context,String databaseName,SQLiteDatabase.CursorFactory factory,int version){
        super(context,databaseName,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table book");
        db.execSQL(create_table);
    }


}
