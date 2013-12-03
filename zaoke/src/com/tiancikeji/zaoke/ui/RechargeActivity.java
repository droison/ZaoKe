package com.tiancikeji.zaoke.ui;

import com.alipay.android.app.sdk.AliPay;
import com.tiancikeji.zaoke.alipay.Result;
import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.RechargeHttp;
import com.tiancikeji.zaoke.httpservice.UserInfoHttp;
import com.tiancikeji.zaoke.httpservice.base.AutoRegBase;
import com.tiancikeji.zaoke.httpservice.base.PayBase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class RechargeActivity extends AbstractActivity implements OnClickListener {

	private ImageView recharge_back;
	private TextView recharge_confirm;
	private TextView recharge_20;
	private TextView recharge_30;
	private TextView recharge_50;
	private TextView recharge_100;
	private TextView recharge_200;
	private TextView recharge_alipay;
	private Double recharge_num = (double) -1;
	private Integer paymode = -1;
	private Boolean isClickPay = false;
	private AccountService as;
	private Dbaccount account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		setUpView();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isClickPay) {
			new Thread(new UserInfoHttp(RechargeActivity.this, isClickPayHandler, account.getName(), account.getUserid(), account.getTicket())).start();
		}
	}

	public void setUpView() {
		recharge_back = (ImageView) this.findViewById(R.id.recharge_back);
		recharge_confirm = (TextView) this.findViewById(R.id.recharge_confirm);
		recharge_20 = (TextView) this.findViewById(R.id.recharge_20);
		recharge_30 = (TextView) this.findViewById(R.id.recharge_30);
		recharge_50 = (TextView) this.findViewById(R.id.recharge_50);
		recharge_100 = (TextView) this.findViewById(R.id.recharge_100);
		recharge_200 = (TextView) this.findViewById(R.id.recharge_200);
		recharge_alipay = (TextView) this.findViewById(R.id.recharge_alipay);

		as = new AccountService(RechargeActivity.this);
		account = as.getAccount();

		recharge_back.setOnClickListener(this);
		recharge_confirm.setOnClickListener(this);
		recharge_20.setOnClickListener(this);
		recharge_30.setOnClickListener(this);
		recharge_50.setOnClickListener(this);
		recharge_100.setOnClickListener(this);
		recharge_200.setOnClickListener(this);
		recharge_alipay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recharge_back:
			finish();
			break;
		case R.id.recharge_confirm:
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(RechargeActivity.this);
			alertDialog.setItems(new String[] { "支付宝网页充值", "支付宝快捷充值" }, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						paymode = 2;
						break;
					case 1:
						paymode = 3;
						break;
					}
					showProgressDialog("正在提交充值请求...");
					new Thread(new RechargeHttp(RechargeActivity.this, payOrderHandler, account.getUserid(), account.getTicket(), paymode, recharge_num)).start();
				}
			});
			alertDialog.show();
			break;

		case R.id.recharge_20:
			setClick(recharge_20);
			recharge_num = (double) 20;
			break;

		case R.id.recharge_30:
			setClick(recharge_30);
			recharge_num = (double) 30;
			break;

		case R.id.recharge_50:
			setClick(recharge_50);
			recharge_num = (double) 50;
			break;

		case R.id.recharge_100:
			setClick(recharge_100);
			recharge_num = (double) 100;
			break;

		case R.id.recharge_200:
			setClick(recharge_200);
			recharge_num = (double) 200;
			break;

		case R.id.recharge_alipay:
			recharge_alipay.setBackgroundResource(R.drawable.recharge_clicked);
			recharge_alipay.setTextColor(getResources().getColor(R.color.izaoke_white));
			break;
		default:
			break;
		}
	}

	private Handler payOrderHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				PayBase pb = (PayBase) msg.obj;
				isClickPay = false;
				if (pb.getMsg() != null) {
					displayResponse("错误：" + pb.getMsg());
				} else {
					account.setBalance(pb.getBalance());
					as.saveOrUpdate(account);

					switch (paymode) {
					case 2:
						Log.v("shopingcarturl", pb.getUrl());
						Uri uri = Uri.parse(pb.getUrl());
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						RechargeActivity.this.startActivity(intent);
						isClickPay = true;
						break;
					case 3:
						Log.v("shopingcarturl", pb.getUrl());
						final String url = pb.getUrl();
						new Thread() {
							public void run() {
								String result = new AliPay(RechargeActivity.this, alipayHandler).pay(url);
								Log.i("Alipay", "result = " + result);
								alipayHandler.sendMessage(alipayHandler.obtainMessage(AppConstant.RQF_PAY, result));
							}
						}.start();
						break;
					default:
						break;
					}
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

	public void setClick(TextView tv) {

		recharge_20.setBackgroundResource(R.drawable.recharge_unclick);
		recharge_20.setTextColor(getResources().getColor(R.color.zaoke_orange));
		recharge_30.setBackgroundResource(R.drawable.recharge_unclick);
		recharge_30.setTextColor(getResources().getColor(R.color.zaoke_orange));
		recharge_50.setBackgroundResource(R.drawable.recharge_unclick);
		recharge_50.setTextColor(getResources().getColor(R.color.zaoke_orange));
		recharge_100.setBackgroundResource(R.drawable.recharge_unclick);
		recharge_100.setTextColor(getResources().getColor(R.color.zaoke_orange));
		recharge_200.setBackgroundResource(R.drawable.recharge_unclick);
		recharge_200.setTextColor(getResources().getColor(R.color.zaoke_orange));
		
		tv.setBackgroundResource(R.drawable.recharge_clicked);
		tv.setTextColor(getResources().getColor(R.color.izaoke_white));
	}

	public void isPaySuccess(boolean isSuccess) {
		if (!isSuccess) {
			displayResponse("支付失败，请重试");
		} else {
			displayResponse("支付成功！");
			account.setBalance(account.getBalance() + recharge_num);
			as.saveOrUpdate(account);
			finish();
		}
	}

	private Handler isClickPayHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				AutoRegBase arb = (AutoRegBase) msg.obj;
				if (arb.getMsg() != null) {
					displayResponse(arb.getMsg());
					isPaySuccess(false);
				} else {
					double balance = account.getBalance()+recharge_num;
					isPaySuccess(balance == arb.getBalance());
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

	private Handler alipayHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result.sResult = (String) msg.obj;

			switch (msg.what) {
			case AppConstant.RQF_PAY: {
				isPaySuccess(Result.isSuccess());
			}
				break;
			default:
				break;
			}
		};
	};
}
