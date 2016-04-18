package com.martin.cloudmarket;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.loopj.android.image.SmartImageView;
import com.martin.cloudmarket.demo.Goods;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MarketActivity extends Activity {
	
	TextView tv_title = null;
	
	private Bundle bundle;
	private String shop_id="tt";
	private String title;
	private Activity context=this;
	
	List<Goods> goodslist;
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			ListView lv = (ListView) findViewById(R.id.lv_market_goods_item);
			lv.setAdapter(new MyAdapter());
			setListViewHeightBasedOnChildren(lv);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.market_detail);
		
		tv_title = (TextView) findViewById(R.id.market_title);
		
		bundle = this.getIntent().getExtras();
		shop_id = bundle.getString("ID");
		title = bundle.getString("Title");
		tv_title.setText(title);
		
		getNewsInfo();
	}
	
	class MyAdapter extends BaseAdapter{
		//返回的要显示的条目的数量
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return goodslist.size();
		}
		
		//返回一个View对象，会作为ListView的一个条目显示在界面上
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Goods good = goodslist.get(position);
			
			View v = null;
			ViewHolder mHolder = null;
			if(convertView == null){
				v = View.inflate(MarketActivity.this, R.layout.goods_item, null);
				
				//创建viewHoler封装所有条目使用的组件
				mHolder = new ViewHolder();
				mHolder.goods_tv_title = (TextView) v.findViewById(R.id.goods_tv_title);
				mHolder.goods_tv_xiaoliang = (TextView) v.findViewById(R.id.goods_tv_xiaoliang);
				mHolder.goods_tv_type = (TextView) v.findViewById(R.id.goods_tv_type);
				mHolder.goods_tv_number = (TextView) v.findViewById(R.id.goods_tv_number);
				mHolder.goods_tv_detail = (TextView) v.findViewById(R.id.goods_tv_detail);
				mHolder.goods_tv_jiage = (TextView) v.findViewById(R.id.goods_tv_jiage);
				mHolder.goods_siv = (SmartImageView) v.findViewById(R.id.goods_siv);
				
				//把viewHolder封装至view对象中，这样view被缓存时，viewHolder也就被缓存了
				v.setTag(mHolder);
			}
			else{
				v = convertView;
				//从view中取出保存的viewHolder，viewHolder中就有所有的组件对象，不需要再去findViewById
				mHolder = (ViewHolder) v.getTag();
			}
			//给条目中的每个组件设置要显示的内容
			mHolder.goods_tv_title.setText(good.getTitle());
			mHolder.goods_tv_xiaoliang.setText("销量："+good.getXiaoliang());
			mHolder.goods_tv_type.setText("类型："+good.getType());
			mHolder.goods_tv_number.setText("剩余："+good.getNumber());
			mHolder.goods_tv_detail.setText("商品描述："+good.getDetail());
			mHolder.goods_tv_jiage.setText("售价："+good.getJiage());
			
			mHolder.goods_siv.setImageUrl(good.getGoodsURL());
			
			//设置监听
			v.setOnClickListener(new OnClickListener() {
				
				@SuppressLint("InflateParams")
				@Override
				public void onClick(View v) {
					//Obtains the LayoutInflater from the given context.
					LayoutInflater inflater = LayoutInflater.from(context);	
					View dialogView = inflater.inflate(R.layout.goods_dialog, null);
					//创建对话框创建器
					AlertDialog.Builder builder = new Builder(context);
					//设置图标
					builder.setIcon(android.R.drawable.ic_dialog_info);
					//设置标题
					builder.setTitle("是否要将"+good.getTitle()+"加入购物车");
					//Sets a custom view to be the contents of the alert dialog. 
					builder.setView(dialogView);
					
					TextView goods_tv_title = (TextView) dialogView.findViewById(R.id.goods_tv_title);
					TextView goods_tv_detail = (TextView) dialogView.findViewById(R.id.goods_tv_detail);
					TextView goods_tv_jiage = (TextView) dialogView.findViewById(R.id.goods_tv_jiage);
					SmartImageView goods_siv = (SmartImageView) dialogView.findViewById(R.id.goods_siv);
					
					goods_tv_title.setText(good.getTitle());
					goods_tv_detail.setText("商品描述："+good.getDetail());
					goods_tv_jiage.setText("售价："+good.getJiage());
					goods_siv.setImageUrl(good.getGoodsURL());
					
					//设置确定按钮
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@SuppressLint("ShowToast")
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(context, "你点击了确定", 0).show();
						}
					});
					//设置取消按钮
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Toast.makeText(context, "你点击了取消", 0).show();	
						}						
					});
					//显示对话框
					AlertDialog alertDialog = builder.create();
					alertDialog.show();
				}
			});
			
			return v;
		}
		
		//把条目需要使用到的所有组件封装在这个类中
		class ViewHolder{
			TextView goods_tv_title;
			TextView goods_tv_xiaoliang;
			TextView goods_tv_type;
			TextView goods_tv_number;
			TextView goods_tv_detail;
			TextView goods_tv_jiage;
			SmartImageView goods_siv;
		}

		@Override
		public Object getItem(int position) {
			return goodslist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}

	private void getNewsInfo() {
		Thread t = new Thread(){
			@Override
			public void run() {
				//Log.e("shop_ID", shop_id);
				String path = "http://couldmarket-10021250.file.myqcloud.com/shoplist/shop_"+shop_id+".xml";
				//Toast.makeText(MarketActivity.this, path, Toast.LENGTH_LONG).show();
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
			
			Goods good = null;
			while(type != XmlPullParser.END_DOCUMENT){
				switch (type) {
				case XmlPullParser.START_TAG:
					if("goodslist".equals(xp.getName())){
						goodslist = new ArrayList<Goods>();
					}
					else if("good".equals(xp.getName())){
						good = new Goods();
					}else if("id".equals(xp.getName())){
						String id = xp.nextText();
						good.setId(id);
					}else if("title".equals(xp.getName())){
						String title = xp.nextText();
						good.setTitle(title);
					}else if("type".equals(xp.getName())){
						String type1 = xp.nextText();
						good.setType(type1);;
					}else if("xiaoliang".equals(xp.getName())){
						String xiaoliang = xp.nextText();
						good.setXiaoliang(xiaoliang);;
					}else if("number".equals(xp.getName())){
						String number = xp.nextText();
						good.setNumber(number);;
					}else if("detail".equals(xp.getName())){
						String detail = xp.nextText();
						good.setDetail(detail);
					}else if("jiage".equals(xp.getName())){
						String jiage = xp.nextText();
						good.setJiage(jiage);;
					}else if("logo".equals(xp.getName())){
						String goodsURL = xp.nextText();
						good.setGoodsURL(goodsURL);;
					}					
					break;
				case XmlPullParser.END_TAG:
					if("good".equals(xp.getName())){
						goodslist.add(good);
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
