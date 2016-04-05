package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;
import com.util.tool.UiUtil;
import com.util.weight.RectView;
import com.util.weight.RoundProgressBar;

/**
 * flashCard操作结果页面
 * 
 * @author yh
 */
public class LessonFlashCardResultActivity extends BaseActivity {

	private String TAG = "==LessonFlashCardResultActivity==";
	public Context context;
	private Resources resources;
	private int width;
	private int height;
	View contentView;

	TextView btn_redo;

	TextView remember_prefectly, remembered, barely_remembered,
			almost_remembered, forgot, dont_know;

	int rememberPrefectlyCount, rememberedCount, barelyRememberedCount,
			almostRememberedCount, forgotCount, dontKnowCount;

	ArrayList<TbMyCharacter> tbMyCharacterList;
	ArrayList<TbMyWord> tbMyWordList;
	ArrayList<TbMySentence> tbMySentenceList;
	RectView pro_remember_prefectly;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_flashcard_op_result, null);

		setContentView(contentView);

		CustomApplication.app.addActivity(this);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 5;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 3;
		resources = context.getResources();
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		LayoutParams lp = new LayoutParams(width, height);

		// tv_lose_all = (ImageView) findViewById(R.id.tv_lose_all);
		// img_lose_all = (ImageView) findViewById(R.id.img_lose_all);
		// tv_lose_all.setLayoutParams(lp);
		// img_lose_all.setLayoutParams(lp);

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

		LayoutParams lp1 = new LayoutParams(UiUtil.dip2px(
				getApplicationContext(), 10), UiUtil.dip2px(
				getApplicationContext(), 50));
		pro_remember_prefectly = (RectView) contentView
				.findViewById(R.id.pro_remember_prefectly);
		pro_remember_prefectly.setLayoutParams(lp1);
		pro_remember_prefectly.setMax(100);
		pro_remember_prefectly.setProgress(50);
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// while (rememberPrefectlyCount <= 100) {
		// progress += progressAdd;
		// pro_remember_prefectly.setProgress(progress);
		// try {
		// Thread.sleep(50);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// }
		// }).start();

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
