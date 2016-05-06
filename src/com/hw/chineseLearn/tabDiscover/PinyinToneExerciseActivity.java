package com.hw.chineseLearn.tabDiscover;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.hw.chineseLearn.dao.bean.TbMyPinyinTone;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.hw.chineseLearn.model.PinyinToneLessonExerciseModel;
import com.hw.chineseLearn.tabLearn.LessonResultActivity;
import com.util.tool.HttpHelper;
import com.util.tool.MediaPlayUtil;
import com.util.tool.MediaPlayerHelper;
import com.util.weight.CustomDialog;

/**
 * 拼音发音课程练习页面
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class PinyinToneExerciseActivity extends BaseActivity {

	private String TAG = "==PinyinToneExerciseActivity==";
	public Context context;
	private LinearLayout lin_pander_life;
	private LinearLayout lin_lesson_progress;
	private Button btn_continue;
	View contentView;
	/**
	 * 当前题目下标
	 */
	int exerciseIndex = 0;// 第一道题
	/**
	 * 总题数
	 */
	int exerciseCount = 0;

	/**
	 * 生命值
	 */
	int panderLife = 0;

	// 一个自定义的布局，作为显示的内容
	View checkView = null;
	CustomDialog builder;
	TbMyPinyinTone tbMyPinyinTone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_pytone_exercise, null);
		setContentView(contentView);
		context = this;
		initBudle();// 初始化数据
		CustomApplication.app.addActivity(this);
		init();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 初始化bundle数据
	 */
	@SuppressWarnings("unchecked")
	private void initBudle() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("model")) {
				tbMyPinyinTone = (TbMyPinyinTone) bundle
						.getSerializable("model");
			}
		}
	}

	public void playWrongSound() {
		new MediaPlayerHelper("sounds/wrong_sound.mp3").play();
	}

	public void playRightSound() {
		new MediaPlayerHelper("sounds/correct_sound.mp3").play();
	}

	// 存放panderView的集合
	HashMap<Integer, ImageView> panderView = new HashMap<Integer, ImageView>();

	// 存放progressView的集合
	HashMap<Integer, ImageView> progressView = new HashMap<Integer, ImageView>();

	/**
	 * 当前测试的题答案是否正确
	 */
	private boolean isCurrentTestRight;

	/**
	 * 0错误，1正确
	 */
	int status = 0;

	/**
	 * 正确的个数
	 */
	int rightCount = 0;

	/**
	 * 错误个数
	 */
	int wrongCount = 0;

	private ArrayList<PinyinToneLessonExerciseModel> lessonModels = new ArrayList<PinyinToneLessonExerciseModel>();

	String filePath, voicePath;

	ImageView btn_play;

	/**
	 * 初始化
	 */
	public void init() {
		checkView = LayoutInflater.from(context).inflate(
				R.layout.layout_learn_exercise_check_dialog, null);
		btn_continue = (Button) findViewById(R.id.btn_continue);
		btn_continue.setOnClickListener(onClickListener);

		btn_play = (ImageView) findViewById(R.id.btn_play);
		btn_play.setOnClickListener(onClickListener);
		initDatas();
	}

	private void initDatas() {
		if (builder != null) {
			builder.dismiss();
			builder = null;
		}

		rightCount = 0;
		wrongCount = 0;

		lin_pander_life = (LinearLayout) findViewById(R.id.lin_pander_life);
		lin_lesson_progress = (LinearLayout) findViewById(R.id.lin_lesson_progress);
		exerciseIndex = 0;// 第一道题

		panderLife = 2;
		panderView.clear();
		lin_pander_life.removeAllViews();
		for (int i = 0; i < panderLife; i++) {
			ImageView imageView = new ImageView(context);
			LayoutParams layoutParams = new LayoutParams(60, 60);
			layoutParams.setMargins(5, 5, 5, 5);
			imageView.setLayoutParams(layoutParams);
			imageView.setBackground(context.getResources().getDrawable(
					R.drawable.panda_sit));
			imageView.setId(i);
			lin_pander_life.addView(imageView);
			panderView.put(i, imageView);
		}
		if (lessonModels != null) {
			lessonModels = tbMyPinyinTone.getLessonModels();
			exerciseCount = lessonModels.size();
		} else {
			Log.e(TAG, "lessonModels==null");
		}

		progressView.clear();
		lin_lesson_progress.removeAllViews();
		// 加上十几个背景块
		for (int i = 0; i < exerciseCount; i++) {
			ImageView imageView = new ImageView(context);
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, 37, 1);
			layoutParams.setMargins(0, 5, 5, 5);
			imageView.setLayoutParams(layoutParams);
			imageView.setBackground(context.getResources().getDrawable(
					R.drawable.bg_progress_noraml));
			lin_lesson_progress.addView(imageView);
			progressView.put(i, imageView);
		}
		setData(0);
	}

	/**
	 * 播放声音
	 */
	private void play() {
		MediaPlayUtil.getInstance().play(filePath);
	}

	/**
	 * 设置要显示的数据
	 * 
	 * @param index
	 */
	private void setData(int index) {
		if (lessonModels != null) {
			PinyinToneLessonExerciseModel model = lessonModels.get(index);
			if (model != null) {
				String cn = model.getCn();
				String en = model.getEn();
				String py = model.getPy();
				if (!"".equals(py)) {
					String pinList[] = py.split(" ");
					if (pinList != null) {
						for (int i = 0; i < pinList.length; i++) {
						}
					}
				}
				voicePath = model.getVoicePath();
				filePath = DatabaseHelperMy.LESSON_SOUND_PATH + "/" + voicePath;
				File file = new File(filePath);
				if (!file.exists()) {
					// 下载并播放
					HttpHelper.downLoadLessonVoices(voicePath, true);
				} else {
					play();
				}

			} else {
				Log.e(TAG, "setData()-" + index + "-model==null");
			}
		}
	}

	/**
	 * 设置进度块的背景色
	 * 
	 * @param index
	 * @param drawableId
	 */
	private void setProgressViewBg(int index, int drawableId) {

		for (Map.Entry<Integer, ImageView> entry : progressView.entrySet()) {
			int position = entry.getKey();
			ImageView imageView = entry.getValue();

			if (index != position) {
				continue;
			}
			imageView.setBackground(context.getResources().getDrawable(
					drawableId));

			switch (drawableId) {
			case R.drawable.bg_progress_rigth:

				break;
			case R.drawable.bg_progress_wrong:

				lin_pander_life.removeView(panderView.get(panderLife - 1));
				panderLife--;
				Log.d(TAG, "panderLife:" + panderLife);

				if (panderLife == 0) {// lose all pander
					Intent intent = new Intent(PinyinToneExerciseActivity.this,
							LessonResultActivity.class);
					intent.putExtra("loseAllPanders", "loseAllPanders");
					startActivityForResult(intent, 100);

				} else {
					// playWrongSound();
					// showCheckDialog(false);
				}
				break;
			default:
				break;

			}
			break;
		}
	}

	OnClickListener onClickListener = new OnClickListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.btn_continue:

				// isCurrentTestRight = baseFragment.isRight();
				// if (isCurrentTestRight) {
				// setProgressViewBg(exerciseIndex,// 设置进度快的背景
				// R.drawable.bg_progress_rigth);
				// showCheckDialog(true);// 弹出正确的对话框
				// playRightSound();// 播放正确的声音
				// } else {
				// setProgressViewBg(exerciseIndex,
				// R.drawable.bg_progress_wrong);
				// showCheckDialog(false);// 展示错误对话框
				// playWrongSound();// 播放错误音乐
				// }
				if (exerciseIndex == exerciseCount - 1) {// 最后一道题目

					Intent intent = new Intent(PinyinToneExerciseActivity.this,
							LessonResultActivity.class);
					intent.putExtra("loseAllPanders", "");
					startActivityForResult(intent, 100);
				}
				if (exerciseIndex < exerciseCount - 1) {
					exerciseIndex++;
				}

				break;

			case R.id.btn_play:
				File file = new File(filePath);
				if (!file.exists()) {
					// 下载并播放
					HttpHelper.downLoadLessonVoices(voicePath, true);
				} else {
					play();
				}
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
			showQuitDialog();
		}
		return super.dispatchKeyEvent(event);
	}

	AlertDialog mModifyDialog;

	/**
	 * 对话框
	 */
	private void showQuitDialog() {
		if (mModifyDialog == null) {
			mModifyDialog = new AlertDialog.Builder(context).create();
		}

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.layout_dialog_loginout, null);
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		Button ok = (Button) view.findViewById(R.id.commit_btn);
		Button cancel = (Button) view.findViewById(R.id.cancel_btn);

		title.setText("Quit?");
		content.setText("Are you sure you what to quit? You will lose all progress in this lesson");
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		ok.setText("Ok");
		cancel.setText("Cancel");
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				CustomApplication.app
						.finishActivity(PinyinToneExerciseActivity.this);
				mModifyDialog.dismiss();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mModifyDialog.dismiss();
			}
		});

		mModifyDialog.show();
		mModifyDialog.setContentView(view);
	}

	/**
	 * check的对话框
	 * 
	 * @param isRight
	 */
	private void showCheckDialog(boolean isRight) {
		int screenWidth = CustomApplication.app.displayMetrics.widthPixels;
		int width = screenWidth * 6 / 10;
		int height = width / 78 * 100;
		if (isRight) {
			status = 1;
			rightCount++;
			height = width / 95 * 100;
		} else {
			status = 0;
			wrongCount++;
			height = width / 78 * 100;
		}

		ImageView img_is_right = (ImageView) checkView
				.findViewById(R.id.img_is_right);

		LayoutParams layoutParams = new LayoutParams(width, height);
		img_is_right.setLayoutParams(layoutParams);
		Button btn_next = (Button) checkView.findViewById(R.id.btn_next);
		TextView tv_answer = (TextView) checkView.findViewById(R.id.tv_answer);

		TextView tv_tip = (TextView) checkView.findViewById(R.id.tv_tip);

		if (isRight) {
			img_is_right.setBackground(context.getResources().getDrawable(
					R.drawable.correct_graphic));
			tv_tip.setVisibility(View.VISIBLE);
			// btn_next.setTextColor(context.getResources().getColor(
			// R.color.chinese_skill_blue));
		} else {

			img_is_right.setBackground(context.getResources().getDrawable(
					R.drawable.incorrect_graphic));
			// btn_next.setTextColor(context.getResources().getColor(
			// R.color.chinese_skill_yellow));
			tv_tip.setVisibility(View.INVISIBLE);
		}

		if (exerciseIndex == exerciseCount - 1) {// 最后一道题目
			btn_next.setText("FINISH");
		} else {
			btn_next.setText("CONTINUE");
		}

		if (builder == null) {
			builder = new CustomDialog(this, R.style.my_dialog).create(
					checkView, false, 1f, 1f, 1);
		}
		builder.show();
		builder.setCancelable(false);
		builder.setOnKeyListener(onKeyListener);

		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (exerciseIndex == exerciseCount - 1) {// 最后一道题目

					Intent intent = new Intent(PinyinToneExerciseActivity.this,
							LessonResultActivity.class);
					intent.putExtra("loseAllPanders", "");
					startActivityForResult(intent, 100);
				}
				builder.dismiss();
				// learn表中regex第一位 对应View关系
				if (exerciseIndex < exerciseCount - 1) {
					exerciseIndex++;
				}
			}

		});

		builder.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
			}
		});

	}

	OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				showQuitDialog();
				return true;
			} else {
				return false;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		setResult(0);
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		switch (resultCode) {
		case 0:// redo
			init();
			break;
		case 1:// continue
			setResult(1);
			CustomApplication.app
					.finishActivity(PinyinToneExerciseActivity.class);
			break;

		default:
			break;
		}
		Log.d(TAG, "resultCode:" + resultCode);
	};

	/**
	 * continue是否可点击
	 */
	public void isCheckBtnActived(boolean isActived) {
		if (isActived) {
			btn_continue.setEnabled(true);
			btn_continue.setBackgroundColor(context.getResources().getColor(
					R.color.chinese_skill_blue));
			btn_continue.setTextColor(context.getResources().getColor(
					R.color.white));

		} else {
			btn_continue.setEnabled(false);
			btn_continue.setBackgroundColor(context.getResources().getColor(
					R.color.min_grey));
			btn_continue.setTextColor(context.getResources().getColor(
					R.color.chinese_skill_blue));
		}
	}

}
