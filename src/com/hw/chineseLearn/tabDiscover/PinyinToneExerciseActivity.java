package com.hw.chineseLearn.tabDiscover;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.bean.TbMyPinyinTone;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.hw.chineseLearn.model.PinyinToneLessonExerciseModel;
import com.hw.chineseLearn.tabLearn.LessonResultActivity;
import com.hw.chineseLearn.tabLearn.LessonReviewResultActivity;
import com.util.tool.BitmapLoader;
import com.util.tool.HttpHelper;
import com.util.tool.MediaPlayUtil;
import com.util.tool.MediaPlayerHelper;
import com.util.tool.UiUtil;
import com.util.tool.UtilMedthod;
import com.util.weight.CustomDialog;
import com.util.weight.LocusPassWordView;
import com.util.weight.XieLineView;
import com.util.weight.LocusPassWordView.OnAnamationCompleteListener;
import com.util.weight.LocusPassWordView.OnCompleteListener;
import com.util.weight.Point;

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

	private LocusPassWordView lpwv;
	Resources resourse;
	int colorBlue = 0;
	int colorBlack = 0;
	int colorRed = 0;
	int colorGrey = 0;
	Drawable drawable;
	Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_pytone_exercise, null);
		setContentView(contentView);
		context = this;
		resourse = context.getResources();
		colorBlue = resourse.getColor(R.color.chinese_skill_blue);
		colorBlack = resourse.getColor(R.color.black);
		colorRed = resourse.getColor(R.color.chinese_skill_red);
		colorGrey = resourse.getColor(R.color.mid_grey);
		drawable = resourse.getDrawable(R.drawable.bg_border_grey2);
		bitmap = BitmapLoader.drawableToBitmap(drawable);
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
	private boolean isDrawRight = false;
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
	LinearLayout lin_content;

	String[] yunmuOneTone = { "ā", "ō", "ē", "ī", "ū", "ǖ" };
	String[] yunmuTwoTone = { "á", "ó", "é", "í", "ú", "ǘ" };
	String[] yunmuThreeTone = { "ǎ", "ǒ", "ě", "ǐ", "ǔ", "ǚ" };
	String[] yunmuFourTone = { "à", "ò", "è", "ì", "ù", "ǜ" };
	String[] yunmu = { "a", "o", "e", "i", "u", "ü" };

	/**
	 * 第一声声调的集合
	 */
	ArrayList<String> oneTone = new ArrayList<String>();
	/**
	 * 第二声声调的集合
	 */
	ArrayList<String> twoTone = new ArrayList<String>();
	/**
	 * 第三声声调的集合
	 */
	ArrayList<String> threeTone = new ArrayList<String>();
	/**
	 * 第四声声调的集合
	 */
	ArrayList<String> fourTone = new ArrayList<String>();
	/**
	 * 正确声调的集合
	 */
	ArrayList<Integer> rightToneList = new ArrayList<Integer>();

	/**
	 * 画声调的下标
	 */
	int drawIndex = 0;

	/**
	 * 
	 */
	ArrayList<View> itemViewList = new ArrayList<View>();

	/**
	 * 这里做一个判断正确的规则，错0，对list.size()。如果一个题目有三个字，则正确答案scoreNum = 1*3；错误答案scoreNum=
	 * 0；
	 * 
	 */
	int scoreNum = 0;

	/**
	 * 初始化
	 */
	public void init() {
		initToneList();
		checkView = LayoutInflater.from(context).inflate(
				R.layout.layout_learn_exercise_check_dialog, null);
		
		container1 = (LinearLayout) findViewById(R.id.container1);//放line的容器
		
		btn_continue = (Button) findViewById(R.id.btn_continue);
		btn_continue.setOnClickListener(onClickListener);
		isCheckBtnActived(false);
		lin_content = (LinearLayout) findViewById(R.id.lin_content);

		btn_play = (ImageView) findViewById(R.id.btn_play);
		btn_play.setOnClickListener(onClickListener);
		lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
		lpwv.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String mPassword) {
				lpwv.clearPassword();
				setViews(checkDrawToneIsRight(mPassword));
			}
		});
		initDatas();
	}

	/**
	 * 四个声调集合
	 */
	private void initToneList() {
		oneTone.clear();
		twoTone.clear();
		threeTone.clear();
		fourTone.clear();

		oneTone.add("0, 1");
		oneTone.add("0,1,2");
		oneTone.add("1,2");
		oneTone.add("3,4");
		oneTone.add("3,4,5");
		oneTone.add("4,5");
		oneTone.add("6,7");
		oneTone.add("6,7,8");
		oneTone.add("7,8");

		twoTone.add("3,1");
		twoTone.add("4,2");
		twoTone.add("6,4");
		twoTone.add("7,5");
		twoTone.add("6,4,2");

		threeTone.add("3,7,5");
		threeTone.add("0,4,2");

		fourTone.add("0,4");
		fourTone.add("1,5");
		fourTone.add("3,7");
		fourTone.add("4,8");
		fourTone.add("0,4,8");
	}

	/**
	 * 根据拼音判断是第几声
	 */
	private String findToneByPy(String pinyin) {
		int pinyinOrder = -1;
		if (pinyin != null) {
			for (int i = 0; i < yunmuOneTone.length; i++) {
				String str = yunmuOneTone[i];
				if (!pinyin.contains(str)) {
					continue;
				}
				pinyin = pinyin.replace(str, yunmu[i]);
				pinyinOrder = 1;
				break;
			}
			for (int i = 0; i < yunmuTwoTone.length; i++) {
				String str = yunmuTwoTone[i];
				if (!pinyin.contains(str)) {
					continue;
				}
				pinyin = pinyin.replace(str, yunmu[i]);
				pinyinOrder = 2;
				break;
			}
			for (int i = 0; i < yunmuThreeTone.length; i++) {
				String str = yunmuThreeTone[i];
				if (!pinyin.contains(str)) {
					continue;
				}
				pinyin = pinyin.replace(str, yunmu[i]);
				pinyinOrder = 3;
				break;
			}

			for (int i = 0; i < yunmuFourTone.length; i++) {
				String str = yunmuFourTone[i];
				if (!pinyin.contains(str)) {
					continue;
				}
				pinyin = pinyin.replace(str, yunmu[i]);
				pinyinOrder = 4;
				break;
			}
		}
		if (pinyinOrder != -1) {
			rightToneList.add(pinyinOrder);
		}
		return pinyin;
	}

	/**
	 * 检查画的声调是否正确
	 * 
	 * @param mPassword
	 * @return
	 */
	private boolean checkDrawToneIsRight(String mPassword) {
		boolean isDrawRight = false;
		int pinyinOrder = 0;
		for (int i = 0; i < oneTone.size(); i++) {
			String toneStr = oneTone.get(i);
			if (!mPassword.equals(toneStr)) {
				continue;
			}
			pinyinOrder = 1;
			break;
		}
		for (int i = 0; i < twoTone.size(); i++) {
			String toneStr = twoTone.get(i);
			if (!mPassword.equals(toneStr)) {
				continue;
			}
			pinyinOrder = 2;
			break;
		}
		for (int i = 0; i < threeTone.size(); i++) {
			String toneStr = threeTone.get(i);
			if (!mPassword.equals(toneStr)) {
				continue;
			}
			pinyinOrder = 3;
			break;
		}
		for (int i = 0; i < fourTone.size(); i++) {
			String toneStr = fourTone.get(i);
			if (!mPassword.equals(toneStr)) {
				continue;
			}
			pinyinOrder = 4;
			break;
		}
		int pinyinOrdFormList = rightToneList.get(drawIndex);
		if (pinyinOrdFormList == pinyinOrder) {
			isDrawRight = true;
		} else {
			isDrawRight = false;
		}
		Log.d(TAG, "checkDrawToneIsRight()-drawIndex:" + drawIndex);
		Log.d(TAG, "checkDrawToneIsRight()-isDrawRight:" + isDrawRight);
		return isDrawRight;
	}

	private void setViews(boolean isDrawRight) {

		if (pinList.length == rightToneList.size()
				&& pinList.length == itemViewList.size()) {

			String py = pinList[drawIndex];
			View view = itemViewList.get(drawIndex);
			ImageView iv_tone = (ImageView) view.findViewById(R.id.iv_tone);
			TextView tv_py = (TextView) view.findViewById(R.id.tv_py);
			TextView tv_cn = (TextView) view.findViewById(R.id.tv_cn);
			// 当前置黑
			tv_py.setTextColor(colorBlack);
			tv_cn.setTextColor(colorBlack);
			if (isDrawRight) {
				scoreNum += 1;
				iv_tone.setImageBitmap(UtilMedthod.translateImageColor(bitmap,
						colorBlue));
			} else {// 错一个则全错
				scoreNum = 0;
				iv_tone.setImageBitmap(UtilMedthod.translateImageColor(bitmap,
						colorRed));
				// 调用九宫格内提示动画 （内部俩动画） 外边一个动画
				lpwv.startAnamation(rightToneList.get(drawIndex));
				lpwv.setOnAnamationCompleteListener(new OnAnamationCompleteListener() {

					@Override
					public void onCompleteListener() {
						// 画直线 做动画

						drawLineAndAnamation();

						UiUtil.showToast(PinyinToneExerciseActivity.this,
								"anamation end");
					}
				});
			}

			//下一个置蓝
			int nextIndex = drawIndex + 1;
			if (nextIndex <= itemViewList.size() - 1) {
				if (rightToneList.size() > 1) {// 多个字的
					View viewNext = itemViewList.get(nextIndex);
					TextView tv_py_next = (TextView) viewNext
							.findViewById(R.id.tv_py);
					TextView tv_cn_next = (TextView) viewNext
							.findViewById(R.id.tv_cn);
					tv_py_next.setTextColor(colorBlue);
					tv_cn_next.setTextColor(colorBlue);
				}
			}
			drawIndex++;
			if (drawIndex > itemViewList.size() - 1) {//最后一个
				drawIndex = itemViewList.size() - 1;
				isCheckBtnActived(true);
				tv_py.setTextColor(colorBlack);
				tv_py.setText(py);
				tv_cn.setTextColor(colorBlack);
			}

			if (rightToneList.size() > 1) {// 多个字的
				// 不是最后一个置蓝
				if (drawIndex < itemViewList.size() - 1) {
					tv_py.setTextColor(colorBlue);
					tv_cn.setTextColor(colorBlue);
				}
			}
		}
	}

	/**
	 * 画出平移的直线并做位移和缩放的动画
	 * 
	 */
	private void drawLineAndAnamation() {

		Integer tone = rightToneList.get(drawIndex);
		Point[][] points = lpwv.getmPoints();
		
		

		switch (tone) {
		case 1://平线动画
			XieLineView lineView=new XieLineView(context);
			container1.addView(lineView);
			
			ObjectAnimator ofFloat = ObjectAnimator.ofFloat(lineView, "translationX", 300,200);
		    ofFloat.setDuration(2000);
		    ofFloat.start();
			
			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

			break;
		}
	}

	/**
	 * 
	 */
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
			// lin_pander_life.addView(imageView);
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

	String pinList[];

	/**
	 * 设置要显示的数据
	 * 
	 * @param index
	 */
	private void setData(int index) {
		//貌似重新设置数据需要去掉前一个lineview
		if (lessonModels != null && lessonModels.size() != 0) {
			PinyinToneLessonExerciseModel model = lessonModels.get(index);
			if (model != null) {
				String py = model.getPy();
				String cn = model.getCn();
				String en = model.getEn();

				if (!"".equals(py)) {
					pinList = py.split(" ");
					if (pinList != null) {
						// reset
						isCheckBtnActived(false);
						rightToneList.clear();
						itemViewList.clear();
						lin_content.removeAllViews();
						scoreNum = 0;
						for (int i = 0; i < pinList.length; i++) {
							String pinyin = pinList[i];
							pinyin = findToneByPy(pinyin);
							View view = LayoutInflater.from(context).inflate(
									R.layout.layout_pytone_item, null);
							ImageView iv_tone = (ImageView) view
									.findViewById(R.id.iv_tone);
							iv_tone.setImageBitmap(bitmap);
							TextView tv_py = (TextView) view
									.findViewById(R.id.tv_py);
							tv_py.setText(pinyin);

							TextView tv_cn = (TextView) view
									.findViewById(R.id.tv_cn);
							tv_cn.setText("" + cn.charAt(i));
							if (i == 0) {
								tv_cn.setTextColor(colorBlue);
								tv_py.setTextColor(colorBlue);
							} else {
								tv_cn.setTextColor(colorGrey);
								tv_py.setTextColor(colorGrey);
							}
							lin_content.addView(view);
							itemViewList.add(view);
						}

					}
				}
				TextView tv_en = (TextView) findViewById(R.id.tv_en);
				tv_en.setText("" + en);
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

					// Intent intent = new
					// Intent(PinyinToneExerciseActivity.this,
					// PinyinToneResultActivity.class);
					// intent.putExtra("loseAllPanders", "loseAllPanders");
					// startActivityForResult(intent, 100);

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

				if (scoreNum == pinList.length) {// 全部正确，才算对。

					setProgressViewBg(exerciseIndex,// 设置进度快的背景
							R.drawable.bg_progress_rigth);
					// showCheckDialog(true);// 弹出正确的对话框
					// playRightSound();// 播放正确的声音
				} else {
					setProgressViewBg(exerciseIndex,
							R.drawable.bg_progress_wrong);
					// showCheckDialog(false);// 展示错误对话框
					// playWrongSound();// 播放错误音乐
				}
				Log.d(TAG, "exerciseIndex:" + exerciseIndex);
				if (exerciseIndex == exerciseCount - 1) {// 最后一道题目

					Intent intent = new Intent(PinyinToneExerciseActivity.this,
							LessonReviewResultActivity.class);
					intent.putExtra("loseAllPanders", "");
					startActivityForResult(intent, 100);
				}
				if (exerciseIndex < exerciseCount - 1) {
					exerciseIndex++;
					setData(exerciseIndex);
				}
				drawIndex = 0;
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
	private LinearLayout container1;

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
			btn_continue.setBackgroundColor(colorBlue);
			btn_continue.setTextColor(context.getResources().getColor(
					R.color.white));

		} else {
			btn_continue.setEnabled(false);
			btn_continue.setBackgroundColor(colorGrey);
			btn_continue.setTextColor(colorBlue);
		}
	}

}
