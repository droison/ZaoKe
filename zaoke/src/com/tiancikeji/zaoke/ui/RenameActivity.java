package com.tiancikeji.zaoke.ui;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.ReNameHttp;
import com.tiancikeji.zaoke.httpservice.base.SendCodeBase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RenameActivity extends AbstractActivity implements OnClickListener{

	private EditText rename_newname;
	private TextView common_confirm;
	private ImageView common_back;
	private AccountService as;
	private Dbaccount account;
	private String newname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rename);
		setUpView();
	}
	
	private void setUpView(){
		rename_newname = (EditText)this.findViewById(R.id.rename_newname);
		common_confirm = (TextView) this.findViewById(R.id.common_confirm);
		common_back = (ImageView) this.findViewById(R.id.common_back);
		common_confirm.setOnClickListener(this);
		common_back.setOnClickListener(this);
		as = new AccountService(RenameActivity.this);
		account = as.getAccount();
		common_confirm.setText("提交");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_confirm:
			newname = rename_newname.getText().toString();
			if(TextUtils.isEmpty(newname)){
				displayResponse("昵称不能为空");
			}else if(newname.length()>30){
				displayResponse("昵称长度应小于30");
			}else{
				showProgressDialog("正在修改");
				new Thread(new ReNameHttp(RenameActivity.this, sendCodeHandler, newname, as.getAccount().getUserid(), as.getAccount().getTicket())).start();
			}
			break;
		case R.id.common_back:
			finish();
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
					displayResponse("修改成功");
					account.setName(newname);
					as.saveOrUpdate(account);
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
