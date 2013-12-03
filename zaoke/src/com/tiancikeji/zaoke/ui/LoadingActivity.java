package com.tiancikeji.zaoke.ui;

import com.tiancikeji.zaoke.db.service.FirstloginService;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class LoadingActivity extends AbstractActivity {
	private RelativeLayout layout;
	private FirstloginService firstloginService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		firstloginService = new FirstloginService(this);
		layout = (RelativeLayout) findViewById(R.id.loading);
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(3000);
		String model = android.os.Build.MODEL;

		layout.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (!firstloginService.isExist("LoadingActivity")) {
					Intent toStartActivity = new Intent(LoadingActivity.this, StartActivity.class);
					startActivity(toStartActivity);
					finish();
				} else {
					Intent toMainActivity = new Intent(LoadingActivity.this, MainActivity.class);
					startActivity(toMainActivity);
					finish();
				}
			}
		});

	}

}
