package com.hw.chineseLearn.tabMe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.base.MainWebActivity;
import com.hw.chineseLearn.interfaces.AppConstants;

/**
 * @author yh
 * 
 */
@SuppressLint("NewApi")
public class MineFragment extends BaseFragment implements OnClickListener {
	private View contentView;// 主view
	private LinearLayout lin_personal_info;
	private RadioGroup rg_chinese_display, rg_character_system;
	private RelativeLayout rel_download_mererials, rel_like_us_on_facebook,
			rel_rate_this_app, rel_settings;

	public static MineFragment fragment;

	private TextView text_name; 
	
	private RelativeLayout rel_reminders;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_mine, null);

		init();

		return contentView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_personal_info:
			if (CustomApplication.app.isLogin) {
				startActivity(new Intent(getActivity(), MyAccountActivity.class));
			} else {
				startActivity(new Intent(getActivity(), MyLoginActivity.class));
			}

			break;
		case R.id.rel_download_mererials:
			// startActivity(new Intent(getActivity(), MyLoginActivity.class));
			startActivity(new Intent(getActivity(), DownloadMererialsActivity.class));
			break;
		case R.id.rel_like_us_on_facebook:
			Intent intentFacebook = new Intent();
			intentFacebook.putExtra("url", "http://www.baidu.com");
			intentFacebook.putExtra("title", "Like us on Facebook");
			intentFacebook.setClass(getActivity(), MainWebActivity.class);
			startActivity(intentFacebook);

			break;
		case R.id.rel_rate_this_app:
//			Intent intentRate = new Intent();
//			intentRate.putExtra("url", "http://www.baidu.com");
//			intentRate.putExtra("title", "Rate this app");
//			intentRate.setClass(getActivity(), MainWebActivity.class);
//			startActivity(intentRate);
			break;
		case R.id.rel_settings:
			startActivity(new Intent(getActivity(), MySettingActivity.class));
			break;
			
		case R.id.rel_reminders:
			startActivity(new Intent(getActivity(),
					MySettingRemindersActivity.class));

			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUserData();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		closeWindowSoftInput();
	}

	/**
	 * 初始化
	 */
	public void init() {
		lin_personal_info = (LinearLayout) contentView
				.findViewById(R.id.lin_personal_info);
		
		//设置头像背景的高度为屏幕的1/4
		int heigh = CustomApplication.app.getScreenSize().heightPixels;
		
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, heigh/4);
		lin_personal_info.setLayoutParams(params);

		text_name = (TextView) contentView.findViewById(R.id.text_name);
		rel_reminders = (RelativeLayout)contentView.findViewById(R.id.rel_reminders);
		rel_reminders.setOnClickListener(this);
		rg_chinese_display = (RadioGroup) contentView
				.findViewById(R.id.rg_chinese_display);
		rg_chinese_display
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int checkedId) {
						// TODO Auto-generated method stub
						switch (checkedId) {

						case R.id.rb_character://

							break;
						case R.id.rb_Pinyin:

							break;
						case R.id.rb_Both:

							break;

						}
					}
				});
		rg_character_system = (RadioGroup) contentView
				.findViewById(R.id.rg_character_system);
		rg_character_system
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int checkedId) {
						// TODO Auto-generated method stub
						switch (checkedId) {

						case R.id.rb_simplified://

							break;
						case R.id.rb_traditional:

							break;

						}
					}
				});
		rel_download_mererials = (RelativeLayout) contentView
				.findViewById(R.id.rel_download_mererials);

		rel_like_us_on_facebook = (RelativeLayout) contentView
				.findViewById(R.id.rel_like_us_on_facebook);
		rel_rate_this_app = (RelativeLayout) contentView
				.findViewById(R.id.rel_rate_this_app);
		rel_settings = (RelativeLayout) contentView
				.findViewById(R.id.rel_settings);

		lin_personal_info.setOnClickListener(this);
		rel_download_mererials.setOnClickListener(this);
		rel_like_us_on_facebook.setOnClickListener(this);
		rel_rate_this_app.setOnClickListener(this);
		rel_settings.setOnClickListener(this);
	}

	/**
	 * 设置个人帐号数据
	 */
	private void setUserData() {

		if (CustomApplication.app.isLogin) {
			String emailString = CustomApplication.app.preferencesUtil
					.getValue(AppConstants.LOGIN_USERNAME, "");
			text_name.setText(emailString);
		} else {
			text_name.setText("Sign in/Sign up");
		}

	}

	private void closeWindowSoftInput() {

		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isSoftActive = imm.isActive();
		Log.d("-MianWebActivity-", "isSoftActive：" + isSoftActive);
		if (isSoftActive) {
			imm.hideSoftInputFromWindow(
					contentView.getApplicationWindowToken(), 0); // 强制隐藏键盘
			// Log.d(TAG, "强制隐藏键盘");
		}
	}

	@Override
	public boolean isRight() {
		// TODO Auto-generated method stub
		return false;
	}
}
