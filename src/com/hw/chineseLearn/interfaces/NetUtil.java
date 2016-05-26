package com.hw.chineseLearn.interfaces;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.hw.chineseLearn.model.RequestVo;
import com.util.tool.AppFinal;
import com.util.tool.UiUtil;

/**
 * 
 * @author yh
 * 
 */
public class NetUtil {
	private static final String TAG = "NetUtil";
	private static Header[] headers = new BasicHeader[11];
	private static Gson gson;
	static {
		headers[0] = new BasicHeader("Appkey", "");
		headers[1] = new BasicHeader("Udid", "");
		headers[2] = new BasicHeader("Os", "");
		headers[3] = new BasicHeader("Osversion", "");
		headers[4] = new BasicHeader("Appversion", "");
		headers[5] = new BasicHeader("Sourceid", "");
		headers[6] = new BasicHeader("Ver", "");
		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");
		headers[9] = new BasicHeader("Unique", "");
		headers[10] = new BasicHeader("Cookie", "");

	}

	/*
	 * 
	 */
	public static Object post(RequestVo vo) {

		if (gson == null) {
			gson = new Gson();
		}
		DefaultHttpClient client = new DefaultHttpClient();
		String url = "";
		if (vo.useFullUrl) {
			url = vo.fullUrl;
		}
		Log.d(TAG, "Post " + url);
		HttpPost post = new HttpPost(url);
		post.setHeaders(headers);
		Object obj = null;
		try {

			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			if (vo.requestDataMap != null) {
				HashMap<String, String> map = vo.requestDataMap;

				for (Map.Entry<String, String> entry : map.entrySet()) {
					BasicNameValuePair pair = new BasicNameValuePair(
							entry.getKey(), entry.getValue());
					multipartEntity.addPart(entry.getKey(), new StringBody(
							entry.getValue(), Charset.forName(HTTP.UTF_8)));
				}
			}
			if (vo.requestFileMap != null) {
				HashMap<String, String> map = vo.requestFileMap;
				for (Map.Entry<String, String> kv : map.entrySet()) {

					if (!vo.useFullFilePath) {
						File file = new File(AppFinal.CACHE_DIR_UPLOADING_IMG,
								kv.getValue());
						multipartEntity
								.addPart(kv.getKey(), new FileBody(file));
					} else {
						File file = new File(kv.getValue());
						multipartEntity
								.addPart(kv.getKey(), new FileBody(file));
					}
				}
			}

			post.setEntity(multipartEntity);
			client.setCookieStore(RestClient.cookieStore);// 设置登录过的cook对象
			List<Cookie> cookies = client.getCookieStore().getCookies();
			if (!cookies.isEmpty()) {
                for (Cookie cookie : cookies) {
                    String name = cookie.getName();
                    String value = cookie.getValue();

                    if (name.equals("SHAREJSESSIONID")) {
                       Log.d("yanghao:",value);
                    }
                    Log.d("==-cookie-==", name + "-" + value);
                    // String[] nvp = cookies.get(0).split("=");
                    // BasicClientCookie c = new BasicClientCookie(nvp[0],
                    // nvp[1]);

                    // BasicClientCookie c = new BasicClientCookie(name,
                    // value);
                    // c.setDomain(c.getDomain());
                    // c.setPath("/");
                    // cookieStore.addCookie(c);
                }
            }
			HttpResponse response = client.execute(post);
			Log.d(TAG, "getStatusCode: " + "结果码:"
					+ response.getStatusLine().getStatusCode());

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				setCookie(response);
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				Log.d(TAG, "result: " + result);

				try {
					obj = vo.jsonParser.parseJSON(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return obj;
			}
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
		}
		return obj;
	}

	/**
	 * 添加Cookie
	 * 
	 * @param response
	 */
	private static void setCookie(HttpResponse response) {
		if (response.getHeaders("Set-Cookie").length > 0) {
			String cookieStr = response.getHeaders("Set-Cookie")[0].getValue();
			Log.e(TAG, "cookieStr:" + cookieStr);
			headers[10] = new BasicHeader("Cookie", cookieStr);
		}
	}

	/**
	 * 获得网络连接是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			UiUtil.showToast(context, "网络连接不可用");
			return false;
		}
		return true;
	}

	public static enum Status {
		Login
	}
	
	/**
     * 检测网络是否可用
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }        
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(extraInfo != null && extraInfo.length() > 0){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }
}
