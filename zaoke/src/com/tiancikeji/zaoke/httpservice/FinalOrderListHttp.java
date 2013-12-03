package com.tiancikeji.zaoke.httpservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.httpservice.base.FinalOrderListBase;
import com.tiancikeji.zaoke.httpservice.base.HttpResponseEntity;
import com.tiancikeji.zaoke.util.JsonUtil;
import com.tiancikeji.zaoke.util.StringUtil;

import android.os.Handler;
import android.util.Log;

/**
 * 
 * @author Administrator
 * 返回FinalOrderListBase
 */
public class FinalOrderListHttp implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;

	public FinalOrderListHttp(Context context, Handler mHandler, String userid,String ticket) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.finalOrderList + "?userid=" + userid + "&ticket=" + ticket;
		
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
		
		System.out.println(url);
		HttpResponseEntity hre = HTTP.get(url);
		switch (hre.getHttpResponseCode()) {
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				FinalOrderListBase folb = JsonUtil.jsonToOrderList(json);
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, folb));
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
