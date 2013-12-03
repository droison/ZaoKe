package com.tiancikeji.zaoke.ui;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.httpservice.BuyInfoHttp;
import com.tiancikeji.zaoke.httpservice.FoodListHttp;
import com.tiancikeji.zaoke.httpservice.base.BuyInfoBase;
import com.tiancikeji.zaoke.httpservice.base.FoodInfoBase;
import com.tiancikeji.zaoke.httpservice.base.FoodListBase;
import com.tiancikeji.zaoke.httpservice.base.OrderBase;
import com.tiancikeji.zaoke.util.AsyncImageLoader;
import com.tiancikeji.zaoke.util.AsyncImageLoader.ImageCallback;
import com.tiancikeji.zaoke.util.ExitApplication;
import com.tiancikeji.zaoke.util.GetImageFromLocal;
import com.tiancikeji.zaoke.util.NumberFormateUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class OrderActivity extends AbstractActivity {

	private ProgressBar order_pb;
	private ImageView order_shoppingcart;
	private LinearLayout order_linearlayout;
	private LinearLayout order_shoppingcart_layout;
	private RelativeLayout order_bottom;
	private ImageView order_bg;
	private RelativeLayout order_top;
	private View popView = null;
	private PopupWindow popWindow;
	private ImageView order_back;
	private ImageView order_switch;
	private TextView order_title;
	// private ImageView order_toleft;
	// private ImageView order_toright;
	private RelativeLayout order_story_layout;
	private ImageView order_shipin;
	private ImageView order_yinliao;
	private TextView order_story_text;
	private ScrollView order_story_scrolltext;
	private RelativeLayout order_joinorder_layout;
	private TextView order_go;
	private TextView order_shipin_price;
	private TextView order_yinliao_price;
	private TextView order_shipinname;
	private TextView order_yinliaoname;
	private TextView order_price;
	private TextView original_price;

	private TextView order_tags;
	private RelativeLayout order_tags_layout;
	private RelativeLayout order_tags_relativelayout;
	private TextView order_only_price;

	// private HorizontalScrollView horizontalScrollView;
	private FoodListBase foodListBase = new FoodListBase();
	private FoodInfoBase foodInfoBase;
	private AccountService accountService;
	private Dbaccount aDbaccount;
	private static final String TAG = "OrderActivity";
	private Boolean is_shipin = true;
	// 缩略图相关
	// private LinearLayout imageroot;
	private RelativeLayout order_image_bg;
	private int shipin_position = 0;
	private int yinliao_position = 0;
	private double shiwuPrice = 0;
	private double yinliaoPrice = 0;
	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	private TranslateAnimation mShowAction, mHiddenAction;
	private String shiwuUrl;
	private String yinliaoUrl;
	private String currentUrl;

	private ViewPager viewPager_shipin, viewPager_yinliao;
	private List<View> pagerViews_shipin, pagerViews_yinliao;
	private AsyncImageLoader asyncImageLoader;
	/*
	 * private FoodInfoBase shipin = new FoodInfoBase(); private FoodInfoBase
	 * yinliao = new FoodInfoBase();
	 */
	private OrderBase orderBase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		init();

	}

	public void init() {
		orderBase = new OrderBase();

		order_top = (RelativeLayout) findViewById(R.id.order_top);
		order_top.getBackground().setAlpha(200);
		order_bottom = (RelativeLayout) findViewById(R.id.order_bottom);
		order_bottom.getBackground().setAlpha(200);
		order_back = (ImageView) findViewById(R.id.order_back);
		order_switch = (ImageView) findViewById(R.id.order_switch);
		order_title = (TextView) findViewById(R.id.order_title);

		order_bottom = (RelativeLayout) findViewById(R.id.order_bottom);
		order_shoppingcart = (ImageView) findViewById(R.id.order_shoppingcart);
		order_linearlayout = (LinearLayout) findViewById(R.id.order_linearlayout);
		order_shoppingcart_layout = (LinearLayout) findViewById(R.id.order_shoppingcart_layout);
		order_story_layout = (RelativeLayout) findViewById(R.id.order_story_layout);
		order_story_text = (TextView) findViewById(R.id.order_story_text);
		order_story_scrolltext = (ScrollView) findViewById(R.id.order_story_scrolltext);
		order_joinorder_layout = (RelativeLayout) findViewById(R.id.order_joinorder_layout);
		order_go = (TextView) findViewById(R.id.order_go);
		order_shipin = (ImageView) findViewById(R.id.order_shipin);
		order_yinliao = (ImageView) findViewById(R.id.order_yinliao);
		order_shipin_price = (TextView) findViewById(R.id.order_shipin_price);
		order_yinliao_price = (TextView) findViewById(R.id.order_yinliao_price);
		order_shipinname = (TextView) findViewById(R.id.order_shipinname);
		order_yinliaoname = (TextView) findViewById(R.id.order_yinliaoname);
		order_price = (TextView) findViewById(R.id.order_price);
		original_price = (TextView) this.findViewById(R.id.original_price);
		order_pb = (ProgressBar) findViewById(R.id.order_pb);
		order_pb.setVisibility(ProgressBar.INVISIBLE);

		pagerViews_shipin = new ArrayList<View>();
		pagerViews_yinliao = new ArrayList<View>();
		viewPager_shipin = (ViewPager) findViewById(R.id.order_viewpager_shipin);
		viewPager_yinliao = (ViewPager) findViewById(R.id.order_viewpager_yinliao);

		// 监听按钮
		// order_bg.setOnClickListener(new onclick());
		order_go.setOnClickListener(new onclick());
		order_story_layout.setOnClickListener(new onclick());
		order_joinorder_layout.setOnClickListener(new onclick());

		order_shoppingcart.setOnClickListener(new onclick());
		order_back.setOnClickListener(new onclick());
		order_switch.setOnClickListener(new onclick());
		order_title.setOnClickListener(new onclick());
		order_shipin.setOnClickListener(new onclick());
		order_yinliao.setOnClickListener(new onclick());

		accountService = new AccountService(this);
		aDbaccount = new Dbaccount();
		aDbaccount = accountService.getAccount();
		asyncImageLoader = new AsyncImageLoader();
		// order_bg.setImageResource(R.drawable.sanwish1);
		// 设置动画效果
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.8f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(300);
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.8f, Animation.RELATIVE_TO_SELF, 0.0f);
		mHiddenAction.setDuration(300);
		// 联网初始化
		new Thread(new FoodListHttp(this, mHandler)).start();
		showProgressDialog("正在加载最新数据");

	}

	// 添加视频pager
	public void initShiPinPager(FoodListBase flb) {
		for (int i = 0; i < flb.getShipin().size(); i++) {
			View v = View.inflate(this, R.layout.activity_order_item, null);
			order_bg = (ImageView) v.findViewById(R.id.order_bg);
			order_tags = (TextView) v.findViewById(R.id.order_tags);
			order_tags_layout = (RelativeLayout) v.findViewById(R.id.order_tags_layout);
			order_tags_relativelayout = (RelativeLayout) v.findViewById(R.id.order_tags_relativelayout);
			order_only_price = (TextView) v.findViewById(R.id.order_only_price);

			setTags(order_tags, flb.getShipin().get(i).getTags(), order_tags_relativelayout);
			order_only_price.setText(String.valueOf("￥" + flb.getShipin().get(i).getSale_price()));
			pagerViews_shipin.add(v);

		}
	}

	// 添加视频pager
	public void initYinLiaoPager(FoodListBase flb) {
		for (int i = 0; i < flb.getYinliao().size(); i++) {
			View v = View.inflate(this, R.layout.activity_order_item, null);
			order_bg = (ImageView) v.findViewById(R.id.order_bg);
			order_tags = (TextView) v.findViewById(R.id.order_tags);
			order_tags_layout = (RelativeLayout) v.findViewById(R.id.order_tags_layout);
			order_tags_relativelayout = (RelativeLayout) v.findViewById(R.id.order_tags_relativelayout);
			order_only_price = (TextView) v.findViewById(R.id.order_only_price);
			setTags(order_tags, flb.getYinliao().get(i).getTags(), order_tags_relativelayout);
			order_only_price.setText(String.valueOf("￥" + flb.getYinliao().get(i).getSale_price()));
			pagerViews_yinliao.add(v);
		}
	}

	// 设置背景图片
	public void setImageBg(final ImageView imageView, String url) {

		Drawable temp = new GetImageFromLocal().getFromlocal(OrderActivity.this, url);
		if (temp == null) {
			imageView.setImageResource(R.drawable.open_fail);
		} else {
			imageView.setImageDrawable(temp);
		}

		if (url == null) {
			imageView.setImageResource(R.drawable.image_bg);
		}
	}

	// 设置标签
	public void setTags(TextView view, List<String> tags, RelativeLayout relativeLayout) {
		if (tags == null) {
			relativeLayout.setVisibility(View.INVISIBLE);
		} else {
			relativeLayout.setVisibility(View.VISIBLE);

			System.out.println(tags);
			view.setText(tags.toString());
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:

				Log.d(TAG, "获取成功");
				foodListBase = (FoodListBase) msg.obj;
				order_title.setText(foodListBase.getShipin().get(shipin_position).getName().toString() + "(" + (shipin_position + 1) + "/" + foodListBase.getShipin().size() + ")");
				order_story_text.setText(foodListBase.getShipin().get(shipin_position).getDesc().toString());

				initShiPinPager(foodListBase);
				initYinLiaoPager(foodListBase);
				viewPager_shipin.setAdapter(new MyViewPagerAdapter(pagerViews_shipin));
				viewPager_shipin.setOnPageChangeListener(new pagerChanger_shipin());
				viewPager_yinliao.setAdapter(new MyViewPagerAdapter(pagerViews_yinliao));
				viewPager_yinliao.setOnPageChangeListener(new pagerChanger_linliao());
				order_pb.setVisibility(ProgressBar.VISIBLE);
				asyncImageLoader.loadBitmap(OrderActivity.this, foodListBase.getShipin().get(shipin_position).getImage_url(), new ImageCallback() {

					@Override
					public void imageLoaded(Bitmap bitmap, String imageUrl) {
						order_pb.setVisibility(ProgressBar.INVISIBLE);
						ImageView view = (ImageView) pagerViews_shipin.get(shipin_position).findViewById(R.id.order_bg);
						view.setImageBitmap(bitmap);
					}
				});

				
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

	private Handler getCombHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			dismissProgressDialog();
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				BuyInfoBase bib = (BuyInfoBase) msg.obj;
				orderBase.setCombinePrice(bib.getCombo_price() == null ? bib.getSale_price() : bib.getCombo_price());
				orderBase.setPrice(bib.getSale_price());
				order_price.setText("￥" + NumberFormateUtil.Formate(bib.getCombo_price() == null ? bib.getSale_price() : bib.getCombo_price()));
				original_price.setText("￥" + bib.getSale_price());
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

	public void focusChange() {
		order_linearlayout.setVisibility(View.GONE);
		order_bottom.setVisibility(View.VISIBLE);
	}

	public void orderSwitch_shipin() {
		releaseImage();
		is_shipin = true;
		order_switch.setImageResource(R.drawable.change_drink_selector);
		order_title.setText(foodListBase.getShipin().get(shipin_position).getName().toString() + "(" + (shipin_position + 1) + "/" + foodListBase.getShipin().size() + ")");

		viewPager_yinliao.setVisibility(View.GONE);
		viewPager_shipin.setVisibility(View.VISIBLE);
		order_pb.setVisibility(ProgressBar.VISIBLE);
		asyncImageLoader.loadBitmap(OrderActivity.this, foodListBase.getShipin().get(shipin_position).getImage_url(), new ImageCallback() {

			@Override
			public void imageLoaded(Bitmap bitmap, String imageUrl) {
				order_pb.setVisibility(ProgressBar.INVISIBLE);
				ImageView view = (ImageView) pagerViews_shipin.get(shipin_position).findViewById(R.id.order_bg);
				view.setImageBitmap(bitmap);
			}
		});

		
		order_story_text.setText(foodListBase.getShipin().get(shipin_position).getDesc());
	}

	public void orderSwitch_yinliao() {
		releaseImage();
		is_shipin = false;
		order_switch.setImageResource(R.drawable.change_sandwich_selector);
		order_title.setText(foodListBase.getYinliao().get(yinliao_position).getName().toString() + "(" + (yinliao_position + 1) + "/" + foodListBase.getYinliao().size() + ")");

		viewPager_yinliao.setVisibility(View.VISIBLE);
		viewPager_shipin.setVisibility(View.GONE);
		// 由于 视频和饮料的都加载了，而只下载了 视频的第一张图片。所以切换的时候需要下载一下
		order_pb.setVisibility(ProgressBar.VISIBLE);
		asyncImageLoader.loadBitmap(OrderActivity.this, foodListBase.getYinliao().get(yinliao_position).getImage_url(), new ImageCallback() {

			@Override
			public void imageLoaded(Bitmap bitmap, String imageUrl) {
				order_pb.setVisibility(ProgressBar.INVISIBLE);
				ImageView view = (ImageView) pagerViews_yinliao.get(yinliao_position).findViewById(R.id.order_bg);
				view.setImageBitmap(bitmap);
			}
		});

		
		order_story_text.setText(foodListBase.getYinliao().get(yinliao_position).getDesc());
	}

	private class onclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.order_shoppingcart:
				if (order_linearlayout.getVisibility() == View.VISIBLE) {
					order_linearlayout.setVisibility(View.GONE);
					order_bottom.setVisibility(View.VISIBLE);
				} else if (order_linearlayout.getVisibility() == View.GONE) {
					order_shoppingcart_layout.startAnimation(mShowAction);
					order_linearlayout.setVisibility(View.VISIBLE);
					order_bottom.setVisibility(View.INVISIBLE);
					if (!TextUtils.isEmpty(order_shipin_price.getText()) && !TextUtils.isEmpty(order_yinliao_price.getText())) {
						showProgressDialog("正在获取组合价格");
						order_price.setText("");
						new Thread(new BuyInfoHttp(OrderActivity.this, getCombHandler, orderBase.getShipin().getId(), orderBase.getYinliao().getId())).start();
					} else {
						order_price.setText(original_price.getText().toString());
						orderBase.setCombinePrice(orderBase.getPrice());
					}
				}
				break;
			case R.id.order_back:
				finish();
				break;
			case R.id.order_switch:
				focusChange();
				if (!is_shipin) {
					orderSwitch_shipin();
				} else {
					orderSwitch_yinliao();
				}
				break;
			case R.id.order_go:

				Intent toShopingcartActivity = new Intent(OrderActivity.this, ShopingCartActivity.class);
				if (orderBase.getShipin() == null && orderBase.getYinliao() == null) {
					Toast.makeText(OrderActivity.this, "请至少选择一件商品", Toast.LENGTH_SHORT).show();
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putSerializable("orderBase", orderBase);
				ExitApplication.getInstance().setOrderActivity(OrderActivity.this);
				toShopingcartActivity.putExtras(bundle);
				startActivity(toShopingcartActivity);
				break;
			case R.id.order_story_layout:
				if (order_story_scrolltext.getVisibility() == View.VISIBLE) {
					order_story_scrolltext.setVisibility(View.GONE);
				} else {
					order_story_scrolltext.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.order_joinorder_layout:

				if (!is_shipin) {// 饮料
					foodInfoBase = foodListBase.getYinliao().get(yinliao_position);
					yinliaoUrl = foodListBase.getYinliao().get(yinliao_position).getImage_url();
					Drawable temp = new GetImageFromLocal().getThumbFromlocal(OrderActivity.this, yinliaoUrl);
					if (temp != null)
						order_yinliao.setImageDrawable(temp);
					Toast.makeText(OrderActivity.this, "添加饮料" + foodInfoBase.getName() + "成功", Toast.LENGTH_SHORT).show();
					yinliaoPrice = foodInfoBase.getSale_price();
					order_yinliao_price.setText("￥" + yinliaoPrice + "");
					order_yinliaoname.setText(foodInfoBase.getName());
					orderBase.setPrice(shiwuPrice + yinliaoPrice);
					original_price.setText(String.valueOf("￥" + orderBase.getPrice()));
					orderBase.setYinliao(foodInfoBase);

					orderSwitch_shipin();
				} else {// 食品
					foodInfoBase = foodListBase.getShipin().get(shipin_position);
					shiwuUrl = foodListBase.getShipin().get(shipin_position).getImage_url();
					Drawable temp = new GetImageFromLocal().getThumbFromlocal(OrderActivity.this, shiwuUrl);
					if (temp != null)
						order_shipin.setImageDrawable(temp);
					Toast.makeText(OrderActivity.this, "添加食品" + foodInfoBase.getName() + "成功", Toast.LENGTH_SHORT).show();
					shiwuPrice = foodInfoBase.getSale_price();
					order_shipin_price.setText(String.valueOf("￥" + shiwuPrice));
					order_shipinname.setText(foodInfoBase.getName());
					orderBase.setPrice(shiwuPrice + yinliaoPrice);
					original_price.setText(String.valueOf("￥" + orderBase.getPrice()));
					orderBase.setShipin(foodInfoBase);

					orderSwitch_yinliao();
				}
				break;

			case R.id.order_shipin:
				order_shipin.setImageResource(R.drawable.change_sandwich_hover);
				order_shipin_price.setText("");
				order_shipinname.setText(R.string.cart_nonselected);
				original_price.setText(order_yinliao_price.getText());
				order_price.setText(order_yinliao_price.getText());
				orderBase.setShipin(null);
				shiwuUrl = null;
				shiwuPrice = 0;
				break;
			case R.id.order_yinliao:
				order_yinliao.setImageResource(R.drawable.change_drink_hover);
				order_yinliao_price.setText("");
				order_yinliaoname.setText(R.string.cart_nonselected);
				original_price.setText(order_shipin_price.getText());
				order_price.setText(order_shipin_price.getText());
				orderBase.setYinliao(null);
				yinliaoUrl = null;
				yinliaoPrice = 0;
				break;
			}
		}
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}

		public List<View> getmListViews() {
			return mListViews;
		}

		public void setmListViews(List<View> mListViews) {
			this.mListViews = mListViews;
		}

	}

	public class pagerChanger_shipin implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(final int arg0) {

			shipin_position = arg0;
			if(asyncImageLoader.getUrlSize()==5){
				orderSwitch_shipin();
				return;
			}
			order_title.setText(foodListBase.getShipin().get(arg0).getName().toString() + "(" + (shipin_position + 1) + "/" + foodListBase.getShipin().size() + ")");
			order_story_text.setText(foodListBase.getShipin().get(shipin_position).getDesc());
			order_pb.setVisibility(ProgressBar.VISIBLE);
			asyncImageLoader.loadBitmap(OrderActivity.this, foodListBase.getShipin().get(arg0).getImage_url(), new ImageCallback() {

				@Override
				public void imageLoaded(Bitmap bitmap, String imageUrl) {
					order_pb.setVisibility(ProgressBar.INVISIBLE);
					ImageView view = (ImageView) pagerViews_shipin.get(shipin_position).findViewById(R.id.order_bg);
					view.setImageBitmap(bitmap);
				}
			});
			
		}

	}

	public class pagerChanger_linliao implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(final int arg0) {
			
			yinliao_position = arg0;
			if(asyncImageLoader.getUrlSize()==5){
				orderSwitch_yinliao();
				return;
			}
			order_title.setText(foodListBase.getYinliao().get(arg0).getName().toString() + "(" + (yinliao_position + 1) + "/" + foodListBase.getYinliao().size() + ")");
			order_story_text.setText(foodListBase.getYinliao().get(yinliao_position).getDesc());
			order_pb.setVisibility(ProgressBar.VISIBLE);
			asyncImageLoader.loadBitmap(OrderActivity.this, foodListBase.getYinliao().get(arg0).getImage_url(), new ImageCallback() {

				@Override
				public void imageLoaded(Bitmap bitmap, String imageUrl) {
					order_pb.setVisibility(ProgressBar.INVISIBLE);
					ImageView view = (ImageView) pagerViews_yinliao.get(yinliao_position).findViewById(R.id.order_bg);
					view.setImageBitmap(bitmap);
				}
			});
			
		}
	}

	public void releaseImage() {
		for (View v : pagerViews_yinliao) {
			ImageView view = (ImageView) v.findViewById(R.id.order_bg);
			/*if (view.getDrawingCache() != null)
				view.getDrawingCache().recycle();*/
			view.setImageBitmap(null);
		}
		for (View v : pagerViews_shipin) {
			ImageView view = (ImageView) v.findViewById(R.id.order_bg);
			/*if (view.getDrawingCache() != null)
				view.getDrawingCache().recycle();*/
			view.setImageBitmap(null);
		}
		asyncImageLoader.releaseAll();
		System.gc();
	}

}

// 以前的版本
/*
 * @Override protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState); setContentView(R.layout.activity_order);
 * init();
 * 
 * }
 * 
 * public void init() { orderBase = new OrderBase(); order_toleft = (ImageView)
 * findViewById(R.id.order_toleft); order_toright = (ImageView)
 * findViewById(R.id.order_toright); horizontalScrollView =
 * (HorizontalScrollView) findViewById(R.id.order_scroll); order_top =
 * (RelativeLayout) findViewById(R.id.order_top);
 * order_top.getBackground().setAlpha(200); order_bottom = (RelativeLayout)
 * findViewById(R.id.order_top); order_bottom.getBackground().setAlpha(200);
 * order_back = (ImageView) findViewById(R.id.order_back); order_switch =
 * (ImageView) findViewById(R.id.order_switch); order_title = (TextView)
 * findViewById(R.id.order_title); order_bg = (ImageView)
 * findViewById(R.id.order_bg); order_bottom = (RelativeLayout)
 * findViewById(R.id.order_bottom); order_shoppingcart = (ImageView)
 * findViewById(R.id.order_shoppingcart); order_linearlayout = (LinearLayout)
 * findViewById(R.id.order_linearlayout); order_shoppingcart_layout =
 * (LinearLayout) findViewById(R.id.order_shoppingcart_layout);
 * order_story_layout = (RelativeLayout) findViewById(R.id.order_story_layout);
 * order_story_text = (TextView) findViewById(R.id.order_story_text);
 * order_story_scrolltext = (ScrollView)
 * findViewById(R.id.order_story_scrolltext); order_joinorder_layout =
 * (RelativeLayout) findViewById(R.id.order_joinorder_layout); order_go =
 * (TextView) findViewById(R.id.order_go); order_shipin = (ImageView)
 * findViewById(R.id.order_shipin); order_yinliao = (ImageView)
 * findViewById(R.id.order_yinliao); order_shipin_price = (TextView)
 * findViewById(R.id.order_shipin_price); order_yinliao_price = (TextView)
 * findViewById(R.id.order_yinliao_price); order_shipinname = (TextView)
 * findViewById(R.id.order_shipinname); order_yinliaoname = (TextView)
 * findViewById(R.id.order_yinliaoname); order_price = (TextView)
 * findViewById(R.id.order_price); original_price = (TextView)
 * this.findViewById(R.id.original_price); order_tags =
 * (TextView)findViewById(R.id.order_tags); order_tags_layout =
 * (RelativeLayout)findViewById(R.id.order_tags_layout);
 * order_tags_relativelayout =
 * (RelativeLayout)findViewById(R.id.order_tags_relativelayout);
 * order_only_price = (TextView)findViewById(R.id.order_only_price);
 * 
 * 
 * viewPager_shipin = new ArrayList<View>(); viewPager_yinliao = new
 * ArrayList<View>(); viewPager_shipin = (ViewPager)
 * findViewById(R.id.order_viewpager_shipin); viewPager_yinliao = (ViewPager)
 * findViewById(R.id.viewPager_yinliao);
 * 
 * // 监听按钮 order_bg.setOnClickListener(new onclick());
 * order_go.setOnClickListener(new onclick());
 * order_story_layout.setOnClickListener(new onclick());
 * order_joinorder_layout.setOnClickListener(new onclick());
 * order_toleft.setOnClickListener(new onclick());
 * order_toright.setOnClickListener(new onclick());
 * order_shoppingcart.setOnClickListener(new onclick());
 * order_back.setOnClickListener(new onclick());
 * order_switch.setOnClickListener(new onclick());
 * order_title.setOnClickListener(new onclick());
 * order_shipin.setOnClickListener(new onclick());
 * order_yinliao.setOnClickListener(new onclick());
 * 
 * accountService = new AccountService(this); aDbaccount = new Dbaccount();
 * aDbaccount = accountService.getAccount();
 * order_bg.setImageResource(R.drawable.sanwish1); // 设置动画效果 mShowAction = new
 * TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
 * Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.8f,
 * Animation.RELATIVE_TO_SELF, 0.0f); mShowAction.setDuration(300);
 * mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
 * Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.8f,
 * Animation.RELATIVE_TO_SELF, 0.0f); mHiddenAction.setDuration(300); imageroot
 * = (LinearLayout) findViewById(R.id.tagsLinearLayout); // 联网初始化 new Thread(new
 * FoodListHttp(this, mHandler)).start(); showProgressDialog("正在加载最新数据");
 * 
 * }
 * 
 * public void addImage() {
 * 
 * drawables.clear(); if (is_shipin) { for (int i = 0; i <
 * foodListBase.getShipin().size(); i++) { View view =
 * getLayoutInflater().inflate(R.layout.order_image, null); final ImageView
 * order_image; order_image_bg = (RelativeLayout)
 * view.findViewById(R.id.order_image_bg); order_image = (ImageView)
 * view.findViewById(R.id.order_image);
 * order_image.setImageResource(R.drawable.change_sandwich_hover);
 * imageroot.addView(view); order_image.setTag(i); if (i == 0) {
 * order_image.setBackgroundColor(getResources().getColor(R.color.orange));
 * currentUrl = foodListBase.getShipin().get(i).getImage_url(); Drawable temp =
 * new GetImageFromLocal().getFromlocal(OrderActivity.this, currentUrl); if
 * (temp != null) order_bg.setImageDrawable(temp); }
 * 
 * Drawable temp = new GetImageFromLocal().getThumbFromlocal(OrderActivity.this,
 * foodListBase.getShipin().get(i).getImage_url()); if (temp != null)
 * order_image.setImageDrawable(temp);
 * 
 * order_image.setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View v) { k = (Integer) v.getTag();
 * order_title.setText(foodListBase.getShipin().get(k).getName().toString());
 * order_story_text
 * .setText(foodListBase.getShipin().get(k).getDesc().toString());
 * setTags(order_tags, foodListBase.getShipin().get(k).getTags());
 * order_only_price
 * .setText(foodListBase.getShipin().get(k).getSale_price().toString()); for(int
 * i=0;i<foodListBase.getShipin().size();i++){ ImageView image = (ImageView)
 * imageroot.findViewWithTag(i);
 * image.setBackgroundColor(getResources().getColor(R.color.TextColorWhite)); }
 * 
 * order_image.setBackgroundColor(getResources().getColor(R.color.orange));
 * currentUrl = foodListBase.getShipin().get(k).getImage_url(); Drawable temp =
 * new GetImageFromLocal().getFromlocal(OrderActivity.this, currentUrl); if
 * (temp != null) order_bg.setImageDrawable(temp); } }); } } else { for (int i =
 * 0; i < foodListBase.getYinliao().size(); i++) { View view =
 * getLayoutInflater().inflate(R.layout.order_image, null); final ImageView
 * order_image; order_image_bg = (RelativeLayout)
 * view.findViewById(R.id.order_image_bg); order_image = (ImageView)
 * view.findViewById(R.id.order_image);
 * order_image.setImageResource(R.drawable.change_drink_hover);
 * imageroot.addView(view); order_image.setTag(i); if (i == 0) {
 * order_image.setBackgroundColor(getResources().getColor(R.color.orange));
 * currentUrl = foodListBase.getYinliao().get(i).getImage_url(); Drawable temp =
 * new GetImageFromLocal().getFromlocal(OrderActivity.this, currentUrl); if
 * (temp != null) order_bg.setImageDrawable(temp); }
 * 
 * Drawable temp = new GetImageFromLocal().getThumbFromlocal(OrderActivity.this,
 * foodListBase.getYinliao().get(i).getImage_url()); if (temp != null)
 * order_image.setImageDrawable(temp);
 * 
 * order_image.setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View v) {
 * 
 * k = (Integer) v.getTag();
 * order_title.setText(foodListBase.getYinliao().get(k).getName().toString());
 * order_story_text
 * .setText(foodListBase.getYinliao().get(k).getDesc().toString());
 * setTags(order_tags, foodListBase.getYinliao().get(k).getTags());
 * order_only_price
 * .setText(foodListBase.getYinliao().get(k).getSale_price().toString());
 * for(int i=0;i<foodListBase.getYinliao().size();i++){ ImageView image =
 * (ImageView) imageroot.findViewWithTag(i);
 * image.setBackgroundColor(getResources().getColor(R.color.TextColorWhite)); }
 * 
 * order_image.setBackgroundColor(getResources().getColor(R.color.orange));
 * 
 * currentUrl = foodListBase.getYinliao().get(k).getImage_url(); Drawable temp =
 * new GetImageFromLocal().getFromlocal(OrderActivity.this, currentUrl); if
 * (temp != null) order_bg.setImageDrawable(temp); } }); } } }
 * 
 * 
 * public void setTags(TextView view, String tags){ if(tags == null){
 * order_tags_relativelayout.setVisibility(View.INVISIBLE); }else{
 * order_tags_relativelayout.setVisibility(View.VISIBLE); tags.replace("[", "");
 * tags.replace("]", ""); tags.replaceAll("\"",""); System.out.println(tags);
 * view.setText(tags); } } private Handler mHandler = new Handler() {
 * 
 * @Override public void handleMessage(Message msg) { dismissProgressDialog();
 * super.handleMessage(msg); switch (msg.what) { case
 * AppConstant.HANDLER_MESSAGE_NORMAL: Log.d(TAG, "获取成功"); foodListBase =
 * (FoodListBase) msg.obj;
 * order_title.setText(foodListBase.getShipin().get(k).getName().toString());
 * 
 * setTags(order_tags, foodListBase.getShipin().get(k).getTags());
 * 
 * 
 * order_story_text.setText(foodListBase.getShipin().get(k).getDesc().toString())
 * ;
 * order_only_price.setText(foodListBase.getShipin().get(k).getSale_price().toString
 * ()); addImage(); updateView(); startThread(foodListBase); break; case
 * AppConstant.HANDLER_HTTPSTATUS_ERROR: displayResponse("服务器访问失败"); finish();
 * break; case AppConstant.HANDLER_MESSAGE_NONETWORK: showNoNetWork(); finish();
 * break; case AppConstant.HANDLER_MESSAGE_TIMEOUT: displayResponse("网络访问超时");
 * finish(); break; } } };
 * 
 * private Handler getCombHandler = new Handler() {
 * 
 * @Override public void handleMessage(Message msg) { // TODO Auto-generated
 * method stub super.handleMessage(msg); dismissProgressDialog(); switch
 * (msg.what) { case AppConstant.HANDLER_MESSAGE_NORMAL: BuyInfoBase bib =
 * (BuyInfoBase) msg.obj; orderBase.setCombinePrice(bib.getCombo_price());
 * orderBase.setPrice(bib.getSale_price()); order_price.setText("" +
 * NumberFormateUtil.Formate(bib.getCombo_price())); original_price.setText("" +
 * bib.getSale_price()); break; case AppConstant.HANDLER_HTTPSTATUS_ERROR:
 * displayResponse("服务器访问失败"); break; case
 * AppConstant.HANDLER_MESSAGE_NONETWORK: showNoNetWork(); break; case
 * AppConstant.HANDLER_MESSAGE_TIMEOUT: displayResponse("网络访问超时"); break; } } };
 * 
 * public void focusChange() { order_linearlayout.setVisibility(View.GONE);
 * order_bottom.setVisibility(View.VISIBLE); }
 * 
 * private class onclick implements OnClickListener {
 * 
 * @Override public void onClick(View v) { // TODO Auto-generated method stub
 * switch (v.getId()) { case R.id.order_toleft:
 * horizontalScrollView.arrowScroll(View.FOCUS_LEFT); focusChange(); break; case
 * R.id.order_toright: horizontalScrollView.arrowScroll(View.FOCUS_RIGHT);
 * focusChange(); break; case R.id.order_shoppingcart: if
 * (order_linearlayout.getVisibility() == View.VISIBLE) { //
 * order_shoppingcart_layout.startAnimation(mHiddenAction);
 * order_linearlayout.setVisibility(View.GONE);
 * order_tags_layout.setVisibility(View.VISIBLE);
 * order_bottom.setVisibility(View.VISIBLE);
 * 
 * 
 * } else if (order_linearlayout.getVisibility() == View.GONE) {
 * order_shoppingcart_layout.startAnimation(mShowAction);
 * order_linearlayout.setVisibility(View.VISIBLE);
 * order_tags_layout.setVisibility(View.GONE);
 * order_bottom.setVisibility(View.INVISIBLE);
 * 
 * SpannableString msp = new
 * SpannableString(original_price.getText().toString()); msp.setSpan(new
 * StrikethroughSpan(), 0, original_price.getText().toString().length(),
 * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); original_price.setText(msp);
 * 
 * if (!TextUtils.isEmpty(order_shipin_price.getText()) &&
 * !TextUtils.isEmpty(order_yinliao_price.getText())) {
 * showProgressDialog("正在获取组合价格"); order_price.setText(""); new Thread(new
 * BuyInfoHttp(OrderActivity.this, getCombHandler,
 * orderBase.getShipin().getId(), orderBase.getYinliao().getId())).start(); }
 * else { order_price.setText(original_price.getText().toString());
 * orderBase.setCombinePrice(orderBase.getPrice()); } } // showPopupWindow(v);
 * break; case R.id.order_back: finish(); break; case R.id.order_switch:
 * focusChange(); if (!is_shipin) { imageroot.removeAllViews(); is_shipin =
 * true; addImage(); updateView(); k = 0;
 * order_switch.setImageResource(R.drawable.change_drink_selector);
 * order_title.setText(foodListBase.getShipin().get(0).getName().toString());
 * order_story_text
 * .setText(foodListBase.getShipin().get(k).getDesc().toString());
 * setTags(order_tags, foodListBase.getShipin().get(k).getTags());
 * order_only_price
 * .setText(foodListBase.getShipin().get(k).getSale_price().toString()); } else
 * { imageroot.removeAllViews(); is_shipin = false; addImage(); updateView(); k
 * = 0; order_switch.setImageResource(R.drawable.change_sandwich_selector);
 * order_title.setText(foodListBase.getYinliao().get(0).getName().toString());
 * order_story_text
 * .setText(foodListBase.getYinliao().get(k).getDesc().toString());
 * setTags(order_tags, foodListBase.getYinliao().get(k).getTags());
 * order_only_price
 * .setText(foodListBase.getYinliao().get(k).getSale_price().toString()); }
 * break; case R.id.order_go:
 * 
 * Intent toShopingcartActivity = new Intent(OrderActivity.this,
 * ShopingCartActivity.class); if (orderBase.getShipin() == null &&
 * orderBase.getYinliao() == null) { Toast.makeText(OrderActivity.this,
 * "请至少选择一件商品", Toast.LENGTH_SHORT).show(); return; } Bundle bundle = new
 * Bundle(); bundle.putSerializable("orderBase", orderBase); //
 * bundle.putSerializable("image", (Serializable) // shipin_drawables.get(k));
 * ExitApplication.getInstance().setOrderActivity(OrderActivity.this);
 * toShopingcartActivity.putExtras(bundle);
 * startActivity(toShopingcartActivity); break; case R.id.order_story_layout: if
 * (order_story_scrolltext.getVisibility() == View.VISIBLE) {
 * order_story_scrolltext.setVisibility(View.GONE); } else {
 * order_story_scrolltext.setVisibility(View.VISIBLE); } break; case
 * R.id.order_joinorder_layout: if (k > -1) { if (!is_shipin) {// 饮料
 * foodInfoBase = foodListBase.getYinliao().get(k);
 * 
 * yinliaoUrl = foodListBase.getYinliao().get(k).getImage_url(); Drawable temp =
 * new GetImageFromLocal().getThumbFromlocal(OrderActivity.this, yinliaoUrl); if
 * (temp != null) order_yinliao.setImageDrawable(temp);
 * 
 * Toast.makeText(OrderActivity.this, "添加饮料" + foodInfoBase.getName() + "成功",
 * Toast.LENGTH_SHORT).show(); yinliaoPrice = foodInfoBase.getSale_price();
 * order_yinliao_price.setText(yinliaoPrice + "");
 * order_yinliaoname.setText(foodInfoBase.getName());
 * orderBase.setPrice(shiwuPrice + yinliaoPrice);
 * original_price.setText(String.valueOf(orderBase.getPrice()));
 * orderBase.setYinliao(foodInfoBase); } else {// 食品 foodInfoBase =
 * foodListBase.getShipin().get(k);
 * 
 * shiwuUrl = foodListBase.getShipin().get(k).getImage_url(); Drawable temp =
 * new GetImageFromLocal().getThumbFromlocal(OrderActivity.this, shiwuUrl); if
 * (temp != null) order_shipin.setImageDrawable(temp);
 * 
 * Toast.makeText(OrderActivity.this, "添加食品" + foodInfoBase.getName() + "成功",
 * Toast.LENGTH_SHORT).show(); shiwuPrice = foodInfoBase.getSale_price();
 * order_shipin_price.setText(String.valueOf(shiwuPrice));
 * order_shipinname.setText(foodInfoBase.getName());
 * orderBase.setPrice(shiwuPrice + yinliaoPrice);
 * original_price.setText(String.valueOf(orderBase.getPrice()));
 * orderBase.setShipin(foodInfoBase); } } break; case R.id.order_bg:
 * OrderActivity.this.focusChange(); break; case R.id.order_shipin:
 * order_shipin.setImageResource(R.drawable.change_sandwich_hover);
 * order_shipin_price.setText("");
 * order_shipinname.setText(R.string.cart_nonselected);
 * original_price.setText(order_yinliao_price.getText());
 * order_price.setText(order_yinliao_price.getText()); //
 * original_price.setText(text) orderBase.setShipin(null); shiwuUrl = null;
 * shiwuPrice = 0; break; case R.id.order_yinliao:
 * order_yinliao.setImageResource(R.drawable.change_drink_hover);
 * order_yinliao_price.setText("");
 * order_yinliaoname.setText(R.string.cart_nonselected);
 * original_price.setText(order_shipin_price.getText());
 * order_price.setText(order_shipin_price.getText());
 * orderBase.setYinliao(null); yinliaoUrl = null; yinliaoPrice = 0; break; } } }
 * 
 * public void startThread(FoodListBase foodList){ List<FoodInfoBase> fibase =
 * foodList.getShipin(); for(int i=0;i<fibase.size();i++){ new
 * NewAsyncImageLoader().loadDrawable(OrderActivity.this,
 * fibase.get(i).getImage_url(), this); } fibase = foodList.getYinliao();
 * for(int i=0;i<fibase.size();i++){ new
 * NewAsyncImageLoader().loadDrawable(OrderActivity.this,
 * fibase.get(i).getImage_url(), this); } }
 * 
 * public void updateView() {
 * 
 * Drawable temp = null; if (yinliaoUrl != null) { temp = new
 * GetImageFromLocal().getThumbFromlocal(OrderActivity.this, yinliaoUrl); if
 * (temp != null) order_yinliao.setImageDrawable(temp); } if (shiwuUrl != null)
 * { temp = new GetImageFromLocal().getThumbFromlocal(OrderActivity.this,
 * shiwuUrl); if (temp != null) order_shipin.setImageDrawable(temp); }
 * 
 * if(is_shipin){ for(int i=0;i<foodListBase.getShipin().size();i++){ ImageView
 * image = (ImageView) imageroot.findViewWithTag(i); temp = new
 * GetImageFromLocal().getThumbFromlocal(OrderActivity.this,
 * foodListBase.getShipin().get(i).getImage_url()); if (temp != null)
 * image.setImageDrawable(temp); } }else{ for(int
 * i=0;i<foodListBase.getYinliao().size();i++){ ImageView image = (ImageView)
 * imageroot.findViewWithTag(i); temp = new
 * GetImageFromLocal().getThumbFromlocal(OrderActivity.this,
 * foodListBase.getYinliao().get(i).getImage_url()); if (temp != null)
 * image.setImageDrawable(temp); } }
 * 
 * temp = new GetImageFromLocal().getFromlocal(OrderActivity.this, currentUrl);
 * if (temp != null) order_bg.setImageDrawable(temp); }
 */

