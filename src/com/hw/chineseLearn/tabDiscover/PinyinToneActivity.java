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

		for (int i = 0; i < 67; i++) {
			LearnUnitBaseModel modelBase = new LearnUnitBaseModel();

			if (i == 0) {
				modelBase.setEnable(true);
				modelBase.setIconResSuffix("pinyintone_" + (i + 1));
			} else if (i == 1) {
				modelBase.setEnable(false);
				modelBase.setIconResSuffix("pinyintone_l" + (i + 1));
			} else {
				modelBase.setEnable(false);
				modelBase.setIconResSuffix("pinyintone_" + (i + 1));
			}

			listBase.add(modelBase);
		}

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
				if (isEnable) {
					startActivity(new Intent(PinyinToneActivity.this,
							LessonViewActivity.class));
				}
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
