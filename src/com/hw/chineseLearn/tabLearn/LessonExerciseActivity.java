package com.hw.chineseLearn.tabLearn;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
import com.hw.chineseLearn.dao.bean.LGModelWord;
import com.hw.chineseLearn.dao.bean.LGModelWord.SubLGModel;
import com.hw.chineseLearn.dao.bean.LGModel_Sentence_010;
import com.hw.chineseLearn.dao.bean.LGModel_Word_010;
import com.hw.chineseLearn.dao.bean.LGModel_Word_020;
import com.hw.chineseLearn.dao.bean.LGModel_Word_060;
import com.hw.chineseLearn.dao.bean.LGSentence;
import com.hw.chineseLearn.dao.bean.LGWord;
import com.hw.chineseLearn.dao.bean.LessonRepeatRegex;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;
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
	List<SubLGModel> subLGModeList;
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
	private int lessonId;

	private List<Integer> isFirstList = new ArrayList<Integer>();// 第一次出现保存在此集合

	private Timer timer = new Timer();
	private int secondCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_exercise, null);
		setContentView(contentView);

		initBudle();// 初始化数据
		context = this;
		CustomApplication.app.addActivity(this);
		init();
		initMediaPlayer();
		playRightSound();
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					secondCount++;
					Log.d(TAG, "secondCount:" + secondCount);
				}
			});
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		timer.schedule(task, 0, 1000);
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
			if (bundle.containsKey("LessonId")) {
				lessonId = bundle.getInt("LessonId");
			}
		}
		int randomSubject = 0;
		// 给每个Subject随机赋值确定查哪张表
		for (int i = 0; i < regexes.size(); i++) {
			int lgTable = regexes.get(i).getLgTable();
			if (lgTable == 0) {
				LGWord lgWord = null;
				try {
					lgWord = (LGWord) MyDao.getDao(LGWord.class).queryForId(
							regexes.get(i).getLgTableId());
				} catch (SQLException e) {
				}
				int length = lgWord.getWord().length();
				if (length > 1) {// 长度大于一才可以到第6个表
					randomSubject = new Random().nextInt(6) + 1;
				} else {
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
	 * 0错误，1正确
	 */
	int status = 0;

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
					if (timer != null) {
						timer.cancel();
					}
					if (task != null) {
						task.cancel();
					}

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

				if (modelWord.getCharId() != 0) {
					int charId = modelWord.getCharId();
					TbMyCharacter tbMyCharacter = new TbMyCharacter();
					tbMyCharacter.setCharId(charId);
					tbMyCharacter.setLessonId(lessonId);
					tbMyCharacter.setStatus(status);
					int a = 0;
					try {
						a = MyDao.getDaoMy(TbMyCharacter.class).create(
								tbMyCharacter);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Log.d(TAG, "a:" + a);
				}
				if (modelWord.getSentenceId() != 0) {
					int sentenceId = modelWord.getSentenceId();
					TbMySentence tbMySentence = new TbMySentence();
					tbMySentence.setSentenceId(sentenceId);
					tbMySentence.setLessonId(lessonId);
					tbMySentence.setStatus(status);
					int b = 0;
					try {
						b = MyDao.getDaoMy(TbMySentence.class).create(
								tbMySentence);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d(TAG, "b:" + b);
				}
				if (modelWord.getWordId() != 0) {
					int wordId = modelWord.getWordId();
					TbMyWord tbMyWord = new TbMyWord();
					tbMyWord.setWordId(wordId);
					tbMyWord.setLessonId(lessonId);
					tbMyWord.setStatus(status);
					int c = 0;
					try {
						c = MyDao.getDaoMy(TbMyWord.class).create(tbMyWord);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d(TAG, "c:" + c);
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

		if (isRight) {
			status = 1;
		}
		status = 0;
		Button btn_report_bug = (Button) checkView
				.findViewById(R.id.btn_report_bug);
		ImageView img_is_right = (ImageView) checkView
				.findViewById(R.id.img_is_right);
		Button btn_next = (Button) checkView.findViewById(R.id.btn_next);
		TextView tv_answer = (TextView) checkView.findViewById(R.id.tv_answer);
		tv_answer.setText(modelWord.getAnswerText());

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
				if (exerciseIndex < exerciseCount - 1) {
					exerciseIndex++;
					regexToView(regexes.get(exerciseIndex));
				}

				if (exerciseIndex == exerciseCount - 1) {// 最后一道题目

					Intent intent = new Intent(LessonExerciseActivity.this,
							LessonResultActivity.class);
					intent.putExtra("loseAllPanders", "");
					intent.putExtra("secondCount", secondCount);
					intent.putExtra("score", score);
					startActivityForResult(intent, 100);
					if (timer != null) {
						timer.cancel();
						timer = null;
					}
					if (task != null) {
						task.cancel();
						task = null;
					}
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
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		switch (resultCode) {
		case 0:// redo
				// init();
			exerciseIndex = 0;
			regexToView(regexes.get(exerciseIndex));// 重新循环 还是会崩
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
	private String question;
	private LGModelWord modelWord;

	/**
	 * learn表中regex对应表关系
	 */
	private void regexToView(LessonRepeatRegex lessonRepeatRegex) {
		this.lessonRepeatRegex = lessonRepeatRegex;
		int lgTable = lessonRepeatRegex.getLgTable();
		if (lgTable == 0) {
			int randomSubject = lessonRepeatRegex.getRandomSubject();
			if (isFirst(lessonRepeatRegex.getLgTableId())) {// 判断 如果是第一次出现的ID
															// 就从word010表中查询
				randomSubject = 1;
			}
			switch (randomSubject) {
			case 1:// 对应选择图片题 题目中文
				parseSetData1(lessonRepeatRegex);// 解析数据库数据，并把对应题传过去
				replaceTo2("imageSelectFragment");
				break;
			case 2:// 单选中问题 无图片
				parseSetData2(lessonRepeatRegex);
				replaceTo2("wordSelectFragment");
				break;
			case 3:// tag英文 暂时不 优化
				sentenceMoveFragment = new LearnSentenceMoveFragment();
				baseFragment = sentenceMoveFragment;
				Bundle bundle3 = new Bundle();
				bundle3.putSerializable("lessonRepeatRegex", lessonRepeatRegex);
				sentenceMoveFragment.setArguments(bundle3);// 把题传过去
				replaceTo2("sentenceMoveFragment");
				break;
			case 4:// 单词翻译输入英语 暂不优化
				wordInputFragment = new LearnWordInputFragment();
				baseFragment = wordInputFragment;
				Bundle bundle4 = new Bundle();
				bundle4.putSerializable("lessonRepeatRegex", lessonRepeatRegex);
				wordInputFragment.setArguments(bundle4);// 把题传过去
				replaceTo2("wordInputFragment");
				break;
			case 5:
				parseSetData2(lessonRepeatRegex);
				replaceTo2("wordSelectFragment");// 题目中文 选项英文
				break;
			case 6:
				parseSetData6(lessonRepeatRegex);
				replaceTo2("wordSelectFragment");// 题目中文 选项英文
			}
		} else if (lgTable == 1) {
			int randomSubject = regexes.get(exerciseIndex).getRandomSubject();
			switch (1) {
			case 1:
				parseSentenseData1(lessonRepeatRegex);
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

	private void parseSentenseData1(LessonRepeatRegex lessonRepeatRegex) {
		// lessonRepeatRegex.getid 查询sentence010表id得到选项和答案
		// 查询lgsentence表得到title
		modelWord = new LGModelWord();
		try {
			int lgTableId = lessonRepeatRegex.getLgTableId();
			LGModel_Sentence_010 sentence010 = (LGModel_Sentence_010) MyDao
					.getDao(LGModel_Sentence_010.class).queryBuilder().where()
					.eq("SentenceId", lgTableId).queryForFirst();
			LGSentence lgSentence = (LGSentence) MyDao.getDao(LGSentence.class)
					.queryForId(lgTableId);
			modelWord.setSentenceId(lgTableId);
			modelWord.setLessonId(lessonId);// 拿到lessonId
			modelWord.setTitle(lgSentence.getSentence());
			modelWord.setAnswer(sentence010.getAnswer());
			List<SubLGModel> subLGModelList = modelWord.getSubLGModelList();
			int answer = sentence010.getAnswer();
			String[] options = sentence010.getOptions().split(";");
			for (int i = 0; i < options.length; i++) {
				String[] option = options[i].split("=");
				SubLGModel subLGModel = modelWord.new SubLGModel();
				Integer id = Integer.valueOf(option[0]);
				subLGModel.setWordId(id);
				subLGModel.setOption(option[1]);// 拿到每个选项
				if (id == answer) {
					modelWord.setAnswerText(option[1]);// 拿到答案文本
				}
				subLGModelList.add(subLGModel);
			}
			Collections.shuffle(subLGModelList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wordSelectFragment = new LearnWordSelectFragment();
		baseFragment = wordSelectFragment;
		Bundle bundle2 = new Bundle();
		bundle2.putSerializable("modelWorld", modelWord);
		wordSelectFragment.setArguments(bundle2);// 把题传过去
	}

	/**
	 * word060 数据的解析和传递
	 */
	private void parseSetData6(LessonRepeatRegex lessonRepeatRegex) {
		modelWord = new LGModelWord();
		int lgTableId = lessonRepeatRegex.getLgTableId();
		try {
			LGModel_Word_060 word_060 = (LGModel_Word_060) MyDao
					.getDao(LGModel_Word_060.class).queryBuilder().where()
					.eq("WordId", lgTableId).queryForFirst();
			modelWord.setWordId(lgTableId);// 拿到wordid
			int answer = word_060.getAnswer();
			modelWord.setAnswer(answer);// 拿到答案
			modelWord.setLessonId(lessonId);// 拿到lessonId
			List<SubLGModel> subLGModelList2 = modelWord.getSubLGModelList();
			for (SubLGModel subLGModel : subLGModelList2) {
				if (subLGModel.getWordId() == answer) {
					modelWord.setAnswerText(subLGModel.getOption());
				}
			}
			LGWord lgWord1 = (LGWord) MyDao.getDao(LGWord.class).queryForId(
					lessonRepeatRegex.getLgTableId());
			question = "_____ " + lgWord1.getWord().substring(1);
			modelWord.setTitle(question);// 拿到title
			String[] splitWordId = word_060.getOptions().split(";");
			List<SubLGModel> subLGModelList = modelWord.getSubLGModelList();
			for (int i = 0; i < splitWordId.length; i++) {
				SubLGModel subLGModel = modelWord.new SubLGModel();
				String[] option = splitWordId[i].split("=");
				int id = Integer.valueOf(option[0]);
				subLGModel.setWordId(id);// 拿到判断对错使用的ID
				subLGModel.setOption(option[1].replace("-", "/"));// 拿到选项
				subLGModelList.add(subLGModel);// 拿到4个选项
			}
			Collections.shuffle(subLGModelList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		wordSelectFragment = new LearnWordSelectFragment();
		baseFragment = wordSelectFragment;
		Bundle bundle6 = new Bundle();
		bundle6.putSerializable("modelWorld", modelWord);
		wordSelectFragment.setArguments(bundle6);// 把题传过去
	}

	/**
	 * word020 数据的解析和传递
	 * 
	 * @param lessonRepeatRegex
	 */
	private void parseSetData2(LessonRepeatRegex lessonRepeatRegex) {
		modelWord = new LGModelWord();// 中转每道题
		int lgTableId = lessonRepeatRegex.getLgTableId();
		try {
			LGModel_Word_020 word_020 = (LGModel_Word_020) MyDao
					.getDao(LGModel_Word_020.class).queryBuilder().where()
					.eq("WordId", lgTableId).queryForFirst();
			modelWord.setWordId(lgTableId);
			int answer = word_020.getAnswer();
			modelWord.setAnswer(answer);// 拿到答案
			modelWord.setLessonId(lessonId);// 拿到lessonId
			LGWord lgWordAnswer = (LGWord) MyDao.getDao(LGWord.class)
					.queryForId(answer);
			String left = lgWordAnswer.getTranslations();
			String right = lgWordAnswer.getWord() + "/"
					+ lgWordAnswer.getPinyin();
			modelWord.setAnswerText(left + "=" + right);// 拿到答案文本
			LGWord lgWord1 = (LGWord) MyDao.getDao(LGWord.class).queryForId(
					lessonRepeatRegex.getLgTableId());
			question = "Select" + "\"" + lgWord1.getTranslations() + "\"";
			modelWord.setTitle(question);// 拿到title
			String[] splitWordId = word_020.getOptions().split(";");
			List<SubLGModel> subLGModelList = modelWord.getSubLGModelList();
			for (int i = 0; i < splitWordId.length; i++) {
				LGWord lgWord = (LGWord) MyDao.getDao(LGWord.class).queryForId(
						Integer.valueOf(splitWordId[i]));
				SubLGModel subLGModel = modelWord.new SubLGModel();
				String option = lgWord.getWord() + "/" + lgWord.getPinyin();
				subLGModel.setWordId(Integer.valueOf(splitWordId[i]));// 拿到wordid
				subLGModel.setOption(option);// 拿到选项
				subLGModelList.add(subLGModel);// 拿到4个选项
			}
			Collections.shuffle(subLGModelList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		wordSelectFragment = new LearnWordSelectFragment();
		baseFragment = wordSelectFragment;
		Bundle bundle2 = new Bundle();
		bundle2.putSerializable("modelWorld", modelWord);
		wordSelectFragment.setArguments(bundle2);// 把题传过去
	}

	/**
	 * word010 数据的解析和传递
	 * 
	 * @param lessonRepeatRegex
	 */
	private void parseSetData1(LessonRepeatRegex lessonRepeatRegex) {
		modelWord = new LGModelWord();// 中转每道题
		int lgTableId = lessonRepeatRegex.getLgTableId();
		try {
			LGWord lgWord = (LGWord) MyDao.getDao(LGWord.class).queryForId(
					lgTableId);
			LGModel_Word_010 Word_010 = (LGModel_Word_010) MyDao
					.getDao(LGModel_Word_010.class).queryBuilder().where()
					.eq("WordId", lgWord.getWordId()).queryForFirst();

			modelWord.setWordId(lgTableId);// 拿到wordId
			String title = lgWord.getTranslations();
			modelWord.setTitle("Select " + "\"" + title + "\"");// 拿到title
			modelWord.setLessonId(lessonId);// 拿到lessonId
			int answer = Word_010.getAnswer();
			modelWord.setAnswer(answer);// 拿到答案
			LGWord lgWordAnswer = (LGWord) MyDao.getDao(LGWord.class)
					.queryForId(answer);
			String left = lgWordAnswer.getTranslations();
			String right = lgWordAnswer.getWord() + "/"
					+ lgWordAnswer.getPinyin();
			modelWord.setAnswerText(left + "=" + right);// 拿到答案文本
			String imageOptions = Word_010.getImageOptions();
			String[] splitOptions = imageOptions.split(";");// 得到4个选项
			subLGModeList = modelWord.getSubLGModelList();
			for (int i = 0; i < splitOptions.length; i++) {
				LGWord lGWord = (LGWord) MyDao.getDao(LGWord.class).queryForId(
						splitOptions[i]);
				String mainPicName = lGWord.getWordId() + "-"
						+ lGWord.getMainPic();// 把数据库名字和文件中图片名字对应
				String optionName = lGWord.getWord() + "/" + lGWord.getPinyin();
				// 拿到所有图片和选项的名字
				SubLGModel subLGModel = modelWord.new SubLGModel();
				subLGModel.setImageName(mainPicName);// 拿到图片名字
				subLGModel.setOption(optionName);// 拿到选项文字
				subLGModel.setWordId(Integer.valueOf(splitOptions[i]));// 拿到对应id
				subLGModeList.add(subLGModel);
			}
			Collections.shuffle(subLGModeList);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		imageSelectFragment = new LearnImageSelectFragment();
		baseFragment = imageSelectFragment;
		Bundle bundle = new Bundle();
		bundle.putSerializable("modelWorld", modelWord);
		imageSelectFragment.setArguments(bundle);// 把题传过去
	}

	/**
	 * 判断随机ID是否是第一次出现
	 * 
	 * @return
	 */
	private boolean isFirst(int id) {
		if (isFirstList.contains(id)) {
			return false;
		} else {
			isFirstList.add(id);
			return true;
		}
	}

}
