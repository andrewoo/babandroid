package com.hw.chineseLearn.tabMe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录
 * 
 * @author yh
 */
public class MyLoginActivity extends BaseActivity {

	private String TAG = "==MyLoginActivity==";
	private Context context;

	private TextView btn_login, btn_forgot_psw, btn_sign_up;
	private EditText txt_username, et_pwd;
	private ThreadWithDialogTask tdt;
	HttpInterfaces interfaces;
	View contentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_login, null);
		setContentView(contentView);
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
	public void init() {//line_password
		iv_close = (ImageView) findViewById(R.id.iv_close); 
		iv_close.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				CustomApplication.app.finishActivity(MyLoginActivity.class);
				return false;
			}
		});
		line_password = (View) findViewById(R.id.line_password);
		tv_noty_error = (TextView) findViewById(R.id.tv_noty_error);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_password = (TextView) findViewById(R.id.tv_password);
		txt_username = (EditText) findViewById(R.id.txt_username);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		showOrhideTextView();
		
		et_pwd.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(count==0){
					tv_password.setVisibility(View.INVISIBLE);
				}else{
					tv_password.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}});
		
		et_pwd.setOnTouchListener(new OnTouchListener() {//此监听做两件事1 改变password/text 2 改变点击图标

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = et_pwd.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et_pwd.getWidth()- et_pwd.getPaddingRight()- drawable.getIntrinsicWidth()){
                	if(et_pwd.getInputType()==EditorInfo.TYPE_CLASS_TEXT){//InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                		et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                		et_pwd.setSelection(et_pwd.getText().length());
                		Drawable image = context.getResources().getDrawable(R.drawable.viewpassword );
                		et_pwd.setCompoundDrawablesWithIntrinsicBounds(null, null,image, null);
                		
                	}else{
                		Drawable image = context.getResources().getDrawable(R.drawable.hidepassword  ); 
                		et_pwd.setCompoundDrawablesWithIntrinsicBounds(null, null,image, null);
                		et_pwd.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                		et_pwd.setSelection(et_pwd.getText().length());
                	}
                }
                    return false;
            }
        });
		//inputType = TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD 

		btn_login = (TextView) findViewById(R.id.btn_login);
		btn_forgot_psw = (TextView) findViewById(R.id.btn_forgot_psw);
		btn_sign_up = (TextView) findViewById(R.id.btn_sign_up);
		btn_login.setOnClickListener(onClickListener);
		btn_forgot_psw.setOnClickListener(onClickListener);
		btn_sign_up.setOnClickListener(onClickListener);
		emailString = CustomApplication.app.preferencesUtil.getValue(
				AppConstants.LOGIN_USERNAME, "");
		pswString = CustomApplication.app.preferencesUtil.getValue(
				AppConstants.LOGIN_PWD, "");

		txt_username.setText(emailString); 
		et_pwd.setText(pswString);

	}

	private void showOrhideTextView() {
		txt_username.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()==0){
					tv_username.setVisibility(View.INVISIBLE);
				}else{
					tv_username.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}});
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

	private void closeWindowSoftInput() {
		super.closeWindowSoftInput(contentView);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回
				closeWindowSoftInput();
				CustomApplication.app.finishActivity(MyLoginActivity.this);
				break;

			case R.id.btn_login://
				signIn();
				break;
			case R.id.btn_forgot_psw:
				startActivity(new Intent(MyLoginActivity.this,
						MyForgotPswActivity.class));
				CustomApplication.app.finishActivity(MyLoginActivity.class);
				break;
			case R.id.btn_sign_up:
				startActivity(new Intent(MyLoginActivity.this,
						MyRegisterActivity.class));
				CustomApplication.app.finishActivity(MyLoginActivity.class);
				break;

			default:
				break;
			}
		}
	};
	String emailString = "";
	String pswString = "";
	private TextView tv_username;
	private TextView tv_password;
	private TextView tv_noty_error;
	private View line_password;
	private ImageView iv_close;

	/**
	 * 登录
	 */
	private void signIn() {
		emailString = txt_username.getText().toString();
		pswString = et_pwd.getText().toString();
		if ("".equals(emailString)) {
			showNothingInputDialog(R.id.txt_username);

		} else if ("".equals(pswString)) {
			showNothingInputDialog(R.id.et_pwd);
		} else {
			// sign in
			CustomApplication.app.isLogin = true;
			RequestParams params = new RequestParams();
			params.addBodyParameter("username", emailString);
			params.addBodyParameter("password", pswString);
			HttpUtils http = new HttpUtils();
			String url=AppConstants.BASE_URL_WEB +"/babble-api-app/v1/users/login";
			http.send(HttpRequest.HttpMethod.POST,
				url,
			    params,
			    new RequestCallBack<String>() {

			        @Override
			        public void onStart() {}

			        @Override
			        public void onLoading(long total, long current, boolean isUploading) {}

			        @Override
			        public void onSuccess(ResponseInfo<String> responseInfo) {
			        	//"success":false,"message":"Username already exists","error_code":20002,"results":{}}
			        	//{"success":true,"message":"","error_code":0,"results":{}}
			        	try { 
							JSONObject jsobject=new JSONObject(responseInfo.result);
							Boolean success = jsobject.optBoolean("success");
							JSONObject results = jsobject.optJSONObject("results");
							String token = results.optString("token");
							if(success){
								System.out.println("token"+token);
								CustomApplication.app.preferencesUtil.setValue(AppConstants.LOGIN_USERNAME, emailString);
								CustomApplication.app.preferencesUtil.setValue(AppConstants.LOGIN_PWD, pswString);
								CustomApplication.app.preferencesUtil.setValue(AppConstants.LOGIN_TOKEN, token);
								closeWindowSoftInput();
								CustomApplication.app.finishActivity(MyLoginActivity.this);
								return;
							}
							tv_noty_error.setVisibility(View.VISIBLE);
							line_password.setBackgroundColor(Color.RED);
						} catch (JSONException e) {
							e.printStackTrace();
						}
			        }

			        @Override
			        public void onFailure(HttpException error, String msg) {
			        	Toast.makeText(MyLoginActivity.this, getString(R.string.tabme_login_fail), Toast.LENGTH_SHORT).show();
			        }
			});
		}
	}

	/**
	 * 对话框
	 */
	private void showNothingInputDialog(int editId) {

		final AlertDialog mModifyDialog = new AlertDialog.Builder(context)
				.create();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.layout_dialog_loginout, null);
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		title.setVisibility(View.GONE);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		Button ok = (Button) view.findViewById(R.id.commit_btn);
		Button cancel = (Button) view.findViewById(R.id.cancel_btn);

		title.setText(getString(R.string.title_title));
		switch (editId) {
		case R.id.txt_username:
			content.setText(getString(R.string.tabme_email));
			break;
		case R.id.et_pwd:
			content.setText(getString(R.string.tabme_input_password));
			break;

		default:
			break;
		}

		title.setGravity(Gravity.CENTER_HORIZONTAL);
		ok.setText(getString(R.string.tabme_ok));
		cancel.setVisibility(View.GONE);
		cancel.setText(getString(R.string.tabme_cancel));
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mModifyDialog.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mModifyDialog.dismiss();
			}
		});
		mModifyDialog.show();
		mModifyDialog.setContentView(view);
	}
	
	@Override
	protected void onResume() {
		if(tv_noty_error!=null && line_password!=null ){
			tv_noty_error.setVisibility(View.INVISIBLE);
			line_password.setBackgroundColor(getResources().getColor(R.color.min_grey));
		}
		super.onResume();
	}

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
