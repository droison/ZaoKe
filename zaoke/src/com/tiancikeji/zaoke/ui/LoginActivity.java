package com.tiancikeji.zaoke.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.LoginHttp;
import com.tiancikeji.zaoke.httpservice.base.AutoRegBase;
import com.tiancikeji.zaoke.httpservice.base.FinalOrderBase;
import com.tiancikeji.zaoke.httpservice.base.FinalOrderListBase;
import com.tiancikeji.zaoke.util.MD5Util;
import com.tiancikeji.zaoke.util.QXReaderEncoder;
import com.tiancikeji.zaoke.util.StringUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AbstractActivity implements OnClickListener {

	private EditText login_phone;
	private EditText login_passwd;
	private TextView login_register;
	private TextView login_login;
	private String phone;
	private String passwd;
	private AccountService as;
	private Dbaccount account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setUpView();
	}

	public void setUpView() {
		as = new AccountService(this);
		account = as.getAccount();
		login_phone = (EditText) this.findViewById(R.id.login_phone);
		login_passwd = (EditText) this.findViewById(R.id.login_passwd);
		login_register = (TextView) this.findViewById(R.id.login_register);
		login_login = (TextView) this.findViewById(R.id.login_login);
		login_register.setText(Html.fromHtml("<u>没有账号，点此注册</u>"));
		login_register.setOnClickListener(this);
		login_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_register:
			Intent toRegister = new Intent(LoginActivity.this, Register1Activity.class);
			LoginActivity.this.startActivity(toRegister);
			finish();
			break;
		case R.id.login_login:
			phone = login_phone.getText().toString();
			passwd = login_passwd.getText().toString();
			if (!StringUtil.checkPhone(phone)) {
				displayResponse("请输入正确手机号");
			} else if(TextUtils.isEmpty(passwd)){
				displayResponse("密码不能为空");
			}else {
				showProgressDialog("正在登录...");
				new Thread(new LoginHttp(LoginActivity.this, loginHandler, phone, MD5Util.MD5(passwd), as.getAccount().getUserid(), as.getAccount().getTicket())).start();

			}
			break;
		}
	}

	private Handler loginHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				AutoRegBase arg = (AutoRegBase) msg.obj;
				if (arg.getStatus() == 0) {
					account.setBalance(arg.getBalance());
					account.setUserid(arg.getUserid());
					account.setTicket(arg.getTicket());
					account.setName(arg.getName());
					account.setPassword(passwd);
					account.setPhone(phone);
					Bitmap bm = QXReaderEncoder.encode(String.valueOf(arg.getUserid()), AppConstant.BASE_DIR_PATH + File.separator + "qx.jpg");
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
				    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
				    account.setQxImg(baos.toByteArray());
					as.saveOrUpdate(account);
					displayResponse("登录成功");
					finish();
				} else {
					displayResponse(arg.getMsg());
					Log.v("login", arg.getStatus() + ":" + arg.getMsg());
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
