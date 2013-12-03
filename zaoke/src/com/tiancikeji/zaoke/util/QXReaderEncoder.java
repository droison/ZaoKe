package com.tiancikeji.zaoke.util;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QXReaderEncoder {

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	public static Bitmap encode(String url, String savePath) {
		try {
			BitMatrix bitMatrix;
			bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 320, 320);
			File file = new File(savePath);

			int width = bitMatrix.getWidth();
			int height = bitMatrix.getHeight();
			int[] pixels = new int[width * height];
			// All are 0, or black, by default
			for (int y = 0; y < height; y++) {
				int offset = y * width;
				for (int x = 0; x < width; x++) {
					pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
				}
			}

			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			FileOutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.close();
			return bitmap;
		} catch (Exception e) {
			Log.e("QXReaderEncoderthis", e.getMessage(), e);
			return null;
		}
	}
}
