package com.hw.chineseLearn.tabDiscover;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseFragment;

/**
 * Discover
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class DiscoverFragment extends BaseFragment implements OnClickListener {
	private View contentView;// 主view

	public static DiscoverFragment discoverFragment;
	private RelativeLayout rel_survival_kit, rel_pinyin_chart, rel_pinyin_tone,
			rel_strokes_order, rel_fluent_now;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		discoverFragment = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_discover, null);
		init();
		return contentView;
	}

	/**
	 * 初始化
	 */
	public void init() {
		rel_survival_kit = (RelativeLayout) contentView
				.findViewById(R.id.rel_survival_kit);
		rel_pinyin_chart = (RelativeLayout) contentView
				.findViewById(R.id.rel_pinyin_chart);
		rel_pinyin_tone = (RelativeLayout) contentView
				.findViewById(R.id.rel_pinyin_tone);
		rel_strokes_order = (RelativeLayout) contentView
				.findViewById(R.id.rel_strokes_order);
		rel_fluent_now = (RelativeLayout) contentView
				.findViewById(R.id.rel_fluent_now);

		rel_survival_kit.setOnClickListener(this);
		rel_pinyin_chart.setOnClickListener(this);
		rel_pinyin_tone.setOnClickListener(this);
		rel_strokes_order.setOnClickListener(this);
		rel_fluent_now.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rel_survival_kit:
			startActivity(new Intent(getActivity(), SurvivalKitActivity.class));
			getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);//开启为单例 不用系统默认动画
			break;
		case R.id.rel_pinyin_chart:
			startActivity(new Intent(getActivity(),PinyinExerciseActivity.class));
			break;
		case R.id.rel_pinyin_tone:
			startActivity(new Intent(getActivity(), PinyinToneActivity.class));
			break;
		case R.id.rel_strokes_order:
			startActivity(new Intent(getActivity(), StrokesOrderActivity.class));

			break;
		case R.id.rel_fluent_now:
			startActivity(new Intent(getActivity(), FluentListActivity.class));
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

	@Override
	public boolean isRight() {
		// TODO Auto-generated method stub
		return false;
	}

}
