package com.hw.chineseLearn.tabLearn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.LearnUnitAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.util.thread.ThreadWithDialogTask;

/**
 * 拼句子
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnSentenceMoveFragment extends BaseFragment implements
		OnClickListener {
	private View contentView;// 主view

	private ThreadWithDialogTask task;
	public static LearnSentenceMoveFragment fragment;
	Context context;
	LearnUnitAdapter learnUnit1Adapter, learnUnit2Adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_lesson_sentence_move, null);

		task = new ThreadWithDialogTask();

		init();

		return contentView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

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
	}

}
