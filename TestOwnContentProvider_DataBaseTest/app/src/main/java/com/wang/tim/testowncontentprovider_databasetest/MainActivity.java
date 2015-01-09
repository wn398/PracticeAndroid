package com.wang.tim.testowncontentprovider_databasetest;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity {
    private static final String Author = "com.wang.tim.provider";
    private static final String tableName = "book";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addBtn = (Button)findViewById(R.id.addData);
        Button updateBtn = (Button)findViewById(R.id.updateData);
        Button queryBtn = (Button)findViewById(R.id.queryData);
        Button deleteBtn = (Button)findViewById(R.id.deleteData);
        final Uri parserUri = Uri.parse("content://" + Author + "/" + tableName);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ContentResolver resolver = getContentResolver();

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Add book");
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View addBookView = factory.inflate(R.layout.addbook,null);
                builder.setView(addBookView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name = (EditText) (addBookView.findViewById(R.id.name));
                        EditText pages = (EditText) (addBookView.findViewById(R.id.pages));
                        EditText price = (EditText) (addBookView.findViewById(R.id.price));
                        ContentValues values = new ContentValues();
                        values.put("name", name.getText().toString());
                        values.put("pages", pages.getText().toString());
                        values.put("price", price.getText().toString());
                        try {
                            resolver.insert(parserUri,values);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ContentResolver resolver = getContentResolver();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Update First Data");
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View addBookView = factory.inflate(R.layout.addbook,null);
                builder.setView(addBookView);
                Cursor cursor = resolver.query(parserUri,null,"_id=?",new String[]{"1"},null);
                final EditText nameV = (EditText) (addBookView.findViewById(R.id.name));
                final EditText pagesV = (EditText) (addBookView.findViewById(R.id.pages));
                final EditText priceV = (EditText) (addBookView.findViewById(R.id.price));
                if(cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                    float price = cursor.getFloat(cursor.getColumnIndex("price"));
                    nameV.setText(name);
                    pagesV.setText(Integer.toString(pages));
                    priceV.setText(Float.toString(price));
                }
                builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues values = new ContentValues();
                        values.put("name", nameV.getText().toString());
                        values.put("pages", pagesV.getText().toString());
                        values.put("price", priceV.getText().toString());
                        try {
                            resolver.update(parserUri, values, "_id=?", new String[]{"1"});
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancle",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
            }
        });

        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ContentResolver resolver = getContentResolver();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Show Book");
                LayoutInflater factory = LayoutInflater.from(v.getContext());
                View view = factory.inflate(R.layout.showbookdata,null);
                builder.setView(view);
                ListView listView = (ListView)view.findViewById(R.id.showBookList);
                Cursor cursor = resolver.query(parserUri,null,null,null,null);
                ListAdapter adapter = new SimpleCursorAdapter(v.getContext(),android.R.layout.simple_list_item_2,cursor,new String[]{"_id","name"},new int[]{android.R.id.text1,android.R.id.text2});
                listView.setAdapter(adapter);
                builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ContentResolver resolver = getContentResolver();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Delete book");

                final EditText editText = new EditText(v.getContext());
                editText.setHint("input the id");
                builder.setView(editText);

                builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = editText.getText().toString();
                        try{
                            resolver.delete(parserUri,"_id=?",new String[]{id});
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
