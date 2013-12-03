package com.tiancikeji.zaoke.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class ZaokeAlertDialog{

	private Context context;
	private OnClickListener click1;
	private OnClickListener click2;
	private String info;
	private ZaokeAlertDialog(){
		
	};
	
	public ZaokeAlertDialog(Context context,String info,OnClickListener click1,OnClickListener click2){
		this.context = context;
		this.click1 = click1;
		this.click2 = click2;
		this.info =info;
	};
	
	public void creat(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("系统提示").setMessage(info)
		       .setPositiveButton("确定", click1).setNegativeButton("取消", click2);
		
		builder.create().show();
	}
}
