package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;

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
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;
import com.util.tool.DateUtil;
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
	private TextView tv_score;
	private RelativeLayout rel_1_2_1;
	private TextView tv_right_count;
	private RelativeLayout rel_1_2_2;
	private TextView tv_wrong_count;

	private TextView tv_time_count;
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
	@SuppressWarnings("unchecked")
	public void init() {
		LayoutParams lp = new LayoutParams(width, height);

		if ("".equals(loseAllPanders)) {

			mRoundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
			mRoundProgressBar.setLayoutParams(lp);
			mRoundProgressBar.setMax(progressCount);
			progressAdd = progressCount / 100;
			mRoundProgressBar.setAccurally(progressAdd);
			tv_score = (TextView) contentView.findViewById(R.id.tv_score);
			tv_score.setText("" + score);

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

			tv_time_count.setText(DateUtil.getStringfromInt(secondCount));

			tv_accuracy_percent = (TextView) contentView
					.findViewById(R.id.tv_accuracy_percent);

			new Thread(new Runnable() {

				@Override
				public void run() {
					while (progress <= progressCount) {
						progress += progressAdd;
						mRoundProgressBar.setProgress(progress);
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}).start();

			tv_result = (TextView) contentView.findViewById(R.id.tv_result);
			tv_result.setText("Congratulations!You defeated " + progressCount
					+ "%babbelApp learns!");

			try {
				ArrayList<TbMyCharacter> tbMyCharacterList = (ArrayList<TbMyCharacter>) MyDao
						.getDaoMy(TbMyCharacter.class).queryForAll();

				ArrayList<TbMyWord> tbMyWordList = (ArrayList<TbMyWord>) MyDao
						.getDaoMy(TbMyWord.class).queryForAll();

				ArrayList<TbMySentence> tbMySentenceList = (ArrayList<TbMySentence>) MyDao
						.getDaoMy(TbMySentence.class).queryForAll();

				characterCount = tbMyCharacterList.size();
				wordsCount = tbMyWordList.size();
				sentenceCount = tbMySentenceList.size();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				tv_right_count.setText("" + rightCount);
				tv_wrong_count.setText("" + wrongCount);
				tv_accuracy_percent.setText(progressCount + "%");
			}

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
