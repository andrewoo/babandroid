package com.hw.chineseLearn.tabDiscover;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;

/**
 * pinyinTone结果页面
 * 
 * @author yh
 */
public class PinyinToneResultActivity extends BaseActivity {

	private String TAG = "==PinyinToneResultActivity==";
	public Context context;
	private Resources resources;
	private int screenWidth;
	private int screenHeight;
	View contentView;
	/**
	 * 正确率
	 */
	private TextView tv_accuracy_percent;
	private TextView tv_result;

	private TextView btn_redo;
	private TextView btn_continue;
	private float progress = 0.0f;
	private float progressAdd = 0.0f;
	private float progressCount;
	private String loseAllPanders = "";
	private int secondCount;
	private int score;
	private int exerciseCount;
	private TextView tv_lose_all;
	private ImageView iv_is_failed;
	int characterCount = 0, wordsCount = 0, sentenceCount = 0;
	int rightCount = 0, wrongCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("loseAllPanders")) {
				loseAllPanders = bundle.getString("loseAllPanders");
			}
			if (bundle.containsKey("secondCount")) {
				secondCount = bundle.getInt("secondCount");
			}
			if (bundle.containsKey("score")) {
				score = bundle.getInt("score");
			}
			if (bundle.containsKey("exerciseCount")) {
				exerciseCount = bundle.getInt("exerciseCount");
			}
			if (bundle.containsKey("rightCount")) {
				rightCount = bundle.getInt("rightCount");
			}
			if (bundle.containsKey("wrongCount")) {
				wrongCount = bundle.getInt("wrongCount");
			}
			if (exerciseCount != 0) {
				progressCount = ((float) rightCount / (float) exerciseCount) * 100;
				progressCount = (float) (Math.round(progressCount * 100)) / 100;// 保留小数点后两位
			}

		}

		Log.d(TAG, "loseAllPanders:" + loseAllPanders);

		if ("".equals(loseAllPanders)) {
			contentView = LayoutInflater.from(this).inflate(
					R.layout.activity_lesson_review_result, null);
		} else {
			contentView = LayoutInflater.from(this).inflate(
					R.layout.activity_lesson_result_lose, null);
		}

		setContentView(contentView);

		CustomApplication.app.addActivity(this);
		screenWidth = CustomApplication.app.displayMetrics.widthPixels;
		screenHeight = CustomApplication.app.displayMetrics.heightPixels;
		resources = context.getResources();
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {

		tv_lose_all = (TextView) findViewById(R.id.tv_lose_all);
		iv_is_failed = (ImageView) findViewById(R.id.iv_is_failed);
		int width = screenWidth * 6 / 10;
		int height = width / 92 * 100;
		LayoutParams layoutParams = new LayoutParams(width, height);
		iv_is_failed.setLayoutParams(layoutParams);

		btn_redo = (TextView) contentView.findViewById(R.id.btn_redo);
		btn_redo.setVisibility(View.GONE);
		btn_redo.setOnClickListener(onClickListener);

		btn_continue = (TextView) contentView.findViewById(R.id.btn_continue);
		btn_continue.setOnClickListener(onClickListener);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.btn_redo:
				setResult(0);
				CustomApplication.app
						.finishActivity(PinyinToneResultActivity.this);
				break;

			case R.id.btn_continue:

				// 解锁下一节
				setResult(1);
				CustomApplication.app
						.finishActivity(PinyinToneResultActivity.this);

				break;

			default:
				break;
			}
		}
	};

}
