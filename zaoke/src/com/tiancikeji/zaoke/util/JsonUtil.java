package com.tiancikeji.zaoke.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.tiancikeji.zaoke.httpservice.base.FinalOrderBase;
import com.tiancikeji.zaoke.httpservice.base.FinalOrderListBase;
import com.tiancikeji.zaoke.httpservice.base.FoodInfoBase;
import com.tiancikeji.zaoke.httpservice.base.FoodListBase;

public class JsonUtil {

	public static Object jsonToObject(String json, Class cla) {
		Object ob = JSON.parseObject(json, cla);
		return ob;
	}

	public static FoodListBase jsonToFoodList(String json) throws JSONException {
		FoodListBase flb = new FoodListBase();
		if (json == null)
			return flb;

		JSONObject jo = new JSONObject(json);
		int status = jo.getInt("status");
		flb.setStatus(status);
		if (status == 0) {
			flb.setDefaultInfo(jo.getString("default"));
			List<FoodInfoBase> shiwu = JSON.parseArray(jo.getString("1"), FoodInfoBase.class);
			List<FoodInfoBase> yinliao = JSON.parseArray(jo.getString("2"), FoodInfoBase.class);
			flb.setShipin(shiwu);
			flb.setYinliao(yinliao);
		} else {
			flb.setMsg(jo.getString("msg"));
		}

		return flb;
	}
	
	public static FinalOrderListBase jsonToOrderList(String json) throws JSONException {
		FinalOrderListBase folb = new FinalOrderListBase();
		if(json == null){
			return folb;
		}
		JSONObject jo = new JSONObject(json);
		int status = jo.getInt("status");
		folb.setStatus(status);
		if (status == 0) {
			if(!jo.has("orders"))
				return folb;
			JSONArray ja = jo.getJSONArray("orders");
			Map<String, FinalOrderBase> fobs = new HashMap<String, FinalOrderBase>();
			List<String> orderId = new ArrayList<String>();
			for(int i=0 ; i<ja.length();i++){
				JSONObject mJo = ja.getJSONObject(i);
				
				String orderid = mJo.getString("orderid");
				
				FinalOrderBase fob = new FinalOrderBase();
				FoodInfoBase yinliao = null;
				FoodInfoBase shiwu = null;
				
				
				if(mJo.has("1")){
					shiwu = JSON.parseObject(mJo.getString("1"), FoodInfoBase.class);
				}
				
				if(mJo.has("2")){
					yinliao = JSON.parseObject(mJo.getString("2"), FoodInfoBase.class);
				}
				
				fob.setShipin(shiwu);
				fob.setYinliao(yinliao);
				fob.setOrderid(orderid);
				if(shiwu!=null&&yinliao!=null){
					fob.setCombo_price(mJo.getDouble("combo_price"));
				}else{
					fob.setCombo_price(mJo.getDouble("sale_price"));
				}
				fob.setPaymode(mJo.getInt("paymode"));
				fob.setSale_price(mJo.getDouble("sale_price"));
				fob.setPick_time(mJo.getString("pick_time"));
				fob.setStatus(mJo.getInt("status"));
				fob.setOrder_time(mJo.getString("order_time"));
				fob.setPick_start_time(mJo.getString("pick_start_time"));
				fob.setPick_end_time(mJo.getString("pick_end_time"));
				fobs.put(orderid, fob);
				
				orderId.add(orderid);
			}
			folb.setOrders(fobs);
			
			folb.setOrderid(orderId);
		} else {
			folb.setMsg(jo.getString("msg"));
		}
		return folb;
	}
}
