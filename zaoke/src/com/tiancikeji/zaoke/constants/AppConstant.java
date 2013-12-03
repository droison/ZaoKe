package com.tiancikeji.zaoke.constants;

import java.io.File;

import android.os.Environment;

public class AppConstant {

	public static final int HTTPNULL = 10120; // status参数，表示食品不存在
	public static final int HTTPVerifyERROR = 10521; // status参数，验证码不正确
	
	public static final int HTTPSECC = 0; // status参数，表示获取成功

	public static final int HANDLER_HTTPSTATUS_ERROR = 130901; // HTTP访问结果，此为错误
	public static final int HANDLER_MESSAGE_NORMAL = 130902; // 此为访问正常
	public static final int HANDLER_MESSAGE_TIMEOUT = 130903; // 此为连接超时
	public static final int HANDLER_MESSAGE_NONETWORK = 130904; // 网络连接未打开
	
	public static final int RQF_PAY = 14001;  //表示alipay快捷支付完成
	
	public static final int HANDLER_VERSION_UPDATE = 20001; //表示升级
	public static final int HANDLER_APK_DOWNLOAD_PROGRESS = 20002;
	public static final int HANDLER_APK_DOWNLOAD_FINISH = 20003;
	public static final int HANDLER_APK_STOP = 20004;  //APP禁止使用
	
	public static final int QXReader = 30001;
	
	public static final String APK_NAME = "早客";


	public interface HTTPURL {
		//public static final String foodList = "http://218.249.255.29:81/1111";
		public static final String foodList = "http://zaocan.tiancikeji.com//client/food/list"; // 无参
		public static final String foodInfo = "http://zaocan.tiancikeji.com//client/food/info"; // 参数：id
		public static final String buyInfo = "http://zaocan.tiancikeji.com//client/food/buy"; // 参数1
																								// 食物
																								// 2
																								// 饮料

		public static final String locList = "http://zaocan.tiancikeji.com//client/pick/list";
		public static final String bindVip = "http://zaocan.tiancikeji.com//client/card/bind"; // card_id
																								// card_code
																								// userid
																								// ticket
		public static final String getVerifyCode = "http://zaocan.tiancikeji.com/client/sendverifycode";// phone
																										// userid
																										// ticket
		public static final String verifycode = "http://zaocan.tiancikeji.com/client/verifycode";// phone
																									// userid
																									// ticket
																									// code

		public static final String orderSubmit = "http://zaocan.tiancikeji.com//client/order/check"; // 参数
																										// 1
																										// 2
																										// name
																										// userid
																										// ticket
		public static final String finalOrderList = "http://zaocan.tiancikeji.com//client/order/active"; // userid
																									// ticket
		public static final String orderCancel = "http://zaocan.tiancikeji.com//client/order/cancel";// userid
																										// ticket
																										// orderid
		public static final String orderPay = "http://zaocan.tiancikeji.com//client/order/pay";

		public static final String register = "http://zaocan.tiancikeji.com/client/register";
		public static final String login = "http://zaocan.tiancikeji.com/client/login";
		public static final String userInfo = "http://zaocan.tiancikeji.com/client/zaokedata";  //name ticket userid
		public static final String autoreg = "http://zaocan.tiancikeji.com/client/autoreg";// name
		public static final String checkVersion = "http://code.taobao.org/svn/zaoke/trunk/Zaoke/checkVersion";
		public static final String rename="http://zaocan.tiancikeji.com//client/user/rename"; //userid ticket name
		public static final String getQx="http://zaocan.tiancikeji.com//client/code"; //userid ticket name
		public static final String Recharge="http://zaocan.tiancikeji.com/client/recharge";//userid ticket money paymode
		public static final String passWd="http://zaocan.tiancikeji.com//client/user/passwd"; //userid ticke oldpasswd new1passwd newpasswd
	}

	public static final String BASE_DIR_CACHE = Environment.getExternalStorageDirectory() + File.separator + "zaoke" + File.separator + "cache";
	public static final String BASE_DIR_PATH = Environment.getExternalStorageDirectory() + File.separator + "zaoke" + File.separator + "data";
}
