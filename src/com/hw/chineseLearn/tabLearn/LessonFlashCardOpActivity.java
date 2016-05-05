package com.hw.chineseLearn.tabLearn;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
import at.technikum.mti.fancycoverflow.FancyCoverFlow;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.FlashCardOpGalleryAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.LGCharacter;
import com.hw.chineseLearn.dao.bean.LGModelFlashCard;
import com.hw.chineseLearn.dao.bean.LGSentence;
import com.hw.chineseLearn.dao.bean.LGWord;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.util.tool.MediaPlayUtil;
import com.util.tool.UiUtil;
import com.util.weight.SlideSwitch;
import com.util.weight.SlideSwitch.SlideListener;

/**
 * FlashCard操作页面
 * 
 * @author yh
 */
public class LessonFlashCardOpActivity extends BaseActivity implements
		OnItemSelectedListener {

	private String TAG = "==LessonFlashCardOpActivity==";
	public Context context;

	private LinearLayout lin_1;
	private TextView tv_no;
	private ImageView img_play;
	private TextView tv_translation;
	private TextView tv_word;
	private TextView tv_pinyin;

	LinearLayout lin_is_gorget;
	TextView btn_remember;
	TextView btn_forget;

	LinearLayout lin_remember_level;
	TextView btn_remembered_perfectly;
	TextView btn_remembered;
	TextView btn_barely_remembered;

	LinearLayout lin_forgot_level;
	TextView btn_remembered_almost;
	TextView btn_forgot;
	TextView btn_dont_know;

	private Resources resources;
	private int screenWidth;
	private int screenHeight;
	View contentView;
	int characterCount = 0;
	int wordsCount = 0;
	int sentenceCount = 0;

	int chooseCount = 0;
	int defaultNumber = 0;

	private ArrayList<LGModelFlashCard> datas = new ArrayList<LGModelFlashCard>();
	ArrayList<TbMyCharacter> tbMyCharacterList;
	ArrayList<TbMyWord> tbMyWordList;
	ArrayList<TbMySentence> tbMySentenceList;

	boolean isCharacterChecked = false;
	boolean isSentenceChecked = false;
	boolean isWordChecked = false;
	boolean isAutoPlay = true;

	private FancyCoverFlow gallery;// CoverFlow
	private FlashCardOpGalleryAdapter adapter;

	int id = -1;
	String chinese = "";
	String english = "";
	String pinyin = "";
	int index = 0;
	private String voicePath;
	private static final String ASSETS_SOUNDS_PATH = "sounds/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_flashcard_op, null);
		setContentView(contentView);
		context = this;
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
		screenWidth = CustomApplication.app.displayMetrics.widthPixels;
		screenHeight = CustomApplication.app.displayMetrics.heightPixels;
		resources = context.getResources();
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {

		setTitle(View.GONE, View.VISIBLE,
				R.drawable.btn_selector_top_left_white, "FlashCard", View.GONE,
				View.VISIBLE, R.drawable.img_setting_white);
		lin_1 = (LinearLayout) contentView.findViewById(R.id.lin_1);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				screenWidth - UiUtil.dip2px(getApplicationContext(), 60),
				screenHeight * 5 / 10);
		lin_1.setLayoutParams(params);

		img_play = (ImageView) contentView.findViewById(R.id.img_play);
		tv_no = (TextView) contentView.findViewById(R.id.tv_no);
		tv_translation = (TextView) contentView
				.findViewById(R.id.tv_translation);
		tv_word = (TextView) contentView.findViewById(R.id.tv_word);
		tv_pinyin = (TextView) contentView.findViewById(R.id.tv_pinyin);

		lin_is_gorget = (LinearLayout) contentView
				.findViewById(R.id.lin_is_gorget);
		lin_is_gorget.setVisibility(View.VISIBLE);
		btn_remember = (TextView) contentView.findViewById(R.id.btn_remember);
		btn_forget = (TextView) contentView.findViewById(R.id.btn_forget);

		lin_remember_level = (LinearLayout) contentView
				.findViewById(R.id.lin_remember_level);
		lin_remember_level.setVisibility(View.GONE);

		int width = screenWidth / 3;
		LinearLayout.LayoutParams ly = new LinearLayout.LayoutParams(width,
				width);

		btn_remembered_perfectly = (TextView) contentView
				.findViewById(R.id.btn_remembered_perfectly);
		btn_remembered = (TextView) contentView
				.findViewById(R.id.btn_remembered);
		btn_barely_remembered = (TextView) contentView
				.findViewById(R.id.btn_barely_remembered);

		lin_forgot_level = (LinearLayout) contentView
				.findViewById(R.id.lin_forgot_level);
		lin_forgot_level.setVisibility(View.GONE);
		btn_remembered_almost = (TextView) contentView
				.findViewById(R.id.btn_remembered_almost);
		btn_forgot = (TextView) contentView.findViewById(R.id.btn_forgot);
		btn_dont_know = (TextView) contentView.findViewById(R.id.btn_dont_know);

		btn_remember.setOnClickListener(onClickListener);
		btn_forget.setOnClickListener(onClickListener);

		btn_remembered_perfectly.setOnClickListener(onClickListenerOp);
		btn_remembered.setOnClickListener(onClickListenerOp);
		btn_barely_remembered.setOnClickListener(onClickListenerOp);

		btn_remembered_almost.setOnClickListener(onClickListenerOp);
		btn_forgot.setOnClickListener(onClickListenerOp);
		btn_dont_know.setOnClickListener(onClickListenerOp);

		btn_remembered_perfectly.setLayoutParams(ly);
		btn_remembered.setLayoutParams(ly);
		btn_barely_remembered.setLayoutParams(ly);

		btn_remembered_almost.setLayoutParams(ly);
		btn_forgot.setLayoutParams(ly);
		btn_dont_know.setLayoutParams(ly);

		initDatas();
		setText();
	}

	@SuppressWarnings("unchecked")
	private void initDatas() {
		// String chooseCountStr =
		// CustomApplication.app.preferencesUtil.getValue(
		// "count", "0");
		// chooseCount = Integer.parseInt(chooseCountStr);
		isCharacterChecked = CustomApplication.app.preferencesUtil
				.getValuesBoolean("isCharacterChecked");
		isSentenceChecked = CustomApplication.app.preferencesUtil
				.getValuesBoolean("isSentenceChecked");
		isWordChecked = CustomApplication.app.preferencesUtil
				.getValuesBoolean("isWordChecked");
		isAutoPlay = CustomApplication.app.preferencesUtil
				.getValuesBoolean("isAutoPlay");
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

		if (isCharacterChecked) {
			for (int i = 0; i < tbMyCharacterList.size(); i++) {

				TbMyCharacter model = tbMyCharacterList.get(i);
				if (model == null) {
					continue;
				}
				LGModelFlashCard lGModelFlashCard = new LGModelFlashCard();
				int id = model.getCharId();
				lGModelFlashCard.setMyCharacter(model);
				lGModelFlashCard.setMySentence(null);
				lGModelFlashCard.setMyWord(null);

				try {
					@SuppressWarnings("unchecked")
					LGCharacter character = (LGCharacter) MyDao.getDao(
							LGCharacter.class).queryForId(id);

					if (character == null) {
						Log.e(TAG, "character==null");
						continue;
					}
					String chinese = character.getCharacter();
					String pinyin = character.getPinyin();
					String english = character.getTranslation();
					String dirCode = character.getDirCode();
					String voicePath = "c-" + id + "-" + dirCode + ".mp3";

					lGModelFlashCard.setId(id);
					lGModelFlashCard.setChinese(chinese);
					lGModelFlashCard.setPinyin(pinyin);
					lGModelFlashCard.setEnglish(english);
					lGModelFlashCard.setVoicePath(voicePath);

					datas.add(lGModelFlashCard);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		if (isSentenceChecked) {
			for (int i = 0; i < tbMySentenceList.size(); i++) {

				TbMySentence model = tbMySentenceList.get(i);
				if (model == null) {
					continue;
				}
				LGModelFlashCard lGModelFlashCard = new LGModelFlashCard();
				int id = model.getSentenceId();
				lGModelFlashCard.setMySentence(model);
				lGModelFlashCard.setMyCharacter(null);
				lGModelFlashCard.setMyWord(null);

				try {
					@SuppressWarnings("unchecked")
					LGSentence sentence = (LGSentence) MyDao.getDao(
							LGSentence.class).queryForId(id);

					if (sentence == null) {
						Log.e(TAG, "sentence==null");
						continue;
					}
					String chinese = sentence.getSentence();
					String pinyin = "";
					String english = sentence.getTranslations();
					String dirCode = sentence.getDirCode();
					String voicePath = "s-" + id + "-" + dirCode + ".mp3";

					lGModelFlashCard.setId(id);
					lGModelFlashCard.setChinese(chinese);
					lGModelFlashCard.setPinyin(pinyin);
					lGModelFlashCard.setEnglish(english);
					lGModelFlashCard.setVoicePath(voicePath);

					datas.add(lGModelFlashCard);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		if (isWordChecked) {
			for (int i = 0; i < tbMyWordList.size(); i++) {

				TbMyWord model = tbMyWordList.get(i);
				if (model == null) {
					continue;
				}
				LGModelFlashCard lGModelFlashCard = new LGModelFlashCard();
				int id = model.getWordId();
				lGModelFlashCard.setMyWord(model);
				lGModelFlashCard.setMySentence(null);
				lGModelFlashCard.setMyCharacter(null);
				try {
					@SuppressWarnings("unchecked")
					LGWord word = (LGWord) MyDao.getDao(LGWord.class)
							.queryForId(id);

					if (word == null) {
						Log.e(TAG, "sentence==null");
						continue;
					}
					String chinese = word.getWord();
					String pinyin = word.getPinyin();
					String english = word.getTranslations();
					String dirCode = word.getDirCode();
					String voicePath = "w-" + id + "-" + dirCode + ".mp3";

					lGModelFlashCard.setId(id);
					lGModelFlashCard.setChinese(chinese);
					lGModelFlashCard.setPinyin(pinyin);
					lGModelFlashCard.setEnglish(english);
					lGModelFlashCard.setVoicePath(voicePath);

					datas.add(lGModelFlashCard);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		chooseCount = 0;
		if (isCharacterChecked) {
			chooseCount = chooseCount + characterCount;
		}
		if (isSentenceChecked) {
			chooseCount = chooseCount + sentenceCount;
		}
		if (isWordChecked) {
			chooseCount = chooseCount + wordsCount;
		}
		defaultNumber = chooseCount;
		if (adapter == null) {
			adapter = new FlashCardOpGalleryAdapter(this, datas, datas.size());
		}
		adapter.notifyDataSetChanged();
		gallery = (FancyCoverFlow) findViewById(R.id.flash_gallery);
		gallery.setAdapter(adapter);
		gallery.setOnItemSelectedListener(this);
		gallery.setAnimationDuration(1500);
		// gallery.setSpacing(screenWidth / 10 * 1);
		gallery.setSelection(index);
		gallery.setFocusable(false);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				screenWidth, screenHeight * 5 / 10);
		gallery.setLayoutParams(param);

	}

	/**
	 * 播放asset里的声音文件
	 */
	public void play() {

		String filePath = DatabaseHelperMy.LESSON_SOUND_PATH + "/" + voicePath;
		Log.d(TAG, "filePath:" + filePath);
		if (isAutoPlay) {
			MediaPlayUtil.getInstance().play(filePath);
		}

	}

	private void setText() {

		if (defaultNumber != 0) {

			if (datas.size() != 0 && index < datas.size()) {
				LGModelFlashCard lGModelFlashCard = datas.get(index);
				if (lGModelFlashCard != null) {

					id = lGModelFlashCard.getCharId();
					chinese = lGModelFlashCard.getChinese();
					english = lGModelFlashCard.getEnglish();
					pinyin = lGModelFlashCard.getPinyin();
					voicePath = lGModelFlashCard.getVoicePath();
					Log.d(TAG, "voicePath:" + voicePath);
					tv_no.setText("" + (index + 1) + "/" + defaultNumber);
					tv_translation.setText("" + english);
					tv_word.setText(chinese);
					tv_pinyin.setText(pinyin);
					lin_is_gorget.setVisibility(View.VISIBLE);
					lin_remember_level.setVisibility(View.GONE);
					lin_forgot_level.setVisibility(View.GONE);
				} else {
					Log.e(TAG, "lGModelFlashCard==null");
				}
			} else {
				Log.e(TAG, "datas.size() == 0");
			}
		} else {
			tv_no.setText("");
			tv_translation.setText("");
			tv_word.setText("");
			tv_pinyin.setText("");
			lin_is_gorget.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);

		switch (resultCode) {
		case 0:// finish
			CustomApplication.app
					.finishActivity(LessonFlashCardOpActivity.this);
			break;
		case 1:// redo
			index = 0;
			setText();
			play();
			gallery.setSelection(index);// 选中当前页面
			break;

		default:
			break;
		}
	}

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
		view_title.setBackgroundColor(context.getResources().getColor(
				R.color.chinese_skill_red));
		Button tv_title = (Button) view_title.findViewById(R.id.btn_title);
		tv_title.setText(title);
		tv_title.setTextColor(context.getResources().getColor(R.color.white));
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

		ImageView iv_title_right = (ImageView) view_title
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
						.finishActivity(LessonFlashCardOpActivity.this);
				break;

			case R.id.iv_title_right://
				showPopupWindowMenu();
				break;

			case R.id.btn_remember:
				lin_is_gorget.setVisibility(View.GONE);
				lin_remember_level.setVisibility(View.VISIBLE);
				lin_forgot_level.setVisibility(View.GONE);
				break;

			case R.id.btn_forget:
				lin_is_gorget.setVisibility(View.GONE);
				lin_remember_level.setVisibility(View.GONE);
				lin_forgot_level.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	};

	OnClickListener onClickListenerOp = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			// remember
			case R.id.btn_remembered_perfectly:
				lin_remember_level.setVisibility(View.GONE);
				updateDb(1);
				break;
			case R.id.btn_remembered:
				lin_remember_level.setVisibility(View.GONE);
				updateDb(2);
				break;
			case R.id.btn_barely_remembered:
				lin_remember_level.setVisibility(View.GONE);
				updateDb(3);
				break;
			// forgot
			case R.id.btn_remembered_almost:
				lin_forgot_level.setVisibility(View.GONE);
				updateDb(4);
				break;
			case R.id.btn_forgot:
				lin_forgot_level.setVisibility(View.GONE);
				updateDb(5);
				break;
			case R.id.btn_dont_know:
				lin_forgot_level.setVisibility(View.GONE);
				updateDb(6);
				break;

			default:
				break;
			}

		}
	};

	/**
	 * 
	 * @param Proficient
	 *            熟练度
	 */
	@SuppressWarnings("unchecked")
	private void updateDb(int proficient) {
		if (index < datas.size()) {

			LGModelFlashCard lGModelFlashCard = datas.get(index);
			if (lGModelFlashCard != null) {

				TbMyCharacter tbMyCharacter = lGModelFlashCard.getMyCharacter();
				TbMySentence tbMySentence = lGModelFlashCard.getMySentence();
				TbMyWord tbMyWord = lGModelFlashCard.getMyWord();

				if (tbMyCharacter != null) {
					tbMyCharacter.setProficient(proficient);
					try {
						int C = MyDao.getDaoMy(TbMyCharacter.class).update(
								tbMyCharacter);
						// Log.d(TAG, "更新熟练度C:" + C);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (tbMySentence != null) {
					try {
						tbMySentence.setProficient(proficient);
						int S = MyDao.getDaoMy(TbMySentence.class).update(
								tbMySentence);
						// Log.d(TAG, "更新熟练度S:" + S);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (tbMyWord != null) {
					try {
						tbMyWord.setProficient(proficient);
						int W = MyDao.getDaoMy(TbMyWord.class).update(tbMyWord);
						// Log.d(TAG, "更新熟练度W:" + W);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			index++;
			Log.d(TAG, "index:" + index + "  defaultNumber:" + defaultNumber);
			if (index == defaultNumber) {
				// 做完了
				Intent intent = new Intent(LessonFlashCardOpActivity.this,
						LessonFlashCardResultActivity.class);
				startActivityForResult(intent, 1);
			} else {
				gallery.setSelection(index);// 选中当前页面
				setText();
				play();

			}

		}
	}

	SeekBar seekBar;
	TextView tv_default_number;
	TextView tv_chooseCount;
	View view;

	public void showPopupWindowMenu() {

		view = LayoutInflater.from(this).inflate(R.layout.layout_title_menu1,
				null);

		tv_default_number = (TextView) view
				.findViewById(R.id.tv_default_number);
		tv_default_number.setText("" + defaultNumber);

		tv_chooseCount = (TextView) view.findViewById(R.id.tv_max);
		tv_chooseCount.setText("" + chooseCount);

		int width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
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

		SlideSwitch ck_auto_play = (SlideSwitch) view
				.findViewById(R.id.ck_auto_play);
		ck_auto_play.setState(isAutoPlay);
		ck_auto_play.setSlideListener(new SlideListener() {
			@Override
			public void open() {
				// TODO Auto-generated method stub
				isAutoPlay = true;
				CustomApplication.app.preferencesUtil.setBooleanValue(
						"isAutoPlay", isAutoPlay);
			}

			@Override
			public void close() {
				// TODO Auto-generated method stub
				isAutoPlay = false;
				CustomApplication.app.preferencesUtil.setBooleanValue(
						"isAutoPlay", isAutoPlay);
			}
		});
		seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		seekBar.setProgress(defaultNumber);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() // 监听器
		{
			public void onProgressChanged(SeekBar arg0, int progress,
					boolean fromUser) {
				seekBar.setProgress(progress);
				tv_default_number.setText("" + progress);
				defaultNumber = progress;
				setText();
				adapter.setCount(defaultNumber);
				adapter.notifyDataSetChanged();
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
		if (ck_Character.isChecked()) {
			isCharacterChecked = true;

		} else {
			isCharacterChecked = false;
		}

		CheckBox ck_Word = (CheckBox) view.findViewById(R.id.ck_Word);
		ck_Word.setChecked(isWordChecked);
		ck_Word.setOnCheckedChangeListener(onCheckedChangeListener);
		if (ck_Word.isChecked()) {
			isWordChecked = true;
		} else {
			isWordChecked = false;
		}

		CheckBox ck_Sentence = (CheckBox) view.findViewById(R.id.ck_Sentence);
		ck_Sentence.setChecked(isSentenceChecked);
		ck_Sentence.setOnCheckedChangeListener(onCheckedChangeListener);
		if (ck_Sentence.isChecked()) {
			isSentenceChecked = true;
		} else {
			isSentenceChecked = false;

		}
		seekBar.setMax(chooseCount);
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
				R.drawable.bg_touming));

		// popupWindow.showAsDropDown(iv_title_right, 0, 0);
		popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub

				Log.d(TAG, "chooseCount:" + chooseCount);
				Log.d(TAG, "isCharacterChecked:" + isCharacterChecked);
				Log.d(TAG, "isSentenceChecked:" + isSentenceChecked);
				Log.d(TAG, "isWordChecked:" + isWordChecked);
				Log.d(TAG, "isAutoPlay:" + isAutoPlay);
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

				if (isChecked) {
					chooseCount = chooseCount + characterCount;
					isCharacterChecked = true;

				} else {
					chooseCount = chooseCount - characterCount;
					isCharacterChecked = false;
				}
				CustomApplication.app.preferencesUtil.setBooleanValue(
						"isCharacterChecked", isCharacterChecked);

				break;
			case R.id.ck_Word:
				if (isChecked) {
					chooseCount = chooseCount + wordsCount;
					isWordChecked = true;
				} else {
					chooseCount = chooseCount - wordsCount;
					isWordChecked = false;
				}
				CustomApplication.app.preferencesUtil.setBooleanValue(
						"isWordChecked", isWordChecked);
				break;
			case R.id.ck_Sentence:
				if (isChecked) {
					isSentenceChecked = true;
					chooseCount = chooseCount + sentenceCount;
				} else {

					isSentenceChecked = false;
					chooseCount = chooseCount - sentenceCount;
				}
				CustomApplication.app.preferencesUtil.setBooleanValue(
						"isSentenceChecked", isSentenceChecked);
				break;
			default:
				break;
			}
			initDatas();
			setText();

			tv_chooseCount.setText("" + chooseCount);
			seekBar.setMax(chooseCount);
			CustomApplication.app.preferencesUtil.setValue("count", ""
					+ chooseCount);
			Log.d(TAG, "chooseCount:" + chooseCount);
		}

	};

	@Override
	public void onItemSelected(AdapterView<?> arg0, View convertView,
			final int position, long arg3) {
		// TODO Auto-generated method stub

		index = position;
		adapter.setSelection(position);
		setText();
		play();
		if (convertView == null) {
			convertView = View.inflate(this, R.layout.layout_falsh_card_item,
					null);
		}
		ImageView img_play = (ImageView) convertView
				.findViewById(R.id.img_play);
		img_play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				play();
			}
		});
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MediaPlayUtil.getInstance().stop();
		MediaPlayUtil.getInstance().release();
	}

}
