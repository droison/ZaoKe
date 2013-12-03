package com.tiancikeji.zaoke.ui;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.BindCardHttp;
import com.tiancikeji.zaoke.httpservice.base.BindCardBase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class BindCardActivity extends AbstractActivity implements OnClickListener {

	private ImageView bind_card_back;
	private EditText bind_card_number;
	// private EditText bind_card_passwd;
	private TextView bind_card_confirm;
	private AccountService as;
	private BindCardBase bindCardBase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_card);
		bind_card_back = (ImageView) findViewById(R.id.bind_card_back);
		bind_card_number = (EditText) findViewById(R.id.bind_card_number);
		bind_card_confirm = (TextView) findViewById(R.id.bind_card_confirm);
		as = new AccountService(this);
		bindCardBase = new BindCardBase();

		bind_card_back.setOnClickListener(this);
		bind_card_confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case (R.id.bind_card_back):
			finish();
			break;
		case (R.id.bind_card_confirm):
			if (TextUtils.isEmpty(bind_card_number.getText().toString())) {
				System.out.println("bind_card_number.getText().toString()" + bind_card_number.getText().toString());
				displayResponse("会员卡号不能为空");
				return;
			}
			/*
			 * else
			 * if(TextUtils.isEmpty(bind_card_passwd.getText().toString())){
			 * displayResponse("会员卡密码不能为空"); return; }
			 */
			new Thread(new BindCardHttp(this, mHandler, bind_card_number.getText().toString(), as.getAccount().getUserid(), as.getAccount().getTicket())).start();
			showProgressDialog("正在绑定会员卡");
			break;
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			dismissProgressDialog();
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				bindCardBase = (BindCardBase) msg.obj;
				if (bindCardBase.getStatus() == 0) {
					displayResponse("会员卡绑定成功");
					finish();
				} else if (bindCardBase.getStatus() == 10171) {
					displayResponse("未输入会员卡号");
				} else if (bindCardBase.getStatus() == 10172) {
					displayResponse("用户未登陆");
				}
				break;
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				displayResponse("服务器访问失败");
				break;
			case AppConstant.HANDLER_MESSAGE_NONETWORK:
				showNoNetWork();
				break;
			case AppConstant.HANDLER_MESSAGE_TIMEOUT:
				displayResponse("网络访问超时");
				break;
			}
		}

	};

}
