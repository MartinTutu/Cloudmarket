package com.martin.cloudmarket;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;

import com.martin.cloudmarket.R;
import com.martin.cloudmarket.demo.Shop;
import com.finddreams.adbanner.BaseWebActivity;
import com.finddreams.adbanner.ImagePagerAdapter;
import com.finddreams.bannerview.CircleFlowIndicator;
import com.finddreams.bannerview.ViewFlow;
import com.loopj.android.image.SmartImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

@SuppressLint("HandlerLeak")
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
	
	private List<Shop> shoplist;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			ListView lv = (ListView) v.findViewById(R.id.lv);
			lv.setAdapter(new MyAdapter());
			setListViewHeightBasedOnChildren(lv);
		}
	};
	
	
	public FragmentSupermarket(Activity context) {
		super();
		this.context = context;
	}

	//系统自动调用，返回的View对象作为Fragment的内容显示
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.fragment_supermarket, null);
		initAdvertisement();
		getNewsInfo();
		return v;
	}

	private void initAdvertisement(){
		initView();
		imageUrlList
				.add("http://a.dangdang.com/api/data/cpx/img/39410001/1");
		imageUrlList
				.add("http://a.dangdang.com/api/data/cpx/img/39440001/1");
		imageUrlList
				.add("http://a.dangdang.com/api/data/cpx/img/39290001/1");
		imageUrlList
				.add("http://img11.360buyimg.com/da/jfs/t1942/111/2068438799/98442/d8ae93ff/56b6314dN2ae6ef80.jpg");

	    linkUrlArray
				.add("http://t.dangdang.com/201604_zht");
		linkUrlArray
				.add("http://fashion.dangdang.com/20160331_xswc");
		linkUrlArray
				.add("http://shop.dangdang.com/12111");
		linkUrlArray
				.add("http://blog.csdn.net/finddreams/article/details/44619589");
		titleList.add("天天特价，猴年大狂欢");
		titleList.add("全球吃喝送到家，让你安心过大年");
		titleList.add("日用百货，开年大促销：多件多折，清仓直降 ");
		titleList.add("多快好省，只为品质生活 ");
		initBanner(imageUrlList);
		initRollNotice();
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
				context.runOnUiThread(new Runnable() {
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
	
	private void getNewsInfo() {
		Thread t = new Thread(){
			@Override
			public void run() {
				String path = "http://couldmarket-10021250.file.myqcloud.com/shops.xml";
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(8000);
					conn.setReadTimeout(8000);
					
					if(conn.getResponseCode() == 200){
						//流里的信息是一个xml文件的文本信息，用xml解析器去解析，而不要作为文本去解析
						InputStream is = conn.getInputStream();
						getNewsFromStream(is);
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	private void getNewsFromStream(InputStream is) {
		XmlPullParser xp = Xml.newPullParser();
		try {
			xp.setInput(is, "gb2312");
			
			//获取事件类型，通过事件类型判断出当前解析的是和什么节点
			int type = xp.getEventType();
			
			Shop shop = null;
			while(type != XmlPullParser.END_DOCUMENT){
				switch (type) {
				case XmlPullParser.START_TAG:
					if("shoplist".equals(xp.getName())){
						shoplist = new ArrayList<Shop>();
					}else if("shop".equals(xp.getName())){
						shop = new Shop();
					}else if("id".equals(xp.getName())){
						String id = xp.nextText();
						shop.setId(id);
					}else if("title".equals(xp.getName())){
						String title = xp.nextText();
						shop.setTitle(title);
					}else if("type".equals(xp.getName())){
						String shoptype = xp.nextText();
						shop.setType(shoptype);
					}else if("mark".equals(xp.getName())){
						String mark = xp.nextText();
						shop.setMark(mark);
					}else if("trade".equals(xp.getName())){
						String trade = xp.nextText();
						shop.setTradingarea(trade);
					}else if("qisong".equals(xp.getName())){
						String qisong = xp.nextText();
						shop.setQisong(qisong);
					}else if("distance".equals(xp.getName())){
						String distance = xp.nextText();
						shop.setDistance(distance);
					}else if("logo".equals(xp.getName())){
						String logo = xp.nextText();
						shop.setLogoURL(logo);;
					}
					
					break;
				case XmlPullParser.END_TAG:
					if("shop".equals(xp.getName())){
						shoplist.add(shop);
					}
					break;

				}
				//指针移动到下一个节点并返回事件类型
				type = xp.next();
			}
			
			//发送消息，让主线程刷新listview
			handler.sendEmptyMessage(1);
//			for (News n : newsList) {
//				System.out.println(n.toString());
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
		
	class MyAdapter extends BaseAdapter{
		//返回的要显示的条目的数量
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return shoplist.size();
		}
		
		//返回一个View对象，会作为ListView的一个条目显示在界面上
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Shop shop = shoplist.get(position);
			
			View v = null;
			ViewHolder mHolder = null;
			if(convertView == null){
				v = View.inflate(context, R.layout.market_item, null);
				
				//创建viewHoler封装所有条目使用的组件
				mHolder = new ViewHolder();
				mHolder.tv_title = (TextView) v.findViewById(R.id.tv_title);
				mHolder.tv_mark = (TextView) v.findViewById(R.id.tv_mark);
				mHolder.tv_type = (TextView) v.findViewById(R.id.tv_type);
				mHolder.tv_trading_area = (TextView) v.findViewById(R.id.tv_trading_area);
				mHolder.tv_qisong = (TextView) v.findViewById(R.id.tv_qisong);
				mHolder.tv_distance = (TextView) v.findViewById(R.id.tv_distance);
				mHolder.siv = (SmartImageView) v.findViewById(R.id.siv);
				
				//把viewHolder封装至view对象中，这样view被缓存时，viewHolder也就被缓存了
				v.setTag(mHolder);
			}
			else{
				v = convertView;
				//从view中取出保存的viewHolder，viewHolder中就有所有的组件对象，不需要再去findViewById
				mHolder = (ViewHolder) v.getTag();
			}
			//给条目中的每个组件设置要显示的内容
			mHolder.tv_title.setText(shop.getTitle());
			mHolder.tv_mark.setText(shop.getMark());
			mHolder.tv_type.setText(shop.getType());
			mHolder.tv_trading_area.setText(shop.getTradingarea());
			mHolder.tv_qisong.setText(shop.getQisong());
			mHolder.tv_distance.setText(shop.getDistance());
			
			mHolder.siv.setImageUrl(shop.getLogoURL());
			
			//设置监听
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(context, MarketActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("ID", shop.getId());
					bundle.putString("Title", shop.getTitle());
					intent.putExtras(bundle);
					startActivityForResult(intent, 1);
					Toast.makeText(context,"点了"+shop.getId(),Toast.LENGTH_LONG).show();
				}
			});
			return v;
		}
		
		//把条目需要使用到的所有组件封装在这个类中
		class ViewHolder{
			TextView tv_title;
			TextView tv_mark;
			TextView tv_type;
			TextView tv_trading_area;
			TextView tv_qisong;
			TextView tv_distance;
			SmartImageView siv;
		}

		@Override
		public Object getItem(int position) {
			return shoplist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}
	
    public void setListViewHeightBasedOnChildren(ListView listView) {   
        // 获取ListView对应的Adapter   
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()返回数据项的数目   
            View listItem = listAdapter.getView(i, null, listView);   
            // 计算子项View 的宽高   
            listItem.measure(0, 0);    
            // 统计所有子项的总高度   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        // listView.getDividerHeight()获取子项间分隔符占用的高度   
        // params.height最后得到整个ListView完整显示需要的高度   
        listView.setLayoutParams(params);   
    }
}
