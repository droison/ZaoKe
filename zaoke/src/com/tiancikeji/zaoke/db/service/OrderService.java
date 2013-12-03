package com.tiancikeji.zaoke.db.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.tiancikeji.zaoke.db.DBHelper;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.base.Dborder;

public class OrderService {

	private DBHelper dbOpenHelper;

	public OrderService(Context context) {
		DBHelper.init(context);
		this.dbOpenHelper = DBHelper.dbHelper();
	}

	public void save(Dborder order) {

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		byte[] qx = null;
		if (order.getQxImg() != null) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Bitmap qxImg = BitmapFactory.decodeByteArray(order.getQxImg(), 0, order.getQxImg().length);
			qxImg.compress(CompressFormat.PNG, 100, os);
			qx = os.toByteArray();
		}

		db.execSQL("insert into myorder(" 
		             + "orderId, shiwuName, shiwuId, shiwuPrice, shiwuUrl, "
				     + "yinliaoName, yinliaoId, yinliaoPrice, yinliaoUrl, combPrice,"
		             + "ordertime,locId,starttime,endtime,local,userid,qx_img,paymode) "
				     + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 
				     new Object[] { order.getOrderId(), order.getShiwuName(), order.getShiwuId(), order.getShiwuPrice(), order.getShiwuUrl(), 
		            		 order.getYinliaoName(), order.getYinliaoId(), order.getYinliaoPrice(), order.getYinliaoUrl(), order.getCombPrice(), 
		            		 order.getOrdertime(), order.getLocId(), order.getStarttime(), order.getEndtime(), order.getLocal(),order.getUserid(), qx, order.getPaymode() });

		dbOpenHelper.close();
	}

	public Dborder getLast() {
		Drawable qxImg = null;
		Dborder order = new Dborder();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		long ordertime = System.currentTimeMillis();
		Cursor cursor = db.rawQuery("select * from myorder where ordertime<? order by ordertime desc", new String[] { String.valueOf(ordertime) });
		if (cursor.moveToFirst()) {

			order.setOrderId(cursor.getString(1));

			order.setShiwuName(cursor.getString(2));
			order.setShiwuId(cursor.getInt(3));
			order.setShiwuPrice(cursor.getDouble(4));
			order.setShiwuUrl(cursor.getString(5));

			order.setYinliaoName(cursor.getString(6));
			order.setYinliaoId(cursor.getInt(7));
			order.setYinliaoPrice(cursor.getDouble(8));
			order.setYinliaoUrl(cursor.getString(9));

			order.setCombPrice(cursor.getDouble(10));
			order.setOrdertime(cursor.getLong(11));
			order.setLocId(cursor.getInt(12));
			order.setStarttime(cursor.getString(13));
			order.setEndtime(cursor.getString(14));
			order.setLocal(cursor.getString(15));
			order.setUserid(cursor.getString(16));
			order.setPaymode(cursor.getInt(18));
			if (cursor.getBlob(17) != null) {
				order.setQxImg(cursor.getBlob(17));
			}
			

		} else {
			order = null;
		}
		cursor.close();
		dbOpenHelper.close();
		return order;
	}

	public Dborder getOne(String orderId) {
		Drawable qxImg = null;
		Dborder order = new Dborder();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from myorder where orderId = ? order by ordertime desc", new String[] { orderId });
		if (cursor.moveToFirst()) {

			order.setOrderId(cursor.getString(1));

			order.setShiwuName(cursor.getString(2));
			order.setShiwuId(cursor.getInt(3));
			order.setShiwuPrice(cursor.getDouble(4));
			order.setShiwuUrl(cursor.getString(5));

			order.setYinliaoName(cursor.getString(6));
			order.setYinliaoId(cursor.getInt(7));
			order.setYinliaoPrice(cursor.getDouble(8));
			order.setYinliaoUrl(cursor.getString(9));

			order.setCombPrice(cursor.getDouble(10));
			order.setOrdertime(cursor.getLong(11));
			order.setLocId(cursor.getInt(12));
			order.setStarttime(cursor.getString(13));
			order.setEndtime(cursor.getString(14));
			order.setLocal(cursor.getString(15));
			order.setUserid(cursor.getString(16));
			order.setPaymode(cursor.getInt(18));
			if (cursor.getBlob(17) != null) {
				order.setQxImg(cursor.getBlob(8));
			}

		} else {
			order = null;
		}
		cursor.close();
		dbOpenHelper.close();
		return order;
	}

	public Dborder getLast(long ordertime) {
		Drawable qxImg = null;
		Dborder order = new Dborder();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from myorder where ordertime < ? order by ordertime desc", new String[] { String.valueOf(ordertime) });
		if (cursor.moveToFirst()) {

			order.setOrderId(cursor.getString(1));

			order.setShiwuName(cursor.getString(2));
			order.setShiwuId(cursor.getInt(3));
			order.setShiwuPrice(cursor.getDouble(4));
			order.setShiwuUrl(cursor.getString(5));

			order.setYinliaoName(cursor.getString(6));
			order.setYinliaoId(cursor.getInt(7));
			order.setYinliaoPrice(cursor.getDouble(8));
			order.setYinliaoUrl(cursor.getString(9));

			order.setCombPrice(cursor.getDouble(10));
			order.setOrdertime(cursor.getLong(11));
			order.setLocId(cursor.getInt(12));
			order.setStarttime(cursor.getString(13));
			order.setEndtime(cursor.getString(14));
			order.setLocal(cursor.getString(15));
			order.setUserid(cursor.getString(16));
			order.setPaymode(cursor.getInt(18));
			if (cursor.getBlob(17) != null) {
				order.setQxImg(cursor.getBlob(8));
			}

		} else {
			order = null;
		}
		cursor.close();
		dbOpenHelper.close();
		return order;
	}
	
	public List<Dborder> getLastAll() {
		Drawable qxImg = null;
		List<Dborder> orders = new ArrayList<Dborder>();
		
		long curDate = System.currentTimeMillis();
		long time = curDate-curDate%(24*60*60*1000);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from myorder where ordertime>? order by ordertime desc", new String[] { String.valueOf(time)});
		while(cursor.moveToNext()){
			Dborder order = new Dborder();
			order.setOrderId(cursor.getString(1));
			order.setShiwuName(cursor.getString(2));
			order.setShiwuId(cursor.getInt(3));
			order.setShiwuPrice(cursor.getDouble(4));
			order.setShiwuUrl(cursor.getString(5));

			order.setYinliaoName(cursor.getString(6));
			order.setYinliaoId(cursor.getInt(7));
			order.setYinliaoPrice(cursor.getDouble(8));
			order.setYinliaoUrl(cursor.getString(9));

			order.setCombPrice(cursor.getDouble(10));
			order.setOrdertime(cursor.getLong(11));
			order.setLocId(cursor.getInt(12));
			order.setStarttime(cursor.getString(13));
			order.setEndtime(cursor.getString(14));
			order.setLocal(cursor.getString(15));
			order.setUserid(cursor.getString(16));
			order.setPaymode(cursor.getInt(18));
			if (cursor.getBlob(17) != null) {
				order.setQxImg(cursor.getBlob(17));
			}
			orders.add(order);
		}
		cursor.close();
		dbOpenHelper.close();
		return orders;
	}
	
	
	

	public void delete(String orderId) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from myorder where orderId=?", new String[] { orderId });
		dbOpenHelper.close();
	}

}
