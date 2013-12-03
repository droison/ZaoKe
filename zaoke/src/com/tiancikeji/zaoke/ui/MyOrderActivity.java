package com.tiancikeji.zaoke.ui;

import java.util.ArrayList;
import java.util.List;
import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.db.base.Dbaccount;
import com.tiancikeji.zaoke.db.base.Dborder;
import com.tiancikeji.zaoke.db.service.AccountService;
import com.tiancikeji.zaoke.db.service.OrderService;
import com.tiancikeji.zaoke.httpservice.FinalOrderListHttp;
import com.tiancikeji.zaoke.httpservice.OrderCancelHttp;
import com.tiancikeji.zaoke.httpservice.base.FinalOrderBase;
import com.tiancikeji.zaoke.httpservice.base.FinalOrderListBase;
import com.tiancikeji.zaoke.httpservice.base.FoodListBase;
import com.tiancikeji.zaoke.httpservice.base.SendCodeBase;
import com.tiancikeji.zaoke.util.AsyncImageLoader;
import com.tiancikeji.zaoke.util.GetImageFromLocal;
import com.tiancikeji.zaoke.util.NumberFormateUtil;
import com.tiancikeji.zaoke.util.TimeUtil;
import com.tiancikeji.zaoke.util.AsyncImageLoader.ImageCallback;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MyOrderActivity extends AbstractActivity implements OnClickListener {

	private TextView my_order_cancel;
	private ImageView my_order_back;

	private TextView my_order_shipinname;
	private TextView my_order_shipin_yinliaoname;
	private TextView my_order_shipinprice;
	private TextView my_order_yinliaoprice;
	private TextView order_price_original;
	private TextView order_price;
	private TextView my_order_time;
	private TextView my_order_gettime;
	private TextView my_order_pager;
	private ImageView my_order_shiwu_pic;
	private ImageView my_order_yinliao_pic;

	private OrderService os;
	private AccountService as;
	private Dbaccount account;
	private List<Dborder> order;
	private FinalOrderListBase folb;
	
	private ViewPager viewPager;
	private List<View> pagerViews;
	private int positon = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
		setUpView();
	}

	private void setUpView() {
		
		showProgressDialog("正在加载个人订单");
		my_order_cancel = (TextView) findViewById(R.id.my_order_cancel);
		my_order_back = (ImageView) findViewById(R.id.my_order_back);
		os = new OrderService(MyOrderActivity.this);
		as = new AccountService(MyOrderActivity.this);
		
		
		
		order = os.getLastAll();
		account = as.getAccount();
		my_order_back.setOnClickListener(this);
		my_order_cancel.setOnClickListener(this);
		//LayoutInflater inflater = getLayoutInflater();
		pagerViews = new ArrayList<View>();

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		
		new Thread(new FinalOrderListHttp(this, mHandler, as.getAccount().getUserid(), as.getAccount().getTicket())).start();
		
	}
	public void initPager(FinalOrderListBase folb){
		for (int i = folb.getOrders().size()-1; i >= 0; i--) {
			View v = View.inflate(this, R.layout.activity_my_order_item, null);
			my_order_shipinname = (TextView) v.findViewById(R.id.my_order_shipinname);
			my_order_shipin_yinliaoname = (TextView) v.findViewById(R.id.my_order_shipin_yinliaoname);
			my_order_shipinprice = (TextView) v.findViewById(R.id.my_order_shipinprice);
			my_order_yinliaoprice = (TextView) v.findViewById(R.id.my_order_yinliaoprice);
			order_price_original = (TextView) v.findViewById(R.id.order_price_original);
			order_price = (TextView) v.findViewById(R.id.order_price);
			my_order_time = (TextView) v.findViewById(R.id.my_order_time);
			my_order_gettime = (TextView) v.findViewById(R.id.my_order_gettime);
			my_order_pager = (TextView)v.findViewById(R.id.my_order_pager);
			my_order_shiwu_pic = (ImageView) v.findViewById(R.id.my_order_shiwu_pic);
			my_order_yinliao_pic = (ImageView) v.findViewById(R.id.my_order_yinliao_pic);
			
			my_order_shipinname.setText(folb.getOrders().get(folb.getOrderid().get(i)).getShipin()==null?"未选择":folb.getOrders().get(folb.getOrderid().get(i)).getShipin().getName());
			my_order_shipin_yinliaoname.setText(folb.getOrders().get(folb.getOrderid().get(i)).getYinliao()==null?"未选择":folb.getOrders().get(folb.getOrderid().get(i)).getYinliao().getName());

			
			my_order_time.setText(folb.getOrders().get(folb.getOrderid().get(i)).getOrder_time());
			my_order_gettime.setText(folb.getOrders().get(folb.getOrderid().get(i)).getPick_time() + " " +
					folb.getOrders().get(folb.getOrderid().get(i)).getPick_start_time() + "至" +
					folb.getOrders().get(folb.getOrderid().get(i)).getPick_end_time());
			
			if(folb.getOrders().get(folb.getOrderid().get(i)).getShipin() == null){
				my_order_yinliaoprice.setText("￥" + String.valueOf(folb.getOrders().get(folb.getOrderid().get(i)).getYinliao().getSale_price()));
				order_price_original.setText(String.valueOf(folb.getOrders().get(folb.getOrderid().get(i)).getYinliao().getSale_price()));
				order_price.setText(String.valueOf(folb.getOrders().get(folb.getOrderid().get(i)).getYinliao().getSale_price()));
				setImageBg(my_order_yinliao_pic, folb.getOrders().get(folb.getOrderid().get(i)).getYinliao().getImage_url());
			}else if(folb.getOrders().get(folb.getOrderid().get(i)).getYinliao()==null){
				my_order_shipinprice.setText("￥" + String.valueOf(folb.getOrders().get(folb.getOrderid().get(i)).getShipin().getSale_price()));
				order_price_original.setText(String.valueOf(folb.getOrders().get(folb.getOrderid().get(i)).getShipin().getSale_price()));
				order_price.setText(String.valueOf(folb.getOrders().get(folb.getOrderid().get(i)).getShipin().getSale_price()));
				setImageBg(my_order_shiwu_pic, folb.getOrders().get(folb.getOrderid().get(i)).getShipin().getImage_url());
			}else{
				my_order_shipinprice.setText("￥" + String.valueOf(folb.getOrders().get(folb.getOrderid().get(i)).getShipin().getSale_price()));
				my_order_yinliaoprice.setText("￥" + String.valueOf(folb.getOrders().get(folb.getOrderid().get(i)).getYinliao().getSale_price()));
				order_price_original.setText(String.valueOf(folb.getOrders().get(folb.getOrderid().get(i)).getShipin().getSale_price()+folb.getOrders().get(folb.getOrderid().get(i)).getYinliao().getSale_price())+"元");
				order_price.setText(NumberFormateUtil.Formate(folb.getOrders().get(folb.getOrderid().get(i)).getCombo_price())+"元");
				setImageBg(my_order_shiwu_pic, folb.getOrders().get(folb.getOrderid().get(i)).getShipin().getImage_url());
				setImageBg(my_order_yinliao_pic, folb.getOrders().get(folb.getOrderid().get(i)).getYinliao().getImage_url());
				
			}
			
			pagerViews.add(v);
		}
	}
	
	private  Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			dismissProgressDialog();
			super.handleMessage(msg);
			switch(msg.what){
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				folb = (FinalOrderListBase)msg.obj;
				//System.out.println(folb.getOrders().get(folb.getOrderid().get(0)));
				if(folb.getOrders() == null){
					finish();
					return;
					
				}
				initPager(folb);
				viewPager.setAdapter(new MyViewPagerAdapter(pagerViews));
				viewPager.setOnPageChangeListener(new pagerChanger());
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
	
	public class MyViewPagerAdapter extends PagerAdapter{  
        private List<View> mListViews;  
        public MyViewPagerAdapter(List<View> mListViews) {  
            this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。   
        }  
        
        @Override  
        public void destroyItem(ViewGroup container, int position, Object object)   {     
            container.removeView(mListViews.get(position));//删除页卡   
        }    
        
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡          
             container.addView(mListViews.get(position), 0);//添加页卡   
             return mListViews.get(position);  
        }  
        
        @Override  
        public int getCount() {           
            return  mListViews.size();//返回页卡的数量   
        }  
          
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {             
            return arg0==arg1;//官方提示这样写   
        }  
    } 

	public class pagerChanger implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			positon = arg0;
		}
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_order_cancel:
			showProgressDialog("正在取消...");
			new Thread(new OrderCancelHttp(MyOrderActivity.this, cancelHandler, account.getUserid(), account.getTicket(), folb.getOrders().get(folb.getOrderid().get( folb.getOrders().size()-1 - positon)).getOrderid())).start();;
			break;
		case R.id.my_order_back:
			finish();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private Handler cancelHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissProgressDialog();
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				SendCodeBase scb = (SendCodeBase) msg.obj;
				if (scb.getStatus() != 0 || scb.getMsg() != null) {
					if (scb.getStatus() == 10230 && scb.getMsg().equals("订单不存在")) {
						//os.delete(order.get(positon).getOrderId());
						displayResponse("取消订单成功");
						finish();
					} else {
						displayResponse(scb.getMsg());
					}
				} else {
					//os.delete(order.get(positon).getOrderId());
					displayResponse("取消订单成功");
					finish();
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
		};
	};

	public void setImageBg(final ImageView imageView, String url) {
		
		Drawable temp = new GetImageFromLocal().getThumbFromlocal(MyOrderActivity.this, url);
		if(temp==null){
			imageView.setImageResource(R.drawable.open_fail);
		}else{
			imageView.setImageDrawable(temp);
		}
		
		if(url==null){
			imageView.setImageResource(R.drawable.image_bg);
		}
	}
}
