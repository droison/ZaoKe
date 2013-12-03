package com.tiancikeji.zaoke.httpservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.tiancikeji.zaoke.httpservice.base.HttpResponseEntity;

import android.util.Log;

public class HTTP {
	private static final String TAG = "HTTP";

	private static DefaultHttpClient httpClient = createHttpClient();

	private static HttpContext localContext = new BasicHttpContext();

	private static CookieStore cookieStore = new BasicCookieStore();

	private static DefaultHttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "ISO-8859-1");
		HttpProtocolParams.setUseExpectContinue(params, true);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 433));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
		DefaultHttpClient hc = new DefaultHttpClient(conMgr, params);
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 8000);

		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 8000);
		return hc;
	};

	static {
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	public static boolean deleteMothod(String pathUrl) {

		InputStream input = null;
		boolean b = false;
		HttpURLConnection httpConn = null;
		try {
			// 建立连接
			URL url = new URL(pathUrl);
			httpConn = (HttpURLConnection) url.openConnection();

			httpConn.setRequestMethod("DELETE");// 设置URL请求方法
			httpConn.setRequestProperty("x-msg-require-headers", "timestamp, expiry, persistence");
			int responseCode = httpConn.getResponseCode();
			if (responseCode == 200)
				b = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (httpConn != null)
				httpConn.disconnect();
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;

	}

	// public static HttpResponseEntity postByHttpUrlConnection(String
	// pathUrl,Object o){}

	public static HttpResponseEntity get(String URL) {
		String url;
		if(URL.indexOf("?") > 0){
			url = URL + "&os=android";
		}else{
			url = URL + "?os=android";
		}
		Log.i("HTTP_URL", url);
		HttpGet listGet = new HttpGet(url);
		HttpResponse response;
		HttpResponseEntity hre = new HttpResponseEntity();
		InputStream input = null;
		try {
			response = httpClient.execute(listGet, localContext);
			int code = response.getStatusLine().getStatusCode();

			hre.setHttpResponseCode(code);
			if (code != 204) {
				input = response.getEntity().getContent();
				byte[] b = readInputStream(input);
				hre.setB(b);
			}
			return hre;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "HTTPCONNECTION", e);
			return hre;
		} finally {
			if (input != null) {
				try {
					input.close();
					Log.d(TAG, "CONNECTIONCLOSE");
				} catch (IOException e) {
					Log.e(TAG, "CONNECTIONCLOSE", e);
				}
			}
		}

	}

	public static List<Cookie> getCookie() {
		return cookieStore.getCookies();
	}

	public static void clearCookie() {
		if (cookieStore != null) {
			cookieStore.clear();
		}
	}

	static void addCookie(HttpGet get) {
		StringBuilder tmpcookies = new StringBuilder();

		List<Cookie> cookies = getCookie();
		for (Cookie c : cookies) {
			tmpcookies.append(c.getName());
			tmpcookies.append("=");
			tmpcookies.append(c.getValue());
			tmpcookies.append(";");
			tmpcookies.append("domain=");
			tmpcookies.append(c.getDomain());
		}

		get.setHeader("Cookie", tmpcookies.toString());
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
}
