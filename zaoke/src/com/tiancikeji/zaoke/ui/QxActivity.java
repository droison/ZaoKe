package com.tiancikeji.zaoke.ui;

import com.tiancikeji.zaoke.db.service.AccountService;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class QxActivity extends AbstractActivity {

	private TextView qx_backtomain;
	private ImageView imageView2;
	private AccountService as;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qx);
		qx_backtomain = (TextView) findViewById(R.id.qx_backtomain);
		qx_backtomain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		setUpView();
	}

	public void setUpView() {
		imageView2 = (ImageView) this.findViewById(R.id.imageView2);
		as = new AccountService(QxActivity.this);
		imageView2.setImageBitmap(BitmapFactory.decodeByteArray(as.getAccount().getQxImg(), 0, as.getAccount().getQxImg().length));
	}

}
