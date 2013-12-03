package com.tiancikeji.zaoke.httpservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.httpservice.base.AutoRegBase;
import com.tiancikeji.zaoke.httpservice.base.HttpResponseEntity;
import com.tiancikeji.zaoke.util.JsonUtil;
import com.tiancikeji.zaoke.util.StringUtil;

import android.os.Handler;
import android.util.Log;

public class UserInfoHttp implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;

	public UserInfoHttp(Context context, Handler mHandler, String name,String userid,String ticket) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.userInfo + "?name=" + name + "&ticket=" + ticket + "&userid=" + userid;
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
				AutoRegBase ari = (AutoRegBase) JsonUtil.jsonToObject(json, AutoRegBase.class);
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, ari));
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
