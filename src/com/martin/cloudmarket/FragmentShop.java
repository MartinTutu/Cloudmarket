package com.martin.cloudmarket;

import com.martin.cloudmarket.R;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentShop extends Fragment {
	
		//系统自动调用，返回的View对象作为Fragment的内容显示
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View v = inflater.inflate(R.layout.fragment_shop, null);			
			return v;
		}		
}
