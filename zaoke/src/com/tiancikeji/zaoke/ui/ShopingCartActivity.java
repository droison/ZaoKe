package com.tiancikeji.zaoke.ui;

import java.util.HashMap;
import java.util.Map;

import com.alipay.android.app.sdk.AliPay;
import com.tiancikeji.zaoke.alipay.Result;
import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.base.Dborder;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.BindCardHttp;
import com.tiancikeji.zaoke.httpservice.CheckOrderHttp;
import com.tiancikeji.zaoke.httpservice.FinalOrderListHttp;
import com.tiancikeji.zaoke.httpservice.PayHttp;
import com.tiancikeji.zaoke.httpservice.base.BindCardBase;
import com.tiancikeji.zaoke.httpservice.base.CheckOrderBase;
import com.tiancikeji.zaoke.httpservice.base.FinalOrderBase;
import com.tiancikeji.zaoke.httpservice.base.FinalOrderListBase;
import com.tiancikeji.zaoke.httpservice.base.OrderBase;
import com.tiancikeji.zaoke.httpservice.base.PayBase;
import com.tiancikeji.zaoke.httpservice.base.PaymodeBase;
import com.tiancikeji.zaoke.util.AsyncImageLoader;
import com.tiancikeji.zaoke.util.NumberFormateUtil;
import com.tiancikeji.zaoke.util.ZaokeAlertDialog;
import com.tiancikeji.zaoke.util.AsyncImageLoader.ImageCallback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShopingCartActivity extends AbstractActivity implements OnClickListener {

	private ImageView shoping_cart_back;
	private TextView shoping_cart_submit;
	private OrderBase orderBase;
	private TextView my_order_shipinname;
	private TextView my_order_yinliaoname;
	private TextView my_order_shipinprice;
	private TextView my_order_yinliaoprice;
	private ImageView cart_shipin_image;
	private ImageView cart_yinliao_image;
	private TextView order_price_original;
	private TextView order_combprice;
	private TextView cart_nick_name;
	private TextView cart_place_name;
	private RelativeLayout shoping_cart_recharge;
	private RelativeLayout shoping_cart_bindcart;
	private RelativeLayout shoping_cart_changeplace;
	private TextView cart_user_account_yue;
	private TextView shopping_cart_locmsg;
	private TextView cart_zhifubao;
	private TextView cart_yue;
	private TextView cart_xianjin;
	private AccountService as;
	private Dbaccount dbaccount;
	private String name;
	private CheckOrderBase checkOrderBase = new CheckOrderBase();
	private Dborder order = new Dborder();
	private int paymode = -1;
	private Map<String, String> payMap = new HashMap<String, String>();
	private boolean isClickPay = false;

	@Override
	protected void onRestart() {
		super.onRestart();
		setClick(null);
		if (as.isExist()) {
			dbaccount = as.getAccount();
			cart_place_name.setText(dbaccount.getLocal());
			cart_user_account_yue.setText(String.valueOf(dbaccount.getBalance()));
			if (dbaccount.getEndtime() == null) {
				shopping_cart_locmsg.setText(dbaccount.getStarttime());
			} else {
				shopping_cart_locmsg.setText("请您于明天" + dbaccount.getStarttime() + "到" + dbaccount.getEndtime() + "取餐");
			}
			shopping_cart_locmsg.setText(checkOrderBase.getPick_time());
			
			
		}

		if (isClickPay) {
			new Thread(new FinalOrderListHttp(ShopingCartActivity.this, isClickPayHandler, dbaccount.getUserid(), dbaccount.getTicket())).start();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoping_cart);
		Bundle bundle = this.getIntent().getExtras();
		orderBase = (OrderBase) bundle.getSerializable("orderBase");
		init();

	}

	public void init() {
		my_order_shipinname = (TextView) this.findViewById(R.id.my_order_shipinname);
		my_order_yinliaoname = (TextView) this.findViewById(R.id.my_order_yinliaoname);
		my_order_shipinprice = (TextView) this.findViewById(R.id.my_order_shipinprice);
		my_order_yinliaoprice = (TextView) this.findViewById(R.id.my_order_yinliaoprice);
		shoping_cart_back = (ImageView) findViewById(R.id.shoping_cart_back);
		shoping_cart_submit = (TextView) findViewById(R.id.shoping_cart_submit);
		cart_shipin_image = (ImageView) findViewById(R.id.cart_shipin_image);
		cart_yinliao_image = (ImageView) findViewById(R.id.cart_yinliao_image);
		order_price_original = (TextView) findViewById(R.id.order_price_original);
		order_combprice = (TextView) this.findViewById(R.id.order_combprice);
		shopping_cart_locmsg = (TextView) this.findViewById(R.id.shopping_cart_locmsg);

		cart_nick_name = (TextView) findViewById(R.id.cart_nick_name);

		shoping_cart_recharge = (RelativeLayout) findViewById(R.id.shoping_cart_recharge);
		cart_place_name = (TextView) findViewById(R.id.cart_place_name);
		shoping_cart_bindcart = (RelativeLayout) findViewById(R.id.shoping_cart_bindcart);
		cart_user_account_yue = (TextView) findViewById(R.id.cart_user_account_yue);
		shoping_cart_changeplace = (RelativeLayout) findViewById(R.id.shoping_cart_changeplace);

		cart_zhifubao = (TextView) findViewById(R.id.cart_zhifubao);
		cart_yue = (TextView) findViewById(R.id.cart_yue);
		cart_xianjin = (TextView) findViewById(R.id.cart_xianjin);

		showProgressDialog("正在加载订单...");
		as = new AccountService(this);
		dbaccount = new Dbaccount();
		
		SpannableString msp = new SpannableString("￥" + orderBase.getPrice());
		msp.setSpan(new StrikethroughSpan(), 0, String.valueOf(orderBase.getPrice()).length()+	1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		order_price_original.setText(msp);
		order_combprice.setText("￥" + NumberFormateUtil.Formate(orderBase.getCombinePrice()));
		order.setCombPrice(orderBase.getCombinePrice());
		dbaccount = as.getAccount();

		if (orderBase.getShipin() != null && orderBase.getYinliao() != null) {

			order.setShiwuId(orderBase.getShipin().getId());
			order.setShiwuName(orderBase.getShipin().getName());
			order.setShiwuPrice(orderBase.getShipin().getSale_price());
			order.setShiwuUrl(orderBase.getShipin().getImage_url());
			my_order_shipinname.setText(order.getShiwuName());
			my_order_shipinprice.setText("￥" + order.getShiwuPrice());
			setImageBg(cart_shipin_image, order.getShiwuUrl());

			order.setYinliaoId(orderBase.getYinliao().getId());
			order.setYinliaoName(orderBase.getYinliao().getName());
			order.setYinliaoPrice(orderBase.getYinliao().getSale_price());
			order.setYinliaoUrl(orderBase.getYinliao().getImage_url());
			my_order_yinliaoname.setText(order.getYinliaoName());
			my_order_yinliaoprice.setText("￥" + order.getYinliaoPrice());
			setImageBg(cart_yinliao_image, order.getYinliaoUrl());
			new Thread(new CheckOrderHttp(ShopingCartActivity.this, orderHandler, orderBase.getShipin().getId(), orderBase.getYinliao().getId(), name, dbaccount.getUserid(), dbaccount.getTicket())).start();
		} else if (orderBase.getShipin() == null) {
			order.setYinliaoId(orderBase.getYinliao().getId());
			order.setYinliaoName(orderBase.getYinliao().getName());
			order.setYinliaoPrice(orderBase.getYinliao().getSale_price());
			order.setYinliaoUrl(orderBase.getYinliao().getImage_url());
			my_order_yinliaoname.setText(order.getYinliaoName());
			my_order_yinliaoprice.setText("￥" + order.getYinliaoPrice());
			setImageBg(cart_yinliao_image, order.getYinliaoUrl());
			new Thread(new CheckOrderHttp(ShopingCartActivity.this, orderHandler, 0, orderBase.getYinliao().getId(), name, dbaccount.getUserid(), dbaccount.getTicket())).start();
		} else if (orderBase.getYinliao() == null) {
			order.setShiwuId(orderBase.getShipin().getId());
			order.setShiwuName(orderBase.getShipin().getName());
			order.setShiwuPrice(orderBase.getShipin().getSale_price());
			order.setShiwuUrl(orderBase.getShipin().getImage_url());
			my_order_shipinname.setText(order.getShiwuName());
			my_order_shipinprice.setText("￥" + order.getShiwuPrice());
			setImageBg(cart_shipin_image, order.getShiwuUrl());
			new Thread(new CheckOrderHttp(ShopingCartActivity.this, orderHandler, orderBase.getShipin().getId(), 0, name, dbaccount.getUserid(), dbaccount.getTicket())).start();
		}

		shoping_cart_submit.setOnClickListener(this);
		shoping_cart_back.setOnClickListener(this);
		shoping_cart_recharge.setOnClickListener(this);
		shoping_cart_bindcart.setOnClickListener(this);
		shoping_cart_changeplace.setOnClickListener(this);
		cart_zhifubao.setOnClickListener(this);
		cart_yue.setOnClickListener(this);
		cart_xianjin.setOnClickListener(this);
	}

	private Handler orderHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				checkOrderBase = (CheckOrderBase) msg.obj;
				for (PaymodeBase pmb : checkOrderBase.getPaymode()) {
					payMap.put(pmb.getMode(), pmb.getName());
				}
				if (!payMap.containsKey("0")) {
					cart_xianjin.setVisibility(View.GONE);
				}
				if (!payMap.containsKey("1")) {
					cart_yue.setVisibility(View.GONE);
				}
				if (!payMap.containsKey("2") && !payMap.containsKey("3")) {
					cart_zhifubao.setVisibility(View.GONE);
				}
				cart_nick_name.setText(dbaccount.getName());
				cart_user_account_yue.setText("" + checkOrderBase.getBalance());

				if (checkOrderBase.getPick_loc_name() != null) {
					shopping_cart_locmsg.setText(checkOrderBase.getPick_time());
					cart_place_name.setText(checkOrderBase.getPick_loc_name());
					dbaccount.setLocal(checkOrderBase.getPick_loc_name());
					dbaccount.setLocid(checkOrderBase.getPick_loc_id());
					dbaccount.setStarttime(checkOrderBase.getPick_date());
					System.out.println(dbaccount.getStarttime());
					dbaccount.setBalance(checkOrderBase.getBalance());
					dbaccount.setEndtime("");
					as.saveOrUpdate(dbaccount);
				} //else if (dbaccount.getLocal() != null) {
				else{
					if (dbaccount.getEndtime() == null) {
						shopping_cart_locmsg.setText("请您于" + checkOrderBase.getPick_date() + dbaccount.getStarttime()  + "取餐");
					} else {
						shopping_cart_locmsg.setText("请您于" + checkOrderBase.getPick_date() + dbaccount.getStarttime() + "到" + dbaccount.getEndtime() + "取餐");
					}
					
					cart_place_name.setText(dbaccount.getLocal());
				}
				break;
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				finish();
				break;
			case AppConstant.HANDLER_MESSAGE_NONETWORK:
				showNoNetWork();
				finish();
				break;
			}

		}

	};

	public void setImageBg(final ImageView imageView, String url) {
		new AsyncImageLoader().loadBitmap(ShopingCartActivity.this, url, new ImageCallback() {

			@Override
			public void imageLoaded(Bitmap bitmap, String imageUrl) {
				imageView.setImageBitmap(bitmap);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shoping_cart_submit:
			if (order.isComplete()) {
				if (TextUtils.isEmpty(cart_place_name.getText())) {
					displayResponse("请选择取餐地点");
				} else if (paymode == -1) {
					displayResponse("请选择支付方式");
				} else {
					order.setPaymode(paymode);
					order.setLocal(dbaccount.getLocal());
					order.setLocId(dbaccount.getLocid());
					order.setStarttime(dbaccount.getStarttime());
					order.setEndtime(dbaccount.getEndtime());
					order.setUserid(dbaccount.getUserid());
					final String shiwuId = order.getShiwuId() == null ? "" : String.valueOf(order.getShiwuId());
					final String yinliaoId = order.getYinliaoId() == null ? "" : String.valueOf(order.getYinliaoId());

					if (paymode == 2 || paymode == 3)// 支付宝支付
					{
						if (payMap.containsKey("2") && payMap.containsKey("3")) { // 选择

							AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShopingCartActivity.this);
							alertDialog.setItems(new String[] { "支付宝网页支付", "支付宝快捷支付" }, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									switch (which) {
									case 0:
										paymode = 2;
										showProgressDialog("提交订单中...");
										new Thread(new PayHttp(ShopingCartActivity.this, payOrderHandler, shiwuId, yinliaoId, order.getUserid(), dbaccount.getTicket(), paymode, order.getLocId())).start();
										break;
									case 1:
										paymode = 3;
										order.setPaymode(paymode);
										showProgressDialog("提交订单中...");
										new Thread(new PayHttp(ShopingCartActivity.this, payOrderHandler, shiwuId, yinliaoId, order.getUserid(), dbaccount.getTicket(), paymode, order.getLocId())).start();
										break;
									}
								}
							});
							alertDialog.show();
						} else if (payMap.containsKey("3")) { // 支付宝快捷
							paymode = 3;
							order.setPaymode(paymode);
							showProgressDialog("提交订单中...");
							new Thread(new PayHttp(ShopingCartActivity.this, payOrderHandler, shiwuId, yinliaoId, order.getUserid(), dbaccount.getTicket(), paymode, order.getLocId())).start();
						} else { // 支付宝网页
							showProgressDialog("提交订单中...");
							new Thread(new PayHttp(ShopingCartActivity.this, payOrderHandler, shiwuId, yinliaoId, order.getUserid(), dbaccount.getTicket(), paymode, order.getLocId())).start();
						}
					} else if (paymode == 1) {
						showProgressDialog("提交订单中...");
						new Thread(new PayHttp(ShopingCartActivity.this, payOrderHandler, shiwuId, yinliaoId, order.getUserid(), dbaccount.getTicket(), paymode, order.getLocId())).start();
					} else {
						showProgressDialog("提交订单中...");
						new Thread(new PayHttp(ShopingCartActivity.this, payOrderHandler, shiwuId, yinliaoId, order.getUserid(), dbaccount.getTicket(), paymode, order.getLocId())).start();
					}
				}
			} else {
				displayResponse("订单信息不完整，请您检查一下");
			}
			break;
		case R.id.shoping_cart_back:
			finish();
			break;
		case R.id.shoping_cart_changeplace:
			Intent toChangelocal = new Intent(ShopingCartActivity.this, ChangeLocalActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean("isRoot", true);
			toChangelocal.putExtras(bundle);
			startActivity(toChangelocal);
			break;
		case R.id.shoping_cart_bindcart:
			if (as.getAccount().getPhone() != null) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShopingCartActivity.this);
				alertDialog.setItems(new String[] { "扫描条码", "手工输入" }, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent toQXReader = new Intent(ShopingCartActivity.this, QXReaderActivity.class);
							startActivityForResult(toQXReader, AppConstant.QXReader);
							break;
						case 1:
							Intent toBindCardActivity = new Intent(ShopingCartActivity.this, BindCardActivity.class);
							startActivity(toBindCardActivity);
							break;
						}
					}
				});
				alertDialog.show();
			} else {
				new ZaokeAlertDialog(this, "用户注册后才能绑定会员卡，是否前往", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent toRegister1 = new Intent(ShopingCartActivity.this, Register1Activity.class);
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
		case R.id.shoping_cart_recharge:
			if (as.getAccount().getPhone() == null) {
				new ZaokeAlertDialog(this, "用户注册后才能充值，是否前往", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent toRegister1 = new Intent(ShopingCartActivity.this, Register1Activity.class);
						startActivity(toRegister1);
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).creat();
			} else {
				Intent toRechargeActivity = new Intent(ShopingCartActivity.this, RechargeActivity.class);
				startActivity(toRechargeActivity);
			}
			break;
		case R.id.cart_zhifubao:
			setClick(cart_zhifubao);
			paymode = 2;
			break;
		case R.id.cart_yue:
			setClick(cart_yue);
			paymode = 1;
			break;
		case R.id.cart_xianjin:
			setClick(cart_xianjin);
			paymode = 0;
			break;

		}
	}

	public void setClick(TextView tv) {

		cart_zhifubao.setBackgroundResource(R.drawable.recharge_unclick);
		cart_yue.setBackgroundResource(R.drawable.recharge_unclick);
		cart_xianjin.setBackgroundResource(R.drawable.recharge_unclick);
		cart_zhifubao.setTextColor(getResources().getColor(R.color.zaoke_orange));
		cart_yue.setTextColor(getResources().getColor(R.color.zaoke_orange));
		cart_xianjin.setTextColor(getResources().getColor(R.color.zaoke_orange));
		paymode = -1;
		if (tv != null) {
			tv.setBackgroundResource(R.drawable.recharge_clicked);
			tv.setTextColor(getResources().getColor(R.color.izaoke_white));
		}
	}

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

	private Handler isClickPayHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				FinalOrderListBase folb = (FinalOrderListBase) msg.obj;
				if (folb.getStatus() != 0) {
					displayResponse("错误：" + folb.getMsg());
					isPaySuccess(false);
				} else {
					if (folb.getOrders() == null || !folb.getOrders().containsKey(String.valueOf(order.getOrderId()))) {
						displayResponse("订单失败，请重新下单");
						finish();
					} else {
						FinalOrderBase fob = folb.getOrders().get(order.getOrderId());
						isPaySuccess(fob.getStatus() == 1);
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

	public void isPaySuccess(boolean isSuccess) {
		if (!isSuccess) {
			displayResponse("支付失败，订单转为领取时支付");
			Intent toOrderSuccessActivity = new Intent(ShopingCartActivity.this, OrderSuccessActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("order", order);
			toOrderSuccessActivity.putExtras(bundle);
			startActivity(toOrderSuccessActivity);
			finish();
		} else {
			displayResponse("支付成功！");
			Intent toOrderSuccessActivity = new Intent(ShopingCartActivity.this, OrderSuccessActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("order", order);
			toOrderSuccessActivity.putExtras(bundle);
			startActivity(toOrderSuccessActivity);
			finish();
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
					order.setOrderId(pb.getOrderid());
					order.setOrdertime(System.currentTimeMillis());
					order.setBalance(pb.getBalance());
					order.setQxImg(pb.getCode());
					if (paymode == 1) {
						dbaccount.setBalance(pb.getBalance() - order.getCombPrice());
					} else {
						dbaccount.setBalance(pb.getBalance());
					}
					dbaccount.setQxImg(pb.getCode());
					//dbaccount.setStarttime(pb.get)
					as.saveOrUpdate(dbaccount);
					Intent toOrderSuccessActivity = new Intent(ShopingCartActivity.this, OrderSuccessActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("order", order);
					toOrderSuccessActivity.putExtras(bundle);
					switch (paymode) {
					case 0:
						startActivity(toOrderSuccessActivity);
						finish();
						break;
					case 1:
						startActivity(toOrderSuccessActivity);
						finish();
						break;
					case 2:
						Log.v("shopingcarturl", pb.getUrl());
						Uri uri = Uri.parse(pb.getUrl());
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						ShopingCartActivity.this.startActivity(intent);
						isClickPay = true;

						break;
					case 3:
						Log.v("shopingcarturl", pb.getUrl());
						final String url = pb.getUrl();
						new Thread() {
							public void run() {
								String result = new AliPay(ShopingCartActivity.this, alipayHandler).pay(url);

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
