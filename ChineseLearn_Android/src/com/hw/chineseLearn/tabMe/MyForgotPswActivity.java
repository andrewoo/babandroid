package com.hw.chineseLearn.tabMe;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.interfaces.HttpInterfaces;
import com.util.thread.ThreadWithDialogTask;

/**
 * 忘记密码
 * 
 * @author yh
 */
public class MyForgotPswActivity extends BaseActivity {

	private String TAG = "==MyForgotPswActivity==";
	private Context context;

	EditText et_email;
	TextView btn_confirm;

	private ThreadWithDialogTask tdt;
	HttpInterfaces interfaces;
	View contentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_forgot_psw, null);
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
	public void init() {//iv_close
		iv_close = (ImageView) findViewById(R.id.iv_close); 
		iv_close.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				CustomApplication.app.finishActivity(MyForgotPswActivity.class);
				return false;
			}
		});
		tv_email = (TextView) findViewById(R.id.tv_email);
		et_email = (EditText) findViewById(R.id.et_email);
		et_email.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count == 0) {
					tv_email.setVisibility(View.INVISIBLE);
				}else{
					tv_email.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		btn_confirm = (TextView) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(onClickListener);

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
				CustomApplication.app.finishActivity(MyForgotPswActivity.this);
				break;

			case R.id.btn_confirm:
				if ("".equals(et_email.getText().toString()))
					showNothingInputDialog();

				// confirm
				break;

			default:
				break;
			}
		}
	};
	private TextView tv_email;
	private ImageView iv_close;

	/**
	 * 对话框
	 */
	private void showNothingInputDialog() {

		final AlertDialog mModifyDialog = new AlertDialog.Builder(context)
				.create();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.layout_dialog_loginout, null);
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		title.setVisibility(View.GONE);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		Button ok = (Button) view.findViewById(R.id.commit_btn);
		Button cancel = (Button) view.findViewById(R.id.cancel_btn);
		cancel.setVisibility(View.GONE);

		title.setText(getString(R.string.title_title));
		content.setText(getString(R.string.tabme_email));
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		ok.setText(getString(R.string.tabme_ok));
		ok.setOnClickListener(new OnClickListener() {
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
