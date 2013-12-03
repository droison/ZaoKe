package com.tiancikeji.zaoke.ui.adapter;

import java.io.Serializable;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.base.LocalBase;
import com.tiancikeji.zaoke.ui.ChangeLocalActivity;
import com.tiancikeji.zaoke.ui.R;
import com.tiancikeji.zaoke.util.ExitApplication;

public class ChangeLocalAdapter extends BaseAdapter {

	private Context mContext;
	private List<LocalBase> pick_locs;
	private LayoutInflater mInflater;

	public ChangeLocalAdapter(Context mContext, List<LocalBase> pick_locs) {
		super();
		this.mContext = mContext;
		this.pick_locs = pick_locs;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pick_locs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return pick_locs.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final LocalBase locs = pick_locs.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.change_local_item, null);
			viewHolder = new ViewHolder();

			viewHolder.title = (TextView) convertView.findViewById(R.id.change_local_item_title);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.change_local_item_image);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(locs.getPick_loc_name());

		if (locs.getPick_loc_list() != null && locs.getPick_loc_list().size() != 0) {
			viewHolder.image.setVisibility(View.VISIBLE);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent toChangeLocalActivity = new Intent();
					toChangeLocalActivity.setClass(mContext, ChangeLocalActivity.class);
					Bundle bundle = new Bundle();
					bundle.putBoolean("isRoot", false);
					bundle.putSerializable("pick_locs", (Serializable) locs.getPick_loc_list());
					toChangeLocalActivity.putExtras(bundle);
					mContext.startActivity(toChangeLocalActivity);

				}
			});

		} else {
			viewHolder.image.setVisibility(View.GONE);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AccountService as = new AccountService(mContext);
					Dbaccount account = as.getAccount();
					account.setLocal(locs.getPick_loc_name());
					account.setStarttime(locs.getPick_start_time());
					account.setEndtime(locs.getPick_end_time());
					account.setLocid(locs.getPick_loc_id());
					as.saveOrUpdate(account);
					ExitApplication.getInstance().exit();
				}
			});

		}

		return convertView;
	}

	class ViewHolder {

		TextView title;
		ImageView image;
	}

}
