package com.hw.chineseLearn.tabMe;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.interfaces.HttpInterfaces;
import com.hw.chineseLearn.model.SimpleModel;
import com.util.thread.ThreadWithDialogTask;

/**
 * 设置
 * 
 * @author yh
 */
public class MySettingActivity extends BaseActivity {

	private String TAG = "==MySettingActivity==";
	private Context context;

	private RelativeLayout rel_reminders;
	private RelativeLayout rel_logout;
	private ThreadWithDialogTask tdt;
	HttpInterfaces interfaces;
	SimpleModel simpleModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
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
				"Settings", View.GONE, View.GONE, 0);

		rel_reminders = (RelativeLayout) findViewById(R.id.rel_reminders);
		rel_logout = (RelativeLayout) findViewById(R.id.rel_logout);
		rel_logout.setVisibility(View.GONE);
		rel_reminders.setOnClickListener(onClickListener);
		rel_logout.setOnClickListener(onClickListener);

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

		View view_title = (View) this.findViewById(R.id.view_title);
		TextView tv_title = (TextView) view_title.findViewById(R.id.tv_title);
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

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				finish();
				break;

			case R.id.rel_reminders:
				// Intent intent2 = new Intent();
				// intent2.setClass(MySettingActivity.this,
				// YTGAboutActivity1.class);
				// startActivity(intent2);

				break;

			case R.id.rel_logout:
				showLogOutDialog();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 退出登录对话框
	 */
	private void showLogOutDialog() {

		final AlertDialog mModifyDialog = new AlertDialog.Builder(
				MySettingActivity.this).create();
		LayoutInflater inflater = LayoutInflater.from(MySettingActivity.this);
		View view = inflater.inflate(R.layout.layout_dialog_loginout, null);
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		Button ok = (Button) view.findViewById(R.id.commit_btn);
		Button cancel = (Button) view.findViewById(R.id.cancel_btn);
		title.setText("退出");
		content.setText("您确定要退出吗？");
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		ok.setText("确定");
		cancel.setText("取消");
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// tdt.RunWithMsg(MySettingActivity.this, new LoginOut(),
				// "正在退出...");
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
