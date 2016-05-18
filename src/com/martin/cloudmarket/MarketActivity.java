package com.martin.cloudmarket;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.loopj.android.image.SmartImageView;
import com.martin.cloudmarket.database.MyOpenHelper;
import com.martin.cloudmarket.demo.Goods;
import com.martin.cloudmarket.demo.Order;
import com.martin.cloudmarket.demo.OrderItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MarketActivity extends Activity {
	
	TextView tv_title = null;
	Button   return_market = null;
	
	private Bundle bundle;
	private String shop_id;
	private String title;
	private MyOpenHelper oh;
	private String  myDB = "couldmarket.db";
	private SQLiteDatabase db;
	private String  dbTableName;
	private Activity context=this;
	
	private List<Goods> goodslist;
	private List<OrderItem> orderlist = new ArrayList<OrderItem>();
	private Order myorder;
	private int   myorderID = 0;
	private int   myorderFlag = 0;
	private String orderName;
	
	Intent intent;
	
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
		return_market = (Button) findViewById(R.id.return_market);
		
		intent = this.getIntent();
		bundle = intent.getExtras();
		shop_id = bundle.getString("ID");
		title = bundle.getString("Title");
		tv_title.setText(title);
		
		dbTableName = "shop_"+shop_id;
		
		oh = new MyOpenHelper(context, myDB, null, 1);
		//������ݿⲻ���ڣ��ȴ������ٴ򿪣�������ڣ���ֱ�Ӵ�
		db = oh.getWritableDatabase();
		Cursor cursor = db.query ("ordertable",null,null,null,null,null,null);   			  
		//�ж��α��Ƿ�Ϊ��   
		myorderID = 0;
		if(cursor.moveToFirst()){   
			//�����α�   
			for(int i=0;i<cursor.getCount();i++){   
				cursor.moveToPosition(i);;   
				int id = cursor.getInt(0);
				if(id > myorderID){
					myorderID = id;
				}   
			}   
		}
		myorderID++;

        db.close();
        
		getNewsInfo();
		
		return_market.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ����MarketActivity�Ľ����(resultCode)���������ڵ�ǰ�������˻�ȥ��Activity
				MarketActivity.this.setResult(1, intent);
				MarketActivity.this.finish();
			}
		});
	}
	
	public void close(){
		if(myorderFlag == 1){
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
			String date = sDateFormat.format(new java.util.Date()); 
			
			myorder = new Order();
			myorder.setID(myorderID);
			myorder.setDate(date);
			myorder.setAddress("������ʡ���������ϸ�����������ѧC��");
			myorder.setTel("13888888888");
			myorder.setOrderList(orderlist);
			myorder.setPaycondition(0);
			float price = 0;
			for(OrderItem oi : orderlist){
				price += oi.getGoodNum()*oi.getGoodPrice();				
			}
			myorder.setPrice(price);
			
			oh = new MyOpenHelper(context, myDB, null, 1);
			//������ݿⲻ���ڣ��ȴ������ٴ򿪣�������ڣ���ֱ�Ӵ�
			db = oh.getWritableDatabase();
				
			ContentValues values = new ContentValues();
			values.put("id", myorder.getID());
			values.put("date", myorder.getDate());
			values.put("address", myorder.getAddress());
			values.put("tel", myorder.getTel());
			values.put("paycondition", myorder.getPaycondition());
			values.put("price", myorder.getPrice() + "");
			long l = db.insert("ordertable", null, values);
			System.out.println(l);
			
			db.close();
			
		}	
		
	}
	
	@Override
	protected void onDestroy() {
		close();
		super.onDestroy();		
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
					AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
					//���ñ���
					builder.setTitle("�Ƿ�Ҫ��"+good.getTitle()+"���빺�ﳵ");
					//Sets a custom view to be the contents of the alert dialog. 
					builder.setView(dialogView);
					
					TextView goods_tv_title = (TextView) dialogView.findViewById(R.id.goods_tv_title);
					TextView goods_tv_detail = (TextView) dialogView.findViewById(R.id.goods_tv_detail);
					TextView goods_tv_jiage = (TextView) dialogView.findViewById(R.id.goods_tv_jiage);
					SmartImageView goods_siv = (SmartImageView) dialogView.findViewById(R.id.goods_siv);
					Button   buy_sub = (Button) dialogView.findViewById(R.id.buy_sub);
					Button   buy_add = (Button) dialogView.findViewById(R.id.buy_add);
					final EditText buy_goods_num = (EditText) dialogView.findViewById(R.id.buy_goods_num);
					goods_tv_title.setText(good.getTitle());
					goods_tv_detail.setText("��Ʒ������ "+good.getDetail());
					goods_tv_jiage.setText("�ۼ�:"+good.getJiage());
					goods_siv.setImageUrl(good.getGoodsURL());
					
					buy_sub.setOnClickListener(new OnClickListener() {						
						@Override
						public void onClick(View v) {
							int num = Integer.parseInt(buy_goods_num.getText()+"");
							num--;
							if(num>=0)
								buy_goods_num.setText(num+"");
							else
								Toast.makeText(context, "��Ʒ��������С��0", 0).show();
						}
					});
					buy_add.setOnClickListener(new OnClickListener() {						
						@Override
						public void onClick(View v) {
							int num = Integer.parseInt(buy_goods_num.getText()+"");
							num++;
							if(num<=Integer.parseInt(good.getNumber()))
								buy_goods_num.setText(num+"");
							else 
								Toast.makeText(context, "�����˸���Ʒ��ʣ����", 0).show();
						}
					});
					
					//����ȷ����ť
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							int num = Integer.parseInt(buy_goods_num.getText()+"");
							if(num>0){
								OrderItem orderItem = new OrderItem();
								orderItem.setShopName(title);
								orderItem.setShopID(shop_id);
								orderItem.setGoodName(good.getTitle());
								orderItem.setGoodID(good.getId());
								orderItem.setGoodNum(num);
								orderItem.setGoodPrice(good.getJiage().substring(1));
								
								orderName = "order_"+myorderID;
								
								oh = new MyOpenHelper(context, myDB, null, 1);
								//������ݿⲻ���ڣ��ȴ������ٴ򿪣�������ڣ���ֱ�Ӵ�
								db = oh.getWritableDatabase();
								
								String sql;
								sql = "create table if not exists "+orderName+"(_id integer primary key autoincrement, shopName char(50), shopID char(10), goodName char(50)"
																			  + ", goodID char(10), goodNum integer(10), goodPrice char(20))";
								db.execSQL(sql);
								
								int flag = 0;
								int goodnum = 0;
								int position = 0;
								//��ѯ����α�   
								Cursor cursor = db.query (orderName,null,null,null,null,null,null);   			  
								//�ж��α��Ƿ�Ϊ��   
								if(cursor.moveToFirst()){   
									//�����α�   
									for(int i=0;i<cursor.getCount();i++){   
										cursor.moveToPosition(i);;   
										String s = cursor.getString(4);
										//System.out.println(s);
										if(good.getId().equals(s)){
											goodnum = cursor.getInt(5);
											position = i;
											System.out.println(s);
											flag = 1;
											break;
										}   
									}   
								}   
								ContentValues values = new ContentValues();
								values.put("shopName", orderItem.getShopName());
								values.put("shopID", orderItem.getShopID());
								values.put("goodName", orderItem.getGoodName());
								values.put("goodID", orderItem.getGoodID());
								values.put("goodNum", orderItem.getGoodNum()+goodnum);
								values.put("goodPrice", orderItem.getGoodPrice()+"");
								if(flag == 0){									
									//����ֵ-1������ʧ��
									long l = db.insert(orderName, null, values);
									orderlist.add(orderItem);				
									System.out.println(l);
								}else if(flag == 1){
									long l = db.update(orderName, values, "goodID = ?", new String[]{orderItem.getGoodID()});
									OrderItem mm = orderlist.get(position);
									mm.setGoodNum(mm.getGoodNum()+goodnum);
									orderlist.set(position, mm);
									System.out.println(l);
								}
								myorderFlag = 1;
								db.close();
							
							}
							//Toast.makeText(context, "������ȷ��", 0).show();
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
					//͸��
					Window window = alertDialog.getWindow(); 
					WindowManager.LayoutParams lp = window.getAttributes();
					lp.alpha = 0.9f;
					window.setAttributes(lp);
					
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
						oh = new MyOpenHelper(context, myDB, null, 1);
						//������ݿⲻ���ڣ��ȴ������ٴ򿪣�������ڣ���ֱ�Ӵ�
						db = oh.getWritableDatabase();
						
						String sql;
						sql = "create table if not exists "+dbTableName+"(_id integer primary key autoincrement, id char(10), title char(50), type char(20), xiaoliang char(20)"
								              + ", number char(20), detail char(200) , jiage char(20), goodURL char(200))";
						db.execSQL(sql);
						
						int flag = 0;
						//��ѯ����α�   
						Cursor cursor = db.query (dbTableName,null,null,null,null,null,null);   			  
						//�ж��α��Ƿ�Ϊ��   
						if(cursor.moveToFirst()){   
							//�����α�   
							for(int i=0;i<cursor.getCount();i++){   
								cursor.moveToPosition(i);;   
								String s = cursor.getString(1);
								if(good.getId().equals(s)){
									System.out.println(s);
									flag = 1;
									break;
								}   
							}   
						}   
						if(flag == 0){
							ContentValues values = new ContentValues();
							values.put("id", good.getId());
							values.put("title", good.getTitle());
							values.put("type", good.getType());
							values.put("xiaoliang", good.getXiaoliang());
							values.put("number", good.getNumber());
							values.put("detail", good.getDetail());
							values.put("jiage", good.getJiage());
							values.put("goodURL", good.getGoodsURL());
							//����ֵ-1������ʧ��
							long l = db.insert(dbTableName, null, values);
							System.out.println(l);
						}
//						sql = "select max(_id) as md from ordertable";
//						Cursor cr = db.rawQuery(sql, null);
//						myorderID = cr.getInt(cr.getColumnIndex("md")) + 1;
						db.close();
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
