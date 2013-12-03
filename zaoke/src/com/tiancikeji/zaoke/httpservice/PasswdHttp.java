package com.tiancikeji.zaoke.httpservice;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.httpservice.base.BindCardBase;
import com.tiancikeji.zaoke.httpservice.base.BuyInfoBase;
import com.tiancikeji.zaoke.httpservice.base.HttpResponseEntity;
import com.tiancikeji.zaoke.httpservice.base.PasswdBase;
import com.tiancikeji.zaoke.util.JsonUtil;
import com.tiancikeji.zaoke.util.MD5Util;
import com.tiancikeji.zaoke.util.StringUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class PasswdHttp implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;

	public PasswdHttp(Context context, Handler mHandler,  String userid, String ticket, String old, String new1, String new2) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.passWd +  "?userid=" + userid + "&ticket=" + ticket + "&old=" + MD5Util.MD5(old) + "&new1=" + MD5Util.MD5(new1) + "&new2=" + MD5Util.MD5(new2);
	}

	public void run() {

		Boolean b = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			b = networkInfo.isAvailable();
		}
		if (!b) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}
		HttpResponseEntity hre = HTTP.get(url);
		switch (hre.getHttpResponseCode()) {
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				
				PasswdBase passwdBase = (PasswdBase) JsonUtil.jsonToObject(json, PasswdBase.class);
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, passwdBase));
			} catch (Exception e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
				Log.e("StringGet", "200", e);
			}
			break;
		default:
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			Log.d("StringGet", "" + hre.getHttpResponseCode());
			break;
		}
	}

}
