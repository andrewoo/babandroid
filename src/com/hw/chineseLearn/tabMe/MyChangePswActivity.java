package com.hw.chineseLearn.tabMe;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.util.tool.Utility;

/**
 * 修改密码
 * 
 * @author yh
 */
public class MyChangePswActivity extends BaseActivity {

	private String TAG = "==MyChangePswActivity==";
	private Context context;

	EditText et_old_psw, et_new_psw, et_confirm_new_psw;
	TextView btn_confirm;

	private ThreadWithDialogTask tdt;
	HttpInterfaces interfaces;
	View contentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_change_psw2, null);
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
	public void init() {
		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_close.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				CustomApplication.app.finishActivity(MyChangePswActivity.class);
				return false;
			}
		});
		// oldpassword
		et_old_psw = (EditText) findViewById(R.id.et_old_psw);
		changeEyeIcon(et_old_psw);
		tv_old_psw = (TextView) findViewById(R.id.tv_old_psw);
		showOrhideTextView(et_old_psw, tv_old_psw);
		// newpassword
		et_new_psw = (EditText) findViewById(R.id.et_new_psw);
		changeEyeIcon(et_new_psw);
		tv_new_psw = (TextView) findViewById(R.id.tv_new_psw);
		showOrhideTextView(et_new_psw, tv_new_psw);
		// change newpassword
		 et_confirm_new_psw = (EditText) findViewById(R.id.et_confirm_new_psw);
		changeEyeIcon(et_confirm_new_psw);
		TextView tv_confirm_new_psw = (TextView) findViewById(R.id.tv_confirm_new_psw);
		showOrhideTextView(et_confirm_new_psw, tv_confirm_new_psw);
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

//			case R.id.iv_title_left:// 返回
//				closeWindowSoftInput();
//				CustomApplication.app.finishActivity(MyChangePswActivity.this);
//				break;

			case R.id.btn_confirm:
				// if ("".equals(et_old_psw.getText().toString())) {
				// // showNothingInputDialog(R.id.et_old_psw);
				// } else if ("".equals(et_new_psw.getText().toString())) {
				// showNothingInputDialog(R.id.et_new_psw);
				// } else if
				// ("".equals(et_confirm_new_psw.getText().toString())) {
				// showNothingInputDialog(R.id.et_confirm_new_psw);
				// } else {
				//
				// }
				String oldPwd = et_old_psw.getText().toString().trim();
				String newPwd = et_new_psw.getText().toString().trim();
				String newCPwd = et_confirm_new_psw.getText().toString().trim();
				if (oldPwd.length() < 6 || newPwd.length() < 6
						|| newCPwd.length() < 6) {
					Toast.makeText(MyChangePswActivity.this,
							"at lease 6 characters", Toast.LENGTH_SHORT).show();
					return;
				} else if (!newPwd.equals(newCPwd)) {
					Toast.makeText(MyChangePswActivity.this,
							"Two passwords are not consistent please re-enter",
							Toast.LENGTH_SHORT).show();
					return;
				}
				String token = CustomApplication.app.preferencesUtil.getValue(
						AppConstants.LOGIN_TOKEN, "");
				RequestParams params = new RequestParams();
				params.addBodyParameter("oldPwd", oldPwd);
				params.addBodyParameter("newPwd", newPwd);
				params.addBodyParameter("token", token);// token
				HttpUtils http = new HttpUtils();
				String url = AppConstants.BASE_URL+"/babble-api-app/v1/users/password/reset";
				http.send(HttpRequest.HttpMethod.POST, url, params,
						new RequestCallBack<String>() {

							@Override
							public void onStart() {
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
							}

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								try {
									JSONObject jsobject = new JSONObject(
											responseInfo.result);
									Boolean success = jsobject
											.optBoolean("success");
									if (success) {
										CustomApplication.app.preferencesUtil
												.setValue(
														AppConstants.LOGIN_PWD,
														"");
										CustomApplication.app.preferencesUtil
												.setValue(
														AppConstants.LOGIN_TOKEN,
														"");
										closeWindowSoftInput();
										startActivity(new Intent(context,
												MyLoginActivity.class));
										CustomApplication.app
												.finishActivity(MyChangePswActivity.this);
										return;
									}
									Toast.makeText(MyChangePswActivity.this,
											"Changer failed",
											Toast.LENGTH_SHORT).show();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								Toast.makeText(context, "network error",
										Toast.LENGTH_SHORT).show();
							}
						});

				closeWindowSoftInput();
				break;

			default:
				break;
			}
		}
	};
	private ImageView iv_close;
	private TextView tv_old_psw;
	private TextView tv_new_psw;

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
		cancel.setVisibility(View.GONE);

		title.setText("Title");
		switch (editId) {
		// et_old_psw, et_new_psw, et_confirm_new_psw;
		case R.id.et_old_psw:
			content.setText("Please input your old password");
			break;
		case R.id.et_new_psw:
			content.setText("Please input your new password");
			break;
		case R.id.et_confirm_new_psw:
			content.setText("Please input your new password");
			break;

		default:
			break;
		}

		title.setGravity(Gravity.CENTER_HORIZONTAL);
		ok.setText("OK");
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mModifyDialog.dismiss();
			}
		});

		mModifyDialog.show();
		mModifyDialog.setContentView(view);
	}

	/**
	 * 对话框
	 */
	private void showInputErrorDialog(int editId) {

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

		title.setText("Title");
		switch (editId) {
		// et_old_psw, et_new_psw, et_confirm_new_psw;
		case R.id.et_old_psw:
			content.setText("Please input your old password");
			break;
		case R.id.et_new_psw:
			content.setText("Please input your new password");
			break;
		case R.id.et_confirm_new_psw:
			content.setText("Please input your new password");
			break;

		default:
			break;
		}

		title.setGravity(Gravity.CENTER_HORIZONTAL);
		ok.setText("OK");
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mModifyDialog.dismiss();
			}
		});

		mModifyDialog.show();
		mModifyDialog.setContentView(view);
	}

	public void changeEyeIcon(final EditText et) {
		et.setOnTouchListener(new OnTouchListener() {// 此监听做两件事1 改变password/text
														// 2 改变点击图标

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
				Drawable drawable = et.getCompoundDrawables()[2];
				// 如果右边没有图片，不再处理
				if (drawable == null)
					return false;
				// 如果不是按下事件，不再处理
				if (event.getAction() != MotionEvent.ACTION_UP)
					return false;
				if (event.getX() > et.getWidth() - et.getPaddingRight()
						- drawable.getIntrinsicWidth()) {
					if (et.getInputType() == EditorInfo.TYPE_CLASS_TEXT) {// InputType.TYPE_CLASS_TEXT
																			// |
																			// InputType.TYPE_TEXT_VARIATION_PASSWORD
						et.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						et.setSelection(et.getText().length());
						Drawable image = context.getResources().getDrawable(
								R.drawable.viewpassword);
						et.setCompoundDrawablesWithIntrinsicBounds(null, null,
								image, null);

					} else {
						Drawable image = context.getResources().getDrawable(
								R.drawable.hidepassword);
						et.setCompoundDrawablesWithIntrinsicBounds(null, null,
								image, null);
						et.setInputType(EditorInfo.TYPE_CLASS_TEXT);
						et.setSelection(et.getText().length());
					}
				}
				return false;
			}
		});
	}

	private void showOrhideTextView(EditText et, final TextView tv) {
		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 0) {
					tv.setVisibility(View.INVISIBLE);
				} else {
					tv.setVisibility(View.VISIBLE);
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
