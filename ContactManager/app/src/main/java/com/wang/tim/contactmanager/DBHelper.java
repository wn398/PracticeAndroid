package com.wang.tim.contactmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by twang on 2014/12/24.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASENAME = "contacts.db";//database name
    public static final int DATABASE_VERSION = 2;//version
    public static final String CONTACTS_TABLE = "contacts";//table name

    //create table
    public static final String DATABASE_CREATE = "CREATE TABLE" + CONTACTS_TABLE + "("
            +ContactColumnInfo._ID + " integer primarey key autoincrement,"
            +ContactColumnInfo.NAME + " text,"
            +ContactColumnInfo.MOBILENUM + " text,"
            +ContactColumnInfo.HOMENUM + " text,"
            +ContactColumnInfo.ADDRESS + " text,"
            +ContactColumnInfo.EMAIL + " text,"
            +ContactColumnInfo.BLOG + " text);";

    public DBHelper(Context context) {
        super(context, DATABASENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE);
        onCreate(db);
    }
}
