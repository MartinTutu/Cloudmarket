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
		//���ص�Ҫ��ʾ����Ŀ������
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return goodslist.size();
		}
		
		//����һ��View���󣬻���ΪListView��һ����Ŀ��ʾ�ڽ�����
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Goods good = goodslist.get(position);
			
			View v = null;
			ViewHolder mHolder = null;
			if(convertView == null){
				v = View.inflate(MarketActivity.this, R.layout.goods_item, null);
				
				//����viewHoler��װ������Ŀʹ�õ����
				mHolder = new ViewHolder();
				mHolder.goods_tv_title = (TextView) v.findViewById(R.id.goods_tv_title);
				mHolder.goods_tv_xiaoliang = (TextView) v.findViewById(R.id.goods_tv_xiaoliang);
				mHolder.goods_tv_type = (TextView) v.findViewById(R.id.goods_tv_type);
				mHolder.goods_tv_number = (TextView) v.findViewById(R.id.goods_tv_number);
				mHolder.goods_tv_detail = (TextView) v.findViewById(R.id.goods_tv_detail);
				mHolder.goods_tv_jiage = (TextView) v.findViewById(R.id.goods_tv_jiage);
				mHolder.goods_siv = (SmartImageView) v.findViewById(R.id.goods_siv);
				
				//��viewHolder��װ��view�����У�����view������ʱ��viewHolderҲ�ͱ�������
				v.setTag(mHolder);
			}
			else{
				v = convertView;
				//��view��ȡ�������viewHolder��viewHolder�о������е�������󣬲���Ҫ��ȥfindViewById
				mHolder = (ViewHolder) v.getTag();
			}
			//����Ŀ�е�ÿ���������Ҫ��ʾ������
			mHolder.goods_tv_title.setText(good.getTitle());
			mHolder.goods_tv_xiaoliang.setText("������"+good.getXiaoliang());
			mHolder.goods_tv_type.setText("���ͣ�"+good.getType());
			mHolder.goods_tv_number.setText("ʣ�ࣺ"+good.getNumber());
			mHolder.goods_tv_detail.setText("��Ʒ������"+good.getDetail());
			mHolder.goods_tv_jiage.setText("�ۼۣ�"+good.getJiage());
			
			mHolder.goods_siv.setImageUrl(good.getGoodsURL());
			
			//���ü���
			v.setOnClickListener(new OnClickListener() {
				
				@SuppressLint("InflateParams")
				@Override
				public void onClick(View v) {
					//Obtains the LayoutInflater from the given context.
					LayoutInflater inflater = LayoutInflater.from(context);	
					View dialogView = inflater.inflate(R.layout.goods_dialog, null);
					//�����Ի��򴴽���
					AlertDialog.Builder builder = new Builder(context);
					//����ͼ��
					builder.setIcon(android.R.drawable.ic_dialog_info);
					//���ñ���
					builder.setTitle("�Ƿ�Ҫ��"+good.getTitle()+"���빺�ﳵ");
					//Sets a custom view to be the contents of the alert dialog. 
					builder.setView(dialogView);
					
					TextView goods_tv_title = (TextView) dialogView.findViewById(R.id.goods_tv_title);
					TextView goods_tv_detail = (TextView) dialogView.findViewById(R.id.goods_tv_detail);
					TextView goods_tv_jiage = (TextView) dialogView.findViewById(R.id.goods_tv_jiage);
					SmartImageView goods_siv = (SmartImageView) dialogView.findViewById(R.id.goods_siv);
					
					goods_tv_title.setText(good.getTitle());
					goods_tv_detail.setText("��Ʒ������"+good.getDetail());
					goods_tv_jiage.setText("�ۼۣ�"+good.getJiage());
					goods_siv.setImageUrl(good.getGoodsURL());
					
					//����ȷ����ť
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						
						@SuppressLint("ShowToast")
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(context, "������ȷ��", 0).show();
						}
					});
					//����ȡ����ť
					builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Toast.makeText(context, "������ȡ��", 0).show();	
						}						
					});
					//��ʾ�Ի���
					AlertDialog alertDialog = builder.create();
					alertDialog.show();
				}
			});
			
			return v;
		}
		
		//����Ŀ��Ҫʹ�õ������������װ���������
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
						//�������Ϣ��һ��xml�ļ����ı���Ϣ����xml������ȥ����������Ҫ��Ϊ�ı�ȥ����
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
			
			//��ȡ�¼����ͣ�ͨ���¼������жϳ���ǰ�������Ǻ�ʲô�ڵ�
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
				//ָ���ƶ�����һ���ڵ㲢�����¼�����
				type = xp.next();
			}
			
			//������Ϣ�������߳�ˢ��listview
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
	        // ��ȡListView��Ӧ��Adapter   
	        ListAdapter listAdapter = listView.getAdapter();   
	        if (listAdapter == null) {   
	            return;   
	        }   
	   
	        int totalHeight = 0;   
	        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
	            // listAdapter.getCount()�������������Ŀ   
	            View listItem = listAdapter.getView(i, null, listView);   
	            // ��������View �Ŀ��   
	            listItem.measure(0, 0);    
	            // ͳ������������ܸ߶�   
	            totalHeight += listItem.getMeasuredHeight();    
	        }   
	   
	        ViewGroup.LayoutParams params = listView.getLayoutParams();   
	        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
	        // listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�   
	        // params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�   
	        listView.setLayoutParams(params);   
	    }
}
