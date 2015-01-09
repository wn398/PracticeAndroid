package com.wang.tim.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by twang on 2015/1/8.
 */
public class MyContentProvider extends ContentProvider {
    private final static int BOOK_TABLE_DIR = 1;
    private final static int BOOK_TABLE_ITEM = 2;

    private static String AUTHOR = "com.wang.tim.provider";
    public static UriMatcher uriMatcher;
    private MyDataBaseHelper myDataBaseHelper;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHOR,"book",BOOK_TABLE_DIR);
        uriMatcher.addURI(AUTHOR,"book/#",BOOK_TABLE_ITEM);
    }
    @Override
    public boolean onCreate() {
        myDataBaseHelper = new MyDataBaseHelper(this.getContext(),"book.db",null,2);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)) {
            case BOOK_TABLE_DIR:
                cursor = db.query("book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_TABLE_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("book", projection, "_id=?", new String[]{bookId}, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_TABLE_DIR:
                return "vnd.android.cursor.dir/vnd."+AUTHOR+".book";
            case BOOK_TABLE_ITEM:
                return "vnd.android.cursor.item/vnd."+AUTHOR+".book";
            default:
                break;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case BOOK_TABLE_DIR:
            case BOOK_TABLE_ITEM:
                long id = db.insert("book",null,values);
                uriReturn = Uri.parse("content://"+AUTHOR+"/book/"+id);
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_TABLE_DIR:
                deletedRows = db.delete("book",selection,selectionArgs);
                break;
            case BOOK_TABLE_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deletedRows = db.delete("book","_id=?",new String[]{bookId});
                break;
            default:
                break;
        }
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_TABLE_DIR:
                updateRows = db.update("book",values,selection,selectionArgs);
                break;
            case BOOK_TABLE_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = db.update("book",values,"_id=?",new String[]{bookId});
                break;
            default:
                break;
        }
        return updateRows;
    }
}
