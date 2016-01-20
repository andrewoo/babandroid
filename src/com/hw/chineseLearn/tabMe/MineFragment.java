package com.hw.chineseLearn.tabMe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseFragment;

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
			startActivity(new Intent(getActivity(), MyLoginActivity.class));
			break;
		case R.id.rel_download_mererials:

			break;
		case R.id.rel_like_us_on_facebook:

			break;
		case R.id.rel_rate_this_app:

			break;
		case R.id.rel_settings:
			startActivity(new Intent(getActivity(), MySettingActivity.class));
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
	}

	/**
	 * 初始化
	 */
	public void init() {
		lin_personal_info = (LinearLayout) contentView
				.findViewById(R.id.lin_personal_info);

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

}
