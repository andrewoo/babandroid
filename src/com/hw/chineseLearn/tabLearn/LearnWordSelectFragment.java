package com.hw.chineseLearn.tabLearn;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.LearnUnitAdapter;
import com.hw.chineseLearn.adapter.LearnWordSelectListAdapter;
import com.hw.chineseLearn.adapter.ReviewListAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.thread.ThreadWithDialogTask;
import com.util.weight.SelfGridView;

/**
 * 句子选择
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnWordSelectFragment extends BaseFragment implements
		OnClickListener {
	private View contentView;// 主view

	private ThreadWithDialogTask task;
	public static LearnWordSelectFragment fragment;
	Context context;
	TextView txt_name;
	String question = "我是男人";
	private ListView listView;
	LearnWordSelectListAdapter adapter;
	ArrayList<LearnUnitBaseModel> listBase = new ArrayList<LearnUnitBaseModel>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();

		for (int i = 0; i < 4; i++) {
			LearnUnitBaseModel modelBase1 = new LearnUnitBaseModel();
			if (i == 0) {
				modelBase1.setUnitName("I am a man.");
			} else if (i == 1) {
				modelBase1.setUnitName("I am a woman.");
			} else if (i == 2) {
				modelBase1.setUnitName("He is a man.");
			} else if (i == 3) {
				modelBase1.setUnitName("She is a girl.");
			}

			listBase.add(modelBase1);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_lesson_word_select,
				null);
		task = new ThreadWithDialogTask();
		init();
		System.out.println("onCreateView");
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
		// task.RunWithMsg(getActivity(), new LoadNoticesThread(),
		// "Learn is loading…");
	}

	/**
	 * 初始化
	 */
	public void init() {
		txt_name = (TextView) contentView.findViewById(R.id.txt_name);
		txt_name.setText(question);

		listView = (ListView) contentView.findViewById(R.id.list_view);
		adapter = new LearnWordSelectListAdapter(context, listBase);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onItemclickListener);
		adapter.notifyDataSetChanged();
	}

	OnItemClickListener onItemclickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View convertView,
				int position, long arg3) {
			// TODO Auto-generated method stub

			adapter.setSelection(position);
			adapter.notifyDataSetChanged();

		}
	};

}
