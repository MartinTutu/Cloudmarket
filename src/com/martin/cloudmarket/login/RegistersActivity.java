package com.martin.cloudmarket.login;

import com.martin.cloudmarket.R;
import com.martin.cloudmarket.database.MyOpenHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegistersActivity extends Activity {

	private EditText edname1;  
    private EditText edpassword1;
    private EditText edaddress;
    private EditText edtel;
    private Button register1;
    
    private MyOpenHelper oh;
	private String  myDB = "couldmarket.db";
	private SQLiteDatabase db;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		edname1 = (EditText) findViewById(R.id.edname1);
		edpassword1 = (EditText) findViewById(R.id.edpassword1);
		edaddress = (EditText) findViewById(R.id.edaddress1);
		edtel = (EditText) findViewById(R.id.edtel1);
		register1 = (Button) findViewById(R.id.btregister1);
		
		register1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = edname1.getText().toString();
				String password = edpassword1.getText().toString();
				String address = edaddress.getText().toString();
				String tel = edtel.getText().toString();
				
				oh = new MyOpenHelper(RegistersActivity.this, myDB, null, 1);
				db = oh.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("username", name);
				values.put("password", password);
				values.put("address", address);
				values.put("tel", tel);
				db.insert("user", null, values);
				db.close();
				RegistersActivity.this.finish();
			}
		});
	}

}
