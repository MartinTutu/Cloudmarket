package com.martin.cloudmarket;

import com.martin.cloudmarket.UI.CustomImageView;
import com.martin.cloudmarket.login.LoginActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMine extends Fragment {
	
	View v;
	Button login_button;
	Button exit_button;
	TextView welcome;
	Activity context;
	String username = null;
	
	public FragmentMine(Activity context) {
		super();
		this.context = context;
	}
	
	public FragmentMine(Activity context,String username) {
		super();
		this.context = context;
		this.username = username;
	}

	//ϵͳ�Զ����ã����ص�View������ΪFragment��������ʾ
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.fragment_mine, null);
		CustomImageView civ = (CustomImageView) v.findViewById(R.id.customimageview);
		civ.setImageResource(R.drawable.ic_touxiang);
		
		initlogin();
		return v;
	}

	private void initlogin() {
		login_button = (Button) v.findViewById(R.id.personal_login_button);
		exit_button = (Button) v.findViewById(R.id.personal_exit);
		welcome = (TextView) v.findViewById(R.id.welcome);
		if(username != null){
			welcome.setText(username);
			login_button.setText(null);
		}
		login_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, LoginActivity.class);
				context.startActivityForResult(intent, 2);
			}
		});
		exit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				welcome.setText("��ӭ�����Ƴ���");
				login_button.setText("��¼/ע��");
				MainActivity.flag = 0;
				Toast.makeText(context, "�˳��ɹ�", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
