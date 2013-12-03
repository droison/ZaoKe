package com.tiancikeji.zaoke.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.common.BitArray;
import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.ApkDownloadService;
import com.tiancikeji.zaoke.httpservice.AutoRegHttp;
import com.tiancikeji.zaoke.httpservice.CheckVersionService;
import com.tiancikeji.zaoke.httpservice.base.AutoRegBase;
import com.tiancikeji.zaoke.util.QXReaderEncoder;
import com.tiancikeji.zaoke.util.ZaokeAlertDialog;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AbstractActivity {
	private TextView main_start;
	private TextView main_izaoke;
	private TextView main_iqx;
	private AccountService accountService;
	private Dbaccount dbacout;
	private AutoRegBase autoRegBase;
	private String name;
	private static final String TAG = "MainActivity";
	private Boolean isReg = true;
	private Boolean isThreadComplete = true;
	private ImageView logo;
	private Animation translateAnimation = null;  
	private Animation alphaAnimation1,alphaAnimation2,alphaAnimation3;
/*	private int count = 0;
	private Count counter;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	public void init() {
		main_start = (TextView) findViewById(R.id.main_start);
		main_izaoke = (TextView) findViewById(R.id.main_izaoke);
		main_iqx = (TextView) findViewById(R.id.main_iqx);
		
		logo = (ImageView)findViewById(R.id.main_logo);
		
		translateAnimation = new TranslateAnimation(0f, 0f, 140f, 0f);
		translateAnimation.setDuration(1000);
		logo.startAnimation(translateAnimation);
		
		alphaAnimation1 = new AlphaAnimation(0f, 1.0f);
		alphaAnimation1.setDuration(1000);
		alphaAnimation1.setStartOffset(1000);
		
		alphaAnimation2 = new AlphaAnimation(0f, 1.0f);
		alphaAnimation2.setDuration(1400);
		alphaAnimation2.setStartOffset(1000);
		
		alphaAnimation3 = new AlphaAnimation(0f, 1.0f);
		alphaAnimation3.setDuration(1600);
		alphaAnimation3.setStartOffset(1000);
		
		main_start.startAnimation(alphaAnimation1);
		main_izaoke.startAnimation(alphaAnimation2);
		main_iqx.startAnimation(alphaAnimation3);
/*		counter = new Count();
		handler.post(counter);*/

		accountService = new AccountService(this);
		dbacout = accountService.getAccount();
		name = android.os.Build.MODEL.replace(" ", "");
		if(name.length()>10){
			name = name.substring(0, 10);
		}
		if (dbacout == null){
			isReg = false;
			new Thread(new AutoRegHttp(this, mHandler, name)).start();
			isThreadComplete = false;
		}
		UpdateHandler uhandler = new UpdateHandler(this);
		new Thread(new CheckVersionService(this, uhandler)).start();
		main_start.setOnClickListener(new onclick());
		main_izaoke.setOnClickListener(new onclick());
		main_iqx.setOnClickListener(new onclick());

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			isThreadComplete = true;
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				Log.d(TAG, "自动注册成功");
				Toast.makeText(MainActivity.this, "自动注册成功", Toast.LENGTH_SHORT).show();
				isReg = true;
				dbacout = new Dbaccount();
				autoRegBase = (AutoRegBase) msg.obj;
				dbacout.setName(name);
				dbacout.setUserid(autoRegBase.getUserid() + "");
				dbacout.setTicket(autoRegBase.getTicket());
				Bitmap bm = QXReaderEncoder.encode(String.valueOf(autoRegBase.getUserid()), AppConstant.BASE_DIR_PATH + File.separator + "qx.jpg");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
				dbacout.setQxImg(baos.toByteArray());
				accountService.saveOrUpdate(dbacout);
				break;
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				showDialog();
				break;
			case AppConstant.HANDLER_MESSAGE_NONETWORK:
				showDialog();
				showNoNetWork();
				break;
			case AppConstant.HANDLER_MESSAGE_TIMEOUT:
				showDialog();
				break;
			}

		}

	};

	public void showDialog() {
		(new ZaokeAlertDialog(MainActivity.this, "自动注册失败，是否重新注册",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						new Thread(new AutoRegHttp(MainActivity.this, mHandler,
								name)).start();
						isThreadComplete = false;
					}
				}, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.cancel();
					}
				})).creat();
	}
	class onclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(isReg){
				switch (v.getId()) {
				case R.id.main_start:
					Intent toOrderActivity = new Intent(MainActivity.this, OrderActivity.class);
					startActivity(toOrderActivity);
					break;
				case R.id.main_izaoke:
					Intent toIZaokeActivity = new Intent(MainActivity.this, IZaokeActivity.class);
					startActivity(toIZaokeActivity);
					break;
				case R.id.main_iqx:
					Intent toQxActivity = new Intent(MainActivity.this, QxActivity.class);
					startActivity(toQxActivity);
					break;
				}
			}else if(isThreadComplete==true&&isReg==false){
				new Thread(new AutoRegHttp(MainActivity.this, mHandler, name)).start();
				isThreadComplete = false;
				Toast.makeText(MainActivity.this, "正在自动注册", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(MainActivity.this, "正在自动注册", Toast.LENGTH_SHORT).show();
			}

		}

	}

	public static class UpdateHandler extends Handler {

		/**
		 * 更新版本使用的URL
		 */
		private String downloadUrl;

		/**
		 * 更新进度
		 */
		private ProgressBar mProgress;

		/**
		 * 下载提示框
		 */
		private Dialog downloadDialog;

		private Activity context;

		public UpdateHandler(Activity context) {
			this.context = context;
		}

		protected void installApk(File file) {

			Intent intent = new Intent();

			intent.setAction(Intent.ACTION_VIEW);

			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");

			context.startActivity(intent);
		}

		@Override
		public void handleMessage(Message mes) {
			switch (mes.what) {
			case AppConstant.HANDLER_APK_STOP:
				Toast.makeText(context, "您的应用被禁止", 1).show();
				context.finish();
				break;
			case AppConstant.HANDLER_VERSION_UPDATE:
				String result = (String) mes.obj;
				JSONObject jo;
				String info = "";
				try {
					jo = new JSONObject(result);
					JSONObject data = jo.getJSONObject("data");
					info = jo.getString("info");
			        downloadUrl = data.getString("url");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	           
				AlertDialog.Builder builer = new Builder(context);
				builer.setTitle("升级提示");
				builer.setMessage(info.equals("")?"新版本发布了，强烈建议更新":info);

				builer.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								AlertDialog.Builder builder = new Builder(
										context);
								builder.setTitle("早客新版本下载更新中");
								final LayoutInflater inflater = LayoutInflater
										.from(context);
								View v = inflater.inflate(R.layout.update_progress,
										null);
								mProgress = (ProgressBar) v
										.findViewById(R.id.update_progress);
								builder.setView(v);
								downloadDialog = builder.create();
								downloadDialog.setCancelable(false);
								downloadDialog.show();
								new Thread(new ApkDownloadService(
										downloadUrl, UpdateHandler.this)).start();
							}
						});

				builer.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								context.finish();
							}
						});
				AlertDialog dialog = builer.create();
				dialog.show();
				break;
			case AppConstant.HANDLER_APK_DOWNLOAD_PROGRESS:
				mProgress.setProgress((Integer) mes.obj);
				break;
			case AppConstant.HANDLER_APK_DOWNLOAD_FINISH:
				File file = new File(AppConstant.BASE_DIR_PATH,
						AppConstant.APK_NAME);
				installApk(file);
				break;
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				Log.v("update", "检查失败");
				break;

			}
		}

	}
}
