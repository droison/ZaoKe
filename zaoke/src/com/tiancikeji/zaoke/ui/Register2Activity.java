package com.tiancikeji.zaoke.ui;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.SendCodeHttp;
import com.tiancikeji.zaoke.httpservice.VerifyCodeHttp;
import com.tiancikeji.zaoke.httpservice.base.SendCodeBase;
import com.tiancikeji.zaoke.util.StringUtil;

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

public class Register2Activity extends AbstractActivity implements OnClickListener{

	private EditText regester2_insertnumber;
	private TextView common_confirm;
	private ImageView common_back;
	private TextView common_cancel;
	private AccountService as;
	private String phone;
	private String code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register2);
		phone = this.getIntent().getStringExtra("phone");
		setUpView();
	}
	
	private void setUpView(){
		regester2_insertnumber = (EditText)this.findViewById(R.id.regester2_insertnumber);
		common_confirm = (TextView) this.findViewById(R.id.common_confirm);
		common_back = (ImageView) this.findViewById(R.id.common_back);
		common_cancel = (TextView) this.findViewById(R.id.common_cancel);
		common_cancel.setVisibility(View.VISIBLE);
		
		common_confirm.setOnClickListener(this);
		common_back.setOnClickListener(this);
		common_cancel.setOnClickListener(this);
		as = new AccountService(Register2Activity.this);
		common_confirm.setText("提交验证码");
		common_cancel.setText("重获验证码");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_login:
			displayResponse("暂不提供");
			break;
		case R.id.common_confirm:
			code = regester2_insertnumber.getText().toString();
			if(TextUtils.isEmpty(code)){
				displayResponse("验证码不能为空");
			}else{
				new Thread(new VerifyCodeHttp(Register2Activity.this, verifyCodeHandler, phone, as.getAccount().getUserid(), as.getAccount().getTicket(),code)).start();	
				displayResponse("正在验证");
			}	
			break;
		case R.id.common_back:
			finish();
			break;
		case R.id.common_cancel:
			showProgressDialog("正在发送验证码");
			new Thread(new SendCodeHttp(Register2Activity.this, sendCodeHandler, phone, as.getAccount().getUserid(), as.getAccount().getTicket())).start();	
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
					displayResponse("发送成功！");	
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
	
	private Handler verifyCodeHandler = new Handler(){
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
					displayResponse("验证通过");
					Intent toReg3 = new Intent(Register2Activity.this,Register3Activity.class);
					toReg3.putExtra("phone", phone);
					toReg3.putExtra("code", code);
					startActivity(toReg3);
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
