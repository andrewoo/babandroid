package com.hw.chineseLearn.tabMe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.interfaces.HttpInterfaces;
import com.hw.chineseLearn.model.SimpleModel;
import com.util.thread.ThreadWithDialogTask;

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
	SimpleModel simpleModel;
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
	}

	/**
	 * 初始化
	 */
	public void init() {
		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Sign In", View.GONE, View.GONE, 0);

		txt_username = (EditText) findViewById(R.id.txt_username);
		et_pwd = (EditText) findViewById(R.id.et_pwd);

		btn_login = (TextView) findViewById(R.id.btn_login);
		btn_forgot_psw = (TextView) findViewById(R.id.btn_forgot_psw);
		btn_sign_up = (TextView) findViewById(R.id.btn_sign_up);
		btn_login.setOnClickListener(onClickListener);
		btn_forgot_psw.setOnClickListener(onClickListener);
		btn_sign_up.setOnClickListener(onClickListener);

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
				finish();
				break;

			case R.id.btn_login://
				signIn();
				break;
			case R.id.btn_forgot_psw:
				startActivity(new Intent(MyLoginActivity.this,
						MyForgotPswActivity.class));
				break;
			case R.id.btn_sign_up:
				startActivity(new Intent(MyLoginActivity.this,
						MyRegisterActivity.class));
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 登录
	 */
	private void signIn() {
		String emailString = txt_username.getText().toString();
		String pswString = et_pwd.getText().toString();
		if ("".equals(emailString)) {
			showNothingInputDialog(R.id.txt_username);

		} else if ("".equals(pswString)) {
			showNothingInputDialog(R.id.et_pwd);
		} else {
			// sign in
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

		title.setText("Title");
		switch (editId) {
		case R.id.txt_username:
			content.setText("Please input your email");
			break;
		case R.id.et_pwd:
			content.setText("Please input your password");
			break;

		default:
			break;
		}

		title.setGravity(Gravity.CENTER_HORIZONTAL);
		ok.setText("OK");
		cancel.setVisibility(View.GONE);
		cancel.setText("Cancel");
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
