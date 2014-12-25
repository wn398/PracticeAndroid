package com.wang.tim.contactmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ContactEditor extends Activity {
    public static final String TAG = "ContactEditor";

    private Cursor mCursor;
    private int mState;
    private Uri mUri;
    private EditText nameText;
    private EditText mobileText;
    private EditText homeText;
    private EditText addressText;
    private EditText emailText;
    private EditText blogText;

    private Button button_ok;
    private Button button_cancle;
    public static final int STATE_EDIT = 0;
    public static final int STATE_INSERT = 1;

    public static final int REVERT_ID = Menu.FIRST;
    public static final int DISCARD_ID = Menu.FIRST+1;
    public static final int DELETE_ID = Menu.FIRST + 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String action = intent.getAction();
        Log.e(TAG + ":onCreate,", action);
        //根据action不同进行不同操作
        if(Intent.ACTION_EDIT.equals(action)){
            mState = STATE_EDIT;
            mUri = intent.getData();
        }else if(Intent.ACTION_INSERT.equals(action)){
            mState = STATE_INSERT;
            mUri = getContentResolver().insert(intent.getData(),null);
            if(mUri == null){
                Log.e(TAG + ":onCreate","Failed to insert new Contact into "+getIntent().getData());
                finish();
                return;
            }
            setResult(RESULT_OK,(new Intent()).setAction(mUri.toString()));
        }else{
            Log.e(TAG + ":onCreate","unknown action");
            finish();
            return;
        }
        setContentView(R.layout.activity_contact_editor);
        nameText = (EditText)findViewById(R.id.EditName);
        mobileText = (EditText) findViewById(R.id.EditMobile);
        homeText = (EditText) findViewById(R.id.EditHome);
        addressText = (EditText)findViewById(R.id.EditAddress);
        emailText = (EditText) findViewById(R.id.EditEmail);
        blogText = (EditText) findViewById(R.id.EditBlog);

        button_ok = (Button) findViewById(R.id.Button_OK);
        button_cancle = (Button) findViewById(R.id.Button_Cancle);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                if(name.length() ==0){
                    //如果没有输入东西则不添加记录
                    setResult(RESULT_CANCELED);
                    deleteContact();
                    finish();
                }else {//添加一条数据
                    updateContact();

                }
            }
        });

        button_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mState == STATE_INSERT){
                    //不添加记录
                    setResult(RESULT_CANCELED);
                    deleteContact();
                    finish();
                }else {
                    //恢复到编辑状态
                    backupContact();
                }
            }
        });

        Log.e(TAG+":onCreate",mUri.toString());
        //获得并保存原始联系人信息
        mCursor = managedQuery(mUri, ContactColumnInfo.RESULTS, null, null, null);
        mCursor.moveToFirst();
        Log.e(TAG, "end of onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCursor !=null){
            Log.e(TAG+":onResume","count:"+mCursor.getColumnCount());
            //读取并显示联系人信息
            mCursor.moveToFirst();
            if(mState==STATE_EDIT){
                setTitle(getText(R.string.editor_user));
            }else if(mState == STATE_INSERT){
                setTitle(getText(R.string.add_user));
            }
            String name = mCursor.getString(ContactColumnInfo.NAME_COLUMN);
            String moblie = mCursor.getString(ContactColumnInfo.MOBILNUM_COLUMN);
            String home = mCursor.getString(ContactColumnInfo.HOMENUM_COLUMN);
            String address = mCursor.getString(ContactColumnInfo.ADDRESS_COLUMN);
            String email = mCursor.getString(ContactColumnInfo.EMAIL_COLUMN);
            String blog = mCursor.getString(ContactColumnInfo.BLOG_COLUMN);

            nameText.setText(name);
            mobileText.setText(moblie);
            homeText.setText(home);
            addressText.setText(address);
            emailText.setText(email);
            blogText.setText(blog);
        }else{
            setTitle("error message");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCursor != null){
            String text = nameText.getText().toString();
            if(text.length() == 0){
                Log.e(TAG+":onPause","nameText is null");
                setResult(RESULT_CANCELED);
                deleteContact();
            }else{
                ContentValues values = new ContentValues();
                values.put(ContactColumnInfo.NAME, nameText.getText().toString());
                values.put(ContactColumnInfo.MOBILENUM, mobileText.getText().toString());
                values.put(ContactColumnInfo.HOMENUM, homeText.getText().toString());
                values.put(ContactColumnInfo.ADDRESS, addressText.getText().toString());
                values.put(ContactColumnInfo.EMAIL, emailText.getText().toString());
                values.put(ContactColumnInfo.BLOG, blogText.getText().toString());

                Log.e(TAG + ":onPause", mUri.toString());
                Log.e(TAG + ":onPause", values.toString());
                getContentResolver().update(mUri, values, null, null);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(mState == STATE_EDIT){
            menu.add(0,REVERT_ID,0,R.string.revert).setShortcut('0','r').setIcon(R.drawable.listuser);
            menu.add(0,DELETE_ID,0,R.string.delete_user).setShortcut('0','f').setIcon(R.drawable.remove);
        }else{
            menu.add(0,DISCARD_ID,0,R.string.revert).setShortcut('0','d').setIcon(R.drawable.listuser);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case DELETE_ID:
                deleteContact();
                finish();
                break;
            case DISCARD_ID:
                cancelContact();
                break;
            case REVERT_ID:
                backupContact();
                break;
        }
         return super.onOptionsItemSelected(item);
    }

    //删除联系人信息
    private void deleteContact() {
        if(mCursor !=null)
            mCursor.close();
            mCursor = null;
        getContentResolver().delete(mUri, null, null);
        nameText.setText("");
    }

    //丢弃编辑的信息
    private void cancelContact() {
        if(mCursor !=null){
            deleteContact();
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    //更新变更的信息
    private void updateContact() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
            ContentValues values = new ContentValues();
            values.put(ContactColumnInfo.NAME, nameText.getText().toString());
            values.put(ContactColumnInfo.MOBILENUM, mobileText.getText().toString());
            values.put(ContactColumnInfo.HOMENUM, homeText.getText().toString());
            values.put(ContactColumnInfo.ADDRESS, addressText.getText().toString());
            values.put(ContactColumnInfo.EMAIL, emailText.getText().toString());
            values.put(ContactColumnInfo.BLOG, blogText.getText().toString());
            Log.e(TAG+":onPause",mUri.toString());
            Log.e(TAG+":onPause",values.toString());
            getContentResolver().update(mUri, values, null, null);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    //取消，回退到最初的信息
    private void backupContact() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
            ContentValues values = new ContentValues();
            values.put(ContactColumnInfo.NAME, nameText.getText().toString());
            values.put(ContactColumnInfo.MOBILENUM, mobileText.getText().toString());
            values.put(ContactColumnInfo.HOMENUM, homeText.getText().toString());
            values.put(ContactColumnInfo.ADDRESS, addressText.getText().toString());
            values.put(ContactColumnInfo.EMAIL, emailText.getText().toString());
            values.put(ContactColumnInfo.BLOG, blogText.getText().toString());
            Log.e(TAG+":onPause",mUri.toString());
            Log.e(TAG+":onPause",values.toString());
            getContentResolver().update(mUri, values, null, null);
        }
        setResult(RESULT_CANCELED);
        finish();
    }
}
