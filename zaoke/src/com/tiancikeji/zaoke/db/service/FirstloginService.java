package com.tiancikeji.zaoke.db.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.ContentValues;
import com.tiancikeji.zaoke.db.DBHelper;

public class FirstloginService {

	private DBHelper dbOpenHelper;

	public FirstloginService(Context context) {
		DBHelper.init(context);
		this.dbOpenHelper = DBHelper.dbHelper();
	}

	public void save(String activityname) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (isExist(activityname)) {

			update(activityname);
		} else {
			db.execSQL("insert into first_login(activityname) values(?)", new Object[] { activityname });
		}
		dbOpenHelper.close();
	}

	public void save(String activityname, String value) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		if (isExist(activityname)) {

			update(activityname, value);
		} else {
			db.execSQL("insert into first_login( activityname, value) values(?,?)", new Object[] { activityname, value });
		}
		dbOpenHelper.close();
	}

	public boolean isExist(String activityname) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select _id from first_login where activityname=?", new String[] { activityname });
		flag = cursor.moveToFirst();
		cursor.close();
		dbOpenHelper.close();
		return flag;
	}

	public void update(String activityname, String value) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("value", value);
		db.update("first_login", cv, "activityname=?", new String[] { activityname });
		dbOpenHelper.close();
	}

	public void update(String activityname) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("value", "");
		db.update("first_login", cv, "activityname=?", new String[] { activityname });
		dbOpenHelper.close();
	}

	public void delete() {

	}

}
