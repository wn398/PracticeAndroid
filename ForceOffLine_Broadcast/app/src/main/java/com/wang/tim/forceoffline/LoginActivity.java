package com.wang.tim.forceoffline;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends BaseActivity {
    private EditText usEditText;
    private EditText psEditText;
    private Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usEditText = (EditText)findViewById(R.id.inputName);
        psEditText = (EditText)findViewById(R.id.password);
        submitBtn = (Button)findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usEditText.getText().toString();
                String password = psEditText.getText().toString();
                if(username.equals("admin")&&password.equals("123456")){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("WARNING");
                    builder.setMessage("Username or Password wrong");
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Cancle",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
