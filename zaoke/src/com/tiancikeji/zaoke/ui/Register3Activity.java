package com.tiancikeji.zaoke.ui;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.SendCodeHttp;
import com.tiancikeji.zaoke.httpservice.SubmitRegHttp;
import com.tiancikeji.zaoke.httpservice.base.AutoRegBase;
import com.tiancikeji.zaoke.httpservice.base.SendCodeBase;
import com.tiancikeji.zaoke.util.MD5Util;
import com.tiancikeji.zaoke.util.StringUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Register3Activity extends AbstractActivity implements OnClickListener{

	private EditText register3_passwd;
	private EditText register3_nick;
	private TextView common_confirm;
	private ImageView common_back;
	private TextView common_cancel;
	private AccountService as;
	private String phone;
	private String code;
	private String passwd;
	private String nick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register3);
		phone = this.getIntent().getStringExtra("phone");
		code = this.getIntent().getStringExtra("code");
		setUpView();
	}
	
	private void setUpView(){
		register3_passwd = (EditText)this.findViewById(R.id.register3_passwd);
		register3_nick = (EditText) this.findViewById(R.id.register3_nick);
		common_confirm = (TextView) this.findViewById(R.id.common_confirm);
		common_back = (ImageView) this.findViewById(R.id.common_back);
		common_cancel = (TextView) this.findViewById(R.id.common_cancel);
		common_cancel.setVisibility(View.GONE);
		
		common_confirm.setOnClickListener(this);
		common_back.setOnClickListener(this);
		common_cancel.setOnClickListener(this);
		as = new AccountService(Register3Activity.this);
		common_confirm.setText("完成注册");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_confirm:
			passwd = register3_passwd.getText().toString();
			nick = register3_nick.getText().toString();
			if(TextUtils.isEmpty(passwd)){
				displayResponse("密码不能为空");
			}else if(TextUtils.isEmpty(nick)){
				displayResponse("昵称不能为空");
			}else if(!StringUtil.checkpasswd(passwd)){
				displayResponse("密码应该为6为数字");
			}else{
				showProgressDialog("正在完成注册");
				new Thread(new SubmitRegHttp(Register3Activity.this, submitRegHandler, phone, as.getAccount().getUserid(), as.getAccount().getTicket(),code,passwd)).start();
			}
			
			break;
		case R.id.common_back:
			finish();
			break;
		}
	}
	
	private Handler submitRegHandler = new Handler(){
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
				AutoRegBase arb = (AutoRegBase) msg.obj;
				if (arb.getStatus() == 0) {
					displayResponse("注册成功！");
					Dbaccount account = as.getAccount();
					account.setPassword(MD5Util.MD5(passwd));
				//	account.setName(nick);
					account.setPhone(phone);
					as.saveOrUpdate(account);
					finish();
				} else {
					displayResponse("错误："+arb.getMsg());
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
