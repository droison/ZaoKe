package com.tiancikeji.zaoke.httpservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.httpservice.base.AutoRegBase;
import com.tiancikeji.zaoke.httpservice.base.HttpResponseEntity;
import com.tiancikeji.zaoke.util.JsonUtil;
import com.tiancikeji.zaoke.util.MD5Util;
import com.tiancikeji.zaoke.util.StringUtil;

import android.os.Handler;
import android.util.Log;

public class SubmitRegHttp implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;

	public SubmitRegHttp(Context context, Handler mHandler, String phone, String userid,String ticket,String code,String passwd) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.register + "?phone=" + phone + "&userid=" +userid + "&ticket=" +ticket + "&code=" +code+ "&password=" + MD5Util.MD5(passwd);
		Log.d("MD5_passwd", MD5Util.MD5(passwd));
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
				AutoRegBase arb = (AutoRegBase) JsonUtil.jsonToObject(json, AutoRegBase.class);
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, arb));
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
