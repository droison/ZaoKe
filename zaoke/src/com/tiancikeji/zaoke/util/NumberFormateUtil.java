package com.tiancikeji.zaoke.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NumberFormateUtil {
	public static String Formate(Object o) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		String s = nf.format(o);
		return s;
	}
	public static double formate2(Double d){
		String result = String.format("%.2f", d);
		return Double.parseDouble(result);
	}
}
