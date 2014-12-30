package com.wang.tim.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ContactView extends Activity {
    private TextView mTextViewName;
    private TextView mTextViewMobile;
    private TextView mTextViewHome;
    private TextView mTextViewAddress;
    private TextView mTextViewEmail;
    private TextView mTextViewBlog;

    private Cursor mCursor;
    private Uri mUri;

    public static final int REVERT_ID = Menu.FIRST;
    public static final int DELETE_ID = Menu.FIRST+1;
    private static final int EDITOR_ID = Menu.FIRST + 2;
    private static final int CALL_ID = Menu.FIRST + 3;
    private static final int SENDSMS_ID = Menu.FIRST + 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getIntent().getData();
        setContentView(R.layout.activity_contacty_view);

        mTextViewName = (TextView) findViewById(R.id.TextView_Name);
        mTextViewMobile = (TextView)findViewById(R.id.TextView_Mobile);
        mTextViewHome = (TextView) findViewById(R.id.TextView_Home);
        mTextViewAddress = (TextView) findViewById(R.id.TextView_Address);
        mTextViewEmail = (TextView) findViewById(R.id.TextView_Email);
        mTextViewBlog = (TextView) findViewById(R.id.TextView_Blog);

        //查询获得联系人信息
        mCursor = managedQuery(mUri, ContactColumnInfo.RESULTS, null, null, null);
        mCursor.moveToFirst();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCursor !=null){
            //读取并显示联系人信息
            mCursor.moveToFirst();
            mTextViewName.setText(mCursor.getString(ContactColumnInfo.NAME_COLUMN));
            mTextViewMobile.setText(mCursor.getString(ContactColumnInfo.MOBILNUM_COLUMN));
            mTextViewHome.setText(mCursor.getString(ContactColumnInfo.HOMENUM_COLUMN));
            mTextViewAddress.setText(mCursor.getString(ContactColumnInfo.ADDRESS_COLUMN));
            mTextViewEmail.setText(mCursor.getString(ContactColumnInfo.EMAIL_COLUMN));
            mTextViewBlog.setText(mCursor.getString(ContactColumnInfo.BLOG_COLUMN));
        }else{
            setTitle("Error Message(No contact)");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Add the menu item by code
        menu.add(0,REVERT_ID,0,R.string.revert).setShortcut('0', 'r').setIcon(R.drawable.listuser);
        menu.add(0,DELETE_ID,0,R.string.delete_user).setShortcut('0','d').setIcon(R.drawable.remove);
        menu.add(0,EDITOR_ID,0,R.string.editor_user).setShortcut('0','e').setIcon(R.drawable.edituser);
        menu.add(0,CALL_ID,0,R.string.call_user).setShortcut('0','c').setIcon(R.drawable.calluser).setTitle(getResources().getString(R.string.call_user)+mTextViewName.getText());
        menu.add(0,SENDSMS_ID,0,R.string.sendsms_user).setShortcut('0','s').setIcon(R.drawable.sendsms).setTitle(getResources().getString(R.string.sendsms_user)+mTextViewName.getText());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
                 //删除
            case DELETE_ID:
                deleteContact();
                finish();
                break;
            //返回
            case REVERT_ID:
                setResult(RESULT_CANCELED);
                finish();
                break;
            //编辑
            case EDITOR_ID:
                startActivity(new Intent(Intent.ACTION_EDIT,mUri));
                break;
            //call
            case CALL_ID:
                Intent call = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+mTextViewMobile.getText()));
                startActivity(call);
                break;
            //发送短信
            case SENDSMS_ID:
                Intent sendM = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mTextViewMobile.getText()));
                startActivity(sendM);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //删除联系人信息
    private void deleteContact() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
            getContentResolver().delete(mUri,null,null);
            setResult(RESULT_CANCELED);
        }
    }
}
