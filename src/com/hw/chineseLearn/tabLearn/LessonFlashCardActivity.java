package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.LGCharacter;
import com.hw.chineseLearn.dao.bean.LGModelFlashCard;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;
import com.util.weight.SlideSwitch;
import com.util.weight.SlideSwitch.SlideListener;

/**
 * FlashCard页面
 * 
 * @author yh
 */
public class LessonFlashCardActivity extends BaseActivity {

	private String TAG = "==LessonFlashCardActivity==";
	public Context context;
	private Button btn_go;
	private Resources resources;
	private int width;
	private int height;
	View contentView;
	int characterCount = 0;
	int wordsCount = 0;
	int sentenceCount = 0;
	int chooseCount = 0;

	private TextView tv_character_count;
	private TextView tv_words_count;
	private TextView tv_sentence_count;
	private ImageView iv_title_right;

	SeekBar seekBar;
	TextView tv_default_number;
	TextView tv_chooseCount;

	private ArrayList<LGModelFlashCard> datas = new ArrayList<LGModelFlashCard>();
	ArrayList<TbMyCharacter> tbMyCharacterList;
	ArrayList<TbMyWord> tbMyWordList;
	ArrayList<TbMySentence> tbMySentenceList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_flashcard, null);
		setContentView(contentView);
		context = this;
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 4;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 5;
		resources = context.getResources();
		init();
	}

	boolean isCharacterChecked = false;
	boolean isSentenceChecked = false;
	boolean isWordChecked = false;
	boolean isAutoPlay = true;

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {

		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"FlashCard", View.GONE, View.VISIBLE,
				R.drawable.btn_selector_top_right);

		btn_go = (Button) contentView.findViewById(R.id.btn_go);
		LayoutParams py = btn_go.getLayoutParams();
		py.width = width;
		py.height = width;
		btn_go.setLayoutParams(py);
		btn_go.setOnClickListener(onClickListener);

		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					runOnUiThread(new Runnable() {
						public void run() {
							playHeartbeatAnimation();
						}
					});
				}
			};
		}.start();
		tv_character_count = (TextView) contentView
				.findViewById(R.id.tv_character_count);
		tv_words_count = (TextView) contentView
				.findViewById(R.id.tv_words_count);
		tv_sentence_count = (TextView) contentView
				.findViewById(R.id.tv_sentence_count);

		tv_character_count.setText("0");
		tv_words_count.setText("0");
		tv_sentence_count.setText("0");
		try {
			tbMyCharacterList = (ArrayList<TbMyCharacter>) MyDao.getDaoMy(
					TbMyCharacter.class).queryForAll();

			tbMyWordList = (ArrayList<TbMyWord>) MyDao.getDaoMy(TbMyWord.class)
					.queryForAll();

			tbMySentenceList = (ArrayList<TbMySentence>) MyDao.getDaoMy(
					TbMySentence.class).queryForAll();

			characterCount = tbMyCharacterList.size();
			wordsCount = tbMyWordList.size();
			sentenceCount = tbMySentenceList.size();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tv_character_count.setText("" + characterCount);
		tv_words_count.setText("" + wordsCount);
		tv_sentence_count.setText("" + sentenceCount);
	}

	public void showPopupWindowMenu() {

		String chooseCountStr = CustomApplication.app.preferencesUtil.getValue(
				"count", "0");
		chooseCount = Integer.parseInt(chooseCountStr);
		isCharacterChecked = CustomApplication.app.preferencesUtil.getValuesBoolean("isCharacterChecked");
		isSentenceChecked = CustomApplication.app.preferencesUtil.getValuesBoolean("isSentenceChecked");
		isWordChecked = CustomApplication.app.preferencesUtil.getValuesBoolean("isWordChecked");
		isAutoPlay = CustomApplication.app.preferencesUtil.getValuesBoolean("isAutoPlay");

		View view = LayoutInflater.from(this).inflate(
				R.layout.layout_title_menu1, null);

		int width = CustomApplication.app.displayMetrics.widthPixels / 10 * 6;
		final PopupWindow popupWindow = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		LinearLayout lin_content = (LinearLayout) view
				.findViewById(R.id.lin_content);
		LayoutParams ly = lin_content.getLayoutParams();
		ly.width = width;
		lin_content.setLayoutParams(ly);

		LinearLayout lin_aa = (LinearLayout) view.findViewById(R.id.lin_aa);
		lin_aa.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});
		RadioGroup rg_display = (RadioGroup) view.findViewById(R.id.rg_display);
		rg_display.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {

				case R.id.rb_Chinese://

					break;
				case R.id.rb_Settings:

					break;
				case R.id.rb_Pinyin:

					break;

				}
			}
		});

		SlideSwitch ck_auto_play = (SlideSwitch)view.findViewById(R.id.ck_auto_play);
		ck_auto_play.setState(isAutoPlay);
		ck_auto_play.setSlideListener(new SlideListener() {
			@Override
			public void open() {
				// TODO Auto-generated method stub
				isAutoPlay = true;
			}

			@Override
			public void close() {
				// TODO Auto-generated method stub
				isAutoPlay = false;
			}
		});
		seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		seekBar.setMax(chooseCount);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() // 监听器
		{
			public void onProgressChanged(SeekBar arg0, int progress,
					boolean fromUser) {
				seekBar.setProgress(progress);
				tv_default_number.setText("" + progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});

		CheckBox ck_Character = (CheckBox) view.findViewById(R.id.ck_Character);
		ck_Character.setChecked(isCharacterChecked);
		ck_Character.setOnCheckedChangeListener(onCheckedChangeListener);

		CheckBox ck_Word = (CheckBox) view.findViewById(R.id.ck_Word);
		ck_Word.setChecked(isWordChecked);
		ck_Word.setOnCheckedChangeListener(onCheckedChangeListener);

		CheckBox ck_Sentence = (CheckBox) view.findViewById(R.id.ck_Sentence);
		ck_Sentence.setChecked(isSentenceChecked);
		ck_Sentence.setOnCheckedChangeListener(onCheckedChangeListener);

		tv_default_number = (TextView) view
				.findViewById(R.id.tv_default_number);
		tv_default_number.setText("" + 0);

		tv_chooseCount = (TextView) view.findViewById(R.id.tv_max);
		tv_chooseCount.setText("" + chooseCount);

		// seekBar.setProgress(max);

		popupWindow.setTouchable(true);
		popupWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("mengdd", "onTouch : ");
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bg_half_touming));

		// popupWindow.showAsDropDown(iv_title_right, 0, 0);
		popupWindow.showAtLocation(contentView, Gravity.RIGHT, 0, 60);

		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				CustomApplication.app.preferencesUtil.setValue("count", ""
						+ chooseCount);
				CustomApplication.app.preferencesUtil.setBooleanValue(
						"isCharacterChecked", isCharacterChecked);
				CustomApplication.app.preferencesUtil.setBooleanValue(
						"isSentenceChecked", isSentenceChecked);
				CustomApplication.app.preferencesUtil.setBooleanValue(
						"isWordChecked", isWordChecked);
				CustomApplication.app.preferencesUtil.setBooleanValue(
						"isAutoPlay", isAutoPlay);
			}
		});
	}

	CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub

			switch (buttonView.getId()) {
			case R.id.ck_Character:
				// ArrayList<TbMyCharacter> tbMyCharacterList;
				// ArrayList<TbMyWord> tbMyWordList;
				// ArrayList<TbMySentence> tbMySentenceList;

				if (isChecked) {
					chooseCount = chooseCount + characterCount;
					isCharacterChecked = true;
					// for (int i = 0; i < tbMyCharacterList.size(); i++) {
					//
					// TbMyCharacter model = tbMyCharacterList.get(i);
					// if (model == null) {
					// continue;
					// }
					// LGModelFlashCard lGModelFlashCard = new
					// LGModelFlashCard();
					// int id = model.getCharId();
					//
					// try {
					// @SuppressWarnings("unchecked")
					// LGCharacter character = (LGCharacter) MyDao.getDao(
					// LGCharacter.class).queryForId(id);
					//
					// if (character == null) {
					// Log.e(TAG, "character==null");
					// continue;
					// }
					// String chinese = character.getCharacter();
					// String pinyin = character.getPinyin();
					// String english = character.getTranslation();
					//
					// lGModelFlashCard.setId(id);
					// lGModelFlashCard.setChinese(chinese);
					// lGModelFlashCard.setPinyin(pinyin);
					// lGModelFlashCard.setEnglish(english);
					// datas.add(lGModelFlashCard);
					//
					// } catch (SQLException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					//
					// }

				} else {
					chooseCount = chooseCount - characterCount;
					isCharacterChecked = false;
				}
				break;
			case R.id.ck_Word:
				if (isChecked) {
					chooseCount = chooseCount + wordsCount;
					isWordChecked = true;
				} else {
					chooseCount = chooseCount - wordsCount;
					isWordChecked = false;
				}
				break;
			case R.id.ck_Sentence:
				if (isChecked) {
					isSentenceChecked = true;
					chooseCount = chooseCount + sentenceCount;
				} else {

					isSentenceChecked = false;
					chooseCount = chooseCount - sentenceCount;
				}
				break;
			default:
				break;
			}
			Log.d(TAG, "max:" + chooseCount);

			tv_chooseCount.setText("" + chooseCount);
			seekBar.setMax(chooseCount);
		}

	};

	/**
	 * 顶部标题栏
	 * 
	 * @param textLeft
	 *            是否显示左边文字
	 * @param imgLeft
	 *            是否显示左边图片
	 * @param title
	 *            标题
	 * @param imgLeftDrawable
	 *            左边图片资源
	 * @param textRight
	 *            是否显示右边文字
	 * @param imgRight
	 *            是否显示右边图片
	 * @param imgRightDrawable
	 *            右边图片资源
	 */
	public void setTitle(int textLeft, int imgLeft, int imgLeftDrawable,
			String title, int textRight, int imgRight, int imgRightDrawable) {

		View view_title = (View) this.findViewById(R.id.view_title);
		Button tv_title = (Button) view_title.findViewById(R.id.btn_title);
		tv_title.setText(title);
		TextView tv_title_left = (TextView) view_title
				.findViewById(R.id.tv_title_left);
		tv_title_left.setVisibility(textLeft);

		ImageView iv_title_left = (ImageView) view_title
				.findViewById(R.id.iv_title_left);
		iv_title_left.setVisibility(imgLeft);
		iv_title_left.setOnClickListener(onClickListener);
		iv_title_left.setImageResource(imgLeftDrawable);

		TextView tv_title_right = (TextView) view_title
				.findViewById(R.id.tv_title_right);
		tv_title_right.setVisibility(textRight);
		tv_title_right.setOnClickListener(onClickListener);

		iv_title_right = (ImageView) view_title
				.findViewById(R.id.iv_title_right);
		iv_title_right.setVisibility(imgRight);
		iv_title_right.setImageResource(imgRightDrawable);
		iv_title_right.setOnClickListener(onClickListener);

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回
				CustomApplication.app
						.finishActivity(LessonFlashCardActivity.this);
				break;

			case R.id.iv_title_right://
				showPopupWindowMenu();
				break;

			case R.id.btn_go:

				startActivity(new Intent(LessonFlashCardActivity.this,
						LessonFlashCardOpActivity.class));
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 按钮模拟心脏跳动
	 */
	private void playHeartbeatAnimation() {
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f));
		animationSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
		animationSet.setDuration(1000);
		animationSet.setInterpolator(new AccelerateInterpolator());
		animationSet.setFillAfter(true);
		animationSet.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				AnimationSet animationSet = new AnimationSet(true);
				animationSet.addAnimation(new ScaleAnimation(1.1f, 1.0f, 1.1f,
						1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f));
				animationSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
				animationSet.setDuration(2000);
				animationSet.setInterpolator(new DecelerateInterpolator());
				animationSet.setFillAfter(false);
				// 实现心跳的View
				btn_go.startAnimation(animationSet);
			}
		});
		// 实现心跳的View
		btn_go.startAnimation(animationSet);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
