package com.hw.chineseLearn.tabMe;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.interfaces.HttpInterfaces;
import com.util.thread.ThreadWithDialogTask;

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
				break;

			default:
				break;
			}
		}
	};

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
