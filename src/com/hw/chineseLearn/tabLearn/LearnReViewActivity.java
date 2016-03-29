package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;

/**
 * 章节复习页面
 * 
 * @author yh
 */
public class LearnReViewActivity extends BaseActivity implements
		OnItemSelectedListener {

	private String TAG = "==LearnReViewActivity==";
	public Context context;
	private ImageButton btn_play_characters;
	private ImageButton btn_play_words;
	private ImageButton btn_play_sentence;

	private TextView tv_character_count;
	private TextView tv_words_count;
	private TextView tv_sentence_count;

	private LinearLayout lin_review_characters;
	private LinearLayout lin_review_words;
	private LinearLayout lin_review_sentence;
	private LinearLayout lin_botton_view;
	private Resources resources;
	private int width;
	private int height;
	View contentView;

	int characterCount = 0;
	int wordsCount = 0;
	int sentenceCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_learn_review, null);
		setContentView(contentView);
		context = this;
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 5;
		resources = context.getResources();
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {

		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Review", View.GONE, View.VISIBLE,
				R.drawable.btn_bg_flashcard_without_point);

		btn_play_characters = (ImageButton) contentView
				.findViewById(R.id.btn_play_characters);
		btn_play_characters.setImageDrawable(resources
				.getDrawable(R.drawable.character_play));
		btn_play_words = (ImageButton) contentView
				.findViewById(R.id.btn_play_words);
		btn_play_characters.setImageDrawable(resources
				.getDrawable(R.drawable.word_play));

		btn_play_sentence = (ImageButton) contentView
				.findViewById(R.id.btn_play_sentence);
		btn_play_characters.setImageDrawable(resources
				.getDrawable(R.drawable.sentence_play));

		tv_character_count = (TextView) contentView
				.findViewById(R.id.tv_characters_count);
		tv_words_count = (TextView) contentView
				.findViewById(R.id.tv_words_count);
		tv_sentence_count = (TextView) contentView
				.findViewById(R.id.tv_sentence_count);

		tv_character_count.setText("0");
		tv_words_count.setText("0");
		tv_sentence_count.setText("0");

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
		}
		tv_character_count.setText("" + characterCount);
		tv_words_count.setText("" + wordsCount);
		tv_sentence_count.setText("" + sentenceCount);

		lin_review_characters = (LinearLayout) contentView
				.findViewById(R.id.lin_review_characters);
		lin_review_words = (LinearLayout) contentView
				.findViewById(R.id.lin_review_words);
		lin_review_sentence = (LinearLayout) contentView
				.findViewById(R.id.lin_review_sentence);

		btn_play_characters.setOnClickListener(onClickListener);
		btn_play_words.setOnClickListener(onClickListener);
		btn_play_sentence.setOnClickListener(onClickListener);

		lin_review_characters.setOnClickListener(onClickListener);
		lin_review_words.setOnClickListener(onClickListener);
		lin_review_sentence.setOnClickListener(onClickListener);

		lin_botton_view = (LinearLayout) contentView
				.findViewById(R.id.lin_botton_view);
		lin_botton_view.setVisibility(View.INVISIBLE);
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

	boolean isPlayingCharacters = false;
	boolean isPlayingWords = false;
	boolean isPlayingSentence = false;

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回
				CustomApplication.app.finishActivity(LearnReViewActivity.this);
				break;

			case R.id.iv_title_right://
				startActivity(new Intent(LearnReViewActivity.this,
						LessonFlashCardActivity.class));
				break;

			case R.id.btn_play_characters:

				if (isPlayingCharacters) {// 正在播放时的操作
					lin_botton_view.setVisibility(View.INVISIBLE);
					btn_play_characters.setImageDrawable(resources
							.getDrawable(R.drawable.character_play));
					isPlayingCharacters = false;
				} else {// 暂停状态
					lin_botton_view.setVisibility(View.VISIBLE);
					lin_botton_view.setBackgroundColor(resources
							.getColor(R.color.chinese_skill_red));
					btn_play_characters.setImageDrawable(resources
							.getDrawable(R.drawable.character_pause));
					isPlayingCharacters = true;

					if (isPlayingWords) {
						btn_play_words.setImageDrawable(resources
								.getDrawable(R.drawable.word_play));
						isPlayingWords = false;
					}
					if (isPlayingSentence) {
						btn_play_sentence.setImageDrawable(resources
								.getDrawable(R.drawable.sentence_play));
						isPlayingSentence = false;
					}
				}

				break;
			case R.id.btn_play_words:

				if (isPlayingWords) {// 正在播放时的操作
					lin_botton_view.setVisibility(View.INVISIBLE);
					btn_play_words.setImageDrawable(resources
							.getDrawable(R.drawable.word_play));
					isPlayingWords = false;
				} else {// 暂停状态
					lin_botton_view.setVisibility(View.VISIBLE);
					lin_botton_view.setBackgroundColor(resources
							.getColor(R.color.chinese_skill_green));
					btn_play_words.setImageDrawable(resources
							.getDrawable(R.drawable.word_pause));
					isPlayingWords = true;

					if (isPlayingCharacters) {
						btn_play_characters.setImageDrawable(resources
								.getDrawable(R.drawable.character_play));
						isPlayingCharacters = false;
					}
					if (isPlayingSentence) {
						btn_play_sentence.setImageDrawable(resources
								.getDrawable(R.drawable.sentence_play));
						isPlayingSentence = false;
					}

				}

				break;
			case R.id.btn_play_sentence:

				if (isPlayingSentence) {// 正在播放时的操作
					lin_botton_view.setVisibility(View.INVISIBLE);
					btn_play_sentence.setImageDrawable(resources
							.getDrawable(R.drawable.sentence_play));
					isPlayingSentence = false;
				} else {// 暂停状态
					lin_botton_view.setVisibility(View.VISIBLE);
					lin_botton_view.setBackgroundColor(resources
							.getColor(R.color.chinese_skill_yellow));
					btn_play_sentence.setImageDrawable(resources
							.getDrawable(R.drawable.sentence_pause));
					isPlayingSentence = true;

					if (isPlayingWords) {
						btn_play_words.setImageDrawable(resources
								.getDrawable(R.drawable.word_play));
						isPlayingWords = false;
					}
					if (isPlayingCharacters) {
						btn_play_characters.setImageDrawable(resources
								.getDrawable(R.drawable.character_play));
						isPlayingCharacters = false;
					}
				}

				break;

			case R.id.lin_review_characters:

				startActivity(new Intent(LearnReViewActivity.this,
						LessonReViewCharacterActivity.class));
				break;

			case R.id.lin_review_words:

				startActivity(new Intent(LearnReViewActivity.this,
						LessonReViewWordActivity.class));
				break;
			case R.id.lin_review_sentence:

				startActivity(new Intent(LearnReViewActivity.this,
						LessonReViewSentenceActivity.class));
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onItemSelected(AdapterView<?> arg0, View convertView,
			int position, long arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
