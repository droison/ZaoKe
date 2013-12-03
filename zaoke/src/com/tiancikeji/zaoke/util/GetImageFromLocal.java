package com.tiancikeji.zaoke.util;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.util.ImageUtil;

public class GetImageFromLocal {
	
	private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();;

	
	public Drawable getThumbFromlocal(Context context, String imageUrl){
		
		Drawable drawable = null;
		if (imageUrl == null)
			return drawable;
		String fileName = "";
		if (imageUrl != null && imageUrl.length() != 0) {
			fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf(".")) + ".jpg";
		}
		
		if (imageCache.containsKey(fileName)) {
			SoftReference<Drawable> softReference = imageCache.get(fileName);
			drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		
		File file = new File(AppConstant.BASE_DIR_PATH, fileName);
		File fileThumb = new File(AppConstant.BASE_DIR_PATH, fileName+".thumb");
		if(fileThumb.exists()){
			drawable = Drawable.createFromPath(fileThumb.toString());
			imageCache.put(fileName, new SoftReference<Drawable>(drawable));
		}else if(file.exists()&&!fileThumb.exists()){
			try{
				ImageUtil.bitmapCompress(file.toString(), fileThumb.toString(),150);
			}catch(NullPointerException e){
				Log.v("NullPointerException", "没啥事");
			}
			drawable = Drawable.createFromPath(fileThumb.toString());
			imageCache.put(fileName, new SoftReference<Drawable>(drawable));
		}	
		return drawable;
	}
	
	public Drawable getFromlocal(Context context, String imageUrl){
		
		Drawable drawable = null;
		if (imageUrl == null)
			return drawable;
		String fileName = "";
		if (imageUrl != null && imageUrl.length() != 0) {
			fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf(".")) + ".jpg";
		}
		
		if (imageCache.containsKey(fileName)) {
			SoftReference<Drawable> softReference = imageCache.get(fileName);
			drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		File file = new File(AppConstant.BASE_DIR_PATH, fileName);
		if(file.exists()){
			drawable = Drawable.createFromPath(file.toString());
			imageCache.put(fileName, new SoftReference<Drawable>(drawable));
		}	
		return drawable;
	}
}
