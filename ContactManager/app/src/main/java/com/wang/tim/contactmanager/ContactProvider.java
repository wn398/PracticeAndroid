package com.wang.tim.contactmanager;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by twang on 2014/12/25.
 */
public class ContactProvider extends ContentProvider {
    public static final String TAG = "ContactProvider";

    private DBHelper dbHelper;
    private SQLiteDatabase contactsDB;

    public static final String AUTHRITY = "com.wang.tim.provider.ContactProvider";
    public static final String CONTACT_TABLE = "contacts";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHRITY+"/"+CONTACT_TABLE);

    //下面是自定义类型
    public static final int CONTACTS = 1;
    public static final int CONTACT_ID = 2;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHRITY, "contacts", CONTACTS);
        //单独列
        uriMatcher.addURI(AUTHRITY, "contacts/#", CONTACT_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        contactsDB = dbHelper.getWritableDatabase();
        return (contactsDB==null)?false:true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)){
            case CONTACTS:
                count = contactsDB.delete(CONTACT_TABLE,selection,selectionArgs);
                break;
            case CONTACT_ID:
                String contactID = uri.getPathSegments().get(1);
                count = contactsDB.delete(CONTACT_TABLE,ContactColumnInfo._ID+"="+contactID+(!TextUtils.isEmpty(selection)?" AND ("+selection+")" : ""),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:"+uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
    //URI类型转换
    @Override
    public String getType(Uri uri){
        switch (uriMatcher.match(uri)){
            case CONTACTS://多行数据
                return "vnd.android.cursor.dir/vnd.wang.tim.contactmanager";
            case CONTACT_ID://单行数据
                return "vnd.android.cursor.item/vnd.wang.tim.contactmanager";
            default:
                throw new IllegalArgumentException("Unsupported URI: "+uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (uriMatcher.match(uri) != CONTACTS)
        {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        ContentValues values;
        if (initialValues != null)
        {
            values = new ContentValues(initialValues);
            Log.e(TAG + "-insert", "initialValues is not null");
        }
        else
        {
            values = new ContentValues();
        }
        // 设置默认值
        if (values.containsKey(ContactColumnInfo.NAME) == false)
        {
            values.put(ContactColumnInfo.NAME, "");
        }
        if (values.containsKey(ContactColumnInfo.MOBILENUM) == false)
        {
            values.put(ContactColumnInfo.MOBILENUM, "");
        }
        if (values.containsKey(ContactColumnInfo.HOMENUM) == false)
        {
            values.put(ContactColumnInfo.HOMENUM, "");
        }
        if (values.containsKey(ContactColumnInfo.ADDRESS) == false)
        {
            values.put(ContactColumnInfo.ADDRESS, "");
        }
        if (values.containsKey(ContactColumnInfo.EMAIL) == false)
        {
            values.put(ContactColumnInfo.EMAIL, "");
        }
        if (values.containsKey(ContactColumnInfo.BLOG) == false)
        {
            values.put(ContactColumnInfo.BLOG, "");
        }
        Log.e(TAG + "-insert", values.toString());
        long rowId = contactsDB.insert(CONTACT_TABLE, null, values);
        if (rowId > 0)
        {
            Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            Log.e(TAG + "-insert", noteUri.toString());
            return noteUri;
        }
        throw new SQLException("Failed to insert row into " + uri);

    }
    //查询
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.e(TAG + ":query","in Query");
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(CONTACT_TABLE);
        switch (uriMatcher.match(uri)){
            case CONTACT_ID:
                qb.appendWhere(ContactColumnInfo._ID + "="+uri.getPathSegments().get(1));
                break;
            default:
                break;
        }

        String orderBy;
        if(TextUtils.isEmpty(sortOrder)){
            orderBy = ContactColumnInfo._ID;
        }else{
            orderBy = sortOrder;
        }
        Cursor c = qb.query(contactsDB,projection,selection,selectionArgs,null,null,orderBy);
        c.setNotificationUri(getContext().getContentResolver(),uri);
        return c;
    }
    //更新数据库

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        Log.e(TAG + "-update",values.toString());
        Log.e(TAG + "-update",uri.toString());
        Log.e(TAG + "update :match", "" + uriMatcher.match(uri));
        switch (uriMatcher.match(uri)){
            case CONTACTS:
                Log.e(TAG + "-update",CONTACTS+"");
                count = contactsDB.update(CONTACT_TABLE,values,selection,selectionArgs);
                break;
            case CONTACT_ID:
                String contactID = uri.getPathSegments().get(1);
                Log.e(TAG+"-update",contactID+"");
                count = contactsDB.update(CONTACT_TABLE,values,ContactColumnInfo._ID+"="+contactID+
                        (!TextUtils.isEmpty(selection)? " AND (" +selection+")":""),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;

    }
}
