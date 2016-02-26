package com.hw.chineseLearn.tabLearn;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.LearnImageSelectAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.UiUtil;

/**
 * 学习-选图片
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnImageSelectFragment extends BaseFragment implements
		OnClickListener {
	private View contentView;// 主view

	private ThreadWithDialogTask task;
	public static LearnImageSelectFragment fragment;
	Context context;
	LearnImageSelectAdapter learnImageSelectAdapter;
	ArrayList<LearnUnitBaseModel> listBase = new ArrayList<LearnUnitBaseModel>();
	GridView gv_image;
	TextView txt_name;
	String question = "\"color\"";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();

		LearnUnitBaseModel modelBase1 = new LearnUnitBaseModel();
		modelBase1.setIconResSuffix("lu1_1_1");
		modelBase1.setUnitName("Basics1");
		modelBase1.setLessonList("1;");
		listBase.add(modelBase1);

		LearnUnitBaseModel modelBase2 = new LearnUnitBaseModel();
		modelBase2.setIconResSuffix("lu0_1_2");
		modelBase2.setUnitName("Basics2");
		modelBase2.setLessonList("1;2;3;4");
		listBase.add(modelBase2);

		LearnUnitBaseModel modelBase3 = new LearnUnitBaseModel();
		modelBase3.setIconResSuffix("lu0_1_3");
		modelBase3.setUnitName("Basics3");
		modelBase3.setLessonList("1;2;3");
		listBase.add(modelBase3);

		LearnUnitBaseModel modelBase4 = new LearnUnitBaseModel();
		modelBase4.setIconResSuffix("lu0_1_4");
		modelBase4.setUnitName("Color");
		modelBase4.setLessonList("1;2;3");
		listBase.add(modelBase4);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_lesson_image_select,
				null);

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
		// task.RunWithMsg(getActivity(), new LoadNoticesThread(),
		// "Learn is loading…");
	}

	/**
	 * 初始化
	 */
	public void init() {
		// TODO Auto-generated method stub
		txt_name = (TextView) contentView.findViewById(R.id.txt_name);
		txt_name.setText("Select " + question);

		gv_image = (GridView) contentView.findViewById(R.id.gv_image);

		learnImageSelectAdapter = new LearnImageSelectAdapter(context, listBase);
		gv_image.setAdapter(learnImageSelectAdapter);
		gv_image.setOnItemClickListener(itemClickListener);
		learnImageSelectAdapter.notifyDataSetChanged();

	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			learnImageSelectAdapter.setSelection(position);
			learnImageSelectAdapter.notifyDataSetChanged();
			LearnUnitBaseModel learnUnitBaseModel = listBase.get(position);
			if (learnUnitBaseModel != null) {
			}
		}
	};

}
