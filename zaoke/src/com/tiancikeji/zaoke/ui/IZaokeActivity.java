package com.tiancikeji.zaoke.ui;

import java.util.List;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.base.Dborder;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.db.service.OrderService;
import com.tiancikeji.zaoke.httpservice.BindCardHttp;
import com.tiancikeji.zaoke.httpservice.FinalOrderListHttp;
import com.tiancikeji.zaoke.httpservice.base.BindCardBase;
import com.tiancikeji.zaoke.httpservice.base.FinalOrderListBase;
import com.tiancikeji.zaoke.ui.MyOrderActivity.MyViewPagerAdapter;
import com.tiancikeji.zaoke.ui.MyOrderActivity.pagerChanger;
import com.tiancikeji.zaoke.util.ZaokeAlertDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IZaokeActivity extends AbstractActivity implements OnClickListener {

	private ImageView izaoke_back;
	private TextView izaoke_showorder;
	private TextView izaoke_changepwd;

	private TextView izaoke_name;
	private TextView izaoke_local;
	private TextView izaoke_balance;
	

	private LinearLayout izaoke_changecard;
	private RelativeLayout izaoke_changelocal;
	private RelativeLayout izaoke_recharge;

	private AccountService as;
	private OrderService os;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_izaoke);
		setUpView();
	}

	public void setUpView() {
		izaoke_back = (ImageView) this.findViewById(R.id.izaoke_back);
		izaoke_showorder = (TextView) this.findViewById(R.id.izaoke_showorder);
		izaoke_changepwd = (TextView) this.findViewById(R.id.izaoke_changepwd);

		izaoke_name = (TextView) this.findViewById(R.id.izaoke_name);
		izaoke_local = (TextView) this.findViewById(R.id.izaoke_local);
		izaoke_balance = (TextView) this.findViewById(R.id.izaoke_balance);

		izaoke_changecard = (LinearLayout) this.findViewById(R.id.izaoke_changecard);
		izaoke_changelocal = (RelativeLayout) this.findViewById(R.id.izaoke_changelocal);
		izaoke_recharge = (RelativeLayout) this.findViewById(R.id.izaoke_recharge);

		izaoke_back.setOnClickListener(this);
		izaoke_showorder.setOnClickListener(this);
		izaoke_changepwd.setOnClickListener(this);
		izaoke_changecard.setOnClickListener(this);
		izaoke_changelocal.setOnClickListener(this);
		izaoke_recharge.setOnClickListener(this);
		izaoke_name.setOnClickListener(this);

	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

	public void init() {
		as = new AccountService(this);
		//if (as.isExist()) {
			Dbaccount account = new Dbaccount();
			account = as.getAccount();
			izaoke_name.setText(account.getName());
			if (account.getLocal() == null) {
				izaoke_local.setText("");
			} else {
				izaoke_local.setText(account.getLocal());
			}
			if (account.getBalance() == null) {
				izaoke_balance.setText("0");
			} else {
				izaoke_balance.setText("" + account.getBalance());
			}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		init();
	}
	
	private  Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			dismissProgressDialog();
			super.handleMessage(msg);
			switch(msg.what){
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				FinalOrderListBase folb = (FinalOrderListBase)msg.obj;
				if (folb.getOrders() == null) {

					(new ZaokeAlertDialog(IZaokeActivity.this, "您今天还没有任何订单，点击确认前往订餐", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							Intent toOrderActivity = new Intent(IZaokeActivity.this, OrderActivity.class);
							startActivity(toOrderActivity);
							IZaokeActivity.this.finish();
						}
					},  new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.cancel();
						}
					})).creat();

				} else {
					Intent toMyOrderActivity = new Intent(IZaokeActivity.this, MyOrderActivity.class);
					startActivity(toMyOrderActivity);
				}
				
				break;
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				displayResponse("服务器访问失败");
				finish();
				break;
			case AppConstant.HANDLER_MESSAGE_NONETWORK:
				showNoNetWork();
				finish();
				break;
			case AppConstant.HANDLER_MESSAGE_TIMEOUT:
				displayResponse("网络访问超时");
				finish();
				break;
			}
			
		}
		
		
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.izaoke_back:
			finish();
			break;
		case R.id.izaoke_showorder:
/*			os = new OrderService(IZaokeActivity.this);
			List<Dborder> order = os.getLastAll();*/
			new Thread(new FinalOrderListHttp(this, mHandler, as.getAccount().getUserid(), as.getAccount().getTicket())).start();
			break;
		case R.id.izaoke_changepwd:
			if(as.getAccount().getPhone()!=null){
			Intent toPasswdActivity = new Intent(IZaokeActivity.this, PasswdActivity.class);
			startActivity(toPasswdActivity);
			}else{
				new ZaokeAlertDialog(this, "您需要先登录，是否前往", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent toLogin = new Intent(IZaokeActivity.this, LoginActivity.class);
						startActivity(toLogin);
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).creat();
			}
			break;
		case R.id.izaoke_changecard:
			if(as.getAccount().getPhone()!=null){
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(IZaokeActivity.this);
				alertDialog.setItems(new String[] { "扫描条码", "手工输入" }, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent toQXReader = new Intent(IZaokeActivity.this, QXReaderActivity.class);
							startActivityForResult(toQXReader, AppConstant.QXReader);
							break;
						case 1:
							Intent toBindCardActivity = new Intent(IZaokeActivity.this, BindCardActivity.class);
							startActivity(toBindCardActivity);
							break;
						}
					}
				});
				alertDialog.show();
			}else{
				new ZaokeAlertDialog(this, "用户登录后才能绑定会员卡，是否前往", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent toRegister1 = new Intent(IZaokeActivity.this, LoginActivity.class);
						startActivity(toRegister1);
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).creat();
			}
			break;
		case R.id.izaoke_changelocal:
			Intent toChangelocal = new Intent(IZaokeActivity.this, ChangeLocalActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean("isRoot", true);
			toChangelocal.putExtras(bundle);
			startActivity(toChangelocal);
			break;
		case R.id.izaoke_recharge:
			if(as.getAccount().getPhone()==null){
				new ZaokeAlertDialog(this, "用户登录后才能充值，是否前往", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent toRegister1 = new Intent(IZaokeActivity.this, LoginActivity.class);
						startActivity(toRegister1);
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).creat();
			}else{
				Intent toRechargeActivity = new Intent(IZaokeActivity.this, RechargeActivity.class);
				startActivity(toRechargeActivity);
			}
			break;
		case R.id.izaoke_name:
			Intent rename = new Intent(IZaokeActivity.this, RenameActivity.class);
			startActivity(rename);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (AppConstant.QXReader == requestCode) {

			if (resultCode != RESULT_OK)
				return;
			String code = data.getStringExtra("code");
			new Thread(new BindCardHttp(this, bindCardHandler, code, as.getAccount().getUserid(), as.getAccount().getTicket())).start();
			showProgressDialog("正在绑定会员卡");
		} 
	}
	private Handler bindCardHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			dismissProgressDialog();
			BindCardBase bindCardBase = new BindCardBase();
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				bindCardBase = (BindCardBase) msg.obj;
				if (bindCardBase.getStatus() == 0) {
					displayResponse("会员卡绑定成功");
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
