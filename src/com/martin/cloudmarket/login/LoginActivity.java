package com.martin.cloudmarket.login;

import com.martin.cloudmarket.MarketActivity;
import com.martin.cloudmarket.R;
import com.martin.cloudmarket.database.MyOpenHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	
	private EditText username;
	private EditText password;
	private Button   login;
	private Button   register;
	
	private MyOpenHelper oh;
	private String  myDB = "couldmarket.db";
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		username = (EditText) findViewById(R.id.edname);
		password = (EditText) findViewById(R.id.edpassword);
		login    = (Button)   findViewById(R.id.btlogin);
		register = (Button)   findViewById(R.id.btregister);		
		
		// 跳转到注册界面  
        register.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
	                Intent intent = new Intent();  
	                intent.setClass(LoginActivity.this, RegistersActivity.class);  
	                startActivity(intent);  
            }  
        });  
        login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String name = username.getText().toString();
				String pw   = password.getText().toString();
				String s1 = null;
				String s2 = null;
				String s3 = null;
				String s4 = null;
				oh = new MyOpenHelper(LoginActivity.this, myDB, null, 1);
				db = oh.getWritableDatabase();
				int flag = 0;
				//查询获得游标   
				Cursor cursor = db.query ("user",null,null,null,null,null,null);   			  
				//判断游标是否为空   
				if(cursor.moveToFirst()){   
					//遍历游标   
					for(int i=0;i<cursor.getCount();i++){   
						cursor.moveToPosition(i);;   
						s1 = cursor.getString(1);
						s2 = cursor.getString(2);
						s3 = cursor.getString(3);
						s4 = cursor.getString(4);
						if(name.equals(s1)&&pw.equals(s2)){
							flag = 1;
							break;
						}   
					}   
				}
				db.close();
				if(flag == 1){
					Intent intent = LoginActivity.this.getIntent();
					Bundle bundle = new Bundle();
					bundle.putString("username", s1);
					bundle.putString("password", s2);
					bundle.putString("address", s3);
					bundle.putString("tel", s4);
					intent.putExtras(bundle);
					LoginActivity.this.setResult(1, intent);
					LoginActivity.this.finish();
					Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();					
				}else if(flag == 0){
					LoginActivity.this.setResult(0);
					LoginActivity.this.finish();
					Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
				}
			}
        	
        }); 
	}

}
