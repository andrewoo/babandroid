package com.hw.chineseLearn.tabLearn;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
public class LessonResultActivity extends BaseActivity {

	private String TAG = "==LessonResultActivity==";
	public Context context;
	private Resources resources;
	private int width;
	private int height;
	View contentView;

	private RoundProgressBar mRoundProgressBar;
	private RelativeLayout rel_1_2_1;
	private TextView tv_right_count;
	private RelativeLayout rel_1_2_2;
	private TextView tv_wrong_count;

	private TextView tv_time_count;
	private TextView tv_accuracy_percent;

	private TextView btn_redo;
	private TextView btn_continue;
	private int progress = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_result, null);
		setContentView(contentView);
		context = this;
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 5;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 3;
		resources = context.getResources();
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {
		mRoundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);

		LayoutParams lp = new LayoutParams(width, height);
		mRoundProgressBar.setLayoutParams(lp);

		rel_1_2_1 = (RelativeLayout) contentView
				.findViewById(R.id.rel_1_2_right);
		rel_1_2_1.setOnClickListener(onClickListener);
		tv_right_count = (TextView) contentView
				.findViewById(R.id.tv_right_count);
		rel_1_2_2 = (RelativeLayout) contentView
				.findViewById(R.id.rel_1_2_wrong);
		rel_1_2_2.setOnClickListener(onClickListener);
		tv_wrong_count = (TextView) contentView
				.findViewById(R.id.tv_wrong_count);

		tv_time_count = (TextView) contentView.findViewById(R.id.tv_time_count);
		tv_accuracy_percent = (TextView) contentView
				.findViewById(R.id.tv_accuracy_percent);

		btn_redo = (TextView) contentView.findViewById(R.id.btn_redo);
		btn_redo.setOnClickListener(onClickListener);

		btn_continue = (TextView) contentView.findViewById(R.id.btn_continue);
		btn_continue.setOnClickListener(onClickListener);

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (progress <= 100) {
					progress += 3;
					System.out.println(progress);
					mRoundProgressBar.setProgress(progress);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.rel_1_2_right://

				break;
			case R.id.rel_1_2_wrong://

				break;

			case R.id.btn_redo:

				CustomApplication.app.finishActivity(LessonResultActivity.this);
				setResult(0);
				break;

			case R.id.btn_continue:

				// startActivity(new Intent(LessonResultActivity.this,
				// LessonExerciseActivity.class));
				CustomApplication.app.finishActivity(LessonResultActivity.this);
				CustomApplication.app
						.finishActivity(LessonExerciseActivity.class);
				break;

			default:
				break;
			}
		}
	};

}
