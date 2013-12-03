package com.tiancikeji.zaoke.ui;

import java.util.List;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.httpservice.LocListHttp;
import com.tiancikeji.zaoke.httpservice.base.LocListBase;
import com.tiancikeji.zaoke.httpservice.base.LocalBase;
import com.tiancikeji.zaoke.ui.adapter.ChangeLocalAdapter;
import com.tiancikeji.zaoke.util.ExitApplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public class ChangeLocalActivity extends AbstractActivity {

	private ListView change_local_listView;
	private LocListBase llb = new LocListBase();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_local);
		setUpView();

		Bundle bundle = getIntent().getExtras();

		ExitApplication.getInstance().addActivity(ChangeLocalActivity.this);
		if (bundle.getBoolean("isRoot")) {
			showProgressDialog("正在获取地址...");
			new Thread(new LocListHttp(this, mHandler)).start();
		} else {
			ChangeLocalAdapter adapter = new ChangeLocalAdapter(ChangeLocalActivity.this, (List<LocalBase>) bundle.getSerializable("pick_locs"));
			change_local_listView.setAdapter(adapter);
		}
	}

	public void setUpView() {
		change_local_listView = (ListView) this.findViewById(R.id.change_local_listView);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dismissProgressDialog();
			switch (msg.what) {
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				displayResponse("服务器访问失败");
				break;
			case AppConstant.HANDLER_MESSAGE_NONETWORK:
				showNoNetWork();
				break;
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				llb = (LocListBase) msg.obj;
				if (llb.getStatus() == 0) {
					ChangeLocalAdapter adapter = new ChangeLocalAdapter(ChangeLocalActivity.this, llb.getPick_locs());
					change_local_listView.setAdapter(adapter);
				} else {
					displayResponse("服务器无数据");
				}
				break;
			case AppConstant.HANDLER_MESSAGE_TIMEOUT:
				displayResponse("网络访问超时");
				break;
			default:
				break;
			}
		}
	};

}
