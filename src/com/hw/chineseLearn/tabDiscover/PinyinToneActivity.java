package com.hw.chineseLearn.tabDiscover;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.ToneAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.hw.chineseLearn.tabLearn.LessonViewActivity;
import com.util.weight.SelfGridView;

/**
 * 音调练习
 * 
 * @author yh
 */
public class PinyinToneActivity extends BaseActivity {

	private String TAG = "==PinyinToneActivity==";
	private Context context;
	View contentView;
	ToneAdapter adapter;
	SelfGridView centGridView;
	ArrayList<LearnUnitBaseModel> listBase = new ArrayList<LearnUnitBaseModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_pinyin_tone, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {
		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Pinyin Tone", View.GONE, View.GONE, 0);

		centGridView = (SelfGridView) contentView
				.findViewById(R.id.gv_tone_gridview);

		LearnUnitBaseModel modelBase1 = new LearnUnitBaseModel();
		modelBase1.setIconResSuffix("pinyintone_1");
		modelBase1.setUnitName("");
		modelBase1.setLessonList("");
		modelBase1.setEnable(true);
		listBase.add(modelBase1);

		LearnUnitBaseModel modelBase2 = new LearnUnitBaseModel();
		modelBase2.setIconResSuffix("pinyintone_l2");
		modelBase2.setUnitName("");
		modelBase2.setLessonList("");
		listBase.add(modelBase2);

		LearnUnitBaseModel modelBase3 = new LearnUnitBaseModel();
		modelBase3.setIconResSuffix("pinyintone_3");
		modelBase3.setUnitName("");
		modelBase3.setLessonList("");
		listBase.add(modelBase3);

		LearnUnitBaseModel modelBase4 = new LearnUnitBaseModel();
		modelBase4.setIconResSuffix("pinyintone_4");
		modelBase4.setUnitName("");
		modelBase4.setLessonList("");
		listBase.add(modelBase4);

		LearnUnitBaseModel modelBase5 = new LearnUnitBaseModel();
		modelBase5.setIconResSuffix("pinyintone_5");
		modelBase5.setUnitName("");
		modelBase5.setLessonList("");
		listBase.add(modelBase5);

		LearnUnitBaseModel modelBase6 = new LearnUnitBaseModel();
		modelBase6.setIconResSuffix("pinyintone_6");
		modelBase6.setUnitName("Food");
		modelBase6.setLessonList("");
		listBase.add(modelBase6);

		LearnUnitBaseModel modelBase7 = new LearnUnitBaseModel();
		modelBase7.setIconResSuffix("pinyintone_7");
		modelBase7.setUnitName("");
		modelBase7.setLessonList("");
		listBase.add(modelBase7);

		LearnUnitBaseModel modelBase8 = new LearnUnitBaseModel();
		modelBase8.setIconResSuffix("pinyintone_8");
		modelBase8.setUnitName("");
		modelBase8.setLessonList("");
		listBase.add(modelBase8);

		LearnUnitBaseModel modelBase9 = new LearnUnitBaseModel();
		modelBase9.setIconResSuffix("pinyintone_9");
		modelBase9.setUnitName("");
		modelBase9.setLessonList("");
		listBase.add(modelBase9);

		LearnUnitBaseModel modelBase10 = new LearnUnitBaseModel();
		modelBase10.setIconResSuffix("pinyintone_10");
		modelBase10.setUnitName("");
		modelBase10.setLessonList("");
		listBase.add(modelBase10);

		LearnUnitBaseModel modelBase11 = new LearnUnitBaseModel();
		modelBase11.setIconResSuffix("pinyintone_11");
		modelBase11.setUnitName("");
		modelBase11.setLessonList("");
		listBase.add(modelBase11);

		LearnUnitBaseModel modelBase12 = new LearnUnitBaseModel();
		modelBase12.setIconResSuffix("pinyintone_12");
		modelBase12.setUnitName("");
		modelBase12.setLessonList("");
		listBase.add(modelBase12);

		LearnUnitBaseModel modelBase13 = new LearnUnitBaseModel();
		modelBase13.setIconResSuffix("pinyintone_13");
		modelBase13.setUnitName("");
		modelBase13.setLessonList("");
		listBase.add(modelBase13);

		LearnUnitBaseModel modelBase14 = new LearnUnitBaseModel();
		modelBase14.setIconResSuffix("pinyintone_14");
		modelBase14.setUnitName("");
		modelBase14.setLessonList("");
		listBase.add(modelBase14);

		LearnUnitBaseModel modelBase15 = new LearnUnitBaseModel();
		modelBase15.setIconResSuffix("pinyintone_15");
		modelBase15.setUnitName("");
		modelBase15.setLessonList("");
		listBase.add(modelBase15);

		LearnUnitBaseModel modelBase16 = new LearnUnitBaseModel();
		modelBase16.setIconResSuffix("pinyintone_16");
		modelBase16.setUnitName("");
		modelBase16.setLessonList("");
		listBase.add(modelBase16);

		LearnUnitBaseModel modelBase17 = new LearnUnitBaseModel();
		modelBase17.setIconResSuffix("pinyintone_17");
		modelBase17.setUnitName("");
		modelBase17.setLessonList("");
		listBase.add(modelBase17);

		adapter = new ToneAdapter(context, listBase);
		centGridView.setAdapter(adapter);
		centGridView.setOnItemClickListener(itemClickListener);
		adapter.notifyDataSetChanged();
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app.finishActivity(PinyinToneActivity.this);
				break;

			default:
				break;
			}
		}
	};
	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			LearnUnitBaseModel learnUnitBaseModel = listBase.get(arg2);
			if (learnUnitBaseModel != null) {
				boolean isEnable = learnUnitBaseModel.isEnable();
				// if (isEnable) {
				startActivity(new Intent(PinyinToneActivity.this,
						LessonViewActivity.class));
				// }
			}
		}
	};

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
		Button tv_title = (Button) view_title.findViewById(R.id.btn_title);
		tv_title.setText(title);

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

}
