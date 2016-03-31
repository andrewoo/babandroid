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
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.LGCharacter;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;

/**
 * FlashCard操作页面
 * 
 * @author yh
 */
public class LessonFlashCardOpActivity extends BaseActivity {

	private String TAG = "==LessonFlashCardOpActivity==";
	public Context context;
	private Button btn_go;
	private Resources resources;
	private int width;
	private int height;
	View contentView;
	int characterCount = 0;
	int wordsCount = 0;
	int sentenceCount = 0;

	private TextView tv_no;
	private ImageView img_play;
	private TextView tv_content;

	private TextView tv_word;
	private TextView tv_translation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_flashcard_op, null);
		setContentView(contentView);
		context = this;
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 4;
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
				"FlashCard", View.GONE, View.VISIBLE,
				R.drawable.btn_selector_top_right);

		LayoutParams py = btn_go.getLayoutParams();
		py.width = width;
		py.height = width;
		// btn_go.setLayoutParams(py);
		// btn_go.setOnClickListener(onClickListener);

		img_play = (ImageView) contentView.findViewById(R.id.img_play);
		tv_no = (TextView) contentView.findViewById(R.id.tv_no);
		tv_content = (TextView) contentView.findViewById(R.id.tv_content);
		tv_word = (TextView) contentView.findViewById(R.id.tv_word);
		tv_translation = (TextView) contentView
				.findViewById(R.id.tv_translation);

		try {
			ArrayList<TbMyCharacter> tbMyCharacterList = (ArrayList<TbMyCharacter>) MyDao
					.getDaoMy(TbMyCharacter.class).queryForAll();

			if (tbMyCharacterList != null && tbMyCharacterList.size() != 0) {
				for (int i = 0; i < tbMyCharacterList.size(); i++) {
					TbMyCharacter tbMyCharacter = tbMyCharacterList.get(i);

					if (tbMyCharacter == null) {
						continue;
					}
					int charId = tbMyCharacter.getCharId();
					LGCharacter character = (LGCharacter) MyDao.getDao(
							LGCharacter.class).queryForId(charId);
					if (character != null) {
						String characterStr = character.getCharacter();
						tv_content.setText("" + characterStr);
					}
				}

			}

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
	}

	private void setText(int index) {

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

			case R.id.btn_go:

				startActivity(new Intent(LessonFlashCardOpActivity.this,
						LessonExerciseActivity.class));
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
