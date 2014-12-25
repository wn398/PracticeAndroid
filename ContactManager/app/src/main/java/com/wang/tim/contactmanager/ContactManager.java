package com.wang.tim.contactmanager;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ContactManager extends ListActivity {
    private static final String TAG = "ContactManager";

    private static final int AddContact_ID = Menu.FIRST;
    private static final int EditContact_ID = Menu.FIRST+1;
    private static final int DELEContact_ID = Menu.FIRST+2;
    private static final int EXITContact_ID = Menu.FIRST+3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
        Intent intent = getIntent();
        if(intent.getData()==null){
            intent.setData(ContactProvider.CONTENT_URI);
        }
        getListView().setOnCreateContextMenuListener(this);
        getListView().setBackgroundResource(R.drawable.bg);
        Cursor cursor = managedQuery(getIntent().getData(),ContactColumnInfo.RESULTS,null,null,null);
        //注册每个列表表示形式，姓名和电话
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,cursor,new String[]{ContactColumnInfo.NAME,ContactColumnInfo.MOBILENUM},
                new int[]{android.R.id.text1,android.R.id.text2});
        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //添加
        menu.add(0, AddContact_ID, 0, R.string.add_user).setShortcut('3', 'a').setIcon(R.drawable.add);
        Intent intent = new Intent(null,getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);

        //退出程序
        menu.add(0,EXITContact_ID,0,R.string.exit).setShortcut('4','d').setIcon(R.drawable.exit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case AddContact_ID:
                startActivity(new Intent(Intent.ACTION_INSERT,getIntent().getData()));
                return true;
            case EXITContact_ID:
                this.finish();
                return true;
        }
               return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean haveItems = getListAdapter().getCount()>0;
        if(haveItems){
            Uri uri = ContentUris.withAppendedId(getIntent().getData(),getSelectedItemId());
            Intent[] specifics = new Intent[2];
            specifics[0] = new Intent(Intent.ACTION_EDIT,uri);
            specifics[1] = new Intent(Intent.ACTION_VIEW, uri);
            MenuItem[] items = new MenuItem[2];
            //添加满足条件的菜单
            Intent intent = new Intent(null,uri);
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
            menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE,0,0,null,specifics,intent,0,items);
            if(items[0]!=null){
                //编辑联系人
                items[0].setShortcut('1','e').setIcon(R.drawable.edituser).setTitle(R.string.editor_user);
            }
            if(items[1]!=null){
                //查看联系人
                items[1].setShortcut('2', 'f').setTitle(R.string.view_user).setIcon(R.drawable.viewuser);
            }
        }else{
            menu.removeGroup(Menu.CATEGORY_ALTERNATIVE);
        }
        return true;
    }
    //动态菜单处理  点击的默认操作也可以在这里处理

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Uri uri = ContentUris.withAppendedId(getIntent().getData(),id);

        String action = getIntent().getAction();
        if(Intent.ACTION_EDIT.equals(action)){
            //编辑
            startActivity(new Intent(Intent.ACTION_EDIT,uri));
        }else{
            //查看
            startActivity(new Intent(Intent.ACTION_VIEW,uri));
        }
    }
    //长按触发的菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info;
        try{
            info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        }catch (ClassCastException e){
            e.printStackTrace();
            return;
        }
        //得到长按的数据项
        Cursor cursor = (Cursor)getListAdapter().getItem(info.position);
        if(cursor == null){
            return;
        }
        menu.setHeaderTitle(cursor.getString(1));
        //添加删除菜单
        menu.add(0,DELEContact_ID,0,R.string.delete_user);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try{
            info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        }catch (ClassCastException e){
            e.printStackTrace();
            return false;
        }
        switch (item.getItemId()){
            case DELEContact_ID:
                //删除一条记录
                Uri uri = ContentUris.withAppendedId(getIntent().getData(),info.id);
                getContentResolver().delete(uri,null,null);
                return true;
        }
        return false;
    }
}
