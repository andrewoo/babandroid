package com.hw.chineseLearn.tabLearn;

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
import android.widget.RelativeLayout;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.LearnUnitAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.base.MainActivity;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbLessonMaterialStatus;
import com.hw.chineseLearn.dao.bean.TbSetting;
import com.hw.chineseLearn.dao.bean.Unit;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.MediaPlayerHelper;
import com.util.tool.UiUtil;
import com.util.weight.SelfGridView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private RelativeLayout rel_test_out;
	Context context;
	LearnUnitAdapter learnUnit1Adapter, learnUnit2Adapter;
	private String TAG = "LearnFragment";

	ArrayList<Unit> firstList = new ArrayList<Unit>();// testout上边的集合
	ArrayList<Unit> sencondList = new ArrayList<Unit>();// testout下边的集合

	SelfGridView centGridView, centGridView1;
	private ImageView img_lock;

	private List<Unit> unitDataList;// 存储Unit的集合

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();
		getListDataAndSpit();// 得到数据库中集合数据并拆分为两个集合
	}



	/**
	 * 得到unit集合并拆分为两个子集合
	 */
	private void getListDataAndSpit() {

		MainActivity mainActicity = (MainActivity) context;
		unitDataList = mainActicity.getUnitData();// 得到数据库数据
		Collections.sort(unitDataList);// 对Unit排序
		// 把前12个存放到firstList
		for (int i = 0; i < 12; i++) {
			firstList.add(unitDataList.get(i));
		}
		for (int i = 12; i < unitDataList.size(); i++) {
			sencondList.add(unitDataList.get(i));
		}
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
		case R.id.rel_test_out:
			startActivity(new Intent(getActivity(), LessonTestOutActivity.class));
			break;
		default:
			break;
		}
	}

	/**
	 * 0未解锁1解锁
	 */
	int unLock = 0;

	@SuppressWarnings("unchecked")
	@Override
	public void onResume() {
		super.onResume();
		// 更新当前状态
		if (learnUnit1Adapter != null && learnUnit2Adapter != null) {
			learnUnit1Adapter.notifyDataSetChanged();
			learnUnit2Adapter.notifyDataSetChanged();
		}

		try {
			TbSetting tbSetting = (TbSetting) MyDao.getDaoMy(TbSetting.class)
					.queryForId(getString(R.string.learn_unlock));
			if (tbSetting != null) {
				unLock = tbSetting.getSettingValue();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (unLock == 0) {
			rel_test_out.setEnabled(true);
			rel_test_out.setBackground(context.getResources().getDrawable(
					R.drawable.btn_orange_bg_n));
			img_lock.setImageDrawable(context.getResources().getDrawable(
					R.drawable.testout_img));
		} else {
			rel_test_out.setEnabled(false);
			rel_test_out.setBackground(context.getResources().getDrawable(
					R.drawable.btn_grey_unlock));
			img_lock.setImageDrawable(context.getResources().getDrawable(
					R.drawable.testout_img_grey));
		}

	}

	/**
	 * 显示数据并设置监听
	 * 
	 * @param
	 */
	public void init() {
		// TODO Auto-generated method stub
		rel_test_out = (RelativeLayout) contentView
				.findViewById(R.id.rel_test_out);
		rel_test_out.setOnClickListener(this);

		img_lock = (ImageView) contentView.findViewById(R.id.img_lock);

		centGridView = (SelfGridView) contentView
				.findViewById(R.id.gv_center_gridview);

		if (learnUnit1Adapter == null) {
			learnUnit1Adapter = new LearnUnitAdapter(context, firstList);
			centGridView.setAdapter(learnUnit1Adapter);
			centGridView.setOnItemClickListener(itemClickListener);
			learnUnit1Adapter.notifyDataSetChanged();
		}

		centGridView1 = (SelfGridView) contentView
				.findViewById(R.id.gv_center_gridview1);
		if (learnUnit2Adapter == null) {
			learnUnit2Adapter = new LearnUnitAdapter(context, sencondList);
			centGridView1.setAdapter(learnUnit2Adapter);
			learnUnit2Adapter.notifyDataSetChanged();
		}
		centGridView1.setOnItemClickListener(itemClickListener1);
	}

	public void playPageIntoSound() {
		new MediaPlayerHelper("sounds/page_into.mp3").play();
	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			boolean isEnable = false;

			Unit unit = firstList.get(position);
			TbLessonMaterialStatus ms = null;
			try {
				ms = (TbLessonMaterialStatus) MyDao.getDaoMy(// 拿到第一个lessonid
																// 查表确定status
						TbLessonMaterialStatus.class).queryForId(
						UiUtil.getListFormString(unit.getLessonList())[0]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (ms != null) {
				if (ms.getStatus() == 0) {
					isEnable = false;
				} else {
					isEnable = true;
				}
			}
			if (unit != null) {
				// boolean isEnable = unit.isEnable();
				if (isEnable) {
					playPageIntoSound();
					Intent intent = new Intent(getActivity(),
							LessonViewActivity.class);
					intent.putExtra("unit", unit);
					intent.putExtra("position", position);
					startActivity(intent);
				}
			}
		}
	};

	OnItemClickListener itemClickListener1 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			boolean isEnable = false;
			Unit unit = sencondList.get(position);

			TbLessonMaterialStatus ms = null;
			try {
				ms = (TbLessonMaterialStatus) MyDao.getDaoMy(// 拿到第一个lessonid
																// 查表确定status
						TbLessonMaterialStatus.class).queryForId(
						UiUtil.getListFormString(unit.getLessonList())[0]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (ms != null) {
				if (ms.getStatus() == 0) {
					isEnable = false;
				} else {
					isEnable = true;
				}
			}

			if (unit != null) {
				// boolean isEnable = learnUnitBaseModel.isEnable();
				if (isEnable) {
					playPageIntoSound();
					Intent intent = new Intent(getActivity(),
							LessonViewActivity.class);
					intent.putExtra("unit", unit);
					intent.putExtra("position", position);
					startActivity(intent);
				}
			}
		}
	};

	@Override
	public boolean isRight() {
		// TODO Auto-generated method stub
		return false;
	}

}
