package com.hw.chineseLearn.tabLearn;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.LGWord;
import com.hw.chineseLearn.dao.bean.LessonRepeatRegex;
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
	BaseFragment baseFragment;
	LearnImageMoveFragment imageMoveFragment;
	LearnSentenceMoveFragment sentenceMoveFragment;
	LearnImageSelectFragment imageSelectFragment;
	LearnWordSelectFragment wordSelectFragment;
	LearnWordInputFragment wordInputFragment;

	// 一个自定义的布局，作为显示的内容
	View checkView = null;
	CustomDialog builder;
	private int utilId = 0;

	private List<LessonRepeatRegex> regexes;

	private List<Integer> isFirstList = new ArrayList<Integer>();// 第一次出现保存在此集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_exercise, null);
		setContentView(contentView);

		initBudle();// 初始化数据

		// for test
		// if (utilId == 0) {
		// isCurrentTestRight = true;
		// } else {
		// isCurrentTestRight = false;
		// }
		context = this;
		CustomApplication.app.addActivity(this);
		init();
		initMediaPlayer();
		playRightSound();
	}

	/**
	 * 初始化bundle数据
	 */
	private void initBudle() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("utilId")) {
				utilId = bundle.getInt("utilId");
			}
			if (bundle.containsKey("regexes")) {
				regexes = (List<LessonRepeatRegex>) bundle
						.getSerializable("regexes");
			}
		}
		int randomSubject = 0;
		// 给每个Subject随机赋值确定查哪张表
		for (int i = 0; i < regexes.size(); i++) {
			int lgTable = regexes.get(i).getLgTable();
			if (lgTable == 0) {
				LGWord lgWord = null;
				try {
					lgWord = (LGWord)MyDao.getDao(LGWord.class).queryForId(regexes.get(i).getLgTableId());} catch (SQLException e) {
				}
				int length = lgWord.getWord().length();
				if(length>1){//长度大于一才可以到第6个表
					randomSubject = new Random().nextInt(6) + 1;
				}else{
					randomSubject = new Random().nextInt(5) + 1;
				}
				regexes.get(i).setRandomSubject(randomSubject);
			} else if (lgTable == 1) {
				randomSubject = new Random().nextInt(5) + 1;
				regexes.get(i).setRandomSubject(randomSubject);
			} else if (lgTable == 2) {
				regexes.get(i).setRandomSubject(1);
			}
		}
	}

	MediaPlayer mediaPlayer = null;

	private void initMediaPlayer() {

		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
	}

	public void playWrongSound() {
		AssetManager am = getAssets();// 获得该应用的AssetManager
		try {
			AssetFileDescriptor afd = am.openFd("sounds/wrong_sound.mp3");
			mediaPlayer.setDataSource(afd.getFileDescriptor());
			mediaPlayer.prepare(); // 准备
			mediaPlayer.start();
			return;
		} catch (IOException localIOException1) {
		}
	}

	public void playRightSound() {
		AssetManager am = getAssets();// 获得该应用的AssetManager
		try {
			AssetFileDescriptor afd = am.openFd("sounds/correct_sound.mp3");
			mediaPlayer.setDataSource(afd.getFileDescriptor());
			mediaPlayer.prepare(); // 准备
			mediaPlayer.start();
			return;
		} catch (IOException localIOException1) {
		}
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
	 * 初始化
	 */
	public void init() {
		checkView = LayoutInflater.from(context).inflate(
				R.layout.layout_learn_exercise_check_dialog, null);

		btn_check = (Button) findViewById(R.id.btn_check);
		btn_check.setOnClickListener(onClickListener);
		initTestDatas();
	}

	private void initTestDatas() {
		lin_pander_life = (LinearLayout) findViewById(R.id.lin_pander_life);
		txt_lesson_score = (TextView) findViewById(R.id.txt_lesson_score);
		lin_lesson_progress = (LinearLayout) findViewById(R.id.lin_lesson_progress);

		score = 0;
		txt_lesson_score.setText("" + score);
		exerciseIndex = 0;// 第一道题

		panderLife = 5;
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

		// exerciseCount=list.size();
		exerciseCount = regexes.size();
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
		regexToView(regexes.get(exerciseIndex));// 第一次进入时
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
				score = score + 60;
				txt_lesson_score.setText("" + score);
				if (index == exerciseCount - 1) {
					// UiUtil.showToast(context, "last test");
				}
				// playRightSound();
				// showCheckDialog(true);
				break;
			case R.drawable.bg_progress_wrong:
				score = score - 60;
				if (score < 0)
					score = 0;
				txt_lesson_score.setText("" + score);

				lin_pander_life.removeView(panderView.get(panderLife - 1));
				panderLife--;
				Log.d(TAG, "panderLife:" + panderLife);

				if (panderLife == 0) {// lose all pander
					Intent intent = new Intent(LessonExerciseActivity.this,
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

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.btn_check:

				if (btn_check.getVisibility() == View.VISIBLE) {
					btn_check.setVisibility(View.GONE);
				}
				isCurrentTestRight = baseFragment.isRight();
				if (isCurrentTestRight) {
					setProgressViewBg(exerciseIndex,// 设置进度快的背景
							R.drawable.bg_progress_rigth);
					showCheckDialog(true);
				} else {
					setProgressViewBg(exerciseIndex,
							R.drawable.bg_progress_wrong);
					showCheckDialog(false);
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

	RelativeLayout rel_op;

	/**
	 * check的对话框
	 * 
	 * @param isRight
	 */
	private void showCheckDialog(boolean isRight) {

		Button btn_report_bug = (Button) checkView
				.findViewById(R.id.btn_report_bug);
		ImageView img_is_right = (ImageView) checkView
				.findViewById(R.id.img_is_right);
		Button btn_next = (Button) checkView.findViewById(R.id.btn_next);
		TextView tv_answer = (TextView) checkView.findViewById(R.id.tv_answer);
		String answer = MyDao.getAnswer(lessonRepeatRegex);// 得到答案后赋值
		tv_answer.setText(answer);
		rel_op = (RelativeLayout) checkView.findViewById(R.id.rel_op);
		rel_op.setOnTouchListener(onTouchListener);
		LinearLayout lin_color_bg = (LinearLayout) checkView
				.findViewById(R.id.lin_color_bg);
		TextView tv_tip = (TextView) checkView.findViewById(R.id.tv_tip);

		if (isRight) {
			btn_report_bug.setBackground(context.getResources().getDrawable(
					R.drawable.bugreport_img_right));
			img_is_right.setBackground(context.getResources().getDrawable(
					R.drawable.test_correct_3));
			lin_color_bg.setBackgroundColor(context.getResources().getColor(
					R.color.chinese_skill_blue));
			tv_tip.setVisibility(View.VISIBLE);
			btn_next.setTextColor(context.getResources().getColor(
					R.color.chinese_skill_blue));
		} else {

			btn_report_bug.setBackground(context.getResources().getDrawable(
					R.drawable.bugreport_img_wrong));
			img_is_right.setBackground(context.getResources().getDrawable(
					R.drawable.test_wrong_3));
			lin_color_bg.setBackgroundColor(context.getResources().getColor(
					R.color.chinese_skill_yellow));
			btn_next.setTextColor(context.getResources().getColor(
					R.color.chinese_skill_yellow));
			tv_tip.setVisibility(View.INVISIBLE);
		}

		if (exerciseIndex == exerciseCount - 1) {// 最后一道题目
			btn_next.setText("Finish");
		} else {
			btn_next.setText("Next");
		}
		// 题目下标加一
		// exerciseIndex++;
		// Log.d(TAG, "题目下标:" + exerciseIndex);

		if (builder == null) {
			builder = new CustomDialog(this, R.style.my_dialog).create(
					checkView, false, 1f, 1f, 1);
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
				// learn表中regex第一位 对应View关系
				if (exerciseIndex < exerciseCount) {
					exerciseIndex++;
					regexToView(regexes.get(exerciseIndex));
				}

				if (exerciseIndex == exerciseCount) {// 最后一道题目

					Intent intent = new Intent(LessonExerciseActivity.this,
							LessonResultActivity.class);
					intent.putExtra("loseAllPanders", "");
					startActivityForResult(intent, 100);
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

	private void replaceTo2(String type) {
		if ("wordSelectFragment".equals(type)) {
			replaceTo1(wordSelectFragment);
		} else if ("wordInputFragment".equals(type)) {
			replaceTo1(wordInputFragment);
		} else if ("imageSelectFragment".equals(type)) {
			replaceTo1(imageSelectFragment);
		} else if ("imageMoveFragment".equals(type)) {
			replaceTo1(imageMoveFragment);
		} else if ("sentenceMoveFragment".equals(type)) {
			replaceTo1(sentenceMoveFragment);
		}
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
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
		}
		isFirstList.clear();
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		switch (resultCode) {
		case 0:// redo
			init();
			break;
		case 1:// continue
			setResult(1);
			CustomApplication.app.finishActivity(LessonExerciseActivity.class);
			break;

		default:
			break;
		}
		Log.d(TAG, "resultCode:" + resultCode);
	};

	private int startx;
	private int starty;

	OnTouchListener onTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			// 如果手指放在imageView上拖动
			case R.id.iv_dv_view:
				// event.getRawX(); //获取手指第一次接触屏幕在x方向的坐标
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
					int x = (int) event.getRawX();
					int y = (int) event.getRawY();

					// if (y < 400) {
					// // 设置TextView在窗体的下面
					// tv_drag_view.layout(tv_drag_view.getLeft(), 420,
					// tv_drag_view.getRight(), 440);
					// } else {
					// tv_drag_view.layout(tv_drag_view.getLeft(), 60,
					// tv_drag_view.getRight(), 80);
					// }

					// 获取手指移动的距离
					int dx = x - startx;
					int dy = y - starty;
					// 得到imageView最开始的各顶点的坐标
					int l = rel_op.getLeft();
					int r = rel_op.getRight();
					int t = rel_op.getTop();
					int b = rel_op.getBottom();

					int newl = l + dx;
					int newt = t + dy;
					int newr = r + dx;
					int newb = b + dy;
					// 更改imageView在窗体的位置
					rel_op.layout(newl, newt, newr, newb);

					// 获取移动后的位置
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();

					int screenWidth = CustomApplication.app.displayMetrics.widthPixels;
					int screenHeight = CustomApplication.app.displayMetrics.heightPixels;

					if ((newl < 20) || (newt < 20) || (newr > screenWidth / 2)
							|| (newb > screenHeight / 2)) {
						break;
					}

					break;
				case MotionEvent.ACTION_UP:// 手指离开屏幕对应事件
					// Log.i(TAG, "手指离开屏幕");
					// 记录最后view在窗体的位置
					int lasty = rel_op.getTop();
					int lastx = rel_op.getLeft();
					// Editor editor = sp.edit();
					// editor.putInt("lasty", lasty);
					// editor.putInt("lastx", lastx);
					// editor.commit();
					break;
				}
				break;

			}
			return true;// 不会中断触摸事件的返回
		}
	};
	private LessonRepeatRegex lessonRepeatRegex;

	/**
	 * learn表中regex对应表关系
	 */
	private void regexToView(LessonRepeatRegex lessonRepeatRegex) {
		this.lessonRepeatRegex = lessonRepeatRegex;
		int lgTable = lessonRepeatRegex.getLgTable();
		if (lgTable == 0) {
			int randomSubject = lessonRepeatRegex.getRandomSubject();
			if (isFirst(lessonRepeatRegex.getLgTableId())) {// 判断 如果是第一次出现的ID 就从word010表中查询
				randomSubject = 1;
			}
			switch (randomSubject) {
			case 1:// 对应选择图片题 题目中文
				imageSelectFragment = new LearnImageSelectFragment();
				baseFragment = imageSelectFragment;
				Bundle bundle = new Bundle();
				bundle.putSerializable("lessonRepeatRegex", lessonRepeatRegex);
				imageSelectFragment.setArguments(bundle);// 把题传过去
				replaceTo2("imageSelectFragment");
				break;
			case 2:// 单选中问题 无图片
				wordSelectFragment = new LearnWordSelectFragment();
				baseFragment = wordSelectFragment;
				Bundle bundle2 = new Bundle();
				bundle2.putSerializable("lessonRepeatRegex", lessonRepeatRegex);
				wordSelectFragment.setArguments(bundle2);// 把题传过去
				replaceTo2("wordSelectFragment");
				break;
			case 3:// tag英文
				sentenceMoveFragment = new LearnSentenceMoveFragment();
				baseFragment = sentenceMoveFragment;
				Bundle bundle3 = new Bundle();
				bundle3.putSerializable("lessonRepeatRegex", lessonRepeatRegex);
				sentenceMoveFragment.setArguments(bundle3);// 把题传过去
				replaceTo2("sentenceMoveFragment");
				break;
			case 4:// 单词翻译输入英语
				wordInputFragment = new LearnWordInputFragment();
				baseFragment = wordInputFragment;
				Bundle bundle4 = new Bundle();
				bundle4.putSerializable("lessonRepeatRegex", lessonRepeatRegex);
				wordInputFragment.setArguments(bundle4);// 把题传过去
				replaceTo2("wordInputFragment");
				break;
			case 5:
				wordSelectFragment = new LearnWordSelectFragment();
				baseFragment = wordSelectFragment;
				Bundle bundle5 = new Bundle();
				bundle5.putSerializable("lessonRepeatRegex", lessonRepeatRegex);
				wordSelectFragment.setArguments(bundle5);// 把题传过去
				replaceTo2("wordSelectFragment");// 题目中文 选项英文
				break;
			case 6:
				wordSelectFragment = new LearnWordSelectFragment();
				baseFragment = wordSelectFragment;
				Bundle bundle6 = new Bundle();
				bundle6.putSerializable("lessonRepeatRegex", lessonRepeatRegex);
				wordSelectFragment.setArguments(bundle6);// 把题传过去
				replaceTo2("wordSelectFragment");// 题目中文 选项英文
			}
		} else if (lgTable == 1) {
			switch (regexes.get(exerciseIndex).getRandomSubject()) {
			case 1:
				replaceTo2("wordSelectFragment");
				break;
			case 2:
				replaceTo2("wordInputFragment");
				break;
			case 3:
				replaceTo2("sentenceMoveFragment");
				break;
			case 4:
				replaceTo2("sentenceMoveFragment");
				break;
			case 5:
				replaceTo2("imageMoveFragment");
				break;
			}
		} else if (lgTable == 2) {
			imageMoveFragment = new LearnImageMoveFragment();
			baseFragment = imageMoveFragment;
			Bundle bundle = new Bundle();
			bundle.putSerializable("lessonRepeatRegex", lessonRepeatRegex);
			imageMoveFragment.setArguments(bundle);// 把题传过去
			replaceTo2("imageMoveFragment");
		}
	}

	/**
	 * 判断随机ID是否是第一次出现
	 * 
	 * @return
	 */
	private boolean isFirst(int id) {
		System.out.println("id--" + id);
		System.out.println("isFirstList--" + isFirstList);
		if (isFirstList.contains(id)) {
			return false;
		} else {
			isFirstList.add(id);
			return true;
		}
	}

}
