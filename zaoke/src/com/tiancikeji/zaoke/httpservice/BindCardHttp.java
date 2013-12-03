package com.tiancikeji.zaoke.httpservice;

import com.alibaba.fastjson.JSONArray;
import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.httpservice.base.BindCardBase;
import com.tiancikeji.zaoke.httpservice.base.BuyInfoBase;
import com.tiancikeji.zaoke.httpservice.base.HttpResponseEntity;
import com.tiancikeji.zaoke.util.JsonUtil;
import com.tiancikeji.zaoke.util.StringUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class BindCardHttp implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;

	public BindCardHttp(Context context, Handler mHandler, String card_number, String userid, String ticket) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.bindVip + "?card_id=" + card_number + "&userid=" + userid + "&ticket=" + ticket;
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
				BindCardBase bcb = (BindCardBase) JsonUtil.jsonToObject(json, BindCardBase.class);
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, bcb));
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
