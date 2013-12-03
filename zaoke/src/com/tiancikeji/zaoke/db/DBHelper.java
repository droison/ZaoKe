package com.tiancikeji.zaoke.db;

import java.util.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static DBHelper dbHelper;

	private int openedConnections = 0;

	public synchronized SQLiteDatabase getReadableDatabase() {
		openedConnections++;
		return super.getReadableDatabase();
	}

	public synchronized SQLiteDatabase getWritableDatabase() {
		openedConnections++;
		return super.getWritableDatabase();
	}

	public synchronized void close() {
		openedConnections--;
		if (openedConnections == 0) {
			super.close();
		}
	}

	public static DBHelper dbHelper() {
		return dbHelper;
	}

	public static void init(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}
	}

	private DBHelper(Context context) {
		super(context, "account.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS first_login ( _id integer primary key, activityname varchar(20), value varchar(20))");
		db.execSQL("CREATE TABLE IF NOT EXISTS  account ( _id integer primary key, name varchar(100), userid varchar(20), ticket varchar(100), phone varchar(20), password varchar(100), vipid varchar(100), balance double, qx_img BLOB, local varchar(20),starttime varchar(20),endtime varchar(20),locid integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS myorder ( _id integer primary key AUTOINCREMENT, orderId long, " + "shiwuName varchar(20),shiwuId integer,shiwuPrice double,shiwuUrl varchar(100)," + "yinliaoName varchar(20),yinliaoId integer,yinliaoPrice double,yinliaoUrl varchar(20)," + "combPrice double,ordertime long," + "locId integer,starttime varchar(20),endtime varchar(20),local varchar(20),userid varchar(20),qx_img BLOB,paymode int)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF first_login");
		db.execSQL("DROP TABLE IF account");
		db.execSQL("DROP TABLE IF order");
		onCreate(db);
	}

}
