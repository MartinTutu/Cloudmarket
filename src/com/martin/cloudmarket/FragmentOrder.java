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
	//ϵͳ�Զ����ã����ص�View������ΪFragment��������ʾ
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
		//������ݿⲻ���ڣ��ȴ������ٴ򿪣�������ڣ���ֱ�Ӵ�
		db = oh.getWritableDatabase();
		//arg1: ��ѯ���ֶ�
		//arg2: ��ѯ��where����
		//arg3: where������ռλ��
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
		//���ص�Ҫ��ʾ����Ŀ������
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orderList.size();
		}
		
		//����һ��View���󣬻���ΪListView��һ����Ŀ��ʾ�ڽ�����
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final Order order = orderList.get(position);
			
			View v = null;
			final ViewHolder mHolder;
			if(convertView == null){
				v = View.inflate(content, R.layout.order_item, null);
				
				//����viewHoler��װ������Ŀʹ�õ����
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
				
				//��viewHolder��װ��view�����У�����view������ʱ��viewHolderҲ�ͱ�������
				v.setTag(mHolder);
			}
			else{
				v = convertView;
				//��view��ȡ�������viewHolder��viewHolder�о������е�������󣬲���Ҫ��ȥfindViewById
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
			
			//����Ŀ�е�ÿ���������Ҫ��ʾ������
			mHolder.order_tv_num.setText("�����ţ�"+order.getID());
			mHolder.order_tv_date.setText("�µ�ʱ�䣺"+order.getDate());
			mHolder.order_tv_title.setText(title);
			
			orderItemList = order.getOrderList();
			mHolder.lv_order_item.setAdapter(new OrderItemAdapter());
			setListViewHeightBasedOnChildren(mHolder.lv_order_item);
			
			mHolder.order_tv_address.setText("��ַ��"+order.getAddress());
			mHolder.order_tv_tel.setText("�绰��"+order.getTel());
			mHolder.order_tv_price.setText("�ܼۣ���"+order.getPrice());
			if(order.getPaycondition() == 0){
				mHolder.order_tv_paystation.setText("֧��״̬��δ֧��");
			}else if(order.getPaycondition() == 1){
				mHolder.order_tv_paystation.setText("֧��״̬����֧��");
			}
			mHolder.order_siv.setImageUrl(logoURL);
						
			mHolder.order_bt_pay.setFocusable(false);
			mHolder.order_bt_cancel.setFocusable(false);
			
			final String shop_title = title;
			//���ü���
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
					//�����Ի��򴴽���
					AlertDialog.Builder builder = new AlertDialog.Builder(content,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
					//���ñ���
					builder.setTitle("�޸Ķ�����ַ�붩���绰");
					//Sets a custom view to be the contents of the alert dialog. 
					builder.setView(dialogView);
					
					final EditText address = (EditText) dialogView.findViewById(R.id.modify_ed_address);
					final EditText tel     = (EditText) dialogView.findViewById(R.id.modify_ed_tel);
					address.setText(order.getAddress());
					tel.setText(order.getTel());
					//����ȷ����ť
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Toast.makeText(context, "������ȷ��", 0).show();
							String myadr = address.getText().toString();
							String mytel = tel.getText().toString();
							oh = new MyOpenHelper(content, myDB, null, 1);
							db = oh.getWritableDatabase();
							
							ContentValues values = new ContentValues();
							values.put("address", myadr);
							values.put("tel", mytel);
							db.update("ordertable", values, "id = ?", new String[]{order.getID()});
							db.close();
							mHolder.order_tv_address.setText("��ַ��"+myadr);
							mHolder.order_tv_tel.setText("�绰��"+mytel);
							//showOrder();
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
			mHolder.order_tv_address.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Obtains the LayoutInflater from the given context.
					LayoutInflater inflater = LayoutInflater.from(content);	
					View dialogView = inflater.inflate(R.layout.modify_dialog, null);
					//�����Ի��򴴽���
					AlertDialog.Builder builder = new AlertDialog.Builder(content,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
					//���ñ���
					builder.setTitle("�޸Ķ�����ַ�붩���绰");
					//Sets a custom view to be the contents of the alert dialog. 
					builder.setView(dialogView);
					
					final EditText address = (EditText) dialogView.findViewById(R.id.modify_ed_address);
					final EditText tel     = (EditText) dialogView.findViewById(R.id.modify_ed_tel);
					address.setText(order.getAddress());
					tel.setText(order.getTel());
					//����ȷ����ť
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Toast.makeText(context, "������ȷ��", 0).show();
							String myadr = address.getText().toString();
							String mytel = tel.getText().toString();
							oh = new MyOpenHelper(content, myDB, null, 1);
							db = oh.getWritableDatabase();
							
							ContentValues values = new ContentValues();
							values.put("address", myadr);
							values.put("tel", mytel);
							db.update("ordertable", values, "id = ?", new String[]{order.getID()});
							db.close();
							mHolder.order_tv_address.setText("��ַ��"+myadr);
							mHolder.order_tv_tel.setText("�绰��"+mytel);
							//showOrder();
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
	
	 // listview�е�����������Ի���  
    public void showInfo(final int position,final String tableName,final Order order) {  
        new AlertDialog.Builder(content).setTitle("�ҵ���ʾ").setMessage("ȷ��Ҫɾ����")                  
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
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
		//���ص�Ҫ��ʾ����Ŀ������
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orderItemList.size();
		}
		
		//����һ��View���󣬻���ΪListView��һ����Ŀ��ʾ�ڽ�����
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final OrderItem orderItem = orderItemList.get(position);
			
			View v = null;
			ViewHolder mHolder = null;
			if(convertView == null){
				v = View.inflate(content, R.layout.order_goods_item, null);
				
				//����viewHoler��װ������Ŀʹ�õ����
				mHolder = new ViewHolder();
				mHolder.order_goods_tv_title = (TextView) v.findViewById(R.id.order_goods_tv_title);
				mHolder.order_goods_tv_detail = (TextView) v.findViewById(R.id.order_goods_tv_detail);
				mHolder.order_goods_tv_jiage = (TextView) v.findViewById(R.id.order_goods_tv_jiage);
				mHolder.order_goods_tv_number = (TextView) v.findViewById(R.id.order_goods_tv_number);
				mHolder.order_goods_siv = (SmartImageView) v.findViewById(R.id.order_goods_siv);
				
				//��viewHolder��װ��view�����У�����view������ʱ��viewHolderҲ�ͱ�������
				v.setTag(mHolder);
			}
			else{
				v = convertView;
				//��view��ȡ�������viewHolder��viewHolder�о������е�������󣬲���Ҫ��ȥfindViewById
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
			
			//����Ŀ�е�ÿ���������Ҫ��ʾ������
			mHolder.order_goods_tv_title.setText(orderItem.getGoodName());
			mHolder.order_goods_tv_detail.setText("��Ʒ������"+detail);
			mHolder.order_goods_tv_jiage.setText("���ۣ�"+orderItem.getGoodPrice());
			mHolder.order_goods_tv_number.setText("�ѹ���"+orderItem.getGoodNum()+"��");
			 
			mHolder.order_goods_siv.setImageUrl(goodURL);
						
			//���ü���
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
//					Toast.makeText(context,"����"+shop.getId(),Toast.LENGTH_LONG).show();
//				}
//			});
			return v;
		}
		
		//����Ŀ��Ҫʹ�õ������������װ���������
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
