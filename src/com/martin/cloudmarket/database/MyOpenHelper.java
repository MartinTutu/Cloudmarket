package com.martin.cloudmarket.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

	
	public MyOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	//创建数据库时调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql;
		sql = "create table shop(_id integer primary key autoincrement, id char(10), title char(50), type char(20), mark char(20)"
				              + ", tradingarea char(20), qisong char(20) , distance char(20), logoURL char(200))";
		db.execSQL(sql);
		sql = "create table ordertable(_id integer primary key autoincrement, id char(20), date char(50), address char(200)"
							  + ", tel char(10), paycondition integer(10), price char(20))";
		db.execSQL(sql);
	}

	//升级数据库时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
