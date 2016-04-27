package com.hw.chineseLearn.tabMe;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.interfaces.AppConstants;
import com.hw.chineseLearn.interfaces.HttpInterfaces;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.Utility;

/**
 * 注册
 * 
 * @author yh
 */
public class MyRegisterActivity extends BaseActivity {

	private String TAG = "==MyRegisterActivity==";
	private Context context;

	TextView btn_login;
	private ThreadWithDialogTask tdt;
	HttpInterfaces interfaces;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		context = this;
		tdt = new ThreadWithDialogTask();
		interfaces = new HttpInterfaces(this);
		init();
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
	}

	/**
	 * 初始化
	 */
	public void init() {
		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Sign up", View.GONE, View.GONE, 0);
		
		txt_username = (EditText) findViewById(R.id.txt_username);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
		
		btn_login = (TextView) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(onClickListener);

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

				CustomApplication.app.finishActivity(MyRegisterActivity.this);
				break;

			case R.id.btn_login:
				
				checkAndLogin();
				
				break;

			default:
				break;
			}
		}

	};
	
	private void checkAndLogin() {
		text = txt_username.getText().toString().trim();
		pwd = et_pwd.getText().toString().trim();
		String confirm_pwd = et_confirm_pwd.getText().toString().trim();
		if(!Utility.isEmail(text)){
			Toast.makeText(this, "Please enter the correct email format", Toast.LENGTH_SHORT).show();
			return;
		}else if(pwd.length()<6 || confirm_pwd.length()<6){
			Toast.makeText(this, "at lease 6 characters", Toast.LENGTH_SHORT).show();
			return;
		}else if(!pwd.equals(confirm_pwd)){
			Toast.makeText(this, "Two passwords are not consistent please re-enter", Toast.LENGTH_SHORT).show();
			return;
		}
		
		RequestParams params = new RequestParams();
//		params.addHeader("name", "value");
//		params.addQueryStringParameter("name", "value");

		// 只包含字符串参数时默认使用BodyParamsEntity，
		// 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
		params.addBodyParameter("username", text);
		params.addBodyParameter("password", pwd);

		HttpUtils http = new HttpUtils();
		String url="http://58.67.154.138:8088/babbel-api-app/v1/users/register";
		http.send(HttpRequest.HttpMethod.POST,
			url,
		    params,
		    new RequestCallBack<String>() {

		        @Override
		        public void onStart() {
		        }

		        @Override
		        public void onLoading(long total, long current, boolean isUploading) {
		        }

		        @Override
		        public void onSuccess(ResponseInfo<String> responseInfo) {
		        	//"success":false,"message":"Username already exists","error_code":20002,"results":{}}
		        	//{"success":true,"message":"","error_code":0,"results":{}}
		        	try {
						JSONObject jsobject=new JSONObject(responseInfo.result);
						Boolean success = jsobject.optBoolean("success");
						if(success){
							
							CustomApplication.app.preferencesUtil.setValue(AppConstants.LOGIN_USERNAME, text);
							CustomApplication.app.preferencesUtil.setValue(AppConstants.LOGIN_PWD, pwd);
							
							Intent intent=new Intent(MyRegisterActivity.this,MyAccountActivity.class);
							startActivity(intent);
							
							return;
						}
						String message = jsobject.optString("message");
						Toast.makeText(MyRegisterActivity.this, message, Toast.LENGTH_SHORT).show();
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
		        }

		        @Override
		        public void onFailure(HttpException error, String msg) {
		        	Toast.makeText(MyRegisterActivity.this, "register failed", Toast.LENGTH_SHORT).show();
		        }
		});
		
	}

	private EditText txt_username;
	private EditText et_pwd;
	private EditText et_confirm_pwd;
	private String text;
	private String pwd;

	// public class LoginOut implements ThreadWithDialogListener {
	//
	// @Override
	// public boolean TaskMain() {
	// simpleModel = interfaces.loginout();
	// return true;
	// }
	//
	// @Override
	// public boolean OnTaskDismissed() {
	// // TODO Auto-generated method stub
	// return true;
	// }
	//
	// @Override
	// public boolean OnTaskDone() {
	//
	// if (simpleModel == null) {
	// UiUtil.showToast(getApplicationContext(), "退出登录失败！");
	// return false;
	// }
	// if ("success".equals(simpleModel.getStatus())) {
	//
	// UiUtil.showToast(getApplicationContext(), "退出登录成功！");
	//
	// CustomApplication.app.loginBaseModel = null;
	// CustomApplication.app.isLogin = false;
	// CustomApplication.app.openId = "";
	// CustomApplication.app.weChatNickName = "";
	// CustomApplication.app.preferencesUtil.setValue(
	// AppConstants.LOGIN_PWD, "");
	//
	// CustomApplication.app.preferencesUtil.getValue(
	// AppConstants.LOGIN_PWD, "");
	//
	// Intent intent = new Intent();
	// intent.setClass(MySettingActivity.this, WXEntryActivity.class);
	// intent.putExtra("logout", false);
	// intent.putExtra("login", -1);
	// startActivity(intent);
	// // MainActivity.mainActivity.selectIndex = 0;
	// NoticeFragment.IsRefresh = true;
	// CustomApplication.app.finishAllActivity();
	// MainActivity.mainActivity.performClickBtn(0);
	//
	// } else {
	//
	// UiUtil.showToast(getApplicationContext(), "退出登录失败！");
	// }
	// return true;
	// }
	// }

}
