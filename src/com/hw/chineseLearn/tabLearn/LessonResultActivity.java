package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbLessonMaterialStatus;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;
import com.hw.chineseLearn.dao.bean.Unit;
import com.hw.chineseLearn.tabDiscover.FluentAddLessonActivity;
import com.hw.chineseLearn.tabDiscover.FluentListActivity;
import com.util.tool.DateUtil;
import com.util.tool.UiUtil;
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

	private RoundProgressBar mRoundProgressBar, roundProgressBar_accuracy;
	private TextView tv_score;
	private TextView tv_right_count;
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
	private TextView tv_lose_all;
	int characterCount = 0, wordsCount = 0, sentenceCount = 0;
	int rightCount = 0, wrongCount = 0;
	private int lessonId;// 确定当前lesson
	int screenWidth = 0;
	int screenHeigth = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		// 到此界面说明题做完 解锁下个lesson或unit
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
			if (bundle.containsKey("LessonId")) {
				lessonId = bundle.getInt("LessonId");
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
		unLockLesson();
		CustomApplication.app.addActivity(this);
		screenWidth = CustomApplication.app.displayMetrics.widthPixels;
		screenHeigth = CustomApplication.app.displayMetrics.heightPixels;
		width = screenWidth / 10 * 5;
		height = width;
		resources = context.getResources();
		init();
	}

	/**
	 * 解锁下个lesson或unit
	 * 
	 */
	private void unLockLesson() {
		// 知道当前lessonid 得到下个lessonid 更新表 确定数据
		List<Unit> unitList = CustomApplication.app.unitList;
		// 遍历unit表找到对应lessonid 并解锁
		for (int i = 0; i < unitList.size(); i++) {
			String lessonList = unitList.get(i).getLessonList();
			if (lessonList.endsWith(";")) {
				lessonList = lessonList.substring(0, lessonList.length() - 1);
			}
			String[] lessonIdAarray = unitList.get(i).getLessonList()
					.split(";");// 所有的lessonid
			for (int j = 0; j < lessonIdAarray.length; j++) {
				if ("".equals(lessonIdAarray[j])) {
					continue;
				}
				int id = Integer.valueOf(lessonIdAarray[j]);
				if (id == lessonId) {// 找到对应id后 把后边一个lessonid存入表 解锁
					int idLast = Integer
							.valueOf(lessonIdAarray[lessonIdAarray.length - 1]);// 最后一个
					TbLessonMaterialStatus msCurrent = new TbLessonMaterialStatus();
					msCurrent.setLessonId(lessonId);
					msCurrent.setStatus(2);// 把当前表的lessonid对应状态改为2
					try {
						MyDao.getDaoMy(TbLessonMaterialStatus.class).update(
								msCurrent);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (id == idLast) {// 如果是最后一个就解锁下个unit的第一个lesson
						if (i < unitList.size() - 1) {
							String firstId = unitList.get(i + 1)
									.getLessonList().split(";")[0];// 下一个unit的第一个lessonid
							TbLessonMaterialStatus materialStatus = new TbLessonMaterialStatus();
							materialStatus
									.setLessonId(Integer.valueOf(firstId));
							materialStatus.setStatus(1);
							try {
								MyDao.getDaoMy(TbLessonMaterialStatus.class)
										.createOrUpdate(materialStatus);// 更新status
																		// 解锁
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					} else {// 不是最后一个就解锁下个lesson
						TbLessonMaterialStatus materialStatus = new TbLessonMaterialStatus();
						try {
							materialStatus.setLessonId(Integer
									.valueOf(lessonIdAarray[j + 1]));
							materialStatus.setStatus(1);
							MyDao.getDaoMy(TbLessonMaterialStatus.class)
									.createOrUpdate(materialStatus);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

				}
			}
		}

	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {

		if ("".equals(loseAllPanders)) {

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,
					height);
			mRoundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
			mRoundProgressBar.setLayoutParams(lp);
			mRoundProgressBar.setMax(progressCount);
			progressAdd = progressCount / 100;
			mRoundProgressBar.setAccurally(progressAdd);
			mRoundProgressBar.isDrawText(true);
			mRoundProgressBar.bringToFront();
			tv_score = (TextView) contentView.findViewById(R.id.tv_score);
			tv_score.setText("" + score);

			tv_right_count = (TextView) contentView
					.findViewById(R.id.tv_right_count);

			tv_wrong_count = (TextView) contentView
					.findViewById(R.id.tv_wrong_count);

			tv_time_count = (TextView) contentView
					.findViewById(R.id.tv_time_count);

			tv_time_count.setText(DateUtil.getStringfromInt(secondCount));

			tv_accuracy_percent = (TextView) contentView
					.findViewById(R.id.tv_accuracy_percent);

			ImageView img_right = (ImageView) contentView
					.findViewById(R.id.img_right);
			ImageView img_wrong = (ImageView) contentView
					.findViewById(R.id.img_wrong);

			roundProgressBar_accuracy = (RoundProgressBar) findViewById(R.id.roundProgressBar_accuracy);
			roundProgressBar_accuracy.setMax(progressCount);
			roundProgressBar_accuracy.setAccurally(progressAdd);
			roundProgressBar_accuracy.isDrawText(false);
			LayoutParams layoutParams = new LayoutParams(screenWidth * 1 / 5,
					screenWidth * 1 / 5);
			img_right.setLayoutParams(layoutParams);
			img_wrong.setLayoutParams(layoutParams);
			roundProgressBar_accuracy.setLayoutParams(layoutParams);

			new Thread(new Runnable() {

				@Override
				public void run() {
					while (progress <= progressCount) {
						progress += progressAdd * 2;
						mRoundProgressBar.setProgress(progress);
						roundProgressBar_accuracy.setProgress(progress);
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
			// tv_lose_all = (TextView) findViewById(R.id.tv_lose_all);
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

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			setResult(1);
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
}
