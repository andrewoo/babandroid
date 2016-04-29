package com.hw.chineseLearn.base;

import java.util.List;

import org.apache.http.cookie.Cookie;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.interfaces.RestClient;
import com.util.tool.SystemHelper;
import com.util.weight.MyDialog;
import com.util.weight.MyDialog.MyDialogListener;

/**
 * 网页跳转
 * 
 * @author yh
 * 
 */
public class MainWebActivity extends BaseActivity {
	private String TAG = "-MainWebActivity-";
	Context context;
	View contentView;
	// 顶部标题栏
	private View view_title;

	public static String COOKIE_STRING = null;
	private MyReceiver receiverNet = null;
	MyDialog dialog;
	WebView wv;
	String address = null; // 用户输入的URL地址
	String title = null; // 标题
	public static final String DEFAULT_URL = "";
	private String cookieKeys = "";
	private String memberId = "";

	private String type = "";
	private String param = "";
	private String bussId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		CustomApplication.app.addActivity(this);
		contentView = LayoutInflater.from(context).inflate(
				R.layout.activity_main_web, null);
		setContentView(contentView);
		address = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");

		type = getIntent().getStringExtra("type");
		if (type != null && type.contains("tip")) {
			param = getIntent().getStringExtra("param");
		}
		if (address != null && address.length() > 0) {
			initWebView();
		} else {
			finish();
		}
		CustomApplication.app.addActivity(this);
		registerNetReceiver();
	}

	/**
	 * 网络监听的广播
	 */
	private void registerNetReceiver() {
		receiverNet = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiverNet, intentFilter);
		System.out.println("主页--【网络监听】广播已经注册");
	}

	/**
	 * 顶部标题栏
	 * 
	 * @param textLeft
	 *            是否显示左边文字
	 * @param imgLeft
	 *            是否显示左边图片
	 * @param title
	 *            标题
	 * @param imgLeftDrawable
	 *            左边图片资源
	 * @param textRight
	 *            是否显示右边文字
	 * @param imgRight
	 *            是否显示右边图片
	 * @param imgRightDrawable
	 *            右边图片资源
	 */
	public void setTitle(int textLeft, int imgLeft, int imgLeftDrawable,
			String title, int textRight, int imgRight, int imgRightDrawable) {

		View view_title = (View) findViewById(R.id.view_title);
		Button tv_title = (Button) view_title.findViewById(R.id.btn_title);
		tv_title.setText(title);

		TextView tv_title_left = (TextView) view_title
				.findViewById(R.id.tv_title_left);
		tv_title_left.setVisibility(textLeft);

		ImageView iv_title_left = (ImageView) view_title
				.findViewById(R.id.iv_title_left);
		iv_title_left.setVisibility(imgLeft);
		iv_title_left.setOnClickListener(onClickListener);
		iv_title_left.setImageResource(imgLeftDrawable);

		TextView tv_title_right = (TextView) view_title
				.findViewById(R.id.tv_title_right);
		tv_title_right.setVisibility(textRight);
		tv_title_right.setOnClickListener(onClickListener);

		ImageView iv_title_right = (ImageView) view_title
				.findViewById(R.id.iv_title_right);
		iv_title_right.setVisibility(imgRight);
		iv_title_right.setImageResource(imgRightDrawable);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回
				closeWindowSoftInput();
				finish();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		closeWindowSoftInput();
		if (null != receiverNet) {
			this.unregisterReceiver(receiverNet);
		} else {
			Log.e(TAG, "onDestroy(),网络监听注销失败");
		}
	}

	public static void synCookies(Context context, String url, String cookies) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();// 移除
		cookieManager.setCookie(url, cookies);// cookies是在HttpClient中获得的cookie

		CookieSyncManager.getInstance().sync();
	}

	/**
	 * 清除cookie
	 * 
	 * @param context
	 */
	private void removeCookie(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
	}

	Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					break;
				case 1:
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	private void closeWindowSoftInput() {
		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isSoftActive = imm.isActive();
		Log.d("-MianWebActivity-", "isSoftActive：" + isSoftActive);
		if (isSoftActive) {
			imm.hideSoftInputFromWindow(wv.getApplicationWindowToken(), 0); // 强制隐藏键盘
			Log.d(TAG, "强制隐藏键盘");
		}
	}

	boolean isclick = false;

	@SuppressLint({ "NewApi", "JavascriptInterface" })
	protected void initWebView() {
		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				title, View.GONE, View.GONE, 0);
		wv = (WebView) findViewById(R.id.tutorialView); // 得到webView的视图

		WebSettings websetting = wv.getSettings();
		websetting.setDomStorageEnabled(true);
		String appCacheDir = this.getApplicationContext()
				.getDir("cache", Context.MODE_PRIVATE).getPath();
		websetting.setAppCachePath(appCacheDir);
		websetting.setAllowFileAccess(true);
		websetting.setAppCacheEnabled(true);
		websetting.setCacheMode(WebSettings.LOAD_DEFAULT);
		websetting.setJavaScriptEnabled(true);

		wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			websetting.setAllowUniversalAccessFromFileURLs(true);
		}

		// 为WebView设置WebViewClient处理某些操作
		wv.setWebViewClient(new webViewClient());
		wv.setWebChromeClient(new MyWebChromeClient());
		wv.addJavascriptInterface(this, "mzywxappjs_notice");

		String cookieStr = "";
		if (RestClient.cookieStore != null) {

			// 获取cookie中的各种信息
			List<Cookie> cookies = RestClient.cookieStore.getCookies();
			if (!cookies.isEmpty()) {
				for (Cookie cookie : cookies) {
					cookieStr = cookie.getName() + "=" + cookie.getValue()
							+ "; domain=" + cookie.getDomain() + "; path="
							+ cookie.getPath();
				}
			}

			if (!cookies.isEmpty()) {
				for (Cookie cookie : cookies) {
					String name = cookie.getName();
					String value = cookie.getValue();

					Log.d("==-cookie-==", name + "-" + value);

					if (name.equals("SHAREJSESSIONID")) {
						// cookieStr = "" + value;

						Log.d("==-cookie-==", name + "-" + value);
						break;
					}
				}
			}

		}
		synCookies(getBaseContext(), address, cookieStr);
		Log.d("cookieStr==", "" + cookieStr);
		loadWebsit();
	}

	/**
	 * 加载网页数据
	 */
	private void loadWebsit() {

		if (SystemHelper.isConnected(MainWebActivity.this) == true && // 如果网络连接正常
				SystemHelper.getNetworkType(MainWebActivity.this) != -1) {
			CustomApplication.app.isNetConnect = true;

			wv.loadUrl(address); // 加载网页
			Log.d(TAG, "address：" + address);

		} else {

			CustomApplication.app.isNetConnect = false;
			createSetNetworkDialog();

		}

	}

	/**
	 * 弹出设置网络连接的提示
	 * 
	 * @param activity
	 */
	public void createSetNetworkDialog() {
		if (dialog == null) {
			dialog = new MyDialog(MainWebActivity.this);
		}

		dialog.setTitle("Please set network");
		dialog.setMessage("network is avliable？");
		dialog.setOnButtonGroupClick("Cancel", new MyDialogListener() {
			@Override
			public void onClick() {
				CustomApplication.app.finishActivity(MainWebActivity.this);
				dialog.dismiss();
			}
		}, "Set", new MyDialogListener() {
			@Override
			public void onClick() {

				startActivityForResult(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS), 0);// 进入无线网络配置界面

				dialog.dismiss();
			}
		});
		dialog.show();

	}

	@Override
	public void onBackPressed() {
		if (wv.canGoBack()) {
			wv.goBack();
		} else {
			finish();
		}
	}

	class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			// TODO Auto-generated method stub
			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			// TODO Auto-generated method stub
			return super.onJsConfirm(view, url, message, result);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			// System.out.println(newProgress);
			if (newProgress != 100) {

			} else {

			}
		}
	}

	class webViewClient extends WebViewClient {
		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			// 如果不需要其他对点击链接事件的处理返回true，否则返回false

			if (url.startsWith("tel:")) {
				// 通过Intent 拨打电话
				Intent callIntent = new Intent(Intent.ACTION_CALL,
						Uri.parse(url));
				startActivity(callIntent);
			} else {
				view.loadUrl(url);// 在当前的webview中跳转到新的url
			}

			if (url.startsWith("")) {

			}

			return true;
		}

	}

	/**
	 * 自定义广播接收器
	 * 
	 * @author yh
	 */
	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 接收
			if (intent.getAction().equals(
					"android.net.conn.CONNECTIVITY_CHANGE")) {

				if (SystemHelper.isConnected(MainWebActivity.this) == true && // 如果网络连接正常
						SystemHelper.getNetworkType(MainWebActivity.this) != -1) {
					CustomApplication.app.isNetConnect = true;
					wv.loadUrl(address); // 加载网页
				} else {

					CustomApplication.app.isNetConnect = false;
					createSetNetworkDialog();

				}
			}
		}

	}
}
