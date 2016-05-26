package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;

/**
 * 课程复习页面
 * 
 * @author yh
 */
public class LessonReViewActivity extends BaseActivity {

	private String TAG = "==LessonReViewActivity==";
	public Context context;
	private Resources resources;
	private int width;
	private int height;
	View contentView;
	ImageView img_panda;

	private LinearLayout lin_character;
	private LinearLayout lin_words;
	private LinearLayout lin_sentence;

	private TextView tv_character_count;
	private TextView tv_words_count;
	private TextView tv_sentence_count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_review, null);
		setContentView(contentView);
		context = this;
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 5;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 5;
		resources = context.getResources();
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {

		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Lesson Review", View.GONE, View.GONE, 0);

		img_panda = (ImageView) contentView.findViewById(R.id.img_panda);
		LayoutParams layoutParams = (LayoutParams) img_panda.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = height;
		img_panda.setLayoutParams(layoutParams);
		// img_panda.setImageDrawable(getResources().getDrawable(
		// R.drawable.review_panda));

		lin_character = (LinearLayout) findViewById(R.id.lin_character);
		lin_words = (LinearLayout) findViewById(R.id.lin_words);
		lin_sentence = (LinearLayout) findViewById(R.id.lin_sentence);

		tv_character_count = (TextView) findViewById(R.id.tv_character_count);
		tv_words_count = (TextView) findViewById(R.id.tv_words_count);
		tv_sentence_count = (TextView) findViewById(R.id.tv_sentence_count);

		lin_character.setOnClickListener(onClickListener);
		lin_words.setOnClickListener(onClickListener);
		lin_sentence.setOnClickListener(onClickListener);

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
		iv_title_right.setOnClickListener(onClickListener);

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回
				CustomApplication.app.finishActivity(LessonReViewActivity.this);
				break;
			case R.id.lin_character:
				// startActivity(new Intent(LessonReViewActivity.this,
				// LessonFlashCardActivity.class));
				break;

			case R.id.lin_words:
				// startActivity(new Intent(LessonReViewActivity.this,
				// LessonFlashCardActivity.class));
				break;

			case R.id.lin_sentence:
				// startActivity(new Intent(LessonReViewActivity.this,
				// LessonFlashCardActivity.class));
				break;

			default:
				break;
			}
		}
	};

}
