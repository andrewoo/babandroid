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
	private String loseAllPanders = "";

	private ImageView tv_lose_all;
	private ImageView img_lose_all;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("loseAllPanders")) {
				loseAllPanders = bundle.getString("loseAllPanders");
			}
		}

		if ("".equals(loseAllPanders)) {
			contentView = LayoutInflater.from(this).inflate(
					R.layout.activity_lesson_result, null);
		} else {
			contentView = LayoutInflater.from(this).inflate(
					R.layout.activity_lesson_result_lose, null);
		}

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
	public void init() {
		LayoutParams lp = new LayoutParams(width, height);

		if ("".equals(loseAllPanders)) {

			mRoundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);

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

			tv_time_count = (TextView) contentView
					.findViewById(R.id.tv_time_count);
			tv_accuracy_percent = (TextView) contentView
					.findViewById(R.id.tv_accuracy_percent);

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

		} else {
			tv_lose_all = (ImageView) findViewById(R.id.tv_lose_all);
			img_lose_all = (ImageView) findViewById(R.id.img_lose_all);
			tv_lose_all.setLayoutParams(lp);
			img_lose_all.setLayoutParams(lp);
		}

		btn_redo = (TextView) contentView.findViewById(R.id.btn_redo);
		btn_redo.setOnClickListener(onClickListener);

		btn_continue = (TextView) contentView.findViewById(R.id.btn_continue);
		btn_continue.setOnClickListener(onClickListener);
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
				setResult(0);
				CustomApplication.app.finishActivity(LessonResultActivity.this);
				break;

			case R.id.btn_continue:

				setResult(1);
				CustomApplication.app.finishActivity(LessonResultActivity.this);

				break;

			default:
				break;
			}
		}
	};

}
