package com.tiancikeji.zaoke.db.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
import com.tiancikeji.zaoke.util.NumberFormateUtil;

public class AccountService {

	private DBHelper dbOpenHelper;

	public AccountService(Context context) {
		DBHelper.init(context);
		this.dbOpenHelper = DBHelper.dbHelper();
	}

	private void save(Dbaccount account) {

		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		byte[] qx = account.getQxImg();

		db.execSQL("insert into account(_id, name, userid, ticket, phone, password, vipid, balance, qx_img, local,starttime,endtime,locid) values(?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[] { 2013, account.getName(), account.getUserid(), account.getTicket(), account.getPhone(), account.getPassword(), account.getVipid(), account.getBalance(), qx, account.getLocal(), account.getStarttime(), account.getEndtime(), account.getLocid() });

		dbOpenHelper.close();
	}

	public void saveOrUpdate(Dbaccount account) {
		if (isExist()) {
			if (isExist(account.getName(), account.getUserid(), account.getTicket())) {
				update(account);
			} else {
				delete();
				save(account);
			}
		} else {
			save(account);
		}
	}

	public Dbaccount getAccount() {
		Drawable qxImg = null;
		Dbaccount account = new Dbaccount();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from account where _id = ?", new String[] { "2013" });
		if (cursor.moveToFirst()) {

			account.setId(cursor.getInt(0));
			account.setName(cursor.getString(1));
			account.setUserid(cursor.getString(2));
			account.setTicket(cursor.getString(3));
			account.setPhone(cursor.getString(4));
			account.setPassword(cursor.getString(5));
			account.setVipid(cursor.getString(6));
			account.setBalance(NumberFormateUtil.formate2(cursor.getDouble(7)));
			account.setLocal(cursor.getString(9));
			account.setStarttime(cursor.getString(10));
			account.setEndtime(cursor.getString(11));
			account.setLocid(cursor.getInt(12));
			if (cursor.getBlob(8) != null) {
				account.setQxImg(cursor.getBlob(8));
			}
			account.setQxImg(cursor.getBlob(8));

		} else {
			account = null;
		}
		cursor.close();
		dbOpenHelper.close();
		return account;
	}

	public boolean isExist() {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from account where _id=?", new String[] { "2013" });
		flag = cursor.moveToFirst();
		cursor.close();
		dbOpenHelper.close();
		return flag;
	}

	public boolean isExist(String name, String userid, String ticket) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from account where _id=? and name=? and userid=? and ticket=?", new String[] { "2013", name, userid, ticket });
		flag = cursor.moveToFirst();
		cursor.close();
		dbOpenHelper.close();
		return flag;
	}

	private void update(Dbaccount account) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("phone", account.getPhone());
		cv.put("password", account.getPassword());
		cv.put("vipid", account.getVipid());
		cv.put("balance", account.getBalance());
		cv.put("local", account.getLocal());
		cv.put("starttime", account.getStarttime());
		cv.put("endtime", account.getEndtime());
		cv.put("locid", account.getLocid());

		if (account.getQxImg() != null) {
			cv.put("qx_img", account.getQxImg());
		}

		db.update("account", cv, "_id=?", new String[] { "2013" });
		dbOpenHelper.close();
	}

	public void delete() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from account where _id=2013");
		dbOpenHelper.close();
	}

}
