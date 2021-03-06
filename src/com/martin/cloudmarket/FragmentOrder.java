package com.martin.cloudmarket;

import java.util.ArrayList;
import java.util.List;

import com.example.fpay_foruser.Activity_4user;
import com.loopj.android.image.SmartImageView;
import com.martin.cloudmarket.database.MyOpenHelper;
import com.martin.cloudmarket.demo.Order;
import com.martin.cloudmarket.demo.OrderItem;
import com.martin.cloudmarket.demo.PayMsg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentOrder extends Fragment {
	
	private Activity content = null;
	private View v;
	
	private MyOpenHelper oh;
	private String  myDB = "couldmarket.db";
	private SQLiteDatabase db;
	
	private List<Order> orderList;
	private List<OrderItem> orderItemList;
	
	public FragmentOrder(Activity content) {
		super();
		this.content = content;
	}
	public FragmentOrder(Activity content,String ordernum) {
		super();
		this.content = content;
		oh = new MyOpenHelper(content, myDB, null, 1);
		db = oh.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("paycondition", "1");
		db.update("ordertable", values, "id = ?", new String[]{ordernum});
		db.close();
	}
	//系统自动调用，返回的View对象作为Fragment的内容显示
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.fragment_order, null);
		initMyOrder();
		showOrder();
		return v;
	}
	
	private void initMyOrder() {
		orderList = new ArrayList<Order>();
		oh = new MyOpenHelper(content, myDB, null, 1);
		//如果数据库不存在，先创建，再打开，如果存在，就直接打开
		db = oh.getWritableDatabase();
		//arg1: 查询的字段
		//arg2: 查询的where条件
		//arg3: where条件的占位符
		Cursor cursor = db.query("ordertable", null, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			Order myOrder = new Order();
			myOrder.setID(cursor.getString(1));
			myOrder.setDate(cursor.getString(2));
			myOrder.setAddress(cursor.getString(3));
			myOrder.setTel(cursor.getString(4));
			myOrder.setPaycondition(cursor.getInt(5));
			myOrder.setPrice(cursor.getString(6));
			processOrder(myOrder);
			orderList.add(myOrder);
		}
		db.close();
	}
	
	private void showOrder() {
		ListView order_lv = (ListView) v.findViewById(R.id.lv_order_item);
		order_lv.setAdapter(new MyAdapter());
		setListViewHeightBasedOnChildren(order_lv);
	}

	private void processOrder(Order myOrder) {
		String tableName = "order_"+myOrder.getID();
		List<OrderItem> myorderList = new ArrayList<OrderItem>();		
		Cursor cs = db.query(tableName, null, null, null, null, null, null, null);
		while(cs.moveToNext()){
			OrderItem myorderItem = new OrderItem();
			myorderItem.setOrderID(myOrder.getID());
			myorderItem.setShopName(cs.getString(1));
			myorderItem.setShopID(cs.getString(2));
			myorderItem.setGoodName(cs.getString(3));
			myorderItem.setGoodID(cs.getString(4));
			myorderItem.setGoodNum(cs.getInt(5));
			myorderItem.setGoodPrice(cs.getString(6));
			myorderList.add(myorderItem);
		}
		myOrder.setOrderList(myorderList);
	}
	
	class MyAdapter extends BaseAdapter{
		//返回的要显示的条目的数量
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orderList.size();
		}
		
		//返回一个View对象，会作为ListView的一个条目显示在界面上
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final Order order = orderList.get(position);
			
			View v = null;
			final ViewHolder mHolder;
			if(convertView == null){
				v = View.inflate(content, R.layout.order_item, null);
				
				//创建viewHoler封装所有条目使用的组件
				mHolder = new ViewHolder();
				mHolder.order_tv_num = (TextView) v.findViewById(R.id.order_tv_number);
				mHolder.order_tv_date = (TextView) v.findViewById(R.id.order_tv_date);
				mHolder.order_tv_title = (TextView) v.findViewById(R.id.order_tv_title);
				mHolder.lv_order_item = (ListView) v.findViewById(R.id.lv_order_item);
				mHolder.order_tv_address = (TextView) v.findViewById(R.id.order_tv_address);
				mHolder.order_tv_tel = (TextView) v.findViewById(R.id.order_tv_tel);
				mHolder.order_tv_price = (TextView) v.findViewById(R.id.order_tv_price);
				mHolder.order_tv_paystation = (TextView) v.findViewById(R.id.order_tv_paystation);
				mHolder.order_siv = (SmartImageView) v.findViewById(R.id.order_siv);
				mHolder.order_bt_pay = (Button) v.findViewById(R.id.order_bt_pay);
				mHolder.order_bt_cancel = (Button) v.findViewById(R.id.order_bt_cancel);
				
				//把viewHolder封装至view对象中，这样view被缓存时，viewHolder也就被缓存了
				v.setTag(mHolder);
			}
			else{
				v = convertView;
				//从view中取出保存的viewHolder，viewHolder中就有所有的组件对象，不需要再去findViewById
				mHolder = (ViewHolder) v.getTag();
			}
			
			oh = new MyOpenHelper(content, myDB, null, 1);
			db = oh.getWritableDatabase();
			final String tableName = "order_"+order.getID();
			Cursor cs = db.query(tableName, null, null, null, null, null, null, null);
			cs.moveToNext();
			String shopID = cs.getString(2);
			Cursor cursor = db.query("shop", new String[]{"title","logoURL"}, "id=?", new String[]{shopID}, null, null, null); 
			String title = null;
			String logoURL = null;
			while(cursor.moveToNext()){  
				title = cursor.getString(cursor.getColumnIndex("title"));  
				logoURL = cursor.getString(cursor.getColumnIndex("logoURL"));  
			} 
			db.close();
			
			//给条目中的每个组件设置要显示的内容
			mHolder.order_tv_num.setText("订单号："+order.getID());
			mHolder.order_tv_date.setText("下单时间："+order.getDate());
			mHolder.order_tv_title.setText(title);
			
			orderItemList = order.getOrderList();
			mHolder.lv_order_item.setAdapter(new OrderItemAdapter());
			setListViewHeightBasedOnChildren(mHolder.lv_order_item);
			
			mHolder.order_tv_address.setText("地址："+order.getAddress());
			mHolder.order_tv_tel.setText("电话："+order.getTel());
			mHolder.order_tv_price.setText("总价：￥"+order.getPrice());
			if(order.getPaycondition() == 0){
				mHolder.order_tv_paystation.setText("支付状态：未支付");
			}else if(order.getPaycondition() == 1){
				mHolder.order_tv_paystation.setText("支付状态：已支付");
			}
			mHolder.order_siv.setImageUrl(logoURL);
						
			mHolder.order_bt_pay.setFocusable(false);
			mHolder.order_bt_cancel.setFocusable(false);
			
			final String shop_title = title;
			//设置监听
			mHolder.order_bt_pay.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					//Toast.makeText(content, "pay", Toast.LENGTH_SHORT).show();
					PayMsg message = new PayMsg();
					message.setOrdernum(order.getID());
					message.setUsername(MainActivity.username);
					message.setShopname(shop_title);
					message.setPrice(order.getPrice()+"");
					Intent intent = new Intent(content, Activity_4user.class);
					intent.putExtra("msg", message.toString());
					content.startActivityForResult(intent, 3);
				}
			});
			mHolder.order_bt_cancel.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					
					//Toast.makeText(content, "cancel", Toast.LENGTH_SHORT).show();			
					showInfo(position,tableName,order);
				}
			});
			mHolder.order_tv_tel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Obtains the LayoutInflater from the given context.
					LayoutInflater inflater = LayoutInflater.from(content);	
					View dialogView = inflater.inflate(R.layout.modify_dialog, null);
					//创建对话框创建器
					AlertDialog.Builder builder = new AlertDialog.Builder(content,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
					//设置标题
					builder.setTitle("修改订单地址与订单电话");
					//Sets a custom view to be the contents of the alert dialog. 
					builder.setView(dialogView);
					
					final EditText address = (EditText) dialogView.findViewById(R.id.modify_ed_address);
					final EditText tel     = (EditText) dialogView.findViewById(R.id.modify_ed_tel);
					address.setText(order.getAddress());
					tel.setText(order.getTel());
					//设置确定按钮
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Toast.makeText(context, "你点击了确定", 0).show();
							String myadr = address.getText().toString();
							String mytel = tel.getText().toString();
							oh = new MyOpenHelper(content, myDB, null, 1);
							db = oh.getWritableDatabase();
							
							ContentValues values = new ContentValues();
							values.put("address", myadr);
							values.put("tel", mytel);
							db.update("ordertable", values, "id = ?", new String[]{order.getID()});
							db.close();
							mHolder.order_tv_address.setText("地址："+myadr);
							mHolder.order_tv_tel.setText("电话："+mytel);
							//showOrder();
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
					//透明
					Window window = alertDialog.getWindow(); 
					WindowManager.LayoutParams lp = window.getAttributes();
					lp.alpha = 0.9f;
					window.setAttributes(lp);
					
					alertDialog.show();
				}
			});
			mHolder.order_tv_address.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Obtains the LayoutInflater from the given context.
					LayoutInflater inflater = LayoutInflater.from(content);	
					View dialogView = inflater.inflate(R.layout.modify_dialog, null);
					//创建对话框创建器
					AlertDialog.Builder builder = new AlertDialog.Builder(content,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
					//设置标题
					builder.setTitle("修改订单地址与订单电话");
					//Sets a custom view to be the contents of the alert dialog. 
					builder.setView(dialogView);
					
					final EditText address = (EditText) dialogView.findViewById(R.id.modify_ed_address);
					final EditText tel     = (EditText) dialogView.findViewById(R.id.modify_ed_tel);
					address.setText(order.getAddress());
					tel.setText(order.getTel());
					//设置确定按钮
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Toast.makeText(context, "你点击了确定", 0).show();
							String myadr = address.getText().toString();
							String mytel = tel.getText().toString();
							oh = new MyOpenHelper(content, myDB, null, 1);
							db = oh.getWritableDatabase();
							
							ContentValues values = new ContentValues();
							values.put("address", myadr);
							values.put("tel", mytel);
							db.update("ordertable", values, "id = ?", new String[]{order.getID()});
							db.close();
							mHolder.order_tv_address.setText("地址："+myadr);
							mHolder.order_tv_tel.setText("电话："+mytel);
							//showOrder();
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
					//透明
					Window window = alertDialog.getWindow(); 
					WindowManager.LayoutParams lp = window.getAttributes();
					lp.alpha = 0.9f;
					window.setAttributes(lp);
					
					alertDialog.show();
				}
			});
			return v;
		}
		
		//把条目需要使用到的所有组件封装在这个类中
		class ViewHolder{
			TextView order_tv_num;
			TextView order_tv_date;
			TextView order_tv_title;
			ListView lv_order_item;
			TextView order_tv_address;
			TextView order_tv_tel;
			TextView order_tv_price;
			TextView order_tv_paystation;
			SmartImageView order_siv;
			Button   order_bt_pay;
			Button   order_bt_cancel;
		}

		@Override
		public Object getItem(int position) {
			return orderList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}
	
	 // listview中点击按键弹出对话框  
    public void showInfo(final int position,final String tableName,final Order order) {  
        new AlertDialog.Builder(content).setTitle("我的提示").setMessage("确定要删除吗？")                  
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {
                    	oh = new MyOpenHelper(content, myDB, null, 1);
    					db = oh.getWritableDatabase();
    					db.delete("ordertable", "id = ?", new String[]{order.getID()});
    					db.delete(tableName, null, null);
    					db.close();
                    	orderList.remove(position);  
                    	showOrder();  
                    }  
                }).show();  
    }  
	
	class OrderItemAdapter extends BaseAdapter{
		//返回的要显示的条目的数量
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orderItemList.size();
		}
		
		//返回一个View对象，会作为ListView的一个条目显示在界面上
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final OrderItem orderItem = orderItemList.get(position);
			
			View v = null;
			ViewHolder mHolder = null;
			if(convertView == null){
				v = View.inflate(content, R.layout.order_goods_item, null);
				
				//创建viewHoler封装所有条目使用的组件
				mHolder = new ViewHolder();
				mHolder.order_goods_tv_title = (TextView) v.findViewById(R.id.order_goods_tv_title);
				mHolder.order_goods_tv_detail = (TextView) v.findViewById(R.id.order_goods_tv_detail);
				mHolder.order_goods_tv_jiage = (TextView) v.findViewById(R.id.order_goods_tv_jiage);
				mHolder.order_goods_tv_number = (TextView) v.findViewById(R.id.order_goods_tv_number);
				mHolder.order_goods_siv = (SmartImageView) v.findViewById(R.id.order_goods_siv);
				
				//把viewHolder封装至view对象中，这样view被缓存时，viewHolder也就被缓存了
				v.setTag(mHolder);
			}
			else{
				v = convertView;
				//从view中取出保存的viewHolder，viewHolder中就有所有的组件对象，不需要再去findViewById
				mHolder = (ViewHolder) v.getTag();
			}
			
			oh = new MyOpenHelper(content, myDB, null, 1);
			db = oh.getWritableDatabase();
			String tableName = "shop_"+orderItem.getShopID();
			Cursor cursor = db.query(tableName, new String[]{"detail","goodURL"}, "id=?", new String[]{orderItem.getGoodID()}, null, null, null); 
			String detail = null;
			String goodURL = null;
			while(cursor.moveToNext()){  
				detail = cursor.getString(cursor.getColumnIndex("detail"));  
				goodURL = cursor.getString(cursor.getColumnIndex("goodURL"));  
			} 
			db.close();
			
			//给条目中的每个组件设置要显示的内容
			mHolder.order_goods_tv_title.setText(orderItem.getGoodName());
			mHolder.order_goods_tv_detail.setText("商品描述："+detail);
			mHolder.order_goods_tv_jiage.setText("单价："+orderItem.getGoodPrice());
			mHolder.order_goods_tv_number.setText("已购："+orderItem.getGoodNum()+"件");
			 
			mHolder.order_goods_siv.setImageUrl(goodURL);
						
			//设置监听
//			v.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent();
//					intent.setClass(context, MarketActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putString("ID", shop.getId());
//					bundle.putString("Title", shop.getTitle());
//					intent.putExtras(bundle);
//					startActivityForResult(intent, 1);
//					Toast.makeText(context,"点了"+shop.getId(),Toast.LENGTH_LONG).show();
//				}
//			});
			return v;
		}
		
		//把条目需要使用到的所有组件封装在这个类中
		class ViewHolder{
			TextView order_goods_tv_title;
			TextView order_goods_tv_detail;
			TextView order_goods_tv_jiage;
			TextView order_goods_tv_number;
			SmartImageView order_goods_siv;
		}

		@Override
		public Object getItem(int position) {
			return orderItemList.get(position);
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
