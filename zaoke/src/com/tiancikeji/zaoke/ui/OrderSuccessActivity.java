package com.tiancikeji.zaoke.ui;

import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.base.Dborder;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.db.service.OrderService;
import com.tiancikeji.zaoke.util.ExitApplication;
import com.tiancikeji.zaoke.util.ZaokeAlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderSuccessActivity extends AbstractActivity {

	private TextView order_success_tomain;
	private TextView my_order_recharge;
	private ImageView order_success_qx;
	private OrderService os;
	private AccountService as;
	private Dbaccount account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_success);

		Bundle bundle = this.getIntent().getExtras();
		Dborder order = (Dborder) bundle.getSerializable("order");

		os = new OrderService(OrderSuccessActivity.this);
		as = new AccountService(OrderSuccessActivity.this);
		account = as.getAccount();
		((TextView) this.findViewById(R.id.order_success_balance)).setText("" + account.getBalance());
		((TextView) this.findViewById(R.id.order_success_id)).setText("" + order.getOrderId());
		order_success_qx = (ImageView) findViewById(R.id.order_success_qx);
		if (order.getQxImg() != null) {
			order_success_qx.setImageBitmap(BitmapFactory.decodeByteArray(order.getQxImg(), 0, order.getQxImg().length));
		}/*
		 * else{
		 * order_success_qx.setImageBitmap(QXReaderEncoder.encode(order.getUserid
		 * (), AppConstant.BASE_DIR_PATH + File.separator + "qx.jpg")); }
		 */
		os.save(order);
		order_success_tomain = (TextView) findViewById(R.id.order_success_tomain);
		my_order_recharge = (TextView) findViewById(R.id.my_order_recharge);
		order_success_tomain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExitApplication.getInstance().exitOrderActivity();
				finish();
			}
		});
		my_order_recharge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (as.getAccount().getPhone() == null) {
					new ZaokeAlertDialog(OrderSuccessActivity.this, "用户注册后才能充值，是否前往", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent toRegister1 = new Intent(OrderSuccessActivity.this, Register1Activity.class);
							startActivity(toRegister1);
						}
					}, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).creat();
				} else {
					Intent toRechargeActivity = new Intent(OrderSuccessActivity.this, RechargeActivity.class);
					startActivity(toRechargeActivity);
				}
			}
		});
	}
}
