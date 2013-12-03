package com.tiancikeji.zaoke.httpservice;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.httpservice.base.FoodListBase;
import com.tiancikeji.zaoke.httpservice.base.HttpResponseEntity;
import com.tiancikeji.zaoke.util.JsonUtil;
import com.tiancikeji.zaoke.util.StringUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class FoodListHttp implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;

	public FoodListHttp(Context context, Handler mHandler) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.foodList;
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
				FoodListBase flb = JsonUtil.jsonToFoodList(json);
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, flb));
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
