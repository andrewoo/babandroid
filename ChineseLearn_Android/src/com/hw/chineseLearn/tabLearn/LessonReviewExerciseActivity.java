package com.hw.chineseLearn.tabLearn;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.LGCharacter;
import com.hw.chineseLearn.dao.bean.LGCharacterPart;
import com.hw.chineseLearn.dao.bean.LGModelWord;
import com.hw.chineseLearn.dao.bean.LGModelWord.SubLGModel;
import com.hw.chineseLearn.dao.bean.LGModel_Sentence_010;
import com.hw.chineseLearn.dao.bean.LGModel_Sentence_020;
import com.hw.chineseLearn.dao.bean.LGModel_Sentence_030;
import com.hw.chineseLearn.dao.bean.LGModel_Sentence_040;
import com.hw.chineseLearn.dao.bean.LGModel_Sentence_050;
import com.hw.chineseLearn.dao.bean.LGModel_Word_010;
import com.hw.chineseLearn.dao.bean.LGModel_Word_020;
import com.hw.chineseLearn.dao.bean.LGModel_Word_030;
import com.hw.chineseLearn.dao.bean.LGModel_Word_040;
import com.hw.chineseLearn.dao.bean.LGModel_Word_060;
import com.hw.chineseLearn.dao.bean.LGSentence;
import com.hw.chineseLearn.dao.bean.LGWord;
import com.hw.chineseLearn.dao.bean.LessonRepeatRegex;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;
import com.j256.ormlite.dao.Dao;
import com.util.tool.UiUtil;
import com.util.weight.CustomDialog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 课程复习练习页面
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LessonReviewExerciseActivity extends BaseActivity {

	private String TAG = "==LessonReviewExerciseActivity==";
	public Context context;
	private LinearLayout lin_pander_life;
	private TextView txt_lesson_score;
	private ImageView iv_quit;
	private LinearLayout lin_lesson_progress;
	private LinearLayout lin_lesson_score;
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

	private List<LessonRepeatRegex> regexes = new ArrayList<LessonRepeatRegex>();;
	private int lessonId;

	/**
	 * 传过来的是哪张表 0 word,1 sentence,2 character
	 */
	private int lgTable;

	private List<Integer> isFirstList = new ArrayList<Integer>();// 第一次出现保存在此集合

	private int secondCount = 0;

	private List<String> randomList = new ArrayList<String>();
	private List<String> partPicNameList = new ArrayList<String>();

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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	ArrayList<TbMyWord> tbMyWordList;
	ArrayList<TbMySentence> tbMySentenceList;
	ArrayList<TbMyCharacter> tbMyCharList;

	/**
	 * 初始化bundle数据
	 */
	@SuppressWarnings("unchecked")
	private void initBudle() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {

			if (bundle.containsKey("lgTable")) {
				lgTable = bundle.getInt("lgTable");
			}
		}
		regexes.clear();
		if (lgTable == 0) {// word
			try {
				// 数据集合 wordid
				tbMyWordList = (ArrayList<TbMyWord>) MyDao.getDaoMy(
						TbMyWord.class).queryForAll();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < tbMyWordList.size(); i++) {
				TbMyWord model = tbMyWordList.get(i);

				if (model == null) {
					continue;
				}
				LessonRepeatRegex lessonRepeatRegex = new LessonRepeatRegex();
				int wordId = model.getWordId();
				lessonId = model.getLessonId();

				Integer random = getRandom(wordId);
				lessonRepeatRegex.setLgTable(lgTable);
				lessonRepeatRegex.setLgTableId(wordId);
				lessonRepeatRegex.setRandomSubject(random);
				regexes.add(lessonRepeatRegex);

			}
		} else if (lgTable == 1) {// sentence
			try {
				// 数据集合
				tbMySentenceList = (ArrayList<TbMySentence>) MyDao.getDaoMy(
						TbMySentence.class).queryForAll();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < tbMySentenceList.size(); i++) {
				TbMySentence model = tbMySentenceList.get(i);
				if (model == null) {
					continue;
				}
				LessonRepeatRegex lessonRepeatRegex = new LessonRepeatRegex();
				int sentenceId = model.getSentenceId();
				lessonId = model.getLessonId();

				Integer random = getRandom(sentenceId);
				lessonRepeatRegex.setLgTable(lgTable);
				lessonRepeatRegex.setLgTableId(sentenceId);
				lessonRepeatRegex.setRandomSubject(random);
				regexes.add(lessonRepeatRegex);

			}

		} else if (lgTable == 2) {// character
			try {
				// 数据集合
				tbMyCharList = (ArrayList<TbMyCharacter>) MyDao.getDaoMy(
						TbMyCharacter.class).queryForAll();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < tbMyCharList.size(); i++) {
				TbMyCharacter model = tbMyCharList.get(i);
				if (model == null) {
					continue;
				}
				LessonRepeatRegex lessonRepeatRegex = new LessonRepeatRegex();
				int charId = model.getCharId();
				lessonId = model.getLessonId();

				Integer random = getRandom(charId);
				lessonRepeatRegex.setLgTable(lgTable);
				lessonRepeatRegex.setLgTableId(charId);
				lessonRepeatRegex.setRandomSubject(random);
				regexes.add(lessonRepeatRegex);
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

	/**
	 * 初始化
	 */
	public void init() {
		checkView = LayoutInflater.from(context).inflate(
				R.layout.layout_learn_exercise_check_dialog, null);

		btn_check = (Button) findViewById(R.id.btn_check);
		btn_check.setOnClickListener(onClickListener);
		iv_quit = (ImageView) findViewById(R.id.iv_quit);
		iv_quit.setOnClickListener(onClickListener);
		initTestDatas();
	}

	private void initTestDatas() {
		if (builder != null) {
			builder.dismiss();
			builder = null;
		}

		lin_pander_life = (LinearLayout) findViewById(R.id.lin_pander_life);
		lin_pander_life.setVisibility(View.GONE);
		txt_lesson_score = (TextView) findViewById(R.id.txt_lesson_score);
		lin_lesson_progress = (LinearLayout) findViewById(R.id.lin_lesson_progress);
		lin_lesson_score = (LinearLayout) findViewById(R.id.lin_lesson_score);
		lin_lesson_score.setVisibility(View.GONE);
		score = 0;
		txt_lesson_score.setText("" + score);
		exerciseIndex = 0;// 第一道题

		lin_pander_life.removeAllViews();
		exerciseCount = regexes.size();
		progressView.clear();
		lin_lesson_progress.removeAllViews();
		// 加上十几个背景块
		for (int i = 0; i < exerciseCount; i++) {
			ImageView imageView = new ImageView(context);
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, 20, 1);
			layoutParams.setMargins(0, 2, 2, 2);
			imageView.setLayoutParams(layoutParams);
			imageView.setBackground(context.getResources().getDrawable(
					R.drawable.bg_progress_noraml));
			lin_lesson_progress.addView(imageView);
			progressView.put(i, imageView);
		}
		if (regexes.size() != 0) {
			regexToView(regexes.get(exerciseIndex));// 第一次进入时
		} else {
			Log.e(TAG, "regexes.size() == 0");
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
				score = score + 60;
				txt_lesson_score.setText("" + score);
				if (index == exerciseCount - 1) {
					// UiUtil.showToast(context, "last test");
				}
				// playRightSound();
				// showCheckDialog(true);
				break;
			case R.drawable.bg_progress_wrong:
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
				case R.id.iv_quit:
					showQuitDialog();
					break;
			case R.id.btn_check:

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
					Log.d(TAG, "charId:" + charId);
					int c = 0;
					try {

						TbMyCharacter tbMyCharacterQuery = (TbMyCharacter) MyDao
								.getDaoMy(TbMyCharacter.class).queryForId(
										charId);
						if (tbMyCharacterQuery == null) {
							c = MyDao.getDaoMy(TbMyCharacter.class).create(
									tbMyCharacter);
						} else {
							c = MyDao.getDaoMy(TbMyCharacter.class).update(
									tbMyCharacter);
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Log.d(TAG, "c:" + c);
				}
				if (modelWord.getSentenceId() != 0) {
					int sentenceId = modelWord.getSentenceId();
					TbMySentence tbMySentence = new TbMySentence();
					tbMySentence.setSentenceId(sentenceId);
					tbMySentence.setLessonId(lessonId);
					tbMySentence.setStatus(status);
					Log.d(TAG, "sentenceId:" + sentenceId);
					int s = 0;
					try {

						TbMySentence tbMySentenceQuery = (TbMySentence) MyDao
								.getDaoMy(TbMySentence.class).queryForId(
										sentenceId);
						if (tbMySentenceQuery == null) {
							s = MyDao.getDaoMy(TbMySentence.class).create(
									tbMySentence);
						} else {
							s = MyDao.getDaoMy(TbMySentence.class).update(
									tbMySentence);
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d(TAG, "s:" + s);
				}
				if (modelWord.getWordId() != 0) {
					int wordId = modelWord.getWordId();
					TbMyWord tbMyWord = new TbMyWord();
					tbMyWord.setWordId(wordId);
					tbMyWord.setLessonId(lessonId);
					tbMyWord.setStatus(status);
					Log.d(TAG, "wordId:" + wordId);
					int w = 0;
					try {

						TbMyWord tbMyWordQuery = (TbMyWord) MyDao.getDaoMy(
								TbMyWord.class).queryForId(wordId);
						if (tbMyWordQuery == null) {
							w = MyDao.getDaoMy(TbMyWord.class).create(tbMyWord);
						} else {
							w = MyDao.getDaoMy(TbMyWord.class).update(tbMyWord);
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d(TAG, "w:" + w);
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

		title.setText(getString(R.string.dialog_quit_title));
		content.setText(getString(R.string.dialog_quit_text));
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		ok.setText(getString(R.string.dialog_quit_ok));
		cancel.setText(getString(R.string.dialog_quit_cancel));
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				CustomApplication.app
						.finishActivity(LessonReviewExerciseActivity.this);
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
		tv_answer.setText(modelWord.getAnswerText());

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
			btn_next.setText(getString(R.string.button_finish));
		} else {
			btn_next.setText(getString(R.string.button_continue));
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
				btn_check.setVisibility(View.VISIBLE);

				if (exerciseIndex == exerciseCount - 1) {// 最后一道题目

					Intent intent = new Intent(LessonReviewExerciseActivity.this,
							LessonReviewResultActivity.class);
					intent.putExtra("loseAllPanders", "");
					intent.putExtra("secondCount", secondCount);// 用时秒数
					intent.putExtra("score", score);// 得分
					intent.putExtra("exerciseCount", exerciseCount);// 总练习题目数
					intent.putExtra("rightCount", rightCount);// 正确个数
					intent.putExtra("wrongCount", wrongCount);// 错误个数
					intent.putExtra("LessonId", lessonId);// 传递lessonid确定哪个lesson做完
					startActivityForResult(intent, 100);
				}
				builder.dismiss();
				// learn表中regex第一位 对应View关系
				if (exerciseIndex < exerciseCount - 1) {
					exerciseIndex++;
					regexToView(regexes.get(exerciseIndex));
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
		case 1:// continue
			setResult(1);
			CustomApplication.app
					.finishActivity(LessonReviewExerciseActivity.class);
			break;

		default:
			break;
		}
		Log.d(TAG, "resultCode:" + resultCode);
	};

	private int startx;
	private int starty;

	private LessonRepeatRegex lessonRepeatRegex;
	private String question;
	private LGModelWord modelWord;

	/**
	 * learn表中regex对应表关系
	 */
	/**
	 * @param lessonRepeatRegex
	 */
	private void regexToView(LessonRepeatRegex lessonRepeatRegex) {
		isCheckBtnActived(false);
		this.lessonRepeatRegex = lessonRepeatRegex;
		int lgTable = lessonRepeatRegex.getLgTable();
		if (lgTable == 0) {// word
			int randomSubject = lessonRepeatRegex.getRandomSubject();
			if (isFirst(lessonRepeatRegex.getLgTableId())) {// 判断 如果是第一次出现的ID
															// 就从word010表中查询
				randomSubject = 1;
			}
			switch (randomSubject) {
			case 1:// 对应选择图片题 题目中文
				parseSetData1(lessonRepeatRegex);// 解析数据库数据，并把对应题传过去
				Log.i("answer", modelWord.getAnswerText());
				replaceTo2("imageSelectFragment");
				break;
			case 2:// 单选中问题 无图片
				parseSetData2(lessonRepeatRegex);
				Log.i("answer", modelWord.getAnswerText());
				replaceTo2("wordSelectFragment");
				break;
			case 3:// tag英文
				parseSetData3(lessonRepeatRegex);
				Log.i("answer", modelWord.getAnswerText());
				replaceTo2("sentenceMoveFragment");
				break;
			case 4:// 单词翻译输入英语
				parseSetData4(lessonRepeatRegex);
				Log.i("answer", modelWord.getAnswerText());
				replaceTo2("wordInputFragment");
				break;
			case 5:
				parseSetData2(lessonRepeatRegex);
				Log.i("answer", modelWord.getAnswerText());
				replaceTo2("wordSelectFragment");// 题目中文 选项英文
				break;
			case 6:
				parseSetData6(lessonRepeatRegex);
				replaceTo2("wordSelectFragment");// 题目中文 选项英文
			}
		} else if (lgTable == 1) {// sentence
			int randomSubject = regexes.get(exerciseIndex).getRandomSubject();
			switch (randomSubject) {
			case 1:
				parseSentenseData1(lessonRepeatRegex);
				Log.i("answer", modelWord.getAnswerText());
				replaceTo2("wordSelectFragment");
				break;
			case 2:
				parseSentenseData2(lessonRepeatRegex);
				Log.i("answer", modelWord.getAnswerText());
				replaceTo2("wordInputFragment");
				break;
			case 3:
				parseSentenseData3(lessonRepeatRegex);
				Log.i("answer", modelWord.getAnswerText());
				replaceTo2("wordSelectFragment");
				break;
			case 4:
				parseSentenseData4(lessonRepeatRegex);
				Log.i("answer", modelWord.getAnswerText());
				replaceTo2("sentenceMoveFragment");
				break;
			case 5:
				parseSentenseData5(lessonRepeatRegex);
				Log.i("answer", modelWord.getAnswerText());
				replaceTo2("sentenceMoveFragment");
				break;
			}
		} else if (lgTable == 2) {
			// 查询lgcharacid title= 得到 partoption和partanswer spit;
			// 查lgcharpart得到imagename
			modelWord = new LGModelWord();
			int lgTableId = lessonRepeatRegex.getLgTableId();
			LGCharacter lGCharacterPart = null;
			try {
				lGCharacterPart = (LGCharacter) MyDao.getDao(LGCharacter.class)
						.queryForId(lgTableId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String title = "[" + lGCharacterPart.getPinyin() + "]"
					+ lGCharacterPart.getTranslation().replace(";", "/");
			modelWord.setTitle(title);// 拿到title
			modelWord.setCharId(lgTableId);// 拿到CharId
			modelWord.setLessonId(lessonId);// 拿到lessonId

			String dirCode = lGCharacterPart.getDirCode();// 得到mp3文件名
			dirCode = "c-" + lgTableId + "-" + dirCode + ".mp3";
			modelWord.setVoicePath(dirCode);
			String[] picArray = UiUtil.getListFormString(lGCharacterPart
					.getPartOptions());// 所有选项
			String answerString = lGCharacterPart.getPartAnswer();
			Log.d(TAG, "answerString:" + answerString);
			String[] answerPicArray = UiUtil.getListFormString(answerString);// 答案选项
			List<String> answerList = new ArrayList<String>();
			for (int i = 0; i < answerPicArray.length; i++) {
				answerList.add(answerPicArray[i]);
			}
			modelWord.setAnswerList(answerList);// 拿到答案集合
			List<String> picList = new ArrayList<String>();// 存放选择图片名字的集合
			for (int i = 0; i < picArray.length; i++) {
				for (int j = 0; j < answerPicArray.length; j++) {
					if (picArray[i].equals(answerPicArray[j])) {
						picList.add(picArray[i]);// 加入答案
					}
				}
				if (!randomList.contains(picArray[i])
						&& !picList.contains(picArray[i])) {
					randomList.add(picArray[i]);// 加入答案外的
				}
			}
			Collections.shuffle(randomList);
			int length = picArray.length;
			if (picList.size() < length) {
				length = Math.min(length, 5);// 可能是4 5 6 选最小
				int x = length - picList.size();
				for (int i = 0; i < x; i++) {
					picList.add(randomList.get(i));// 答案以外随机加入到picArray.length个
				}
			}
			List<SubLGModel> subLGModelList = modelWord.getSubLGModelList();
			try {
				for (int i = 0; i < picList.size(); i++) {
					LGCharacterPart lgCharacterPart = (LGCharacterPart) MyDao
							.getDao(LGCharacterPart.class).queryForId(
									Integer.valueOf(picList.get(i)));
					String picName = lgCharacterPart.getPartName();
					int partId = lgCharacterPart.getPartId();
					String strRect = lgCharacterPart.getStrRect();
					SubLGModel subLGModel = modelWord.new SubLGModel();
					subLGModel.setImageName(picName);
					subLGModel.setWordId(partId);// 拿到tag图片对应的id
					subLGModel.setStrRect(strRect);
					subLGModelList.add(subLGModel);// 拿到所有图片选项

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			modelWord.setSubLGModelList(subLGModelList);

			imageMoveFragment = new LearnImageMoveFragment();
			baseFragment = imageMoveFragment;
			Bundle bundle = new Bundle();
			bundle.putSerializable("modelWord", modelWord);
			imageMoveFragment.setArguments(bundle);// 把题传过去
			replaceTo2("imageMoveFragment");
		}
	}

	private void parseSentenseData5(LessonRepeatRegex lessonRepeatRegex) {

		modelWord = new LGModelWord();
		int lgTableId = lessonRepeatRegex.getLgTableId();
		try {
			LGSentence lgSentence = (LGSentence) MyDao.getDao(LGSentence.class)
					.queryForId(lgTableId);
			String title = lgSentence.getTranslations();
			modelWord.setTitle(title);// 拿到标题
			modelWord.setSentenceId(lgTableId);// 拿到SentenceId
			modelWord.setLessonId(lessonId);// 拿到lessonId
			String dirCode = lgSentence.getDirCode();// 得到mp3文件名
			dirCode = "s-" + lgTableId + "-" + dirCode + ".mp3";
			modelWord.setVoicePath(dirCode);
			List<String> answerList = modelWord.getAnswerList();
			LGModel_Sentence_050 sentence050 = (LGModel_Sentence_050) MyDao
					.getDao(LGModel_Sentence_050.class).queryBuilder().where()
					.eq("SentenceId", lgTableId).queryForFirst();
			String[] splitAnswer = UiUtil.getListFormString(sentence050
					.getAnswer());
			StringBuffer buffer = new StringBuffer();
			Dao dao = MyDao.getDao(LGWord.class);
			for (int i = 0; i < splitAnswer.length; i++) {
				LGWord lgWord = (LGWord) dao.queryForId(Integer
						.valueOf(splitAnswer[i]));
				String word = lgWord.getWord();
				buffer = buffer.append(word + " ");
			}
			String strBuffer = buffer.toString();
			modelWord.setAnswerText(strBuffer);
			answerList.add(UiUtil.StringFilter(strBuffer));// 把答案加进去
			modelWord.setAnswerList(answerList);
			String[] split = UiUtil.getListFormString(sentence050.getOptions());
			List<SubLGModel> subLGModelList = modelWord.getSubLGModelList();
			for (int i = 0; i < split.length; i++) {
				SubLGModel subLGModel = modelWord.new SubLGModel();
				LGWord lgWord = (LGWord) dao.queryForId(Integer
						.valueOf(split[i]));
				String option = lgWord.getWord();
				subLGModel.setOption(option);// 拿到选项
				subLGModelList.add(subLGModel);
			}
			Collections.shuffle(subLGModelList);
			modelWord.setSubLGModelList(subLGModelList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sentenceMoveFragment = new LearnSentenceMoveFragment();
		baseFragment = sentenceMoveFragment;
		Bundle bundle3 = new Bundle();
		bundle3.putSerializable("modelWord", modelWord);
		sentenceMoveFragment.setArguments(bundle3);// 把题传过去

	}

	private void parseSentenseData4(LessonRepeatRegex lessonRepeatRegex) {
		modelWord = new LGModelWord();
		int lgTableId = lessonRepeatRegex.getLgTableId();
		try {
			LGSentence lgSentence = (LGSentence) MyDao.getDao(LGSentence.class)
					.queryForId(lgTableId);
			String title = lgSentence.getSentence();
			modelWord.setTitle(title);// 拿到标题
			modelWord.setSentenceId(lgTableId);// 拿到SentenceId
			modelWord.setLessonId(lessonId);// 拿到lessonId
			String dirCode = lgSentence.getDirCode();// 得到mp3文件名
			dirCode = "s-" + lgTableId + "-" + dirCode + ".mp3";
			modelWord.setVoicePath(dirCode);
			List<String> answerList = modelWord.getAnswerList();
			LGModel_Sentence_040 sentence040 = (LGModel_Sentence_040) MyDao
					.getDao(LGModel_Sentence_040.class).queryBuilder().where()
					.eq("SentenceId", lgTableId).queryForFirst();
			String[] splitAnswer = sentence040.getAnswer().split("!@@@!");
			for (int i = 0; i < splitAnswer.length; i++) {
				answerList.add(UiUtil.StringFilter(splitAnswer[i]));// 加入答案
			}
			modelWord.setAnswerList(answerList);
			modelWord.setAnswerText(splitAnswer[0]);// 拿到答案 暂时为第一个
			String[] split = UiUtil.getListFormString(sentence040.getOptions());
			List<SubLGModel> subLGModelList = modelWord.getSubLGModelList();
			for (int i = 0; i < split.length; i++) {
				SubLGModel subLGModel = modelWord.new SubLGModel();
				subLGModel.setOption(split[i]);// 拿到选项
				subLGModelList.add(subLGModel);
			}
			Collections.shuffle(subLGModelList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sentenceMoveFragment = new LearnSentenceMoveFragment();
		baseFragment = sentenceMoveFragment;
		Bundle bundle3 = new Bundle();
		bundle3.putSerializable("modelWord", modelWord);
		sentenceMoveFragment.setArguments(bundle3);// 把题传过去

	}

	private void parseSentenseData2(LessonRepeatRegex lessonRepeatRegex2) {
		// 查sentence得到title
		// 查sentence020 得到answerList
		modelWord = new LGModelWord();
		int lgTableId = lessonRepeatRegex.getLgTableId();
		try {
			LGSentence lgSentence = (LGSentence) MyDao.getDao(LGSentence.class)
					.queryForId(lgTableId);
			String title = lgSentence.getSentence();
			modelWord.setTitle(title);// 拿到title
			modelWord.setSentenceId(lgTableId);// 拿到sentenceId
			String dirCode = lgSentence.getDirCode();// 得到mp3文件名
			dirCode = "s-" + lgTableId + "-" + dirCode + ".mp3";
			modelWord.setVoicePath(dirCode);
			LGModel_Sentence_020 lgSentence020 = (LGModel_Sentence_020) MyDao
					.getDao(LGModel_Sentence_020.class).queryBuilder().where()
					.eq("SentenceId", lgTableId).queryForFirst();
			String[] splitAnswer = lgSentence020.getAnswer().split("!@@@!");
			modelWord.setAnswerText(splitAnswer[0]);// 设置答案文本
			List<String> answerList = modelWord.getAnswerList();
			for (int i = 0; i < splitAnswer.length; i++) {
				String stringFilter = UiUtil.StringFilter(splitAnswer[i]);
				answerList.add(stringFilter);
			}
			modelWord.setAnswerList(answerList);// 加入答案集合
		} catch (SQLException e) {
			e.printStackTrace();
		}
		wordInputFragment = new LearnWordInputFragment();
		baseFragment = wordInputFragment;
		Bundle bundle4 = new Bundle();
		bundle4.putSerializable("modelWord", modelWord);
		wordInputFragment.setArguments(bundle4);// 把题传过去
	}

	private void parseSetData4(LessonRepeatRegex lessonRepeatRegex) {
		// 先查询拿到wordid 查询 LGWORD 得到title
		// 查询040得到answer
		modelWord = new LGModelWord();
		int lgTableId = lessonRepeatRegex.getLgTableId();
		try {
			LGWord lgWord = (LGWord) MyDao.getDao(LGWord.class).queryForId(
					lgTableId);
			String title = lgWord.getWord() + "/" + lgWord.getPinyin();
			modelWord.setTitle(title);// 拿到title
			modelWord.setWordId(lgTableId);// 拿到wordId
			modelWord.setLessonId(lessonId);// 拿到lessonId
			String dirCode = lgWord.getDirCode();// 得到mp3文件名
			dirCode = "w-" + lgTableId + "-" + dirCode + ".mp3";
			modelWord.setVoicePath(dirCode);
			LGModel_Word_040 lgWord040 = (LGModel_Word_040) MyDao
					.getDao(LGModel_Word_040.class).queryBuilder().where()
					.eq("WordId", lgTableId).queryForFirst();
			String[] splitAnswer = lgWord040.getAnswers().split("!@@@!");
			modelWord.setAnswerText(splitAnswer[0]);// 设置答案文本
			List<String> answerList = modelWord.getAnswerList();
			for (int i = 0; i < splitAnswer.length; i++) {
				String stringFilter = UiUtil.StringFilter(splitAnswer[i]);
				answerList.add(stringFilter);
			}
			modelWord.setAnswerList(answerList);// 加入答案集合
		} catch (SQLException e) {
			e.printStackTrace();
		}
		wordInputFragment = new LearnWordInputFragment();
		baseFragment = wordInputFragment;
		Bundle bundle4 = new Bundle();
		bundle4.putSerializable("modelWord", modelWord);
		wordInputFragment.setArguments(bundle4);// 把题传过去
	}

	private void parseSetData3(LessonRepeatRegex lessonRepeatRegex) {
		// 查询lgword表title= word+/+pinyin set过去id
		// 查询030表得到options 以;号分割 得到String answer
		modelWord = new LGModelWord();
		int lgTableId = lessonRepeatRegex.getLgTableId();
		try {
			LGWord lgWord = (LGWord) MyDao.getDao(LGWord.class).queryForId(
					lgTableId);
			String title = lgWord.getWord() + "/" + lgWord.getPinyin();
			modelWord.setTitle(title);// 拿到标题
			modelWord.setWordId(lgTableId);// 拿到wordid
			modelWord.setLessonId(lessonId);// 拿到lessonId
			String dirCode = lgWord.getDirCode();// 得到mp3文件名
			dirCode = "w-" + lgTableId + "-" + dirCode + ".mp3";
			modelWord.setVoicePath(dirCode);
			List<String> answerList = modelWord.getAnswerList();
			LGModel_Word_030 word030 = (LGModel_Word_030) MyDao
					.getDao(LGModel_Word_030.class).queryBuilder().where()
					.eq("WordId", lgTableId).queryForFirst();
			String[] splitAnswer = word030.getAnswer().split("!@@@!");
			for (int i = 0; i < splitAnswer.length; i++) {
				answerList.add(UiUtil.StringFilter(splitAnswer[i]));// 加入答案
			}
			modelWord.setAnswerList(answerList);
			modelWord.setAnswerText(splitAnswer[0]);// 拿到答案 暂时为第一个
			String[] split = UiUtil.getListFormString(word030.getOptions());
			List<SubLGModel> subLGModelList = modelWord.getSubLGModelList();
			for (int i = 0; i < split.length; i++) {
				SubLGModel subLGModel = modelWord.new SubLGModel();
				subLGModel.setOption(split[i]);// 拿到选项
				subLGModelList.add(subLGModel);
			}
			Collections.shuffle(subLGModelList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sentenceMoveFragment = new LearnSentenceMoveFragment();
		baseFragment = sentenceMoveFragment;
		Bundle bundle3 = new Bundle();
		bundle3.putSerializable("modelWord", modelWord);
		sentenceMoveFragment.setArguments(bundle3);// 把题传过去
	}

	private void parseSentenseData3(LessonRepeatRegex lessonRepeatRegex) {
		modelWord = new LGModelWord();
		int lgTableId = lessonRepeatRegex.getLgTableId();
		try {
			LGSentence lgSentence = (LGSentence) MyDao.getDao(LGSentence.class)
					.queryForId(lgTableId);
			LGModel_Sentence_030 sentence030 = (LGModel_Sentence_030) MyDao
					.getDao(LGModel_Sentence_030.class).queryBuilder().where()
					.eq("SentenceId", lgTableId).queryForFirst();
			modelWord.setSentenceId(lgTableId);// 拿到sentenceId
			int answer = sentence030.getAnswer();
			modelWord.setAnswer(answer);// 拿到answer
			modelWord.setLessonId(lessonId);// 拿到lessonId
			String dirCode = lgSentence.getDirCode();// 得到mp3文件名
			dirCode = "s-" + lgTableId + "-" + dirCode + ".mp3";
			modelWord.setVoicePath(dirCode);
			List<SubLGModel> subLGModelList = modelWord.getSubLGModelList();
			String sentence = lgSentence.getSentence();
			String[] options = UiUtil.getListFormString(sentence030
					.getOptions());
			for (int i = 0; i < options.length; i++) {
				SubLGModel subLGModel = modelWord.new SubLGModel();
				String[] option = options[i].split("=");
				String optionWord = option[1];
				int valueOf = Integer.valueOf(option[0]);
				subLGModel.setWordId(valueOf);// 拿到id
				if (answer == valueOf) {
					sentence = sentence.replace(optionWord.split("-")[0],
							" ___ ");
					modelWord.setAnswerText(option[1]);// 拿到answer文本
				}
				optionWord = optionWord.replace("-", "/");
				subLGModelList.add(subLGModel);// 加入到集合
				subLGModel.setOption(optionWord);// 拿到选项
			}
			modelWord.setTitle(sentence);
			Collections.shuffle(subLGModelList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		wordSelectFragment = new LearnWordSelectFragment();
		baseFragment = wordSelectFragment;
		Bundle bundle3 = new Bundle();
		bundle3.putSerializable("modelWorld", modelWord);
		wordSelectFragment.setArguments(bundle3);// 把题传过去
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
			modelWord.setSentenceId(lgTableId);// 拿到sentenceid
			modelWord.setLessonId(lessonId);// 拿到lessonId
			modelWord.setTitle(lgSentence.getSentence());
			modelWord.setAnswer(sentence010.getAnswer());
			String dirCode = lgSentence.getDirCode();// 得到mp3文件名
			dirCode = "s-" + lgTableId + "-" + dirCode + ".mp3";
			modelWord.setVoicePath(dirCode);
			List<SubLGModel> subLGModelList = modelWord.getSubLGModelList();
			int answer = sentence010.getAnswer();
			String[] options = UiUtil.getListFormString(sentence010
					.getOptions());
			for (int i = 0; i < options.length; i++) {
				String[] option = options[i].split("=");
				SubLGModel subLGModel = modelWord.new SubLGModel();
				int id = Integer.valueOf(option[0]);
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
			LGWord lgWord1 = (LGWord) MyDao.getDao(LGWord.class).queryForId(
					lessonRepeatRegex.getLgTableId());
			question = lgWord1.getWord();
			String dirCode = lgWord1.getDirCode();// 得到mp3文件名
			dirCode = "w-" + lgTableId + "-" + dirCode + ".mp3";
			modelWord.setVoicePath(dirCode);
			String[] splitWordId = UiUtil.getListFormString(word_060
					.getOptions());
			List<SubLGModel> subLGModelList = modelWord.getSubLGModelList();
			for (int i = 0; i < splitWordId.length; i++) {
				SubLGModel subLGModel = modelWord.new SubLGModel();
				String[] option = splitWordId[i].split("=");
				int id = Integer.valueOf(option[0]);
				subLGModel.setWordId(id);// 拿到判断对错使用的ID
				String optionRep = option[1].replace("-", "/");
				subLGModel.setOption(optionRep);// 拿到选项
				if (id == answer) {
					modelWord.setAnswerText(optionRep);
					question = question.replace(optionRep.split("/")[0],
							" ____ ");
					modelWord.setTitle(question);// 拿到title
				}

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
			LGWord lgWord1 = (LGWord) MyDao.getDao(LGWord.class).queryForId(
					lessonRepeatRegex.getLgTableId());
			modelWord.setWordId(lgTableId);// 拿到wordId
			int answer = word_020.getAnswer();
			modelWord.setAnswer(answer);// 拿到答案
			modelWord.setLessonId(lessonId);// 拿到lessonId
			String dirCode = lgWord1.getDirCode();// 得到mp3文件名
			dirCode = "w-" + lgTableId + "-" + dirCode + ".mp3";
			modelWord.setVoicePath(dirCode);
			LGWord lgWordAnswer = (LGWord) MyDao.getDao(LGWord.class)
					.queryForId(answer);
			String left = lgWordAnswer.getTranslations();
			String right = lgWordAnswer.getWord() + "/"
					+ lgWordAnswer.getPinyin();
			modelWord.setAnswerText(left + "=" + right);// 拿到答案文本

			question = getString(R.string.learn_select)+" " + "\"" + lgWord1.getTranslations() + "\"";
			modelWord.setTitle(question);// 拿到title
			String[] splitWordId = UiUtil.getListFormString(word_020
					.getOptions());
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
			// String dirCode = lgWord.getDirCode();//得到mp3文件名
			// dirCode="w-"+lgTableId+"-"+dirCode+".mp3";
			// modelWord.setVoicePath(dirCode);
			modelWord.setWordId(lgTableId);// 拿到wordId
			String title = lgWord.getTranslations();
			modelWord.setTitle(getString(R.string.learn_select)+" " + "\"" + title + "\"");// 拿到title
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
			String[] splitOptions = UiUtil.getListFormString(imageOptions);// 得到4个选项
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
				// dirCode="w-"+lgTableId+"-"+dirCode+".mp3";
				subLGModel.setSubVoicePath("w-" + splitOptions[i] + "-"
						+ lGWord.getDirCode() + ".mp3");// 拿到对应声音
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

	private Integer getRandom(int id) {
		// 给每个Subject随机赋值确定查哪张表
		int randomSubject = -1;
		if (lgTable == 0) {
			LGWord lgWord = null;
			try {
				lgWord = (LGWord) MyDao.getDao(LGWord.class).queryForId(id);
			} catch (SQLException e) {
			}
			int length = lgWord.getWord().length();
			if (length > 1) {// 长度大于一才可以到第6个表
				randomSubject = new Random().nextInt(6) + 1;
			} else {
				randomSubject = new Random().nextInt(5) + 1;
			}
		} else if (lgTable == 1) {
			randomSubject = new Random().nextInt(5) + 1;
		} else if (lgTable == 2) {
			randomSubject = 1;
		}
		return randomSubject;
	}

	/**
	 * checkbtn是否可点击
	 */
	public void isCheckBtnActived(boolean isActived) {
		if (isActived) {
			btn_check.setEnabled(true);
			btn_check.setBackgroundColor(context.getResources().getColor(
					R.color.chinese_skill_blue));
			btn_check.setTextColor(context.getResources().getColor(
					R.color.white));

		} else {
			btn_check.setEnabled(false);
			btn_check.setBackgroundColor(context.getResources().getColor(
					R.color.min_grey));
			btn_check.setTextColor(context.getResources().getColor(
					R.color.chinese_skill_blue));
		}
	}

}
