package com.martin.cloudmarket;

import com.martin.cloudmarket.database.MyOpenHelper;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RadioGroup;

public class MainActivity extends Activity {
	
	private RadioGroup rg = null;
//	private MyOpenHelper oh;
//	private String  myDB = "couldmarket.db";
//	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//通过控件的ID来得到代表控件的对象 
		rg = (RadioGroup) findViewById(R.id.rg);
		//为RadioGroup设置监听器，需要注意的是，这里的监听器和Button控件的监听器有所不同
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
		
				switch (checkedId) {
					case R.id.radio_button1:
						//显示主SuperMarket的Fragment
						showFragmentSuperMarket();
						break;
					case R.id.radio_button2:
						//显示Order的Fragment
						showFragmentOrder();
						break;
					case R.id.radio_button3:
						//显示Shop的Fragment
						showFragmentShop();
						break;
					case R.id.radio_button4:
						//显示Mine的Fragment
						showFragmentMine();
						break;					
					default:
						break;
				}
			}
			
		});
		//显示主SuperMarket的Fragment
		showFragmentSuperMarket();
		
//		oh = new MyOpenHelper(this, myDB, null, 1);
//		//如果数据库不存在，先创建，再打开，如果存在，就直接打开
//		db = oh.getWritableDatabase();
//		db.close();
	}
    
    private void showFragmentSuperMarket(){
    	//1.创建fragment对象
    	FragmentSupermarket fs = new FragmentSupermarket(MainActivity.this);
    	//2.获取fragment管理器
    	FragmentManager fm = getFragmentManager();
    	//3.开启事物
    	FragmentTransaction ft = fm.beginTransaction();
    	//4.显示fragment
    	ft.replace(R.id.fl, fs);
    	//5.提交
    	ft.commit();
    }
    
    private void showFragmentMine(){
    	//1.创建fragment对象
    	FragmentMine fmine = new FragmentMine();
    	//2.获取fragment管理器
    	FragmentManager fm = getFragmentManager();
    	//3.开启事物
    	FragmentTransaction ft = fm.beginTransaction();
    	//4.显示fragment
    	ft.replace(R.id.fl, fmine);
    	//5.提交
    	ft.commit();
    }
    
    private void showFragmentOrder(){
    	//1.创建fragment对象
    	FragmentOrder fo = new FragmentOrder(MainActivity.this);
    	//2.获取fragment管理器
    	FragmentManager fm = getFragmentManager();
    	//3.开启事物
    	FragmentTransaction ft = fm.beginTransaction();
    	//4.显示fragment
    	ft.replace(R.id.fl, fo);
    	//5.提交
    	ft.commit();
    }
    
    private void showFragmentShop(){
    	//1.创建fragment对象
    	FragmentShop fs = new FragmentShop();
    	//2.获取fragment管理器
    	FragmentManager fm = getFragmentManager();
    	//3.开启事物
    	FragmentTransaction ft = fm.beginTransaction();
    	//4.显示fragment
    	ft.replace(R.id.fl, fs);
    	//5.提交
    	ft.commit();
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
         case 1:
         	break;
         case 2:
         	break;
      }
	}
    
}
