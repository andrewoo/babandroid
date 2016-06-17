package com.hw.chineseLearn.tabLearn;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 章节复习页面
 *
 * @author yh
 */
public class LearnReViewActivity extends BaseActivity {

	private String TAG = "==LearnReViewActivity==";
	public Context context;
	private Resources resources;
	private int width;
	private int height;
	View contentView;

	int characterCount = 0;
	int wordsCount = 0;
	int sentenceCount = 0;
	int chooseCount = 0;

	private Button btn_go;
	private TextView tv_character_count;
	private TextView tv_words_count;
	private TextView tv_sentence_count;

	private RelativeLayout lin_review_characters;
	private RelativeLayout lin_review_words;
	private RelativeLayout lin_review_sentence;

	private ImageView btn_go_circle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_learn_review, null);
		setContentView(contentView);
		context = this;
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 5;
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
				"Babble Review", View.GONE, View.GONE, 0);
		//btn_go_circle
		btn_go_circle = (ImageView) contentView.findViewById(R.id.btn_go_circle);

		LayoutParams layoutParams = btn_go_circle.getLayoutParams();
		layoutParams.height=width;
		layoutParams.width=width;
		btn_go_circle.setLayoutParams(layoutParams);

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
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					runOnUiThread(new Runnable() {
						public void run() {
//							playHeartbeatAnimation();
							heartAnaimation();
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

		lin_review_characters = (RelativeLayout) findViewById(R.id.lin_review_characters);
		lin_review_words = (RelativeLayout) findViewById(R.id.lin_review_words);
		lin_review_sentence = (RelativeLayout) findViewById(R.id.lin_review_sentence);

		lin_review_characters.setOnClickListener(onClickListener);
		lin_review_words.setOnClickListener(onClickListener);
		lin_review_sentence.setOnClickListener(onClickListener);

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
		view_title.setBackgroundColor(getResources().getColor(R.color.chinese_skill_blue));
		Button tv_title = (Button) view_title.findViewById(R.id.btn_title);
		tv_title.setTextColor(Color.WHITE);
		tv_title.setText(title);
		TextView tv_title_left = (TextView) view_title
				.findViewById(R.id.tv_title_left);
		tv_title_left.setVisibility(textLeft);

		ImageView iv_title_left = (ImageView) view_title
				.findViewById(R.id.iv_title_left);

		iv_title_left.setVisibility(imgLeft);
		iv_title_left.setOnClickListener(onClickListener);
		iv_title_left.setImageResource(R.drawable.closeicon);
		TextView tv_title_right = (TextView) view_title
				.findViewById(R.id.tv_title_right);
		tv_title_right.setVisibility(textRight);
		tv_title_right.setOnClickListener(onClickListener);

		ImageView iv_title_right = (ImageView) view_title
				.findViewById(R.id.iv_title_right);
		iv_title_right.setVisibility(View.INVISIBLE);
		iv_title_right.setImageResource(imgRightDrawable);

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回
				CustomApplication.app.finishActivity(LearnReViewActivity.this);
				break;

			case R.id.btn_go:

				Intent intent = new Intent(LearnReViewActivity.this,
						LessonFlashCardOpActivity.class);
				startActivity(intent);
				break;

			case R.id.lin_review_characters:

				if (characterCount > 0) {
					startActivity(new Intent(LearnReViewActivity.this,
							LessonReViewCharacterActivity.class));
				}

				break;

			case R.id.lin_review_words:
				if (wordsCount > 0) {
					startActivity(new Intent(LearnReViewActivity.this,
							LessonReViewWordActivity.class));
				}

				break;
			case R.id.lin_review_sentence:
				if (sentenceCount > 0) {
					startActivity(new Intent(LearnReViewActivity.this,
							LessonReViewSentenceActivity.class));
				}

				break;

			default:
				break;
			}
		}
	};

	private void heartAnaimation(){

		ScaleAnimation scaleAnimation=new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setInterpolator(new AccelerateInterpolator());
		scaleAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//缩放完后 圆形放大 继续缩放
				AnimationSet animationSet = new AnimationSet(true);
				ScaleAnimation scaleCicle=new ScaleAnimation(1.0f, 1.5f,1.0f,1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				AlphaAnimation alphaAnimation = new AlphaAnimation(0.8f, 0.0f);
				animationSet.setDuration(1000);
				animationSet.addAnimation(scaleCicle);
				animationSet.addAnimation(alphaAnimation);
				animationSet.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						btn_go_circle.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				btn_go_circle.setVisibility(View.VISIBLE);
				btn_go_circle.startAnimation(animationSet);
			}
		});
		btn_go.startAnimation(scaleAnimation);

	}

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

}
