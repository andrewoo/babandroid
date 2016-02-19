package com.hw.chineseLearn.tabLearn;

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
import com.hw.chineseLearn.tabMe.MineFragment;
import com.util.weight.CustomDialog;

/**
 * 课程练习页面
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LessonExerciseActivity extends BaseActivity {

	private String TAG = "==LessonExerciseActivity==";
	public Context context;
	private LinearLayout lin_pander_life;
	private TextView txt_lesson_score;
	private LinearLayout lin_lesson_progress;
	private Button btn_check;
	View contentView;
	/**
	 * 成绩
	 */
	int score = 0;
	/**
	 * 当前课程下标
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
	LearnImageMoveFragment imageMoveFragment;
	LearnSentenceMoveFragment sentenceMoveFragment;

	LearnImageSelectFragment imageSelectFragment;
	LearnWordSelectFragment wordSelectFragment;
	LearnWordInputFragment wordInputFragment;

	Button btn_next;
	Button btn_report_bug;
	// 一个自定义的布局，作为显示的内容
	View pview = null;
	CustomDialog builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_exercise, null);
		setContentView(contentView);
		context = this;
		pview = LayoutInflater.from(context).inflate(
				R.layout.layout_learn_exercise_check_dialog, null);

		init();
		CustomApplication.app.addActivity(this);

		if (imageSelectFragment == null) {
			imageSelectFragment = new LearnImageSelectFragment();
			navigateToNoAnimWithId(imageSelectFragment, R.id.container1);
		}

		if (wordSelectFragment == null) {
			wordSelectFragment = new LearnWordSelectFragment();
		}
		if (wordInputFragment == null) {
			wordInputFragment = new LearnWordInputFragment();
		}
		if (imageMoveFragment == null) {
			imageMoveFragment = new LearnImageMoveFragment();
		}
		if (sentenceMoveFragment == null) {
			sentenceMoveFragment = new LearnSentenceMoveFragment();
		}

	}

	// 存放progressView的集合
	HashMap<Integer, ImageView> panderView = new HashMap<Integer, ImageView>();

	// 存放progressView的集合
	HashMap<Integer, ImageView> progressView = new HashMap<Integer, ImageView>();

	/**
	 * 初始化
	 */
	public void init() {
		lin_pander_life = (LinearLayout) findViewById(R.id.lin_pander_life);
		txt_lesson_score = (TextView) findViewById(R.id.txt_lesson_score);

		lin_lesson_progress = (LinearLayout) findViewById(R.id.lin_lesson_progress);
		btn_check = (Button) findViewById(R.id.btn_check);
		btn_check.setOnClickListener(onClickListener);
		panderLife = 5;
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

		// exerciseCount=list.size();
		exerciseCount = 17;
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
		// setProgressViewBg(0, R.drawable.bg_progress_rigth);
		// setProgressViewBg(1, R.drawable.bg_progress_rigth);
		// setProgressViewBg(2, R.drawable.bg_progress_rigth);
		// setProgressViewBg(3, R.drawable.bg_progress_wrong);
		// setProgressViewBg(4, R.drawable.bg_progress_rigth);
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
			if (index == position) {
				imageView.setBackground(context.getResources().getDrawable(
						drawableId));

				switch (drawableId) {
				case R.drawable.bg_progress_rigth:
					score = score + 60;
					txt_lesson_score.setText("" + score);
					break;
				case R.drawable.bg_progress_wrong:
					score = score - 60;
					if (score < 0)
						score = 0;
					txt_lesson_score.setText("" + score);
					if (panderView.size() > 1) {
						lin_pander_life.removeView(panderView.get(panderView
								.size() - 1));
					}

					break;
				default:
					break;

				}
				break;
			} else {

			}
		}
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.btn_check:

				if (btn_check.getVisibility() == View.VISIBLE) {
					btn_check.setVisibility(View.GONE);
				}
				setProgressViewBg(exerciseIndex, R.drawable.bg_progress_rigth);
				showCheckDialog();

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
						.finishActivity(LessonExerciseActivity.this);
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

	private void closeWindowSoftInput() {
		super.closeWindowSoftInput(contentView);
	}

	private void showCheckDialog() {
		// 题目下标加一
		exerciseIndex++;
		Log.d(TAG, "题目下标:" + exerciseIndex);

		btn_report_bug = (Button) pview.findViewById(R.id.btn_report_bug);
		btn_next = (Button) pview.findViewById(R.id.btn_next);

		if (exerciseIndex == exerciseCount) {// 最后一道题目
			btn_next.setText("Finish");
		} else {
			btn_next.setText("Next");
		}

		if (builder == null) {
			builder = new CustomDialog(this, R.style.my_dialog).create(pview,
					false, 1f, 1f, 1);
		}
		builder.show();
		builder.setCancelable(false);
		builder.setOnKeyListener(onKeyListener);
		btn_report_bug.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//
			}
		});

		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_check.setVisibility(View.VISIBLE);
				switch (exerciseIndex) {
				case 0:
					replaceTo1(wordSelectFragment);
					break;
				case 1:
					replaceTo1(wordInputFragment);
					break;
				case 2:
					replaceTo1(imageSelectFragment);
					break;
				case 3:
					replaceTo1(imageMoveFragment);
					break;
				case 4:
					replaceTo1(sentenceMoveFragment);
					break;

				default:
					break;
				}

				if (exerciseIndex == exerciseCount) {// 最后一道题目
					startActivityForResult(new Intent(
							LessonExerciseActivity.this,
							LessonResultActivity.class), 100);
				}

				builder.dismiss();
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

	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		switch (resultCode) {
		case 0:
			exerciseIndex = 0;
			break;

		default:
			break;
		}

	};

}
