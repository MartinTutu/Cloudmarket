package com.martin.cloudmarket;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioGroup;

public class MainActivity extends Activity {
	
	private RadioGroup rg = null;
//	private RadioButton rb1 = null;
//	private RadioButton rb2 = null;
//	private RadioButton rb3 = null;
//	private RadioButton rb4 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//ͨ���ؼ���ID���õ�����ؼ��Ķ��� 
		rg = (RadioGroup) findViewById(R.id.rg);
//		rb1 = (RadioButton) findViewById(R.id.radio_button1);
//		rb2 = (RadioButton) findViewById(R.id.radio_button2);
//		rb3 = (RadioButton) findViewById(R.id.radio_button3);
//		rb4 = (RadioButton) findViewById(R.id.radio_button4);		
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
						showFragmentMine();
						break;					
					default:
						break;
				}
			}
			
		});
		//��ʾ��SuperMarket��Fragment
		showFragmentSuperMarket();
	}
    
    private void showFragmentSuperMarket(){
    	//1.����fragment����
    	FragmentSupermarket fs = new FragmentSupermarket();
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
    	FragmentMine fmine = new FragmentMine();
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
    	FragmentOrder fo = new FragmentOrder();
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
}
