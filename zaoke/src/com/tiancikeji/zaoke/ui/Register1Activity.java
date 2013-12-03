package com.tiancikeji.zaoke.ui;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.SendCodeHttp;
import com.tiancikeji.zaoke.httpservice.base.LocListBase;
import com.tiancikeji.zaoke.httpservice.base.SendCodeBase;
import com.tiancikeji.zaoke.ui.adapter.ChangeLocalAdapter;
import com.tiancikeji.zaoke.util.StringUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Register1Activity extends AbstractActivity implements OnClickListener{

	private EditText regester1_insertnumber;
	private TextView register_login;
	private TextView common_confirm;
	private ImageView common_back;
	private TextView common_cancel;
	private AccountService as;
	private String phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register1);
		setUpView();
	}
	
	private void setUpView(){
		regester1_insertnumber = (EditText)this.findViewById(R.id.regester1_insertnumber);
		register_login = (TextView) this.findViewById(R.id.register_login);
		common_confirm = (TextView) this.findViewById(R.id.common_confirm);
		common_back = (ImageView) this.findViewById(R.id.common_back);
		common_cancel = (TextView) this.findViewById(R.id.common_cancel);
		register_login.setText(Html.fromHtml("<u>已有账号，点此登录</u>"));
		common_cancel.setVisibility(View.GONE);
		
		register_login.setOnClickListener(this);
		common_confirm.setOnClickListener(this);
		common_back.setOnClickListener(this);
		common_cancel.setOnClickListener(this);
		as = new AccountService(Register1Activity.this);
		common_confirm.setText("获取验证码");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_login:
			Intent toLogin = new Intent(Register1Activity.this, LoginActivity.class);
			Register1Activity.this.startActivity(toLogin);
			finish();
			break;
		case R.id.common_confirm:
			phone = regester1_insertnumber.getText().toString();
			if(TextUtils.isEmpty(phone)){
				displayResponse("手机号不能为空");
			}else if(!StringUtil.checkPhone(phone)){
				displayResponse("请输入正确手机号");
			}else{
				showProgressDialog("正在发送验证码");
				new Thread(new SendCodeHttp(Register1Activity.this, sendCodeHandler, phone, as.getAccount().getUserid(), as.getAccount().getTicket())).start();
			}
			
			break;
		case R.id.common_back:
			finish();
			break;
		case R.id.common_cancel:
			
			break;
		}
	}
	
	private Handler sendCodeHandler = new Handler(){
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			switch (msg.what) {
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				displayResponse("服务器访问失败");
				break;
			case AppConstant.HANDLER_MESSAGE_NONETWORK:
				showNoNetWork();
				break;
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				SendCodeBase scb = (SendCodeBase) msg.obj;
				if (scb.getStatus() == 0) {
					Intent toReg2 = new Intent(Register1Activity.this,Register2Activity.class);
					toReg2.putExtra("phone", phone);
					startActivity(toReg2);
					finish();
				} else {
					displayResponse("错误："+scb.getMsg());
				}
				break;
			case AppConstant.HANDLER_MESSAGE_TIMEOUT:
				displayResponse("网络访问超时");
				break;
			default:
				break;
			}
		};
	};
}
