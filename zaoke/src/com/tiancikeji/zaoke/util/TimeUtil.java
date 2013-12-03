package com.tiancikeji.zaoke.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.util.Log;

public class TimeUtil {

	private static final String TAG = "TimeUtil";

	public static long convertTimeToNumber(String time) {
		String tem[] = time.split(" ");
		String temp[] = tem[1].split(":");
		int hours = Integer.valueOf(temp[0]);
		int minutes = Integer.valueOf(temp[1]);
		// int seconds = Integer.valueOf(temp[2]);
		long allSeconds = hours * 60 * 60 + minutes * 60;
		return allSeconds;
	}

	public static String convertNumberToTime(long number) {
		StringBuilder sb = new StringBuilder();
		int hour = (int) (number / 3600);
		if (hour > 0) {
			number -= hour * 3600;
		}
		int minute = (int) (number / 60);
		if (minute > 0) {
			number -= minute * 60;
		}
		int second = (int) number;
		sb.append((hour < 10 ? "0" : "")).append(hour).append(":")
				.append((minute < 10 ? "0" : "")).append(minute).append(":")
				.append((second < 10 ? "0" : "")).append(second);
		return sb.toString();
	}

	// 把秒转换成day，hour，minute，second。
	public static String convertTimeLongToString(long times) {
		long time = times / 1000;
		String ds = null, hs = null, ms = null, ss = null;
		int dates = (int) (time / (24 * 60 * 60));
		if (dates > 0)
			ds = dates + "天";
		int hours = (int) (time - dates * 60 * 60 * 24) / (60 * 60);
		if (hours > 0)
			hs = hours + "";
		/* hs = hours + "时"; */
		int mms = (int) (time - dates * 60 * 60 * 24 - hours * 60 * 60) / 60;
		if (mms > 0)
			ms = mms + "";
		/* ms = mms + "分"; */
		int sss = (int) time - dates * 60 * 60 * 24 - hours * 60 * 60 - mms
				* 60;
		if (sss > 0)
			ss = sss + "秒";
		/* String strTime = "距离直播开始还有：" + ds + hs + ms + ss; */
		String strTime = hs + ":" + ms;
		return strTime;
	}

	// 判断直播是否开始
	public static boolean isStart(String time) {
		boolean ret = false;
		long currentTime = System.currentTimeMillis();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			long startTime = sdf.parse(time).getTime();
			if (currentTime - startTime >= 0)
				ret = true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage(), e);
		}
		return ret;
	}

	// 判断直播是否结束
	public static boolean isEnd(String time) {
		boolean ret = false;
		long currentTime = System.currentTimeMillis();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			long endTime = sdf.parse(time).getTime();
			if (currentTime - endTime >= 0)
				ret = true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage(), e);
		}
		return ret;
	}

	public static long convertStringToTime(String time) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			long startTime = sdf.parse(time).getTime();
			return startTime;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage(), e);
		}
		return 0;

	}

	public static String getShortTime(String time) {
		long updateTime = convertStringToTime(time);
		long currentTime = System.currentTimeMillis();
		String realTime = null;
		long retTime = currentTime - updateTime;
		String[] times = time.split(" ");
		if (retTime < 86400000) {
			String[] realTimes = times[1].split(":");
			realTime = realTimes[0] + ":" + realTimes[1];
		} else if (retTime > 86400000 && retTime < 86400000 * 2) {
			realTime = "昨天";
		} else if (retTime > 86400000 * 2 && retTime < 86400000 * 3) {
			realTime = "前天";
		}else{
			String[] realDates = times[0].split("-");
			realTime = realDates[1]+"-"+realDates[2];
		}

		return realTime;
	}
	
	public static String formatData(long time){
		return new SimpleDateFormat("yyyy-MM-dd kk:mm E",Locale.CHINESE).format(time);
	}
}

