package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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
import com.util.tool.UiUtil;

/**
 * flashCard操作结果页面
 * 
 * @author yh
 */
public class LessonFlashCardResultActivity extends BaseActivity {

	private String TAG = "==LessonFlashCardResultActivity==";
	public Context context;
	private Resources resources;
	private int screenWidth;
	private int screenHeight;
	View contentView;

	TextView btn_redo;

	TextView remember_prefectly, remembered, barely_remembered,
			almost_remembered, forgot, dont_know;

	ImageView iv_remember_prefectly, iv_remembered, iv_barely_remembered,
			iv_almost_remembered, iv_forgot, iv_dont_know;

	int rememberPrefectlyCount, rememberedCount, barelyRememberedCount,
			almostRememberedCount, forgotCount, dontKnowCount;

	ArrayList<TbMyCharacter> tbMyCharacterList;
	ArrayList<TbMyWord> tbMyWordList;
	ArrayList<TbMySentence> tbMySentenceList;
	ValueAnimator value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_flashcard_op_result, null);

		setContentView(contentView);

		CustomApplication.app.addActivity(this);
		screenWidth = CustomApplication.app.displayMetrics.widthPixels;
		screenHeight = CustomApplication.app.displayMetrics.heightPixels;
		resources = context.getResources();
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		LinearLayout lin_bottom = (LinearLayout) contentView
				.findViewById(R.id.lin_bottom);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, screenHeight / 2);
		lin_bottom.setLayoutParams(params);

		btn_redo = (TextView) contentView.findViewById(R.id.btn_redo);
		btn_redo.setOnClickListener(onClickListener);

		remember_prefectly = (TextView) contentView
				.findViewById(R.id.remember_prefectly);
		remembered = (TextView) contentView.findViewById(R.id.remembered);
		barely_remembered = (TextView) contentView
				.findViewById(R.id.barely_remembered);
		almost_remembered = (TextView) contentView
				.findViewById(R.id.almost_remembered);
		forgot = (TextView) contentView.findViewById(R.id.forgot);
		dont_know = (TextView) contentView.findViewById(R.id.dont_know);

		try {
			tbMyCharacterList = (ArrayList<TbMyCharacter>) MyDao.getDaoMy(
					TbMyCharacter.class).queryForAll();

			tbMyWordList = (ArrayList<TbMyWord>) MyDao.getDaoMy(TbMyWord.class)
					.queryForAll();

			tbMySentenceList = (ArrayList<TbMySentence>) MyDao.getDaoMy(
					TbMySentence.class).queryForAll();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < tbMyCharacterList.size(); i++) {
			TbMyCharacter model = tbMyCharacterList.get(i);
			if (model == null) {
				continue;
			}
			int proficient = model.getProficient();
			switch (proficient) {
			case 1:
				rememberPrefectlyCount++;
				break;
			case 2:
				rememberedCount++;
				break;
			case 3:
				barelyRememberedCount++;
				break;
			case 4:
				almostRememberedCount++;
				break;
			case 5:
				forgotCount++;
				break;
			case 6:
				dontKnowCount++;
				break;

			default:
				break;
			}
		}

		for (int i = 0; i < tbMyWordList.size(); i++) {
			TbMyWord model = tbMyWordList.get(i);
			if (model == null) {
				continue;
			}
			int proficient = model.getProficient();
			switch (proficient) {
			case 1:
				rememberPrefectlyCount++;
				break;
			case 2:
				rememberedCount++;
				break;
			case 3:
				barelyRememberedCount++;
				break;
			case 4:
				almostRememberedCount++;
				break;
			case 5:
				forgotCount++;
				break;
			case 6:
				dontKnowCount++;
				break;

			default:
				break;
			}
		}

		for (int i = 0; i < tbMySentenceList.size(); i++) {
			TbMySentence model = tbMySentenceList.get(i);
			if (model == null) {
				continue;
			}
			int proficient = model.getProficient();
			switch (proficient) {
			case 1:
				rememberPrefectlyCount++;
				break;
			case 2:
				rememberedCount++;
				break;
			case 3:
				barelyRememberedCount++;
				break;
			case 4:
				almostRememberedCount++;
				break;
			case 5:
				forgotCount++;
				break;
			case 6:
				dontKnowCount++;
				break;

			default:
				break;
			}
		}

		remember_prefectly.setText("" + rememberPrefectlyCount);
		remembered.setText("" + rememberedCount);
		barely_remembered.setText("" + barelyRememberedCount);
		almost_remembered.setText("" + almostRememberedCount);
		forgot.setText("" + forgotCount);
		dont_know.setText("" + dontKnowCount);

		iv_remember_prefectly = (ImageView) contentView
				.findViewById(R.id.iv_remember_prefectly);
		iv_remembered = (ImageView) contentView
				.findViewById(R.id.iv_remembered);
		iv_barely_remembered = (ImageView) contentView
				.findViewById(R.id.iv_barely_remembered);
		iv_almost_remembered = (ImageView) contentView
				.findViewById(R.id.iv_almost_remembered);
		iv_forgot = (ImageView) contentView.findViewById(R.id.iv_forgot);
		iv_dont_know = (ImageView) contentView.findViewById(R.id.iv_dont_know);

		setRectValueAnim(iv_remember_prefectly, rememberPrefectlyCount);
		setRectValueAnim(iv_remembered, rememberedCount);
		setRectValueAnim(iv_barely_remembered, barelyRememberedCount);

		setRectValueAnim(iv_almost_remembered, almostRememberedCount);
		setRectValueAnim(iv_forgot, forgotCount);
		setRectValueAnim(iv_dont_know, dontKnowCount);

	}

	/**
	 * @param imageView
	 * @param maxValue
	 */
	private void setRectValueAnim(final ImageView imageView, int maxValue) {
		value = ValueAnimator.ofInt(0, maxValue * 5);
		value.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {

				int height = (Integer) animation.getAnimatedValue();

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						UiUtil.dip2px(getApplicationContext(), 10), height);
				imageView.setLayoutParams(params);
			}
		});
		value.setDuration(1000);
		value.start();
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.btn_redo:
				setResult(1);
				CustomApplication.app
						.finishActivity(LessonFlashCardResultActivity.this);
				break;

			default:
				break;
			}
		}
	};

}
