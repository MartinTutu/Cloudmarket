package com.martin.cloudmarket;

import com.martin.cloudmarket.UI.CustomImageView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMine extends Fragment {
	
	//ϵͳ�Զ����ã����ص�View������ΪFragment��������ʾ
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View v = inflater.inflate(R.layout.fragment_mine, null);
			CustomImageView civ = (CustomImageView) v.findViewById(R.id.customimageview);
			civ.setImageResource(R.drawable.ic_touxiang);
			return v;
		}
}
