package com.martin.cloudmarket;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentOrder extends Fragment {
	
	//ϵͳ�Զ����ã����ص�View������ΪFragment��������ʾ
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View v = inflater.inflate(R.layout.fragment_order, null);
			return v;
		}
}
