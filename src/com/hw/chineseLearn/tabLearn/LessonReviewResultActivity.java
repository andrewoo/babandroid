package com.hw.chineseLearn.tabLearn;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.util.weight.RoundProgressBar;

/**
 * 课程结果页面
 * 
 * @author yh
 */
public class LessonReviewResultActivity extends BaseActivity {

	private String TAG = "==LessonReviewResultActivity==";
	public Context context;
	private Resources resources;
	private int width;
	private int height;
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
	private ImageView tv_lose_all, img_lose_all;
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

		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_review_result, null);

		Log.d(TAG, "loseAllPanders:" + loseAllPanders);

		setContentView(contentView);

		CustomApplication.app.addActivity(this);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 5;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 3;
		resources = context.getResources();
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		LayoutParams lp = new LayoutParams(width, height);

		tv_lose_all = (ImageView) findViewById(R.id.tv_lose_all);
		img_lose_all = (ImageView) findViewById(R.id.img_lose_all);
		tv_lose_all.setLayoutParams(lp);
		img_lose_all.setLayoutParams(lp);

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
						.finishActivity(LessonReviewResultActivity.this);
				break;

			case R.id.btn_continue:

				setResult(1);
				CustomApplication.app
						.finishActivity(LessonReviewResultActivity.this);

				break;

			default:
				break;
			}
		}
	};

}
