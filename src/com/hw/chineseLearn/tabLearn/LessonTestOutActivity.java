package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.TestOutGalleryAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbLessonMaterialStatus;
import com.hw.chineseLearn.dao.bean.Unit;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.tool.UiUtil;
import com.util.weight.MyGallery;

/**
 * TestOut页面
 * 
 * @author yh
 */
public class LessonTestOutActivity extends BaseActivity implements
		OnItemSelectedListener, OnItemClickListener {

	private static String TAG = "==LessonTestOutActivity==";
	public Context context;
	private MyGallery gallery;// CoverFlow
	private LinearLayout lin_gallery_test_out;
	private TestOutGalleryAdapter adapter;
	private Button btn_test_now;
	View contentView;
	int width;
	int height;
	ArrayList<LearnUnitBaseModel> listBase = new ArrayList<LearnUnitBaseModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_test_out, null);
		setContentView(contentView);
		context = this;
		width = CustomApplication.app.displayMetrics.widthPixels;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 5;
		init();
		CustomApplication.app.addActivity(this);
		super.gestureDetector();

	}

	/**
	 * 初始化
	 */
	public void init() {

		setTitle(View.GONE, View.VISIBLE,
				R.drawable.btn_selector_top_left_white, "TestOut", View.GONE,
				View.GONE, 0);

		LearnUnitBaseModel modelBase1 = new LearnUnitBaseModel();
		modelBase1.setIconResSuffix("lu1_1_1");
		modelBase1.setUnitName("Basics1");
		listBase.add(modelBase1);

		LearnUnitBaseModel modelBase2 = new LearnUnitBaseModel();
		modelBase2.setIconResSuffix("lu1_1_2");
		modelBase2.setUnitName("Basics2");
		listBase.add(modelBase2);

		LearnUnitBaseModel modelBase3 = new LearnUnitBaseModel();
		modelBase3.setIconResSuffix("lu1_1_3");
		modelBase3.setUnitName("Basics3");
		listBase.add(modelBase3);

		LearnUnitBaseModel modelBase4 = new LearnUnitBaseModel();
		modelBase4.setIconResSuffix("lu1_1_4");
		modelBase4.setUnitName("Color");
		listBase.add(modelBase4);

		LearnUnitBaseModel modelBase5 = new LearnUnitBaseModel();
		modelBase5.setIconResSuffix("lu1_1_5");
		modelBase5.setUnitName("Number&Measure");
		listBase.add(modelBase5);

		LearnUnitBaseModel modelBase6 = new LearnUnitBaseModel();
		modelBase6.setIconResSuffix("lu1_1_6");
		modelBase6.setUnitName("Food");
		listBase.add(modelBase6);

		LearnUnitBaseModel modelBase7 = new LearnUnitBaseModel();
		modelBase7.setIconResSuffix("lu1_2_1");
		modelBase7.setUnitName("Shape");
		listBase.add(modelBase7);

		LearnUnitBaseModel modelBase8 = new LearnUnitBaseModel();
		modelBase8.setIconResSuffix("lu1_2_2");
		modelBase8.setUnitName("Nature");
		listBase.add(modelBase8);

		LearnUnitBaseModel modelBase9 = new LearnUnitBaseModel();
		modelBase9.setIconResSuffix("lu1_2_3");
		modelBase9.setUnitName("Negation");
		listBase.add(modelBase9);

		LearnUnitBaseModel modelBase10 = new LearnUnitBaseModel();
		modelBase10.setIconResSuffix("lu1_2_4");
		modelBase10.setUnitName("Question");
		listBase.add(modelBase10);

		LearnUnitBaseModel modelBase11 = new LearnUnitBaseModel();
		modelBase11.setIconResSuffix("lu1_3_1");
		modelBase11.setUnitName("Time");
		listBase.add(modelBase11);

		LearnUnitBaseModel modelBase12 = new LearnUnitBaseModel();
		modelBase12.setIconResSuffix("lu1_3_2");
		modelBase12.setUnitName("Tense");
		listBase.add(modelBase12);

		gallery = new MyGallery(this);
		LayoutParams ly = new Gallery.LayoutParams(width, height);
		gallery.setLayoutParams(ly);
		adapter = new TestOutGalleryAdapter(context, listBase);
		gallery.setAdapter(adapter);
		gallery.setAnimationDuration(1500);
		gallery.setSpacing(30);
		gallery.setOnItemSelectedListener(this);
		gallery.setOnItemClickListener(this);

		lin_gallery_test_out = (LinearLayout) findViewById(R.id.lin_gallery_test_out);
		lin_gallery_test_out.addView(gallery);

		btn_test_now = (Button) findViewById(R.id.btn_test_now);
		btn_test_now.setOnClickListener(onClickListener);
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

		View view_title = (View) this.findViewById(R.id.view_title);
		view_title.setBackgroundColor(context.getResources().getColor(
				R.color.chinese_skill_orange));
		Button tv_title = (Button) view_title.findViewById(R.id.btn_title);
		tv_title.setText(title);
		tv_title.setTextColor(context.getResources().getColor(R.color.white));
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

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回
				CustomApplication.app
						.finishActivity(LessonTestOutActivity.this);
				break;

			case R.id.btn_test_now://
				Intent intent = new Intent(LessonTestOutActivity.this,
						LessonTestOutTestActivity.class);
				startActivityForResult(intent, 0);
				break;

			default:
				break;
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		switch (resultCode) {
		case 1:// miss，点击了continue

			break;
		case 100:// pass,解锁所有的unit
			// 弹出dialog
			showPassedDialog();
			break;

		default:
			break;
		}
		Log.d(TAG, "resultCode:" + resultCode);
	};

	@Override
	public void onItemSelected(AdapterView<?> arg0, View convertView,
			int position, long arg3) {
		// TODO Auto-generated method stub
		adapter.setSelect(position);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View convertView,
			int position, long arg3) {
		// TODO Auto-generated method stub
	}

	AlertDialog mModifyDialog;

	/**
	 * 对话框
	 */
	private void showPassedDialog() {
		if (mModifyDialog == null) {
			mModifyDialog = new AlertDialog.Builder(context).create();
		}

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.layout_dialog_congratulation,
				null);
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		Button ok = (Button) view.findViewById(R.id.commit_btn);
		Button cancel = (Button) view.findViewById(R.id.cancel_btn);

		title.setText("Congratulations");
		content.setText("You have successfully passed the TestOut!");
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		ok.setText("Ok");
		cancel.setText("Cancel");
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				CustomApplication.app
						.finishActivity(LessonTestOutActivity.this);
				mModifyDialog.dismiss();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mModifyDialog.dismiss();
			}
		});

		mModifyDialog.show();
		mModifyDialog.setContentView(view);
	}

	/**
	 * 更新数据库，解锁所有的Unit
	 */
	@SuppressWarnings("unchecked")
	private void updateDb() {
		int unitListSize = CustomApplication.app.unitList.size();

		if (CustomApplication.app.unitList != null && unitListSize != 0) {
			for (int i = 0; i < unitListSize; i++) {
				Unit unit = CustomApplication.app.unitList.get(i);
				if (unit == null) {
					continue;
				}
				String lessonList = unit.getLessonList();
				String[] aa = UiUtil.getListFormString(lessonList);

				for (int j = 0; j < aa.length; j++) {
					String lessonIdStr = aa[0];// 只更新lessonList中的第一节
					try {
						TbLessonMaterialStatus model = (TbLessonMaterialStatus) MyDao
								.getDaoMy(TbLessonMaterialStatus.class)
								.queryForId(lessonIdStr);
						if (model == null) {// 没有查询到数据,创建

							TbLessonMaterialStatus modelNew = new TbLessonMaterialStatus();

							int lessonId = Integer.parseInt(lessonIdStr);
							modelNew.setLessonId(lessonId);
							modelNew.setStatus(1);
							MyDao.getDaoMy(TbLessonMaterialStatus.class)
									.create(modelNew);
						} else {// 更新数据
							model.setStatus(1);
							MyDao.getDaoMy(TbLessonMaterialStatus.class)
									.update(model);
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}
}
