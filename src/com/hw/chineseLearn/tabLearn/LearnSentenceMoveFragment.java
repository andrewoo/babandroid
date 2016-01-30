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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.LearnUnitAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.thread.ThreadWithDialogTask;
import com.util.weight.SelfGridView;

/**
 * 学习-首页
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnSentenceMoveFragment extends BaseFragment implements OnClickListener {
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
		contentView = inflater.inflate(R.layout.fragment_learn, null);

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
		getViewPagerData();
	}

	ArrayList<LearnUnitBaseModel> listBase = new ArrayList<LearnUnitBaseModel>();
	ArrayList<LearnUnitBaseModel> listAdvance = new ArrayList<LearnUnitBaseModel>();

	SelfGridView centGridView, centGridView1;

	/**
	 * 显示数据并设置监听
	 * 
	 * @param
	 */
	public void getViewPagerData() {
		// TODO Auto-generated method stub
		centGridView = (SelfGridView) contentView
				.findViewById(R.id.gv_center_gridview);

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

		LearnUnitBaseModel modelBase5 = new LearnUnitBaseModel();
		modelBase5.setIconResSuffix("lu0_1_5");
		modelBase5.setUnitName("Number&Measure");
		modelBase5.setLessonList("1;2;3");
		listBase.add(modelBase5);

		LearnUnitBaseModel modelBase6 = new LearnUnitBaseModel();
		modelBase6.setIconResSuffix("lu0_1_6");
		modelBase6.setUnitName("Food");
		modelBase6.setLessonList("1;2;3");
		listBase.add(modelBase6);

		LearnUnitBaseModel modelBase7 = new LearnUnitBaseModel();
		modelBase7.setIconResSuffix("lu0_2_1");
		modelBase7.setUnitName("Shape");
		modelBase7.setLessonList("1;2;3");
		listBase.add(modelBase7);

		LearnUnitBaseModel modelBase8 = new LearnUnitBaseModel();
		modelBase8.setIconResSuffix("lu0_2_2");
		modelBase8.setUnitName("Nature");
		modelBase8.setLessonList("1;2;3");
		listBase.add(modelBase8);

		LearnUnitBaseModel modelBase9 = new LearnUnitBaseModel();
		modelBase9.setIconResSuffix("lu0_2_3");
		modelBase9.setUnitName("Negation");
		modelBase9.setLessonList("1;2;3");
		listBase.add(modelBase9);

		LearnUnitBaseModel modelBase10 = new LearnUnitBaseModel();
		modelBase10.setIconResSuffix("lu0_2_4");
		modelBase10.setUnitName("Question");
		modelBase10.setLessonList("1;2;3");
		listBase.add(modelBase10);

		LearnUnitBaseModel modelBase11 = new LearnUnitBaseModel();
		modelBase11.setIconResSuffix("lu0_3_1");
		modelBase11.setUnitName("Time");
		modelBase11.setLessonList("1;2;3");
		listBase.add(modelBase11);

		LearnUnitBaseModel modelBase12 = new LearnUnitBaseModel();
		modelBase12.setIconResSuffix("lu0_3_2");
		modelBase12.setUnitName("Tense");
		modelBase12.setLessonList("1;2;3;4");
		listBase.add(modelBase12);

		learnUnit1Adapter = new LearnUnitAdapter(context, listBase);
		centGridView.setAdapter(learnUnit1Adapter);
		centGridView.setOnItemClickListener(itemClickListener1);
		learnUnit1Adapter.notifyDataSetChanged();

		ImageView img = (ImageView) contentView.findViewById(R.id.img);
		img.setImageResource(context.getResources().getIdentifier(
				"account_changepsw", "drawable", context.getPackageName()));

		centGridView1 = (SelfGridView) contentView
				.findViewById(R.id.gv_center_gridview1);

		LearnUnitBaseModel modelAdvance1 = new LearnUnitBaseModel();
		modelAdvance1.setIconResSuffix("lu0_3_3");
		modelAdvance1.setUnitName("Number");
		modelAdvance1.setLessonList("1;2;3");
		listAdvance.add(modelAdvance1);

		LearnUnitBaseModel modelAdvance2 = new LearnUnitBaseModel();
		modelAdvance2.setIconResSuffix("lu0_3_4");
		modelAdvance2.setUnitName("Animal & Adverb");
		modelAdvance2.setLessonList("1;2;3");
		listAdvance.add(modelAdvance2);

		LearnUnitBaseModel modelAdvance3 = new LearnUnitBaseModel();
		modelAdvance3.setIconResSuffix("lu0_3_5");
		modelAdvance3.setUnitName("Question");
		modelAdvance3.setLessonList("1;2;3");
		listAdvance.add(modelAdvance3);

		LearnUnitBaseModel modelAdvance4 = new LearnUnitBaseModel();
		modelAdvance4.setIconResSuffix("lu0_4_1");
		modelAdvance4.setUnitName("Family & Adjective");
		modelAdvance4.setLessonList("1;2;3");
		listAdvance.add(modelAdvance4);

		LearnUnitBaseModel modelAdvance5 = new LearnUnitBaseModel();
		modelAdvance5.setIconResSuffix("lu0_4_2");
		modelAdvance5.setUnitName("Clothing & Aspect");
		modelAdvance5.setLessonList("1;2;3");
		listAdvance.add(modelAdvance5);

		LearnUnitBaseModel modelAdvance6 = new LearnUnitBaseModel();
		modelAdvance6.setIconResSuffix("lu0_4_3");
		modelAdvance6.setUnitName("Shopping");
		modelAdvance6.setLessonList("1;2;3");
		listAdvance.add(modelAdvance6);

		LearnUnitBaseModel modelAdvance7 = new LearnUnitBaseModel();
		modelAdvance7.setIconResSuffix("lu0_4_4");
		modelAdvance7.setUnitName("HouseHold & Existential");
		modelAdvance7.setLessonList("1;2;3");
		listAdvance.add(modelAdvance7);

		LearnUnitBaseModel modelAdvance8 = new LearnUnitBaseModel();
		modelAdvance8.setIconResSuffix("lu0_4_5");
		modelAdvance8.setUnitName("Country & Verb+guo");
		modelAdvance8.setLessonList("1;2;3");
		listAdvance.add(modelAdvance8);

		LearnUnitBaseModel modelAdvance9 = new LearnUnitBaseModel();
		modelAdvance9.setIconResSuffix("lu0_4_6");
		modelAdvance9.setUnitName("Language");
		modelAdvance9.setLessonList("1;2;3");
		listAdvance.add(modelAdvance9);

		LearnUnitBaseModel modelAdvance10 = new LearnUnitBaseModel();
		modelAdvance10.setIconResSuffix("lu0_4_7");
		modelAdvance10.setUnitName("A+bu/mei+A");
		modelAdvance10.setLessonList("1;2;3");
		listAdvance.add(modelAdvance10);

		LearnUnitBaseModel modelAdvance11 = new LearnUnitBaseModel();
		modelAdvance11.setIconResSuffix("lu0_5_1");
		modelAdvance11.setUnitName("Travel & Alternative");
		modelAdvance11.setLessonList("1;2;3");
		listAdvance.add(modelAdvance11);

		LearnUnitBaseModel modelAdvance12 = new LearnUnitBaseModel();
		modelAdvance12.setIconResSuffix("lu0_5_2");
		modelAdvance12.setUnitName("Festival");
		modelAdvance12.setLessonList("1;2;3");
		listAdvance.add(modelAdvance12);

		LearnUnitBaseModel modelAdvance13 = new LearnUnitBaseModel();
		modelAdvance13.setIconResSuffix("lu0_5_3");
		modelAdvance13.setUnitName("Dinning & Double Object");
		modelAdvance13.setLessonList("1;2;3");
		listAdvance.add(modelAdvance13);

		LearnUnitBaseModel modelAdvance14 = new LearnUnitBaseModel();
		modelAdvance14.setIconResSuffix("lu0_5_4");
		modelAdvance14.setUnitName("Food & Passive Voice");
		modelAdvance14.setLessonList("1;2;3");
		listAdvance.add(modelAdvance14);

		learnUnit2Adapter = new LearnUnitAdapter(context, listAdvance);
		centGridView1.setAdapter(learnUnit2Adapter);
		centGridView1.setOnItemClickListener(itemClickListener1);
		learnUnit2Adapter.notifyDataSetChanged();
	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			LearnUnitBaseModel learnUnitBaseModel = listBase.get(arg2);
			if (learnUnitBaseModel != null) {
				boolean isEnable = learnUnitBaseModel.isEnable();
				// if (isEnable) {
				startActivity(new Intent(getActivity(),
						LessonViewActivity.class));
				// }
			}
		}
	};

	OnItemClickListener itemClickListener1 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			LearnUnitBaseModel learnUnitBaseModel = listAdvance.get(arg2);
			if (learnUnitBaseModel != null) {
				boolean isEnable = learnUnitBaseModel.isEnable();
				// if (isEnable) {
				startActivity(new Intent(getActivity(),
						LessonViewActivity.class));
				// }
			}
		}
	};

}
