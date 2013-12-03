package com.tiancikeji.zaoke.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpStatus;

import com.tiancikeji.zaoke.Thread.ThreadExecutor;
import com.tiancikeji.zaoke.constants.AppConstant;
import com.tiancikeji.zaoke.httpservice.HTTP;
import com.tiancikeji.zaoke.httpservice.base.HttpResponseEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/*
 * author chaisong
 * 
 * 
 * */
public class AsyncImageLoader {

	private static final String TAG = "AsyncImageLoader";
	private HashMap<String, SoftReference<Bitmap>> mImageCache;
	private List<String> allPath;

	public AsyncImageLoader() {
		mImageCache = new HashMap<String, SoftReference<Bitmap>>();
		allPath = new ArrayList<String>();
	}

	public void loadBitmap(final Context context, final String imageUrl, final ImageCallback imageCallback) {

		
		
		if(mImageCache.containsKey(imageUrl)) {

            SoftReference<Bitmap> softReference = mImageCache.get(imageUrl);

            Bitmap bitmap = softReference.get();

            if(null != bitmap)
            {
            	imageCallback.imageLoaded(bitmap, imageUrl);
            	return;
            }else{
            	mImageCache.remove(imageUrl);
            	allPath.remove(imageUrl);
            }

     }else{
    	 allPath.add(imageUrl);
     }


		
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);
			}
		};

		ThreadExecutor.execute(new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = loadImageFromUrl(context, imageUrl);
				mImageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
				Message message = handler.obtainMessage(0, bitmap);
				handler.sendMessage(message);
			}
		}));

	}
	
	public int getUrlSize(){
		return allPath.size();
	}
	
	public Bitmap loadImageFromUrl(Context context, String imageUrl) {
		Bitmap bitmap = null;
		if (imageUrl == null)
			return bitmap;
		String imagePath = "";
		String fileName = "";
		String imageDataPath = "";
		if (imageUrl != null && imageUrl.length() != 0) {
			fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf(".")) + ".jpg";
		}

		imagePath = AppConstant.BASE_DIR_CACHE + "/" + fileName;
		imageDataPath = AppConstant.BASE_DIR_PATH + "/" + fileName;
		File file = new File(AppConstant.BASE_DIR_PATH, fileName);
		File cacheFile = new File(AppConstant.BASE_DIR_CACHE, fileName); 
		if (!file.exists()) {
			try {

				FileOutputStream fos = new FileOutputStream(cacheFile);
				HttpResponseEntity hre = HTTP.get(imageUrl);

				byte[] is = null;
				if (hre != null) {
					if (hre.getHttpResponseCode() == HttpStatus.SC_OK) {
						is = hre.getB();
						for (int i = 0; i < is.length; i++) {
							fos.write(is[i]);
						}
					}
				}
				fos.close();
				ImageUtil.bitmapCompress(imagePath, imageDataPath,1080);
				bitmap = BitmapFactory.decodeFile(imageDataPath);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			}
		} else {
			bitmap = BitmapFactory.decodeFile(imageDataPath);
		}
		return bitmap;
	}
	
	public void releaseImage(String path) {

        if(mImageCache.containsKey(path)) {

               SoftReference<Bitmap> reference = mImageCache.get(path);

               Bitmap bitmap = reference.get();

               if(null != bitmap) {
                      bitmap.recycle();
               }
               mImageCache.remove(path);
        }

 }

	public void releaseAll(){
		for(String path:allPath){
			releaseImage(path);
		}
		allPath.clear();
	}


	
	public interface ImageCallback {
		public void imageLoaded(Bitmap bitmap, String imageUrl);
	}

}
