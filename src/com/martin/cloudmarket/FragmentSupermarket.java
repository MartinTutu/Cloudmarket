package com.martin.cloudmarket;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.martin.cloudmarket.R;

import com.finddreams.adbanner.BaseWebActivity;
import com.finddreams.adbanner.ImagePagerAdapter;
import com.finddreams.bannerview.CircleFlowIndicator;
import com.finddreams.bannerview.ViewFlow;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class FragmentSupermarket extends Fragment {
	
	private ViewFlow mViewFlow;
	private CircleFlowIndicator mFlowIndicator;
	private ArrayList<String> imageUrlList = new ArrayList<String>();
	ArrayList<String> linkUrlArray= new ArrayList<String>();
	ArrayList<String> titleList= new ArrayList<String>();
	private LinearLayout notice_parent_ll;
	private LinearLayout notice_ll;
	private ViewFlipper notice_vf;
	private int mCurrPos;
	
	private View v;
	private Activity context = null;
	
	public FragmentSupermarket(Activity context) {
		super();
		this.context = context;
	}

	//系统自动调用，返回的View对象作为Fragment的内容显示
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.fragment_supermarket, null);
		initView();
		imageUrlList
				.add("https://img.alicdn.com/tps/i4/TB1RymbLFXXXXcoXXXXJ02v2pXX-750-291.jpg_Q90.jpg");
		imageUrlList
				.add("https://img.alicdn.com/tps/i4/TB1dHBxLFXXXXbUXFXXJ02v2pXX-750-291.jpg_Q90.jpg");
		imageUrlList
				.add("https://img.alicdn.com/tps/TB1_R8ULFXXXXcNXpXXXXXXXXXX-750-291.jpg_Q90.jpg");
		imageUrlList
				.add("https://img.alicdn.com/imgextra/i2/2091381017/TB2AwankXXXXXbCXXXXXXXXXXXX_!!2091381017.jpg_Q90.jpg");

		linkUrlArray
				.add("http://blog.csdn.net/finddreams/article/details/44301359");
		linkUrlArray
				.add("http://blog.csdn.net/finddreams/article/details/43486527");
		linkUrlArray
				.add("http://blog.csdn.net/finddreams/article/details/44648121");
		linkUrlArray
				.add("http://blog.csdn.net/finddreams/article/details/44619589");
		titleList.add("天天特价，猴年大狂欢");
		titleList.add("全球吃喝送到家，让你安心过大年");
		titleList.add("日用百货，开年大促销：多件多折，清仓直降 ");
		titleList.add("多快好省，只为品质生活 ");
		initBanner(imageUrlList);
		initRollNotice();
		return v;
	}

	private void initView() {
		mViewFlow = (ViewFlow) v.findViewById(R.id.viewflow);
		mFlowIndicator = (CircleFlowIndicator) v.findViewById(R.id.viewflowindic);

	}
	private void initRollNotice() {
		FrameLayout main_notice = (FrameLayout) v.findViewById(R.id.main_notice);
		notice_parent_ll = (LinearLayout) getActivity().getLayoutInflater().inflate(
				R.layout.layout_notice, null);
		notice_ll = ((LinearLayout) this.notice_parent_ll
				.findViewById(R.id.homepage_notice_ll));
		notice_vf = ((ViewFlipper) this.notice_parent_ll
				.findViewById(R.id.homepage_notice_vf));
		main_notice.addView(notice_parent_ll);
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				((Activity)context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try{
						    moveNext();
						    Log.d("Task", "下一个");       
                        }catch(NullPointerException e){
                        	e.printStackTrace();
                        }
						
					}
				});

			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 0, 4000);
	}

	private void moveNext() {
		setView(this.mCurrPos, this.mCurrPos + 1);
		this.notice_vf.setInAnimation(getActivity(), R.anim.in_bottomtop);
		this.notice_vf.setOutAnimation(getActivity(), R.anim.out_bottomtop);
		this.notice_vf.showNext();
	}

	private void setView(int curr, int next) {

		View noticeView = getActivity().getLayoutInflater().inflate(R.layout.notice_item,
				null);
		TextView notice_tv = (TextView) noticeView.findViewById(R.id.notice_tv);
		if ((curr < next) && (next > (titleList.size() - 1))) {
			next = 0;
		} else if ((curr > next) && (next < 0)) {
			next = titleList.size() - 1;
		}
		notice_tv.setText(titleList.get(next));
		notice_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("url", linkUrlArray.get(mCurrPos));
				bundle.putString("title", titleList.get(mCurrPos));
				Intent intent = new Intent(context,
						BaseWebActivity.class);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		if (notice_vf.getChildCount() > 1) {
			notice_vf.removeViewAt(0);
		}
		notice_vf.addView(noticeView, notice_vf.getChildCount());
		mCurrPos = next;

	}
	private void initBanner(ArrayList<String> imageUrlList) {
		
		mViewFlow.setAdapter(new ImagePagerAdapter(context, imageUrlList,
				linkUrlArray, titleList).setInfiniteLoop(true));
		mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，
														// 我的ImageAdapter实际图片张数为3

		mViewFlow.setFlowIndicator(mFlowIndicator);
		mViewFlow.setTimeSpan(4500);
		mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
	}

}
