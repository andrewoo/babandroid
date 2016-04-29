package com.hw.chineseLearn.tabMe;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
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
		setContentView(R.layout.activity_register2);
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
		iv_close = (ImageView) findViewById(R.id.iv_close);  
		iv_close.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				CustomApplication.app.finishActivity(MyRegisterActivity.class);
				return false;
			}
		});//showOrhideTextView()
		//your_name
		et_your_name = (EditText) findViewById(R.id.et_your_name);
		tv_your_name = (TextView) findViewById(R.id.tv_your_name);
		showOrhideTextView(et_your_name,tv_your_name);
		//email
		txt_username = (EditText) findViewById(R.id.txt_username);
		tv_username = (TextView) findViewById(R.id.tv_username);
		showOrhideTextView(txt_username,tv_username);
		//pwd tv_password
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		changeEyeIcon(et_pwd);
		tv_password = (TextView) findViewById(R.id.tv_password);
		showOrhideTextView(et_pwd,tv_password);
		//et_confirm_pwd
		et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
		tv_confirm_pwd = (TextView) findViewById(R.id.tv_confirm_pwd);
		showOrhideTextView(et_confirm_pwd,tv_confirm_pwd);
		changeEyeIcon(et_confirm_pwd);
		btn_login = (TextView) findViewById(R.id.btn_login);
		btn_forgot_psw = (TextView) findViewById(R.id.btn_forgot_psw);
		btn_forgot_psw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MyRegisterActivity.this,
						MyForgotPswActivity.class));
				CustomApplication.app.finishActivity(MyRegisterActivity.class);				
			}
		});
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
							CustomApplication.app.finishActivity(MyRegisterActivity.class);
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
	
	private void showOrhideTextView(EditText et,final TextView tv) {
		et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()==0){
					tv.setVisibility(View.INVISIBLE);
				}else{
					tv.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}});
	}
	
	public void changeEyeIcon(final EditText et){
		et.setOnTouchListener(new OnTouchListener() {//此监听做两件事1 改变password/text 2 改变点击图标

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = et.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et.getWidth()- et.getPaddingRight()- drawable.getIntrinsicWidth()){
                	if(et.getInputType()==EditorInfo.TYPE_CLASS_TEXT){//InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                		et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                		et.setSelection(et.getText().length());
                		Drawable image = context.getResources().getDrawable(R.drawable.viewpassword );
                		et.setCompoundDrawablesWithIntrinsicBounds(null, null,image, null);
                		
                	}else{
                		Drawable image = context.getResources().getDrawable(R.drawable.hidepassword); 
                		et.setCompoundDrawablesWithIntrinsicBounds(null, null,image, null);
                		et.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                		et.setSelection(et.getText().length());
                	}
                }
                    return false;
            }
        });
	}

	private EditText txt_username;
	private EditText et_pwd;
	private EditText et_confirm_pwd;
	private String text;
	private String pwd;
	private ImageView 		iv_close;
	private EditText et_your_name;
	private TextView tv_your_name;
	private TextView tv_username;
	private TextView tv_password;
	private TextView tv_confirm_pwd;
	private TextView btn_forgot_psw;

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
