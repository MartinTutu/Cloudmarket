package com.martin.cloudmarket;

import com.martin.cloudmarket.database.MyOpenHelper;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private RadioGroup rg = null;
	private Bundle bundle;
	public static String username = "�ο�";
	public static int flag = 0;
//	private MyOpenHelper oh;
//	private String  myDB = "couldmarket.db";
//	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//ͨ���ؼ���ID���õ�����ؼ��Ķ��� 
		rg = (RadioGroup) findViewById(R.id.rg);
		//ΪRadioGroup���ü���������Ҫע����ǣ�����ļ�������Button�ؼ��ļ�����������ͬ
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
		
				switch (checkedId) {
					case R.id.radio_button1:
						//��ʾ��SuperMarket��Fragment
						showFragmentSuperMarket();
						break;
					case R.id.radio_button2:
						//��ʾOrder��Fragment
						showFragmentOrder();
						break;
					case R.id.radio_button3:
						//��ʾShop��Fragment
						showFragmentShop();
						break;
					case R.id.radio_button4:
						//��ʾMine��Fragment
						if(flag == 0){
							showFragmentMine();
						}else if(flag == 1){
							showFragmentMine(username);
						}
						break;					
					default:
						break;
				}
			}
			
		});
		//��ʾ��SuperMarket��Fragment
		showFragmentSuperMarket();
		
//		oh = new MyOpenHelper(this, myDB, null, 1);
//		//������ݿⲻ���ڣ��ȴ������ٴ򿪣�������ڣ���ֱ�Ӵ�
//		db = oh.getWritableDatabase();
//		db.close();
	}
    
    private void showFragmentSuperMarket(){
    	//1.����fragment����
    	FragmentSupermarket fs = new FragmentSupermarket(MainActivity.this);
    	//2.��ȡfragment������
    	FragmentManager fm = getFragmentManager();
    	//3.��������
    	FragmentTransaction ft = fm.beginTransaction();
    	//4.��ʾfragment
    	ft.replace(R.id.fl, fs);
    	//5.�ύ
    	ft.commit();
    }
    
    private void showFragmentMine(){
    	//1.����fragment����
    	FragmentMine fmine = new FragmentMine(MainActivity.this);
    	//2.��ȡfragment������
    	FragmentManager fm = getFragmentManager();
    	//3.��������
    	FragmentTransaction ft = fm.beginTransaction();
    	//4.��ʾfragment
    	ft.replace(R.id.fl, fmine);
    	//5.�ύ
    	ft.commit();
    }
    
    private void showFragmentOrder(){
    	//1.����fragment����
    	FragmentOrder fo = new FragmentOrder(MainActivity.this);
    	//2.��ȡfragment������
    	FragmentManager fm = getFragmentManager();
    	//3.��������
    	FragmentTransaction ft = fm.beginTransaction();
    	//4.��ʾfragment
    	ft.replace(R.id.fl, fo);
    	//5.�ύ
    	ft.commit();
    }
    
    private void showFragmentShop(){
    	//1.����fragment����
    	FragmentShop fs = new FragmentShop();
    	//2.��ȡfragment������
    	FragmentManager fm = getFragmentManager();
    	//3.��������
    	FragmentTransaction ft = fm.beginTransaction();
    	//4.��ʾfragment
    	ft.replace(R.id.fl, fs);
    	//5.�ύ
    	ft.commit();
    }    

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Toast.makeText(this, "1234567890-", Toast.LENGTH_SHORT).show();
		switch(requestCode){
	         case 1:
		    	 if(resultCode == 1){
			        	showFragmentSuperMarket();
						//Toast.makeText(this, "ssfsdfsdf", Toast.LENGTH_LONG).show();
		    	 }
		    	 break;
	         case 2:
	        	 if(resultCode == 1){
	        		 bundle = data.getExtras();
	        		 username = bundle.getString("username");
	        		 flag = 1;
	        		 showFragmentMine(username);		         	 
	        	 }else if(resultCode == 0){
	        		 showFragmentMine();
	        	 }
	        	 break;
	         case 3:
	        	 if(resultCode == 1){
	        		 String ordernum = data.getStringExtra("ordernum");
	        		 showFragmentOrder(ordernum);
	        		 
	        	 }else if(resultCode == 0){
	        		 showFragmentOrder();
	        	 }
	        	 break;
	    }
		//super.onActivityResult(requestCode, resultCode, data);
	}

	private void showFragmentOrder(String ordernum) {
		//1.����fragment����
    	FragmentOrder fo = new FragmentOrder(MainActivity.this,ordernum);
    	//2.��ȡfragment������
    	FragmentManager fm = getFragmentManager();
    	//3.��������
    	FragmentTransaction ft = fm.beginTransaction();
    	//4.��ʾfragment
    	ft.replace(R.id.fl, fo);
    	//5.�ύ
    	ft.commit();
	}

	private void showFragmentMine(String username) {		
		//1.����fragment����
    	FragmentMine fmine = new FragmentMine(MainActivity.this,username);
    	//2.��ȡfragment������
    	FragmentManager fm = getFragmentManager();
    	//3.��������
    	FragmentTransaction ft = fm.beginTransaction();
    	//4.��ʾfragment
    	ft.replace(R.id.fl, fmine);
    	//5.�ύ
    	ft.commit();
	}

}
