package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
import com.hw.chineseLearn.dao.bean.Lesson;
import com.hw.chineseLearn.dao.bean.LessonRepeatRegex;
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
	private String rules;
	ArrayList<LessonRepeatRegex> regexes ;

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
				//点击后调用方法 传递过去
				regexes =  (ArrayList<LessonRepeatRegex>) getRepeatRegexBeanList();
				Intent intent = new Intent(LessonTestOutActivity.this,
						LessonTestOutTestActivity.class);
				intent.putExtra("regexes", regexes);
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
		updateDb();
		if (mModifyDialog == null) {
			mModifyDialog = new AlertDialog.Builder(context).create();
		}

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.layout_dialog_congratulation,
				null);
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		Button ok = (Button) view.findViewById(R.id.commit_btn);

		title.setText("Congratulations");
		content.setText("You have successfully passed the TestOut!");
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		ok.setText("Ok");
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				CustomApplication.app
						.finishActivity(LessonTestOutActivity.this);
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
					String lessonIdStr = aa[j];// 只更新lessonList中的第一节
					int lessonId = Integer.parseInt(lessonIdStr);

					try {
						TbLessonMaterialStatus model = (TbLessonMaterialStatus) MyDao
								.getDaoMy(TbLessonMaterialStatus.class)
								.queryForId(lessonId);

						if (model == null) {// 没有查询到数据,创建

							TbLessonMaterialStatus modelNew = new TbLessonMaterialStatus();
							modelNew.setLessonId(lessonId);

							if (j == 0) {
								modelNew.setStatus(1);
							} else {
								modelNew.setStatus(0);
							}
							MyDao.getDaoMy(TbLessonMaterialStatus.class)
									.createOrUpdate(modelNew);
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}
	private List<LessonRepeatRegex> getRepeatRegexBeanList() {

		rules="2:6;1;1#1:1237;2;1#0:1228;2;1#0:69;4;1#1:1386;4;1#0:122-0:117;5;2#1:248;5;1#2:46;6;1#0:200;6;1#0:77;7;1#1:135;8;1#2:18;9;1#1:155-1:1294;14;2#1:1398;15;1#0:107;55;1#0:311;57;1#";
		
		String[] questions = rules.split("#");
		List<LessonRepeatRegex> regexes = new ArrayList<LessonRepeatRegex>();
		List<LessonRepeatRegex> subRegexes = new ArrayList<LessonRepeatRegex>();
		for (int i = 0; i < questions.length; i++) {// 遍历把值加入到实体beanList
			LessonRepeatRegex regex = new LessonRepeatRegex();
			String[] splitFenHao = questions[i].split(";");
			if (splitFenHao[0].indexOf("-") != -1) {
				String[] splitLgTableId = splitFenHao[0].split("-");
				for (int j = 0; j < Integer
						.valueOf(splitFenHao[splitFenHao.length - 1]); j++) {
					if (j == 0) {
						regex.setLgTable(Integer.valueOf(splitLgTableId[j]
								.split(":")[0]));
						regex.setLgTableId(Integer.valueOf(splitLgTableId[j]
								.split(":")[1]));
						regex.setCount(Integer
								.valueOf(splitFenHao[splitFenHao.length - 1]));
						subRegexes.add(regex);
					} else {
						LessonRepeatRegex regex1 = new LessonRepeatRegex();
						regex1.setLgTable(Integer.valueOf(splitLgTableId[j]
								.split(":")[0]));
						regex1.setLgTableId(Integer.valueOf(splitLgTableId[j]
								.split(":")[1]));
						regex1.setCount(Integer
								.valueOf(splitFenHao[splitFenHao.length - 1]));
						subRegexes.add(new Random().nextInt(subRegexes.size()),
								regex1);
					}
				}
				Collections.shuffle(subRegexes);// 打乱顺序
				regexes.addAll(subRegexes);
				subRegexes.clear();
				continue;
			}
			String[] splitMaoHao = splitFenHao[0].split(":");
			regex.setLgTable(Integer.valueOf(splitMaoHao[0]));// -分割后：前的数字
			regex.setLgTableId(Integer.valueOf(splitMaoHao[1]));// -分割后：后的数字
			regex.setCount(Integer.valueOf(splitFenHao[splitFenHao.length - 1]));// 得到最后一个字符
			regexes.add(regex);
		}
		return regexes;
	
	}
}
