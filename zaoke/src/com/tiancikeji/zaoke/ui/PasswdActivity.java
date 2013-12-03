package com.tiancikeji.zaoke.ui;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.PasswdHttp;
import com.tiancikeji.zaoke.httpservice.base.PasswdBase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PasswdActivity extends AbstractActivity implements OnClickListener {

	private EditText passwd_passwdpast;
	private EditText passwd_passwdnew;
	private EditText passwd_passwdnew_submit;

	private TextView common_confirm;
	private ImageView common_back;
	private AccountService as;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_passwd);
		setUpView();
	}

	private void setUpView() {
		passwd_passwdpast = (EditText) this.findViewById(R.id.passwd_passwdpast);
		passwd_passwdnew = (EditText) this.findViewById(R.id.passwd_passwdnew);
		passwd_passwdnew_submit = (EditText) this.findViewById(R.id.passwd_passwdnew_submit);
		common_confirm = (TextView) this.findViewById(R.id.common_confirm);
		common_back = (ImageView) this.findViewById(R.id.common_back);

		as = new AccountService(this);
		
		common_confirm.setOnClickListener(this);
		common_back.setOnClickListener(this);
	}
	


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_confirm:

			if(passwd_passwdpast.getText().toString().length() == 0){
				displayResponse("当前密码不能为空");
			}else if(passwd_passwdnew.getText().toString().length() == 0){
				displayResponse("新密码不能为空");
			}else if(passwd_passwdnew_submit.getText().toString().length() == 0){
				displayResponse("确认输入密码不能为空");
			}else if(passwd_passwdpast.getText().toString().equals(passwd_passwdnew.getText())){
				displayResponse("新密码和旧密码不能相同");
			}else if(!passwd_passwdnew.getText().toString().equals(passwd_passwdnew_submit.getText().toString())){
				displayResponse("请确认两次输入新密码一致");
			}else if(passwd_passwdnew_submit.getText().toString().length() != 6 && passwd_passwdnew_submit.getText().toString().length() != 6){
				displayResponse("密码长度必须为六位");
			}else{
				new Thread(new PasswdHttp(this, mHandler, as.getAccount().getUserid(), as.getAccount().getTicket(),
						passwd_passwdpast.getText().toString(), passwd_passwdnew.getText().toString(), 
						passwd_passwdnew_submit.getText().toString())).start();
				showProgressDialog("修改密码中");
			}
			break;
		case R.id.common_back:
			finish();
			break;
		default:
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
				PasswdBase passwd = (PasswdBase)msg.obj;
				if(passwd.getStatus() == 0){
					Dbaccount account = as.getAccount();
					account.setPassword(passwd_passwdnew.getText().toString());
					as.saveOrUpdate(account);
					displayResponse("修改密码成功");
					finish();
				}else{
					displayResponse("错误："+passwd.getMsg());
					passwd_passwdpast.setText("");
					passwd_passwdnew.setText("");
					passwd_passwdnew_submit.setText("");
					
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
