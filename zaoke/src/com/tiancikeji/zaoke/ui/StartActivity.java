package com.tiancikeji.zaoke.ui;

import com.tiancikeji.zaoke.db.service.FirstloginService;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class StartActivity extends AbstractActivity {

	private FirstloginService firstloginService;
	private TextView start_next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		firstloginService = new FirstloginService(this);
		firstloginService.save("LoadingActivity");
		start_next = (TextView) findViewById(R.id.start_next);
		start_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toMainActivity = new Intent(StartActivity.this, MainActivity.class);
				startActivity(toMainActivity);
				finish();
			}
		});

	}

}
