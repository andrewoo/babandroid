package com.hw.chineseLearn.tabLearn;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.HomeFunctionAdapter_new;
import com.hw.chineseLearn.adapter.LearnUnitAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.model.LearnSubjectBaseModel;
import com.hw.chineseLearn.model.NoticeModel;
import com.util.thread.ThreadWithDialogTask;
import com.util.weight.AddressSelectPopMenu;
import com.util.weight.SelfGridView;

/**
 * 学习-首页
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnFragment extends BaseFragment implements OnClickListener {
	private View contentView;// 主view

	private ThreadWithDialogTask task;
	public static LearnFragment fragment;
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

	ArrayList<LearnSubjectBaseModel> listBase = new ArrayList<LearnSubjectBaseModel>();
	ArrayList<LearnSubjectBaseModel> listAdvance = new ArrayList<LearnSubjectBaseModel>();

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

		LearnSubjectBaseModel modelBase1 = new LearnSubjectBaseModel();
		modelBase1.setIconResSuffix("lu1_1_1");
		modelBase1.setUnitName("Basics1");
		modelBase1.setLessonList("1;");
		listBase.add(modelBase1);

		LearnSubjectBaseModel modelBase2 = new LearnSubjectBaseModel();
		modelBase2.setIconResSuffix("lu0_1_2");
		modelBase2.setUnitName("Basics2");
		modelBase2.setLessonList("1;2;3;4");
		listBase.add(modelBase2);

		LearnSubjectBaseModel modelBase3 = new LearnSubjectBaseModel();
		modelBase3.setIconResSuffix("lu0_1_3");
		modelBase3.setUnitName("Basics3");
		modelBase3.setLessonList("1;2;3");
		listBase.add(modelBase3);

		LearnSubjectBaseModel modelBase4 = new LearnSubjectBaseModel();
		modelBase4.setIconResSuffix("lu0_1_4");
		modelBase4.setUnitName("Color");
		modelBase4.setLessonList("1;2;3");
		listBase.add(modelBase4);

		LearnSubjectBaseModel modelBase5 = new LearnSubjectBaseModel();
		modelBase5.setIconResSuffix("lu0_1_5");
		modelBase5.setUnitName("Number&Measure");
		modelBase5.setLessonList("1;2;3");
		listBase.add(modelBase5);

		LearnSubjectBaseModel modelBase6 = new LearnSubjectBaseModel();
		modelBase6.setIconResSuffix("lu0_1_6");
		modelBase6.setUnitName("Food");
		modelBase6.setLessonList("1;2;3");
		listBase.add(modelBase6);

		LearnSubjectBaseModel modelBase7 = new LearnSubjectBaseModel();
		modelBase7.setIconResSuffix("lu0_2_1");
		modelBase7.setUnitName("Shape");
		modelBase7.setLessonList("1;2;3");
		listBase.add(modelBase7);

		LearnSubjectBaseModel modelBase8 = new LearnSubjectBaseModel();
		modelBase8.setIconResSuffix("lu0_2_2");
		modelBase8.setUnitName("Nature");
		modelBase8.setLessonList("1;2;3");
		listBase.add(modelBase8);

		LearnSubjectBaseModel modelBase9 = new LearnSubjectBaseModel();
		modelBase9.setIconResSuffix("lu0_2_3");
		modelBase9.setUnitName("Negation");
		modelBase9.setLessonList("1;2;3");
		listBase.add(modelBase9);

		LearnSubjectBaseModel modelBase10 = new LearnSubjectBaseModel();
		modelBase10.setIconResSuffix("lu0_2_4");
		modelBase10.setUnitName("Question");
		modelBase10.setLessonList("1;2;3");
		listBase.add(modelBase10);

		LearnSubjectBaseModel modelBase11 = new LearnSubjectBaseModel();
		modelBase11.setIconResSuffix("lu0_3_1");
		modelBase11.setUnitName("Time");
		modelBase11.setLessonList("1;2;3");
		listBase.add(modelBase11);

		LearnSubjectBaseModel modelBase12 = new LearnSubjectBaseModel();
		modelBase12.setIconResSuffix("lu0_3_2");
		modelBase12.setUnitName("Tense");
		modelBase12.setLessonList("1;2;3;4");
		listBase.add(modelBase12);

		learnUnit1Adapter = new LearnUnitAdapter(context, listBase);
		centGridView.setAdapter(learnUnit1Adapter);
		learnUnit1Adapter.notifyDataSetChanged();

		ImageView img = (ImageView) contentView.findViewById(R.id.img);
		img.setImageResource(context.getResources().getIdentifier(
				"account_changepsw", "drawable", context.getPackageName()));

		centGridView1 = (SelfGridView) contentView
				.findViewById(R.id.gv_center_gridview1);

		LearnSubjectBaseModel modelAdvance1 = new LearnSubjectBaseModel();
		modelAdvance1.setIconResSuffix("lu0_3_3");
		modelAdvance1.setUnitName("Number");
		modelAdvance1.setLessonList("1;2;3");
		listAdvance.add(modelAdvance1);

		LearnSubjectBaseModel modelAdvance2 = new LearnSubjectBaseModel();
		modelAdvance2.setIconResSuffix("lu0_3_4");
		modelAdvance2.setUnitName("Animal & Adverb");
		modelAdvance2.setLessonList("1;2;3");
		listAdvance.add(modelAdvance2);

		LearnSubjectBaseModel modelAdvance3 = new LearnSubjectBaseModel();
		modelAdvance3.setIconResSuffix("lu0_3_5");
		modelAdvance3.setUnitName("Question");
		modelAdvance3.setLessonList("1;2;3");
		listAdvance.add(modelAdvance3);

		LearnSubjectBaseModel modelAdvance4 = new LearnSubjectBaseModel();
		modelAdvance4.setIconResSuffix("lu0_4_1");
		modelAdvance4.setUnitName("Family & Adjective");
		modelAdvance4.setLessonList("1;2;3");
		listAdvance.add(modelAdvance4);

		LearnSubjectBaseModel modelAdvance5 = new LearnSubjectBaseModel();
		modelAdvance5.setIconResSuffix("lu0_4_2");
		modelAdvance5.setUnitName("Clothing & Aspect");
		modelAdvance5.setLessonList("1;2;3");
		listAdvance.add(modelAdvance5);

		LearnSubjectBaseModel modelAdvance6 = new LearnSubjectBaseModel();
		modelAdvance6.setIconResSuffix("lu0_4_3");
		modelAdvance6.setUnitName("Shopping");
		modelAdvance6.setLessonList("1;2;3");
		listAdvance.add(modelAdvance6);

		LearnSubjectBaseModel modelAdvance7 = new LearnSubjectBaseModel();
		modelAdvance7.setIconResSuffix("lu0_4_4");
		modelAdvance7.setUnitName("HouseHold & Existential");
		modelAdvance7.setLessonList("1;2;3");
		listAdvance.add(modelAdvance7);

		LearnSubjectBaseModel modelAdvance8 = new LearnSubjectBaseModel();
		modelAdvance8.setIconResSuffix("lu0_4_5");
		modelAdvance8.setUnitName("Country & Verb+guo");
		modelAdvance8.setLessonList("1;2;3");
		listAdvance.add(modelAdvance8);

		LearnSubjectBaseModel modelAdvance9 = new LearnSubjectBaseModel();
		modelAdvance9.setIconResSuffix("lu0_4_6");
		modelAdvance9.setUnitName("Language");
		modelAdvance9.setLessonList("1;2;3");
		listAdvance.add(modelAdvance9);

		LearnSubjectBaseModel modelAdvance10 = new LearnSubjectBaseModel();
		modelAdvance10.setIconResSuffix("lu0_4_7");
		modelAdvance10.setUnitName("A+bu/mei+A");
		modelAdvance10.setLessonList("1;2;3");
		listAdvance.add(modelAdvance10);

		LearnSubjectBaseModel modelAdvance11 = new LearnSubjectBaseModel();
		modelAdvance11.setIconResSuffix("lu0_5_1");
		modelAdvance11.setUnitName("Travel & Alternative");
		modelAdvance11.setLessonList("1;2;3");
		listAdvance.add(modelAdvance11);

		LearnSubjectBaseModel modelAdvance12 = new LearnSubjectBaseModel();
		modelAdvance12.setIconResSuffix("lu0_5_2");
		modelAdvance12.setUnitName("Festival");
		modelAdvance12.setLessonList("1;2;3");
		listAdvance.add(modelAdvance12);

		LearnSubjectBaseModel modelAdvance13 = new LearnSubjectBaseModel();
		modelAdvance13.setIconResSuffix("lu0_5_3");
		modelAdvance13.setUnitName("Dinning & Double Object");
		modelAdvance13.setLessonList("1;2;3");
		listAdvance.add(modelAdvance13);

		LearnSubjectBaseModel modelAdvance14 = new LearnSubjectBaseModel();
		modelAdvance14.setIconResSuffix("lu0_5_4");
		modelAdvance14.setUnitName("Food & Passive Voice");
		modelAdvance14.setLessonList("1;2;3");
		listAdvance.add(modelAdvance14);

		 learnUnit2Adapter = new LearnUnitAdapter(context, listAdvance);
		 centGridView1.setAdapter(learnUnit2Adapter);

	}
}
