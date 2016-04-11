package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
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
import com.util.tool.UiUtil;

/**
 * FlashCard操作页面
 * 
 * @author yh
 */
public class LessonFlashCardOpActivity extends BaseActivity {

	private String TAG = "==LessonFlashCardOpActivity==";
	public Context context;

	private LinearLayout lin_1;
	private Button btn_go;
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

	private ArrayList<LGModelFlashCard> datas = new ArrayList<LGModelFlashCard>();
	ArrayList<TbMyCharacter> tbMyCharacterList;
	ArrayList<TbMyWord> tbMyWordList;
	ArrayList<TbMySentence> tbMySentenceList;

	boolean isCharacterChecked = false;
	boolean isSentenceChecked = false;
	boolean isWordChecked = false;
	boolean isAutoPlay = true;
	int defaultNumber = 0;

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
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("defaultNumber")) {
				defaultNumber = bundle.getInt("defaultNumber", 0);
			}
		}
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {

		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"FlashCard", View.GONE, View.GONE, 0);
		lin_1 = (LinearLayout) contentView.findViewById(R.id.lin_1);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				screenWidth - UiUtil.dip2px(getApplicationContext(), 60),
				screenHeight * 7 / 10);
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

		initDatas();
		setText();
	}

	@SuppressWarnings("unchecked")
	private void initDatas() {
		String chooseCountStr = CustomApplication.app.preferencesUtil.getValue(
				"count", "0");
		chooseCount = Integer.parseInt(chooseCountStr);
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

					lGModelFlashCard.setId(id);
					lGModelFlashCard.setChinese(chinese);
					lGModelFlashCard.setPinyin(pinyin);
					lGModelFlashCard.setEnglish(english);
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

					lGModelFlashCard.setId(id);
					lGModelFlashCard.setChinese(chinese);
					lGModelFlashCard.setPinyin(pinyin);
					lGModelFlashCard.setEnglish(english);
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

					lGModelFlashCard.setId(id);
					lGModelFlashCard.setChinese(chinese);
					lGModelFlashCard.setPinyin(pinyin);
					lGModelFlashCard.setEnglish(english);
					datas.add(lGModelFlashCard);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	int id = -1;
	String chinese = "";
	String english = "";
	String pinyin = "";

	int index = 0;

	private void setText() {

		if (datas.size() != 0) {
			LGModelFlashCard lGModelFlashCard = datas.get(index);
			if (lGModelFlashCard != null) {

				id = lGModelFlashCard.getCharId();
				chinese = lGModelFlashCard.getChinese();
				english = lGModelFlashCard.getEnglish();
				pinyin = lGModelFlashCard.getPinyin();

				tv_no.setText("" + (index + 1) + "/" + defaultNumber);
				tv_translation.setText("" + english);
				tv_word.setText(chinese);
				tv_pinyin.setText(pinyin);
				lin_is_gorget.setVisibility(View.VISIBLE);
				index++;
			} else {
				Log.e(TAG, "lGModelFlashCard==null");
			}
		} else {
			Log.e(TAG, "datas.size() == 0");
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
						Log.d(TAG, "更新熟练度C:" + C);
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
						Log.d(TAG, "更新熟练度S:" + S);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (tbMyWord != null) {
					try {
						tbMyWord.setProficient(proficient);
						int W = MyDao.getDaoMy(TbMyWord.class).update(tbMyWord);
						Log.d(TAG, "更新熟练度W:" + W);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			if (index == defaultNumber) {
				// 做完了
				Intent intent = new Intent(LessonFlashCardOpActivity.this,
						LessonFlashCardResultActivity.class);
				startActivityForResult(intent, 1);
			} else {
				setText();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
